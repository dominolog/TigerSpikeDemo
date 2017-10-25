package pl.cubesoft.tigerspiketest.utils;

import android.text.format.DateUtils;

import java.util.Date;

/**
 * Created by CUBESOFT on 18.09.2017.
 */

public class TextUtils {
    public static CharSequence formatItems(int itemCount) {
        return String.format("%d Items", itemCount);
    }

    public static CharSequence formatTimestampRelative(Date date) {
        return DateUtils.getRelativeTimeSpanString(date.getTime());
        //return DateUtils.getRelativeTimeSpanString(date.getTime(), new Date().getTime(), DateUtils.MINUTE_IN_MILLIS, DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE );
    }

    public static boolean isEmpty(String text) {
        return android.text.TextUtils.isEmpty(text);
    }
}
