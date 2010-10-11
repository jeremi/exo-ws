package org.exoplatform.api.webservices.v1.helper;

import org.exoplatform.api.webservices.v1.helper.formaters.AtomFormater;
import org.exoplatform.api.webservices.v1.helper.formaters.JsonFormater;
import org.exoplatform.api.webservices.v1.helper.model.Element;
import org.exoplatform.api.webservices.v1.helper.model.Entry;
import org.exoplatform.api.webservices.v1.helper.model.Feed;
import org.exoplatform.services.rest.resource.ResourceContainer;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;


public abstract class BaseFeedWebService implements ResourceContainer {
    protected JsonFormater jsonFormater = new JsonFormater();
    protected AtomFormater atomFormater = new AtomFormater();

    
    protected Entry fillEntry(Entry entry, InputStream data, String format) throws IOException {
        if ("json".equals(format)) {
            jsonFormater.fill(entry, data);
        } else {
            atomFormater.fill(entry, data);
        }
        return entry;
    }

    protected Response renderResponse(Element element, String format, int code) {
        //CSV would be nice as well
        if ("json".equals(format)) {
            String resp = jsonFormater.format(element);
            //return text/plain instead of application/json because of a bug in WS https://jira.jboss.org/browse/EXOJCR-992
            return Response.ok(resp).status(code).type(MediaType.TEXT_PLAIN).build();
        } else {
            return Response.ok(atomFormater.format(element)).status(code).type(MediaType.APPLICATION_ATOM_XML).build();
        }
    }

    protected Response renderResponse(Element element, String format) {
        return renderResponse(element, format, 200);
    }
}
