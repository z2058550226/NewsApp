package com.suikajy.newsapp.module.news.article;

import com.orhanobut.logger.Logger;
import com.suikajy.core.utils.ListUtils;
import com.suikajy.newsapp.api.RetrofitService;
import com.suikajy.newsapp.api.bean.NewsDetailInfo;
import com.suikajy.newsapp.module.base.presenter.IBasePresenter;
import com.suikajy.newsapp.widget.EmptyLayout;

import rx.Subscriber;
import rx.functions.Action0;
import rx.functions.Action1;

/**
 * Created by suikajy on 2017/4/18.
 *
 */

public class NewsArticlePresenter implements IBasePresenter {

    private static final String HTML_IMG_TEMPLATE = "<img src=\"http\" />";

    private final String mNewsId;
    private final INewsArticleView mView;

    public NewsArticlePresenter(String newsId, INewsArticleView view) {
        this.mNewsId = newsId;
        this.mView = view;
    }

    @Override
    public void getData(boolean isRefresh) {
        RetrofitService.getNewsDetail(mNewsId)
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        mView.showLoading();
                    }
                })
                .doOnNext(new Action1<NewsDetailInfo>() {
                    @Override
                    public void call(NewsDetailInfo newsDetailInfo) {
                        _handleRichTextWithImg(newsDetailInfo);
                    }
                })
                .compose(mView.<NewsDetailInfo>bindToLife())
                .subscribe(new Subscriber<NewsDetailInfo>() {
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
                    public void onNext(NewsDetailInfo newsDetailInfo) {
                        mView.loadData(newsDetailInfo);
                    }
                });
    }

    @Override
    public void getMoreData() {

    }

    /**
     * 处理富文本包含图片的情况
     * <p>
     * json中的数据如下
     * <p>　　当你老了，头发白了，儿孙绕膝，你会是怎样的模样？垂垂老矣，睡眼昏沉？</p><!--IMG#0--><!--IMG#1--><p>　　满头银发，但却身材火辣、笑靥如花，气质脱俗优雅。她叫雅思米亚·罗西（Yasmina Rossi），1955年生于法国，今年已经61岁了。</p><!--IMG#2--><!--IMG#3--><p>
     * <p>
     * 该函数的作用就是将body中的<!--IMG#0-->替换成HTML中的img标签, 并将图片的连接放进去
     *
     * @param newsDetailBean 原始数据
     */
    private void _handleRichTextWithImg(NewsDetailInfo newsDetailBean) {
        if (!ListUtils.isEmpty(newsDetailBean.getImg())) {
            String body = newsDetailBean.getBody();
            for (NewsDetailInfo.ImgEntity imgEntity : newsDetailBean.getImg()) {
                String ref = imgEntity.getRef();
                String src = imgEntity.getSrc();
                String img = HTML_IMG_TEMPLATE.replace("http", src);
                body = body.replaceAll(ref, img);
                Logger.w(img);
                Logger.i(body);
            }
            newsDetailBean.setBody(body);
        }
    }
}
