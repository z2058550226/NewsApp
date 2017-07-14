package com.suikajy.newsapp.module.news.newslist;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.daimajia.slider.library.SliderLayout;
import com.suikajy.newsapp.R;
import com.suikajy.newsapp.adapter.item.NewsMultiItem;
import com.suikajy.newsapp.api.bean.NewsInfo;
import com.suikajy.newsapp.injector.components.DaggerNewsListComponent;
import com.suikajy.newsapp.injector.modules.NewsListModule;
import com.suikajy.newsapp.module.base.presenter.IBasePresenter;
import com.suikajy.newsapp.module.base.fragment.BaseFragment;
import com.suikajy.newsapp.utils.SliderHelper;
import com.suikajy.recyclerviewhelper.adapter.BaseQuickAdapter;
import com.suikajy.recyclerviewhelper.helper.RecyclerViewHelper;
import com.suikajy.recyclerviewhelper.listener.OnRequestDataListener;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import jp.wasabeef.recyclerview.adapters.SlideInRightAnimationAdapter;

/**
 * Created by suikajy on 2017/4/8.
 *
 */

public class NewsListFragment extends BaseFragment<IBasePresenter> implements INewsListView {

    private static final String NEWS_TYPE_KEY = "NewsTypeKey";

    @BindView(R.id.rv_news_list1)
    RecyclerView mRvNewsList1;

    @Inject
    BaseQuickAdapter mAdapter;
    private SliderLayout mAdSlider;

    private String mNewsId;

    public static NewsListFragment newInstance(String newsId) {
        NewsListFragment fragment = new NewsListFragment();
        Bundle bundle = new Bundle();
        bundle.putString(NEWS_TYPE_KEY, newsId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mNewsId = getArguments().getString(NEWS_TYPE_KEY);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mAdSlider != null) {
            mAdSlider.startAutoCycle();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAdSlider != null) {
            mAdSlider.stopAutoCycle();
        }
    }


    @Override
    protected int attachLayoutRes() {
        return R.layout.fragment_news_list;
    }

    @Override
    protected void initInjector() {
        DaggerNewsListComponent.builder()
                .applicationComponent(getAppComponent())
                .newsListModule(new NewsListModule(this, mNewsId))
                .build()
                .inject(this);
    }

    @Override
    protected void initViews() {
        //添加slide库的从右划入的列表项动画, 这个库的用法很简单, 就是在new的时候包裹一个自己的adapter就可以了
        //如果要想实现动画叠加, 可以多包裹几个这种Adapter
        SlideInRightAnimationAdapter animationAdapter = new SlideInRightAnimationAdapter(mAdapter);
        RecyclerViewHelper.initRecyclerViewV(mContext, mRvNewsList1, true, animationAdapter);
        mAdapter.setRequestDataListener(new OnRequestDataListener() {
            @Override
            public void onLoadMore() {
                mPresenter.getMoreData();
            }
        });
    }

    @Override
    protected void updateViews(boolean isRefresh) {
        mPresenter.getData(isRefresh);
    }

    @Override
    public void loadData(List<NewsMultiItem> data) {
        mAdapter.updateItems(data);
    }

    @Override
    public void loadMoreData(List<NewsMultiItem> data) {
        mAdapter.loadComplete();
        mAdapter.addItems(data);
    }

    @Override
    public void loadNoData() {
        mAdapter.loadAbnormal();
    }

    @Override
    public void loadAdData(NewsInfo newsBean) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.head_news_list, null);
        mAdSlider = (SliderLayout) view.findViewById(R.id.slider_ads);
        SliderHelper.initAdSlider(mContext, mAdSlider, newsBean);
        mAdapter.addHeaderView(view);
    }
}
