package pl.cubesoft.tigerspiketest.provider.flickr;


import android.content.Context;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import okhttp3.OkHttpClient;


import pl.cubesoft.tigerspiketest.ImageLoader;
import pl.cubesoft.tigerspiketest.ImageLoaderPicasso;
import pl.cubesoft.tigerspiketest.data.AuthProvider;
import pl.cubesoft.tigerspiketest.data.CommentsProvider;
import pl.cubesoft.tigerspiketest.data.GroupProvider;
import pl.cubesoft.tigerspiketest.data.ModelCache;
import pl.cubesoft.tigerspiketest.data.PhotoSetProvider;
import pl.cubesoft.tigerspiketest.data.Provider;
import pl.cubesoft.tigerspiketest.data.UsersProvider;
import pl.cubesoft.tigerspiketest.data.model.Photo;
import pl.cubesoft.tigerspiketest.data.model.PhotoSet;
import pl.cubesoft.tigerspiketest.data.model.User;
import pl.cubesoft.tigerspiketest.utils.NetworkUtils;
import retrofit2.Retrofit;

/**
 * Created by CUBESOFT on 13.09.2017.
 */

public class ProviderImpl implements Provider {

    private final APIService apiService;
    private final PhotoSetProviderImpl photoSetProvider;
    private final UsersProviderImpl usersProvider;
    private final GroupProviderImpl groupProvider;
    private final CommentsProviderImpl commentsProvider;
    private final AuthProviderImpl authProvider;
    private final ImageLoaderPicasso imageLoader;

    public ProviderImpl(Context context) {

        Retrofit.Builder retrofitBuilder = NetworkUtils.buildStandardRetrofitBuilder();
        OkHttpClient.Builder httpClientBuilder = NetworkUtils.buildStandardHttpClientBuilder();

        httpClientBuilder.addNetworkInterceptor(chain -> chain.proceed(chain
                .request()
                .newBuilder()
                //.addHeader("api_key", Constants.API_KEY)
                .addHeader("format", "json")
                .addHeader("nojsoncallback", "1")

                .build()));

        authProvider = new AuthProviderImpl(context.getSharedPreferences("flickr", Context.MODE_PRIVATE), httpClientBuilder );

        OkHttpClient httpClient = httpClientBuilder
                .retryOnConnectionFailure(true)
                .build();

        imageLoader = new ImageLoaderPicasso(context, httpClient);


        this.apiService = retrofitBuilder
                .client(httpClient)
                .baseUrl(Constants.WEB_SERVICE_URL)
                .build()
                .create(APIService.class);

        final ModelCache modelCache = new ModelCache();

        APIServiceWrapper apiServiceWrapper = new APIServiceWrapper(apiService);
        photoSetProvider = new PhotoSetProviderImpl(apiServiceWrapper, modelCache);
        usersProvider = new UsersProviderImpl(apiServiceWrapper, authProvider, modelCache);
        groupProvider = new GroupProviderImpl(apiServiceWrapper);
        commentsProvider = new CommentsProviderImpl(apiServiceWrapper);


    }


    @Override
    public PhotoSetProvider getPhotoSetProvider() {
        return photoSetProvider;
    }

    @Override
    public UsersProvider getUsersProvider() {
        return usersProvider;
    }

    @Override
    public GroupProvider getGroupProvider() {
        return groupProvider;
    }

    @Override
    public CommentsProvider getCommentsProvider() {
        return commentsProvider;
    }

    @Override
    public AuthProvider getAuthProvider() {
        return authProvider;
    }


    @Override
    public String getName() {
        return "Flickr";
    }

    @Override
    public ImageLoader getImageLoader() {
        return imageLoader;
    }

    @Override
    public boolean canUpload(PhotoSet photoSet) {
        return false;
    }

    @Override
    public boolean canFollow(User user) {
        return false;
    }

    @Override
    public boolean canDeleteFromPhotoSet(PhotoSet photoSet) {
        return authProvider.isCurrentUser(photoSet.getOwnerId());
    }

    @Override
    public boolean canFavorite(Photo item) {
        return false;
    }

    @Override
    public List<UserMainViewType> getUserMainViewTypes() {
        return Arrays.asList(UserMainViewType.PHOTOS, UserMainViewType.SETS, UserMainViewType.GALLERIES, UserMainViewType.FAVORITES, UserMainViewType.FRIENDS_PHOTOS, UserMainViewType.FRIENDS);
    }

    @Override
    public List<BrowserViewType> getBrowserViewTypes() {
        return Arrays.asList(BrowserViewType.POPULAR_PHOTOS, BrowserViewType.RECENT_PHOTOS);
    }

    @Override
    public List<UserDetailsViewType> getUserDetailsViewTypes() {
        return Arrays.asList(UserDetailsViewType.POPULAR_PHOTOS, UserDetailsViewType.POPULAR_SETS, UserDetailsViewType.GALLERIES, UserDetailsViewType.SETS, UserDetailsViewType.FRIENDS_PHOTOS, UserDetailsViewType.FRIENDS);
    }

    @Override
    public List<SearchViewType> getSearchViewTypes() {
        return Collections.singletonList(SearchViewType.PHOTOS);
    }
}
