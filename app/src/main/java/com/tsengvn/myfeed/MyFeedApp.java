package com.tsengvn.myfeed;

import android.app.Application;
import android.content.Context;

import com.tsengvn.myfeed.internal.di.component.AppComponent;
import com.tsengvn.myfeed.internal.di.component.DaggerAppComponent;
import com.tsengvn.myfeed.internal.di.module.AppModule;

/**
 * @author : hienngo
 * @since  : Sep 01, 2016.
 */
public class MyFeedApp extends Application{
    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        initInjector();
    }

    private void initInjector() {
        this.appComponent = DaggerAppComponent.builder().appModule(new AppModule(this))
                .build();
    }

    public static AppComponent getAppComponent(Context context) {
        return ((MyFeedApp)context.getApplicationContext()).appComponent;
    }
}
