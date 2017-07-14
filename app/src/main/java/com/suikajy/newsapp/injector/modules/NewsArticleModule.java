package com.suikajy.newsapp.injector.modules;

import com.suikajy.newsapp.injector.PerActivity;
import com.suikajy.newsapp.module.base.presenter.IBasePresenter;
import com.suikajy.newsapp.module.news.article.NewsArticleActivity;
import com.suikajy.newsapp.module.news.article.NewsArticlePresenter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by suikajy on 2017/4/19.
 */

@Module
public class NewsArticleModule {

    private final String mNewsId;
    private final NewsArticleActivity mView;

    public NewsArticleModule(NewsArticleActivity view, String newsId) {
        mNewsId = newsId;
        mView = view;
    }

    @PerActivity
    @Provides
    public IBasePresenter providePresenter() {
        return new NewsArticlePresenter(mNewsId, mView);
    }

}
