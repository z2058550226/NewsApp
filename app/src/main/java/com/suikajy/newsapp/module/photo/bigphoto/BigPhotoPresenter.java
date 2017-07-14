package com.suikajy.newsapp.module.photo.bigphoto;

import com.orhanobut.logger.Logger;
import com.suikajy.newsapp.api.RetrofitService;
import com.suikajy.newsapp.local.table.BeautyPhotoInfo;
import com.suikajy.newsapp.local.table.BeautyPhotoInfoDao;
import com.suikajy.newsapp.module.base.presenter.ILocalPresenter;
import com.suikajy.newsapp.module.base.view.ILoadDataView;
import com.suikajy.newsapp.rxbus.RxBus;
import com.suikajy.newsapp.rxbus.event.LoveEvent;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by suikajy on 2017/4/13.
 */

public class BigPhotoPresenter implements ILocalPresenter<BeautyPhotoInfo> {

    private final ILoadDataView mView;
    private final BeautyPhotoInfoDao mDbDao;
    private final List<BeautyPhotoInfo> mPhotoList;
    private final RxBus mRxBus;
    private List<BeautyPhotoInfo> mDbLovedData;

    private int mPage = 1;

    public BigPhotoPresenter(ILoadDataView mView, BeautyPhotoInfoDao mDbDao, List<BeautyPhotoInfo> mPhotoList, RxBus mRxBus) {
        this.mView = mView;
        this.mDbDao = mDbDao;
        this.mPhotoList = mPhotoList;
        this.mRxBus = mRxBus;
        mDbLovedData = mDbDao.queryBuilder().list();
    }

    @Override
    public void getData(boolean isRefresh) {
        // 因为网易这个原接口参数一大堆，我只传了部分参数，返回的数据会出现图片重复的情况，请不要在意这个问题- -
        Observable.from(mPhotoList)
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        mView.showLoading();
                    }
                })
                .compose(mTransformer)
                .subscribe(new Subscriber<List<BeautyPhotoInfo>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.e(e.toString());
                    }

                    @Override
                    public void onNext(List<BeautyPhotoInfo> photoList) {
                        mView.loadData(photoList);
                    }
                });
    }

    @Override
    public void getMoreData() {
        RetrofitService.getBeautyPhoto(mPage)
                .flatMap(new Func1<List<BeautyPhotoInfo>, Observable<BeautyPhotoInfo>>() {
                    @Override
                    public Observable<BeautyPhotoInfo> call(List<BeautyPhotoInfo> photoList) {
                        return Observable.from(photoList);
                    }
                })
                .compose(mTransformer)
                .subscribe(new Subscriber<List<BeautyPhotoInfo>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.e(e.toString());
                    }

                    @Override
                    public void onNext(List<BeautyPhotoInfo> photoList) {
                        mView.loadMoreData(photoList);
                        mPage++;
                    }
                });
    }

    @Override
    public void insert(BeautyPhotoInfo data) {
        if (mDbLovedData.contains(data)) {
            mDbDao.update(data);
        } else {
            mDbDao.insert(data);
            mDbLovedData.add(data);
        }
        mRxBus.post(new LoveEvent());
    }

    @Override
    public void delete(BeautyPhotoInfo data) {
        if (!data.isLove() && !data.isDownload() && !data.isPraise()) {
            mDbDao.delete(data);
            mDbLovedData.remove(data);
        } else {
            mDbDao.update(data);
        }
        mRxBus.post(new LoveEvent());
    }

    @Override
    public void update(List list) {

    }

    /**
     * 统一变换
     */
    private Observable.Transformer<BeautyPhotoInfo, List<BeautyPhotoInfo>> mTransformer
            = new Observable.Transformer<BeautyPhotoInfo, List<BeautyPhotoInfo>>() {

        @Override
        public Observable<List<BeautyPhotoInfo>> call(Observable<BeautyPhotoInfo> listObservable) {
            return listObservable
                    .doOnNext(new Action1<BeautyPhotoInfo>() {
                        BeautyPhotoInfo tmpBean;

                        @Override
                        public void call(BeautyPhotoInfo bean) {
                            // 判断数据库是否有数据，有则设置对应参数
                            // 这里详细说明下, 因为收藏是通过本地数据库方式实现的,
                            // 所以从网上返回的数据不能判断该图片是否收藏下载等状态
                            // 所以这里要通过本地数据库的记录来进行更改
                            if (mDbLovedData.contains(bean)) {
                                tmpBean = mDbLovedData.get(mDbLovedData.indexOf(bean));
                                bean.setLove(tmpBean.isLove());
                                bean.setPraise(tmpBean.isPraise());
                                bean.setDownload(tmpBean.isDownload());
                            }
                        }
                    })
                    .toList()
                    .compose(mView.<List<BeautyPhotoInfo>>bindToLife());
        }
    };
}
