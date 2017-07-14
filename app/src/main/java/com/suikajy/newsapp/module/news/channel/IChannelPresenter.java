package com.suikajy.newsapp.module.news.channel;

import com.suikajy.newsapp.module.base.presenter.ILocalPresenter;

/**
 * Created by suikajy on 2017/4/30.
 * 频道设置界面的presenter接口
 */

public interface IChannelPresenter<T> extends ILocalPresenter<T> {

    /**
     * 交换
     * @param fromPos
     * @param toPos
     */
    void swap(int fromPos, int toPos);
}
