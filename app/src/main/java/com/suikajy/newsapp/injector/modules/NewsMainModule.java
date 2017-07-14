package com.suikajy.newsapp.injector.modules;

import com.suikajy.newsapp.adapter.ViewPagerAdapter;
import com.suikajy.newsapp.injector.PerFragment;
import com.suikajy.newsapp.local.table.DaoSession;
import com.suikajy.newsapp.module.base.presenter.IRxBusPresenter;
import com.suikajy.newsapp.module.news.main.NewsMainFragment;
import com.suikajy.newsapp.module.news.main.NewsMainPresenter;
import com.suikajy.newsapp.rxbus.RxBus;

import dagger.Module;
import dagger.Provides;

/**
 * Created by suikajy on 2017/4/7.
 */

@Module
public class NewsMainModule {

    private final NewsMainFragment mView;

    public NewsMainModule(NewsMainFragment mView) {
        this.mView = mView;
    }

    @PerFragment
    @Provides
    public IRxBusPresenter provideMainPresenter(DaoSession daoSession, RxBus rxBus) {
        return new NewsMainPresenter(mView, daoSession.getNewsTypeInfoDao(), rxBus);
    }

    @PerFragment
    @Provides
    public ViewPagerAdapter provideViewPagerAdapter() {
        return new ViewPagerAdapter(mView.getChildFragmentManager());
    }
}
