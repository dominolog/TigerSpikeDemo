package pl.cubesoft.tigerspiketest.provider.flickr;

import android.util.Pair;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;

import java.util.Date;
import java.util.List;

import pl.cubesoft.tigerspiketest.data.model.Photo;
import pl.cubesoft.tigerspiketest.data.model.User;
import pl.cubesoft.tigerspiketest.provider.flickr.model.ExifResult;
import pl.cubesoft.tigerspiketest.provider.flickr.model.PhotoInfo;

/**
 * Created by CUBESOFT on 26.09.2017.
 */

class PhotoInfoImpl implements Photo {

    private final ExifResult exifResult;
    private final List<Pair<String, String>> exifList;
    private PhotoInfo photo;
    private static final String format = "jpg";

    public PhotoInfoImpl(pl.cubesoft.tigerspiketest.provider.flickr.model.PhotoInfo photo, ExifResult exifResult) {
        this.photo = photo;
        this.exifResult = exifResult;
        this.exifList = Stream.of(exifResult.getPhoto().getExif()).map(exif -> Pair.create(exif.getLabel(), exif.getRaw().getContent())).collect(Collectors.toList());

    }



    @Override
    public String getId() {
        return Long.toString(photo.getId());
    }

    @Override
    public String getTitle() {
        return photo.getTitle().getContent();
    }

    @Override
    public String getUrl() {
        return null;
    }

    @Override
    public Integer getWidth() {
        return null;
    }

    @Override
    public Integer getHeight() {
        return null;
    }

    @Override
    public User getUser() {
        return null;//new UserImpl(photo.getOwner());
    }

    @Override
    public String getOwnerId() {
        return photo.getOwner().getNsid();
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
        return photo.getDescription().getContent();
    }

    @Override
    public String getCommentPoolId() {
        return Long.toString(photo.getId());
    }

    @Override
    public List<String> getTags() {
        return Stream.of(photo.getTags().getTag()).map(PhotoInfo.Tag::getContent).collect(Collectors.toList());
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
        return exifList;
    }

    @Override
    public Date getDateCreated() {
        return null;
    }

    @Override
    public Location getLocation() {
        return photo.getLocation() != null ? new Location() {
            @Override
            public double getLatitude() {
                return photo.getLocation().getLatitude();
            }

            @Override
            public double getLongitude() {
                return photo.getLocation().getLongitude();
            }

            @Override
            public String getName() {
                return photo.getLocation().getCountry().getContent();
            }
         } : null;
    }

    @Override
    public Type getType() {
        if ("photo".equals(photo.getMedia())) {
            return Type.PHOTO;
        } else {
            return Type.VIDEO;
        }
    }

    public void setPhoto(pl.cubesoft.tigerspiketest.provider.flickr.model.PhotoInfo photo) {
        this.photo = photo;
    }
}
