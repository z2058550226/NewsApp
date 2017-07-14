package com.suikajy.newsapp.module.photo.news;

import android.support.v7.widget.RecyclerView;

import com.suikajy.newsapp.R;
import com.suikajy.newsapp.api.bean.PhotoInfo;
import com.suikajy.newsapp.injector.components.DaggerPhotoNewsComponent;
import com.suikajy.newsapp.injector.modules.PhotoNewsModule;
import com.suikajy.newsapp.module.base.fragment.BaseFragment;
import com.suikajy.newsapp.module.base.presenter.IBasePresenter;
import com.suikajy.newsapp.module.base.view.ILoadDataView;
import com.suikajy.recyclerviewhelper.adapter.BaseQuickAdapter;
import com.suikajy.recyclerviewhelper.helper.RecyclerViewHelper;
import com.suikajy.recyclerviewhelper.listener.OnRequestDataListener;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import jp.wasabeef.recyclerview.adapters.SlideInBottomAnimationAdapter;

/**
 * Created by suikajy on 2017/4/13.
 *
 */

public class PhotoNewsFragment extends BaseFragment<IBasePresenter> implements ILoadDataView<List<PhotoInfo>> {


    @BindView(R.id.rv_photo_list)
    RecyclerView mRvPhotoList;

    @Inject
    BaseQuickAdapter mAdapter;

    @Override
    protected int attachLayoutRes() {
        return R.layout.fragment_photo_list;
    }

    @Override
    protected void initInjector() {
        DaggerPhotoNewsComponent.builder()
                .applicationComponent(getAppComponent())
                .photoNewsModule(new PhotoNewsModule(this))
                .build()
                .inject(this);
    }

    @Override
    protected void initViews() {
        RecyclerViewHelper.initRecyclerViewV(mContext,mRvPhotoList,new SlideInBottomAnimationAdapter(mAdapter));
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
    public void loadData(List<PhotoInfo> photoList) {
        mAdapter.updateItems(photoList);
    }

    @Override
    public void loadMoreData(List<PhotoInfo> photoList) {
        mAdapter.addItems(photoList);
    }

    @Override
    public void loadNoData() {
        mAdapter.noMoreData();
    }
}
