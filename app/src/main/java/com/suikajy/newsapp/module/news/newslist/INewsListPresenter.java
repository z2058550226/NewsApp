package com.suikajy.newsapp.module.news.newslist;

import com.orhanobut.logger.Logger;
import com.suikajy.newsapp.App;
import com.suikajy.newsapp.adapter.item.NewsMultiItem;
import com.suikajy.newsapp.api.RetrofitService;
import com.suikajy.newsapp.api.bean.NewsInfo;
import com.suikajy.newsapp.module.base.presenter.IBasePresenter;
import com.suikajy.newsapp.utils.NewsUtils;
import com.suikajy.newsapp.widget.EmptyLayout;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action0;
import rx.functions.Func1;

/**
 * Created by suikajy on 2017/4/8.
 *
 */

public class INewsListPresenter implements IBasePresenter {

    private INewsListView mView;
    private String mNewsId;

    private int mPage = 0;

    public INewsListPresenter(INewsListView view, String newsId) {
        this.mView = view;
        this.mNewsId = newsId;
    }

    @Override
    public void getData(final boolean isRefresh) {
        RetrofitService.getNewsList(mNewsId, mPage)
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        if (!isRefresh) {
                            mView.showLoading();
                        }
                    }
                })
                .filter(new Func1<NewsInfo, Boolean>() {
                    @Override
                    public Boolean call(NewsInfo info) {//这里对news list进行过滤, 如果判断是AbNews就直接转到专门的加载函数
                        if (NewsUtils.isAbNews(info)) {
                            mView.loadAdData(info);
                        }
                        return !NewsUtils.isAbNews(info);
                    }
                })
                .compose(mTransformer)
                .subscribe(new Subscriber<List<NewsMultiItem>>() {
                    @Override
                    public void onCompleted() {
                        Logger.w("onCompleted " + isRefresh);
                        if (isRefresh) {
                            mView.finishRefresh();
                        } else {
                            mView.hideLoading();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.e(e.toString() + " " + isRefresh);
                        if (isRefresh) {
                            mView.finishRefresh();
                            App.showToast("刷新失败, 网络连接或服务器原因");
                        } else {
                            mView.showNetError(new EmptyLayout.OnRetryListener() {
                                @Override
                                public void onRetry() {
                                    getData(false);
                                }
                            });
                        }
                    }

                    @Override
                    public void onNext(List<NewsMultiItem> newsMultiItems) {
                        mView.loadData(newsMultiItems);
                        mPage++;
                    }
                });
    }

    @Override
    public void getMoreData() {
        RetrofitService.getNewsList(mNewsId, mPage)
                .compose(mTransformer)
                .subscribe(new Subscriber<List<NewsMultiItem>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.e(e.toString());
                        mView.loadNoData();
                    }

                    @Override
                    public void onNext(List<NewsMultiItem> newsMultiItems) {
                        mView.loadMoreData(newsMultiItems);
                        mPage++;
                    }
                });
    }

    private Observable.Transformer<NewsInfo, List<NewsMultiItem>> mTransformer = new Observable.Transformer<NewsInfo, List<NewsMultiItem>>() {
        @Override
        public Observable<List<NewsMultiItem>> call(Observable<NewsInfo> newsInfoObservable) {
            return newsInfoObservable
                    .map(new Func1<NewsInfo, NewsMultiItem>() {
                        @Override
                        public NewsMultiItem call(NewsInfo newsBean) {
                            if (NewsUtils.isNewsPhotoSet(newsBean.getSkipType())) {
                                return new NewsMultiItem(NewsMultiItem.ITEM_TYPE_PHOTO_SET, newsBean);
                            }
                            return new NewsMultiItem(NewsMultiItem.ITEM_TYPE_NORMAL, newsBean);
                        }
                    })
                    .toList()
                    .compose(mView.<List<NewsMultiItem>>bindToLife());
        }
    };
}
