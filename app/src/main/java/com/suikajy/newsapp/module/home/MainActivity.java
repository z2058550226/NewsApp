package com.suikajy.newsapp.module.home;

import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.SparseArray;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.suikajy.core.utils.ToastUtil;
import com.suikajy.newsapp.R;
import com.suikajy.newsapp.module.base.activity.FragmentActivity;
import com.suikajy.newsapp.module.manage.setting.SettingsActivity;
import com.suikajy.newsapp.module.news.main.NewsMainFragment;
import com.suikajy.newsapp.module.photo.main.PhotoMainFragment;
import com.suikajy.newsapp.module.video.main.VideoMainFragment;

import butterknife.BindView;

public class MainActivity extends FragmentActivity implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.fl_main)
    FrameLayout mFlMain;
    @BindView(R.id.nav_view)
    NavigationView mNavView;
    @BindView(R.id.activity_main)
    DrawerLayout mDrawerLayout;

    //每个fragment的标题
    private SparseArray<String> mSparseTags = new SparseArray<>();
    private long mExitTime = 0;
    private int mItemId = -1;
    //用来处理
    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case R.id.nav_news:
                    replaceFragment(R.id.fl_main, new NewsMainFragment(), mSparseTags.get(R.id.nav_news));
                    break;
                case R.id.nav_photos:
                    replaceFragment(R.id.fl_main, new PhotoMainFragment(), mSparseTags.get(R.id.nav_photos));
                    break;
                case R.id.nav_videos:
                    replaceFragment(R.id.fl_main, new VideoMainFragment(), mSparseTags.get(R.id.nav_videos));
                    break;
                case R.id.nav_setting:
                    SettingsActivity.launch(MainActivity.this);
                    break;
            }
            mItemId = -1;
            return true;
        }
    });

    @Override
    protected int setLayoutResourceID() {
        return R.layout.activity_main;
    }

    @Override
    protected void onInit() {
        _initDrawerLayout(mDrawerLayout, mNavView);
        mSparseTags.put(R.id.nav_news, "News");
        mSparseTags.put(R.id.nav_photos, "Photos");
        mSparseTags.put(R.id.nav_videos, "Videos");
    }

    @Override
    protected void initInjector() {

    }

    @Override
    protected void onInitRefresh() {

    }

    @Override
    protected void updateViews(boolean isRefresh) {
        mNavView.setCheckedItem(R.id.nav_news);
        addFragment(R.id.fl_main, new NewsMainFragment(), "News");
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull final MenuItem item) {
        mDrawerLayout.closeDrawer(GravityCompat.START);
        if (item.isChecked()) {
            return true;
        }
        mItemId = item.getItemId();
        return true;
    }

    @Override
    public void onBackPressed() {
        // 获取堆栈里有几个
        final int stackEntryCount = getSupportFragmentManager().getBackStackEntryCount();
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else if (stackEntryCount == 1) {
            // 如果剩一个说明在主页，提示按两次退出app
            _exit();
        } else {
            // 获取上一个堆栈中保存的是哪个页面，根据name来设置导航项的选中状态
            final String tagName = getSupportFragmentManager().getBackStackEntryAt(stackEntryCount - 2).getName();
            mNavView.setCheckedItem(mSparseTags.keyAt(mSparseTags.indexOfValue(tagName)));
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            mDrawerLayout.openDrawer(GravityCompat.START);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 初始化 DrawerLayout
     *
     * @param drawerLayout DrawerLayout
     * @param navView      NavigationView
     */
    private void _initDrawerLayout(DrawerLayout drawerLayout, NavigationView navView) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
            //将侧边栏顶部延伸至status bar
            drawerLayout.setFitsSystemWindows(true);
            //将主页面顶部延伸至status bar
            drawerLayout.setClipToPadding(false);
        }
        drawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerClosed(View drawerView) {
                mHandler.sendEmptyMessage(mItemId);
            }
        });
        navView.setNavigationItemSelectedListener(this);
    }


//    private void _getPermission() {
//        final File dir = new File(FileDownloader.getDownloadDir());
//        if (!dir.exists() || !dir.isDirectory()) {
//            dir.delete();
//            new RxPermissions(this).request(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
//                    .subscribe(new Action1<Boolean>() {
//                        @Override
//                        public void call(Boolean granted) {
//                            if (granted) {
//                                dir.mkdirs();
//                            } else {
//                                SnackbarUtils.showSnackbar(HomeActivity.this, "下载目录创建失败", true);
//                            }
//                        }
//                    });
//        }
//    }


    /**
     * 退出
     */
    private void _exit() {
        if (System.currentTimeMillis() - mExitTime > 2000) {
            ToastUtil.showToast(getAppComponent().getContext(), "再按一次退出程序");
            mExitTime = System.currentTimeMillis();
        } else {
            ToastUtil.cancelToast();
            finish();
        }
    }
}
