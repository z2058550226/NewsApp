package com.suikajy.newsapp.injector.components;

import com.suikajy.newsapp.injector.PerActivity;
import com.suikajy.newsapp.injector.modules.PhotoSetModule;
import com.suikajy.newsapp.module.news.photoset.PhotoSetActivity;

import dagger.Component;

/**
 * Created by suikajy on 2017/4/30.
 */

@PerActivity
@Component(modules = PhotoSetModule.class)
public interface PhotoSetComponent {
    void inject(PhotoSetActivity activity);
}
