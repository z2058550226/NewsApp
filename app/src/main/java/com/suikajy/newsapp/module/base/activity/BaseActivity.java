package com.suikajy.newsapp.module.base.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.suikajy.core.utils.SwipeRefreshHelper;
import com.suikajy.newsapp.App;
import com.suikajy.newsapp.R;
import com.suikajy.newsapp.injector.components.ApplicationComponent;
import com.suikajy.newsapp.module.base.presenter.IBasePresenter;
import com.suikajy.newsapp.module.base.view.IBaseView;
import com.suikajy.newsapp.widget.EmptyLayout;
import com.trello.rxlifecycle.LifecycleTransformer;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by suikajy on 2017/4/4.
 */

public abstract class BaseActivity<T extends IBasePresenter> extends RxAppCompatActivity implements IBaseView {
    /**
     * 把 EmptyLayout 放在基类统一处理，@Nullable 表明 View 可以为 null，详细可看 ButterKnife
     */
    @Nullable
    @BindView(R.id.empty_layout)
    protected EmptyLayout mEmptyLayout;
    @Inject
    protected T mPresenter;
    /**
     * 刷新控件，注意，资源的ID一定要一样
     */
    @Nullable
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout mSwipeRefresh;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(setLayoutResourceID());
        ButterKnife.bind(this);
        initInjector();
        onInit();
        onInitRefresh();
        updateViews(false);
    }

    @Override
    public void showLoading() {
        if (mEmptyLayout != null) {
            mEmptyLayout.setEmptyStatus(EmptyLayout.STATUS_LOADING);
        }
    }

    @Override
    public void hideLoading() {
        if (mEmptyLayout != null) {
            mEmptyLayout.hide();
        }
    }

    @Override
    public void showNetError(final EmptyLayout.OnRetryListener onRetryListener) {
        if (mEmptyLayout != null) {
            mEmptyLayout.setEmptyStatus(EmptyLayout.STATUS_NO_NET);
            mEmptyLayout.setRetryListener(onRetryListener);
        }
    }

    protected abstract void initInjector();

    protected void onInitRefresh() {
        if (mSwipeRefresh != null) {
            SwipeRefreshHelper.init(mSwipeRefresh, new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    updateViews(true);
                }
            });
        }
    }

    ;

    protected abstract int setLayoutResourceID();

    protected abstract void onInit();

    protected abstract void updateViews(boolean isRefresh);

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public <T> LifecycleTransformer<T> bindToLife() {
        return this.<T>bindToLifecycle();
    }

    @Override
    public void finishRefresh() {
        if (mSwipeRefresh != null) {
            mSwipeRefresh.setRefreshing(false);
        }
    }


    /**
     * 获取 ApplicationComponent
     *
     * @return ApplicationComponent
     */
    protected ApplicationComponent getAppComponent() {
        return App.getAppComponent();
//        return ((AndroidApplication) getApplication()).getAppComponent();
    }

    /**
     * 初始化 Toolbar
     *
     * @param toolbar
     * @param homeAsUpEnabled
     * @param title
     */
    public void initToolBar(Toolbar toolbar, boolean homeAsUpEnabled, String title) {
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(homeAsUpEnabled);
    }
}
