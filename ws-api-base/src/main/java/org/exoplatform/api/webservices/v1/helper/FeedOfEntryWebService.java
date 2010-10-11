package org.exoplatform.api.webservices.v1.helper;

import org.exoplatform.api.webservices.v1.helper.model.Entry;
import org.exoplatform.api.webservices.v1.helper.model.Feed;
import org.exoplatform.container.ExoContainer;
import org.exoplatform.container.ExoContainerContext;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.io.*;


public abstract class FeedOfEntryWebService extends BaseFeedWebService {

    @GET
    public Response get(@PathParam("container") String container,
                        @PathParam("main_entry_id") String mainEntryId,
                        @QueryParam("format") @DefaultValue("atom") String format) {
        return get(container, mainEntryId, null, format);
    }
    
    @GET
    @Path("@{group}")
    public Response get(@PathParam("container") String container,
                        @PathParam("main_entry_id") String mainEntryId,
                        @PathParam("group") String group,
                        @QueryParam("format") @DefaultValue("atom") String format) {
        ExoContainer pc = ExoContainerContext.getContainerByName(container);
        ExoContainerContext.setCurrentContainer(pc);

        Feed feed = getFeedOfEntry(mainEntryId, group);
        return renderResponse(feed, format);
    }

    @PUT
    @Path("{id}")
    public Response update(@PathParam("container") String container,
                           @PathParam("main_entry_id") String mainEntryId,
                           @PathParam("id") String id,
                           byte[] data,
                           @QueryParam("format") @DefaultValue("atom") String format) throws IOException {
        ExoContainer pc = ExoContainerContext.getContainerByName(container);
        ExoContainerContext.setCurrentContainer(pc);

        Feed feed = getFeedOfEntry(mainEntryId, null);
        Entry entry = null;

        if (data.length > 0) {
            entry = feed.newEntry();
            //TODO Here it break if there is no content, need to find a way to avoid this
            fillEntry(entry, new ByteArrayInputStream(data), format);
        }
        
        entry = feed.updateEntry(id, entry);
        if (entry == null) {
            return Response.noContent().build();
        }
        return renderResponse(entry, format, 200);
    }

    @DELETE
    @Path("{id}")
    public Response delete(@PathParam("container") String container,
                           @PathParam("main_entry_id") String mainEntryId,
                           @PathParam("id") String id) throws IOException {
        ExoContainer pc = ExoContainerContext.getContainerByName(container);
        ExoContainerContext.setCurrentContainer(pc);

        getFeedOfEntry(mainEntryId, null).deleteEntry(id);

        return Response.noContent().build();
    }


    protected abstract Feed getFeedOfEntry(String mainEntryId, String group);
}
