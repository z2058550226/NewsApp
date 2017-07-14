package com.suikajy.newsapp.module.manage.download.cache;

import android.view.View;
import android.widget.TextView;

import com.suikajy.downloaderlib.entity.FileInfo;
import com.suikajy.newsapp.R;
import com.suikajy.newsapp.injector.components.DaggerVideoCacheComponent;
import com.suikajy.newsapp.injector.modules.VideoCacheModule;
import com.suikajy.newsapp.local.table.VideoInfo;
import com.suikajy.newsapp.module.base.fragment.BaseVideoDLFragment;
import com.suikajy.newsapp.module.base.presenter.IRxBusPresenter;
import com.suikajy.newsapp.module.base.view.ILocalView;
import com.suikajy.newsapp.module.manage.download.DownloadActivity;
import com.suikajy.recyclerviewhelper.helper.RecyclerViewHelper;
import com.suikajy.recyclerviewhelper.listener.OnRemoveDataListener;

import java.util.List;

import butterknife.BindView;
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;
import rx.functions.Action1;

/**
 * Created by long on 2016/12/15.
 * video缓存列表
 */
public class VideoCacheFragment extends BaseVideoDLFragment<IRxBusPresenter> implements ILocalView<VideoInfo> {

    @BindView(R.id.default_bg)
    TextView mDefaultBg;

    @Override
    protected int attachLayoutRes() {
        return R.layout.fragment_download;
    }

    @Override
    protected void initInjector() {
        DaggerVideoCacheComponent.builder()
                .applicationComponent(getAppComponent())
                .videoCacheModule(new VideoCacheModule(this))
                .build()
                .inject(this);
    }

    @Override
    protected void initViews() {
        RecyclerViewHelper.initRecyclerViewV(mContext, mRvVideoList, mAdapter);
        mRvVideoList.setItemAnimator(new SlideInLeftAnimator());
        mAdapter.setRemoveDataListener(new OnRemoveDataListener() {
            @Override
            public void onRemove(int position) {
                if (mAdapter.getItemCount() <= 1 && mDefaultBg.getVisibility() == View.GONE) {
                    mDefaultBg.setVisibility(View.VISIBLE);
                    ((DownloadActivity)getActivity()).enableEditMode(false);
                }
            }
        });
        initItemLongClick();
        mPresenter.registerRxBus(FileInfo.class, new Action1<FileInfo>() {
            @Override
            public void call(FileInfo fileInfo) {
                mAdapter.updateDownload(fileInfo);
            }
        });
    }

    @Override
    protected void updateViews(boolean isRefresh) {
        mPresenter.getData(isRefresh);
    }

    @Override
    public void loadData(List<VideoInfo> dataList) {
        if (mDefaultBg.getVisibility() == View.VISIBLE) {
            mDefaultBg.setVisibility(View.GONE);
        }
        mAdapter.updateItems(dataList);
    }

    @Override
    public void noData() {
        mDefaultBg.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.unregisterRxBus();
    }
}
