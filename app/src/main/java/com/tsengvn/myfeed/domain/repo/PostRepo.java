package com.tsengvn.myfeed.domain.repo;

import android.content.Context;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.tsengvn.myfeed.pojo.Post;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import rx.Observable;
import rx.Subscriber;

/**
 * @author : hienngo
 * @since  : Sep 01, 2016.
 */
public class PostRepo {
    private static final String TAG = "PostRepo";
    private static final String URL = "https://myfeed-b4a00.firebaseio.com/";

    private Firebase firebase;


    public PostRepo(Context context) {
        Firebase.setAndroidContext(context);
        Firebase.getDefaultConfig().setPersistenceEnabled(true);

        firebase = new Firebase(URL).child("post");
    }

    public void addChildEventListener(ChildEventListener childEventListener) {
        firebase.addChildEventListener(childEventListener);
    }

    public void removeChildEventListener(ChildEventListener childEventListener) {
        firebase.removeEventListener(childEventListener);
    }

    public void createPost(final Post post) {
        firebase.push().setValue(post);
    }

    public Observable<List<Post>> getPost(final int start, int offset) {
        return Observable.create(new Observable.OnSubscribe<List<Post>>() {
            @Override
            public void call(final Subscriber<? super List<Post>> subscriber) {
                firebase.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        firebase.removeEventListener(this);
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });
                firebase.limitToLast(100).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        List<Post> result = new ArrayList<>();
                        Iterator<DataSnapshot> snapshotIterator = dataSnapshot.getChildren().iterator();
                        while (snapshotIterator.hasNext()) {
                            Post post = snapshotIterator.next().getValue(Post.class);
                            result.add(post);
                        }
                        Collections.reverse(result);
                        if (!subscriber.isUnsubscribed()) {
                            subscriber.onNext(result);
                            subscriber.onCompleted();
                        }
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                        if(!subscriber.isUnsubscribed()) {
                            subscriber.onError(firebaseError.toException());
                            subscriber.onCompleted();
                        }
                    }
                });
            }
        });
    }

    public void remove(Post post) {
        firebase.child(post.getKey()).removeValue();
    }
}
