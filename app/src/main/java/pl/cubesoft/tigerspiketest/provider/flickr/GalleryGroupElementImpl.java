package pl.cubesoft.tigerspiketest.provider.flickr;

import java.util.Date;

import pl.cubesoft.tigerspiketest.data.model.GroupElement;
import pl.cubesoft.tigerspiketest.provider.flickr.model.GalleriesResult;


/**
 * Created by CUBESOFT on 18.09.2017.
 */

public class GalleryGroupElementImpl implements GroupElement {

    private GalleriesResult.GalleryElement groupElement;
    private static final String format = "jpg";
    @Override
    public int getItemCount() {
        return groupElement.getCountPhotos() + groupElement.getCountVideos();
    }

    @Override
    public String getTitle() {
        return groupElement.getTitle().getContent();
    }

    @Override
    public String getTitlePhotoUrl() {
        return ImageHelper.getPhotoUrl(groupElement.getIconFarm(), groupElement.getPrimaryPhotoServer(), groupElement.getPrimaryPhotoId(),
                groupElement.getPrimaryPhotoSecret(), "z", format);
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
        return GroupElementType.GALLERY;
    }

    public void setGroupElement(GalleriesResult.GalleryElement groupElement) {
        this.groupElement = groupElement;
    }
}
