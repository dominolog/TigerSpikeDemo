package pl.cubesoft.tigerspiketest.provider.flickr;

import android.text.TextUtils;
import android.util.Pair;

import com.annimon.stream.Stream;

import java.util.ArrayList;
import java.util.List;


import pl.cubesoft.tigerspiketest.data.BasePhotoSetProvider;
import pl.cubesoft.tigerspiketest.data.ModelCache;
import pl.cubesoft.tigerspiketest.data.PhotoSetKey;
import pl.cubesoft.tigerspiketest.data.model.Photo;
import pl.cubesoft.tigerspiketest.data.model.PhotoSet;
import rx.Observable;
import rx.schedulers.Schedulers;

/**
 * Created by CUBESOFT on 13.09.2017.
 */

class PhotoSetProviderImpl extends BasePhotoSetProvider {
    private final APIServiceWrapper apiService;

    public PhotoSetProviderImpl(APIServiceWrapper apiService, ModelCache modelCache) {
        super(modelCache);
        this.apiService = apiService;
    }

    @Override
    protected Observable<PhotoSet> getPhotoSet(PhotoSetKey photoSetKey, Integer page) {
        final String userId = photoSetKey.getUserId();
        final String photoSetId = photoSetKey.getPhotoSetId();
        switch (photoSetKey.getPhotoSetType()) {
            case RECENT:
                return apiService.getRecentPhotos(null, page).map( data -> new PhotoSetImpl(data, "Recent", photoSetKey));
            case POPULAR:
                return apiService.getPopularPhotos(userId, null, page).map(data -> new PhotoSetImpl(data, "Popular", photoSetKey));

            case USER_POPULAR:
                return apiService.getUserPopularPhotos(userId, null, page).map( data -> new PhotoSetImpl(data, "Popular", photoSetKey));

            case USER_FAVORITES:
                return apiService.getUserFavoritePhotos(userId, null, page).map( data -> new PhotoSetImpl(data, "Favorites", photoSetKey));

            case GALLERY_ID:
                return apiService.getGalleryPhotos(photoSetId, null, page).map( data -> new PhotoSetImpl(data, "Gallery", photoSetKey));

            case SET_ID:
                return apiService.getPhotoSetPhotos(photoSetId, userId, null, page).map((photosResult) -> new PhotoSetImpl2(photosResult, photoSetKey));

            case USER:
                return apiService.getUserPhotos("me", null, page).map( data -> new PhotoSetImpl(data, "Photos", photoSetKey));

            case USER_FRIENDS_PHOTOS: {
                if (TextUtils.isEmpty(photoSetKey.getUserId())) {
                    return apiService.getContactPhotos(50, 0, 0, 0).map(data -> new PhotoSetImpl(data, "Photos", photoSetKey));
                } else {
                    return apiService.getContactPublicPhotos(photoSetKey.getUserId(), 50, 0, 0, 0).map(data -> new PhotoSetImpl(data, "Photos", photoSetKey));
                }

            }


            case SEARCH:
                return apiService.searchPhotos(photoSetKey.getSearchText(), photoSetKey.getTags(), null, page).map( data -> new PhotoSetImpl(data, "Search", photoSetKey));

            case USER_RECENT:
                //return apiService.getRecentPhotos(userId, null, 1).map( data -> new PhotoSetImpl(data, "Recent"));
        }
        return Observable.empty();
    }

    @Override
    public Observable<PhotoSet> loadMore(PhotoSet photoSet, Integer page) {
        return getPhotoSet(photoSet.getKey(), page)
                .map(photoSet1 -> {
                    // merge result
                    PhotoSetImpl photoSetImpl = (PhotoSetImpl)photoSet;
                    photoSetImpl.addItems(((PhotoSetImpl)photoSet1).getItems());
                    return photoSetImpl;
                });
    }

    @Override
    public Observable<Photo> addPhotoToFavorites(Photo photo) {
        return Observable.empty();
    }

    @Override
    public Observable<Photo> removePhotoFromFavorites(Photo photo) {
        return Observable.empty();
    }

    @Override
    public Observable<PhotoSet> removePhotosFromPhotoSet(PhotoSet photoSet, List<String> ids) {
        List<Observable<Void>> tasks = new ArrayList<>();
        for (String id : ids) {
            tasks.add(apiService.deletePhoto(id));
        }

        return Observable.from(tasks)
                .flatMap(task -> task.observeOn(Schedulers.io()))
                //.timeout(600, TimeUnit.MILLISECONDS, Observable.empty())
                .toList()
                .concatMapDelayError(deletePhotoResults -> {
                    return getPhotoSet(true, photoSet.getKey());
                });
    }

    @Override
    public Observable<Photo> getPhoto(boolean force, String photoId) {
        return Observable.zip(apiService.getPhoto(photoId), apiService.getPhotoExif(photoId), Pair::create)
                .map(result-> new PhotoInfoImpl(result.first.getPhoto(), result.second));
    }

    @Override
    public Observable<String> getVideoUrl(String photoId) {
        return apiService.getPhotoSizes(photoId).map(photoSizesResult -> {
            return Stream.of(photoSizesResult.getSizes().getSize()).filter(size -> "video".equals(size.getMedia()) && "Mobile MP4".equals(size.getLabel())).findFirst().get().getSource();
        });
    }


}
