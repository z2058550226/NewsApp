package com.suikajy.newsapp.module.manage.download;

import com.orhanobut.logger.Logger;
import com.suikajy.newsapp.module.base.presenter.IRxBusPresenter;
import com.suikajy.newsapp.rxbus.RxBus;

import rx.Subscription;
import rx.functions.Action1;

/**
 * Created by suikajy on 2016/12/19.
 * video下载Presenter
 */
public class DownloadPresenter implements IRxBusPresenter {

    private final RxBus mRxBus;

    public DownloadPresenter(RxBus rxBus) {
        mRxBus = rxBus;
    }

    @Override
    public void getData(boolean isRefresh) {
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
