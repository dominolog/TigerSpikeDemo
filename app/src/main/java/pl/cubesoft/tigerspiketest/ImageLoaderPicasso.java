package pl.cubesoft.tigerspiketest;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.ImageView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.squareup.picasso.Target;
import com.squareup.picasso.Transformation;

import okhttp3.OkHttpClient;


public class ImageLoaderPicasso implements ImageLoader {

    private final Picasso picasso;

    private class CircleTransform implements Transformation {
        @Override
        public Bitmap transform(Bitmap source) {
            int size = Math.min(source.getWidth(), source.getHeight());

            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;

            Bitmap squaredBitmap = Bitmap.createBitmap(source, x, y, size, size);
            if (squaredBitmap != source) {
                source.recycle();
            }

            Bitmap bitmap = Bitmap.createBitmap(size, size, source.getConfig());

            Canvas canvas = new Canvas(bitmap);
            Paint paint = new Paint();
            BitmapShader shader = new BitmapShader(squaredBitmap, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
            paint.setShader(shader);
            paint.setAntiAlias(true);

            float r = size / 2f;
            canvas.drawCircle(r, r, r, paint);

            squaredBitmap.recycle();
            return bitmap;
        }

        @Override
        public String key() {
            return "circle";
        }
    }

    private final Context context;
    private final CircleTransform circleTransform = new CircleTransform();

    public ImageLoaderPicasso(Context context, OkHttpClient httpClient) {
        this.context = context;
        picasso = new Picasso.Builder(context)
                .downloader(new OkHttp3Downloader(httpClient))
                .indicatorsEnabled(BuildConfig.DEBUG)
                .loggingEnabled(BuildConfig.DEBUG)
                .listener((picasso, uri, exception) -> {
                }).build();


    }

    @Override
    public void load(Uri uri, Transform transform, Object tag, Listener listener) {
        RequestCreator rq = picasso.load(uri);
        if (transform == Transform.CIRCLE) {
            rq = rq.transform(circleTransform);
        }

        if ( tag != null ) {
            rq.tag(tag);
        }
        rq.into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                listener.onImageLoadSuccess(bitmap);
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
                listener.onImageLoadError();
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        });

    }

    @Override
    public void load(Uri uri, ImageView imageView, boolean fit, boolean noPlaceholder, Transform transform, Object tag) {
        load(uri, imageView, fit, noPlaceholder, transform, tag, null);
    }


    @Override
    public void load(Uri uri, ImageView imageView, boolean fit, boolean noPlaceholder, Transform transform, Object tag, Listener listener) {
        RequestCreator rc = picasso
                .load(uri)
                //.fit()
                .tag(tag);

        if (noPlaceholder) {
            rc = rc.noPlaceholder();
        }
        if (fit) {
            rc = rc.fit().centerInside();
        }
        if (transform == Transform.CIRCLE) {
            rc = rc.transform(circleTransform);
        }

        if (listener != null) {
            rc.into(imageView, new Callback() {
                @Override
                public void onSuccess() {
                    listener.onImageLoadSuccess(null);
                }

                @Override
                public void onError() {
                    listener.onImageLoadError();
                }
            });
        } else {
            rc.into(imageView);
        }
    }

    @Override
    public void load(Uri uri, ImageView imageView, Transform transform, Object imageLoadTag) {

        load(uri, imageView, true, false, transform, imageLoadTag, null);


    }


    @Override
    public void pauseTag(Object tag) {
        picasso.pauseTag(tag);
    }

    @Override
    public void resumeTag(Object tag) {
        picasso.resumeTag(tag);
    }

    @Override
    public void cancelTag(Object tag) {
        picasso.cancelTag(tag);
    }

    @Override
    public void cancelRequest(ImageView imageView) {
        picasso.cancelRequest(imageView);
    }


}
