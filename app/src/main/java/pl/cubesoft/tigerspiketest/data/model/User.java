package pl.cubesoft.tigerspiketest.data.model;

import java.util.Map;

/**
 * Created by CUBESOFT on 13.09.2017.
 */

public interface User {
    String getId();
    String getName();
    String getDisplayName();
    String getDescription();

    String getPhotoUrl();

    String getCoverPhotoUrl();

    String getLocation();

    Boolean isFollowed();

    void setIsFollowed(boolean follow);

    String getRootGroupId();


    Integer getNumFollowers();
    Integer getNumFollowing();
    Integer getNumPhotos();
    Integer getNumViews();

    enum SocialNetwork {
        FACEBOOK,
        TWITTER,
        INSTAGRAM,
        PINTEREST,
        MAIL,
        LINKEDIN,
        WEBSITE,
        FLICKR
    }
    Map<SocialNetwork, String> getContacts();
}
