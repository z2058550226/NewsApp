package com.suikajy.newsapp.module.news.photoset;

import com.orhanobut.logger.Logger;
import com.suikajy.newsapp.api.RetrofitService;
import com.suikajy.newsapp.api.bean.PhotoSetInfo;
import com.suikajy.newsapp.module.base.presenter.IBasePresenter;
import com.suikajy.newsapp.widget.EmptyLayout;

import rx.Subscriber;
import rx.functions.Action0;

/**
 * Created by suikajy on 2017/4/30.
 */

public class PhotoSetPresenter implements IBasePresenter{
    private final IPhotoSetView mView;
    private final String mPhotoSetId;

    public PhotoSetPresenter(IPhotoSetView view, String photoSetId) {
        mView = view;
        mPhotoSetId = photoSetId;
    }

    @Override
    public void getData(boolean isRefresh) {
        RetrofitService.getPhotoSet(mPhotoSetId)
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        mView.showLoading();
                    }
                })
                .compose(mView.<PhotoSetInfo>bindToLife())
                .subscribe(new Subscriber<PhotoSetInfo>() {
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
                    public void onNext(PhotoSetInfo photoSetBean) {
                        mView.loadData(photoSetBean);
                    }
                });
    }

    @Override
    public void getMoreData() {
    }
}
