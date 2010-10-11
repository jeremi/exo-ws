package org.exoplatform.api.webservices.v1.helper.model;

import java.util.List;


public interface Feed  extends Element {


    String getTitle();
    List<Link> getLinks();
    List<Entry> getEntries();
    String getId();


    Entry addEntry(Entry entry);
    Entry getEntryById(String id);
    Entry updateEntry(String id, Entry entry);
    Entry deleteEntry(String id);
    Entry newEntry();
}
