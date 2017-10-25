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
import pl.cubesoft.tigerspiketest.fragment.GroupFragment;
import pl.cubesoft.tigerspiketest.fragment.PhotoSetFragment;


/**
 * Created by CUBESOFT on 01.09.2017.
 */

public class BrowserMainAdapter extends FragmentStatePagerAdapter {


    private final List<Provider.BrowserViewType> viewTypes;
    private final Resources resources;

    public BrowserMainAdapter(FragmentManager fm, Resources resources, List<Provider.BrowserViewType> viewTypes) {
        super(fm);
        this.viewTypes = viewTypes;
        this.resources = resources;
    }

    @Override
    public Fragment getItem(int position) {
        switch (viewTypes.get(position)) {
            case POPULAR_PHOTOS:
                return PhotoSetFragment.createInstance(PhotoSetProvider.PhotoSetType.POPULAR, PhotoSetFragment.DisplayType.NORMAL);
            case RECENT_PHOTOS:
                return PhotoSetFragment.createInstance(PhotoSetProvider.PhotoSetType.RECENT, PhotoSetFragment.DisplayType.TIMELINE);
            case VOTED_PHOTOS:
                return PhotoSetFragment.createInstance(PhotoSetProvider.PhotoSetType.VOTED, PhotoSetFragment.DisplayType.NORMAL);
            case HIGHEST_RATED_PHOTOS:
                return PhotoSetFragment.createInstance(PhotoSetProvider.PhotoSetType.HIGHEST_RATED, PhotoSetFragment.DisplayType.NORMAL);
            case POPULAR_SETS:
                return GroupFragment.createInstance(GroupProvider.GroupType.POPULAR);
            case RECENT_SETS:
                return GroupFragment.createInstance(GroupProvider.GroupType.RECENT);
        }
        return null;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (viewTypes.get(position)) {
            case POPULAR_PHOTOS:
            case POPULAR_SETS:
                return resources.getString(R.string.popular);

            case VOTED_PHOTOS:
                return resources.getString(R.string.editors);
            case HIGHEST_RATED_PHOTOS:
                return resources.getString(R.string.highest_rated);


            case RECENT_PHOTOS:
            case RECENT_SETS:
                return resources.getString(R.string.recent);
        }
        return null;
    }

    @Override
    public int getCount() {
        return viewTypes.size();
    }
}
