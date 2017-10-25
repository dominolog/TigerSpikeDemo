package pl.cubesoft.tigerspiketest.activity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;

import java.util.List;

import pl.cubesoft.tigerspiketest.R;
import pl.cubesoft.tigerspiketest.TigerSpikeApplication;
import pl.cubesoft.tigerspiketest.fragment.ProgressFragment;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

import static android.support.design.widget.Snackbar.LENGTH_SHORT;

/**
 * Created by CUBESOFT on 13.09.2017.
 */

public class BaseActivity extends AppCompatActivity {


    private static final String PROGRESS_DIALOG_TAG = "progressDialog";


    protected TigerSpikeApplication getMyApplication() {
        return (TigerSpikeApplication) getApplication();
    }

    final CompositeSubscription compositeSubscription = new CompositeSubscription();

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        final boolean result = super.onCreateOptionsMenu(menu);
        //MenuUtils.applyTint(menu, ContextCompat.getColor(this, R.color.toolbar_icon_color));
        return result;
    }



    public void showSnackBar(CharSequence text) {
        Snackbar.make(findViewById(android.R.id.content), text, LENGTH_SHORT).show();
    }

    public void showSnackBar(int viewId, CharSequence text) {
        Snackbar.make(findViewById(viewId), text, LENGTH_SHORT).show();
    }


    public void showProgress(boolean show, int text) {
        showProgressInternal(text, show);
    }

    private void showProgressInternal(int message, boolean show) {
        final DialogFragment progressFragment = (ProgressFragment) getSupportFragmentManager().findFragmentByTag(PROGRESS_DIALOG_TAG);
        if (progressFragment != null) {
            progressFragment.dismiss();
        }

        if (show) {
            ProgressFragment.show(this, message, getSupportFragmentManager(), PROGRESS_DIALOG_TAG);
        }
    }

    protected void showUrl(String url) {
        try {
            if ( !url.startsWith("http") ) {
                url = "http://" + url;
            }
            ContextCompat.startActivity(this, new Intent(Intent.ACTION_VIEW, Uri.parse(url)), null);
        } catch (ActivityNotFoundException ex) {
            Timber.e("No activity to display %s", url);
        }

    }

    public void playVideo(String url) {

        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.parse(url), "video/*");
            ContextCompat.startActivity(this, intent, null);
        } catch (ActivityNotFoundException ex) {
            Timber.e("Cannot play video: %s", url);
            showSnackBar(R.id.coordinator, "Unable to play video!");
        }
    }



}
