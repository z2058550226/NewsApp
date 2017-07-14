package com.suikajy.newsapp.module.video.main;

/**
 * Created by suikajy on 2017/5/1.
 *
 * video 主界面窗口
 */

public interface IVideoMainView {

    /**
     * 更新数据
     * @param lovedCount 收藏数
     */
    void updateLovedCount(int lovedCount);

    /**
     * 更新数据
     * @param downloadCount 下载中个数
     */
    void updateDownloadCount(int downloadCount);

}
