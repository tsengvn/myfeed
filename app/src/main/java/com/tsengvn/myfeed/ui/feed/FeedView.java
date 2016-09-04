package com.tsengvn.myfeed.ui.feed;

import com.tsengvn.myfeed.pojo.Post;
import com.tsengvn.myfeed.ui.base.BaseView;

import java.util.List;

/**
 * @author : hienngo
 * @since : Sep 01, 2016.
 */
public interface FeedView extends BaseView{
    void onReceiveNewPosts(List<Post> posts);

    void onPostDeleted(Post post);

    void showNewPostNotice();

    void showError(String message);

}
