package com.suikajy.newsapp.adapter;

import android.content.Context;

import com.suikajy.newsapp.R;
import com.suikajy.newsapp.local.table.NewsTypeInfo;
import com.suikajy.recyclerviewhelper.adapter.BaseQuickAdapter;
import com.suikajy.recyclerviewhelper.adapter.BaseViewHolder;

import java.util.List;

/**
 * Created by suikajy on 2017/4/30.
 */

public class ManageAdapter extends BaseQuickAdapter<NewsTypeInfo> {

    public ManageAdapter(Context context) {
        super(context);
    }

    public ManageAdapter(Context context, List<NewsTypeInfo> data) {
        super(context, data);
    }

    @Override
    protected int attachLayoutRes() {
        return R.layout.adapter_manage;
    }

    @Override
    protected void convert(BaseViewHolder holder, NewsTypeInfo item) {
        holder.setText(R.id.tv_channel_name, item.getName());
    }

}
