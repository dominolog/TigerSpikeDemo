package pl.cubesoft.tigerspiketest.data;



import pl.cubesoft.tigerspiketest.data.model.Photo;
import pl.cubesoft.tigerspiketest.data.model.User;
import pl.cubesoft.tigerspiketest.data.model.UserSet;
import rx.Observable;

/**
 * Created by CUBESOFT on 13.09.2017.
 */

public interface UsersProvider {

    Observable<User> getUser(boolean force, String userId, String username, String email);
    Observable<User> getMyUser(boolean force);

    Observable<User> getPhotoOwner(boolean force, Photo photo);

    Observable<User> followUser(String userId, boolean follow);

    public enum UserSetType {
        FRIENDS,
        FOLLOWERS,
        SEARCH,
    }

    Observable<UserSet> getUserSet(boolean force, UserSetKey photoSetKey);
    Observable<UserSet> loadMore(UserSet userSet, Integer page);

}
