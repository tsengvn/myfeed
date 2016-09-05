package com.tsengvn.myfeed.domain.interactor;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.tsengvn.myfeed.domain.repo.ImgurRepo;
import com.tsengvn.myfeed.domain.repo.PostRepo;
import com.tsengvn.myfeed.pojo.Image;
import com.tsengvn.myfeed.pojo.Post;

import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subjects.ReplaySubject;

/**
 * @author : hienngo
 * @since : Sep 01, 2016.
 */
public class DataService {
    private final PostRepo postRepo;
    private final ImgurRepo imgurRepo;

    private ReplaySubject<Post> syncAddSubject = null;
    private ReplaySubject<Post> syncDeleteSubject = null;

    public DataService(PostRepo postRepo, ImgurRepo imgurRepo) {
        this.postRepo = postRepo;
        this.imgurRepo = imgurRepo;
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
                .toList()
                .single()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());

    }

    public Observable<Boolean> addPost(String text, String imgUrl, float imgRatio) {
        Post post = new Post(text, imgUrl, imgRatio, System.currentTimeMillis());
        return Observable.just(post)
                .map(new Func1<Post, Boolean>() {
                    @Override
                    public Boolean call(Post post) {
                        postRepo.createPost(post);
                        return true;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());

    }

    public Subscription startSyncingPost(final long lastSyncedTime, Observer<List<Post>> observer) {
        if (syncAddSubject == null) {
            syncAddSubject = ReplaySubject.create();
            syncDeleteSubject = ReplaySubject.create();
            postRepo.addChildEventListener(childEventListener);
        }
        return Observable.merge(
                        syncAddSubject.distinct().filter(new Func1<Post, Boolean>() {
                            @Override
                            public Boolean call(Post post) {
                                return post.getCreated() > lastSyncedTime;
                            }
                        }).buffer(1, TimeUnit.SECONDS, 100),
                        syncDeleteSubject.distinct().buffer(1, TimeUnit.SECONDS, 100)
                )
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
        syncAddSubject.onCompleted();
        syncAddSubject = null;
        syncDeleteSubject.onCompleted();
        syncDeleteSubject = null;

        postRepo.removeChildEventListener(childEventListener);
    }

    private final ChildEventListener childEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            Post post = dataSnapshot.getValue(Post.class);
            post.setStatus(Post.Status.Add);
            post.setKey(dataSnapshot.getKey());

            syncAddSubject.onNext(post);
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            //not support
        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {
            Post post = dataSnapshot.getValue(Post.class);
            post.setStatus(Post.Status.Remove);
            post.setKey(dataSnapshot.getKey());

            syncDeleteSubject.onNext(post);

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

    public Observable<Boolean> removePost(Post post) {
        return Observable.just(post)
                .map(new Func1<Post, Boolean>() {
                    @Override
                    public Boolean call(Post post) {
                        postRepo.remove(post);
                        return true;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }
}
