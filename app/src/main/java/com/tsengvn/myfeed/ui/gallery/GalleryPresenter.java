package com.tsengvn.myfeed.ui.gallery;

import android.util.Log;

import com.tsengvn.myfeed.domain.interactor.DataService;
import com.tsengvn.myfeed.pojo.Image;
import com.tsengvn.myfeed.ui.base.BasePresenter;

import java.util.List;

import javax.inject.Inject;

import rx.Observer;
import rx.Subscription;
import rx.functions.Action1;

/**
 * @author : hienngo
 * @since : Sep 03, 2016.
 */
public class GalleryPresenter extends BasePresenter<GalleryView> {
    private final DataService service;

    private int currentPage = 0;
    private Subscription subscription;

    @Inject
    public GalleryPresenter(DataService service) {
        this.service = service;
    }

    public void loadMore() {
        ++currentPage;
        load();
    }

    public void loadData() {
        currentPage = 0;
        load();
    }

    private void load() {
        subscription = service.loadImages(currentPage)
                .subscribe(new Action1<List<Image>>() {
                    @Override
                    public void call(List<Image> images) {
                        getView().onReceiveData(images, currentPage == 0);
                    }
                });
    }

    public void onDestroy() {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
            subscription = null;
        }
    }
}
