package com.suikajy.newsapp.injector.modules;

import com.suikajy.newsapp.adapter.NewsMultiListAdapter;
import com.suikajy.newsapp.injector.PerFragment;
import com.suikajy.newsapp.module.base.presenter.IBasePresenter;
import com.suikajy.newsapp.module.news.newslist.INewsListPresenter;
import com.suikajy.newsapp.module.news.newslist.NewsListFragment;
import com.suikajy.recyclerviewhelper.adapter.BaseQuickAdapter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by suikajy on 2017/4/9.
 */
@Module
public class NewsListModule {

    private final NewsListFragment newsListFragment;
    private final String mNewsId;

    public NewsListModule(NewsListFragment newsListFragment, String mNewsId) {
        this.newsListFragment = newsListFragment;
        this.mNewsId = mNewsId;
    }

    @PerFragment
    @Provides
    public IBasePresenter providePresenter() {
        return new INewsListPresenter(newsListFragment, mNewsId);
    }

    @PerFragment
    @Provides
    public BaseQuickAdapter provideAdapter() {
        return new NewsMultiListAdapter(newsListFragment.getContext());
    }
}
