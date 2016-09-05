package com.tsengvn.myfeed.ui.base;

/**
 * @author Hien Ngo
 * @since 7/27/16
 */
public abstract class BasePresenter<V extends BaseView> {
    private V view;

    public void onTakeView(V view) {
        this.view = view;
    }

    public V getView() {
        return view;
    }

    public void onDestroyView() {
        this.view = null;
    }
}
