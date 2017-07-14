package com.suikajy.newsapp;

import android.app.Application;

import com.orhanobut.logger.Logger;
import com.suikajy.core.utils.PreferencesUtils;
import com.suikajy.core.utils.ToastUtil;
import com.suikajy.downloaderlib.DownloadConfig;
import com.suikajy.downloaderlib.FileDownloader;
import com.suikajy.newsapp.api.RetrofitService;
import com.suikajy.newsapp.engine.DownloaderWrapper;
import com.suikajy.newsapp.injector.components.ApplicationComponent;
import com.suikajy.newsapp.injector.components.DaggerApplicationComponent;
import com.suikajy.newsapp.injector.modules.ApplicationModule;
import com.suikajy.newsapp.local.dao.NewsTypeDao;
import com.suikajy.newsapp.local.table.DaoMaster;
import com.suikajy.newsapp.local.table.DaoSession;
import com.suikajy.newsapp.module.manage.setting.SettingsFragment;
import com.suikajy.newsapp.rxbus.RxBus;
import com.suikajy.newsapp.utils.DownloadUtils;

import org.greenrobot.greendao.database.Database;

import java.io.File;

/**
 * Created by suikajy on 2017/4/4.
 * application
 */

public class App extends Application {

    private static final String DB_NAME = "news-db";

    private static ApplicationComponent sAppComponent;
    private DaoSession mDaoSession;
    private RxBus mRxBus = new RxBus();

    @Override
    public void onCreate() {
        super.onCreate();
        _initDatabase();
        _initInjector();
        _initConfig();
    }

    /**
     * 获取Application的Dagger组件
     */
    public static ApplicationComponent getAppComponent() {
        return sAppComponent;
    }

    private void _initDatabase() {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, DB_NAME);
        Database database = helper.getWritableDb();
        mDaoSession = new DaoMaster(database).newSession();
        NewsTypeDao.updateLocalData(this, mDaoSession);
        DownloadUtils.init(mDaoSession.getBeautyPhotoInfoDao());
    }

    /**
     * 这里没有注入, 只是向外提供属性, 单例由Dagger注解完成
     */
    private void _initInjector() {
        sAppComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this, mDaoSession, mRxBus))
                .build();
    }

    private void _initConfig() {
        if (BuildConfig.DEBUG) {
//            LeakCanary.install(getApplication());
            Logger.init("LogTAG");
        }
        RetrofitService.init();
        DownloaderWrapper.init(mRxBus, mDaoSession.getVideoInfoDao());
        FileDownloader.init(this);
        DownloadConfig config = new DownloadConfig.Builder()
                .setDownloadDir(PreferencesUtils.getString(this, SettingsFragment.SAVE_PATH_KEY, SettingsFragment.DEFAULT_SAVE_PATH) + File.separator + "video/").build();
        FileDownloader.setConfig(config);
    }

    public static void showToast(String msg){
        ToastUtil.showToast(getAppComponent().getContext(),msg);
    }

}
