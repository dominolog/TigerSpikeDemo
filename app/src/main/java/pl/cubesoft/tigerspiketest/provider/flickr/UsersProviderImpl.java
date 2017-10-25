package pl.cubesoft.tigerspiketest.provider.flickr;

import android.util.Pair;


import pl.cubesoft.tigerspiketest.data.BaseUsersProvider;
import pl.cubesoft.tigerspiketest.data.ModelCache;
import pl.cubesoft.tigerspiketest.data.UserSetKey;
import pl.cubesoft.tigerspiketest.data.model.Photo;
import pl.cubesoft.tigerspiketest.data.model.User;
import pl.cubesoft.tigerspiketest.data.model.UserSet;
import pl.cubesoft.tigerspiketest.provider.flickr.model.UserProfileResult;
import pl.cubesoft.tigerspiketest.provider.flickr.model.UserResult;
import rx.Observable;

/**
 * Created by CUBESOFT on 13.09.2017.
 */

class UsersProviderImpl extends BaseUsersProvider {
    private final APIServiceWrapper apiService;
    private final AuthProviderImpl authProvider;

    public UsersProviderImpl(APIServiceWrapper apiService, AuthProviderImpl authProvider, ModelCache modelCache) {
        super(modelCache);
        this.apiService = apiService;
        this.authProvider = authProvider;
    }



    @Override
    public Observable<User> getUser(boolean force, String userId, String username, String email) {
        return apiService.getUser(userId).map(UserResult::getPerson).zipWith(
                apiService.getUserProfile(userId).map(UserProfileResult::getProfile), Pair::create)
                .map(result -> new UserImpl(result.first, result.second));
    }

    @Override
    public Observable<User> getMyUser(boolean force) {
        return apiService.getMyProfile().concatMapDelayError(userProfile -> {
            authProvider.setCurrentUserId(userProfile.getUser().getNsid());
            return apiService.getUser(userProfile.getUser().getNsid());
        }).map(UserImpl::new);
    }

    @Override
    public Observable<User> getPhotoOwner(boolean force, Photo photo) {
        return getUser(force, photo.getOwnerId(), null, null);
    }

    @Override
    public Observable<User> followUser(String userId, boolean follow) {
        return Observable.empty();
    }



    @Override
    protected Observable<UserSet> getUserSet(UserSetKey userSetKey, Integer page) {
        switch (userSetKey.getUserSetType()) {
            case FRIENDS:
                return apiService.getUserContactList(userSetKey.getUserId(), null, page).map(usersResult ->  new UserSetImpl(userSetKey, usersResult));
        }
        return Observable.empty();
    }

    @Override
    public Observable<UserSet> loadMore(UserSet userSet, Integer page) {
        return getUserSet(userSet.getKey(), page)
                .map(userSet1 -> {
                    // merge result
                    UserSetImpl userSetImpl = (UserSetImpl)userSet;
                    userSetImpl.addItems(((UserSetImpl)userSet1).getItems());
                    return userSetImpl;
                });
    }


}
