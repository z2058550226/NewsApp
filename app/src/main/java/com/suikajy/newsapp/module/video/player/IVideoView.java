package com.suikajy.newsapp.module.video.player;

import com.suikajy.newsapp.local.table.VideoInfo;
import com.suikajy.newsapp.module.base.view.IBaseView;

import java.io.InputStream;

/**
 * Created by suikajy on 2017/5/2.
 */

public interface IVideoView extends IBaseView {
    /**
     * 获取Video数据
     * @param data 数据
     */
    void loadData(VideoInfo data);

    /**
     * 获取弹幕数据
     * @param inputStream 数据
     */
    void loadDanmakuData(InputStream inputStream);
}
