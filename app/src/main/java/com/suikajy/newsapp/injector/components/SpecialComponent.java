package com.suikajy.newsapp.injector.components;

import com.suikajy.newsapp.injector.PerActivity;
import com.suikajy.newsapp.injector.modules.SpecialModule;
import com.suikajy.newsapp.module.news.special.SpecialActivity;

import dagger.Component;

/**
 * Created by suikajy on 2017/4/27.
 */

@PerActivity
@Component(modules = SpecialModule.class)
public interface SpecialComponent {
    void inject(SpecialActivity activity);
}
