package com.suikajy.newsapp.injector.components;

import com.suikajy.newsapp.injector.PerActivity;
import com.suikajy.newsapp.injector.modules.BigPhotoModule;
import com.suikajy.newsapp.module.photo.bigphoto.BigPhotoActivity;

import dagger.Component;

/**
 * Created by suikajy on 2017/4/17.
 */

@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = BigPhotoModule.class)
public interface BigPhotoComponent {

    void inject(BigPhotoActivity activity);

}
