package pl.cubesoft.tigerspiketest.data;



import pl.cubesoft.tigerspiketest.data.model.PhotoSet;
import rx.Observable;

/**
 * Created by CUBESOFT on 13.09.2017.
 */

public abstract class BasePhotoSetProvider implements PhotoSetProvider{
    private final ModelCache modelCache;
    public BasePhotoSetProvider(ModelCache modelCache) {
        this.modelCache = modelCache;
    }

    @Override
    public final Observable<PhotoSet> getPhotoSet(boolean force, PhotoSetKey photoSetKey) {

        Integer page = 1;

        Observable<PhotoSet> network = getPhotoSet(photoSetKey, page).doOnNext(photoSet -> modelCache.addPhotoSet(photoSetKey, photoSet));
        Observable<PhotoSet> result = force ? network : Observable.concat(getCachedPhotoSet(photoSetKey), network).first();
        return result;
    }



    private  Observable<PhotoSet> getCachedPhotoSet(PhotoSetKey photoSetKey) {
        return Observable.create(subscriber -> {
            final PhotoSet photoSet = modelCache.getPhotoSet(photoSetKey);
            if ( photoSet != null ) {
                subscriber.onNext(photoSet);
            }
            subscriber.onCompleted();
        });

    }

    protected abstract Observable<PhotoSet> getPhotoSet(PhotoSetKey photoSetKey, Integer page);

}
