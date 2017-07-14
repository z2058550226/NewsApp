package com.suikajy.newsapp.module.manage.setting;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.Toolbar;

import com.suikajy.newsapp.R;
import com.suikajy.newsapp.module.base.activity.BaseSwipeBackActivity;

import butterknife.BindView;

public class SettingsActivity extends BaseSwipeBackActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;


    public static void launch(Context context) {
        Intent intent = new Intent(context, SettingsActivity.class);
        context.startActivity(intent);
        ((Activity)context).overridePendingTransition(R.anim.slide_right_entry, R.anim.hold);
    }

    @Override
    protected void initInjector() {
    }

    @Override
    protected int setLayoutResourceID() {
        return R.layout.activity_settings;
    }

    @Override
    protected void onInit() {
        initToolBar(mToolbar, true, "设置");
    }

    @Override
    protected void updateViews(boolean isRefresh) {
        getFragmentManager().beginTransaction().replace(R.id.settings_view, new SettingsFragment()).commit();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.hold, R.anim.slide_right_exit);
    }
}
