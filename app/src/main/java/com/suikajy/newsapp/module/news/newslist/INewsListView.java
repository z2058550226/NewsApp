package com.suikajy.newsapp.module.news.newslist;

import com.suikajy.newsapp.adapter.item.NewsMultiItem;
import com.suikajy.newsapp.api.bean.NewsInfo;
import com.suikajy.newsapp.module.base.view.ILoadDataView;

import java.util.List;

/**
 * Created by suikajy on 2017/4/7.
 */

public interface INewsListView extends ILoadDataView<List<NewsMultiItem>> {

    /**
     * 加载广告数据
     * @param newsBean 新闻
     */
    void loadAdData(NewsInfo newsBean);

}
