package org.exoplatform.api.webservices.v1.social.spaces;

import org.exoplatform.api.webservices.v1.helper.SimpleFeedWebService;
import org.exoplatform.api.webservices.v1.helper.model.Feed;
import org.exoplatform.api.webservices.v1.social.spaces.model.SpaceFeed;

import javax.ws.rs.Path;


@Path("api/v1/social/{container}/spaces")
public class SpacesWebService extends SimpleFeedWebService {

    @Override
    protected Feed getFeed(String group) {
        return new SpaceFeed(group);
    }
}
