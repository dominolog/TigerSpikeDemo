package pl.cubesoft.tigerspiketest.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.cubesoft.tigerspiketest.R;
import pl.cubesoft.tigerspiketest.data.Provider;
import pl.cubesoft.tigerspiketest.data.UserSetKey;
import pl.cubesoft.tigerspiketest.data.UsersProvider;
import pl.cubesoft.tigerspiketest.data.model.User;
import pl.cubesoft.tigerspiketest.data.model.UserSet;
import pl.cubesoft.tigerspiketest.fragment.UsersFragment;
import pl.cubesoft.tigerspiketest.utils.TextUtils;
import rx.Observable;

/**
 * Created by CUBESOFT on 18.09.2017.
 */

public class UsersActivity extends BaseActivity implements UsersFragment.OnUsersFragmentInteractionListener{

    private static final String ARG_TYPE = "type";
    private static final String ARG_USER_ID = "userId";

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Inject
    Provider provider;
    private UsersProvider.UserSetType userSetType;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getMyApplication().getAppComponent().inject(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        userSetType = (UsersProvider.UserSetType)getIntent().getSerializableExtra(ARG_TYPE);
        userId = getIntent().getStringExtra(ARG_USER_ID);

        switch (userSetType) {
            case FOLLOWERS:
                getSupportActionBar().setTitle(R.string.followers);
                break;

            case FRIENDS:
                getSupportActionBar().setTitle(R.string.following);
                break;
        }


        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, UsersFragment.createInstance(userSetType))
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

    public static Intent createIntent(Context context, UsersProvider.UserSetType userSetType, String userId) {
        return new Intent(context, UsersActivity.class)
                .putExtra(ARG_TYPE, userSetType)
                .putExtra(ARG_USER_ID, userId);
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
        getSupportActionBar().setSubtitle(TextUtils.formatItems(userSet.getTotalItemCount()));
    }
}
