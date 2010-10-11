package org.exoplatform.api.webservices.v1.helper.model;

import java.net.URI;


public class LinkImpl implements Link {
    private URI href;
    private String rel;
    private String mimeType;
    private String hrefLang;
    private String title;

    public URI getHref() {
        return href;
    }

    public void setHref(URI href) {
        this.href = href;
    }

    public String getRel() {
        return rel;
    }

    public void setRel(String rel) {
        this.rel = rel;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getHrefLang() {
        return hrefLang;
    }

    public void setHrefLang(String hrefLang) {
        this.hrefLang = hrefLang;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
