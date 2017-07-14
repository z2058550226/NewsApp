package com.suikajy.newsapp.module.video.player;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.orhanobut.logger.Logger;
import com.sackcentury.shinebuttonlib.ShineButton;
import com.suikajy.downloaderlib.model.DownloadStatus;
import com.suikajy.newsapp.R;
import com.suikajy.newsapp.engine.DownloaderWrapper;
import com.suikajy.newsapp.engine.danmaku.DanmakuConverter;
import com.suikajy.newsapp.engine.danmaku.DanmakuLoader;
import com.suikajy.newsapp.engine.danmaku.DanmakuParser;
import com.suikajy.newsapp.injector.components.DaggerVideoPlayerComponent;
import com.suikajy.newsapp.injector.modules.VideoPlayerModule;
import com.suikajy.newsapp.local.table.DanmakuInfo;
import com.suikajy.newsapp.local.table.VideoInfo;
import com.suikajy.newsapp.module.base.activity.BaseActivity;
import com.suikajy.newsapp.utils.DialogHelper;
import com.suikajy.newsapp.utils.SnackBarUtils;
import com.suikajy.newsapp.widget.SimpleButton;
import com.suikajy.newsapp.widget.dialog.ShareBottomDialog;
import com.suikajy.playerview.danmaku.OnDanmakuListener;
import com.suikajy.playerview.media.IjkPlayerView;
import com.suikajy.playerview.utils.SoftInputUtils;

import java.io.InputStream;

import butterknife.BindView;
import butterknife.OnClick;

import static com.suikajy.newsapp.utils.CommonConstant.VIDEO_DATA_KEY;

/**
 * Created by suikajy on 2017/5/2.
 *
 */

public class VideoPlayerActivity extends BaseActivity<IVideoPresenter> implements IVideoView {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.video_player)
    IjkPlayerView mPlayerView;
    @BindView(R.id.et_content)
    EditText mEtContent;
    @BindView(R.id.sb_send)
    SimpleButton mSbSend;
    @BindView(R.id.ll_edit_layout)
    LinearLayout mLlEditLayout;
    @BindView(R.id.iv_video_share)
    ImageView mIvVideoShare;
    @BindView(R.id.iv_video_collect)
    com.sackcentury.shinebuttonlib.ShineButton mIvVideoCollect;
    @BindView(R.id.iv_video_download)
    ImageView mIvVideoDownload;
    @BindView(R.id.ll_operate)
    LinearLayout mLlOperate;

    private VideoInfo mVideoData;

    public static void launch(Context context, VideoInfo data) {
        Intent intent = new Intent(context, VideoPlayerActivity.class);
        intent.putExtra(VIDEO_DATA_KEY, data);
        context.startActivity(intent);
        ((Activity) context).overridePendingTransition(R.anim.slide_bottom_entry, R.anim.hold);
    }

    @Override
    protected int setLayoutResourceID() {
        return R.layout.activity_video_player;
    }

    @Override
    protected void initInjector() {
        mVideoData = getIntent().getParcelableExtra(VIDEO_DATA_KEY);
        DaggerVideoPlayerComponent.builder()
                .applicationComponent(getAppComponent())
                .videoPlayerModule(new VideoPlayerModule(this,mVideoData))
                .build()
                .inject(this);
    }

    @Override
    protected void onInit() {
        initToolBar(mToolbar, true, mVideoData.getTitle());
        mPlayerView.init()
                .setTitle(mVideoData.getTitle())
                .setVideoSource(null, mVideoData.getMp4_url(), mVideoData.getMp4Hd_url(), null, null)
                .enableDanmaku()
                .setDanmakuCustomParser(new DanmakuParser(), DanmakuLoader.instance(), DanmakuConverter.instance())
                .setDanmakuListener(new OnDanmakuListener<DanmakuInfo>() {
                    @Override
                    public boolean isValid() {
                        return true;
                    }

                    @Override
                    public void onDataObtain(DanmakuInfo danmakuInfo) {
                        Logger.w(danmakuInfo.toString());
                        danmakuInfo.setUserName("Long");
                        danmakuInfo.setVid(mVideoData.getVid());
                        mPresenter.addDanmaku(danmakuInfo);
                    }
                });
        mIvVideoCollect.init(this);
        mIvVideoCollect.setShapeResource(R.mipmap.ic_video_collect);
        mIvVideoCollect.setOnCheckStateChangeListener(new ShineButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(View view, boolean checked) {
                // 这里直接点击就处理，通常是需要和服务器交互返回成功才处理的，但是这个库内部直接受理了点击事件，没法方便地
                // 来控制它，需要改代码
                mVideoData.setCollect(checked);
                if (mVideoData.isCollect()) {
                    mPresenter.insert(mVideoData);
                } else {
                    mPresenter.delete(mVideoData);
                }
            }
        });
        Glide.with(this).load(mVideoData.getCover()).fitCenter().into(mPlayerView.mPlayerThumb);
        mEtContent.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    mPlayerView.editVideo();
                }
                mLlOperate.setVisibility(hasFocus ? View.GONE : View.VISIBLE);
            }
        });
    }

    @Override
    public void loadData(VideoInfo data) {
        mVideoData = data;
        mIvVideoCollect.setChecked(data.isCollect());
        mIvVideoDownload.setSelected(data.getDownloadStatus() != DownloadStatus.NORMAL);
    }

    @Override
    public void loadDanmakuData(InputStream inputStream) {
        mPlayerView.setDanmakuSource(inputStream);
    }

    @Override
    protected void updateViews(boolean isRefresh) {
        mPresenter.getData(isRefresh);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPlayerView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPlayerView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPlayerView.onDestroy();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mPlayerView.configurationChanged(newConfig);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (mPlayerView.handleVolumeKey(keyCode)) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        if (mPlayerView.onBackPressed()) {
            return;
        }
        super.onBackPressed();
    }

    @OnClick({R.id.sb_send, R.id.iv_video_share, R.id.iv_video_download})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.sb_send:
                mPlayerView.sendDanmaku(mEtContent.getText().toString(), false);
                mEtContent.setText("");
                _closeSoftInput();
                break;
            case R.id.iv_video_share:
                new ShareBottomDialog(this).show();
                break;
            case R.id.iv_video_download:
                if (view.isSelected()) {
                    DialogHelper.checkDialog(this, mVideoData);
                } else {
                    DialogHelper.downloadDialog(this, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            DownloaderWrapper.start(mVideoData);
                            mIvVideoDownload.setSelected(true);
                            SnackBarUtils.showDownloadSnackbar(VideoPlayerActivity.this, "任务正在下载", true);
                        }
                    });
                }
                break;
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        View view = getCurrentFocus();
        if (_isHideSoftInput(view, (int) ev.getX(), (int) ev.getY())) {
            _closeSoftInput();
            return true;
        }
        return super.dispatchTouchEvent(ev);
    }

    private void _closeSoftInput() {
        mEtContent.clearFocus();
        SoftInputUtils.closeSoftInput(this);
        mPlayerView.recoverFromEditVideo();
    }

    private boolean _isHideSoftInput(View view, int x, int y) {
        if (view == null || !(view instanceof EditText) || !mEtContent.isFocused()) {
            return false;
        }
        return x < mLlEditLayout.getLeft() ||
                x > mLlEditLayout.getRight() ||
                y > mLlEditLayout.getBottom() ||
                y < mLlEditLayout.getTop();
    }
}
