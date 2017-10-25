package pl.cubesoft.tigerspiketest.fragment;

import android.support.v4.app.Fragment;


import pl.cubesoft.tigerspiketest.TigerSpikeApplication;
import pl.cubesoft.tigerspiketest.activity.BaseActivity;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by CUBESOFT on 14.09.2017.
 */

public class BaseFragment extends Fragment {

    final CompositeSubscription compositeSubscription = new CompositeSubscription();
    private Object baseActivity;

    protected void subscribe (Subscription subscription) {
        compositeSubscription.add(subscription);
    }

    protected void unsubscribe (Subscription subscription) {
        compositeSubscription.remove(subscription);
    }


    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        compositeSubscription.clear();
    }

    protected void showSnackBar(CharSequence text) {
        getBaseActivity().showSnackBar(text);
    }

    protected void showSnackBar(int viewId, CharSequence text) {
        getBaseActivity().showSnackBar(viewId, text);
    }

    protected void showProgress(boolean show, int text) {
        getBaseActivity().showProgress(show, text);
    }


    public BaseActivity getBaseActivity() {
        return (BaseActivity) getActivity();
    }

    protected TigerSpikeApplication getMyApplication() {
        return (TigerSpikeApplication) getBaseActivity().getApplication();
    }

    protected void playVideo(String url) {
        getBaseActivity().playVideo(url);
    }
}
