package com.tsengvn.myfeed.ui.feed;

import com.tsengvn.myfeed.domain.interactor.DataService;
import com.tsengvn.myfeed.pojo.Post;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;

import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author : hienngo
 * @since : Sep 05, 2016.
 */
@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class FeedPresenterTest {
    @Mock
    DataService dataService;

    @Mock
    FeedView feedView;

    FeedPresenter feedPresenter;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        feedPresenter = new FeedPresenter(dataService);
        feedPresenter.onTakeView(feedView);

    }

    @Test
    public void testView() throws Exception {
        Assert.assertEquals(feedPresenter.getView(), feedView);
    }

    @Test
    public void testPauseSyncing() throws Exception {

    }

    @Test
    public void testRemovePost() throws Exception {
        Post post = new Post();

        when(dataService.removePost(post)).thenReturn(Observable.just(true));
        feedPresenter.removePost(post);
        verify(feedView, times(1)).onPostDeleted(post);
    }

    @Test
    public void testResumeSyncingWithNewPost() throws Exception {
        List<Post> postList = new ArrayList<>();
        Post post = new Post();
        post.setStatus(Post.Status.Add);
        postList.add(post);

        when(dataService.startSyncingPost(anyLong())).thenReturn(Observable.just(postList));
        feedPresenter.resumeSyncing();
        verify(feedView, times(1)).showNewPostNotice();
        verify(feedView, times(1)).onReceiveNewPosts(postList);
    }

    @Test
    public void testResumeSyncingWithDeletedPost() throws Exception {
        List<Post> postList = new ArrayList<>();
        Post post = new Post();
        post.setStatus(Post.Status.Remove);
        postList.add(post);

        when(dataService.startSyncingPost(anyLong())).thenReturn(Observable.just(postList));
        feedPresenter.resumeSyncing();
        verify(feedView, times(1)).onPostDeleted(post);
    }

    @Test
    public void testResumeSyncingWithError() throws Exception {
        Exception exception = new RuntimeException("test");
        Observable<List<Post>> error = Observable.error(exception);

        when(dataService.startSyncingPost(anyLong())).thenReturn(error);
        feedPresenter.resumeSyncing();
        verify(feedView, times(1)).showError("test");
    }

    @Test
    public void testStopSyncing() throws Exception {
        Observable<List<Post>> observable = Observable.create(new Observable.OnSubscribe<List<Post>>() {
            @Override
            public void call(Subscriber<? super List<Post>> subscriber) {
                List<Post> postList = new ArrayList<>();
                Post post = new Post();
                post.setStatus(Post.Status.Add);
                postList.add(post);
                subscriber.onNext(postList);

                Assert.assertFalse(subscriber.isUnsubscribed());

                feedPresenter.stopSyncing();
                subscriber.onNext(postList);
                verify(feedView, times(1)).showNewPostNotice();
                verify(feedView, times(1)).onReceiveNewPosts(postList);

                Assert.assertTrue(subscriber.isUnsubscribed());

            }
        });
        when(dataService.startSyncingPost(anyLong())).thenReturn(observable);
        feedPresenter.resumeSyncing();
    }
}