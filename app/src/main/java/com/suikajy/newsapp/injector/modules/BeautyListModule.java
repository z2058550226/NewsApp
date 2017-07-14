package com.suikajy.newsapp.injector.modules;

import com.suikajy.newsapp.adapter.BeautyPhotosAdapter;
import com.suikajy.newsapp.injector.PerFragment;
import com.suikajy.newsapp.module.base.presenter.IBasePresenter;
import com.suikajy.newsapp.module.photo.beauty.BeautyListFragment;
import com.suikajy.newsapp.module.photo.beauty.BeautyListPresenter;
import com.suikajy.recyclerviewhelper.adapter.BaseQuickAdapter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by suikajy on 2017/4/15.
 *
 */

@Module
public class BeautyListModule {

    private final BeautyListFragment mView;

    public BeautyListModule(BeautyListFragment view) {
        this.mView = view;
    }

    @PerFragment
    @Provides
    public IBasePresenter providePresenter() {
        return new BeautyListPresenter(mView);
    }

    @PerFragment
    @Provides
    public BaseQuickAdapter provideAdapter() {
        return new BeautyPhotosAdapter(mView.getContext());
    }

}
