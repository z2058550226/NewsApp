package com.suikajy.newsapp.module.photo.main;

import com.orhanobut.logger.Logger;
import com.suikajy.newsapp.local.table.BeautyPhotoInfoDao;
import com.suikajy.newsapp.module.base.presenter.IRxBusPresenter;
import com.suikajy.newsapp.rxbus.RxBus;

import rx.Subscription;
import rx.functions.Action1;

/**
 * Created by suikajy on 2017/4/13.
 *
 */

public class PhotoMainPresenter implements IRxBusPresenter {

    private final IPhotoMainView mView;
    private final BeautyPhotoInfoDao mDao;
    private final RxBus mRxBus;

    public PhotoMainPresenter(IPhotoMainView mView, BeautyPhotoInfoDao beautyPhotoInfoDao, RxBus mRxBus) {
        this.mView=mView;
        this.mDao=beautyPhotoInfoDao;
        this.mRxBus=mRxBus;
    }

    @Override
    public void getData(boolean isRefresh) {
        mView.updateCount((int)mDao.queryBuilder().where(BeautyPhotoInfoDao.Properties.IsLove.eq(true)).count());
    }

    @Override
    public void getMoreData() {

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
}
