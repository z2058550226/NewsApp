package com.suikajy.newsapp.module.manage.love.video;

import com.suikajy.newsapp.R;
import com.suikajy.newsapp.local.table.VideoInfo;
import com.suikajy.newsapp.module.base.fragment.BaseFragment;
import com.suikajy.newsapp.module.base.presenter.ILocalPresenter;
import com.suikajy.newsapp.module.base.view.ILocalView;

import java.util.List;

/**
 * Created by suikajy on 2017/4/18.
 */

public class LoveVideoFragment extends BaseFragment<ILocalPresenter> implements ILocalView<VideoInfo> {
    @Override
    protected void updateViews(boolean isRefresh) {

    }

    @Override
    protected void initViews() {

    }

    @Override
    protected int attachLayoutRes() {
        return R.layout.fragment_toy;
    }

    @Override
    protected void initInjector() {

    }

    @Override
    public void loadData(List<VideoInfo> dataList) {

    }

    @Override
    public void noData() {

    }
}
