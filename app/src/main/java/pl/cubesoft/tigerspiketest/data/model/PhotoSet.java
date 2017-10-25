package pl.cubesoft.tigerspiketest.data.model;


import pl.cubesoft.tigerspiketest.data.PhotoSetKey;

/**
 * Created by CUBESOFT on 13.09.2017.
 */

public interface PhotoSet {
    int getItemCount();
    Photo getItem(int position);

    String getTitle();

    String getId();

    Photo getTitlePhoto();

    String getOwnerId();

    String getCommentPoolId();

    Boolean isPrivate();

    void setItem(int position, Photo photo);

    int getTotalItemCount();

    PhotoSetKey getKey();
}
