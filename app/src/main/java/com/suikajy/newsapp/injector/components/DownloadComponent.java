package com.suikajy.newsapp.injector.components;

import com.suikajy.newsapp.injector.PerActivity;
import com.suikajy.newsapp.injector.modules.DownloadModule;
import com.suikajy.newsapp.module.manage.download.DownloadActivity;

import dagger.Component;

/**
 * Created by suikajy on 2017/5/22.
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = DownloadModule.class)
public interface DownloadComponent {
    void inject(DownloadActivity activity);
}
