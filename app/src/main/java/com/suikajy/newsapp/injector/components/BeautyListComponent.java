package com.suikajy.newsapp.injector.components;

import com.suikajy.newsapp.injector.PerFragment;
import com.suikajy.newsapp.injector.modules.BeautyListModule;
import com.suikajy.newsapp.module.photo.beauty.BeautyListFragment;

import dagger.Component;

/**
 * Created by suikajy on 2017/4/15.
 */

@PerFragment
@Component(dependencies = ApplicationComponent.class, modules = BeautyListModule.class)
public interface BeautyListComponent {
    void inject(BeautyListFragment fragment);
}
