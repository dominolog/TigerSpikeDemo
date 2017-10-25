package pl.cubesoft.tigerspiketest;

import android.app.Application;
import android.content.Context;



import timber.log.Timber;

/**
 * Created by CUBESOFT on 13.09.2017.
 */

public class TigerSpikeApplication extends Application {
    private AppComponent appComponent;


    @Override
    public void onCreate () {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }


        appComponent = DaggerAppComponent.builder().appModule(new AppModule(this)).build();
        //setupNetwork();

        appComponent.inject(this);


        // provide headers

    }

    public AppComponent getAppComponent() {
        return appComponent;
    }
}
