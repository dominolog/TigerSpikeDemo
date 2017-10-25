package pl.cubesoft.tigerspiketest.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.annimon.stream.Objects;

/**
 * Created by CUBESOFT on 09.10.2017.
 */

public final class UserSetKey implements Parcelable{

    private final UsersProvider.UserSetType userSetType;
    private final String userId;
    private final String photoId;
    private final String searchText;

    private UserSetKey(UsersProvider.UserSetType userSetType, String userId, String photoId, String searchText) {
        this.userSetType = userSetType;
        this.userId = userId;
        this.photoId = photoId;
        this.searchText = searchText;
    }

    protected UserSetKey(Parcel in) {
        userSetType = (UsersProvider.UserSetType) in.readSerializable();
        userId = in.readString();
        photoId = in.readString();
        searchText = in.readString();
    }

    public static final Creator<UserSetKey> CREATOR = new Creator<UserSetKey>() {
        @Override
        public UserSetKey createFromParcel(Parcel in) {
            return new UserSetKey(in);
        }

        @Override
        public UserSetKey[] newArray(int size) {
            return new UserSetKey[size];
        }
    };

    public static UserSetKey create (UsersProvider.UserSetType userSetType, String userId, String photoId, String searchText) {
        return new UserSetKey(userSetType, userId, photoId, searchText);
    }



    @Override
    public boolean equals(Object obj) {
        if(obj != null && obj instanceof UserSetKey) {
            UserSetKey k = (UserSetKey)obj;
            return Objects.equals(userSetType, k.userSetType) &&
                    Objects.equals(userId, k.userId) &&
                    Objects.equals(photoId, k.photoId) &&
                    Objects.equals(searchText, k.searchText);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userSetType, userId, photoId, searchText );
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeSerializable(userSetType);
        parcel.writeString(userId);
        parcel.writeString(photoId);
        parcel.writeString(searchText);



    }

    public UsersProvider.UserSetType getUserSetType() {
        return userSetType;
    }

    public String getUserId() {
        return userId;
    }

    public String getPhotoId() {
        return photoId;
    }

    public String getSearchText() {
        return searchText;
    }

}