package com.suikajy.newsapp.module.photo.bigphoto;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.dl7.drag.DragSlopLayout;
import com.jakewharton.rxbinding.view.RxView;
import com.suikajy.core.animation.AnimationHelper;
import com.suikajy.core.utils.NavUtils;
import com.suikajy.newsapp.App;
import com.suikajy.newsapp.R;
import com.suikajy.newsapp.adapter.PhotoPagerAdapter;
import com.suikajy.newsapp.injector.components.DaggerBigPhotoComponent;
import com.suikajy.newsapp.injector.modules.BigPhotoModule;
import com.suikajy.newsapp.local.table.BeautyPhotoInfo;
import com.suikajy.newsapp.module.base.activity.BaseActivity;
import com.suikajy.newsapp.module.base.presenter.ILocalPresenter;
import com.suikajy.newsapp.module.base.view.ILoadDataView;
import com.suikajy.newsapp.utils.CommonConstant;
import com.suikajy.newsapp.utils.SnackBarUtils;
import com.suikajy.newsapp.widget.PhotoViewPager;
import com.tbruyelle.rxpermissions.RxPermissions;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import rx.functions.Action1;

/**
 * Created by suikajy on 2017/4/13.
 */

public class BigPhotoActivity extends BaseActivity<ILocalPresenter> implements ILoadDataView<List<BeautyPhotoInfo>> {

    private static final String BIG_PHOTO_KEY = "BigPhotoKey";
    private static final String PHOTO_INDEX_KEY = "PhotoIndexKey";
    private static final String FROM_LOVE_ACTIVITY = "FromLoveActivity";

    @BindView(R.id.vp_photo)
    PhotoViewPager mVpPhoto;
    @BindView(R.id.iv_favorite)
    ImageView mIvFavorite;
    @BindView(R.id.iv_download)
    ImageView mIvDownload;
    @BindView(R.id.iv_praise)
    ImageView mIvPraise;
    @BindView(R.id.iv_share)
    ImageView mIvShare;
    @BindView(R.id.ll_layout)
    LinearLayout mLlLayout;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.drag_layout)
    DragSlopLayout mDragLayout;


    @Inject
    PhotoPagerAdapter mAdapter;
    private List<BeautyPhotoInfo> mPhotoList;
    private int mIndex; // 初始索引
    private boolean mIsFromLoveActivity;    // 是否从 LoveActivity 启动进来
    private boolean mIsHideToolbar = false; // 是否隐藏 Toolbar
    private boolean mIsInteract = false;    // 是否和 ViewPager 联动
    private int mCurPosition;   // Adapter 当前位置
    private boolean[] mIsDelLove;   // 保存被删除的收藏项
    private RxPermissions mRxPermissions;

    public static void launch(Context context, ArrayList<BeautyPhotoInfo> datas, int index) {
        Intent intent = new Intent(context, BigPhotoActivity.class);
        intent.putParcelableArrayListExtra(BIG_PHOTO_KEY, datas);
        intent.putExtra(PHOTO_INDEX_KEY, index);
        intent.putExtra(FROM_LOVE_ACTIVITY, false);
        context.startActivity(intent);
        ((Activity) context).overridePendingTransition(R.anim.expand_vertical_entry, R.anim.hold);
    }

    // 这个给 LoveActivity 使用，配合 setResult() 返回取消的收藏，这样做体验会好点，其实用 RxBus 会更容易做
    public static void launchForResult(Fragment fragment, ArrayList<BeautyPhotoInfo> datas, int index) {
        Intent intent = new Intent(fragment.getContext(), BigPhotoActivity.class);
        intent.putParcelableArrayListExtra(BIG_PHOTO_KEY, datas);
        intent.putExtra(PHOTO_INDEX_KEY, index);
        intent.putExtra(FROM_LOVE_ACTIVITY, true);
        fragment.startActivityForResult(intent, CommonConstant.REQUEST_CODE);
        fragment.getActivity().overridePendingTransition(R.anim.expand_vertical_entry, R.anim.hold);
    }

    @Override
    protected int setLayoutResourceID() {
        return R.layout.activity_big_photo;
    }

    @Override
    protected void initInjector() {
        mPhotoList = getIntent().getParcelableArrayListExtra(BIG_PHOTO_KEY);
        mIndex = getIntent().getIntExtra(PHOTO_INDEX_KEY, 0);
        mIsFromLoveActivity = getIntent().getBooleanExtra(FROM_LOVE_ACTIVITY, false);
        DaggerBigPhotoComponent.builder()
                .applicationComponent(getAppComponent())
                .bigPhotoModule(new BigPhotoModule(this, mPhotoList))
                .build()
                .inject(this);
    }

    @Override
    protected void onInit() {
        initToolBar(mToolbar, true, "");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mLlLayout.setPadding(0, 0, 0, NavUtils.getNavigationBarHeight(this));
        }
        mVpPhoto.setAdapter(mAdapter);
        mDragLayout.interactWithViewPager(mIsInteract);
        mDragLayout.setAnimatorMode(DragSlopLayout.FLIP_Y);
        mAdapter.setTapListener(new PhotoPagerAdapter.OnTapListener() {
            @Override
            public void onPhotoClick() {
                mIsHideToolbar = !mIsHideToolbar;
                if (mIsHideToolbar) {
                    mDragLayout.startOutAnim();
                    ;
                    mToolbar.animate().translationY(-mToolbar.getBottom()).setDuration(300);
                } else {
                    mDragLayout.startInAnim();
                    ;
                    mToolbar.animate().translationY(0).setDuration(300);
                }
            }
        });
        if (!mIsFromLoveActivity) {
            mAdapter.setLoadMoreListener(new PhotoPagerAdapter.OnLoadMoreListener() {
                @Override
                public void onLoadMore() {
                    mPresenter.getMoreData();
                }
            });
        } else {
            //收藏界面不需要加载更多
            mIsDelLove = new boolean[mPhotoList.size()];
        }
        mVpPhoto.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                mCurPosition = position;
                mIvFavorite.setSelected(mAdapter.isLoved(position));
                mIvDownload.setSelected(mAdapter.isDownload(position));
                mIvPraise.setSelected(mAdapter.isPraise(position));
            }
        });

        mRxPermissions = new RxPermissions(this);
        RxView.clicks(mIvDownload)
                .compose(mRxPermissions.ensure(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE))
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean granted) {
                        if (granted) {
                            //授权成功进行下载或是删除图片
                            App.showToast("进行下载");
                        } else {
                            SnackBarUtils.showSnackbar(BigPhotoActivity.this, "权限授权失败", false);
                        }
                    }
                });
    }

    @Override
    protected void onInitRefresh() {

    }

    @Override
    protected void updateViews(boolean isRefresh) {
        mPresenter.getData(isRefresh);
    }

    @Override
    public void loadData(List<BeautyPhotoInfo> data) {
        mAdapter.updateData(data);
        mVpPhoto.setCurrentItem(mIndex);
        if (mIndex == 0) {
            // 为 0 不会回调 addOnPageChangeListener，所以这里要处理下
            mIvFavorite.setSelected(mAdapter.isLoved(0));
            mIvDownload.setSelected(mAdapter.isDownload(0));
            mIvPraise.setSelected(mAdapter.isPraise(0));
        }
    }

    @Override
    public void loadMoreData(List<BeautyPhotoInfo> data) {
        mAdapter.addData(data);
        mAdapter.startUpdate(mVpPhoto);
    }

    @Override
    public void loadNoData() {

    }

    /**
     * 底部按钮点击事件: 初始化的时候就已经设置好了每个view的selected状态
     * 当被点击的时候selected状态会反过来, 所以根据!isSelected进行设置
     * @param view
     */
    @OnClick({R.id.iv_favorite, R.id.iv_praise, R.id.iv_share})
    public void onClick(View view) {
        final boolean isSelected = !view.isSelected();
        switch (view.getId()) {
            case R.id.iv_favorite:
                mAdapter.getData(mCurPosition).setLove(isSelected);
                break;
            case R.id.iv_praise:
                mAdapter.getData(mCurPosition).setPraise(isSelected);
                break;
            case R.id.iv_share:
                App.showToast("分享:功能没加(╯-╰)");
                break;
        }
        //加入了按钮的动画处理和数据库的处理
        if (view.getId()!=R.id.iv_share){
            view.setSelected(isSelected);
            AnimationHelper.doHeartBeat(view, 500);
            if (isSelected){
                mPresenter.insert(mAdapter.getData(mCurPosition));
            }else {
                mPresenter.delete(mAdapter.getData(mCurPosition));
            }
        }
        if (mIsFromLoveActivity&&view.getId() == R.id.iv_favorite){
            //不选中即去除收藏
            mIsDelLove[mCurPosition] = !isSelected;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_animate, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        item.setChecked(true);
        switch (item.getItemId()) {
            case R.id.slide_bottom:
                mDragLayout.setAnimatorMode(DragSlopLayout.SLIDE_BOTTOM);
                return true;
            case R.id.slide_left:
                mDragLayout.setAnimatorMode(DragSlopLayout.SLIDE_LEFT);
                return true;
            case R.id.slide_right:
                mDragLayout.setAnimatorMode(DragSlopLayout.SLIDE_RIGHT);
                return true;
            case R.id.slide_fade:
                mDragLayout.setAnimatorMode(DragSlopLayout.FADE);
                return true;
            case R.id.slide_flip_x:
                mDragLayout.setAnimatorMode(DragSlopLayout.FLIP_X);
                return true;
            case R.id.slide_flip_y:
                mDragLayout.setAnimatorMode(DragSlopLayout.FLIP_Y);
                return true;
            case R.id.slide_zoom:
                mDragLayout.setAnimatorMode(DragSlopLayout.ZOOM);
                return true;
            case R.id.slide_zoom_left:
                mDragLayout.setAnimatorMode(DragSlopLayout.ZOOM_LEFT);
                return true;
            case R.id.slide_zoom_right:
                mDragLayout.setAnimatorMode(DragSlopLayout.ZOOM_RIGHT);
                return true;

            case R.id.item_interact:
                mIsInteract = !mIsInteract;
                item.setChecked(mIsInteract);
                mDragLayout.interactWithViewPager(mIsInteract);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void finish() {
        if (mIsFromLoveActivity) {
            Intent intent = new Intent();
            intent.putExtra(CommonConstant.RESULT_KEY, mIsDelLove);
            //把数据传给LoveActivity, 因为用户可能在查看收藏的时候删除了某一个图片的收藏
            //mIsDelLove就是删除的列表数组
            setResult(RESULT_OK, intent);
        }
        super.finish();
        overridePendingTransition(R.anim.hold, R.anim.zoom_out_exit);
    }
}
