package org.exoplatform.api.webservices.v1.helper.model;

import java.net.URI;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: jeremi
 * Date: Sep 29, 2010
 * Time: 3:59:11 PM
 * To change this template use File | Settings | File Templates.
 */
public interface Person {
    String getId();
    String getName();
    String getEmail();
    URI getUri();
    List<Link> getLinks();

}
