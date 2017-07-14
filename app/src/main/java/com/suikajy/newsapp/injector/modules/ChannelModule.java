package com.suikajy.newsapp.injector.modules;

import com.suikajy.newsapp.adapter.ManageAdapter;
import com.suikajy.newsapp.injector.PerActivity;
import com.suikajy.newsapp.local.table.DaoSession;
import com.suikajy.newsapp.module.news.channel.ChannelActivity;
import com.suikajy.newsapp.module.news.channel.ChannelPresenter;
import com.suikajy.newsapp.module.news.channel.IChannelPresenter;
import com.suikajy.newsapp.rxbus.RxBus;
import com.suikajy.recyclerviewhelper.adapter.BaseQuickAdapter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by suikajy on 2017/4/30.
 */

@Module
public class ChannelModule {

    private final ChannelActivity mView;

    public ChannelModule(ChannelActivity view) {
        mView = view;
    }

    @Provides
    public BaseQuickAdapter provideManageAdapter() {
        return new ManageAdapter(mView);
    }

    @PerActivity
    @Provides
    public IChannelPresenter provideManagePresenter(DaoSession daoSession, RxBus rxBus) {
        return new ChannelPresenter(mView, daoSession.getNewsTypeInfoDao(), rxBus);
    }

}
