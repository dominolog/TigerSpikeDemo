package pl.cubesoft.tigerspiketest.data;

import java.util.List;


import pl.cubesoft.tigerspiketest.data.model.Photo;
import pl.cubesoft.tigerspiketest.data.model.PhotoSet;
import rx.Observable;

/**
 * Created by CUBESOFT on 13.09.2017.
 */

public interface PhotoSetProvider {


    enum PhotoSetType {
        POPULAR,
        RECENT,
        HIGHEST_RATED,
        VOTED,
        USER_RECENT,
        USER_POPULAR,
        USER_FAVORITES,
        SEARCH,
        SET_ID,
        GALLERY_ID,

        USER,
        USER_FRIENDS_PHOTOS;

    }

    Observable<PhotoSet> getPhotoSet(boolean force, PhotoSetKey photoSetKey);

    Observable<PhotoSet> loadMore(PhotoSet photoSet, Integer page);

    Observable<Photo> addPhotoToFavorites(Photo photo);

    Observable<Photo> removePhotoFromFavorites(Photo photo);

    Observable<PhotoSet> removePhotosFromPhotoSet(PhotoSet photoSet, List<String> ids);

    Observable<Photo> getPhoto(boolean force, String photoId);

    Observable<String> getVideoUrl(String photoId);


}
