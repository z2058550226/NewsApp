package com.suikajy.newsapp.injector.modules;

import com.suikajy.newsapp.adapter.VideoListAdapter;
import com.suikajy.newsapp.injector.PerFragment;
import com.suikajy.newsapp.module.base.presenter.IBasePresenter;
import com.suikajy.newsapp.module.video.list.VideoListFragment;
import com.suikajy.newsapp.module.video.list.VideoListPresenter;
import com.suikajy.recyclerviewhelper.adapter.BaseQuickAdapter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by suikajy on 2017/5/2.
 */

@Module
public class VideoListModule {

    private final VideoListFragment mView;
    private final String mVideoId;

    public VideoListModule(VideoListFragment view, String videoId) {
        this.mView = view;
        this.mVideoId = videoId;
    }

    @PerFragment
    @Provides
    public IBasePresenter providePresenter() {
        return new VideoListPresenter(mView, mVideoId);
    }

    @PerFragment
    @Provides
    public BaseQuickAdapter provideAdapter() {
        return new VideoListAdapter(mView.getContext());
    }

}
