package com.suikajy.newsapp.injector.modules;

import android.content.Context;

import com.suikajy.newsapp.App;
import com.suikajy.newsapp.local.table.DaoSession;
import com.suikajy.newsapp.rxbus.RxBus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by suikajy on 2017/4/6.
 */
@Module
public class ApplicationModule {

    private final App mApp;
    private final DaoSession mDaoSession;
    private final RxBus mRxBus;

    public ApplicationModule(App application, DaoSession daoSession, RxBus rxBus) {
        mApp = application;
        mDaoSession = daoSession;
        mRxBus = rxBus;
    }

    @Provides
    @Singleton
    Context provideApplicationContext() {
        return mApp;
    }

    @Provides
    @Singleton
    RxBus provideRxBus() {
        return mRxBus;
    }

    @Provides
    @Singleton
    DaoSession provideDaoSession() {
        return mDaoSession;
    }
}
