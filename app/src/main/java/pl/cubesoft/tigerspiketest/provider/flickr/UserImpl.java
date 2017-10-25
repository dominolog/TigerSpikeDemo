package pl.cubesoft.tigerspiketest.provider.flickr;

import java.util.HashMap;
import java.util.Map;

import pl.cubesoft.tigerspiketest.data.model.User;
import pl.cubesoft.tigerspiketest.provider.flickr.model.UserProfileResult;
import pl.cubesoft.tigerspiketest.provider.flickr.model.UserResult;
import pl.cubesoft.tigerspiketest.utils.TextUtils;


/**
 * Created by CUBESOFT on 14.09.2017.
 */

public class UserImpl implements User {
    private UserProfileResult.Profile profile;
    private pl.cubesoft.tigerspiketest.provider.flickr.model.User user;

    public UserImpl(UserResult userResult) {
        this.user = userResult != null ? userResult.getPerson() : null;
    }



    public UserImpl(pl.cubesoft.tigerspiketest.provider.flickr.model.User user, UserProfileResult.Profile profile) {
        this.user = user;
        this.profile = profile;
    }



    @Override
    public String getId() {
        return user.getId();
    }

    @Override
    public String getName() {
        return user.getUsername();
    }

    @Override
    public String getDisplayName() {
        return user.getRealname();
    }

    @Override
    public String getDescription() {
        return user.getDescription();
    }

    @Override
    public String getPhotoUrl() {
        return ImageHelper.getProfileUrl(user);
    }

    @Override
    public String getCoverPhotoUrl() {
        return ImageHelper.getProfileUrl(user);
    }

    @Override
    public String getLocation() {
        return user.getLocation();
    }

    @Override
    public Boolean isFollowed() {
        return user.getContact();
    }

    @Override
    public void setIsFollowed(boolean followed) {
        user.setContact(followed);
    }

    @Override
    public String getRootGroupId() {
        return null;
    }

    @Override
    public Integer getNumFollowers() {
        return null;
    }

    @Override
    public Integer getNumFollowing() {
        return null;
    }

    @Override
    public Integer getNumPhotos() {
        return null;
    }

    @Override
    public Integer getNumViews() {
        return null;
    }

    @Override
    public Map<SocialNetwork, String> getContacts() {


        final Map<SocialNetwork, String> result = new HashMap<>();
        if ( profile != null ) {
            if ( !TextUtils.isEmpty(profile.getFacebook())) {
                result.put(SocialNetwork.FACEBOOK, profile.getFacebook());
            }

            if ( !TextUtils.isEmpty(profile.getInstagram())) {
                result.put(SocialNetwork.INSTAGRAM, profile.getInstagram());
            }

            if ( !TextUtils.isEmpty(profile.getPinterest())) {
                result.put(SocialNetwork.PINTEREST, profile.getPinterest());
            }

            if ( !TextUtils.isEmpty(profile.getTwitter())) {
                result.put(SocialNetwork.TWITTER, profile.getTwitter());
            }

            if ( !TextUtils.isEmpty(profile.getWebsite())) {
                result.put(SocialNetwork.WEBSITE, profile.getWebsite());
            }


        }

        return result;
    }


    public void setUser(pl.cubesoft.tigerspiketest.provider.flickr.model.User user) {
        this.user = user;
    }
}
