package com.suikajy.newsapp.injector.components;

import com.suikajy.newsapp.injector.PerActivity;
import com.suikajy.newsapp.injector.modules.VideoPlayerModule;
import com.suikajy.newsapp.module.video.player.VideoPlayerActivity;

import dagger.Component;

/**
 * Created by suikajy on 2017/5/12.
 */

@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = VideoPlayerModule.class)
public interface VideoPlayerComponent {
    void inject(VideoPlayerActivity activity);
}
