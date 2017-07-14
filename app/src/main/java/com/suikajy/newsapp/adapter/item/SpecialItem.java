package com.suikajy.newsapp.adapter.item;

import com.suikajy.newsapp.api.bean.NewsItemInfo;
import com.suikajy.recyclerviewhelper.entity.SectionEntity;

/**
 * Created by suikajy on 2017/4/20.
 * 专题列表项
 */

public class SpecialItem extends SectionEntity<NewsItemInfo>{

    public SpecialItem(boolean isHeader, String header) {
        super(isHeader, header);
    }

    public SpecialItem(NewsItemInfo newsItemBean) {
        super(newsItemBean);
    }

}
