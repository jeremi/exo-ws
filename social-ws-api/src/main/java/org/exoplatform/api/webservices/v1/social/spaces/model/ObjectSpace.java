package org.exoplatform.api.webservices.v1.social.spaces.model;

import org.exoplatform.social.core.space.model.Space;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name = "space")
public class ObjectSpace {
    private Space space;

    public ObjectSpace(Space space) {
        this.space = space;
    }

    public ObjectSpace() {
        this.space = new Space();    
    }

    @XmlElement
    public String getId() {
        return this.space.getId();
    }

    public void setId(String value) {
        this.space.setId(value);
    }

    @XmlElement
    public String getName() {
        return this.space.getName();
    }

    public void setName(String value) {
        this.space.setName(value);
    }

    @XmlElement
    public String getRegistration() {
        return this.space.getRegistration();
    }

    public void setRegistration(String value) {
        this.space.setRegistration(value);
    }

    @XmlElement
    public String getDescription() {
        return this.space.getDescription();
    }

    public void setDescription(String value) {
        this.space.setDescription(value);
    }

    @XmlElement
    public String getType() {
        return this.space.getType();
    }

    public void setType(String value) {
        this.space.setType(value);
    }

    @XmlElement
    public String getVisibility() {
        return this.space.getVisibility();
    }

    public void setVisibility(String value) {
        this.space.setVisibility(value);
    }

    @XmlElement
    public String getPriority() {
        return this.space.getPriority();
    }

    public void setPriority(String value) {
        this.space.setPriority(value);
    }

    protected String getUrl() {
        return this.space.getUrl();
    }

    protected Space getSpace() {
        return this.space;
    }

    public void copyTo(Space destSpace) {
        destSpace.setName(this.space.getName());
        destSpace.setRegistration(this.space.getRegistration());
        destSpace.setDescription(this.space.getDescription());
        destSpace.setType(this.space.getType());
        destSpace.setVisibility(this.space.getVisibility());
        destSpace.setPriority(this.space.getPriority());
    }
}
