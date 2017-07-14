package com.suikajy.newsapp.module.photo.welfare;

import android.support.v7.widget.RecyclerView;

import com.suikajy.newsapp.R;
import com.suikajy.newsapp.adapter.SlideInBottomAdapter;
import com.suikajy.newsapp.api.bean.WelfarePhotoInfo;
import com.suikajy.newsapp.injector.components.DaggerWelfarePhotoComponent;
import com.suikajy.newsapp.injector.modules.WelfarePhotoModule;
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

public class WelfareListFragment extends BaseFragment<IBasePresenter> implements ILoadDataView<List<WelfarePhotoInfo>> {


    @BindView(R.id.rv_photo_list)
    RecyclerView mRvPhotoList;

    @Inject
    BaseQuickAdapter mAdapter;

    @Override
    protected void updateViews(boolean isRefresh) {
        mPresenter.getData(isRefresh);
    }

    @Override
    protected void initViews() {
        SlideInBottomAdapter slideAdapter = new SlideInBottomAdapter(mAdapter);
        RecyclerViewHelper.initRecyclerViewSV(mContext, mRvPhotoList, slideAdapter, 2);
        mAdapter.setRequestDataListener(new OnRequestDataListener() {
            @Override
            public void onLoadMore() {
                mPresenter.getMoreData();
            }
        });
    }

    @Override
    protected int attachLayoutRes() {
        return R.layout.fragment_photo_list;
    }

    @Override
    protected void initInjector() {
        DaggerWelfarePhotoComponent.builder()
                .applicationComponent(getAppComponent())
                .welfarePhotoModule(new WelfarePhotoModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void loadData(List<WelfarePhotoInfo> data) {
        mAdapter.updateItems(data);
    }

    @Override
    public void loadMoreData(List<WelfarePhotoInfo> data) {
        mAdapter.loadComplete();
        mAdapter.updateItems(data);
    }

    @Override
    public void loadNoData() {
        mAdapter.loadAbnormal();
    }
}
