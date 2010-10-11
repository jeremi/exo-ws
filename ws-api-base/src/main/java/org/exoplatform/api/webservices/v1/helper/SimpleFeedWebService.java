package org.exoplatform.api.webservices.v1.helper;

import org.exoplatform.api.webservices.v1.helper.model.Entry;
import org.exoplatform.api.webservices.v1.helper.model.Feed;
import org.exoplatform.container.ExoContainer;
import org.exoplatform.container.ExoContainerContext;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;


public abstract class SimpleFeedWebService extends BaseFeedWebService {

    @GET
    @Path("@{group}")
    public Response get(@PathParam("container") String container,
                        @PathParam("group") String group,
                        @QueryParam("format") @DefaultValue("atom") String format) {
        ExoContainer pc = ExoContainerContext.getContainerByName(container);
        ExoContainerContext.setCurrentContainer(pc);

        Feed feed = getFeed(group);
        return renderResponse(feed, format);
    }

    @GET
    public Response get(@PathParam("container") String container,
                        @QueryParam("format") @DefaultValue("atom") String format) {
        return get(container, null, format);
    }

    @POST
    public Response add(@PathParam("container") String container,
                        InputStream data,
                        @QueryParam("format") @DefaultValue("atom") String format) throws IOException {
        ExoContainer pc = ExoContainerContext.getContainerByName(container);
        ExoContainerContext.setCurrentContainer(pc);

        Feed feed = getFeed();
        Entry entry = fillEntry(feed.newEntry(), data, format);
        feed.addEntry(entry);

        return renderResponse(entry, format, 201);
    }

    @PUT
    @Path("{id}")
    public Response update(@PathParam("container") String container,
                           @PathParam("id") String id,
                           byte[] data,
                           @QueryParam("format") @DefaultValue("atom") String format) throws IOException {
        ExoContainer pc = ExoContainerContext.getContainerByName(container);
        ExoContainerContext.setCurrentContainer(pc);

        Feed feed = getFeed();
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
                           @PathParam("id") String id) throws IOException {
        ExoContainer pc = ExoContainerContext.getContainerByName(container);
        ExoContainerContext.setCurrentContainer(pc);

        getFeed().deleteEntry(id);

        return Response.noContent().build();
    }

    @GET
    @Path("{id}")
    public Response getEntry(@PathParam("container") String container,
                             @PathParam("id") String id,
                             @QueryParam("format") @DefaultValue("atom") String format) {
        ExoContainer pc = ExoContainerContext.getContainerByName(container);
        ExoContainerContext.setCurrentContainer(pc);

        Feed feed = getFeed();
        Entry entry = feed.getEntryById(id);

        return renderResponse(entry, format);
    }

    protected Feed getFeed() {
        return getFeed(null);
    }

    protected abstract Feed getFeed(String group);
}
