package com.suikajy.newsapp.injector.modules;

import com.suikajy.newsapp.adapter.PhotoPagerAdapter;
import com.suikajy.newsapp.injector.PerActivity;
import com.suikajy.newsapp.local.table.BeautyPhotoInfo;
import com.suikajy.newsapp.local.table.DaoSession;
import com.suikajy.newsapp.module.base.presenter.ILocalPresenter;
import com.suikajy.newsapp.module.photo.bigphoto.BigPhotoActivity;
import com.suikajy.newsapp.module.photo.bigphoto.BigPhotoPresenter;
import com.suikajy.newsapp.rxbus.RxBus;

import java.util.List;

import dagger.Module;
import dagger.Provides;

/**
 * Created by suikajy on 2017/4/17.
 *
 */

@Module
public class BigPhotoModule {

    private final BigPhotoActivity mView;
    private List<BeautyPhotoInfo> mPhotoList;

    public BigPhotoModule(BigPhotoActivity view, List<BeautyPhotoInfo> photoList) {
        this.mView = view;
        this.mPhotoList = photoList;
    }

    @PerActivity
    @Provides
    public ILocalPresenter providePresenter(DaoSession daoSession, RxBus rxBus) {
        return new BigPhotoPresenter(mView, daoSession.getBeautyPhotoInfoDao(), mPhotoList, rxBus);
    }

    @PerActivity
    @Provides
    public PhotoPagerAdapter provideAdapter() {
        return new PhotoPagerAdapter(mView);
    }


}
