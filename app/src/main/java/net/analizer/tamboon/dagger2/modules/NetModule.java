package net.analizer.tamboon.dagger2.modules;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import net.analizer.datalayer.api.RetrofitInterface;
import net.analizer.tamboon.BuildConfig;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class NetModule {
    private static final int TIMEOUT = 30;

    private String mBaseApiUrl;

    public NetModule(String baseApiUrl) {
        this.mBaseApiUrl = baseApiUrl;
    }

    @Provides
    @Singleton
    public Gson provideGson() {
        return new GsonBuilder()
                .setDateFormat("yyyy-MM-dd HH:mm:ssZ")
                .create();
    }

    @Provides
    @Singleton
    public OkHttpClient provideOkHttpClient() {

        OkHttpClient.Builder mOkHttpBuilder = new OkHttpClient.Builder();
        mOkHttpBuilder.writeTimeout(TIMEOUT, TimeUnit.SECONDS);
        mOkHttpBuilder.readTimeout(TIMEOUT, TimeUnit.SECONDS);
        mOkHttpBuilder.connectTimeout(TIMEOUT, TimeUnit.SECONDS);

        if (!BuildConfig.PRODUCTION || BuildConfig.DEBUG) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            mOkHttpBuilder.addInterceptor(logging);
        }

        return mOkHttpBuilder.build();
    }

    @Provides
    @Singleton
    public RetrofitInterface provideService(Gson gson, OkHttpClient okHttpClient) {

        return new Retrofit.Builder()
                .baseUrl(mBaseApiUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .build()
                .create(RetrofitInterface.class);
    }
}
