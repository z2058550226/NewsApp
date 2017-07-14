package com.suikajy.newsapp.injector.components;

import com.suikajy.newsapp.injector.PerFragment;
import com.suikajy.newsapp.injector.modules.WelfarePhotoModule;
import com.suikajy.newsapp.module.photo.welfare.WelfareListFragment;

import dagger.Component;

/**
 * Created by suikajy on 2017/4/16.
 */

@PerFragment
@Component(dependencies = ApplicationComponent.class, modules = WelfarePhotoModule.class)
public interface WelfarePhotoComponent {
    void inject(WelfareListFragment fragment);
}
