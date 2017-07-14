package com.suikajy.newsapp.module.news.main;

import com.suikajy.newsapp.local.table.NewsTypeInfo;

import java.util.List;

/**
 * Created by suikajy on 2017/4/6.
 */

public interface INewsMainView {
    /**
     * 显示数据
     * @param checkList     选中栏目
     */
    void loadData(List<NewsTypeInfo> checkList);
}
