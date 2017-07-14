package com.suikajy.newsapp.injector.modules;

import com.suikajy.newsapp.adapter.SpecialAdapter;
import com.suikajy.newsapp.injector.PerActivity;
import com.suikajy.newsapp.module.base.presenter.IBasePresenter;
import com.suikajy.newsapp.module.news.special.SpecialActivity;
import com.suikajy.newsapp.module.news.special.SpecialPresenter;
import com.suikajy.recyclerviewhelper.adapter.BaseQuickAdapter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by suikajy on 2017/4/27.
 */

@Module
public class SpecialModule {
    private final SpecialActivity mView;
    private final String mSpecialId;

    public SpecialModule(SpecialActivity view, String specialId) {
        mView = view;
        mSpecialId = specialId;
    }

    @PerActivity
    @Provides
    public IBasePresenter provideSpecialPresent() {
        return new SpecialPresenter(mView, mSpecialId);
    }

    @PerActivity
    @Provides
    public BaseQuickAdapter provideSpecialAdapter() {
        return new SpecialAdapter(mView);
    }
}
