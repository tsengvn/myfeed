package com.tsengvn.myfeed.ui.add;

import com.tsengvn.myfeed.domain.interactor.DataService;
import com.tsengvn.myfeed.ui.base.BasePresenter;

import javax.inject.Inject;

import rx.functions.Action1;

/**
 * @author : hienngo
 * @since : Sep 02, 2016.
 */
public class AddPresenter extends BasePresenter<AddView> {
    private final DataService dataService;

    @Inject
    public AddPresenter(DataService dataService) {
        this.dataService = dataService;
    }


    public void savePost(String text, String imgUrl, float imgRatio) {
        dataService.addPost(text, imgUrl, imgRatio)
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        getView().onPostAdded();
                    }
                });
    }
}
