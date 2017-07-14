package com.suikajy.newsapp.module.photo.beauty;

import com.orhanobut.logger.Logger;
import com.suikajy.newsapp.api.RetrofitService;
import com.suikajy.newsapp.local.table.BeautyPhotoInfo;
import com.suikajy.newsapp.module.base.presenter.IBasePresenter;
import com.suikajy.newsapp.module.base.view.ILoadDataView;
import com.suikajy.newsapp.widget.EmptyLayout;

import java.util.List;

import rx.Subscriber;
import rx.functions.Action0;

/**
 * Created by suikajy on 2017/4/13.
 */

public class BeautyListPresenter implements IBasePresenter {

    private final ILoadDataView mView;

    private int mPage = 0;

    public BeautyListPresenter(ILoadDataView view) {
        this.mView = view;
    }


    @Override
    public void getData(boolean isRefresh) {
        RetrofitService.getBeautyPhoto(mPage)
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        mView.showLoading();
                    }
                })
                .compose(mView.<List<BeautyPhotoInfo>>bindToLife())
                .subscribe(new Subscriber<List<BeautyPhotoInfo>>() {
                    @Override
                    public void onCompleted() {
                        mView.hideLoading();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.e(e.toString());
                        mView.showNetError(new EmptyLayout.OnRetryListener() {
                            @Override
                            public void onRetry() {
                                getData(false);
                            }
                        });
                    }

                    @Override
                    public void onNext(List<BeautyPhotoInfo> beautyPhotoInfos) {
                        mView.loadData(beautyPhotoInfos);
                        mPage++;
                    }
                });
    }

    @Override
    public void getMoreData() {
        RetrofitService.getBeautyPhoto(mPage)
                .compose(mView.<List<BeautyPhotoInfo>>bindToLife())
                .subscribe(new Subscriber<List<BeautyPhotoInfo>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.e(e.toString());
                        mView.loadNoData();
                    }

                    @Override
                    public void onNext(List<BeautyPhotoInfo> beautyPhotoInfos) {
                        mView.loadMoreData(beautyPhotoInfos);
                        mPage++;
                    }
                });
    }
}
