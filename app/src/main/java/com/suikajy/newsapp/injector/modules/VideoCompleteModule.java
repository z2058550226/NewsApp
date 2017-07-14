package com.suikajy.newsapp.injector.modules;

import com.suikajy.newsapp.adapter.BaseVideoDLAdapter;
import com.suikajy.newsapp.adapter.VideoCompleteAdapter;
import com.suikajy.newsapp.injector.PerFragment;
import com.suikajy.newsapp.local.table.DaoSession;
import com.suikajy.newsapp.module.base.presenter.IRxBusPresenter;
import com.suikajy.newsapp.module.manage.download.complete.VideoCompleteFragment;
import com.suikajy.newsapp.module.manage.download.complete.VideoCompletePresenter;
import com.suikajy.newsapp.rxbus.RxBus;

import dagger.Module;
import dagger.Provides;

/**
 * Created by suikajy on 2017/5/22.
 */
@Module
public class VideoCompleteModule {

    private final VideoCompleteFragment mView;

    public VideoCompleteModule(VideoCompleteFragment view) {
        this.mView = view;
    }

    @PerFragment
    @Provides
    public IRxBusPresenter providePresenter(DaoSession daoSession, RxBus rxBus) {
        return new VideoCompletePresenter(mView, daoSession.getVideoInfoDao(), rxBus);
    }

    @PerFragment
    @Provides
    public BaseVideoDLAdapter provideAdapter(RxBus rxBus) {
        return new VideoCompleteAdapter(mView.getContext(), rxBus);
    }
}
