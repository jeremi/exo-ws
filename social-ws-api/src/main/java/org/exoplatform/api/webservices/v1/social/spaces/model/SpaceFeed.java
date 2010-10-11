package org.exoplatform.api.webservices.v1.social.spaces.model;

import org.exoplatform.api.webservices.v1.helper.model.Entry;
import org.exoplatform.api.webservices.v1.helper.model.Feed;
import org.exoplatform.api.webservices.v1.helper.model.Link;
import org.exoplatform.container.ExoContainer;
import org.exoplatform.container.ExoContainerContext;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.exoplatform.services.security.ConversationState;
import org.exoplatform.social.core.space.model.Space;
import org.exoplatform.social.core.space.spi.SpaceService;
import org.exoplatform.social.core.space.SpaceException;

import javax.ws.rs.WebApplicationException;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;


@XmlRootElement(name = "spacefeed")
public class SpaceFeed implements Feed {
    private static final Log log = ExoLogger.getLogger("org.exoplatform.api.webservices.v1.social.spaces.model.SpaceFeed");


    public SpaceFeed(String group) {
        //We don't have group, we throw a 404 error
        if (group != null) {
            throw new WebApplicationException(404);
        }
    }

    public String getTitle() {
        return "Space Feed";
    }

    public List<Link> getLinks() {
        return null;
    }

    public List<Entry> getEntries() {
        SpaceService service = getComponentInstanceOfType(SpaceService.class);
        List<Entry> entries = new ArrayList<Entry>();

        try {
            List<Space> spaces = service.getAllSpaces();

            for (Space space : spaces) {
                //A user cannot see hidden spaces
                if (!space.getVisibility().equals(Space.HIDDEN)) {
                    entries.add(new SpaceEntry(space));
                }
            }
            return entries;
        } catch (SpaceException e) {
            log.error(e);
            throw new WebApplicationException(e, 500);
        }
    }

    public String getId() {
        return "space-feed";
    }

    public Entry addEntry(Entry entry) {
        SpaceService service = getComponentInstanceOfType(SpaceService.class);
        String userId = getCurrentUser();

        Space space = ((ObjectSpace) entry.getObject()).getSpace();
        try {
            space = service.createSpace(space, userId);
            service.initApps(space);
            return new SpaceEntry(space);
        } catch (SpaceException e) {
            log.error(e);
            throw new WebApplicationException(500);
        }
    }

    public Entry getEntryById(String id) {
        SpaceService service = getComponentInstanceOfType(SpaceService.class);
        String userId = getCurrentUser();

        try {
            Space space = service.getSpaceById(id);

            throw404IfNull(space);

            if (space.getVisibility().equals(Space.HIDDEN) && !service.isMember(space, userId)) {
                //We do like if he does not exist
                throw new WebApplicationException(404);
            }

            return new SpaceEntry(space);
        } catch (SpaceException e) {
            log.error(e);
            throw new WebApplicationException(e, 500);
        }
    }

    public Entry updateEntry(String id, Entry entry) {
        SpaceService service = getComponentInstanceOfType(SpaceService.class);
        String userId = getCurrentUser();

        try {
            Space space = service.getSpaceById(id);

            throw404IfNull(space);

            throwErrorIfNotLeader(userId, space);

            ((ObjectSpace) entry.getObject()).copyTo(space);

            service.saveSpace(space, false);

            return new SpaceEntry(space);
        } catch (SpaceException e) {
            log.error(e);
            throw new WebApplicationException(e, 500);
        }
    }

    public Entry deleteEntry(String id) {
        SpaceService service = getComponentInstanceOfType(SpaceService.class);
        String userId = getCurrentUser();

        try {
            Space space = service.getSpaceById(id);

            throw404IfNull(space);

            throwErrorIfNotLeader(userId, space);

            service.deleteSpace(space);
            return new SpaceEntry(space);
        } catch (SpaceException e) {
            log.error(e);
            throw new WebApplicationException(e, 500);
        }
    }

    public Entry newEntry() {
        return new SpaceEntry(); 
    }


    private <T> T getComponentInstanceOfType(Class<T> klass) {
        ExoContainer container = ExoContainerContext.getCurrentContainer();
        return (T)container.getComponentInstanceOfType(klass);
    }

    private void throwErrorIfNotLeader(String userId, Space space) throws SpaceException {
        SpaceService service = getComponentInstanceOfType(SpaceService.class);

        //Only leaders can edit the space
        if (!service.isLeader(space, userId)) {
            if (space.getVisibility().equals(Space.HIDDEN) && !service.isMember(space, userId)) {
                //We do like if he does not exist
                throw new WebApplicationException(404);
            }
            throw new WebApplicationException(403);
        }

    }

    private void throw404IfNull(Object o) {
        if (o == null) {
            throw new WebApplicationException(404);
        }
    }

    private String getCurrentUser() {
        return ConversationState.getCurrent().getIdentity().getUserId();
    }
}
