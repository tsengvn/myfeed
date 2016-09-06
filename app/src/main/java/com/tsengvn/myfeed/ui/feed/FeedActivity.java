package com.tsengvn.myfeed.ui.feed;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.tsengvn.myfeed.R;
import com.tsengvn.myfeed.internal.di.component.AppComponent;
import com.tsengvn.myfeed.pojo.Post;
import com.tsengvn.myfeed.ui.add.AddActivity;
import com.tsengvn.myfeed.ui.base.BaseActivity;

import java.util.List;

import butterknife.BindView;


public class FeedActivity extends BaseActivity<FeedPresenter> implements FeedView, FeedItemAdapter.OnItemLongClickListener {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.fab)
    FloatingActionButton floatButton;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private FeedItemAdapter feedItemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bindViews();

    }

    @Override
    protected void onStart() {
        getPresenter().resumeSyncing();
        super.onStart();
    }

    @Override
    protected void onStop() {
        getPresenter().pauseSyncing();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        getPresenter().stopSyncing();
        super.onDestroy();
    }

    private void bindViews() {
        setSupportActionBar(toolbar);

        floatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAddScreen();
            }
        });
    }

    @Override
    protected void initInjector(AppComponent appComponent) {
        appComponent.inject(this);
    }

    @Override
    public void showError(String message) {
        Snackbar
                .make(recyclerView, message != null ? message : "Error, try again later.", Snackbar.LENGTH_INDEFINITE)
                .show();
    }

    @Override
    public void onReceiveNewPosts(List<Post> posts) {
        if (feedItemAdapter == null) createAdapter();

        feedItemAdapter.onNewPosts(posts);
    }

    @Override
    public void onPostDeleted(Post post) {
        feedItemAdapter.onPostDeleted(post);
    }

    @Override
    public void showNewPostNotice() {
        if (feedItemAdapter == null) return;

        Snackbar
                .make(recyclerView, R.string.new_post_message, Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.show, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        recyclerView.smoothScrollToPosition(0);
                    }
                })
                .show();
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == 0 && resultCode == RESULT_OK) {
//            showNewPostNotice();
//        }
//        super.onActivityResult(requestCode, resultCode, data);
//    }

    public void openAddScreen() {
        startActivity(new Intent(this, AddActivity.class));
    }

    private void createAdapter() {
        feedItemAdapter = new FeedItemAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(feedItemAdapter);
        feedItemAdapter.setItemLongClickListener(this);
    }

    @Override
    public void onItemLongClick(int position, final Post post) {
        new AlertDialog.Builder(this)
                .setMessage(R.string.delete_message)
                .setNegativeButton(android.R.string.cancel, null)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        getPresenter().removePost(post);
                    }
                })
                .show();

    }
}
