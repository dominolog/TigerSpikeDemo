package pl.cubesoft.tigerspiketest.data;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import pl.cubesoft.tigerspiketest.data.model.PhotoSet;
import pl.cubesoft.tigerspiketest.data.model.User;
import pl.cubesoft.tigerspiketest.data.model.UserSet;


/**
 * Created by CUBESOFT on 20.09.2017.
 */

public class ModelCache {

    private final Map<PhotoSetKey, PhotoSet> photoSetCache = new ConcurrentHashMap<>();
    private final Map<UserSetKey, UserSet> userSetCache = new ConcurrentHashMap<>();
    private final Map<String, User> userCache = new ConcurrentHashMap<>();

    void addUser(String id, User user) {
        userCache.put(id, user);
    }
    User getUser(String userId) {
        return userCache.get(userId);
    }


    public PhotoSet getPhotoSet(PhotoSetKey photoSetKey) {
        return photoSetCache.get(photoSetKey);
    }

    public void addPhotoSet(PhotoSetKey photoSetKey, PhotoSet photoSet) {
        photoSetCache.put(photoSetKey, photoSet);
    }

    public void addUserSet(UserSetKey userSetKey, UserSet userSet) {
        userSetCache.put(userSetKey, userSet);
    }

    public UserSet getUserSet(UserSetKey userSetKey) {
        return userSetCache.get(userSetKey);
    }
}
