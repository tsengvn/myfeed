package com.tsengvn.myfeed.internal.di.module;

import android.app.Application;
import android.content.Context;

import com.tsengvn.myfeed.BuildConfig;
import com.tsengvn.myfeed.domain.interactor.DataService;
import com.tsengvn.myfeed.domain.repo.ImgurRepo;
import com.tsengvn.myfeed.domain.repo.PostRepo;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 * @author : hienngo
 * @since : Sep 01, 2016.
 */
@Module
public class AppModule {
    private final Application application;

    public AppModule(Application application) {
        this.application = application;
    }

    @Provides
    @Singleton
    Context provideContext() {
        return this.application;
    }

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient() {
        return new OkHttpClient.Builder()
                .readTimeout(30, TimeUnit.SECONDS)
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();
    }

    @Provides
    @Singleton
    PostRepo providePostRepo(Context context) {
        return new PostRepo(context);
    }

    @Provides
    @Singleton
    ImgurRepo provideImgurRepo(OkHttpClient okHttpClient) {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();

        if (BuildConfig.DEBUG) {
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        }
        return new Retrofit.Builder()
                .baseUrl(new StringBuilder().append(ImgurRepo.URL).append("/")
                        .append(ImgurRepo.API_VERSION).append("/").toString())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(JacksonConverterFactory.create())
                .client(okHttpClient)
                .build()
                .create(ImgurRepo.class);
    }

    @Provides
    @Singleton
    DataService provideDataService(PostRepo postRepo, ImgurRepo imgurRepo) {
        return new DataService(postRepo, imgurRepo);
    }
}
