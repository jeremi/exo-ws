package org.exoplatform.api.webservices.v1.helper.formaters;

import org.apache.abdera.Abdera;
import org.apache.abdera.factory.Factory;
import org.apache.abdera.model.Document;
import org.apache.abdera.parser.Parser;
import org.apache.abdera.util.Constants;
import org.exoplatform.api.webservices.v1.helper.model.Element;
import org.exoplatform.api.webservices.v1.helper.model.Entry;
import org.exoplatform.api.webservices.v1.helper.model.Feed;
import org.exoplatform.api.webservices.v1.helper.model.Link;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Date;
import java.util.List;


public class AtomFormater implements Formater {
    private Abdera abdera;
    private Factory factory;
    private Parser parser;

    public AtomFormater() {
        this.abdera = new Abdera();
        this.factory = abdera.getFactory();
        this.parser = abdera.getParser();
    }

    public String format(Feed feed) {
        try {
            return convertToString(serialize(feed));
        } catch (IOException e) {
            //TODO: Log warning
            return formatError(e.getLocalizedMessage(), 500);
        }
    }

    private org.apache.abdera.model.Feed serialize(Feed feed) {
        org.apache.abdera.model.Feed atomFeed = factory.newFeed();

        atomFeed.setTitle(feed.getTitle());
        atomFeed.setId(feed.getId());
        atomFeed.setUpdated(new Date());

        serialize(atomFeed, feed.getLinks());

        for (Entry entry : feed.getEntries()) {
            atomFeed.addEntry(serialize(entry));
        }

        return atomFeed;
    }

    private org.apache.abdera.model.Entry serialize(Entry entry) {
        org.apache.abdera.model.Entry atomEntry = factory.newEntry();
        
        try {
            JAXBContext ctx = JAXBContext.newInstance(entry.getObjectClass());

            StringWriter sw = new StringWriter();
            ctx.createMarshaller().marshal(entry.getObject(), sw);
            System.out.println(sw.toString());
            atomEntry.setContent(sw.toString(), Constants.XML_MEDIA_TYPE);
            atomEntry.setSummary(entry.getSummary());
            //atomEntry.setContent(entry.getSummary(), "text/html");
            atomEntry.setId(entry.getId());
            atomEntry.setTitle(entry.getTitle());
            atomEntry.setPublished(entry.getPublished());
            atomEntry.setUpdated(entry.getUpdated());
            serialize(atomEntry, entry.getLinks());
            return atomEntry;
        } catch (JAXBException e) {
            //todo: Should re-throw the error
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return null;
    }

    private void serialize(org.apache.abdera.model.Entry entry, List<Link> links) {
        if (links == null || links.size() == 0) {
            return;
        }

        for (Link link : links) {
            //the last 0 is not very good...
            entry.addLink(link.getHref().toString(), link.getRel(), link.getMimeType(), link.getTitle(), link.getHrefLang(), 0);
        }
    }

    //We have to copy this code, the addLink is not in an interface :(
    private void serialize(org.apache.abdera.model.Feed feed, List<Link> links) {
        if (links == null) {
            return;
        }

        for (Link link : links) {
            //the last 0 is not very good...
            feed.addLink(link.getHref().toString(), link.getRel(), link.getMimeType(), link.getTitle(), link.getHrefLang(), 0);
        }
    }


    public String convertToString(org.apache.abdera.model.Base base) throws IOException {
        StringWriter sw = new StringWriter();

        base.writeTo(sw);
        return sw.toString();
    }

    public String format(Entry entry) {
        try {
            return convertToString(serialize(entry));
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

    public String formatError(String message, int code) {
        return null;
    }

    public void fill(Entry entry, InputStream data) throws IOException {

        Document<org.apache.abdera.model.Entry> doc = parser.parse(data);
        org.apache.abdera.model.Entry atomEntry = doc.getRoot();


        String xmlObject = atomEntry.getContent();
        try {
            JAXBContext ctx = JAXBContext.newInstance(entry.getObjectClass());

            entry.setObject(ctx.createUnmarshaller().unmarshal(new StringReader(xmlObject)));
        } catch (JAXBException e) {
            throw new IOException("Object formating error", e);
        }

    }
}
