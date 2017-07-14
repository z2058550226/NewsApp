package com.suikajy.core;

import android.app.Application;

/**
 * Created by suikajy on 2017/4/4.
 */

public class SuikajyApplication extends Application {

    private static SuikajyApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance=this;
    }

    public static SuikajyApplication getInstance() {
        return instance;
    }
}
