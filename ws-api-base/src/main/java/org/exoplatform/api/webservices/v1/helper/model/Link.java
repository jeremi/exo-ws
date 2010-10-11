package org.exoplatform.api.webservices.v1.helper.model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import java.net.URI;

@XmlRootElement(name = "link")
public interface Link {
    String REL_ALTERNATE = "alternate";
    String REL_CURRENT = "current";
    String REL_ENCLOSURE = "enclosure";
    String REL_FIRST = "first";
    String REL_LAST = "last";
    String REL_NEXT = "next";
    String REL_PAYMENT = "payment";
    String REL_PREVIOUS = "previous";
    String REL_RELATED = "related";
    String REL_SELF = "self";
    String REL_VIA = "via";
    String REL_REPLIES = "replies";
    String REL_LICENSE = "license";
    String REL_EDIT = "edit";
    String REL_EDIT_MEDIA = "edit-media";


    @XmlAttribute(required = true)
    URI getHref();
    
    @XmlAttribute(required = true)
    String getRel();

    @XmlAttribute()
    String getMimeType();

    @XmlAttribute()
    String getHrefLang();

    @XmlAttribute()
    String getTitle();
}
