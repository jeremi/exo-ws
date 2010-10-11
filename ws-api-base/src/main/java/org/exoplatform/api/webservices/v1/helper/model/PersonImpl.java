package org.exoplatform.api.webservices.v1.helper.model;

import java.net.URI;
import java.util.List;


public class PersonImpl implements Person {
    String id;
    String name;
    String email;
    URI uri;
    List<Link> links;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public URI getUri() {
        return uri;
    }

    public void setUri(URI href) {
        this.uri = href;
    }

    public List<Link> getLinks() {
        return links;
    }

    public void getLinks(List<Link> links) {
        this.links = links;
    }


}
