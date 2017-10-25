package pl.cubesoft.tigerspiketest.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


import pl.cubesoft.tigerspiketest.ImageLoader;
import pl.cubesoft.tigerspiketest.R;
import pl.cubesoft.tigerspiketest.adapter.UserDetailAdapter;
import pl.cubesoft.tigerspiketest.data.GroupProvider;
import pl.cubesoft.tigerspiketest.data.PhotoSetKey;
import pl.cubesoft.tigerspiketest.data.PhotoSetProvider;
import pl.cubesoft.tigerspiketest.data.Provider;
import pl.cubesoft.tigerspiketest.data.UserSetKey;
import pl.cubesoft.tigerspiketest.data.UsersProvider;
import pl.cubesoft.tigerspiketest.data.model.Group;
import pl.cubesoft.tigerspiketest.data.model.GroupElement;
import pl.cubesoft.tigerspiketest.data.model.Photo;
import pl.cubesoft.tigerspiketest.data.model.PhotoSet;
import pl.cubesoft.tigerspiketest.data.model.User;
import pl.cubesoft.tigerspiketest.data.model.UserSet;
import pl.cubesoft.tigerspiketest.fragment.GroupFragment;
import pl.cubesoft.tigerspiketest.fragment.PhotoSetFragment;
import pl.cubesoft.tigerspiketest.fragment.UsersFragment;
import pl.cubesoft.tigerspiketest.utils.TextUtils;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

public class UserDetailActivity extends BaseActivity implements PhotoSetFragment.OnPhotoSetFragmentInteractionListener,
        GroupFragment.OnGroupFragmentInteractionListener,
        UsersFragment.OnUsersFragmentInteractionListener {

    private static final String ARG_USER_ID = "userId";
    private static final String ARG_USERNAME = "username";
    private static final String STATE_CHECKED_PAGE_ITEM_ID = "pageItemId";

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.toolbar_layout)
    CollapsingToolbarLayout toolbarLayout;

    @BindView(R.id.tabs)
    TabLayout tabs;

    @BindView(R.id.main_pager)
    ViewPager mainPager;

    @BindView(R.id.photo)
    ImageView photo;

    @BindView(R.id.background)
    ImageView background;




    @BindView(R.id.user_details_layout)
    View userDetailsLayout;

    @BindView(R.id.username)
    TextView theUsername;


    @BindView(R.id.user_description)
    TextView userDescription;


    @BindView(R.id.num_photos_layout)
    View numPhotosLayout;
    @BindView(R.id.num_photos)
    TextView numPhotos;

    @BindView(R.id.num_views_layout)
    View numViewsLayout;
    @BindView(R.id.num_views)
    TextView numViews;

    @BindView(R.id.num_followers_layout)
    View numFollowersLayout;
    @BindView(R.id.num_followers)
    TextView numFollowers;

    @BindView(R.id.num_following_layout)
    View numFollowingLayout;
    @BindView(R.id.num_following)
    TextView numFollowing;

    @BindView(R.id.location_layout)
    View locationLayout;
    @BindView(R.id.location)
    TextView location;

    @BindView(R.id.social_web)
    ImageView socialWeb;

    @BindView(R.id.social_facebook)
    ImageView socialFacebook;

    @BindView(R.id.social_twitter)
    ImageView socialTwitter;

    @BindView(R.id.social_instagram)
    ImageView socialInstagram;

    @BindView(R.id.social_mail)
    ImageView socialMail;

    @BindView(R.id.social_pinterest)
    ImageView socialPinterest;


    @Inject
    ImageLoader imageLoader;


    @Inject
    Provider provider;


    private String userId;


    private Observable<User> observableForData;
    private User user;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getMyApplication().getAppComponent().inject(this);
        //AndroidInjection.inject(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tabs.setupWithViewPager(mainPager);

        userDetailsLayout.setVisibility(View.GONE);
        toolbarLayout.setExpandedTitleGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM);
        toolbarLayout.setExpandedTitleColor(ContextCompat.getColor(this, R.color.transparent)/*Color.argb(220, 255, 255, 255)*/);
        toolbarLayout.setCollapsedTitleTextColor(ContextCompat.getColor(this, R.color.black));

        userId = getIntent().getStringExtra(ARG_USER_ID);
        username = getIntent().getStringExtra(ARG_USERNAME);
        observableForData = provider.getUsersProvider().getUser(false, userId, username, null).cache();

        if (userId != null) {

            UserDetailAdapter adapter = createAdapter(userId);
            mainPager.setAdapter(adapter);
        }
        //ViewCompat.setTransitionName(toolbarLayout.get, "name");

        ActivityCompat.postponeEnterTransition(this);


        refreshData(true);
    }

    private UserDetailAdapter createAdapter(String userId) {
        return new UserDetailAdapter(getSupportFragmentManager(),
                getResources(), provider.getUserDetailsViewTypes(), userId);
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

    private void refreshData(boolean force) {
        subscribe(observableForData
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(() -> setRefreshing(true))
                .subscribe(user -> {

                            setUser(user);
                        },
                        throwable -> {
                            Timber.e(throwable, "Error loading user!");
                            setRefreshing(false);
                            showSnackBar("Error loading user!");
                        }, () -> {
                            setRefreshing(false);
                        }));
    }


    public static Intent createIntent(Context context, String userId, String username) {
        return new Intent(context, UserDetailActivity.class)
                .putExtra(ARG_USER_ID, userId)
                .putExtra(ARG_USERNAME, username);
    }

    public void setRefreshing(boolean refreshing) {

    }

    public void setUser(User user) {
        this.user = user;

        if (mainPager.getAdapter() == null ) {
            this.userId = user.getId();
            mainPager.setAdapter(createAdapter(user.getId()));
        }

        toolbarLayout.setTitle(user.getDisplayName());

        setDescriptionLayout();

        //play.setVisibility(View.VISIBLE);



        ViewCompat.setTransitionName(photo, user.getPhotoUrl());

        final String photoUrl = user.getPhotoUrl();
        if (photoUrl != null) {
            imageLoader.load(Uri.parse(photoUrl),
                    photo, true, true, ImageLoader.Transform.CIRCLE, "", new ImageLoader.Listener() {

                        @Override
                        public void onImageLoadSuccess(Bitmap bitmap) {
                            ActivityCompat.startPostponedEnterTransition(UserDetailActivity.this);
                        }

                        @Override
                        public void onImageLoadError() {
                            ActivityCompat.startPostponedEnterTransition(UserDetailActivity.this);
                        }
                    });
        }
        final String coverPhoto = user.getCoverPhotoUrl();
        if (coverPhoto != null) {
            imageLoader.load(Uri.parse(coverPhoto),
                    background, true, true, ImageLoader.Transform.NONE, "", new ImageLoader.Listener() {

                        @Override
                        public void onImageLoadSuccess(Bitmap bitmap) {
                            //ActivityCompat.startPostponedEnterTransition(UserDetailActivity.this);
                        }

                        @Override
                        public void onImageLoadError() {
                            //ActivityCompat.startPostponedEnterTransition(UserDetailActivity.this);
                        }
                    });
        }
        ActivityCompat.startPostponedEnterTransition(this);

        supportInvalidateOptionsMenu();
    }

    private void setDescriptionLayout() {
        userDetailsLayout.setVisibility(View.VISIBLE);

        theUsername.setText(user.getDisplayName());
        if (!TextUtils.isEmpty(user.getDescription())) {
            userDescription.setText(Html.fromHtml(user.getDescription()));
            userDescription.setVisibility(View.VISIBLE);
        } else {
            userDescription.setVisibility(View.GONE);
        }

        if ( user.getNumPhotos() != null ) {
            numPhotos.setText(getString(R.string.d_photos, user.getNumPhotos()));
            numPhotosLayout.setVisibility(View.VISIBLE);
        } else {
            numPhotosLayout.setVisibility(View.GONE);
        }

        if ( user.getNumFollowers() != null ) {
            numFollowers.setText(getString(R.string.d_followers, user.getNumFollowers()));
            numFollowersLayout.setVisibility(View.VISIBLE);
        } else {
            numFollowersLayout.setVisibility(View.GONE);
        }

        if ( user.getNumFollowing() != null ) {
            numFollowing.setText(getString(R.string.d_following, user.getNumFollowing()));
            numFollowingLayout.setVisibility(View.VISIBLE);
        } else {
            numFollowingLayout.setVisibility(View.GONE);
        }

        if ( user.getNumViews() != null ) {
            numViews.setText(getString(R.string.d_views, user.getNumViews()));
            numViewsLayout.setVisibility(View.VISIBLE);
        } else {
            numViewsLayout.setVisibility(View.GONE);
        }

        if ( !TextUtils.isEmpty(user.getLocation()) ) {
            location.setText(user.getLocation());
            locationLayout.setVisibility(View.VISIBLE);
        } else {
            locationLayout.setVisibility(View.GONE);
        }


        setSocialLayout(user.getContacts());
    }




    @OnClick(R.id.num_followers_layout)
    public void onFollowersClick() {
        ContextCompat.startActivity(this, UsersActivity.createIntent(this, UsersProvider.UserSetType.FOLLOWERS, userId), null);
    }

    @OnClick(R.id.num_following_layout)
    public void onFollowingClick() {
        ContextCompat.startActivity(this, UsersActivity.createIntent(this, UsersProvider.UserSetType.FRIENDS, userId), null);
    }

    @Override
    public Observable<PhotoSet> getPhotoSet(boolean force, PhotoSetProvider.PhotoSetType type) {

        return provider.getPhotoSetProvider().getPhotoSet(force, PhotoSetKey.create(type, userId, null, null, null));
    }

    @Override
    public void onPhotoSetItemClick(int position, View view, Photo item, PhotoSet photoSet) {

        ContextCompat.startActivity(this, PhotoPagerActivity.createIntent(this, photoSet.getKey(), position, photoSet.getTitle()), null);
    }


    @Override
    public void onPhotoSetLoaded(PhotoSet photoSet) {

    }

    @Override
    public boolean canDeleteFromPhotoSet(PhotoSet photoSet) {
        return provider.canDeleteFromPhotoSet(photoSet);
    }

    @Override
    public void onGroupItemClick(int position, View view, GroupElement item, GroupProvider.GroupType groupType) {

        PhotoSetProvider.PhotoSetType photoSetType = null;
        switch (groupType) {
            case USER_SETS:
                photoSetType = PhotoSetProvider.PhotoSetType.SET_ID;
                break;
            case USER_GALLERIES:
                photoSetType = PhotoSetProvider.PhotoSetType.GALLERY_ID;
                break;
            default:
                photoSetType = PhotoSetProvider.PhotoSetType.SET_ID;
                break;

        }
        PhotoSetKey key = PhotoSetKey.create(photoSetType, userId, item.getId(), null, null);
        ContextCompat.startActivity(this, PhotoSetActivity.createIntent(this, key, item.getTitle()), null);
    }

    @Override
    public Observable<Group> getGroup(GroupProvider.GroupType type) {
        return provider.getGroupProvider().getGroup(type, userId, null);
    }

    @Override
    public void onGroupItemOptionsClick(int position, GroupElement item, GroupProvider.GroupType type) {

    }

    @Override
    public void onGroupLoaded(Group group) {

    }


    @Override
    public Observable<UserSet> getUsers(UsersProvider.UserSetType type) {
        return provider.getUsersProvider().getUserSet(false, UserSetKey.create(type, userId, null, null));
    }

    @Override
    public Observable<UserSet> loadMoreUsers(UserSet userSet, int page) {
        return provider.getUsersProvider().loadMore(userSet, page);
    }

    @Override
    public void onUserItemClick(int position, View view, User item) {
        ContextCompat.startActivity(this, UserDetailActivity.createIntent(this, item.getId(), null), null);
    }

    @Override
    public void onUsersLoaded(UsersProvider.UserSetType type, UserSet userSet) {

    }

    public void setSocialLayout(Map<User.SocialNetwork,String> contacts) {
        boolean hasFacebook = false;
        boolean hasTwitter = false;
        boolean hasPinterest = false;
        boolean hasInstagram = false;
        boolean hasMail = false;
        boolean hasWebsite = false;
        if (contacts != null) {

            for ( final Map.Entry<User.SocialNetwork, String> contact : contacts.entrySet()) {
                switch (contact.getKey()) {
                    case FACEBOOK:
                        socialFacebook.setVisibility(View.VISIBLE);
                        socialFacebook.setOnClickListener(view -> showUrl(contact.getValue()));
                        hasFacebook = true;
                        break;

                    case TWITTER:
                        socialTwitter.setVisibility(View.VISIBLE);
                        socialTwitter.setOnClickListener(view -> showUrl(contact.getValue()));
                        hasTwitter = true;
                        break;

                    case PINTEREST:
                        socialPinterest.setVisibility(View.VISIBLE);
                        socialPinterest.setOnClickListener(view -> showUrl(contact.getValue()));
                        hasPinterest = true;
                        break;

                    case INSTAGRAM:
                        socialInstagram.setVisibility(View.VISIBLE);
                        socialInstagram.setOnClickListener(view -> showUrl(contact.getValue()));
                        hasInstagram = true;
                        break;

                    case MAIL:
                        socialMail.setVisibility(View.VISIBLE);
                        socialMail.setOnClickListener(view -> showUrl(contact.getValue()));
                        hasMail = true;
                        break;

                    case WEBSITE:
                        socialWeb.setVisibility(View.VISIBLE);
                        socialWeb.setOnClickListener(view -> showUrl(contact.getValue()));
                        hasWebsite = true;
                        break;
                }
            }
        }

        socialFacebook.setVisibility(hasFacebook ? View.VISIBLE : View.GONE);
        socialTwitter.setVisibility(hasTwitter ? View.VISIBLE : View.GONE);
        socialPinterest.setVisibility(hasPinterest ? View.VISIBLE : View.GONE);
        socialInstagram.setVisibility(hasInstagram ? View.VISIBLE : View.GONE);
        socialMail.setVisibility(hasMail ? View.VISIBLE : View.GONE);
        socialWeb.setVisibility(hasWebsite ? View.VISIBLE : View.GONE);
    }


}
