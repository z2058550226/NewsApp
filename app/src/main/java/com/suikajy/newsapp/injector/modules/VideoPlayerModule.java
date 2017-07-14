package com.suikajy.newsapp.injector.modules;

import com.suikajy.newsapp.injector.PerActivity;
import com.suikajy.newsapp.local.table.DaoSession;
import com.suikajy.newsapp.local.table.VideoInfo;
import com.suikajy.newsapp.module.video.player.IVideoPresenter;
import com.suikajy.newsapp.module.video.player.VideoPlayerActivity;
import com.suikajy.newsapp.module.video.player.VideoPlayerPresenter;
import com.suikajy.newsapp.rxbus.RxBus;

import dagger.Module;
import dagger.Provides;

/**
 * Created by suikajy on 2017/5/12.
 * Video Module
 */
@Module
public class VideoPlayerModule {

    private final VideoPlayerActivity mView;
    private final VideoInfo mVideoData;

    public VideoPlayerModule(VideoPlayerActivity view, VideoInfo videoData) {
        this.mView = view;
        this.mVideoData = videoData;
    }

    @PerActivity
    @Provides
    public IVideoPresenter providePresenter(DaoSession daoSession, RxBus rxBus) {
        return new VideoPlayerPresenter(mView, daoSession.getVideoInfoDao(), rxBus, mVideoData, daoSession.getDanmakuInfoDao());
    }

}
