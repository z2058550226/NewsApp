package com.suikajy.newsapp.injector.modules;

import com.suikajy.newsapp.adapter.BaseVideoDLAdapter;
import com.suikajy.newsapp.adapter.VideoCacheAdapter;
import com.suikajy.newsapp.injector.PerFragment;
import com.suikajy.newsapp.local.table.DaoSession;
import com.suikajy.newsapp.module.base.presenter.IRxBusPresenter;
import com.suikajy.newsapp.module.manage.download.cache.VideoCacheFragment;
import com.suikajy.newsapp.module.manage.download.cache.VideoCachePresenter;
import com.suikajy.newsapp.rxbus.RxBus;

import dagger.Module;
import dagger.Provides;

/**
 * Created by suikajy on 2017/5/22.
 */
@Module
public class VideoCacheModule {

    private final VideoCacheFragment mView;

    public VideoCacheModule(VideoCacheFragment view) {
        this.mView = view;
    }

    @PerFragment
    @Provides
    public IRxBusPresenter providePresenter(DaoSession daoSession, RxBus rxBus) {
        return new VideoCachePresenter(mView, daoSession.getVideoInfoDao(), rxBus);
    }

    @PerFragment
    @Provides
    public BaseVideoDLAdapter provideAdapter(RxBus rxBus) {
        return new VideoCacheAdapter(mView.getContext(), rxBus);
    }
}
