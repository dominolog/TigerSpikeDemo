package pl.cubesoft.tigerspiketest.utils;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import pl.cubesoft.tigerspiketest.BuildConfig;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 * Created by CUBESOFT on 23.09.2017.
 */

public class NetworkUtils {

    public static OkHttpClient.Builder buildStandardHttpClientBuilder() {

        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .followRedirects(true);

        if (BuildConfig.DEBUG) {
            final HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(interceptor);
        }

        return builder;
    }

    public static Retrofit.Builder buildStandardRetrofitBuilder() {
        return new Retrofit.Builder()
                .addConverterFactory(JacksonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create());
    }


}
