package pl.cubesoft.tigerspiketest.fragment;

import android.Manifest;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.ShareActionProvider;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.github.chrisbanes.photoview.PhotoView;
import com.github.chrisbanes.photoview.PhotoViewAttacher;
import com.hootsuite.nachos.NachoTextView;
import com.tbruyelle.rxpermissions.RxPermissions;


import java.util.Collections;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import pl.cubesoft.tigerspiketest.DownloaderManager;
import pl.cubesoft.tigerspiketest.ImageLoader;
import pl.cubesoft.tigerspiketest.R;
import pl.cubesoft.tigerspiketest.activity.CommentsActivity;
import pl.cubesoft.tigerspiketest.activity.PhotoInfoActivity;
import pl.cubesoft.tigerspiketest.data.PhotoSetKey;
import pl.cubesoft.tigerspiketest.data.PhotoSetProvider;
import pl.cubesoft.tigerspiketest.data.Provider;
import pl.cubesoft.tigerspiketest.data.UsersProvider;
import pl.cubesoft.tigerspiketest.data.model.Photo;
import pl.cubesoft.tigerspiketest.data.model.PhotoSet;
import pl.cubesoft.tigerspiketest.data.model.User;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;
import pl.cubesoft.tigerspiketest.utils.CollectionUtils;

/**
 * Created by CUBESOFT on 29.05.2017.
 */

public class PhotoPagerFragment extends BaseFragment {

    private static final Object IMAGE_LOAD_TAG = "photoPager";
    private static final String ARG_POSITION = "position";
    private static final String ARG_IS_PLAYING = "isPlaying";
    private static final String ARG_PHOTOSET_KEY = "key";

    private static final long SLIDESHOW_TIMEOUT = 5000;


    @BindView(R.id.photo_pager)
    ViewPager photoPager;

    @BindView(R.id.progress)
    ProgressBar progress;

    private PhotoPagerAdapter adapter;

    @Inject
    ImageLoader imageLoader;

    @Inject
    DownloaderManager downloaderManager;

    @Inject
    Provider provider;

    private int position;
    private OnPhotoPagerFragmentInteractionListener listener;

    private UsersProvider userProvider;
    private PhotoSet photoSet;
    private PhotoSetKey photoSetKey;
    private boolean showBottomLayout = false;
    private boolean isPlaying = false;
    private Handler handler = new Handler();
    private Runnable changePageRunnable = new Runnable() {
        @Override
        public void run() {
            final int nextPage = photoPager.getCurrentItem() == adapter.getCount() -1 ? 0 : photoPager.getCurrentItem() + 1;
            photoPager.setCurrentItem(nextPage, true);
            switchPage();
        }
    };

    public void setRefreshing(boolean refreshing) {
        progress.setVisibility(refreshing ? View.VISIBLE : View.GONE);
    }


    class PhotoPagerAdapter extends PagerAdapter {


        private PhotoSet photoSet;

        @Override
        public int getCount() {
            return photoSet != null ? photoSet.getItemCount() : 0;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        public void setData(PhotoSet photoSet) {
            this.photoSet = photoSet;
            notifyDataSetChanged();
        }

        public void setDataAtPosition(int position, Photo photo) {
            this.photoSet.setItem(position, photo);
            notifyDataSetChanged();

        }


        class PhotoItem {

            @BindView(R.id.bottom_layout)
            View bottomLayout;

            @BindView(R.id.photo)
            PhotoView photo;

            @BindView(R.id.play)
            View play;


            @BindView(R.id.user_photo)
            ImageView userPhoto;


            @BindView(R.id.user_name)
            TextView username;

            @BindView(R.id.num_views)
            TextView numViews;


            @BindView(R.id.num_favorites)
            TextView numFavorites;


            @BindView(R.id.title)
            TextView title;


            @BindView(R.id.description)
            TextView description;

            @BindView(R.id.tags)
            NachoTextView tags;


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


            private PhotoViewAttacher attacher;

            public void init() {
                attacher = new PhotoViewAttacher(photo);

            }

            @OnClick(R.id.description_layout)
            public void onDescriptionClick() {
                title.setMaxLines(2);
                description.setMaxLines(200);
            }
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            final View view = LayoutInflater.from(container.getContext()).inflate(R.layout.layout_photo_pager_item, container, false);
            container.addView(view);

            final PhotoItem photoItem = new PhotoItem();
            ButterKnife.bind(photoItem, view);
            photoItem.init();

            view.setTag(photoItem);

            final Photo item = getItem(position);

            photoItem.play.setVisibility(item.getType() == Photo.Type.VIDEO ? View.VISIBLE : View.GONE);
            photoItem.play.setOnClickListener(view1 -> {
                final Photo photo = adapter.getItem(position);
                listener.onPagerItemPhotoPlayClick(photo);
            });


            final Uri uri = Uri.parse(item.getUrl());


            ViewCompat.setTransitionName(photoItem.photo, item.getUrl());


            if (item.getNumViews() != null) {
                photoItem.numViews.setText(Integer.toString(item.getNumViews()));
                photoItem.numViews.setVisibility(View.VISIBLE);
                photoItem.numViewsIcon.setVisibility(View.VISIBLE);
            } else {
                photoItem.numViews.setVisibility(View.GONE);
                photoItem.numViewsIcon.setVisibility(View.GONE);
            }

            if (item.getNumFavorites() != null) {
                photoItem.numFavorites.setText(Integer.toString(item.getNumFavorites()));
                photoItem.numFavorites.setVisibility(View.VISIBLE);
                photoItem.numFavoritesIcon.setVisibility(View.VISIBLE);
            } else {
                photoItem.numFavorites.setVisibility(View.GONE);
                photoItem.numFavoritesIcon.setVisibility(View.GONE);

            }

            if (item.isFavorite() != null) {
                photoItem.numFavoritesIcon.setImageResource(item.isFavorite() ? R.drawable.ic_favorite_black_24dp : R.drawable.ic_favorite_border_black_24dp);


                if (provider.canFavorite(item)) {
                    photoItem.numFavoritesIcon.setOnClickListener(view1 -> {
                        final Photo photo = adapter.getItem(position);
                        onPhotoFavoriteClick(position, photo);
                    });
                } else {
                    photoItem.numFavoritesIcon.setOnClickListener(null);
                }

            } else {
                photoItem.numFavoritesIcon.setOnClickListener(null);
            }


            if (item.getNumComments() != null) {
                photoItem.numComments.setText(Integer.toString(item.getNumComments()));
                photoItem.numCommentsLayout.setVisibility(View.VISIBLE);

                photoItem.numCommentsLayout.setOnClickListener(view1 -> {
                    final Photo photo = adapter.getItem(position);
                    listener.onPhotoItemCommentsClick(photo);
                });
            } else {
                photoItem.numCommentsLayout.setVisibility(View.GONE);

            }

            if (item.getRating() != null) {
                photoItem.rating.setMax(item.getMaxRating());
                photoItem.rating.setRating(item.getRating());
                photoItem.rating.setVisibility(View.VISIBLE);
            } else {
                photoItem.rating.setVisibility(View.GONE);
            }
            photoItem.title.setText(item.getTitle());

            photoItem.description.setText(item.getDescription() != null ? Html.fromHtml(item.getDescription()) : null);

            if (false && !CollectionUtils.isEmpty(item.getTags())) {
                photoItem.tags.setText(item.getTags());
                photoItem.tags.setVisibility(View.VISIBLE);
                photoItem.tags.setOnChipClickListener((chip, motionEvent) -> {
                    listener.onPagerItemPhotoTagClick(item, chip.getText());
                });
            } else {
                photoItem.tags.setVisibility(View.GONE);
            }
            photoItem.attacher.setOnPhotoTapListener((view1, x, y) -> {

                listener.onPagerItemClick(position, uri);

            });

            photoItem.attacher.setOnOutsidePhotoTapListener((view1) -> {

                listener.onPagerItemClick(position, uri);

            });

            photoItem.photo.setOnPhotoTapListener((view12, x, y) -> {
                listener.onPagerItemClick(position, uri);

            });


            imageLoader.load(uri, photoItem.photo, true, true, ImageLoader.Transform.NONE, IMAGE_LOAD_TAG, new ImageLoader.Listener() {
                @Override
                public void onImageLoadSuccess(Bitmap bitmap) {
                    //photoItem.attacher.update();
                    //startPostponedEnterTransition();
                }

                @Override
                public void onImageLoadError() {

                    //startPostponedEnterTransition();
                }
            });


            subscribe(userProvider.getPhotoOwner(false, item)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(user -> {
                        setUser(user, photoItem);
                    }, throwable -> {
                        showSnackBar(R.id.coordinator, "Error while loading user profile!");
                    }, () -> {
                    }));


            view.setTag(photoItem);
            showBottomLayout(photoItem.bottomLayout, showBottomLayout);

            return view;
        }


        private Photo getItem(int position) {
            return photoSet.getItem(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            final View view = (View) object;
            container.removeView(view);
            PhotoItem photoItem = (PhotoItem) view.getTag();
            imageLoader.cancelRequest(photoItem.photo);
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }


        public void setUser(User user, PhotoItem photoItem) {
            if (user != null) {
                if (user.getPhotoUrl() != null) {
                    photoItem.userPhoto.setVisibility(View.VISIBLE);

                    imageLoader.load(Uri.parse(user.getPhotoUrl()), photoItem.userPhoto, ImageLoader.Transform.CIRCLE, IMAGE_LOAD_TAG);

                } else {
                    photoItem.userPhoto.setVisibility(View.GONE);

                }
                if (user.getDisplayName() != null) {
                    photoItem.username.setText(user.getDisplayName());
                } else {
                    photoItem.username.setText(user.getName());
                }

                photoItem.userPhoto.setOnClickListener(view1 -> {

                    listener.onPhotoItemUserClick(user);
                });
            } else {
                photoItem.userPhoto.setVisibility(View.GONE);
            }
        }


    }

    private void onPhotoFavoriteClick(int position, Photo item) {

        PhotoSetProvider photoSetProvider = listener.getPhotoSetProvider();
        Observable<Photo> observable = !item.isFavorite() ? photoSetProvider.addPhotoToFavorites(item) :
                photoSetProvider.removePhotoFromFavorites(item);
        subscribe(observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(() -> {
                })
                .subscribe(photo -> {
                    adapter.notifyDataSetChanged();
                    // FAKE
                    //item.setFavorite(!item.isFavorite());

                    //adapter.setDataAtPosition(position, photo);

                    getActivity().invalidateOptionsMenu();
                }, throwable -> {
                    Timber.d(throwable, "Error while updating photo!");

                    showSnackBar(R.id.coordinator, getString(R.string.error_while_updating_photo));
                }, () -> {


                })
        );
    }


    public static PhotoPagerFragment createInstance(PhotoSetKey photoSetKey, int position) {
        PhotoPagerFragment fragment = new PhotoPagerFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PHOTOSET_KEY, photoSetKey);
        args.putInt(ARG_POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getMyApplication().getAppComponent().inject(this);

        if (getArguments() != null) {
            position = getArguments().getInt(ARG_POSITION, 0);
            photoSetKey = getArguments().getParcelable(ARG_PHOTOSET_KEY);
        }

        if (savedInstanceState != null) {
            position = savedInstanceState.getInt(ARG_POSITION);
            isPlaying= savedInstanceState.getBoolean(ARG_IS_PLAYING);
        }

        setHasOptionsMenu(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_photo_pager, container, false);
    }

    @Override
    public void onViewCreated(View view,
                              Bundle savedInstanceState) {
        ButterKnife.bind(this, view);

        //postponeEnterTransition();

        adapter = new PhotoPagerAdapter();
        photoPager.setAdapter(adapter);

        progress.setVisibility(View.GONE);

        photoPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                listener.onPagerItemSelected(position, photoSet.getTotalItemCount());
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        //photoPager.setPageTransformer(true, new BackgroundToForegroundTransformer());


        userProvider = listener.getUsersProvider();

        refreshData(false);

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(ARG_POSITION, photoPager.getCurrentItem());
        outState.putBoolean(ARG_IS_PLAYING, isPlaying);
    }

    @Override
    public void onPause() {
        super.onPause();
        handler.removeCallbacksAndMessages(null);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (isPlaying) {
            startSlideshow();
        }
    }

    private Intent getShareIntent(Photo item) {
        final Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_STREAM, item.getUrl());
        return intent;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnPhotoPagerFragmentInteractionListener) {
            listener = (OnPhotoPagerFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_photo_pager, menu);

        MenuItem itemShare = menu.findItem(R.id.menu_share);


        super.onCreateOptionsMenu(menu, inflater);

    }


    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        MenuItem itemShare = menu.findItem(R.id.menu_share);
        if (adapter != null && adapter.getCount() > 0) {
            itemShare.setVisible(true);
            ShareActionProvider shareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(itemShare);
            shareActionProvider.setShareIntent(getShareIntent(adapter.getItem(photoPager.getCurrentItem())));
        } else {
            itemShare.setVisible(false);
        }

        MenuItem itemComments = menu.findItem(R.id.menu_comments);
        itemComments.setVisible(adapter.getCount() > 0);

        MenuItem itemDownload = menu.findItem(R.id.menu_download);
        itemDownload.setVisible(adapter.getCount() > 0);

        MenuItem itemInfo = menu.findItem(R.id.menu_info);
        itemInfo.setVisible(adapter.getCount() > 0);

        MenuItem itemWallpaper = menu.findItem(R.id.menu_wallpaper);
        itemWallpaper.setVisible(adapter.getCount() > 0);


        MenuItem itemSlideshow = menu.findItem(R.id.menu_slideshow);
        itemSlideshow.setVisible(adapter.getCount() > 0);
        itemSlideshow.setIcon(isPlaying ? R.drawable.ic_pause_black_24dp : R.drawable.ic_play_arrow_black_24dp);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_comments:
                onMenuComments();
                return true;
            case R.id.menu_download:
                onMenuDownload();
                return true;
            case R.id.menu_info:
                onMenuInfo();
                return true;

            case R.id.menu_wallpaper:
                onMenuWallpaper();
                return true;
            case R.id.menu_slideshow:
                onMenuSlideshow();
                return true;




        }

        return super.onOptionsItemSelected(item);
    }

    private void onMenuComments() {
        final Photo item = adapter.getItem(photoPager.getCurrentItem());
        ContextCompat.startActivity(getContext(), CommentsActivity.createIntent(getContext(), item.getCommentPoolId()), null);
    }

    private void onMenuDownload() {
        final Photo item = adapter.getItem(photoPager.getCurrentItem());

        subscribe(downloaderManager.submitDownload(getActivity(), item.getUrl(), Collections.EMPTY_MAP, Collections.EMPTY_MAP)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(o -> {
                    showSnackBar("Photo download started");
                }, throwable -> {
                    showSnackBar("Photo download error!");
                }, () -> {
                }));


    }

    private void onMenuInfo() {
        final Photo item = adapter.getItem(photoPager.getCurrentItem());
        ContextCompat.startActivity(getContext(), PhotoInfoActivity.createIntent(getContext(), item.getId()), null);
    }

    private void onMenuSlideshow() {

        if (!isPlaying) {
            startSlideshow();
        } else {
            stopSlideshow();
        }


        getActivity().invalidateOptionsMenu();
    }

    private void stopSlideshow() {
        isPlaying = false;
        handler.removeCallbacksAndMessages(null);
        showSnackBar(R.id.coordinator, "Slideshow stopped");
    }

    private void startSlideshow() {
        switchPage();
        isPlaying = true;
        showSnackBar(R.id.coordinator, "Slideshow started");
    }

    private void switchPage() {
        handler.postDelayed(changePageRunnable, SLIDESHOW_TIMEOUT);
    }


    private void onMenuWallpaper() {
        final Photo item = adapter.getItem(photoPager.getCurrentItem());
        final Uri uri = Uri.parse(item.getUrl());

        subscribe(new RxPermissions(getActivity())
                .request(Manifest.permission.SET_WALLPAPER)
                .subscribe(granted -> {
                            if (granted) {
                                setWallpaper(uri);
                            } else {
                                showSnackBar(R.id.coordinator, "Error while setting wallpaper!");
                            }
                        },
                        throwable -> {
                        },
                        () -> {

                        }));
    }

    private void setWallpaper(Uri uri) {
        imageLoader.load(uri, ImageLoader.Transform.NONE, "", new ImageLoader.Listener() {
            @Override
            public void onImageLoadSuccess(Bitmap bitmap) {
                WallpaperManager wallpaperManager = WallpaperManager.getInstance(getContext().getApplicationContext());
                try {
                    wallpaperManager.setBitmap(bitmap);
                    showSnackBar(R.id.coordinator, "Wallpaper has been set.");
                } catch (Exception ex) {
                    Timber.e(ex, "Error while setting wallpaper!");
                    showSnackBar(R.id.coordinator, "Error while setting wallpaper!");
                }
            }

            @Override
            public void onImageLoadError() {
                Timber.e("Error while setting wallpaper!");
                showSnackBar(R.id.coordinator, "Error while setting wallpaper!");
            }
        });
    }

    private void showBottomLayout(View layout, boolean show) {
        if (!show) {
            layout.setVisibility(View.GONE);
        } else {
            layout.setVisibility(View.VISIBLE);
        }
        this.showBottomLayout = show;
    }

    public void showBottomLayout(boolean show) {
        int childCount = photoPager.getChildCount();
        for (int i=0; i<childCount; ++i) {

            PhotoPagerAdapter.PhotoItem item = (PhotoPagerAdapter.PhotoItem)photoPager.getChildAt(i).getTag();
            final View bottomLayout = item.bottomLayout;
            showBottomLayout(bottomLayout, show);
        }
    }


    private void refreshData(boolean force) {

        loadPhotoSet(force);

    }


    private void loadPhotoSet(boolean force) {
        PhotoSetProvider photoSetProvider = listener.getPhotoSetProvider();
        subscribe(photoSetProvider.getPhotoSet(force, photoSetKey)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(() -> setRefreshing(true))
                .subscribe(photoSet -> {
                    adapter.setData(photoSet);
                    this.photoSet = photoSet;
                    photoPager.setCurrentItem(position, false);
                    listener.onPhotoSetLoaded(photoSetKey.getPhotoSetType(), photoSet);

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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnPhotoPagerFragmentInteractionListener {
        // TODO: Update argument type and name
        void onPagerItemClick(int position, Uri item);

        void onPagerItemSelected(int position, int totalItems);

        PhotoSetProvider getPhotoSetProvider();

        UsersProvider getUsersProvider();


        void onPhotoItemUserClick(User user);

        void onPagerItemPhotoTagClick(Photo item, CharSequence text);


        void onPhotoSetLoaded(PhotoSetProvider.PhotoSetType type, PhotoSet photoSet);

        void onPhotoItemCommentsClick(Photo item);


        void onPagerItemPhotoPlayClick(Photo item);
    }
}
