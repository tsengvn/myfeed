package com.tsengvn.myfeed.ui.gallery;

import com.tsengvn.myfeed.pojo.Image;
import com.tsengvn.myfeed.ui.base.BaseView;

import java.util.List;

/**
 * @author : hienngo
 * @since : Sep 03, 2016.
 */
public interface GalleryView extends BaseView {
    void onReceiveData(List<Image> images, boolean refresh);

    void showError();
}
