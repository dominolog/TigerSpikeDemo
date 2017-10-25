package pl.cubesoft.tigerspiketest.provider.flickr;

import android.util.Pair;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import pl.cubesoft.tigerspiketest.data.model.Photo;
import pl.cubesoft.tigerspiketest.data.model.User;

/**
 * Created by CUBESOFT on 26.09.2017.
 */

class PhotoImpl implements Photo {

    pl.cubesoft.tigerspiketest.provider.flickr.model.Photo photo;
    private static final String format = "jpg";

    public PhotoImpl(pl.cubesoft.tigerspiketest.provider.flickr.model.Photo photo) {
        this.photo = photo;
    }

    public PhotoImpl() {

    }

    @Override
    public String getId() {
        return Long.toString(photo.getId());
    }

    @Override
    public String getTitle() {
        return photo.getTitle();
    }

    @Override
    public String getUrl() {
        return ImageHelper.getPhotoUrl(photo, "z", format);
    }

    @Override
    public Integer getWidth() {
        return photo.getWidth();
    }

    @Override
    public Integer getHeight() {
        return photo.getHeight();
    }

    @Override
    public User getUser() {
        return null;
    }

    @Override
    public String getOwnerId() {
        return photo.getOwner();
    }

    @Override
    public Integer getNumViews() {
        return photo.getViews();
    }

    @Override
    public Integer getNumFavorites() {
        return null;
    }

    @Override
    public Float getRating() {
        return null;
    }

    @Override
    public Integer getMaxRating() {
        return null;
    }

    @Override
    public String getDescription() {
        return photo.getDescription();
    }

    @Override
    public String getCommentPoolId() {
        return Long.toString(photo.getId());
    }

    @Override
    public List<String> getTags() {
        return photo.getTags();
    }

    @Override
    public Integer getNumComments() {
        return null;
    }

    @Override
    public Boolean isFavorite() {
        return null;
    }

    @Override
    public List<Pair<String, String>> getExifTags() {
        return Collections.EMPTY_LIST;
    }

    @Override
    public Date getDateCreated() {
        return photo.getDateTaken();
    }

    @Override
    public Location getLocation() {
        return null;
    }

    @Override
    public Type getType() {
        if ("photo".equals(photo.getMedia())) {
            return Type.PHOTO;
        } else {
            return Type.VIDEO;
        }
    }

    public void setPhoto(pl.cubesoft.tigerspiketest.provider.flickr.model.Photo photo) {
        this.photo = photo;
    }
}
