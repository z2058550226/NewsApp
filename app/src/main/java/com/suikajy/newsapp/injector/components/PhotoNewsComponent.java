package com.suikajy.newsapp.injector.components;

import com.suikajy.newsapp.injector.PerFragment;
import com.suikajy.newsapp.injector.modules.PhotoNewsModule;
import com.suikajy.newsapp.module.photo.news.PhotoNewsFragment;

import dagger.Component;

/**
 * Created by suikajy on 2017/4/16.
 */


@PerFragment
@Component(dependencies = ApplicationComponent.class, modules = PhotoNewsModule.class)
public interface PhotoNewsComponent {
    void inject(PhotoNewsFragment fragment);
}
