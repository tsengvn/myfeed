package com.tsengvn.myfeed.internal.di.component;

import com.tsengvn.myfeed.internal.di.module.AppModule;
import com.tsengvn.myfeed.ui.add.AddActivity;
import com.tsengvn.myfeed.ui.feed.FeedActivity;
import com.tsengvn.myfeed.ui.gallery.GalleryActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * @author : hienngo
 * @since : Sep 01, 2016.
 */
@Singleton
@Component(modules = AppModule.class)
public interface AppComponent {
    void inject(FeedActivity feedActivity);

    void inject(AddActivity addActivity);

    void inject(GalleryActivity galleryActivity);
}
