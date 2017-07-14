package com.suikajy.newsapp.injector.components;

import com.suikajy.newsapp.injector.PerFragment;
import com.suikajy.newsapp.injector.modules.VideoCacheModule;
import com.suikajy.newsapp.module.manage.download.cache.VideoCacheFragment;

import dagger.Component;

/**
 * Created by suikajy on 2017/5/22.
 */
@PerFragment
@Component(dependencies = ApplicationComponent.class,modules = VideoCacheModule.class)
public interface VideoCacheComponent {
    void inject(VideoCacheFragment fragment);
}
