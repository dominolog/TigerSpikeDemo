package pl.cubesoft.tigerspiketest.provider.flickr;

import java.util.Date;

import pl.cubesoft.tigerspiketest.data.model.GroupElement;
import pl.cubesoft.tigerspiketest.provider.flickr.model.PhotoSetsResult;

/**
 * Created by CUBESOFT on 22.09.2017.
 */

class PhotoSetGroupElementImpl implements GroupElement {
    private PhotoSetsResult.PhotoSetElement groupElement;
    private static final String format = "jpg";

    @Override
    public int getItemCount() {
        return groupElement.getPhotos() + groupElement.getVideos();
    }

    @Override
    public String getTitle() {
        return groupElement.getTitle().getContent();
    }

    @Override
    public String getTitlePhotoUrl() {

        return ImageHelper.getPhotoUrl(groupElement.getFarm(), groupElement.getServer(), Long.parseLong(groupElement.getPrimary()),
                groupElement.getSecret(), "z", format);

    }

    @Override
    public String getId() {
        return groupElement.getId();
    }



    @Override
    public Date getLastUpdated() {
        return groupElement.getDateUpdate();
    }

    @Override
    public Integer getViewCount() {
        return groupElement.getCountViews();
    }

    @Override
    public Boolean isPrivate() {
        return null;
    }

    @Override
    public GroupElementType getType() {
        return GroupElementType.PHOTOSET;
    }

    public void setGroupElement(PhotoSetsResult.PhotoSetElement groupElement) {
        this.groupElement = groupElement;
    }
}
