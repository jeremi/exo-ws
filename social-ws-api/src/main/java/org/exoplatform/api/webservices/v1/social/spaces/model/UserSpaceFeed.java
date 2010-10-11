package org.exoplatform.api.webservices.v1.social.spaces.model;

import org.exoplatform.api.webservices.v1.helper.model.Entry;
import org.exoplatform.api.webservices.v1.helper.model.Feed;
import org.exoplatform.api.webservices.v1.helper.model.Link;
import org.exoplatform.container.ExoContainer;
import org.exoplatform.container.ExoContainerContext;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.exoplatform.services.security.ConversationState;
import org.exoplatform.social.core.space.SpaceException;
import org.exoplatform.social.core.space.model.Space;
import org.exoplatform.social.core.space.spi.SpaceService;

import javax.ws.rs.WebApplicationException;
import java.util.ArrayList;
import java.util.List;


public class UserSpaceFeed  implements Feed {
    private static final Log log = ExoLogger.getLogger("org.exoplatform.api.webservices.v1.social.spaces.model.UserSpaceFeed");
    private String group;
    private String userId;


    public UserSpaceFeed(String userId, String group) {
        this.userId = userId;
        this.group = group;
    }


    public String getTitle() {
        return "Space Feed for " + this.userId;
    }

    public List<Link> getLinks() {
        return null;
    }

    public List<Entry> getEntries() {
        //we want only the user to see his own space
        throwErrorIfNotCurrentUserFeed();

        SpaceService service = getComponentInstanceOfType(SpaceService.class);
        List<Entry> entries = new ArrayList<Entry>();

        try {
            List<Space> spaces;
            if ("pending".equals(this.group)) {
                spaces = service.getPendingSpaces(userId);
            } else if ("invitation".equals(this.group)) {
                spaces = service.getInvitedSpaces(userId);
            } else {
                spaces = service.getSpaces(userId);
            }


            for (Space space : spaces) {
                entries.add(new SpaceEntry(space));
            }
            return entries;
        } catch (SpaceException e) {
            log.error(e);
            throw new WebApplicationException(e, 500);
        }
    }

    public String getId() {
        String id = "user-space-feed:" + this.userId;
        if (this.group != null) {
            id += ":" + this.group;
        }
        return id;
    }

    public Entry addEntry(Entry entry) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Entry getEntryById(String id) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Entry updateEntry(String spaceId, Entry entry) {
        //only a user can change his own space list
        throwErrorIfNotCurrentUserFeed();

        SpaceService service = getComponentInstanceOfType(SpaceService.class);

        try {
            Space space = service.getSpaceById(spaceId);

            if (service.isMember(space, this.userId) ||
                service.isPending(space, this.userId)) {
                return null;
            }

            if (service.isInvited(space, this.userId)) {
                service.acceptInvitation(space, this.userId);
                return null;
            }

            if (Space.OPEN.equals(space.getRegistration())) {
                service.addMember(space, this.userId);
                return null;
            }

            if (Space.VALIDATION.equals(space.getRegistration())) {
                service.requestJoin(space, this.userId);
                return null;
            }

            //Otherwise it's probably something not allowed such as trying to join a closed space
            throw new WebApplicationException(403);

        } catch (SpaceException e) {
            log.error(e);
            throw new WebApplicationException(e, 500);
        }
    }

    public Entry deleteEntry(String spaceId) {
        //only a user can change his own space list
        throwErrorIfNotCurrentUserFeed();

        SpaceService service = getComponentInstanceOfType(SpaceService.class);

        try {
            if (service.isMember(spaceId, this.userId)) {
                service.removeMember(spaceId, userId);
            } else if (service.isInvited(spaceId, this.userId)) {
                service.denyInvitation(spaceId, userId);
            } else if (service.isPending(spaceId, this.userId)) {
                service.declineRequest(spaceId, userId);
            } else {
                //nothing to be done...
                throw new WebApplicationException(404);
            }
            //everything went well
            return null;
        } catch (SpaceException e) {
            log.error(e);
            throw new WebApplicationException(e, 500);
        }
    }

    public Entry newEntry() {
        return new SpaceEntry();
    }

    private void throwErrorIfNotCurrentUserFeed() {
        //later we probably want to allow admin to access this
        if (!this.userId.equals(getCurrentUser())) {
            throw new WebApplicationException(403);
        }
    }

    private <T> T getComponentInstanceOfType(Class<T> klass) {
        ExoContainer container = ExoContainerContext.getCurrentContainer();
        return (T)container.getComponentInstanceOfType(klass);
    }

    private String getCurrentUser() {
        return ConversationState.getCurrent().getIdentity().getUserId();
    }
}
