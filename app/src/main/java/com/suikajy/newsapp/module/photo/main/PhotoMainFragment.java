package com.suikajy.newsapp.module.photo.main;

import android.animation.Animator;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.flyco.tablayout.SlidingTabLayout;
import com.suikajy.core.animation.AnimationHelper;
import com.suikajy.newsapp.R;
import com.suikajy.newsapp.adapter.ViewPagerAdapter;
import com.suikajy.newsapp.injector.components.DaggerPhotoMainComponent;
import com.suikajy.newsapp.injector.modules.PhotoMainModule;
import com.suikajy.newsapp.module.base.fragment.BaseFragment;
import com.suikajy.newsapp.module.base.presenter.IRxBusPresenter;
import com.suikajy.newsapp.module.manage.love.LoveActivity;
import com.suikajy.newsapp.module.photo.beauty.BeautyListFragment;
import com.suikajy.newsapp.module.photo.news.PhotoNewsFragment;
import com.suikajy.newsapp.module.photo.welfare.WelfareListFragment;
import com.suikajy.newsapp.rxbus.event.LoveEvent;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import rx.functions.Action1;

/**
 * Created by suikajy on 2017/4/13.
 */

public class PhotoMainFragment extends BaseFragment<IRxBusPresenter> implements IPhotoMainView {

    @BindView(R.id.iv_count)
    TextView mIvCount;
    @BindView(R.id.tab_layout)
    SlidingTabLayout mTabLayout;
    @BindView(R.id.tool_bar)
    Toolbar mToolBar;
    @BindView(R.id.view_pager)
    ViewPager mViewPager;

    @Inject
    ViewPagerAdapter mPagerAdapter;
    private Animator mLovedAnimator;

    @Override
    protected int attachLayoutRes() {
        return R.layout.fragment_photo_main;
    }

    @Override
    protected void initInjector() {
        DaggerPhotoMainComponent.builder()
                .applicationComponent(getAppComponent())
                .photoMainModule(new PhotoMainModule(this))
                .build()
                .inject(this);
    }

    @Override
    protected void initViews() {
        initToolBar(mToolBar, true, "图片");
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(new BeautyListFragment());
        fragments.add(new WelfareListFragment());
        fragments.add(new PhotoNewsFragment());
        mPagerAdapter.setItems(fragments, new String[]{"大图", "福利", "生活"});
        mViewPager.setAdapter(mPagerAdapter);
        mTabLayout.setViewPager(mViewPager);
        mPresenter.registerRxBus(LoveEvent.class, new Action1<LoveEvent>() {
            @Override
            public void call(LoveEvent loveEvent) {
                mPresenter.getData(false);
            }
        });
    }

    @Override
    protected void updateViews(boolean isRefresh) {
        mPresenter.getData(isRefresh);
    }

    @Override
    public void updateCount(int lovedCount) {
        mIvCount.setText(String.valueOf(lovedCount));
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mLovedAnimator == null) {
            mIvCount.post(new Runnable() {
                @Override
                public void run() {
                    mLovedAnimator = AnimationHelper.doHappyJump(mIvCount, mIvCount.getHeight() * 2 / 3, 3000);
                }
            });
        } else {
            AnimationHelper.startAnimator(mLovedAnimator);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        AnimationHelper.stopAnimator(mLovedAnimator);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.unregisterRxBus();
    }

    @OnClick(R.id.fl_layout)
    public void onClick() {
        LoveActivity.launch(mContext, 0);
    }
}
