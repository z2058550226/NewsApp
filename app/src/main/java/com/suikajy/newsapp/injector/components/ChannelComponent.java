package com.suikajy.newsapp.injector.components;

import com.suikajy.newsapp.injector.PerActivity;
import com.suikajy.newsapp.injector.modules.ChannelModule;
import com.suikajy.newsapp.module.news.channel.ChannelActivity;

import dagger.Component;

/**
 * Created by suikajy on 2017/4/30.
 *
 */

@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = ChannelModule.class)
public interface ChannelComponent {

    void inject(ChannelActivity activity);

}
