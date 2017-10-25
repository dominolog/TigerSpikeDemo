package pl.cubesoft.tigerspiketest.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;


import pl.cubesoft.tigerspiketest.R;
import pl.cubesoft.tigerspiketest.data.PhotoSetKey;
import pl.cubesoft.tigerspiketest.data.PhotoSetProvider;
import pl.cubesoft.tigerspiketest.data.Provider;
import pl.cubesoft.tigerspiketest.data.model.Photo;
import pl.cubesoft.tigerspiketest.data.model.PhotoSet;
import pl.cubesoft.tigerspiketest.fragment.PhotoSetFragment;
import rx.Observable;
import pl.cubesoft.tigerspiketest.utils.TextUtils;

/**
 * Created by CUBESOFT on 18.09.2017.
 */

public class PhotoSetActivity extends BaseActivity implements PhotoSetFragment.OnPhotoSetFragmentInteractionListener {

    private static final String ARG_PHOTOSET_KEY = "key";
    private static final String ARG_PHOTOSET_TITLE = "title";

    private static final int REQUEST_ADD = 100;
    private static final int REQUEST_PICK_MEDIA = 101;


    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.fab_add)
    FloatingActionButton fabAdd;

    @Inject
    Provider provider;
    private PhotoSetKey photoSetKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_set);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getMyApplication().getAppComponent().inject(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        fabAdd.setVisibility(View.GONE);

        photoSetKey = getIntent().getParcelableExtra(ARG_PHOTOSET_KEY);
        final String photoSetTitle = getIntent().getStringExtra(ARG_PHOTOSET_TITLE);
        if ( photoSetTitle != null ) {
            getSupportActionBar().setTitle(photoSetTitle);
        }


        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, PhotoSetFragment.createInstance(photoSetKey.getPhotoSetType(), PhotoSetFragment.DisplayType.NORMAL))
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.activity_photoset, menu);
        return true;
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


    @Override
    public void onPause() {
        super.onPause();


        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onResume() {
        super.onResume();



        EventBus.getDefault().register(this);

    }
    public static Intent createIntent(Context context, PhotoSetKey photoSetKey, @Nullable String photoSetTitle) {
        return new Intent(context, PhotoSetActivity.class)
                .putExtra(ARG_PHOTOSET_KEY, photoSetKey)
                .putExtra(ARG_PHOTOSET_TITLE, photoSetTitle);
    }





    @Override
    public Observable<PhotoSet> getPhotoSet(boolean force, PhotoSetProvider.PhotoSetType type) {
        return provider.getPhotoSetProvider().getPhotoSet(force, photoSetKey);
    }

    @Override
    public void onPhotoSetItemClick(int position, View view, Photo item, PhotoSet photoSet) {
        ContextCompat.startActivity(this, PhotoPagerActivity.createIntent(this, photoSet.getKey(), position, photoSet.getTitle()), null);
    }


    @Override
    public void onPhotoSetLoaded(PhotoSet photoSet) {
        getSupportActionBar().setTitle(photoSet.getTitle());
        getSupportActionBar().setSubtitle(TextUtils.formatItems(photoSet.getTotalItemCount()));


        fabAdd.setVisibility(provider.canUpload(photoSet) ? View.VISIBLE : View.GONE);
    }

    @Override
    public boolean canDeleteFromPhotoSet(PhotoSet photoSet) {
        return provider.canDeleteFromPhotoSet(photoSet);
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode,
                                    Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


    }
}
