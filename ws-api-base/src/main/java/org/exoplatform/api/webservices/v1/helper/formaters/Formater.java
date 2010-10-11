package org.exoplatform.api.webservices.v1.helper.formaters;

import org.exoplatform.api.webservices.v1.helper.model.Element;
import org.exoplatform.api.webservices.v1.helper.model.Entry;
import org.exoplatform.api.webservices.v1.helper.model.Feed;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by IntelliJ IDEA.
 * User: jeremi
 * Date: Sep 29, 2010
 * Time: 4:42:49 PM
 * To change this template use File | Settings | File Templates.
 */
public interface Formater {


    String format(Feed feed);
    String format(Entry entry);
    String format(Element element);
    String formatError(String message, int code);

    void fill(Entry entry, InputStream data) throws IOException;
}
