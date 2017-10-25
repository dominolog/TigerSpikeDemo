package pl.cubesoft.tigerspiketest.provider.flickr;

import pl.cubesoft.tigerspiketest.data.PhotoSetKey;
import pl.cubesoft.tigerspiketest.data.model.Photo;
import pl.cubesoft.tigerspiketest.data.model.PhotoSet;
import pl.cubesoft.tigerspiketest.provider.flickr.model.PhotoSetPhotosResult;

/**
 * Created by CUBESOFT on 22.09.2017.
 */

class PhotoSetImpl2 implements PhotoSet {

    private final PhotoImpl photoImpl;


    private final PhotoSetPhotosResult photosResult;
    private final PhotoSetKey key;

    public PhotoSetImpl2(PhotoSetPhotosResult photosResult, PhotoSetKey key) {
        this.photosResult = photosResult;
        this.key = key;
        this.photoImpl = new PhotoImpl();
    }

    @Override
    public int getItemCount() {
        return photosResult.getPhotoset().getPhoto().size();
    }

    @Override
    public Photo getItem(int position) {
        photoImpl.setPhoto(photosResult.getPhotoset().getPhoto().get(position));
        return photoImpl;
    }

    @Override
    public String getTitle() {
        return photosResult.getPhotoset().getTitle();
    }

    @Override
    public String getId() {
        return Long.toString(photosResult.getPhotoset().getId());
    }

    @Override
    public Photo getTitlePhoto() {
        return null;
    }

    @Override
    public String getOwnerId() {
        return photosResult.getPhotoset().getOwner();
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
        PhotoImpl photoImpl = (PhotoImpl)photo;
        photosResult.getPhotoset().getPhoto().set(position, photoImpl.photo);
    }

    @Override
    public int getTotalItemCount() {
        return photosResult.getPhotoset().getTotal();
    }

    @Override
    public PhotoSetKey getKey() {
        return key;
    }
}
