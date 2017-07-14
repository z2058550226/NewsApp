package com.suikajy.newsapp.module.base.presenter;

import java.util.List;

/**
 * Created by suikajy on 2017/4/13.
 * 提供本地数据库数据的presenter
 */

public interface ILocalPresenter<T> extends IBasePresenter {
    /**
     * 插入数据
     * @param data  数据
     */
    void insert(T data);

    /**
     * 删除数据
     * @param data  数据
     */
    void delete(T data);

    /**
     * 更新数据
     * @param list   所有数据
     */
    void update(List<T> list);
}
