package org.exoplatform.api.webservices.v1.helper.formaters;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.AnnotationIntrospector;
import org.codehaus.jackson.map.MappingJsonFactory;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.ObjectNode;
import org.codehaus.jackson.node.POJONode;
import org.codehaus.jackson.xc.JaxbAnnotationIntrospector;
import org.codehaus.jackson.map.ObjectMapper;
import org.exoplatform.api.webservices.v1.helper.model.Element;
import org.exoplatform.api.webservices.v1.helper.model.Entry;
import org.exoplatform.api.webservices.v1.helper.model.Feed;
import org.exoplatform.api.webservices.v1.helper.model.Link;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.List;


public class JsonFormater implements Formater {
    ObjectMapper mapper;

    public JsonFormater() {
        this.mapper = new ObjectMapper();
        AnnotationIntrospector introspector = new JaxbAnnotationIntrospector();
        // make deserializer use JAXB annotations (only)
        this.mapper.getDeserializationConfig().setAnnotationIntrospector(introspector);
        // make serializer use JAXB annotations (only)
        this.mapper.getSerializationConfig().setAnnotationIntrospector(introspector);
        this.mapper.getSerializationConfig().set(SerializationConfig.Feature.INDENT_OUTPUT, true);
    }

    public String format(Feed feed){
        try {
            return convertDataToString(serialize(feed));
        } catch (IOException e) {
            //TODO: Log warning
            return formatError(e.getLocalizedMessage(), 500);
        }
    }

    public String formatError(String message, int code) {
        ObjectNode rootNode = this.mapper.getNodeFactory().objectNode();


        ObjectNode errorNode = this.mapper.getNodeFactory().objectNode();
        rootNode.put("message", message);
        rootNode.put("code", code);

        rootNode.put("error", errorNode);
        try {
            return convertToString(rootNode);
        } catch (IOException e) {
            //we can't do anything if the error handler create an error... so we let it go
            //TODO: Log error
            e.printStackTrace();
        }
        return null;
    }

    /*
     *
     * exemple of JSON :
     *
     * {
        	data: {
                "id": "596529260",
                "title": "RESTful Web Services",
                "links": {
                    "blog": {
                        "mimeType": null,
                        "title": null,
                        "href": "http://www.jeremi.info",
                        "rel": "blog",
                        "hrefLang": "en"
                    },
                    "company": {
                        "mimeType": null,
                        "title": null,
                        "href": "http://fr.wemakeproject.com",
                        "rel": "company",
                        "hrefLang": "fr"
                    }
                },
                "object": {
                    "title": "RESTful Web Services",
                    "author": "Leonard Richardson",
                    "isbn": "596529260"
                }
            }
        }
     *
     */
    public void fill(Entry entry, InputStream data) throws IOException {
        ObjectNode rootNode = this.mapper.readValue(data, ObjectNode.class);
        JsonNode objectNode = rootNode.get("data").get("object");
        entry.setObject(this.mapper.readValue(objectNode, entry.getObjectClass()));
    }

    private ObjectNode serialize(Feed feed) {
        ObjectNode rootNode = this.mapper.getNodeFactory().objectNode();
        rootNode.put("title", feed.getTitle());
        rootNode.put("links", getLinks(feed.getLinks()));

        ArrayNode entries = this.mapper.getNodeFactory().arrayNode();
        for (Entry entry : feed.getEntries()) {
            entries.add(serialize(entry));
        }
        rootNode.put("items", entries);
        return rootNode;
    }


    private ObjectNode getLinks(List<Link> links) {
        if (links == null) {
            return null;
        }
        
        ObjectNode linksNode = this.mapper.getNodeFactory().objectNode();
        for (Link link : links) {
            POJONode linkNode = this.mapper.getNodeFactory().POJONode(link);

            linksNode.put(link.getRel(), linkNode);
        }
        return linksNode;
    }

    private String convertDataToString(ObjectNode dataNode) throws IOException {
        ObjectNode rootNode = this.mapper.getNodeFactory().objectNode();
        rootNode.put("data", dataNode);
        return convertToString(rootNode);
    }

    private String convertToString(ObjectNode rootNode) throws IOException {
        StringWriter sw = new StringWriter();
        MappingJsonFactory jsonFactory = new MappingJsonFactory();
        JsonGenerator jsonGenerator = jsonFactory.createJsonGenerator(sw);

        mapper.writeValue(jsonGenerator, rootNode);

        sw.close();
        return sw.toString();
    }



    public String format(Entry entry) {
        try {
            return convertDataToString(serialize(entry));
        } catch (IOException e) {
            //TODO: Log warning
            return formatError(e.getLocalizedMessage(), 500);
        }
    }

    public String format(Element element) {
        if (element instanceof Entry) {
            return format((Entry) element);
        } else if (element instanceof Feed) {
            return format((Feed) element);
        }
        return null;
    }

    private ObjectNode serialize(Entry entry) {
        ObjectNode rootNode = this.mapper.getNodeFactory().objectNode();
        rootNode.put("id", entry.getId());
        rootNode.put("title", entry.getTitle());
        rootNode.put("links", getLinks(entry.getLinks()));

        //This is not very web, it should instead be a url
        
        rootNode.put("type", entry.getObject().getClass().getName());
        rootNode.put("object", this.mapper.getNodeFactory().POJONode(entry.getObject()));


        return rootNode;
    }
}
