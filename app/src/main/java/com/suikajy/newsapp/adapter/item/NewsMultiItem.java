package com.suikajy.newsapp.adapter.item;

import android.support.annotation.IntDef;

import com.suikajy.newsapp.api.bean.NewsInfo;
import com.suikajy.recyclerviewhelper.entity.MultiItemEntity;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by suikajy on 2017/4/9.
 * NewsInfo的封装类, 相当于对所有类型的NewsInfo的包装实体, 可以统一使用
 */

public class NewsMultiItem extends MultiItemEntity {

    public static final int ITEM_TYPE_NORMAL = 1;
    public static final int ITEM_TYPE_PHOTO_SET = 2;

    private NewsInfo mNewsBean;

    public NewsMultiItem(@NewsItemType int itemType, NewsInfo newsBean) {
        super(itemType);
        mNewsBean = newsBean;
    }

    public NewsInfo getNewsBean() {
        return mNewsBean;
    }

    public void setNewsBean(NewsInfo newsBean) {
        mNewsBean = newsBean;
    }

    @Override
    public void setItemType(@NewsItemType int itemType) {
        super.setItemType(itemType);
    }

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({ITEM_TYPE_NORMAL, ITEM_TYPE_PHOTO_SET})
    public @interface NewsItemType {
    }
}
