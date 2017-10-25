package pl.cubesoft.tigerspiketest.provider.flickr;

import pl.cubesoft.tigerspiketest.data.model.Group;
import pl.cubesoft.tigerspiketest.data.model.GroupElement;
import pl.cubesoft.tigerspiketest.provider.flickr.model.GalleriesResult;

/**
 * Created by CUBESOFT on 18.09.2017.
 */

public class GalleryGroupImpl implements Group {
    private final GalleriesResult data;
    private GalleryGroupElementImpl groupElement;

    public GalleryGroupImpl(GalleriesResult data, String title) {
        this.data = data;
        this.groupElement = new GalleryGroupElementImpl();
    }

    @Override
    public int getItemCount() {
        return data.getGalleries().getTotal();
    }

    @Override
    public GroupElement getItem(int position) {

        groupElement.setGroupElement(data.getGalleries().getGallery().get(position));
        return groupElement;
    }

    @Override
    public String getTitle() {
        return groupElement.getTitle();
    }

    @Override
    public String getId() {
        return groupElement.getId();
    }
}
