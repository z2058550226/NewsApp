package com.suikajy.newsapp.module.news.special;

import com.suikajy.newsapp.adapter.item.SpecialItem;
import com.suikajy.newsapp.api.bean.SpecialInfo;
import com.suikajy.newsapp.module.base.view.IBaseView;

import java.util.List;

/**
 * Created by suikajy on 2017/4/20.
 *
 */



public interface ISpecialView extends IBaseView{

    /**
     * 显示数据
     * @param specialItems 新闻
     */
    void loadData(List<SpecialItem> specialItems);

    /**
     * 添加头部
     * @param specialBean
     */
    void loadBanner(SpecialInfo specialBean);

}
