package com.suikajy.newsapp.injector.components;

import com.suikajy.newsapp.injector.PerFragment;
import com.suikajy.newsapp.injector.modules.VideoMainModule;
import com.suikajy.newsapp.module.video.main.VideoMainFragment;

import dagger.Component;

/**
 * Created by suikajy on 2017/5/2.
 */


@PerFragment
@Component(dependencies = ApplicationComponent.class, modules = VideoMainModule.class)
public interface VideoMainComponent {
    void inject(VideoMainFragment fragment);
}
