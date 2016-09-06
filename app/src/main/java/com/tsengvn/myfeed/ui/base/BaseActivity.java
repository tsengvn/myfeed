package com.tsengvn.myfeed.ui.base;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.tsengvn.myfeed.MyFeedApp;
import com.tsengvn.myfeed.internal.di.component.AppComponent;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author Hien Ngo
 * @since 7/27/16
 */
public abstract class BaseActivity<P extends BasePresenter> extends AppCompatActivity implements BaseView {
    private Unbinder unbinder;

    @Inject P presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        initInjector(MyFeedApp.getAppComponent(this));
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        unbinder = ButterKnife.bind(this);
        getPresenter().onTakeView(this);
    }

    @Override
    protected void onDestroy() {
        unbinder.unbind();
        getPresenter().onDestroyView();
        super.onDestroy();
    }

    @Override
    final public P getPresenter() {
        return presenter;
    }

    protected abstract void initInjector(AppComponent appComponent);
}
