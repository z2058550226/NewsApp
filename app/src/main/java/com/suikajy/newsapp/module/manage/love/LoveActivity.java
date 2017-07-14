package com.suikajy.newsapp.module.manage.love;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import com.flyco.tablayout.SlidingTabLayout;
import com.suikajy.newsapp.R;
import com.suikajy.newsapp.adapter.ViewPagerAdapter;
import com.suikajy.newsapp.module.base.activity.FragmentActivity;
import com.suikajy.newsapp.module.manage.love.photo.LovePhotoFragment;
import com.suikajy.newsapp.module.manage.love.video.LoveVideoFragment;

import java.util.ArrayList;

import butterknife.BindView;

import static com.suikajy.newsapp.utils.CommonConstant.INDEX_KEY;

/**
 * Created by suikajy on 2017/4/14.
 * 收藏Activity
 */

public class LoveActivity extends FragmentActivity {

    @BindView(R.id.tool_bar)
    Toolbar mToolBar;
    @BindView(R.id.tab_layout)
    SlidingTabLayout mTabLayout;
    @BindView(R.id.view_pager)
    ViewPager mViewPager;

    ViewPagerAdapter mPagerAdapter;
    private int mIndex;

    /**
     * 跳转到本acitivity的函数
     * @param context 上下文
     * @param index 索引, 用来控制直接展示的是视频收藏还是图片收藏
     */
    public static void launch(Context context, int index) {
        Intent intent = new Intent(context, LoveActivity.class);
        intent.putExtra(INDEX_KEY, index);
        context.startActivity(intent);
        ((Activity) context).overridePendingTransition(R.anim.zoom_out_entry, R.anim.hold);
    }

    @Override
    protected void initInjector() {

    }

    @Override
    protected int setLayoutResourceID() {
        return R.layout.activity_love;
    }

    @Override
    protected void onInit() {
        mIndex = getIntent().getIntExtra(INDEX_KEY, 0);
        initToolBar(mToolBar, true, "收藏");
        mPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
    }

    @Override
    protected void updateViews(boolean isRefresh) {
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(new LovePhotoFragment());
        fragments.add(new LoveVideoFragment());
        mTabLayout.setViewPager(mViewPager, new String[] {"图片", "视频"}, this, fragments);
        mViewPager.setOffscreenPageLimit(fragments.size());
        mViewPager.setCurrentItem(mIndex);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.hold, R.anim.zoom_out_exit);
    }
}
