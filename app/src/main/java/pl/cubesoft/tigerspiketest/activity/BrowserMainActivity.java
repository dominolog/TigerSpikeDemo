package pl.cubesoft.tigerspiketest.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.miguelcatalan.materialsearchview.MaterialSearchView;


import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.cubesoft.tigerspiketest.ImageLoader;
import pl.cubesoft.tigerspiketest.R;
import pl.cubesoft.tigerspiketest.adapter.BrowserMainAdapter;
import pl.cubesoft.tigerspiketest.data.GroupProvider;
import pl.cubesoft.tigerspiketest.data.PhotoSetKey;
import pl.cubesoft.tigerspiketest.data.PhotoSetProvider;
import pl.cubesoft.tigerspiketest.data.Provider;
import pl.cubesoft.tigerspiketest.data.model.Group;
import pl.cubesoft.tigerspiketest.data.model.GroupElement;
import pl.cubesoft.tigerspiketest.data.model.Photo;
import pl.cubesoft.tigerspiketest.data.model.PhotoSet;
import pl.cubesoft.tigerspiketest.data.model.User;
import pl.cubesoft.tigerspiketest.fragment.AlertDialogFragment;
import pl.cubesoft.tigerspiketest.fragment.GroupFragment;
import pl.cubesoft.tigerspiketest.fragment.PhotoSetFragment;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

public class BrowserMainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        PhotoSetFragment.OnPhotoSetFragmentInteractionListener,
        GroupFragment.OnGroupFragmentInteractionListener {


    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.tabs)
    TabLayout tabs;

    @BindView(R.id.search_view)
    MaterialSearchView searchView;

    @BindView(R.id.main_pager)
    ViewPager mainPager;

    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;

    @BindView(R.id.nav_view)
    NavigationView navigationView;

    @Inject
    Provider provider;



    @Inject
    ImageLoader imageLoader;
    private View headerView;
    private String queryText;

    class HeaderView {

        @BindView(R.id.header_cover_photo)
        ImageView headerCoverPhoto;

        @BindView(R.id.hader_profile_photo)
        ImageView haderProfilePhoto;

        @BindView(R.id.username)
        TextView username;

        @BindView(R.id.display_name)
        TextView displayName;
    }

    HeaderView header = new HeaderView();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser_main);
        ButterKnife.bind(this);
        getMyApplication().getAppComponent().inject(this);

        setSupportActionBar(toolbar);

        setTitle(provider.getName());
        tabs.setupWithViewPager(mainPager);


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        headerView = navigationView.getHeaderView(0);
        ButterKnife.bind(header, headerView);
        headerView.setOnClickListener(view -> onHeaderLayoutClick());


        navigationView.setNavigationItemSelectedListener(this);


        mainPager.setAdapter(new BrowserMainAdapter(getSupportFragmentManager(), getResources(), provider.getBrowserViewTypes()));
        mainPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        //navigationView.setCheckedItem(R.id.nav_places);
                        break;
                    case 1:
                        //navigationView.setCheckedItem(R.id.nav_tours);
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        searchView.setVoiceSearch(true);
        //searchView.setSuggestions(new String[] {"Sex", "Bikini"});

        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //Do some magic
                showSearch(query);
                searchView.closeSearch();

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                queryText = newText;
                return false;
            }
        });

        //setupHeaderMenu(navigationView.getMenu(), provider.getBrowserViewTypes());


    }


    @Override
    public void onResume() {
        super.onResume();

        if (provider.getAuthProvider().isLoggedIn()) {
            loadUser();
        } else {
            headerView.setVisibility(View.GONE);
        }



    }

    /*private void setupHeaderMenu(Menu menu, List<Provider.BrowserViewType> viewTypes) {

        MenuItem menuProvider = menu.findItem(R.id.menu_provider);
        SubMenu subMenu = menuProvider.getSubMenu();
        int i=0;
        for (Provider.BrowserViewType viewType : viewTypes) {
            MenuItem item = subMenu.add(R.id.menu_provider, i++, viewType.ordinal(), getViewTypeName(viewType));
            item.setIcon(getViewTypeIcon(viewType));
        }
    }*/



    private void loadUser() {
        subscribe(provider.getUsersProvider().getMyUser(false)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(() -> showProgress(true))
                .subscribe(user -> {

                    setUser(user);

                }, throwable -> {
                    Timber.d(throwable, "Error while login!");
                    showProgress(false);
                    showSnackBar(R.id.coordinator, getString(R.string.error_while_loading_photoset));
                }, () -> {

                    showProgress(false);

                }));
    }

    private void showProgress(boolean b) {

    }


    public void setUser(User user) {
        headerView.setVisibility(View.VISIBLE);
        header.username.setText(user.getName());
        header.displayName.setText(user.getDisplayName());

        imageLoader.load(Uri.parse(user.getPhotoUrl()), header.haderProfilePhoto, ImageLoader.Transform.CIRCLE, "");
        imageLoader.load(Uri.parse(user.getCoverPhotoUrl()), header.headerCoverPhoto, ImageLoader.Transform.NONE, "");

        getSupportActionBar().setSubtitle(user.getDisplayName());
    }





    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {

            if (searchView.isSearchOpen()) {
                searchView.closeSearch();
            } else {
                super.onBackPressed();
            }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_browser_main, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = (item.getItemId());
        if (id == Provider.BrowserViewType.POPULAR_PHOTOS.ordinal()) {
        } else if (id == Provider.BrowserViewType.RECENT_PHOTOS.ordinal()) {
        } else if (id == Provider.BrowserViewType.POPULAR_SETS.ordinal()) {
        } else if (id == Provider.BrowserViewType.RECENT_SETS.ordinal()) {
        } else if (id == Provider.BrowserViewType.VOTED_PHOTOS.ordinal()) {
        }

        if (id == R.id.nav_search) {
            showSearch();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @OnClick(R.id.fab_search)
    public void onSearch() {
        showSearch();
    }

    private void showSearch() {
        if (!searchView.isSearchOpen()) {
            searchView.showSearch();
        } else {
            showSearch(queryText);
            //showSearch(searchView.);
        }
    }





    public void onHeaderLayoutClick() {
        //selectProvider();
    }




    static Intent createIntent(Context context) {
        return new Intent(context, BrowserMainActivity.class);
    }

    private void showSearch(String query) {
        ContextCompat.startActivity(BrowserMainActivity.this, SearchActivity.createIntent(BrowserMainActivity.this, null, query, null), null);

    }

    @Override
    public Observable<PhotoSet> getPhotoSet(boolean force, PhotoSetProvider.PhotoSetType type) {

        return provider.getPhotoSetProvider().getPhotoSet(force, PhotoSetKey.create(type, null, null, null, null));
    }

    @Override
    public void onPhotoSetItemClick(int position, View view, Photo item, PhotoSet photoSet) {
        // FIX THIS
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
        final PhotoSetKey key = PhotoSetKey.create(PhotoSetProvider.PhotoSetType.SET_ID, null, item.getId(), null, null);
        ContextCompat.startActivity(this, PhotoSetActivity.createIntent(this, key, item.getTitle()), null);
    }

    @Override
    public Observable<Group> getGroup(GroupProvider.GroupType type) {
        return provider.getGroupProvider().getGroup(type, null, null);
    }

    @Override
    public void onGroupItemOptionsClick(int position, GroupElement item, GroupProvider.GroupType type) {

    }

    @Override
    public void onGroupLoaded(Group group) {

    }


}
