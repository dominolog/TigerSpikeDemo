package pl.cubesoft.tigerspiketest.provider.flickr;

import java.util.List;

import pl.cubesoft.tigerspiketest.data.PhotoSetKey;
import pl.cubesoft.tigerspiketest.data.model.Photo;
import pl.cubesoft.tigerspiketest.data.model.PhotoSet;
import pl.cubesoft.tigerspiketest.provider.flickr.model.PhotosResult;

/**
 * Created by CUBESOFT on 14.09.2017.
 */

public class PhotoSetImpl implements PhotoSet {

    private final PhotoImpl photo;
    private final String title;


    private final PhotosResult photosResult;
    private final PhotoSetKey key;

    public PhotoSetImpl(PhotosResult photosResult, String title, PhotoSetKey key) {
        this.photosResult = photosResult;
        this.key = key;
        this.photo = new PhotoImpl();
        this.title = title;
    }

    @Override
    public int getItemCount() {
        return photosResult.getPhotos().getPhoto().size();
    }

    @Override
    public Photo getItem(int position) {
        photo.setPhoto(photosResult.getPhotos().getPhoto().get(position));
        return photo;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getId() {
        return null;
    }

    @Override
    public Photo getTitlePhoto() {
        return null;
    }

    @Override
    public String getOwnerId() {
        return key.getUserId();
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
        PhotoImpl photoImpl = (PhotoImpl) photo;
        photosResult.getPhotos().getPhoto().set(position, photoImpl.photo);
    }

    @Override
    public int getTotalItemCount() {
        return photosResult.getPhotos().getTotal();
    }

    @Override
    public PhotoSetKey getKey() {
        return key;
    }

    public List<pl.cubesoft.tigerspiketest.provider.flickr.model.Photo> getItems() {
        return photosResult.getPhotos().getPhoto();
    }

    public void addItems(List<pl.cubesoft.tigerspiketest.provider.flickr.model.Photo> items) {
        photosResult.getPhotos().getPhoto().addAll(items);
    }
}
