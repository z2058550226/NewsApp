package com.suikajy.newsapp.injector.components;

import com.suikajy.newsapp.injector.PerActivity;
import com.suikajy.newsapp.injector.modules.NewsArticleModule;
import com.suikajy.newsapp.module.news.article.NewsArticleActivity;

import dagger.Component;

/**
 * Created by suikajy on 2017/4/19.
 */

@PerActivity
@Component(modules = NewsArticleModule.class)
public interface NewsArticleComponent {

    void inject(NewsArticleActivity articleActivity);

}
