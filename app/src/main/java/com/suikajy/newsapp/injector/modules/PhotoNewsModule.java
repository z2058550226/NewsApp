package com.suikajy.newsapp.injector.modules;

import com.suikajy.newsapp.adapter.PhotoListAdapter;
import com.suikajy.newsapp.injector.PerFragment;
import com.suikajy.newsapp.module.base.presenter.IBasePresenter;
import com.suikajy.newsapp.module.photo.news.PhotoNewsFragment;
import com.suikajy.newsapp.module.photo.news.PhotoNewsPresenter;
import com.suikajy.recyclerviewhelper.adapter.BaseQuickAdapter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by suikajy on 2017/4/15.
 */


@Module
public class PhotoNewsModule {

    private final PhotoNewsFragment mView;

    public PhotoNewsModule(PhotoNewsFragment mView) {
        this.mView = mView;
    }

    @PerFragment
    @Provides
    public IBasePresenter providePresenter() {
        return new PhotoNewsPresenter(mView);
    }

    @PerFragment
    @Provides
    public BaseQuickAdapter provideAdapter() {
        return new PhotoListAdapter(mView.getContext());
    }
}
