package com.suikajy.newsapp.module.news.special;

import com.orhanobut.logger.Logger;
import com.suikajy.newsapp.adapter.item.SpecialItem;
import com.suikajy.newsapp.api.RetrofitService;
import com.suikajy.newsapp.api.bean.NewsItemInfo;
import com.suikajy.newsapp.api.bean.SpecialInfo;
import com.suikajy.newsapp.module.base.presenter.IBasePresenter;
import com.suikajy.newsapp.widget.EmptyLayout;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;

/**
 * Created by suikajy on 2017/4/20.
 */

public class SpecialPresenter implements IBasePresenter {


    private final String mSpecialId;
    private final ISpecialView mView;

    public SpecialPresenter(ISpecialView view, String specialId) {
        this.mSpecialId = specialId;
        this.mView = view;
    }

    @Override
    public void getData(boolean isRefresh) {
        RetrofitService.getSpecial(mSpecialId)
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        mView.showLoading();
                    }
                })
                .flatMap(new Func1<SpecialInfo, Observable<SpecialItem>>() {
                    @Override
                    public Observable<SpecialItem> call(SpecialInfo specialBean) {
                        mView.loadBanner(specialBean);
                        return _convertSpecialBeanToItem(specialBean);
                    }
                })
                .toList()
                .compose(mView.<List<SpecialItem>>bindToLife())
                .subscribe(new Subscriber<List<SpecialItem>>() {
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
                    public void onNext(List<SpecialItem> specialItems) {
                        mView.loadData(specialItems);
                    }
                });
    }

    @Override
    public void getMoreData() {

    }

    /**
     * 转换数据，接口数据有点乱，这里做一些处理
     *
     * 实现了二级拆分, 并将item分类, 并统一成一个类发射
     *
     * @param specialBean
     * @return
     */
    private Observable<SpecialItem> _convertSpecialBeanToItem(SpecialInfo specialBean) {
        // 这边 +1 是接口数据还有个 topicsplus 的字段可能是穿插在 topics 字段列表中间。这里没有处理 topicsplus
        final SpecialItem[] specialItems = new SpecialItem[specialBean.getTopics().size() + 1];
        return Observable.from(specialBean.getTopics())
                // 获取头部
                .doOnNext(new Action1<SpecialInfo.TopicsEntity>() {
                    @Override
                    public void call(SpecialInfo.TopicsEntity topicsEntity) {
                        specialItems[topicsEntity.getIndex() - 1] = new SpecialItem(true,
                                topicsEntity.getIndex() + "/" + specialItems.length + " " + topicsEntity.getTname());
                    }
                })
                // 排序
                .toSortedList(new Func2<SpecialInfo.TopicsEntity, SpecialInfo.TopicsEntity, Integer>() {
                    @Override
                    public Integer call(SpecialInfo.TopicsEntity topicsEntity, SpecialInfo.TopicsEntity topicsEntity2) {
                        return topicsEntity.getIndex() - topicsEntity2.getIndex();
                    }
                })
                // 拆分
                .flatMap(new Func1<List<SpecialInfo.TopicsEntity>, Observable<SpecialInfo.TopicsEntity>>() {
                    @Override
                    public Observable<SpecialInfo.TopicsEntity> call(List<SpecialInfo.TopicsEntity> topicsEntities) {
                        return Observable.from(topicsEntities);
                    }
                })
                .flatMap(new Func1<SpecialInfo.TopicsEntity, Observable<SpecialItem>>() {
                    @Override
                    public Observable<SpecialItem> call(SpecialInfo.TopicsEntity topicsEntity) {
                        // 转换并在每个列表项增加头部
                        return Observable.from(topicsEntity.getDocs())
                                .map(new Func1<NewsItemInfo, SpecialItem>() {
                                    @Override
                                    public SpecialItem call(NewsItemInfo newsItemBean) {
                                        return new SpecialItem(newsItemBean);
                                    }
                                })
                                .startWith(specialItems[topicsEntity.getIndex() - 1]);
                    }
                });
    }
}
