package com.suikajy.newsapp.injector.components;

import com.suikajy.newsapp.injector.PerFragment;
import com.suikajy.newsapp.injector.modules.VideoListModule;
import com.suikajy.newsapp.module.video.list.VideoListFragment;

import dagger.Component;

/**
 * Created by suikajy on 2017/5/2.
 */

@PerFragment
@Component(modules = VideoListModule.class)
public interface VideoListComponent {
    void inject(VideoListFragment fragment);
}
