package com.suikajy.newsapp.injector.modules;

import com.suikajy.newsapp.adapter.ViewPagerAdapter;
import com.suikajy.newsapp.injector.PerFragment;
import com.suikajy.newsapp.local.table.DaoSession;
import com.suikajy.newsapp.module.base.presenter.IRxBusPresenter;
import com.suikajy.newsapp.module.video.main.VideoMainFragment;
import com.suikajy.newsapp.module.video.main.VideoMainPresenter;
import com.suikajy.newsapp.rxbus.RxBus;

import dagger.Module;
import dagger.Provides;

/**
 * Created by suikajy on 2017/5/2.
 */
@Module
public class VideoMainModule {
    private final VideoMainFragment mView;

    public VideoMainModule(VideoMainFragment view) {
        mView = view;
    }

    @PerFragment
    @Provides
    public ViewPagerAdapter provideViewPagerAdapter() {
        return new ViewPagerAdapter(mView.getChildFragmentManager());
    }

    @PerFragment
    @Provides
    public IRxBusPresenter provideVideosPresenter(DaoSession daoSession, RxBus rxBus) {
        return new VideoMainPresenter(mView, daoSession.getVideoInfoDao(), rxBus);
    }

}
