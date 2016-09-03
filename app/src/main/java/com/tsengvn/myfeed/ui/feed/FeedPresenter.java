package com.tsengvn.myfeed.ui.feed;

import android.util.Log;

import com.tsengvn.myfeed.domain.interactor.DataService;
import com.tsengvn.myfeed.pojo.Post;
import com.tsengvn.myfeed.ui.base.BasePresenter;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;

/**
 * @author : hienngo
 * @since : Sep 01, 2016.
 */
public class FeedPresenter extends BasePresenter<FeedView> {
    private static final String TAG = "FeedPresenter";

    private final DataService dataService;

    private Subscription subscription;
    private long lastSyncedTime = 0;

    @Inject
    public FeedPresenter(DataService dataService) {
        this.dataService = dataService;
    }

    public void pauseSyncing() {
        Log.v(TAG, "pauseSyncing");
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
            subscription = null;
        }
    }

    public void resumeSyncing() {
        if (subscription != null) return;
        Log.v(TAG, "resumeSyncing");

        subscription = dataService.startSyncingPost(lastSyncedTime, new Subscriber<List<Post>>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                getView().showError();
            }

            @Override
            public void onNext(List<Post> posts) {
                Log.v(TAG, "new post : " + posts.size());
                Collections.reverse(posts);
                if (lastSyncedTime != 0) {
                    getView().showNewPostNotice();
                }
                lastSyncedTime = posts.get(0).getCreated();
                getView().onReceiveNewPosts(posts);
            }
        });
    }

    public void stopSyncing() {
        pauseSyncing();
        Log.v(TAG, "stopSyncing");

        lastSyncedTime = 0;
        dataService.stopSyncingPost();
    }
}
