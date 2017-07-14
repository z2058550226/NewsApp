package com.suikajy.newsapp.module.photo.beauty;

import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.suikajy.newsapp.R;
import com.suikajy.newsapp.adapter.SlideInBottomAdapter;
import com.suikajy.newsapp.injector.components.DaggerBeautyListComponent;
import com.suikajy.newsapp.injector.modules.BeautyListModule;
import com.suikajy.newsapp.local.table.BeautyPhotoInfo;
import com.suikajy.newsapp.module.base.fragment.BaseFragment;
import com.suikajy.newsapp.module.base.presenter.IBasePresenter;
import com.suikajy.newsapp.module.base.view.ILoadDataView;
import com.suikajy.recyclerviewhelper.adapter.BaseQuickAdapter;
import com.suikajy.recyclerviewhelper.helper.RecyclerViewHelper;
import com.suikajy.recyclerviewhelper.listener.OnRequestDataListener;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * Created by suikajy on 2017/4/13.
 */

public class BeautyListFragment extends BaseFragment<IBasePresenter> implements ILoadDataView<List<BeautyPhotoInfo>> {

    @BindView(R.id.rv_photo_list)
    RecyclerView mRvPhotoList;
    @BindView(R.id.iv_transition_photo)
    ImageView mIvTransitionPhoto;

    @Inject
    BaseQuickAdapter mAdapter;

    @Override
    protected int attachLayoutRes() {
        return R.layout.fragment_photo_list;
    }

    @Override
    protected void initInjector() {
        DaggerBeautyListComponent.builder()
                .applicationComponent(getAppComponent())
                .beautyListModule(new BeautyListModule(this))
                .build()
                .inject(this);
    }

    @Override
    protected void initViews() {
        SlideInBottomAdapter slideAdapter = new SlideInBottomAdapter(mAdapter);
        RecyclerViewHelper.initRecyclerViewSV(mContext, mRvPhotoList, mAdapter, 2);
        mAdapter.setRequestDataListener(new OnRequestDataListener() {
            @Override
            public void onLoadMore() {
                mPresenter.getMoreData();
            }
        });
    }

    @Override
    protected void updateViews(boolean isRefresh) {
        mPresenter.getData(isRefresh);
    }

    @Override
    public void loadData(List<BeautyPhotoInfo> data) {
        mAdapter.updateItems(data);
    }

    @Override
    public void loadMoreData(List<BeautyPhotoInfo> data) {
        mAdapter.loadComplete();
        mAdapter.addItems(data);
    }

    @Override
    public void loadNoData() {
        mAdapter.loadAbnormal();
    }
}
