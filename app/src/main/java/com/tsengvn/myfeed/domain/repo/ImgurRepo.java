package com.tsengvn.myfeed.domain.repo;

import com.tsengvn.myfeed.pojo.Image;

import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.Path;
import rx.Observable;

/**
 * @author : hienngo
 * @since : Sep 02, 2016.
 */
public interface ImgurRepo {
    String API_VERSION = "3";
    String URL = "https://api.imgur.com/";

    @GET("gallery/random/random/{page}")
    Observable<Image.List> getRandomImage(@Path("page") int page);
}
