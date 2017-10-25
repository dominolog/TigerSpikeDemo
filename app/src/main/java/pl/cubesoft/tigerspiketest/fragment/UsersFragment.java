package pl.cubesoft.tigerspiketest.fragment;

/**
 * Created by CUBESOFT on 14.09.2017.
 */


import android.animation.LayoutTransition;
import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import pl.cubesoft.tigerspiketest.ImageLoader;
import pl.cubesoft.tigerspiketest.R;
import pl.cubesoft.tigerspiketest.adapter.UsersAdapter;
import pl.cubesoft.tigerspiketest.data.UsersProvider;
import pl.cubesoft.tigerspiketest.data.model.User;
import pl.cubesoft.tigerspiketest.data.model.UserSet;
import pl.cubesoft.tigerspiketest.view.EmptyRecyclerView;
import pl.cubesoft.tigerspiketest.view.EndlessRecyclerViewScrollListener;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;


public class UsersFragment extends BaseFragment {


    private static final String ARG_TYPE = "type";
    private String imageLoadTag;


    private UsersAdapter adapter;

    @BindView(R.id.main_layout)
    ViewGroup mainLayout;

    @BindView(R.id.recycler_view)
    EmptyRecyclerView recycler;

    @BindView(R.id.swipe)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.empty_view)
    View emptyView;

    private OnUsersFragmentInteractionListener listener;

    @Inject
    ImageLoader imageLoader;
    private UsersProvider.UserSetType type;
    private UserSet userSet;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_users, container, false);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        ButterKnife.bind(this, view);
        getMyApplication().getAppComponent().inject(this);
        recycler.setEmptyView(emptyView);


        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recycler.setLayoutManager(layoutManager);
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

        recycler.addOnScrollListener( new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                UsersFragment.this.onLoadMore(page, totalItemsCount);
            }
        });


        imageLoadTag = "Users#" + type;
        adapter = new UsersAdapter(getContext(), imageLoader, imageLoadTag);
        recycler.setAdapter(adapter);


        swipeRefreshLayout.setOnRefreshListener(() -> {
            refreshData(true);
        });

        adapter.setOnItemIteractionListener(new UsersAdapter.OnItemIteractionListener() {
            @Override
            public void onItemClick(View view, View transitionView, int position) {
                final User item = adapter.getItem(position);
                listener.onUserItemClick(position, transitionView, item);
            }

            @Override
            public boolean onItemLongClick(View view, int position) {
                return false;
            }

            @Override
            public void onItemPhotoClick(View view, ImageView transitionView, int position) {
                final User item = adapter.getItem(position);
                listener.onUserItemClick(position, transitionView, item);
            }


        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mainLayout.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
        }

        refreshData(false);
    }

    private void onLoadMore(int page, int totalItemsCount) {
        if (userSet.getTotalItemCount() > totalItemsCount ) {
            doLoadMore(page, totalItemsCount);
        }
    }



    public void setRefreshing(boolean refreshing) {
        swipeRefreshLayout.setRefreshing(refreshing);
    }



    public static UsersFragment createInstance(UsersProvider.UserSetType userSetType) {
        final UsersFragment fragment = new UsersFragment();


        final Bundle args = new Bundle();

        args.putSerializable(ARG_TYPE, userSetType);
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
        if (context instanceof OnUsersFragmentInteractionListener) {
            listener = (OnUsersFragmentInteractionListener) context;


            type = (UsersProvider.UserSetType) getArguments().getSerializable(ARG_TYPE);

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

    }


    @Subscribe
    public void onEvent(String data) {

    }


    private void refreshData(boolean force) {

        loadUsers(force);

    }


    private void loadUsers(boolean force) {
        subscribe(listener.getUsers(type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(() -> setRefreshing(true))
                .subscribe(userSet -> {
                    this.userSet = userSet;
                    adapter.setData(userSet);
                    listener.onUsersLoaded(type, userSet);
                    getActivity().invalidateOptionsMenu();
                }, throwable -> {
                    Timber.d(throwable, "Error while loading users!");
                    setRefreshing(false);
                    showSnackBar(R.id.coordinator, getString(R.string.error_while_loading_users));
                }, () -> {

                    setRefreshing(false);

                })
        );
    }


    private void doLoadMore(int page, int totalItemsCount) {
        subscribe(this.listener.loadMoreUsers(userSet, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(() -> setRefreshing(true))
                .subscribe(userSet -> {
                    this.userSet = userSet;
                    adapter.setData(userSet);
                    listener.onUsersLoaded(type, userSet);

                    getActivity().invalidateOptionsMenu();

                }, throwable -> {
                    Timber.d(throwable, "Error while loading more users");

                    showSnackBar(R.id.coordinator, getString(R.string.error_while_loading_users));
                }, () -> {

                    setRefreshing(false);

                }));
    }


    public interface OnUsersFragmentInteractionListener {

        Observable<UserSet> getUsers(UsersProvider.UserSetType type);
        Observable<UserSet> loadMoreUsers(UserSet userSet, int page);

        void onUserItemClick(int position, View view, User item);

        void onUsersLoaded(UsersProvider.UserSetType type, UserSet userSet);


    }

}