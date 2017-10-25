package pl.cubesoft.tigerspiketest;

import javax.inject.Singleton;

import dagger.Component;

import dagger.android.AndroidInjector;
import pl.cubesoft.tigerspiketest.activity.BrowserMainActivity;
import pl.cubesoft.tigerspiketest.activity.CommentsActivity;
import pl.cubesoft.tigerspiketest.activity.PhotoInfoActivity;
import pl.cubesoft.tigerspiketest.activity.PhotoPagerActivity;
import pl.cubesoft.tigerspiketest.activity.PhotoSetActivity;
import pl.cubesoft.tigerspiketest.activity.SearchActivity;
import pl.cubesoft.tigerspiketest.activity.UserDetailActivity;
import pl.cubesoft.tigerspiketest.activity.UsersActivity;
import pl.cubesoft.tigerspiketest.fragment.CommentsFragment;
import pl.cubesoft.tigerspiketest.fragment.GroupFragment;
import pl.cubesoft.tigerspiketest.fragment.PhotoPagerFragment;
import pl.cubesoft.tigerspiketest.fragment.PhotoSetFragment;
import pl.cubesoft.tigerspiketest.fragment.UsersFragment;


/**
 * Created by CUBESOFT on 16.01.2017.
 */


@Singleton
@Component(modules={AppModule.class})
public interface AppComponent extends AndroidInjector<TigerSpikeApplication> {
    void inject(BrowserMainActivity browserMainActivity);

    void inject(PhotoSetFragment photoSetFragment);

    void inject(GroupFragment groupFragment);

    void inject(PhotoSetActivity photoSetActivity);

    void inject(PhotoPagerActivity photoPagerActivity);

    void inject(CommentsFragment commentsFragment);

    void inject(PhotoPagerFragment photoPagerFragment);

    void inject(UsersFragment usersFragment);

    void inject(CommentsActivity commentsActivity);

    void inject(PhotoInfoActivity photoInfoActivity);

    void inject(UserDetailActivity userDetailActivity);

    void inject(UsersActivity usersActivity);

    void inject(SearchActivity searchActivity);


    // to update the fields in your activities

}
