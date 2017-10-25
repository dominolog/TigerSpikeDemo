package pl.cubesoft.tigerspiketest.provider.flickr;

import android.net.Uri;

import pl.cubesoft.tigerspiketest.provider.flickr.model.User;

/**
 * Created by CUBESOFT on 18.09.2017.
 */

public class ImageHelper {

    public static String getProfileUrl(User user) {
        return user.getIconServer() > 0 ? String.format("http://farm%d.staticflickr.com/%s/buddyicons/%s.jpg",
                user.getIconFarm(),
                user.getIconServer(),
                Uri.encode(user.getNsid())) :
                String.format("https://www.flickr.com/images/buddyicon.gif");
    }

    public static  String getPhotoUrl(pl.cubesoft.tigerspiketest.provider.flickr.model.Photo photo, String size, String format) {
        return getPhotoUrl(
                photo.getFarm(),
                photo.getServer(),
                photo.getId(),
                photo.getSecret(),
                size,
                format);
    }


    public static String getPhotoUrl(int farm, String server, long photoId, String secret, String size, String format) {
        return String.format("https://farm%d.staticflickr.com/%s/%d_%s_%s.%s",
                farm,
                server,
                photoId,
                secret,
                size,
                format);
    }
}
