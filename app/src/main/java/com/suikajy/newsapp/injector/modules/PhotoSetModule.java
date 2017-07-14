package com.suikajy.newsapp.injector.modules;

import com.suikajy.newsapp.injector.PerActivity;
import com.suikajy.newsapp.module.base.presenter.IBasePresenter;
import com.suikajy.newsapp.module.news.photoset.PhotoSetActivity;
import com.suikajy.newsapp.module.news.photoset.PhotoSetPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by suikajy on 2017/4/30.
 */

@Module
public class PhotoSetModule {
    private final PhotoSetActivity mView;
    private final String mPhotoSetId;

    public PhotoSetModule(PhotoSetActivity view, String photoSetId) {
        mView = view;
        mPhotoSetId = photoSetId;
    }

    @PerActivity
    @Provides
    public IBasePresenter providePhotoSetPresenter() {
        return new PhotoSetPresenter(mView, mPhotoSetId);
    }
}
