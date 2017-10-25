package pl.cubesoft.tigerspiketest.provider.flickr;


import android.text.TextUtils;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import pl.cubesoft.tigerspiketest.provider.flickr.model.CommentsResult;
import pl.cubesoft.tigerspiketest.provider.flickr.model.ExifResult;
import pl.cubesoft.tigerspiketest.provider.flickr.model.GalleriesResult;
import pl.cubesoft.tigerspiketest.provider.flickr.model.GalleryResult;
import pl.cubesoft.tigerspiketest.provider.flickr.model.PhotoResult;
import pl.cubesoft.tigerspiketest.provider.flickr.model.PhotoSetPhotosResult;
import pl.cubesoft.tigerspiketest.provider.flickr.model.PhotoSetResult;
import pl.cubesoft.tigerspiketest.provider.flickr.model.PhotoSetsResult;
import pl.cubesoft.tigerspiketest.provider.flickr.model.PhotoSizesResult;
import pl.cubesoft.tigerspiketest.provider.flickr.model.PhotosResult;
import pl.cubesoft.tigerspiketest.provider.flickr.model.UserProfileResult;
import pl.cubesoft.tigerspiketest.provider.flickr.model.UserResult;
import pl.cubesoft.tigerspiketest.provider.flickr.model.UserResult2;
import pl.cubesoft.tigerspiketest.provider.flickr.model.UsersResult;
import rx.Observable;
import pl.cubesoft.tigerspiketest.utils.CollectionUtils;

/**
 * Created by CUBESOFT on 18.09.2017.
 */

public class APIServiceWrapper {
    private final APIService apiService;




    enum Sort {
        FAVES,
        VIEWS,
        COMMENTS,
        INTERESTING
    }

    enum Extra {
        description, license, date_upload, date_taken, owner_name, icon_server, original_format, last_update, geo, tags, machine_tags, o_dims, views, media, path_alias, url_sq, url_t, url_s, url_q, url_m, url_n, url_z, url_c, url_l, url_o;
    }


    private static final List<String> EXTRAS = Stream.of(Arrays.asList(Extra.icon_server, Extra.description, Extra.license, Extra.date_taken, Extra.owner_name, Extra.tags, Extra.views, Extra.media, Extra.o_dims)).map(extra -> extra.name()).collect(Collectors.toList());
    private static final String EXTRAS_STRING = TextUtils.join(",", EXTRAS);

    public APIServiceWrapper(APIService apiService) {
        this.apiService = apiService;
    }

    public Observable<PhotosResult> getUserPhotos(
            String userId,
            Integer perPage,
            Integer page) {
        return apiService.getPhotos("flickr.people.getPhotos", Constants.API_KEY, Constants.JSON_FORMAT, 1,
                userId,
                Sort.VIEWS.toString(),
                EXTRAS_STRING,
                perPage, page);
    }


    public Observable<PhotosResult> getPopularPhotos(
            String userId,
            Integer perPage,
            Integer page) {
        return apiService.getPhotos("flickr.interestingness.getList", Constants.API_KEY, Constants.JSON_FORMAT, 1,
                userId,
                Sort.VIEWS.toString(),
                EXTRAS_STRING,
                perPage, page);
    }

    public Observable<PhotosResult> getUserPopularPhotos(
            String userId,
            Integer perPage,
            Integer page) {
        return apiService.getPhotos("flickr.photos.getPopular", Constants.API_KEY, Constants.JSON_FORMAT, 1,
                userId,
                Sort.VIEWS.toString(),
                EXTRAS_STRING,
                perPage, page);
    }

    public Observable<PhotosResult> getUserFavoritePhotos(
            String userId,
            Integer perPage,
            Integer page) {
        return apiService.getPhotos("flickr.favorites.getList", Constants.API_KEY, Constants.JSON_FORMAT, 1,
                userId,
                null,
                EXTRAS_STRING,
                perPage, page);
    }

    public Observable<PhotosResult> getRecentPhotos(Integer perPage,
                                                    Integer page) {
        return apiService.getPhotos("flickr.photos.getRecent", Constants.API_KEY, Constants.JSON_FORMAT, 1,
                null, null, EXTRAS_STRING, perPage, page);
    }

    public Observable<PhotosResult> getContactPhotos(Integer count,
                                                     Integer justFriends,
                                                     Integer singlePhoto,
                                                     Integer includeSelf) {
        return apiService.getContactsPhotos("flickr.photos.getContactsPhotos", Constants.API_KEY, Constants.JSON_FORMAT, 1,
                count, justFriends, singlePhoto, includeSelf, EXTRAS_STRING);
    }

    public Observable<PhotosResult> getContactPublicPhotos(
            String userId,
            Integer count,
            Integer justFriends,
            Integer singlePhoto,
            Integer includeSelf) {
        return apiService.getContactsPublicPhotos("flickr.photos.getContactsPublicPhotos", Constants.API_KEY, Constants.JSON_FORMAT, 1,
                userId, count, justFriends, singlePhoto, includeSelf, EXTRAS_STRING);
    }

    public Observable<PhotosResult> getGalleryPhotos(String galleryId,
                                                     Integer perPage,
                                                     Integer page) {
        return apiService.getGalleryPhotos("flickr.galleries.getPhotos", Constants.API_KEY, Constants.JSON_FORMAT, 1, galleryId,
                EXTRAS_STRING, perPage, page);
    }

    public Observable<PhotoResult> getPhoto(String photoId) {
        return apiService.getPhoto("flickr.photos.getInfo", Constants.API_KEY, Constants.JSON_FORMAT, 1, photoId);
    }

    public Observable<ExifResult> getPhotoExif(String photoId) {
        return apiService.getPhotoExif("flickr.photos.getExif", Constants.API_KEY, Constants.JSON_FORMAT, 1, photoId);
    }

    public Observable<Void> deletePhoto(String photoId) {
        return apiService.deletePhoto("flickr.photos.delete", Constants.API_KEY, Constants.JSON_FORMAT, 1, photoId);
    }

    public Observable<UserResult> getUser(String userId) {
        return apiService.getUser("flickr.people.getInfo", Constants.API_KEY, Constants.JSON_FORMAT, 1, userId);
    }

    public Observable<UserProfileResult> getUserProfile(String userId) {
        return apiService.getUserProfile("flickr.profile.getProfile", Constants.API_KEY, Constants.JSON_FORMAT, 1, userId);
    }

    public Observable<UserResult2> getMyProfile() {
        return apiService.getUrlProfile("flickr.urls.getUserProfile", Constants.API_KEY, Constants.JSON_FORMAT, 1, null);
    }

    public Observable<GalleriesResult> getGalleries(String userId, Integer perPage,
                                                    Integer page) {
        return apiService.getGalleries("flickr.galleries.getList", Constants.API_KEY, Constants.JSON_FORMAT, 1, userId, EXTRAS_STRING, perPage, page);
    }

    public Observable<PhotoSizesResult> getPhotoSizes(String photoId) {
        return apiService.getPhotoSizes("flickr.photos.getSizes", Constants.API_KEY, Constants.JSON_FORMAT, 1, photoId);
    }


    public Observable<PhotoSetsResult> getPhotoSets(String userId, Integer perPage,
                                                    Integer page) {
        return apiService.getPhotoSets("flickr.photosets.getList", Constants.API_KEY, Constants.JSON_FORMAT, 1, userId, EXTRAS_STRING, perPage, page);
    }


    public Observable<PhotoSetPhotosResult> getPhotoSetPhotos(String photoSetId,
                                                              String userId,

                                                              Integer perPage,
                                                              Integer page) {
        return apiService.getPhotoSetPhotos("flickr.photosets.getPhotos", Constants.API_KEY, Constants.JSON_FORMAT, 1, photoSetId, userId, EXTRAS_STRING,
                perPage, page);
    }

    public Observable<PhotoSetResult> createPhotoSet(String title, String description,
                                                     Long primaryPhotoId) {
        return apiService.createPhotoSet("flickr.photosets.create", Constants.API_KEY, Constants.JSON_FORMAT, 1, title, description, primaryPhotoId);
    }

    public Observable<Boolean> removePhotoSet(String photoSetId) {
        return apiService.deletePhotoSet("flickr.photosets.delete", Constants.API_KEY, Constants.JSON_FORMAT, 1, photoSetId).map(aVoid -> true);
    }

    public Observable<GalleryResult> createGallery(String title, String description,
                                                   Long primaryPhotoId) {
        return apiService.createGallery("flickr.galleries.create", Constants.API_KEY, Constants.JSON_FORMAT, 1, title, description, primaryPhotoId, null);
    }

    public Observable<CommentsResult> getPhotoComments(String photoId) {
        return apiService.getPhotoComments("flickr.photos.comments.getList", Constants.API_KEY, Constants.JSON_FORMAT, 1, photoId, null, null);
    }

    public Observable<CommentsResult.Comment> addComment(String photoId, String comment) {
        return apiService.addPhotoComment("flickr.photos.comments.addComment", Constants.API_KEY, Constants.JSON_FORMAT, 1, photoId, comment);
    }


    public Observable<PhotosResult> searchPhotos(String query, Collection<String> tags, Integer perPage,
                                                 Integer page) {
        return apiService.searchPhotos("flickr.photos.search", Constants.API_KEY, Constants.JSON_FORMAT, 1, null, !CollectionUtils.isEmpty(tags) ? TextUtils.join(",", tags) : null,
                query, EXTRAS_STRING, perPage, page);
    }


    public Observable<UsersResult> getUserContactList(String userId, Integer perPage,
                                                      Integer page) {
        return apiService.getContactPublicList("flickr.contacts.getPublicList", Constants.API_KEY, Constants.JSON_FORMAT, 1, userId, perPage, page);
    }
}
