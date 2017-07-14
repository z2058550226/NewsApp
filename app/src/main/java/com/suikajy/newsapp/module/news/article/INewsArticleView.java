package com.suikajy.newsapp.module.news.article;

import com.suikajy.newsapp.api.bean.NewsDetailInfo;
import com.suikajy.newsapp.module.base.view.IBaseView;

/**
 * Created by suikajy on 2017/4/18.
 */

public interface INewsArticleView extends IBaseView {

    /**
     * 显示数据
     * @param newsDetailBean 新闻详情
     */
    void loadData(NewsDetailInfo newsDetailBean);

}
