package com.suikajy.newsapp.module.news.channel;

import com.orhanobut.logger.Logger;
import com.suikajy.newsapp.local.dao.NewsTypeDao;
import com.suikajy.newsapp.local.table.NewsTypeInfo;
import com.suikajy.newsapp.local.table.NewsTypeInfoDao;
import com.suikajy.newsapp.rxbus.RxBus;
import com.suikajy.newsapp.rxbus.event.ChannelEvent;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by suikajy on 2017/4/30.
 */

public class ChannelPresenter implements IChannelPresenter<NewsTypeInfo> {

    private final IChannelView mView;
    private final NewsTypeInfoDao mDbDao;
    private final RxBus mRxBus;

    public ChannelPresenter(IChannelView view, NewsTypeInfoDao dbDao, RxBus rxBus) {
        mView = view;
        mDbDao = dbDao;
        mRxBus = rxBus;
    }

    @Override
    public void getData(boolean isRefresh) {
// 从数据库获取
        final List<NewsTypeInfo> checkList = mDbDao.queryBuilder().list();
        final List<String> typeList = new ArrayList<>();
        for (NewsTypeInfo bean : checkList) {
            typeList.add(bean.getTypeId());
        }
        Observable.from(NewsTypeDao.getAllChannels())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .filter(new Func1<NewsTypeInfo, Boolean>() {
                    @Override
                    public Boolean call(NewsTypeInfo newsTypeBean) {
                        // 过滤掉已经选中的栏目
                        return !typeList.contains(newsTypeBean.getTypeId());
                    }
                })
                .toList()
                .subscribe(new Subscriber<List<NewsTypeInfo>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.e(e.toString());
                    }

                    @Override
                    public void onNext(List<NewsTypeInfo> uncheckList) {
                        mView.loadData(checkList, uncheckList);
                    }
                });
    }

    @Override
    public void insert(final NewsTypeInfo data) {
        mDbDao.rx().insert(data)
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<NewsTypeInfo>() {
                    @Override
                    public void onCompleted() {
                        mRxBus.post(new ChannelEvent(ChannelEvent.ADD_EVENT, data));
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.e(e.toString());
                    }

                    @Override
                    public void onNext(NewsTypeInfo newsTypeBean) {
                        Logger.w(newsTypeBean.toString());
                    }
                });
    }

    @Override
    public void delete(final NewsTypeInfo data) {
        mDbDao.rx().delete(data)
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<Void>() {
                    @Override
                    public void onCompleted() {
                        mRxBus.post(new ChannelEvent(ChannelEvent.DEL_EVENT, data));
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.e(e.toString());
                    }

                    @Override
                    public void onNext(Void aVoid) {
                    }
                });
    }

    @Override
    public void update(List<NewsTypeInfo> list) {
        // 这做法不太妥当，而且列表在交互位置时可能产生很多无用的交互没处理掉，暂时没想到简便的方法来处理，以后有想法再改。
        Observable.from(list)
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        // 清空数据库
                        mDbDao.deleteAll();
                    }
                })
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<NewsTypeInfo>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.e(e.toString());
                    }

                    @Override
                    public void onNext(NewsTypeInfo newsTypeBean) {
                        // 把ID清除再加入数据库会从新按列表顺序递增ID
                        newsTypeBean.setId(null);
                        mDbDao.save(newsTypeBean);
                    }
                });
    }

    @Override
    public void getMoreData() {

    }

    @Override
    public void swap(int fromPos, int toPos) {
        mRxBus.post(new ChannelEvent(ChannelEvent.SWAP_EVENT, fromPos, toPos));
    }
}
