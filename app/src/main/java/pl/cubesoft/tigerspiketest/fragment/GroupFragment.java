package pl.cubesoft.tigerspiketest.fragment;

/**
 * Created by CUBESOFT on 14.09.2017.
 */


import android.animation.LayoutTransition;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import pl.cubesoft.tigerspiketest.ImageLoader;
import pl.cubesoft.tigerspiketest.R;
import pl.cubesoft.tigerspiketest.adapter.GroupAdapter;
import pl.cubesoft.tigerspiketest.data.GroupProvider;
import pl.cubesoft.tigerspiketest.data.model.Group;
import pl.cubesoft.tigerspiketest.data.model.GroupElement;
import pl.cubesoft.tigerspiketest.events.GroupEvent;
import pl.cubesoft.tigerspiketest.view.EmptyRecyclerView;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;


public class GroupFragment extends BaseFragment {

    private static final String ARG_TYPE = "type";


    private String imageLoadTag;


    private GroupAdapter adapter;

    @BindView(R.id.main_layout)
    ViewGroup mainLayout;

    @BindView(R.id.recycler_view)
    EmptyRecyclerView recycler;

    @BindView(R.id.swipe)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.empty_view)
    View emptyView;

    private OnGroupFragmentInteractionListener listener;

    @Inject
    ImageLoader imageLoader;

    private GroupProvider.GroupType type;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_group, container, false);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        ButterKnife.bind(this, view);
        getMyApplication().getAppComponent().inject(this);
        recycler.setEmptyView(emptyView);

        final int columnCount = getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT ? 1 : 2;

        final GridLayoutManager layoutManager = new GridLayoutManager(getContext(), columnCount);
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


        imageLoadTag = "PhotoSet#" + type;
        adapter = new GroupAdapter(getContext(), imageLoader, imageLoadTag, columnCount);
        recycler.setAdapter(adapter);


        swipeRefreshLayout.setOnRefreshListener(() -> {
            refreshData(true);
        });

        adapter.setOnItemIteractionListener(new GroupAdapter.OnItemIteractionListener() {
            @Override
            public void onItemClick(View view, View transitionView, int position) {
                GroupElement item = adapter.getItem(position);
                listener.onGroupItemClick(position, transitionView, item, type);
            }

            @Override
            public boolean onItemLongClick(View view, int position) {

                GroupElement item = adapter.getItem(position);
                listener.onGroupItemOptionsClick(position, item, type);
                return true;
            }

            @Override
            public void onItemOptionsClick(View view, int position) {
                GroupElement item = adapter.getItem(position);
                listener.onGroupItemOptionsClick(position, item, type);
            }


        });





        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mainLayout.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
        }


        refreshData(false);
    }

    public void setRefreshing(boolean refreshing) {
        swipeRefreshLayout.setRefreshing(refreshing);
    }




    public static GroupFragment createInstance(GroupProvider.GroupType type) {
        final GroupFragment fragment = new GroupFragment();


        final Bundle args = new Bundle();
        args.putSerializable(ARG_TYPE, type);

        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnGroupFragmentInteractionListener) {
            listener = (OnGroupFragmentInteractionListener) context;
            type = (GroupProvider.GroupType) getArguments().getSerializable(ARG_TYPE);


        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnGroupFragmentInteractionListener");
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


    }

    @Override
    public void onResume() {
        super.onResume();


        imageLoader.resumeTag(imageLoadTag);



    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        imageLoader.cancelTag(imageLoadTag);

        EventBus.getDefault().unregister(this);

    }


    @Subscribe
    public void onEvent(GroupEvent event) {
        refreshData(true);
    }

    private void refreshData(boolean force) {

        loadGroup(force);

    }


    private void loadGroup(boolean force) {
        subscribe(listener.getGroup(type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(() -> setRefreshing(true))
                .subscribe(group -> {
                    adapter.setData(group);
                    listener.onGroupLoaded(group);
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




    public interface OnGroupFragmentInteractionListener {

        void onGroupItemClick(int position, View view, GroupElement item, GroupProvider.GroupType groupType);

        Observable<Group> getGroup(GroupProvider.GroupType type);

        void onGroupItemOptionsClick(int position, GroupElement item, GroupProvider.GroupType type);
        void onGroupLoaded(Group group);
    }

}