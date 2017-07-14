package com.suikajy.newsapp.module.video.player;

import com.suikajy.newsapp.local.table.DanmakuInfo;
import com.suikajy.newsapp.local.table.VideoInfo;
import com.suikajy.newsapp.module.base.presenter.ILocalPresenter;

/**
 * Created by suikajy on 2017/5/2.
 */

public interface IVideoPresenter extends ILocalPresenter<VideoInfo> {

    /**
     * 添加一条弹幕到数据库
     * @param danmakuInfo
     */
    void addDanmaku(DanmakuInfo danmakuInfo);

    /**
     * 清空该视频所有缓存弹幕
     */
    void cleanDanmaku();

}
