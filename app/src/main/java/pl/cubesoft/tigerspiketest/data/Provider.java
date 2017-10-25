package pl.cubesoft.tigerspiketest.data;

import java.util.List;

import pl.cubesoft.tigerspiketest.ImageLoader;
import pl.cubesoft.tigerspiketest.data.model.Photo;
import pl.cubesoft.tigerspiketest.data.model.PhotoSet;
import pl.cubesoft.tigerspiketest.data.model.User;


/**
 * Created by CUBESOFT on 13.09.2017.
 */

public interface Provider {
    PhotoSetProvider getPhotoSetProvider();
    UsersProvider getUsersProvider();
    GroupProvider getGroupProvider();
    CommentsProvider getCommentsProvider();
    AuthProvider getAuthProvider();


    String getName();

    ImageLoader getImageLoader();

    boolean canUpload(PhotoSet photoSet);

    boolean canFollow(User user);

    boolean canDeleteFromPhotoSet(PhotoSet photoSet);

    boolean canFavorite(Photo item);


    enum UserMainViewType {
        PHOTOS,
        SETS,
        GALLERIES,
        ROOT_GROUP,
        FAVORITES,
        FRIENDS_PHOTOS,
        FRIENDS,
        FOLLOWERS
    }

    List<UserMainViewType> getUserMainViewTypes();


    enum BrowserViewType {
        POPULAR_PHOTOS,
        RECENT_PHOTOS,
        HIGHEST_RATED_PHOTOS,
        POPULAR_SETS,
        VOTED_PHOTOS,
        RECENT_SETS
    }

    List<BrowserViewType> getBrowserViewTypes();


    enum UserDetailsViewType {
        POPULAR_PHOTOS,
        RECENT_PHOTOS,
        FAVORITE_PHOTOS,
        POPULAR_SETS,
        RECENT_SETS,
        SETS,
        FRIENDS,
        FOLLOWERS,
        GALLERIES,
        FRIENDS_PHOTOS,
    }
    List<UserDetailsViewType> getUserDetailsViewTypes();

    enum SearchViewType {
        PHOTOS,
        SETS,
        USERS
    }

    List<SearchViewType> getSearchViewTypes();

}
