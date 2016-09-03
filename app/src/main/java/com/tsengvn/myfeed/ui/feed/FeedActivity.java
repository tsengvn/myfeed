package com.tsengvn.myfeed.ui.feed;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.tsengvn.myfeed.R;
import com.tsengvn.myfeed.internal.di.component.AppComponent;
import com.tsengvn.myfeed.pojo.Post;
import com.tsengvn.myfeed.ui.add.AddActivity;
import com.tsengvn.myfeed.ui.base.BaseActivity;
import com.tsengvn.myfeed.ui.base.BasePresenter;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

public class FeedActivity extends BaseActivity implements FeedView {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.fab)
    FloatingActionButton floatButton;

    @Inject
    FeedPresenter feedPresenter;

    private FeedItemAdapter feedItemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bindViews();
    }

    @Override
    protected void onStart() {
        feedPresenter.resumeSyncing();
        super.onStart();
    }

    @Override
    protected void onStop() {
        feedPresenter.pauseSyncing();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        feedPresenter.stopSyncing();
        super.onDestroy();
    }

    private void bindViews() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
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
    public BasePresenter getPresenter() {
        return feedPresenter;
    }

    @Override
    public void showError() {
        Snackbar
                .make(recyclerView, "Error, try again later.", Snackbar.LENGTH_LONG)
                .show();
    }

    @Override
    public void onReceiveNewPosts(List<Post> posts) {
        if (feedItemAdapter == null) createAdapter();

        feedItemAdapter.onNewPosts(posts);
    }

    public void showNewPostNotice() {
        Snackbar
                .make(recyclerView, R.string.new_post_message, Snackbar.LENGTH_LONG)
                .setAction(R.string.show, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        recyclerView.smoothScrollToPosition(0);
                    }
                })
                .show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0 && resultCode == RESULT_OK) {
            showNewPostNotice();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void openAddScreen() {
        startActivityForResult(new Intent(this, AddActivity.class), 0);
    }

    private void createAdapter() {
        feedItemAdapter = new FeedItemAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(feedItemAdapter);
    }
}
