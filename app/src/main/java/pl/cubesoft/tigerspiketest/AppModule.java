package pl.cubesoft.tigerspiketest;

import android.app.Application;
import android.content.Context;

import java.util.Collections;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import pl.cubesoft.tigerspiketest.data.Provider;
import pl.cubesoft.tigerspiketest.provider.flickr.ProviderImpl;


@Module
public class AppModule {

    Application application;



    public AppModule(Application application) {
        this.application = application;
    }

    @Provides
    @Singleton
    Application provideApplication() {
        return application;
    }







    @Provides
    @Singleton
    public DownloaderManager provideDownloaderManager(Application context) {
        return new DownloaderManager(context, () -> Collections.EMPTY_MAP);
    }




    @Provides
    public ImageLoader provideImageLoader(Provider provider) {
        return provider.getImageLoader();
    }





    @Provides
    @Singleton
    public Provider provideProvider(Application context) {

        return new ProviderImpl(context);
    }



}
