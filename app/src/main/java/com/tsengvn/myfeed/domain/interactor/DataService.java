package com.tsengvn.myfeed.domain.interactor;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.tsengvn.myfeed.domain.repo.ImgurRepo;
import com.tsengvn.myfeed.domain.repo.PostRepo;
import com.tsengvn.myfeed.pojo.Image;
import com.tsengvn.myfeed.pojo.Post;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.schedulers.Timestamped;
import rx.subjects.BehaviorSubject;
import rx.subjects.ReplaySubject;

/**
 * @author : hienngo
 * @since : Sep 01, 2016.
 */
public class DataService {
    private final PostRepo postRepo;
    private final ImgurRepo imgurRepo;

    private ReplaySubject<Post> syncSubject = null;

    public DataService(PostRepo postRepo, ImgurRepo imgurRepo) {
        this.postRepo = postRepo;
        this.imgurRepo = imgurRepo;
    }

    public Observable<List<Post>> loadPost(int start) {
        return postRepo.getPost(0, 0);
    }

    public Observable<List<Image>> loadImages(int page) {
        return imgurRepo.getRandomImage(page)
                .flatMap(new Func1<Image.List, Observable<Image>>() {
                    @Override
                    public Observable<Image> call(Image.List list) {
                        return Observable.from(list.getData());
                    }
                })
                .filter(new Func1<Image, Boolean>() {
                    @Override
                    public Boolean call(Image image) {
                        return !image.isAnimated() && !image.isAlbum();
                    }
                })
                .toList();

    }

    public void addPost(String text, String imgUrl) {
        Post post = new Post(text, imgUrl, System.currentTimeMillis());
        postRepo.createPost(post);
    }

    public Subscription startSyncingPost(final long lastSyncedTime, Observer<List<Post>> observer) {
        if (syncSubject == null) {
            syncSubject = ReplaySubject.create();
            postRepo.addChildEventListener(childEventListener);
        }
        return syncSubject.asObservable()
                .filter(new Func1<Post, Boolean>() {
                    @Override
                    public Boolean call(Post post) {
                        return post.getCreated() > lastSyncedTime;
                    }
                })
                .buffer(500, TimeUnit.MILLISECONDS, 100)
                .filter(new Func1<List<Post>, Boolean>() {
                    @Override
                    public Boolean call(List<Post> posts) {
                        return posts.size() > 0;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(observer);
    }

    public void stopSyncingPost() {
        syncSubject.onCompleted();
        syncSubject = null;
        postRepo.removeChildEventListener(childEventListener);
    }

    private ChildEventListener childEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            //not support
            Post post = dataSnapshot.getValue(Post.class);
            syncSubject.onNext(post);
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            //not support
        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {
            //not support
        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            //not support
        }

        @Override
        public void onCancelled(FirebaseError firebaseError) {
            //not support
        }
    };
}
