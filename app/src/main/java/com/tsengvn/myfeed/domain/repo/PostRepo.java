package com.tsengvn.myfeed.domain.repo;

import android.content.Context;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tsengvn.myfeed.pojo.Post;

/**
 * @author : hienngo
 * @since  : Sep 01, 2016.
 */
public class PostRepo {
    private static final String TAG = "PostRepo";

    private final DatabaseReference firebase;


    public PostRepo(Context context) {
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        firebase = FirebaseDatabase.getInstance().getReference("post");
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

    public void remove(Post post) {
        firebase.child(post.getKey()).removeValue();
    }

}
