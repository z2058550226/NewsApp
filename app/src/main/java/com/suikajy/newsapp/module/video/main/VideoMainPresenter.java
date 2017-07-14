package com.suikajy.newsapp.module.video.main;

import com.orhanobut.logger.Logger;
import com.suikajy.downloaderlib.model.DownloadStatus;
import com.suikajy.newsapp.local.table.VideoInfoDao;
import com.suikajy.newsapp.module.base.presenter.IRxBusPresenter;
import com.suikajy.newsapp.rxbus.RxBus;

import rx.Subscription;
import rx.functions.Action1;

/**
 * Created by suikajy on 2017/5/1.
 */

public class VideoMainPresenter implements IRxBusPresenter {

    private final IVideoMainView mView;
    private final VideoInfoDao mDbDao;
    private final RxBus mRxBus;

    public VideoMainPresenter(IVideoMainView view, VideoInfoDao dbDao, RxBus rxBus) {
        mView = view;
        mDbDao = dbDao;
        mRxBus = rxBus;
    }


    @Override
    public <T> void registerRxBus(Class<T> eventType, Action1<T> action) {
        Subscription subscription = mRxBus.doSubscribe(eventType, action, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                Logger.e(throwable.toString());
            }
        });
        mRxBus.addSubscription(this, subscription);
    }

    @Override
    public void unregisterRxBus() {
        mRxBus.unSubscribe(this);
    }

    @Override
    public void getData(boolean isRefresh) {
        mView.updateLovedCount((int) mDbDao.queryBuilder().where(VideoInfoDao.Properties.IsCollect.eq(true)).count());
        mView.updateDownloadCount((int) mDbDao.queryBuilder()
                .where(VideoInfoDao.Properties.DownloadStatus.notIn(DownloadStatus.NORMAL, DownloadStatus.COMPLETE)).count());
    }

    @Override
    public void getMoreData() {

    }
}
