package pl.cubesoft.tigerspiketest.provider.flickr;

import pl.cubesoft.tigerspiketest.data.PhotoSetKey;
import pl.cubesoft.tigerspiketest.data.model.Photo;
import pl.cubesoft.tigerspiketest.data.model.PhotoSet;
import pl.cubesoft.tigerspiketest.provider.flickr.model.Gallery;


/**
 * Created by CUBESOFT on 03.10.2017.
 */

class PhotoSetImpl3 implements PhotoSet {
    private final Gallery gallery;
    private final PhotoSetKey key;

    public PhotoSetImpl3(Gallery gallery, PhotoSetKey key) {
        this.gallery = gallery;
        this.key = key;
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    @Override
    public Photo getItem(int position) {
        return null;
    }

    @Override
    public String getTitle() {
        return null;
    }

    @Override
    public String getId() {
        return gallery.getId();
    }

    @Override
    public Photo getTitlePhoto() {
        return null;
    }

    @Override
    public String getOwnerId() {
        return null;
    }

    @Override
    public String getCommentPoolId() {
        return null;
    }

    @Override
    public Boolean isPrivate() {
        return null;
    }

    @Override
    public void setItem(int position, Photo photo) {

    }

    @Override
    public int getTotalItemCount() {
        return 0;
    }

    @Override
    public PhotoSetKey getKey() {
        return key;
    }
}
