package com.suikajy.newsapp.module.base.view;

import java.util.List;

/**
 * Created by suikajy on 2017/4/18.
 * 和本地数据有关的界面接口
 */

public interface ILocalView<T> {

    /**
     * 显示数据
     * @param dataList 数据
     */
    void loadData(List<T> dataList);

    /**
     * 没有数据
     */
    void noData();

}
