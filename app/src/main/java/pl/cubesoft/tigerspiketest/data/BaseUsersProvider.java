package pl.cubesoft.tigerspiketest.data;



import pl.cubesoft.tigerspiketest.data.model.UserSet;
import rx.Observable;

/**
 * Created by CUBESOFT on 13.09.2017.
 */

public abstract class BaseUsersProvider implements UsersProvider {
    private final ModelCache modelCache;

    public BaseUsersProvider(ModelCache modelCache) {
        this.modelCache = modelCache;
    }

    @Override
    public final Observable<UserSet> getUserSet(boolean force, UserSetKey userSetKey) {

        Integer page = 1;

        Observable<UserSet> network = getUserSet(userSetKey, page).doOnNext(userSet -> modelCache.addUserSet(userSetKey, userSet));
        Observable<UserSet> result = force ? network : Observable.concat(getCachedUserSet(userSetKey), network).first();
        return result;
    }


    private Observable<UserSet> getCachedUserSet(UserSetKey userSetKey) {
        return Observable.create(subscriber -> {
            final UserSet userSet = modelCache.getUserSet(userSetKey);
            if (userSet != null) {
                subscriber.onNext(userSet);
            }
            subscriber.onCompleted();
        });

    }

    protected abstract Observable<UserSet> getUserSet(UserSetKey userSetKey, Integer page);

}
