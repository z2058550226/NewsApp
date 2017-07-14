package com.suikajy.newsapp.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.suikajy.newsapp.R;
import com.suikajy.newsapp.local.table.VideoInfo;
import com.suikajy.newsapp.module.video.player.VideoPlayerActivity;
import com.suikajy.newsapp.utils.DefIconFactory;
import com.suikajy.newsapp.utils.ImageLoader;
import com.suikajy.recyclerviewhelper.adapter.BaseQuickAdapter;
import com.suikajy.recyclerviewhelper.adapter.BaseViewHolder;

import java.util.List;

/**
 * Created by suikajy on 2017/5/2.
 *
 */

public class VideoListAdapter extends BaseQuickAdapter<VideoInfo> {
    public VideoListAdapter(Context context) {
        super(context);
    }

    public VideoListAdapter(Context context, List<VideoInfo> data) {
        super(context, data);
    }

    @Override
    protected int attachLayoutRes() {
        return R.layout.adapter_video_list;
    }

    @Override
    protected void convert(final BaseViewHolder holder, final VideoInfo item) {
        final ImageView ivPhoto = holder.getView(R.id.iv_photo);
        ImageLoader.loadFitCenter(mContext, item.getCover(), ivPhoto, DefIconFactory.provideIcon());
        holder.setText(R.id.tv_title, item.getTitle());
        holder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VideoPlayerActivity.launch(mContext, item);
            }
        });
    }
}
