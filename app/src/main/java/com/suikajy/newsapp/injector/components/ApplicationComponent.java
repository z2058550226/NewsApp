package com.suikajy.newsapp.injector.components;

import android.content.Context;

import com.suikajy.newsapp.injector.modules.ApplicationModule;
import com.suikajy.newsapp.local.table.DaoSession;
import com.suikajy.newsapp.rxbus.RxBus;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by long on 2016/8/19.
 * Application Component
 */
@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

//    void inject(BaseActivity baseActivity);

    // provide
    Context getContext();
    RxBus getRxBus();
    DaoSession getDaoSession();
}
