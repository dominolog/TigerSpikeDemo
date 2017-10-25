package pl.cubesoft.tigerspiketest.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ReplacementSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.cubesoft.tigerspiketest.R;
import pl.cubesoft.tigerspiketest.adapter.NameValueAdapter;
import pl.cubesoft.tigerspiketest.data.Provider;
import pl.cubesoft.tigerspiketest.data.model.Photo;


import pl.cubesoft.tigerspiketest.utils.CollectionUtils;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

public class PhotoInfoActivity extends BaseActivity {

    private static final String ARG_PHOTO_ID = "photoId";
    private static final String STATE_CHECKED_PAGE_ITEM_ID = "pageItemId";
    @BindView(R.id.swipe_view)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.info_layout)
    View info_layout;

    @BindView(R.id.title_header)
    View titleHeader;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.description_header)
    View descriptionHeader;
    @BindView(R.id.description)
    TextView description;


    @BindView(R.id.date_created_header)
    View dateCreatedHeader;
    @BindView(R.id.date_created)
    TextView dateCreated;


    @BindView(R.id.recycler_exif)
    RecyclerView recyclerExif;


    @BindView(R.id.num_views)
    TextView numViews;


    @BindView(R.id.num_favorites)
    TextView numFavorites;


    @BindView(R.id.num_views_icon)
    View numViewsIcon;

    @BindView(R.id.num_favorites_icon)
    ImageView numFavoritesIcon;

    @BindView(R.id.num_comments_layout)
    View numCommentsLayout;

    @BindView(R.id.num_comments)
    TextView numComments;


    @BindView(R.id.rating)
    RatingBar rating;

    @BindView(R.id.exif_card)
    View exifCard;

    @BindView(R.id.tags_card)
    View tagsCard;

    @BindView(R.id.tags)
    TextView tags;

    @BindView(R.id.map_card)
    View mapCard;

    @BindView(R.id.location_name)
    TextView locationName;

    @Inject
    Provider provider;


    private String photoId;


    private Observable<Photo> observableForData;
    private NameValueAdapter exifAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_info);
        ButterKnife.bind(this);
        getMyApplication().getAppComponent().inject(this);
        //AndroidInjection.inject(this);

        recyclerExif.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerExif.setAdapter(exifAdapter = new NameValueAdapter());

        photoId = getIntent().getStringExtra(ARG_PHOTO_ID);
        observableForData = provider.getPhotoSetProvider().getPhoto(false, photoId).share();


        //ViewCompat.setTransitionName(toolbarLayout.get, "name");

        info_layout.setVisibility(View.GONE);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            refreshData(true);
        });


        refreshData(true);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //outState.putInt(STATE_CHECKED_PAGE_ITEM_ID, selectedPageItemId);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.activity_tour_detail, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //MenuItem itemDownload = menu.findItem(R.id.menu_download);
        //itemDownload.setVisible(this.tour != null);
        return super.onPrepareOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        //noinspection SimplifiableIfStatement
        switch (item.getItemId()) {
            case android.R.id.home:
                //NavUtils.navigateUpFromSameTask(this);
                //ActivityCompat.finishAfterTransition(this);
                supportFinishAfterTransition();
                break;


        }

        return super.onOptionsItemSelected(item);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDialogEvent(String event) {

    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @OnClick(R.id.ok)
    public void onOk() {
        finish();
    }

    private void refreshData(boolean force) {
        subscribe(observableForData
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(() -> setRefreshing(true))
                .subscribe(photo -> {


                            setPhoto(photo);
                        },
                        throwable -> {
                            Timber.e("Error loading user!");
                            setRefreshing(false);
                            showSnackBar("Error loading user!");
                        }, () -> {
                            setRefreshing(false);
                        }));
    }


    public static Intent createIntent(Context context, String photoId) {
        return new Intent(context, PhotoInfoActivity.class)
                .putExtra(ARG_PHOTO_ID, photoId);
    }

    public void setRefreshing(boolean refreshing) {
        swipeRefreshLayout.setRefreshing(refreshing);
    }

    public void setPhoto(Photo photo) {

        if (!TextUtils.isEmpty(photo.getTitle())) {
            title.setText(photo.getTitle());
            title.setVisibility(View.VISIBLE);
            titleHeader.setVisibility(View.VISIBLE);
        } else {
            title.setVisibility(View.GONE);
            titleHeader.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(photo.getDescription())) {
            description.setText(photo.getDescription() != null ? Html.fromHtml(photo.getDescription()) : null);
            description.setVisibility(View.VISIBLE);
            descriptionHeader.setVisibility(View.VISIBLE);
        } else {
            description.setVisibility(View.GONE);
            descriptionHeader.setVisibility(View.GONE);
        }


        if (photo.getDateCreated() != null) {
            dateCreated.setText(pl.cubesoft.tigerspiketest.utils.TextUtils.formatTimestampRelative(photo.getDateCreated()));
            dateCreated.setVisibility(View.VISIBLE);
            dateCreatedHeader.setVisibility(View.VISIBLE);
        } else {
            dateCreated.setVisibility(View.GONE);
            dateCreatedHeader.setVisibility(View.GONE);
        }


        if ( !CollectionUtils.isEmpty(photo.getExifTags())) {
            exifAdapter.setData(photo.getExifTags());
            exifCard.setVisibility(View.VISIBLE);
        } else {
            exifCard.setVisibility(View.GONE);
        }


        if (photo.getNumViews() != null) {
            numViews.setText(Integer.toString(photo.getNumViews()));
            numViews.setVisibility(View.VISIBLE);
            numViewsIcon.setVisibility(View.VISIBLE);
        } else {
            numViews.setVisibility(View.GONE);
            numViewsIcon.setVisibility(View.GONE);
        }

        if (photo.getNumFavorites() != null) {
            numFavorites.setText(Integer.toString(photo.getNumFavorites()));
            numFavorites.setVisibility(View.VISIBLE);
            numFavoritesIcon.setVisibility(View.VISIBLE);


        } else {
            numFavorites.setVisibility(View.GONE);
            numFavoritesIcon.setVisibility(View.GONE);

        }

        if (photo.isFavorite() != null) {
            numFavoritesIcon.setImageResource(photo.isFavorite() ? R.drawable.ic_favorite_black_24dp : R.drawable.ic_favorite_border_black_24dp);


            numFavoritesIcon.setOnClickListener(view1 -> {
                //onPhotoFavoriteClick(position, item);
            });

        } else {
            numFavoritesIcon.setOnClickListener(null);
        }


        if (photo.getNumComments() != null) {
            numComments.setText(Integer.toString(photo.getNumComments()));
            numCommentsLayout.setVisibility(View.VISIBLE);

            numCommentsLayout.setOnClickListener(view1 -> {
                //listener.onPhotoItemCommentsClick(item);
            });
        } else {
            numCommentsLayout.setVisibility(View.GONE);

        }

        if (photo.getRating() != null) {
            rating.setMax(photo.getMaxRating());
            rating.setRating(photo.getRating());
            rating.setVisibility(View.VISIBLE);
        } else {
            rating.setVisibility(View.GONE);
        }

        if (!CollectionUtils.isEmpty(photo.getTags())) {
            setTextTags(tags, photo.getTags(),
                    ContextCompat.getColor(this, R.color.colorAccent),
                    ContextCompat.getColor(this, R.color.white));
            tagsCard.setVisibility(View.VISIBLE);
        } else {
            tagsCard.setVisibility(View.GONE);
        }


        Photo.Location location = photo.getLocation();
        if (location != null) {
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            GoogleMapOptions options = new GoogleMapOptions()
                    .useViewLifecycleInFragment(true)
                    .liteMode(true)
                    .camera(CameraPosition.fromLatLngZoom(latLng, 10));
            SupportMapFragment mapFragment = null;
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.map_container, mapFragment = SupportMapFragment.newInstance(options))
                    .commit();

            mapFragment.getMapAsync(googleMap -> {
                googleMap.addMarker(new MarkerOptions().position(latLng));
            });

            mapCard.setVisibility(View.VISIBLE);
            if (!TextUtils.isEmpty(location.getName())) {
                locationName.setText(location.getName());
                locationName.setVisibility(View.VISIBLE);
            } else {
                locationName.setVisibility(View.GONE);
            }
        } else {
            mapCard.setVisibility(View.GONE);
        }
        info_layout.setVisibility(View.VISIBLE);
    }

    private void setTextTags(TextView textView, List<String> tags, int tagBackgroundColor, int tagTextColor) {

        class RoundedBackgroundSpan extends ReplacementSpan
        {
            private final int mPadding = 5;
            private final int mRadius = 30;
            private int mBackgroundColor;
            private int mTextColor;

            public RoundedBackgroundSpan(int backgroundColor, int textColor) {
                super();
                mBackgroundColor = backgroundColor;
                mTextColor = textColor;
            }

            @Override
            public int getSize(Paint paint, CharSequence text, int start, int end, Paint.FontMetricsInt fm) {
                return (int) (mPadding + paint.measureText(text.subSequence(start, end).toString()) + mPadding);
            }

            @Override
            public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, Paint paint)
            {
                float width = paint.measureText(text.subSequence(start, end).toString());
                RectF rect = new RectF(x, top+mPadding, x + width + 2*mPadding, bottom);
                paint.setColor(mBackgroundColor);
                canvas.drawRoundRect(rect, mRadius, mRadius, paint);
                paint.setColor(mTextColor);
                canvas.drawText(text, start, end, x+mPadding, y, paint);
            }
        }
        textView.setTextColor(tagTextColor);
        SpannableStringBuilder builder = new SpannableStringBuilder();
        for (String tag : tags) {
            Spannable word = new SpannableString(" " + tag + " ");

            word.setSpan(new RoundedBackgroundSpan(tagBackgroundColor, tagTextColor), 0, word.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            builder.append(word);
            builder.append(" ");

        }
        textView.setText(builder);
    }


}
