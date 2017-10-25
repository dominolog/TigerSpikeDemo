package pl.cubesoft.tigerspiketest.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.annimon.stream.Objects;

import java.util.ArrayList;
import java.util.Collection;

import pl.cubesoft.tigerspiketest.utils.CollectionUtils;


/**
 * Created by CUBESOFT on 09.10.2017.
 */

public final class PhotoSetKey implements Parcelable{
    private final PhotoSetProvider.PhotoSetType photoSetType;
    private final String userId;
    private final String photoSetId;
    private final String searchText;
    private final Collection<String> tags;

    private PhotoSetKey(PhotoSetProvider.PhotoSetType photoSetType, String userId, String photoSetId, String searchText, Collection<String> tags) {
        this.photoSetType = photoSetType;
        this.userId = userId;
        this.photoSetId = photoSetId;
        this.searchText = searchText;
        this.tags = tags;
    }

    protected PhotoSetKey(Parcel in) {
        photoSetType = (PhotoSetProvider.PhotoSetType) in.readSerializable();
        userId = in.readString();
        photoSetId = in.readString();
        searchText = in.readString();
        ArrayList<String> tags = in.readArrayList(null);
        this.tags = tags;
    }

    public static final Creator<PhotoSetKey> CREATOR = new Creator<PhotoSetKey>() {
        @Override
        public PhotoSetKey createFromParcel(Parcel in) {
            return new PhotoSetKey(in);
        }

        @Override
        public PhotoSetKey[] newArray(int size) {
            return new PhotoSetKey[size];
        }
    };

    public static PhotoSetKey create (PhotoSetProvider.PhotoSetType photoSetType, String userId, String photoSetId, String searchText, Collection<String> tags) {
        return new PhotoSetKey(photoSetType, userId, photoSetId, searchText, tags);
    }



    @Override
    public boolean equals(Object obj) {
        if(obj != null && obj instanceof PhotoSetKey) {
            PhotoSetKey k = (PhotoSetKey)obj;
            return Objects.equals(photoSetType, k.photoSetType) &&
                    Objects.equals(userId, k.userId) &&
                    Objects.equals(photoSetId, k.photoSetId) &&
                    Objects.equals(searchText, k.searchText) &&
                    Objects.equals(tags, k.tags);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(photoSetType, userId, photoSetId, searchText, tags );
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeSerializable(photoSetType);
        parcel.writeString(userId);
        parcel.writeString(photoSetId);
        parcel.writeString(searchText);

        if ( !CollectionUtils.isEmpty(tags)) {
            parcel.writeSerializable(new ArrayList<>(tags));
        } else {
            parcel.writeValue(null);
        }

    }

    public PhotoSetProvider.PhotoSetType getPhotoSetType() {
        return photoSetType;
    }

    public String getUserId() {
        return userId;
    }

    public String getPhotoSetId() {
        return photoSetId;
    }

    public String getSearchText() {
        return searchText;
    }

    public Collection<String> getTags() {
        return tags;
    }
}