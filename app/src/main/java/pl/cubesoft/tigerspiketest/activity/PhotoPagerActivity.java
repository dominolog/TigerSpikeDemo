package pl.cubesoft.tigerspiketest.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

import java.util.Collections;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;


import pl.cubesoft.tigerspiketest.ImageLoader;
import pl.cubesoft.tigerspiketest.R;
import pl.cubesoft.tigerspiketest.data.PhotoSetKey;
import pl.cubesoft.tigerspiketest.data.PhotoSetProvider;
import pl.cubesoft.tigerspiketest.data.Provider;
import pl.cubesoft.tigerspiketest.data.UsersProvider;
import pl.cubesoft.tigerspiketest.data.model.Photo;
import pl.cubesoft.tigerspiketest.data.model.PhotoSet;
import pl.cubesoft.tigerspiketest.data.model.User;
import pl.cubesoft.tigerspiketest.fragment.PhotoPagerFragment;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;
import pl.cubesoft.tigerspiketest.utils.DeviceUtils;
import pl.cubesoft.tigerspiketest.utils.TextUtils;


public class PhotoPagerActivity extends BaseActivity implements PhotoPagerFragment.OnPhotoPagerFragmentInteractionListener {

    private static final String STATE_CURRENT_PAGE_POSITION = "currentPage";
    private static final String ARG_POSITION = "position";
    private static final String ARG_PHOTOSET_KEY = "key";
    private static final String ARG_PHOTOSET_TITLE = "title";
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.container)
    View container;


    @Inject
    ImageLoader imageLoader;

    @Inject
    Provider provider;

    private int currentPagePosition;
    private PhotoPagerFragment fragment;
    private ActionBar actionBar;
    private PhotoSetKey photoSetKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_pager);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        getMyApplication().getAppComponent().inject(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //ViewCompat.setTransitionName(photo, "photo");

        // Set up activity to go full screen
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Enable some additional newer visibility and ActionBar features to create a more
        // immersive photo viewing experience
        if (DeviceUtils.hasHoneycomb()) {


            // Hide title text and set home as up
            //actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);

            // Hide and show the ActionBar as the visibility changes
            container.setOnSystemUiVisibilityChangeListener(
                    vis -> {
                        if ((vis & View.SYSTEM_UI_FLAG_LOW_PROFILE) != 0) {
                            actionBar.hide();
                            fragment.showBottomLayout(false);
                        } else {
                            actionBar.show();
                            fragment.showBottomLayout(true);
                        }
                    });

            // Start low profile mode and hide ActionBar
            container.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
            actionBar.hide();
        }

        photoSetKey = getIntent().getParcelableExtra(ARG_PHOTOSET_KEY);
        currentPagePosition = getIntent().getIntExtra(ARG_POSITION, 0);
        final String photoSetTitle = getIntent().getStringExtra(ARG_PHOTOSET_TITLE);
        if ( photoSetTitle != null ) {
            getSupportActionBar().setTitle(photoSetTitle);
        }

        //ViewCompat.setTransitionName(toolbarLayout.get, "name");

        //ActivityCompat.postponeEnterTransition(this);

        if (savedInstanceState != null) {
            currentPagePosition = savedInstanceState.getInt(STATE_CURRENT_PAGE_POSITION);
            fragment= (PhotoPagerFragment)getSupportFragmentManager().findFragmentById(R.id.container);
        } else {
            fragment = PhotoPagerFragment.createInstance(photoSetKey, currentPagePosition);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, fragment)
                    .commit();
        }


    }
    @Override
    public void onDestroy() {
        super.onDestroy();

        container.setOnSystemUiVisibilityChangeListener(null);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        //noinspection SimplifiableIfStatement
        if (item.getItemId() == android.R.id.home) {
            //NavUtils.navigateUpFromSameTask(this);
            //ActivityCompat.finishAfterTransition(this);
            supportFinishAfterTransition();

        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onPagerItemClick(int position, Uri item) {
        final int vis = container.getSystemUiVisibility();
        if ((vis & View.SYSTEM_UI_FLAG_LOW_PROFILE) != 0) {
            container.setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
        } else {
            container.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
        }
    }

    @Override
    public void onPagerItemSelected(int position, int totalItems) {
        actionBar.setSubtitle(String.format("%d/%d", position + 1, totalItems));
    }

    @Override
    public PhotoSetProvider getPhotoSetProvider() {
        return provider.getPhotoSetProvider();
    }

    @Override
    public UsersProvider getUsersProvider() {
        return provider.getUsersProvider();
    }


    @Override
    public void onPhotoItemUserClick(User user) {
        ContextCompat.startActivity(this, UserDetailActivity.createIntent(this, user.getId(), null), null);
    }

    @Override
    public void onPagerItemPhotoTagClick(Photo item, CharSequence text) {
        ContextCompat.startActivity(this, SearchActivity.createIntent(this, null, null, Collections.singletonList(text.toString())), null);
    }

    @Override
    public void onPhotoSetLoaded(PhotoSetProvider.PhotoSetType type, PhotoSet photoSet) {
        actionBar.setTitle(photoSet.getTitle());
        actionBar.setSubtitle(TextUtils.formatItems(photoSet.getTotalItemCount()));
    }

    @Override
    public void onPhotoItemCommentsClick(Photo item) {
        ContextCompat.startActivity(this, CommentsActivity.createIntent(this, item.getCommentPoolId()), null);
    }

    @Override
    public void onPagerItemPhotoPlayClick(Photo item) {

        subscribe(getPhotoSetProvider().getVideoUrl(item.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(() -> showProgress(true, R.string.loading))
                .subscribe(url -> {
                    super.playVideo(item.getUrl());


                }, throwable -> {
                    Timber.d(throwable, "Error while obtaining video playback url!");
                    showProgress(false, -1);
                    showSnackBar(R.id.coordinator, getString(R.string.error_while_getting_video_url));
                }, () -> {

                    showProgress(false, -1);

                }));


    }


    public static Intent createIntent(Context context, PhotoSetKey photoSetKey, int position, @Nullable String photoSetTitle) {
        return new Intent(context, PhotoPagerActivity.class)
                .putExtra(ARG_PHOTOSET_KEY, photoSetKey)
                .putExtra(ARG_POSITION, position)
                .putExtra(ARG_PHOTOSET_TITLE, photoSetTitle);
    }


}
