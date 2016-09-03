package com.tsengvn.myfeed.ui.feed;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tsengvn.myfeed.R;
import com.tsengvn.myfeed.pojo.Post;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

/**
 * @author : hienngo
 * @since : Sep 01, 2016.
 */
public class FeedItemAdapter extends RecyclerView.Adapter<FeedItemAdapter.ViewHolder> {
    private List<Post> posts;
    private LayoutInflater layoutInflater;

    public FeedItemAdapter(Context context) {
        posts = new ArrayList<>();
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public FeedItemAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_feed, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FeedItemAdapter.ViewHolder holder, int position) {
        Post post = posts.get(position);
        holder.textView.setText(post.getText());
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public void onNewPosts(List<Post> posts) {
        this.posts.addAll(0, posts);
        notifyItemRangeInserted(0, posts.size());
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView textView;
        ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);

            textView = ButterKnife.findById(itemView, R.id.text);
            imageView = ButterKnife.findById(itemView, R.id.image);
        }
    }
}
