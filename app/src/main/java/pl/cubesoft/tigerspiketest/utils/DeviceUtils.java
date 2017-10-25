package pl.cubesoft.tigerspiketest.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Point;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.os.Build;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.List;

public class DeviceUtils {
    private static final String TAG = DeviceUtils.class.getName();
    static List<Size> resolutions = null;

    public static String getPictureStorageDir() {
        return Environment.DIRECTORY_DCIM;
    }

    public static List<Size> getSupportedCameraResolutions() {

        Camera camera = null;
        try {
            if (resolutions == null) {
                camera = Camera.open();
                resolutions = camera != null ? camera.getParameters().getSupportedPictureSizes() : new ArrayList<Size>();
            }
        } catch (Exception ex) {
            resolutions = new ArrayList<Size>();
            Log.e(TAG, "[getSupportedCameraResolutions] ERROR :", ex);
        } finally {
            if (camera != null) {
                camera.release();
            }
        }
        return resolutions;
    }

    public static boolean isCameraDevicePresent(Context context) {
        final PackageManager pm = context.getPackageManager();

        //It would be safer to use the constant PackageManager.FEATURE_CAMERA_FRONT
        //but since it is not defined for Android 2.2, I substituted the literal value
        final boolean frontCam = pm.hasSystemFeature("android.hardware.camera.front");
        final boolean rearCam = pm.hasSystemFeature(PackageManager.FEATURE_CAMERA);

        return frontCam || rearCam;
    }

    public static Point getScreenSize(Context context) {
        final WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        final Display display = windowManager.getDefaultDisplay();

        final DisplayMetrics metrics = new DisplayMetrics();
        /*if ( android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1 ) {
			display.getRealMetrics(metrics);
		} else*/
        {
            display.getMetrics(metrics);
        }


        return new Point(metrics.widthPixels, metrics.heightPixels);
    }

    public static long getDeviceAvailableMemory(Context context) {
        final MemoryInfo mi = new MemoryInfo();
        final ActivityManager activityManager = (ActivityManager) context.getSystemService(Activity.ACTIVITY_SERVICE);
        activityManager.getMemoryInfo(mi);
        return mi.availMem;
    }

    public static android.os.Debug.MemoryInfo getProcessMemoryInfo(Context context) {
        final ActivityManager activityManager = (ActivityManager) context.getSystemService(Activity.ACTIVITY_SERVICE);
        final android.os.Debug.MemoryInfo[] pmi = activityManager.getProcessMemoryInfo(new int[]{android.os.Process.myPid()});
        return pmi != null && pmi.length > 0 ? pmi[0] : null;
    }


    public static boolean hasHoneycomb() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
    }

    public enum Orientation {
        PORTRAIT,
        UNDEFINED,
        LANDSCAPE
    }

    public static Orientation getDeviceScreenOrientation(Context context) {

        switch (context.getResources().getConfiguration().orientation) {
            case Configuration.ORIENTATION_PORTRAIT:
                return Orientation.PORTRAIT;
            case Configuration.ORIENTATION_LANDSCAPE:
                return Orientation.LANDSCAPE;
            default:
                return Orientation.UNDEFINED;
        }

    }

    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }
}
