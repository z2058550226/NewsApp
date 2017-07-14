package com.suikajy.newsapp.injector.modules;

import com.suikajy.newsapp.adapter.BeautyPhotosAdapter;
import com.suikajy.newsapp.injector.PerFragment;
import com.suikajy.newsapp.local.table.DaoSession;
import com.suikajy.newsapp.module.base.presenter.ILocalPresenter;
import com.suikajy.newsapp.module.manage.love.photo.LovePhotoFragment;
import com.suikajy.newsapp.module.manage.love.photo.LovePhotoPresenter;
import com.suikajy.newsapp.rxbus.RxBus;
import com.suikajy.recyclerviewhelper.adapter.BaseQuickAdapter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by suikajy on 2017/4/18.
 *
 */
@Module
public class LovePhotoModule {

    private final LovePhotoFragment mView;

    public LovePhotoModule(LovePhotoFragment mView) {
        this.mView = mView;
    }

    @PerFragment
    @Provides
    public ILocalPresenter providePresenter(DaoSession daoSession, RxBus rxBus) {
        return new LovePhotoPresenter(mView, daoSession.getBeautyPhotoInfoDao(), rxBus);
    }

    @PerFragment
    @Provides
    public BaseQuickAdapter provideAdapter() {
        return new BeautyPhotosAdapter(mView);
    }

}
