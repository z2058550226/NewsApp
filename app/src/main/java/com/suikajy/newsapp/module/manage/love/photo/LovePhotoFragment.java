package com.suikajy.newsapp.module.manage.love.photo;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.suikajy.newsapp.R;
import com.suikajy.newsapp.adapter.SlideInBottomAdapter;
import com.suikajy.newsapp.injector.components.DaggerLovePhotoComponent;
import com.suikajy.newsapp.injector.modules.LovePhotoModule;
import com.suikajy.newsapp.local.table.BeautyPhotoInfo;
import com.suikajy.newsapp.module.base.fragment.BaseFragment;
import com.suikajy.newsapp.module.base.presenter.ILocalPresenter;
import com.suikajy.newsapp.module.base.view.ILocalView;
import com.suikajy.newsapp.utils.CommonConstant;
import com.suikajy.newsapp.utils.DialogHelper;
import com.suikajy.recyclerviewhelper.adapter.BaseQuickAdapter;
import com.suikajy.recyclerviewhelper.helper.RecyclerViewHelper;
import com.suikajy.recyclerviewhelper.listener.OnRecyclerViewItemLongClickListener;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import jp.wasabeef.recyclerview.animators.FlipInLeftYAnimator;

import static android.app.Activity.RESULT_OK;

/**
 * Created by suikajy on 2017/4/18.
 * 图片收藏fragment
 */

public class LovePhotoFragment extends BaseFragment<ILocalPresenter> implements ILocalView<BeautyPhotoInfo> {


    @BindView(R.id.rv_love_list)
    RecyclerView mRvPhotoList;
    @BindView(R.id.default_bg)
    TextView mDefaultBg;

    @Inject
    BaseQuickAdapter mAdapter;

    @Override
    protected void updateViews(boolean isRefresh) {
        mPresenter.getData(isRefresh);
    }

    /**
     * 设置了低端滑动动画, item的删除动画, 长按删除功能
     */
    @Override
    protected void initViews() {
        SlideInBottomAdapter slideAdapter = new SlideInBottomAdapter(mAdapter);
        RecyclerViewHelper.initRecyclerViewSV(mContext, mRvPhotoList, slideAdapter, 2);
        mRvPhotoList.setItemAnimator(new FlipInLeftYAnimator());
        mAdapter.setOnItemLongClickListener(new OnRecyclerViewItemLongClickListener() {
            @Override
            public boolean onItemLongClick(View view, final int position) {
                DialogHelper.deleteDialog(mContext, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mPresenter.delete(mAdapter.getItem(position));
                        mAdapter.removeItem(position);
                    }
                });
                return true;
            }
        });
    }

    @Override
    protected int attachLayoutRes() {
        return R.layout.fragment_love_list;
    }

    @Override
    protected void initInjector() {
        DaggerLovePhotoComponent.builder()
                .applicationComponent(getAppComponent())
                .lovePhotoModule(new LovePhotoModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void loadData(List<BeautyPhotoInfo> dataList) {
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CommonConstant.REQUEST_CODE && resultCode == RESULT_OK) {
            final boolean[] delLove = data.getBooleanArrayExtra(CommonConstant.RESULT_KEY);
            // 延迟 500MS 做删除操作，不然退回来看不到动画效果
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    for (int i = delLove.length - 1; i >= 0; i--) {
                        if (delLove[i]) {
                            mAdapter.removeItem(i);
                        }
                    }
                }
            }, 500);
        }
    }

}
