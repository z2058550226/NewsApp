package com.suikajy.newsapp.injector.components;

import com.suikajy.newsapp.injector.PerFragment;
import com.suikajy.newsapp.injector.modules.NewsListModule;
import com.suikajy.newsapp.module.news.newslist.NewsListFragment;

import dagger.Component;

/**
 * Created by suikajy on 2017/4/10.
 */

@PerFragment
@Component(dependencies = ApplicationComponent.class, modules = NewsListModule.class)
public interface NewsListComponent {
    void inject(NewsListFragment fragment);
}
