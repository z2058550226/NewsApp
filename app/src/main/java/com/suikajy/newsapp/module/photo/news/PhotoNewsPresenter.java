package com.suikajy.newsapp.module.photo.news;

import com.orhanobut.logger.Logger;
import com.suikajy.newsapp.api.RetrofitService;
import com.suikajy.newsapp.api.bean.PhotoInfo;
import com.suikajy.newsapp.module.base.presenter.IBasePresenter;
import com.suikajy.newsapp.module.base.view.ILoadDataView;
import com.suikajy.newsapp.widget.EmptyLayout;

import java.util.List;

import rx.Subscriber;
import rx.functions.Action0;

/**
 * Created by suikajy on 2017/4/13.
 */

public class PhotoNewsPresenter implements IBasePresenter {

    private String mNextSetId;
    private ILoadDataView mView;

    public PhotoNewsPresenter(ILoadDataView view) {
        this.mView = view;
    }

    @Override
    public void getData(boolean isRefresh) {
        RetrofitService.getPhotoList()
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        mView.showLoading();
                    }
                })
                .compose(mView.<List<PhotoInfo>>bindToLife())
                .subscribe(new Subscriber<List<PhotoInfo>>() {
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
                    public void onNext(List<PhotoInfo> photoList) {
                        mView.loadData(photoList);
                        mNextSetId = photoList.get(photoList.size() - 1).getSetid();
                    }
                });
    }

    @Override
    public void getMoreData() {
        RetrofitService.getPhotoMoreList(mNextSetId)
                .compose(mView.<List<PhotoInfo>>bindToLife())
                .subscribe(new Subscriber<List<PhotoInfo>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.loadNoData();
                    }

                    @Override
                    public void onNext(List<PhotoInfo> photoList) {
                        mView.loadMoreData(photoList);
                    }
                });
    }
}
