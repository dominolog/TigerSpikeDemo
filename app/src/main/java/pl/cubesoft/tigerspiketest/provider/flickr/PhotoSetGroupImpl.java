package pl.cubesoft.tigerspiketest.provider.flickr;

import pl.cubesoft.tigerspiketest.data.model.Group;
import pl.cubesoft.tigerspiketest.data.model.GroupElement;
import pl.cubesoft.tigerspiketest.provider.flickr.model.PhotoSetsResult;

/**
 * Created by CUBESOFT on 22.09.2017.
 */

class PhotoSetGroupImpl implements Group {
    private final PhotoSetsResult photoSetsResult;
    private final String title;
    private PhotoSetGroupElementImpl photoSetElement;
    public PhotoSetGroupImpl(PhotoSetsResult photoSetsResult, String title) {
        this.photoSetsResult = photoSetsResult;
        photoSetElement = new PhotoSetGroupElementImpl();
        this.title = title;
    }

    @Override
    public int getItemCount() {
        return photoSetsResult.getPhotoSets().getPhotoSet().size();
    }

    @Override
    public GroupElement getItem(int position) {
        photoSetElement.setGroupElement(photoSetsResult.getPhotoSets().getPhotoSet().get(position));
        return photoSetElement;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getId() {
        return null;
    }
}
