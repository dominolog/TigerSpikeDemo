package pl.cubesoft.tigerspiketest.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collection;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.cubesoft.tigerspiketest.ImageLoader;
import pl.cubesoft.tigerspiketest.R;
import pl.cubesoft.tigerspiketest.adapter.SearchActivityAdapter;
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
import pl.cubesoft.tigerspiketest.utils.CollectionUtils;
import rx.Observable;

public class SearchActivity extends BaseActivity implements PhotoSetFragment.OnPhotoSetFragmentInteractionListener,
        GroupFragment.OnGroupFragmentInteractionListener,
        UsersFragment.OnUsersFragmentInteractionListener {

    private static final String STATE_CHECKED_PAGE_ITEM_ID = "pageItemId";
    private static final String ARG_SEARCH_TEXT = "searchText";
    private static final String ARG_SEARCH_TAGS = "searchTags";
    private static final String ARG_USER_ID = "userId";

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.tabs)
    TabLayout tabs;

    @BindView(R.id.main_pager)
    ViewPager mainPager;

    @Inject
    ImageLoader imageLoader;


    @Inject
    Provider provider;
    private String searchText;
    private ArrayList<String> searchTags;
    private String userId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getMyApplication().getAppComponent().inject(this);
        //AndroidInjection.inject(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tabs.setupWithViewPager(mainPager);

        userId = getIntent().getStringExtra(ARG_USER_ID);
        searchText = getIntent().getStringExtra(ARG_SEARCH_TEXT);
        if ( getIntent().hasExtra(ARG_SEARCH_TAGS )) {
            searchTags = (ArrayList<String>) getIntent().getSerializableExtra(ARG_SEARCH_TAGS);
        }
        
        String subtitle = "";
        if ( searchText != null ) {
            subtitle += searchText;
        }
        if (!CollectionUtils.isEmpty(searchTags)) {
            subtitle += TextUtils.join(", ", searchTags);
        }

        getSupportActionBar().setSubtitle(subtitle);
        
        

        mainPager.setAdapter(new SearchActivityAdapter(getSupportFragmentManager(), getResources(), provider.getSearchViewTypes(), searchText));
        //ViewCompat.setTransitionName(toolbarLayout.get, "name");


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



    public static Intent createIntent(Context context, String userId, String searchText, Collection<String> tags) {
        Intent intent = new Intent(context, SearchActivity.class);
        if ( !TextUtils.isEmpty(userId)) {
            intent.putExtra(ARG_USER_ID, userId);
        }
        if ( !TextUtils.isEmpty(searchText)) {
            intent.putExtra(ARG_SEARCH_TEXT, searchText);
        }
        if ( !CollectionUtils.isEmpty(tags)) {
            intent.putExtra(ARG_SEARCH_TAGS, new ArrayList<>(tags));
        }
        return intent;
    }

    public void setRefreshing(boolean refreshing) {

    }


    @Override
    public Observable<PhotoSet> getPhotoSet(boolean force, PhotoSetProvider.PhotoSetType type) {
        return provider.getPhotoSetProvider().getPhotoSet(force, PhotoSetKey.create(PhotoSetProvider.PhotoSetType.SEARCH, userId, null, searchText, searchTags));
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
        PhotoSetKey key = PhotoSetKey.create(PhotoSetProvider.PhotoSetType.SET_ID, null, item.getId(), null, null);
        ContextCompat.startActivity(this, PhotoSetActivity.createIntent(this, key, item.getTitle()), null);
    }

    @Override
    public Observable<Group> getGroup(GroupProvider.GroupType type) {
        return provider.getGroupProvider().searchGroup(searchText);
    }

    @Override
    public void onGroupItemOptionsClick(int position, GroupElement item, GroupProvider.GroupType type) {

    }

    @Override
    public void onGroupLoaded(Group group) {

    }


    @Override
    public Observable<UserSet> getUsers(UsersProvider.UserSetType type) {
        return provider.getUsersProvider().getUserSet(false, UserSetKey.create(type, null, null, searchText));
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
}
