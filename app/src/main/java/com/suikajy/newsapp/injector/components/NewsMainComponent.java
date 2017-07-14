package com.suikajy.newsapp.injector.components;

import com.suikajy.newsapp.injector.PerFragment;
import com.suikajy.newsapp.injector.modules.NewsMainModule;
import com.suikajy.newsapp.module.news.main.NewsMainFragment;

import dagger.Component;

/**
 * Created by suikajy on 2017/4/7.
 */

@PerFragment
@Component(dependencies = ApplicationComponent.class, modules = NewsMainModule.class)
public interface NewsMainComponent {
    void inject(NewsMainFragment fragment);
}
