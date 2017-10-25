package pl.cubesoft.tigerspiketest.adapter;

import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

import pl.cubesoft.tigerspiketest.R;
import pl.cubesoft.tigerspiketest.data.GroupProvider;
import pl.cubesoft.tigerspiketest.data.PhotoSetProvider;
import pl.cubesoft.tigerspiketest.data.Provider;
import pl.cubesoft.tigerspiketest.data.UsersProvider;
import pl.cubesoft.tigerspiketest.fragment.GroupFragment;
import pl.cubesoft.tigerspiketest.fragment.PhotoSetFragment;
import pl.cubesoft.tigerspiketest.fragment.UsersFragment;


/**
 * Created by CUBESOFT on 01.09.2017.
 */

public class UserDetailAdapter extends FragmentStatePagerAdapter {

    private final String userId;
    private final Resources resources;
    private final List<Provider.UserDetailsViewType> viewTypes;

    public UserDetailAdapter(FragmentManager fm, Resources resources, List<Provider.UserDetailsViewType> viewTypes, String userId) {
        super(fm);
        this.resources = resources;
        this.viewTypes = viewTypes;
        this.userId = userId;
    }

    @Override
    public Fragment getItem(int position) {
        switch (viewTypes.get(position)) {
            case POPULAR_PHOTOS:
                return PhotoSetFragment.createInstance(PhotoSetProvider.PhotoSetType.USER_POPULAR, PhotoSetFragment.DisplayType.NORMAL);
            case RECENT_PHOTOS:
                return PhotoSetFragment.createInstance(PhotoSetProvider.PhotoSetType.USER_RECENT, PhotoSetFragment.DisplayType.NORMAL);
            case FAVORITE_PHOTOS:
                return PhotoSetFragment.createInstance(PhotoSetProvider.PhotoSetType.USER_FAVORITES, PhotoSetFragment.DisplayType.NORMAL);

            case FRIENDS_PHOTOS:
                return PhotoSetFragment.createInstance(PhotoSetProvider.PhotoSetType.USER_FRIENDS_PHOTOS, PhotoSetFragment.DisplayType.TIMELINE);

            case POPULAR_SETS:
                return GroupFragment.createInstance(GroupProvider.GroupType.USER_POPULAR);

            case SETS:
                return GroupFragment.createInstance(GroupProvider.GroupType.USER_SETS);

            case GALLERIES:
                return GroupFragment.createInstance(GroupProvider.GroupType.USER_GALLERIES);

            case FRIENDS:
                return UsersFragment.createInstance(UsersProvider.UserSetType.FRIENDS);
            case FOLLOWERS:
                return UsersFragment.createInstance(UsersProvider.UserSetType.FOLLOWERS);




        }
        return null;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (viewTypes.get(position)) {
            case POPULAR_PHOTOS:
                return resources.getString(R.string.popular_photos);
            case RECENT_PHOTOS:
                return resources.getString(R.string.recent_photos);
            case FAVORITE_PHOTOS:
                return resources.getString(R.string.favorites);
            case FRIENDS_PHOTOS:
                return resources.getString(R.string.friends_photos);
            case POPULAR_SETS:
                return resources.getString(R.string.popular_sets);
            case SETS:
                return resources.getString(R.string.sets);

            case GALLERIES:
                return resources.getString(R.string.galleries);
            case RECENT_SETS:
                return resources.getString(R.string.recent);

            case FOLLOWERS:
                return resources.getString(R.string.followers);
            case FRIENDS:
                return resources.getString(R.string.friends);

        }
        return null;
    }

    @Override
    public int getCount() {
        return viewTypes.size();
    }
}
