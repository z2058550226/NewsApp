package com.suikajy.newsapp.injector.components;

import com.suikajy.newsapp.injector.PerFragment;
import com.suikajy.newsapp.injector.modules.VideoCompleteModule;
import com.suikajy.newsapp.module.manage.download.complete.VideoCompleteFragment;

import dagger.Component;

/**
 * Created by suikajy on 2017/5/22.
 */

@PerFragment
@Component(dependencies = ApplicationComponent.class, modules = VideoCompleteModule.class)
public interface VideoCompleteComponent {
    void inject(VideoCompleteFragment fragment);
}
