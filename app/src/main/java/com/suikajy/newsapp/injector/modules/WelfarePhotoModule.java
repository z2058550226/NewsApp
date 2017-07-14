package com.suikajy.newsapp.injector.modules;

import com.suikajy.newsapp.adapter.WelfarePhotoAdapter;
import com.suikajy.newsapp.injector.PerFragment;
import com.suikajy.newsapp.module.base.presenter.IBasePresenter;
import com.suikajy.newsapp.module.photo.welfare.WelfareListFragment;
import com.suikajy.newsapp.module.photo.welfare.WelfareListPresenter;
import com.suikajy.recyclerviewhelper.adapter.BaseQuickAdapter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by suikajy on 2017/4/16.
 *
 */


@Module
public class WelfarePhotoModule {

    private final WelfareListFragment mView;

    public WelfarePhotoModule(WelfareListFragment view) {
        this.mView = view;
    }

    @PerFragment
    @Provides
    public IBasePresenter providePresenter() {
        return new WelfareListPresenter(mView);
    }

    @PerFragment
    @Provides
    public BaseQuickAdapter provideAdapter() {
        return new WelfarePhotoAdapter(mView.getContext());
    }
}
