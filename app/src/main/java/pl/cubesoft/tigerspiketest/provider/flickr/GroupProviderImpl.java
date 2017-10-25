package pl.cubesoft.tigerspiketest.provider.flickr;

import java.util.Arrays;
import java.util.List;

import pl.cubesoft.tigerspiketest.data.GroupProvider;
import pl.cubesoft.tigerspiketest.data.PhotoSetKey;
import pl.cubesoft.tigerspiketest.data.PhotoSetProvider;
import pl.cubesoft.tigerspiketest.data.model.Group;
import pl.cubesoft.tigerspiketest.data.model.GroupElement;
import pl.cubesoft.tigerspiketest.data.model.PhotoSet;
import rx.Observable;

/**
 * Created by CUBESOFT on 18.09.2017.
 */

public class GroupProviderImpl implements GroupProvider {
    private final APIServiceWrapper apiService;

    public GroupProviderImpl(APIServiceWrapper apiServiceWrapper) {
        this.apiService = apiServiceWrapper;
    }



    @Override
    public Observable<Group> getGroup(GroupType groupType, String userId, String groupId) {
        switch (groupType) {
            case USER_SETS:
                return apiService.getPhotoSets(userId, null, 1).map( data -> new PhotoSetGroupImpl(data, "Sets"));

            case USER_GALLERIES:
                return apiService.getGalleries(userId, null, 1).map( data -> new GalleryGroupImpl(data, "Galleries"));
        }
        return Observable.empty();
    }

    @Override
    public Observable<Group> searchGroup(String searchText) {
        return Observable.empty();
    }

    @Override
    public Observable<PhotoSet> createPhotoSet(String userId, String parentId, String title, String subTitle, String description, List<String> tags, boolean isPrivate) {
        return apiService.createPhotoSet(title, description, null).map(photoSetResult -> null);
    }

    @Override
    public Observable<PhotoSet> createGallery(String userId, String parentId, String title, String subTitle, String description, List<String> tags, boolean isPrivate) {
        PhotoSetKey key = PhotoSetKey.create(PhotoSetProvider.PhotoSetType.GALLERY_ID, userId, null, null, null);
        return apiService.createGallery(title, description, null).map(galleryResult -> new PhotoSetImpl3(galleryResult.getGallery(), key));
    }

    @Override
    public Observable<Group> createGroup(String userId, String parentId, String title, String subTitle, String description, List<String> tags, boolean isPrivate) {
        return Observable.empty();
    }

    @Override
    public Observable<Boolean> removeGallery(String userId, String galleryId) {
        return Observable.empty();
    }

    @Override
    public Observable<Boolean> removePhotoSet(String userId, String photoSetId) {
        return apiService.removePhotoSet(photoSetId);
    }

    @Override
    public Observable<Boolean> removeGroup(String userId, String groupId) {
        return Observable.empty();
    }

    @Override
    public List<GroupElement.GroupElementType> getCreateTypeList() {
        return Arrays.asList(GroupElement.GroupElementType.GALLERY, GroupElement.GroupElementType.PHOTOSET);
    }
}
