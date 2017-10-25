package pl.cubesoft.tigerspiketest.data;

import java.util.List;


import pl.cubesoft.tigerspiketest.data.model.Group;
import pl.cubesoft.tigerspiketest.data.model.GroupElement;
import pl.cubesoft.tigerspiketest.data.model.PhotoSet;
import rx.Observable;

/**
 * Created by CUBESOFT on 13.09.2017.
 */

public interface GroupProvider {




    enum GroupType {
        POPULAR,
        RECENT,
        USER_RECENT,
        USER_POPULAR,
        SEARCH,
        USER_SETS,
        USER_GALLERIES
    }

    Observable<Group> getGroup(GroupType groupType, String userId, String groupId);

    Observable<Group> searchGroup(String searchText);

    Observable<PhotoSet> createPhotoSet(String userId, String parentId, String title, String subTitle, String description, List<String> tags, boolean isPrivate);
    Observable<PhotoSet> createGallery(String userId, String parentId, String title, String subTitle, String description, List<String> tags, boolean isPrivate);
    Observable<Group> createGroup(String userId, String parentId, String title, String subTitle, String description, List<String> tags, boolean isPrivate);

    Observable<Boolean> removeGallery(String userId, String galleryId);
    Observable<Boolean> removePhotoSet(String userId, String photoSetId);
    Observable<Boolean> removeGroup(String userId, String groupId);

    List<GroupElement.GroupElementType> getCreateTypeList();

}
