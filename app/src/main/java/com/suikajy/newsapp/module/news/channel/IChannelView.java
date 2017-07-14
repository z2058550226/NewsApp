package com.suikajy.newsapp.module.news.channel;

import com.suikajy.newsapp.local.table.NewsTypeInfo;

import java.util.List;

/**
 * Created by suikajy on 2017/4/30.
 */

public interface IChannelView {

    /**
     * 显示数据
     * @param checkList     选中栏目
     * @param uncheckList   未选中栏目
     */
    void loadData(List<NewsTypeInfo> checkList, List<NewsTypeInfo> uncheckList);

}
