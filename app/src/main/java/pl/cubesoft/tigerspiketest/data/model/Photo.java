package pl.cubesoft.tigerspiketest.data.model;

import android.util.Pair;

import java.util.Date;
import java.util.List;

/**
 * Created by CUBESOFT on 13.09.2017.
 */

public interface Photo {
    String getId();
    String getTitle();
    String getUrl();

    Integer getWidth();

    Integer getHeight();

    User getUser();

    String getOwnerId();

    Integer getNumViews();

    Integer getNumFavorites();

    Float getRating();

    Integer getMaxRating();

    String getDescription();

    String getCommentPoolId();

    List<String> getTags();

    Integer getNumComments();

    Boolean isFavorite();

    List<Pair<String, String>> getExifTags();

    Date getDateCreated();

    public interface Location {
        double getLatitude();
        double getLongitude();
        String getName();
    }
    Location getLocation();

    enum Type {
        PHOTO,
        VIDEO
    }
    Type getType();
}
