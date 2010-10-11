package org.exoplatform.api.webservices.v1.helper.model;

import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: jeremi
 * Date: Sep 29, 2010
 * Time: 3:17:47 PM
 * To change this template use File | Settings | File Templates.
 */
public interface Entry extends Element {
    String getId();
    String getTitle();
    List<Link> getLinks();
    List<Person> getAuthors();
    Object getObject();
    void setObject(Object obj);
    String getSummary();
    Class  getObjectClass();
    Date getPublished();
    Date getUpdated();

}
