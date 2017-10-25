package pl.cubesoft.tigerspiketest.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.cubesoft.tigerspiketest.R;
import pl.cubesoft.tigerspiketest.data.CommentsProvider;
import pl.cubesoft.tigerspiketest.data.Provider;
import pl.cubesoft.tigerspiketest.data.model.Comment;
import pl.cubesoft.tigerspiketest.data.model.CommentPool;
import pl.cubesoft.tigerspiketest.fragment.CommentsFragment;
import pl.cubesoft.tigerspiketest.utils.TextUtils;


/**
 * Created by CUBESOFT on 18.09.2017.
 */

public class CommentsActivity extends BaseActivity implements CommentsFragment.OnCommentsFragmentInteractionListener {

    private static final String ARG_COMMENT_POOL_ID = "commentPoolId";

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Inject
    Provider provider;
    private String commentPoolId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getMyApplication().getAppComponent().inject(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        commentPoolId = getIntent().getStringExtra(ARG_COMMENT_POOL_ID);


        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, CommentsFragment.createInstance(commentPoolId))
                    .commit();
        }
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

    public static Intent createIntent(Context context, @Nullable String commentPoolId) {
        return new Intent(context, CommentsActivity.class)
                .putExtra(ARG_COMMENT_POOL_ID, commentPoolId);
    }


    @Override
    public CommentsProvider getCommentsProvider() {
        return provider.getCommentsProvider();
    }

    @Override
    public void onCommentItemClick(int position, View view, Comment item) {

    }

    @Override
    public void onCommentItemCreatorClick(int position, ImageView view, Comment item) {
        ContextCompat.startActivity(this, UserDetailActivity.createIntent(this, item.getCreatorId(), null), null);
    }

    @Override
    public void onCommentPoolLoaded(String commentPoolId, CommentPool commentPool) {
        getSupportActionBar().setSubtitle(TextUtils.formatItems(commentPool.getTotalItemCount()));
    }
}
