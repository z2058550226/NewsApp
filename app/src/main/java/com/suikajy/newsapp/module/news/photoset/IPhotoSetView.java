package com.suikajy.newsapp.module.news.photoset;

import com.suikajy.newsapp.api.bean.PhotoSetInfo;
import com.suikajy.newsapp.module.base.view.IBaseView;

/**
 * Created by suikajy on 2017/4/16.
 */

public interface IPhotoSetView extends IBaseView {
    /**
     * 显示数据
     * @param photoSetBean 图集
     */
    void loadData(PhotoSetInfo photoSetBean);
}
