package com.suikajy.newsapp.injector.components;

import com.suikajy.newsapp.injector.PerFragment;
import com.suikajy.newsapp.injector.modules.LovePhotoModule;
import com.suikajy.newsapp.module.manage.love.photo.LovePhotoFragment;

import dagger.Component;

/**
 * Created by suikajy on 2017/4/18.
 *
 */
@PerFragment
@Component(dependencies = ApplicationComponent.class, modules = LovePhotoModule.class)
public interface LovePhotoComponent {
    void inject(LovePhotoFragment fragment);
}
