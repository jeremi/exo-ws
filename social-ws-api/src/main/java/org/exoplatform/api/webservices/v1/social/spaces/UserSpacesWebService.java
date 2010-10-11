package org.exoplatform.api.webservices.v1.social.spaces;

import org.exoplatform.api.webservices.v1.helper.FeedOfEntryWebService;
import org.exoplatform.api.webservices.v1.helper.model.Feed;
import org.exoplatform.api.webservices.v1.social.spaces.model.UserSpaceFeed;

import javax.ws.rs.Path;

@Path("api/v1/social/{container}/people/{main_entry_id}/spaces")
public class UserSpacesWebService extends FeedOfEntryWebService {

    @Override
    protected Feed getFeedOfEntry(String userId, String group) {
        return new UserSpaceFeed(userId, group);
    }
}
