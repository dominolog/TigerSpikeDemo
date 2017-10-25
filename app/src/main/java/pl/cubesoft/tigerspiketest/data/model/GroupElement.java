package pl.cubesoft.tigerspiketest.data.model;

import java.util.Date;

/**
 * Created by CUBESOFT on 16.09.2017.
 */

public interface GroupElement {

    int getItemCount();
    String getTitle();
    String getTitlePhotoUrl();
    String getId();
    Date getLastUpdated();

    Integer getViewCount();

    Boolean isPrivate();

    enum GroupElementType {
        PHOTOSET,
        GALLERY,
        GROUP
    }
    GroupElementType getType();
}
