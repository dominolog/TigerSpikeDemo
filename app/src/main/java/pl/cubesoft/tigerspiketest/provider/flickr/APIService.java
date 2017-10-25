package pl.cubesoft.tigerspiketest.provider.flickr;


import java.util.Date;


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
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by CUBESOFT on 13.09.2017.
 */

public interface APIService {

    @GET("services/rest")
    Observable<PhotosResult> getPhotos(@Query("method") String method,
                                       @Query("api_key") String apiKey,
                                       @Query("format") String format,
                                       @Query("nojsoncallback") int nojsoncallback,
                                       @Query("user_id") String userId,
                                       @Query("sort") String sort,
                                       @Query("extras") String extras,
                                       @Query("per_page") Integer perPage,
                                       @Query("page") Integer page);


    @GET("services/rest")
    Observable<PhotosResult> getContactsPhotos(@Query("method") String method,
                                               @Query("api_key") String apiKey,
                                               @Query("format") String format,
                                               @Query("nojsoncallback") int nojsoncallback,
                                               @Query("count") Integer count,
                                               @Query("just_friends") Integer justFriends,
                                               @Query("single_photo") Integer singlePhoto,
                                               @Query("include_self") Integer includeSelf,
                                               @Query("extras") String extras);


    @GET("services/rest")
    Observable<PhotosResult> getContactsPublicPhotos(@Query("method") String method,
                                                     @Query("api_key") String apiKey,
                                                     @Query("format") String format,
                                                     @Query("nojsoncallback") int nojsoncallback,
                                                     @Query("user_id") String userId,
                                                     @Query("count") Integer count,
                                                     @Query("just_friends") Integer justFriends,
                                                     @Query("single_photo") Integer singlePhoto,
                                                     @Query("include_self") Integer includeSelf,
                                                     @Query("extras") String extras);

    @GET("services/rest")
    Observable<PhotosResult> getGalleryPhotos(@Query("method") String method,
                                              @Query("api_key") String apiKey,
                                              @Query("format") String format,
                                              @Query("nojsoncallback") int nojsoncallback,
                                              @Query("gallery_id") String galleryId,
                                              @Query("extras") String extras,
                                              @Query("per_page") Integer perPage,
                                              @Query("page") Integer page);


    @GET("services/rest")
    Observable<PhotoSetPhotosResult> getPhotoSetPhotos(@Query("method") String method,
                                                       @Query("api_key") String apiKey,
                                                       @Query("format") String format,
                                                       @Query("nojsoncallback") int nojsoncallback,
                                                       @Query("photoset_id") String photoSetId,
                                                       @Query("user_id") String userId,
                                                       @Query("extras") String extras,
                                                       @Query("per_page") Integer perPage,
                                                       @Query("page") Integer page);

    @GET("services/rest")
    Observable<PhotosResult> searchPhotos(@Query("method") String method,
                                          @Query("api_key") String apiKey,
                                          @Query("format") String format,
                                          @Query("nojsoncallback") int nojsoncallback,
                                          @Query("user_id") String userId,
                                          @Query("tags") String tags,
                                          @Query("text") String text,
                                          @Query("extras") String extras,
                                          @Query("per_page") Integer perPage,
                                          @Query("page") Integer page);

    @GET("services/rest")
    Observable<PhotoResult> getPhoto(@Query("method") String method,
                                     @Query("api_key") String apiKey,
                                     @Query("format") String format,
                                     @Query("nojsoncallback") int nojsoncallback,
                                     @Query("photo_id") String photoId);

    @GET("services/rest")
    Observable<ExifResult> getPhotoExif(@Query("method") String method,
                                        @Query("api_key") String apiKey,
                                        @Query("format") String format,
                                        @Query("nojsoncallback") int nojsoncallback,
                                        @Query("photo_id") String photoId);


    @GET("services/rest")
    Observable<UserResult> getUser(@Query("method") String method,
                                   @Query("api_key") String apiKey,
                                   @Query("format") String format,
                                   @Query("nojsoncallback") int nojsoncallback,
                                   @Query("user_id") String id);

    @GET("services/rest")
    Observable<UserProfileResult> getUserProfile(@Query("method") String method,
                                                 @Query("api_key") String apiKey,
                                                 @Query("format") String format,
                                                 @Query("nojsoncallback") int nojsoncallback,
                                                 @Query("user_id") String userId);


    @GET("services/rest")
    Observable<UserResult2> getUrlProfile(@Query("method") String method,
                                          @Query("api_key") String apiKey,
                                          @Query("format") String format,
                                          @Query("nojsoncallback") int nojsoncallback,
                                          @Query("user_id") String userId);




    @GET("services/rest")
    Observable<GalleriesResult> getGalleries(@Query("method") String method,
                                             @Query("api_key") String apiKey,
                                             @Query("format") String format,
                                             @Query("nojsoncallback") int nojsoncallback,
                                             @Query("user_id") String userId,
                                             @Query("primary_photo_extras") String primary_photo_extras,
                                             @Query("per_page") Integer perPage,
                                             @Query("page") Integer page);


    @GET("services/rest")
    Observable<PhotoSetsResult> getPhotoSets(@Query("method") String method,
                                             @Query("api_key") String apiKey,
                                             @Query("format") String format,
                                             @Query("nojsoncallback") int nojsoncallback,
                                             @Query("user_id") String userId,
                                             @Query("primary_photo_extras") String primary_photo_extras,
                                             @Query("per_page") Integer perPage,
                                             @Query("page") Integer page);

    @POST("services/rest")
    Observable<PhotoSetResult> createPhotoSet(@Query("method") String method,
                                              @Query("api_key") String apiKey,
                                              @Query("format") String format,
                                              @Query("nojsoncallback") int nojsoncallback,
                                              @Query("title") String title,
                                              @Query("description") String description,
                                              @Query("primary_photo_id") Long primaryPhotoId);
    @POST("services/rest")
    Observable<Void> deletePhotoSet(@Query("method") String method,
                                    @Query("api_key") String apiKey,
                                    @Query("format") String format,
                                    @Query("nojsoncallback") int nojsoncallback,
                                    @Query("photoset_id") String photoSetId);


    @POST("services/rest")
    Observable<GalleryResult> createGallery(@Query("method") String method,
                                            @Query("api_key") String apiKey,
                                            @Query("format") String format,
                                            @Query("nojsoncallback") int nojsoncallback,
                                            @Query("title") String title,
                                            @Query("description") String description,
                                            @Query("primary_photo_id") Long primaryPhotoId,
                                            @Query("full_result") Integer fullResult);

    @GET("services/rest")
    Observable<CommentsResult> getPhotoComments(@Query("method") String method,
                                                @Query("api_key") String apiKey,
                                                @Query("format") String format,
                                                @Query("nojsoncallback") int nojsoncallback,
                                                @Query("photo_id") String photoId,
                                                @Query("min_comment_date") Date minCommentDate,
                                                @Query("max_comment_date") Date maxCommentDate);

    @POST("services/rest")
    Observable<CommentsResult.Comment> addPhotoComment(@Query("method") String method,
                                                       @Query("api_key") String apiKey,
                                                       @Query("format") String format,
                                                       @Query("nojsoncallback") int nojsoncallback,
                                                       @Query("photo_id") String photoId,
                                                       @Query("comment_text") String comment);


    @GET("services/rest")
    Observable<UsersResult> getContactPublicList(@Query("method") String method,
                                                 @Query("api_key") String apiKey,
                                                 @Query("format") String format,
                                                 @Query("nojsoncallback") int nojsoncallback,
                                                 @Query("user_id") String userId,
                                                 @Query("per_page") Integer perPage,
                                                 @Query("page") Integer page);

    @GET("services/rest")
    Observable<PhotoSizesResult> getPhotoSizes(@Query("method") String method,
                                               @Query("api_key") String apiKey,
                                               @Query("format") String format,
                                               @Query("nojsoncallback") int nojsoncallback,
                                               @Query("photo_id") String photoId);

    @POST("services/rest")
    Observable<Void> deletePhoto(@Query("method") String method,
                                 @Query("api_key") String apiKey,
                                 @Query("format") String format,
                                 @Query("nojsoncallback") int nojsoncallback,
                                 @Query("photo_id") String photoId);
}
