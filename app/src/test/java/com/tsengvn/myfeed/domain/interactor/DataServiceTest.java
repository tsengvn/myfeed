package com.tsengvn.myfeed.domain.interactor;

import com.tsengvn.myfeed.domain.repo.ImgurRepo;
import com.tsengvn.myfeed.domain.repo.PostRepo;
import com.tsengvn.myfeed.pojo.Image;
import com.tsengvn.myfeed.pojo.Post;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Scheduler;
import rx.android.plugins.RxAndroidPlugins;
import rx.android.plugins.RxAndroidSchedulersHook;
import rx.functions.Action1;
import rx.plugins.RxJavaPlugins;
import rx.plugins.RxJavaSchedulersHook;
import rx.schedulers.Schedulers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author : hienngo
 * @since : Sep 05, 2016.
 */
@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class DataServiceTest {
    @Mock
    PostRepo postRepo;

    @Mock
    ImgurRepo imgurRepo;

    DataService dataService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        dataService = new DataService(postRepo, imgurRepo);

        RxJavaPlugins.getInstance().registerSchedulersHook(new RxJavaSchedulersHook() {
            @Override
            public Scheduler getIOScheduler() {
                return Schedulers.immediate();
            }


        });
        RxAndroidPlugins.getInstance().registerSchedulersHook(new RxAndroidSchedulersHook(){
            @Override
            public Scheduler getMainThreadScheduler() {
                return Schedulers.immediate();
            }
        });
    }

    @After
    public void tearDown() {
        RxAndroidPlugins.getInstance().reset();
        RxJavaPlugins.getInstance().reset();
    }


    @Test
    public void testLoadImages() throws Exception {
        final List<Image> imageList = new ArrayList<>();
        Image.List dataList = new Image.List();
        dataList.setData(imageList);

        when(imgurRepo.getRandomImage(0)).thenReturn(Observable.just(dataList));

        dataService.loadImages(0)
                .subscribe(new Action1<List<Image>>() {
                    @Override
                    public void call(List<Image> images) {
                        assertEquals(images.size(), imageList.size());
                    }
                });
    }

    @Test
    public void testLoadImagesWithTypeFilter() throws Exception {
        final List<Image> imageList = new ArrayList<>();
        Image image = new Image();
        image.setAnimated(true);
        imageList.add(image);

        image = new Image();
        image.setAlbum(true);
        imageList.add(image);

        image = new Image();
        image.setAlbum(false);
        image.setAnimated(false);
        image.setId("1");
        imageList.add(image);

        Image.List dataList = new Image.List();
        dataList.setData(imageList);

        when(imgurRepo.getRandomImage(0)).thenReturn(Observable.just(dataList));

        dataService.loadImages(0)
                .subscribe(new Action1<List<Image>>() {
                    @Override
                    public void call(List<Image> images) {
                        assertNotEquals(images.size(), imageList.size());
                        assertEquals(1, images.size());

                        assertEquals("1", images.get(0).getId());
                    }
                });
    }

    @Test
    public void testAddPost() throws Exception {
        dataService.addPost("abc", "url", 1)
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        ArgumentCaptor<Post> captor = ArgumentCaptor.forClass(Post.class);

                        verify(postRepo, times(1)).createPost(captor.capture());
                        assertEquals(1, captor.getAllValues().size());
                        Post value = captor.getValue();
                        assertEquals("abc", value.getText());
                        assertEquals("url", value.getImgUrl());
                        assertEquals(1, value.getImgRatio(), 0);
                    }
                });

    }

    @Test
    public void testStartSyncingPost() throws Exception {
//        dataService.startSyncingPost()
    }

    @Test
    public void testStopSyncingPost() throws Exception {

    }

    @Test
    public void testRemovePost() throws Exception {
        Post post = new Post();
        post.setCreated(1);
        post.setText("abc");
        post.setImgRatio(1);
        post.setImgUrl("url");
        post.setKey("2");

        dataService.removePost(post)
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        assertTrue(aBoolean);

                        ArgumentCaptor<Post> captor = ArgumentCaptor.forClass(Post.class);
                        verify(postRepo, times(1)).remove(captor.capture());
                        assertEquals(1, captor.getAllValues().size());

                        Post value = captor.getValue();
                        assertEquals("2", value.getKey());
                        assertEquals(1, value.getCreated());
                        assertEquals("abc", value.getText());
                        assertEquals("url", value.getImgUrl());
                        assertEquals(1, value.getImgRatio(), 0);
                    }
                });
    }
}