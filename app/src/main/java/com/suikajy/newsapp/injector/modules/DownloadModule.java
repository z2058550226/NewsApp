package com.suikajy.newsapp.injector.modules;

import com.suikajy.newsapp.adapter.ViewPagerAdapter;
import com.suikajy.newsapp.injector.PerActivity;
import com.suikajy.newsapp.module.base.presenter.IRxBusPresenter;
import com.suikajy.newsapp.module.manage.download.DownloadActivity;
import com.suikajy.newsapp.module.manage.download.DownloadPresenter;
import com.suikajy.newsapp.rxbus.RxBus;

import dagger.Module;
import dagger.Provides;

/**
 * Created by suikajy on 2017/5/22.
 *
 */

@Module
public class DownloadModule {

    private final DownloadActivity mView;

    public DownloadModule(DownloadActivity view) {
        mView = view;
    }

    @PerActivity
    @Provides
    public ViewPagerAdapter provideViewPagerAdapter() {
        return new ViewPagerAdapter(mView.getSupportFragmentManager());
    }

    @PerActivity
    @Provides
    public IRxBusPresenter provideVideosPresenter(RxBus rxBus) {
        return new DownloadPresenter(rxBus);
    }

}
