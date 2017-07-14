package com.suikajy.newsapp.injector.components;

import com.suikajy.newsapp.injector.PerFragment;
import com.suikajy.newsapp.injector.modules.PhotoMainModule;
import com.suikajy.newsapp.module.photo.main.PhotoMainFragment;

import dagger.Component;

/**
 * Created by suikajy on 2017/4/13.
 */
@PerFragment
@Component(dependencies = ApplicationComponent.class,modules = PhotoMainModule.class)
public interface PhotoMainComponent {
    void inject(PhotoMainFragment fragment);
}
