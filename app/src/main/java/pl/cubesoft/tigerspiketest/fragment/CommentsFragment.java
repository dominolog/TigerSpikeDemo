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
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


import pl.cubesoft.tigerspiketest.ImageLoader;
import pl.cubesoft.tigerspiketest.R;
import pl.cubesoft.tigerspiketest.adapter.CommentsAdapter;
import pl.cubesoft.tigerspiketest.data.CommentsProvider;
import pl.cubesoft.tigerspiketest.data.model.Comment;
import pl.cubesoft.tigerspiketest.data.model.CommentPool;
import pl.cubesoft.tigerspiketest.view.EmptyRecyclerView;
import pl.cubesoft.tigerspiketest.view.EndlessRecyclerViewScrollListener;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;


public class CommentsFragment extends BaseFragment {

    private static final String ARG_COMMENT_POOL_ID = "commentPoolId";


    private String imageLoadTag;


    private CommentsAdapter adapter;

    @BindView(R.id.main_layout)
    ViewGroup mainLayout;

    @BindView(R.id.recycler_view)
    EmptyRecyclerView recycler;

    @BindView(R.id.swipe)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.empty_view)
    View emptyView;


    @BindView(R.id.send_comment_layout)
    View sendCommentLayout;


    @BindView(R.id.comment)
    EditText comment;

    @BindView(R.id.send)
    View send;

    private OnCommentsFragmentInteractionListener listener;

    @Inject
    ImageLoader imageLoader;


    private String commentPoolId;
    private CommentPool commentPool;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_comments, container, false);

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
        recycler.addOnScrollListener(new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                if (commentPool.getTotalItemCount() > totalItemsCount) {
                    doLoadMore(page);
                }
            }
        });

        imageLoadTag = "Comments#" + commentPoolId;
        adapter = new CommentsAdapter(getContext(), imageLoader, imageLoadTag);
        recycler.setAdapter(adapter);


        swipeRefreshLayout.setOnRefreshListener(() -> {
            refreshData(true);
        });

        adapter.setOnItemIteractionListener(new CommentsAdapter.OnItemIteractionListener() {
            @Override
            public void onItemClick(View view, View transitionView, int position) {
                final Comment item = adapter.getItem(position);
                listener.onCommentItemClick(position, transitionView, item);
            }

            @Override
            public boolean onItemLongClick(View view, int position) {
                return false;
            }

            @Override
            public void onItemPhotoClick(View view, ImageView transitionView, int position) {
                final Comment item = adapter.getItem(position);
                listener.onCommentItemCreatorClick(position, transitionView, item);
            }


        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mainLayout.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
        }

        sendCommentLayout.setVisibility(View.GONE);

        refreshData(false);
    }


    public void setRefreshing(boolean refreshing) {
        swipeRefreshLayout.setRefreshing(refreshing);

    }


    public static CommentsFragment createInstance(String commentPoolId) {
        final CommentsFragment fragment = new CommentsFragment();


        final Bundle args = new Bundle();


        if (commentPoolId != null) {
            args.putString(ARG_COMMENT_POOL_ID, commentPoolId);
        }
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
        if (context instanceof OnCommentsFragmentInteractionListener) {
            listener = (OnCommentsFragmentInteractionListener) context;


            commentPoolId = getArguments().getString(ARG_COMMENT_POOL_ID);


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

    @OnClick(R.id.send)
    public void onSendComment() {
        final String comment = this.comment.getText().toString();
        if (!TextUtils.isEmpty(comment)) {
            sendComment(comment);
        }
    }

    private void refreshData(boolean force) {

        loadComments(force);

    }


    private void loadComments(boolean force) {
        CommentsProvider commentsProvider = listener.getCommentsProvider();
        subscribe(commentsProvider.getComments(commentPoolId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(() -> setRefreshing(true))
                .subscribe(comentPool -> {
                    this.commentPool = comentPool;
                    adapter.setData(comentPool);
                    sendCommentLayout.setVisibility(commentsProvider.canComment(comentPool) ? View.VISIBLE : View.GONE);
                    listener.onCommentPoolLoaded(commentPoolId, comentPool);
                    getActivity().invalidateOptionsMenu();
                }, throwable -> {
                    Timber.d(throwable, "Error while loading comments!");
                    setRefreshing(false);
                    showSnackBar(R.id.coordinator, getString(R.string.error_while_loading_comments));
                }, () -> {

                    setRefreshing(false);

                })
        );
    }

    private void doLoadMore(int page) {
        CommentsProvider commentsProvider = listener.getCommentsProvider();
        subscribe(commentsProvider.loadMore(commentPoolId, commentPool, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(() -> setRefreshing(true))
                .subscribe(comentPool -> {
                    this.commentPool = comentPool;
                    adapter.setData(comentPool);
                    sendCommentLayout.setVisibility(commentsProvider.canComment(comentPool) ? View.VISIBLE : View.GONE);
                    listener.onCommentPoolLoaded(commentPoolId, comentPool);
                    getActivity().invalidateOptionsMenu();
                }, throwable -> {
                    Timber.d(throwable, "Error while loading comments!");
                    setRefreshing(false);
                    showSnackBar(R.id.coordinator, getString(R.string.error_while_loading_comments));
                }, () -> {

                    setRefreshing(false);

                })
        );
    }



    private void sendComment(String comment) {
        CommentsProvider commentsProvider = listener.getCommentsProvider();
        subscribe(commentsProvider.addComment(commentPoolId, comment)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(() -> {
                    //setRefreshing(true);
                    sendCommentLayout.setEnabled(false);
                })
                .subscribe(newComment -> {

                    commentPool.addItem(newComment);
                    adapter.setData(commentPool);
                    recycler.smoothScrollToPosition(adapter.getItemCount() - 1);

                    this.comment.setText("");

                    getActivity().invalidateOptionsMenu();
                }, throwable -> {
                    Timber.d(throwable, "Error while adding comments!");
                    showSnackBar(R.id.coordinator, getString(R.string.error_while_adding_comment));
                }, () -> {

                    //setRefreshing(false);
                    sendCommentLayout.setEnabled(true);

                })
        );
    }


    public interface OnCommentsFragmentInteractionListener {

        CommentsProvider getCommentsProvider();

        void onCommentItemClick(int position, View view, Comment item);

        void onCommentItemCreatorClick(int position, ImageView view, Comment item);

        void onCommentPoolLoaded(String commentPoolId, CommentPool commentPool);
    }

}