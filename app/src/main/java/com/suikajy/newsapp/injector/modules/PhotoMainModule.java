package com.suikajy.newsapp.injector.modules;

import com.suikajy.newsapp.adapter.ViewPagerAdapter;
import com.suikajy.newsapp.injector.PerFragment;
import com.suikajy.newsapp.local.table.DaoSession;
import com.suikajy.newsapp.module.base.presenter.IRxBusPresenter;
import com.suikajy.newsapp.module.photo.main.PhotoMainFragment;
import com.suikajy.newsapp.module.photo.main.PhotoMainPresenter;
import com.suikajy.newsapp.rxbus.RxBus;

import dagger.Module;
import dagger.Provides;

/**
 * Created by suikajy on 2017/4/13.
 */
@Module
public class PhotoMainModule {

    private final PhotoMainFragment mView;

    public PhotoMainModule(PhotoMainFragment mView) {
        this.mView = mView;
    }

    @PerFragment
    @Provides
    public ViewPagerAdapter provideViewPagerAdapter() {
        return new ViewPagerAdapter(mView.getChildFragmentManager());
    }

    @PerFragment
    @Provides
    public IRxBusPresenter providePhotosPresenter(DaoSession daoSession, RxBus rxBus) {
        return new PhotoMainPresenter(mView, daoSession.getBeautyPhotoInfoDao(), rxBus);
    }
}
