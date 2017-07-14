package com.suikajy.newsapp.module.video.list;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.suikajy.newsapp.R;
import com.suikajy.newsapp.adapter.SlideInBottomAdapter;
import com.suikajy.newsapp.injector.components.DaggerVideoListComponent;
import com.suikajy.newsapp.injector.modules.VideoListModule;
import com.suikajy.newsapp.local.table.VideoInfo;
import com.suikajy.newsapp.module.base.fragment.BaseFragment;
import com.suikajy.newsapp.module.base.presenter.IBasePresenter;
import com.suikajy.newsapp.module.base.view.ILoadDataView;
import com.suikajy.recyclerviewhelper.adapter.BaseQuickAdapter;
import com.suikajy.recyclerviewhelper.helper.RecyclerViewHelper;
import com.suikajy.recyclerviewhelper.listener.OnRequestDataListener;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * Created by suikajy on 2017/5/2.
 */

public class VideoListFragment extends BaseFragment<IBasePresenter> implements ILoadDataView<List<VideoInfo>> {

    private static final String VIDEO_ID_KEY = "VideoIdKey";

    @Inject
    BaseQuickAdapter mAdapter;

    @BindView(R.id.rv_photo_list)
    RecyclerView mRvPhotoList;

    private String mVideoId;

    public static VideoListFragment newInstance(String videoId) {
        VideoListFragment fragment = new VideoListFragment();
        Bundle bundle = new Bundle();
        bundle.putString(VIDEO_ID_KEY, videoId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mVideoId = getArguments().getString(VIDEO_ID_KEY);
        }
    }

    @Override
    protected int attachLayoutRes() {
        return R.layout.fragment_photo_list;
    }

    @Override
    protected void initInjector() {
        DaggerVideoListComponent.builder()
                .videoListModule(new VideoListModule(this, mVideoId))
                .build()
                .inject(this);
    }

    @Override
    public void loadData(List<VideoInfo> data) {
        mAdapter.updateItems(data);
        for (VideoInfo bean : data) {
            Log.e("VideoListFragment", bean.toString());
        }
    }

    @Override
    public void loadMoreData(List<VideoInfo> data) {
        mAdapter.loadComplete();
        mAdapter.addItems(data);
    }

    @Override
    public void loadNoData() {
        mAdapter.loadAbnormal();
    }

    @Override
    protected void updateViews(boolean isRefresh) {
        mPresenter.getData(isRefresh);
    }

    @Override
    protected void initViews() {
        SlideInBottomAdapter slideAdapter = new SlideInBottomAdapter(mAdapter);
        RecyclerViewHelper.initRecyclerViewV(mContext, mRvPhotoList, slideAdapter);
        mAdapter.setRequestDataListener(new OnRequestDataListener() {
            @Override
            public void onLoadMore() {
                mPresenter.getMoreData();
            }
        });
    }
}
