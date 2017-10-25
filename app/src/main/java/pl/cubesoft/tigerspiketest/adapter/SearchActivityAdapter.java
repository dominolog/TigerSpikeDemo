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

public class SearchActivityAdapter extends FragmentStatePagerAdapter {


    private final List<Provider.SearchViewType> viewTypes;
    private final String searchText;
    private final Resources resources;

    public SearchActivityAdapter(FragmentManager fm, Resources resources, List<Provider.SearchViewType> viewTypes, String searchText) {
        super(fm);
        this.resources = resources;
        this.viewTypes = viewTypes;
        this.searchText = searchText;
    }

    @Override
    public Fragment getItem(int position) {
        switch (viewTypes.get(position)) {
            case PHOTOS:
                return PhotoSetFragment.createInstance(PhotoSetProvider.PhotoSetType.SEARCH, PhotoSetFragment.DisplayType.NORMAL);
            case SETS:
                return GroupFragment.createInstance(GroupProvider.GroupType.SEARCH);
            case USERS:
                // implement search users
                return UsersFragment.createInstance(UsersProvider.UserSetType.SEARCH);


        }
        return null;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (viewTypes.get(position)) {
            case PHOTOS:
                return resources.getString(R.string.photos);
            case SETS:
                return resources.getString(R.string.sets);
            case USERS:
                // implement search users
                return resources.getString(R.string.users);

        }
        return null;
    }

    @Override
    public int getCount() {
        return viewTypes.size();
    }
}
