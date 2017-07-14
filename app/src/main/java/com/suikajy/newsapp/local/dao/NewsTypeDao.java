package com.suikajy.newsapp.local.dao;

import android.content.Context;

import com.suikajy.core.utils.GsonHelper;
import com.suikajy.newsapp.local.table.DaoSession;
import com.suikajy.newsapp.local.table.NewsTypeInfo;
import com.suikajy.newsapp.local.table.NewsTypeInfoDao;
import com.suikajy.core.utils.AssetsHelper;

import java.util.List;

/**
 * Created by suikajy on 2017/4/7.
 * 新闻分类数据访问
 */

public class NewsTypeDao {

    private static List<NewsTypeInfo> sAllChannels;

    private NewsTypeDao() {

    }

    /**
     * 更新本地数据库, 如果数据库新闻列表栏目为0则添加头三个栏目
     */
    public static void updateLocalData(Context context, DaoSession daoSession) {
        sAllChannels = GsonHelper.convertEntities(AssetsHelper.readData(context, "NewsChannel"), NewsTypeInfo.class);
        NewsTypeInfoDao beanDao = daoSession.getNewsTypeInfoDao();
        if (beanDao.count() == 0) {
            beanDao.insertInTx(sAllChannels.subList(0, 3));
        }
    }

    public static List<NewsTypeInfo> getAllChannels() {
        return sAllChannels;
    }
}
