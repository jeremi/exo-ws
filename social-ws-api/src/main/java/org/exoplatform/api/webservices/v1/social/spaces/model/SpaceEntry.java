package org.exoplatform.api.webservices.v1.social.spaces.model;

import org.exoplatform.api.webservices.v1.helper.model.Entry;
import org.exoplatform.api.webservices.v1.helper.model.Link;
import org.exoplatform.api.webservices.v1.helper.model.LinkImpl;
import org.exoplatform.api.webservices.v1.helper.model.Person;

import org.exoplatform.container.ExoContainer;
import org.exoplatform.container.ExoContainerContext;
import org.exoplatform.social.core.space.model.Space;

import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class SpaceEntry implements Entry {
    private ObjectSpace objectSpace;

    SpaceEntry(Space space) {
        this.objectSpace = new ObjectSpace(space);
    }

    SpaceEntry(ObjectSpace objectSpace) {
        this.objectSpace = objectSpace;       
    }

    SpaceEntry() {
        this.objectSpace = new ObjectSpace();
    }

    public String getId() {
        //TODO Should prefix to not conflict with other entry of other type???
        return this.objectSpace.getId();
    }

    public String getTitle() {
        return this.objectSpace.getName();
    }

    public List<Link> getLinks() {
        LinkImpl alternateLink = new LinkImpl();
        alternateLink.setRel(Link.REL_ALTERNATE);
        alternateLink.setMimeType("text/html");
        
        ExoContainer container = ExoContainerContext.getCurrentContainer();
        URI spaceURL = UriBuilder.fromPath("/{portalName}/private/classic/{spaceURL}")
                           .build(container.getContext().getName(), this.objectSpace.getUrl());
        alternateLink.setHref(spaceURL);

        LinkImpl selfLink = new LinkImpl();
        selfLink.setRel(Link.REL_SELF);
        selfLink.setMimeType("text/html");

        URI selfSpaceURL = UriBuilder.fromPath("/rest/private/api/v1/social/{container}/spaces/{spaceID}")
                           .build(container.getContext().getName(), this.objectSpace.getId());
        selfLink.setHref(selfSpaceURL);

        List<Link> links = new ArrayList<Link>();
        links.add(alternateLink);
        links.add(selfLink);
        return links;
    }

    public List<Person> getAuthors() {
        return null;
    }

    public Object getObject() {
        return this.objectSpace;
    }

    public void setObject(Object obj) {
        this.objectSpace = (ObjectSpace) obj;
    }

    public String getSummary() {
        return this.objectSpace.getDescription();
    }

    public Class getObjectClass() {
        return ObjectSpace.class;
    }

    public Date getPublished() {
        return null;
    }

    public Date getUpdated() {
        return null;
    }
}
