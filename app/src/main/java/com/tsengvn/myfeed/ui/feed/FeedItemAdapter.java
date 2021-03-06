package com.tsengvn.myfeed.ui.feed;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tsengvn.myfeed.R;
import com.tsengvn.myfeed.pojo.Post;
import com.tsengvn.myfeed.ui.widget.FixedImageView;
import com.tsengvn.myfeed.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

/**
 * @author : hienngo
 * @since : Sep 01, 2016.
 */
public class FeedItemAdapter extends RecyclerView.Adapter<FeedItemAdapter.ViewHolder> {
    private volatile List<Post> posts;
    private LayoutInflater layoutInflater;
    private Context context;
    private OnItemLongClickListener itemLongClickListener;

    public FeedItemAdapter(Context context) {
        this.posts = new ArrayList<>();
        this.layoutInflater = LayoutInflater.from(context);
        this.context = context;
        Picasso.with(context).setLoggingEnabled(true);
    }

    @Override
    public FeedItemAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_feed, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FeedItemAdapter.ViewHolder holder, final int position) {
        final Post post = posts.get(position);
        holder.textView.setText(post.getText());
        if (StringUtils.isEmpty(post.getImgUrl())) {
            holder.imageView.setImageBitmap(null);
            holder.imageView.setRatio(0);
            holder.imageView.setVisibility(View.INVISIBLE);
        } else {
            holder.imageView.setVisibility(View.VISIBLE);
            holder.imageView.setRatio(post.getImgRatio());
            Picasso.with(context).load(post.getImgUrl()).fit().into(holder.imageView);
        }

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (itemLongClickListener != null) {
                    itemLongClickListener.onItemLongClick(position, post);
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public void onNewPosts(List<Post> posts) {
        this.posts.addAll(0, posts);
        notifyItemRangeInserted(0, posts.size());
    }

    public void onPostDeleted(Post deletedPost) {
        int index = -1;
        for (int i=0 ; i<posts.size() ; i++) {
            if (posts.get(i).getKey().endsWith(deletedPost.getKey())) {
                index = i;
                break;
            }
        }

        if (index != -1) {
            this.posts.remove(index);
            notifyItemRemoved(index);
        }
    }

    public void setItemLongClickListener(OnItemLongClickListener itemLongClickListener) {
        this.itemLongClickListener = itemLongClickListener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView textView;
        FixedImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);

            textView = ButterKnife.findById(itemView, R.id.text);
            imageView = ButterKnife.findById(itemView, R.id.image);
        }
    }

    interface OnItemLongClickListener {
        void onItemLongClick(int position, Post post);
    }
}
