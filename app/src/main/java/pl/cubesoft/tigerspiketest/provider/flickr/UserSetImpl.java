package pl.cubesoft.tigerspiketest.provider.flickr;

import java.util.List;
import java.util.Map;

import pl.cubesoft.tigerspiketest.data.UserSetKey;
import pl.cubesoft.tigerspiketest.data.model.User;
import pl.cubesoft.tigerspiketest.data.model.UserSet;
import pl.cubesoft.tigerspiketest.provider.flickr.model.UsersResult;

/**
 * Created by CUBESOFT on 25.09.2017.
 */

class UserSetImpl implements UserSet{


    private final UserSetKey key;

    class ContactImpl implements User {

        private UsersResult.Contact user;

        @Override
        public String getId() {
            return user.getNsid();
        }

        @Override
        public String getName() {
            return user.getUsername();
        }

        @Override
        public String getDisplayName() {
            return user.getUsername();
        }

        @Override
        public String getDescription() {
            return null;
        }

        @Override
        public String getPhotoUrl() {
            return null;
        }

        @Override
        public String getCoverPhotoUrl() {
            return null;
        }

        @Override
        public String getLocation() {
            return null;
        }

        @Override
        public Boolean isFollowed() {
            return null;
        }

        @Override
        public void setIsFollowed(boolean follow) {

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
            return null;
        }

        public void setUser(UsersResult.Contact user) {
            this.user = user;
        }
    }

    private final UsersResult usersResult;
    ContactImpl contact;



    public UserSetImpl(UserSetKey key, UsersResult usersResult) {
        this.key = key;
        this.usersResult = usersResult;
        this.contact = new ContactImpl();
    }

    @Override
    public int getItemCount() {
        return usersResult.getContacts().getContact().size();
    }

    @Override
    public int getTotalItemCount() {
        return usersResult.getContacts().getTotal();
    }

    @Override
    public User getItem(int position) {
        contact.setUser(usersResult.getContacts().getContact().get(position));
        return contact;
    }

    @Override
    public UserSetKey getKey() {
        return key;
    }

    public List<UsersResult.Contact> getItems() {
        return usersResult.getContacts().getContact();
    }

    public void addItems(List<UsersResult.Contact> items) {
        usersResult.getContacts().getContact().addAll(items);
    }



}
