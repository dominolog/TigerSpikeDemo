package pl.cubesoft.tigerspiketest.fragment;

/**
 * Created by CUBESOFT on 14.09.2017.
 */


import android.animation.LayoutTransition;
import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.cubesoft.tigerspiketest.DownloaderManager;
import pl.cubesoft.tigerspiketest.ImageLoader;
import pl.cubesoft.tigerspiketest.R;
import pl.cubesoft.tigerspiketest.activity.BaseActivity;
import pl.cubesoft.tigerspiketest.activity.CommentsActivity;
import pl.cubesoft.tigerspiketest.activity.PhotoInfoActivity;
import pl.cubesoft.tigerspiketest.activity.UserDetailActivity;
import pl.cubesoft.tigerspiketest.adapter.PhotoSetAdapter;
import pl.cubesoft.tigerspiketest.adapter.PhotoSetBaseAdapter;
import pl.cubesoft.tigerspiketest.adapter.PhotoSetTimelineAdapter;
import pl.cubesoft.tigerspiketest.data.PhotoSetProvider;
import pl.cubesoft.tigerspiketest.data.Provider;
import pl.cubesoft.tigerspiketest.data.model.Photo;
import pl.cubesoft.tigerspiketest.data.model.PhotoSet;
import pl.cubesoft.tigerspiketest.data.model.User;
import pl.cubesoft.tigerspiketest.view.EmptyRecyclerView;
import pl.cubesoft.tigerspiketest.view.EndlessRecyclerViewScrollListener;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;
import pl.cubesoft.tigerspiketest.utils.DeviceUtils;
import pl.cubesoft.tigerspiketest.utils.TextUtils;


public class PhotoSetFragment extends BaseFragment {

    private static final String ARG_TYPE = "type";
    private static final String ARG_DISPLAY_TYPE = "displayType";

    private String imageLoadTag;


    private PhotoSetBaseAdapter adapter;

    @BindView(R.id.main_layout)
    ViewGroup mainLayout;

    @BindView(R.id.recycler_view)
    EmptyRecyclerView recycler;

    @BindView(R.id.swipe)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.empty_view)
    View emptyView;

    private OnPhotoSetFragmentInteractionListener listener;

    @Inject
    ImageLoader imageLoader;

    @Inject
    DownloaderManager downloaderManager;

    @Inject
    Provider provider;

    private PhotoSetProvider.PhotoSetType type;
    private PhotoSet photoSet;
    private ShareActionProvider shareActionProvider;
    private ActionMode actionMode;
    private ViewState viewState = ViewState.STATE_NORMAL;
    private DisplayType displayType;


    enum ViewState {
        STATE_SELECTION, STATE_NORMAL

    }

    private ActionMode.Callback actionModeCallback = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.actionmode_fragment_photoset, menu);
            //MenuUtils.applyTint(menu, ContextCompat.getColor(getContext(), R.color.toolbar_icon_color));
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {

            final MenuItem menuItemDownload = menu.findItem(R.id.menu_download);
            final MenuItem menuItemDelete = menu.findItem(R.id.menu_delete);

            final int selectedItemCount = adapter.getSelectedItemCount();
            final int allItemCount = adapter.getItemCount();
            menuItemDownload.setVisible(selectedItemCount > 0);
            menuItemDelete.setVisible(selectedItemCount > 0 && listener.canDeleteFromPhotoSet(photoSet));


            updateActionModeTitle();
            return true;
        }




        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            int i = item.getItemId();
            switch (item.getItemId()) {
                case R.id.menu_download:
                    onActionDownload();
                    break;


            }

            return false;
        }


        @Override
        public void onDestroyActionMode(ActionMode mode) {
            adapter.clearSelections();
            viewState = ViewState.STATE_NORMAL;

        }


    };




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_photoset, container, false);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        ButterKnife.bind(this, view);
        getMyApplication().getAppComponent().inject(this);
        recycler.setEmptyView(emptyView);
        recycler.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);

                //your padding...
                final int padding = 1;
                final int itemPosition = parent.getChildAdapterPosition(view);
                if (itemPosition == RecyclerView.NO_POSITION) {
                    return;
                }

                final int itemCount = state.getItemCount();

                /** first position */
                if (itemPosition == 0) {
                    outRect.set(padding, padding, 0, padding);
                }
                /** last position */
                else if (itemCount > 0 && itemPosition == itemCount - 1) {
                    outRect.set(0, padding, padding, padding);
                }
                /** positions between first and last */
                else {
                    outRect.set(padding, padding, padding, padding);
                }

            }
        });
        imageLoadTag = "PhotoSet#" + type;
        if ( displayType == DisplayType.NORMAL ) {
            int columnCount = 2;
            if (DeviceUtils.isTablet(getContext())) {
                columnCount = DeviceUtils.getDeviceScreenOrientation(getContext()) == DeviceUtils.Orientation.LANDSCAPE ? 4 : 3;
            } else {
                columnCount = DeviceUtils.getDeviceScreenOrientation(getContext()) == DeviceUtils.Orientation.LANDSCAPE ? 3 : 2;
            }

            final StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(columnCount, StaggeredGridLayoutManager.VERTICAL);

            layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
            recycler.setLayoutManager(layoutManager);
            adapter = new PhotoSetAdapter(getContext(), imageLoader, imageLoadTag, columnCount);
            recycler.addOnScrollListener( new EndlessRecyclerViewScrollListener(layoutManager) {

                @Override
                public void onLoadMore(int page, int totalItemsCount) {
                    PhotoSetFragment.this.onLoadMore(page, totalItemsCount);
                }
            });
        } else if (displayType == DisplayType.TIMELINE) {

            int columnCount = 1;
            if (DeviceUtils.isTablet(getContext())) {
                columnCount = DeviceUtils.getDeviceScreenOrientation(getContext()) == DeviceUtils.Orientation.LANDSCAPE ? 3 : 2;
            } else {
                columnCount = DeviceUtils.getDeviceScreenOrientation(getContext()) == DeviceUtils.Orientation.LANDSCAPE ? 2 : 1;
            }
            RecyclerView.LayoutManager layoutManager;
            if (columnCount == 1) {
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                layoutManager = linearLayoutManager;
                recycler.addOnScrollListener( new EndlessRecyclerViewScrollListener(linearLayoutManager) {
                    @Override
                    public void onLoadMore(int page, int totalItemsCount) {
                        PhotoSetFragment.this.onLoadMore(page, totalItemsCount);
                    }
                });
            } else {
                StaggeredGridLayoutManager staggeredLayoutManager = new StaggeredGridLayoutManager(columnCount, StaggeredGridLayoutManager.VERTICAL);
                staggeredLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
                layoutManager = staggeredLayoutManager;
                recycler.addOnScrollListener( new EndlessRecyclerViewScrollListener(staggeredLayoutManager) {
                    @Override
                    public void onLoadMore(int page, int totalItemsCount) {
                        PhotoSetFragment.this.onLoadMore(page, totalItemsCount);
                    }
                });
            }
            recycler.setLayoutManager(layoutManager);
            adapter = new PhotoSetTimelineAdapter(getContext(), imageLoader, imageLoadTag, columnCount);

        }
        recycler.setAdapter(adapter);



        swipeRefreshLayout.setOnRefreshListener(() -> {
            refreshData(true);
        });

        adapter.setOnItemIteractionListener(new PhotoSetAdapter.OnItemIteractionListener() {
            @Override
            public void onItemClick(View view, View transitionView, int position) {

                final int idx = recycler.getChildAdapterPosition(view);
                if (viewState == ViewState.STATE_SELECTION) {
                    adapter.toggleSelection(idx);
                    if (actionMode != null) {
                        actionMode.invalidate();
                    }

                } else {
                    final Photo item = adapter.getItem(position);
                    listener.onPhotoSetItemClick(position, transitionView, item, photoSet);
                }

            }

            @Override
            public boolean onItemLongClick(View view, int position) {

                final int idx = recycler.getChildAdapterPosition(view);
                adapter.toggleSelection(idx);

                final BaseActivity activity = getBaseActivity();

                if (viewState == ViewState.STATE_NORMAL) {
                    actionMode = activity.startSupportActionMode(actionModeCallback);
                    viewState = ViewState.STATE_SELECTION;
                    updateActionModeTitle();
                }
                return false;
            }

            @Override
            public void onItemLongPlayClick(View view, int position) {
                final Photo item = adapter.getItem(position);
                getPlayVideo(item);
            }

            @Override
            public void onPhotoItemUserClick(User user) {
                ContextCompat.startActivity(getContext(), UserDetailActivity.createIntent(getContext(), user.getId(), null), null);
            }

            @Override
            public void onPhotoItemFavoriteClick(int position) {
                final Photo item = adapter.getItem(position);
                doFavoritePhoto(position, item);
            }

            @Override
            public void onPhotoItemCommentClick(int position) {
                final Photo item = adapter.getItem(position);
                ContextCompat.startActivity(getContext(), CommentsActivity.createIntent(getContext(), item.getCommentPoolId()), null);
            }

            @Override
            public void onPhotoItemInfoClick(int position) {
                final Photo item = adapter.getItem(position);
                ContextCompat.startActivity(getContext(), PhotoInfoActivity.createIntent(getContext(), item.getId()), null);
            }

            @Override
            public boolean canFavorite(Photo item) {
                return provider.canFavorite(item);
            }

            @Override
            public Subscription getPhotoOwner(Photo photo, Callback<User> callback) {
                Subscription subscription  = provider.getUsersProvider().getPhotoOwner(false, photo)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(callback::call,
                                throwable -> {},
                                () -> {});

                subscribe(subscription);
                return subscription;
            }


        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mainLayout.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
        }


        refreshData(false);
    }

    private void doFavoritePhoto(int position, Photo item) {

        PhotoSetProvider photoSetProvider = provider.getPhotoSetProvider();
        Observable<Photo> observable = !item.isFavorite() ? photoSetProvider.addPhotoToFavorites(item) :
                photoSetProvider.removePhotoFromFavorites(item);
        subscribe(observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(() -> {
                })
                .subscribe(photo -> {
                    //adapter.setDataAtPosition(position, photo);
                    adapter.notifyItemChanged(position);
                    getActivity().invalidateOptionsMenu();
                }, throwable -> {
                    Timber.d(throwable, "Error while updating photo!");

                    showSnackBar(R.id.coordinator, getString(R.string.error_while_updating_photo));
                }, () -> {


                })
        );
    }

    private void onLoadMore(int page, int totalItemsCount) {
        if (photoSet.getTotalItemCount() > totalItemsCount ) {
            doLoadMore(page, totalItemsCount);
        }
    }

    private void doLoadMore(int page, int totalItemsCount) {
        subscribe(this.provider.getPhotoSetProvider().loadMore(photoSet, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(() -> setRefreshing(true))
                .subscribe(photoSet -> {

                    this.photoSet = photoSet;
                    adapter.setData(photoSet);
                    listener.onPhotoSetLoaded(photoSet);

                    getActivity().invalidateOptionsMenu();

                }, throwable -> {
                    Timber.d(throwable, "Error while loading more photoset");

                    showSnackBar(R.id.coordinator, getString(R.string.error_while_loading_photoset));
                }, () -> {

                    setRefreshing(false);

                }));
    }

    private void getPlayVideo(Photo item) {
        subscribe(this.provider.getPhotoSetProvider().getVideoUrl(item.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(() -> showProgress(true, R.string.loading))
                .subscribe(url -> {
                    super.playVideo(url);

                }, throwable -> {
                    Timber.d(throwable, "Error while obtaining video playback url!");
                    showProgress(false, -1);
                    showSnackBar(R.id.coordinator, getString(R.string.error_while_getting_video_url));
                }, () -> {

                    showProgress(false, -1);

                }));
    }



    public void setRefreshing(boolean refreshing) {
        swipeRefreshLayout.setRefreshing(refreshing);
    }

    public enum DisplayType {
        NORMAL,
        TIMELINE
    }

    public static PhotoSetFragment createInstance(PhotoSetProvider.PhotoSetType type, DisplayType displayType)  {
        final PhotoSetFragment fragment = new PhotoSetFragment();


        final Bundle args = new Bundle();
        args.putSerializable(ARG_TYPE, type);
        args.putSerializable(ARG_DISPLAY_TYPE, displayType);

        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setHasOptionsMenu(true);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnPhotoSetFragmentInteractionListener) {
            listener = (OnPhotoSetFragmentInteractionListener) context;
            type = (PhotoSetProvider.PhotoSetType) getArguments().getSerializable(ARG_TYPE);
            displayType = (DisplayType) getArguments().getSerializable(ARG_DISPLAY_TYPE);

        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnPhotoSetFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @Override
    public void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_photoset, menu);

        MenuItem itemShare = menu.findItem(R.id.menu_share);
        //itemShare.setVisible(photoSet > 0);
        shareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(itemShare);

        super.onCreateOptionsMenu(menu, inflater);

    }


    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        MenuItem itemShare = menu.findItem(R.id.menu_share);
        itemShare.setVisible(photoSet != null && false);

        MenuItem itemComments = menu.findItem(R.id.menu_comments);
        itemComments.setVisible(photoSet != null && photoSet.getCommentPoolId() != null);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_comments:
                onMenuComments();
                return true;

        }

        return super.onOptionsItemSelected(item);
    }

    private void onMenuComments() {
        ContextCompat.startActivity(getContext(), CommentsActivity.createIntent(getContext(), photoSet.getCommentPoolId()), null);
    }


    @Override
    public void onPause() {
        super.onPause();

        // pause all image loading tasks
        imageLoader.pauseTag(imageLoadTag);

        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onResume() {
        super.onResume();


        imageLoader.resumeTag(imageLoadTag);

        EventBus.getDefault().register(this);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        imageLoader.cancelTag(imageLoadTag);

        if (actionMode != null) {
            actionMode.finish();
        }

    }


    @Subscribe
    public void onEvent(String data) {

    }

    void updateActionModeTitle() {
        if (actionMode != null) {
            actionMode.setTitle(TextUtils.formatItems(adapter.getSelectedItemCount()));
        }
    }

    private void refreshData(boolean force) {

        loadPhotoSet(force);

    }


    private void onActionDownload() {

        final List<String> urls = Stream.of(adapter.getSelectedItems()).map(item -> adapter.getItem(item).getUrl()).collect(Collectors.toList());

        subscribe(downloaderManager.submitDownloads(getActivity(), urls, Collections.EMPTY_MAP, Collections.EMPTY_MAP)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(o -> {
                    clearActionMode();
                    showSnackBar(R.id.coordinator, "Download started");
                }, throwable -> {
                    showSnackBar(R.id.coordinator, "Download error!");
                }, () -> {
                }));
    }



    void clearActionMode () {
        adapter.clearSelections();
        actionMode.finish();
        actionMode = null;
    }





    private void loadPhotoSet(boolean force) {
        subscribe(listener.getPhotoSet(force, type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(() -> setRefreshing(true))
                .subscribe(photoSet -> {
                    this.photoSet = photoSet;
                    adapter.setData(photoSet);
                    listener.onPhotoSetLoaded(photoSet);

                    getActivity().invalidateOptionsMenu();
                }, throwable -> {
                    Timber.d(throwable, "Error while loading photoset!");
                    setRefreshing(false);
                    showSnackBar(R.id.coordinator, getString(R.string.error_while_loading_photoset));
                }, () -> {

                    setRefreshing(false);

                })
        );
    }




    public interface OnPhotoSetFragmentInteractionListener {
        Observable<PhotoSet> getPhotoSet(boolean force, PhotoSetProvider.PhotoSetType type);
        void onPhotoSetItemClick(int position, View view, Photo item, PhotoSet photoSet);

        void onPhotoSetLoaded(PhotoSet photoSet);

        boolean canDeleteFromPhotoSet(PhotoSet photoSet);

    }

}