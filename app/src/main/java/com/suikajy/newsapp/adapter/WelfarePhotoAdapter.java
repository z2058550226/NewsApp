package com.suikajy.newsapp.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.suikajy.newsapp.App;
import com.suikajy.newsapp.R;
import com.suikajy.newsapp.api.bean.WelfarePhotoInfo;
import com.suikajy.newsapp.utils.DefIconFactory;
import com.suikajy.newsapp.utils.ImageLoader;
import com.suikajy.newsapp.utils.StringUtils;
import com.suikajy.recyclerviewhelper.adapter.BaseQuickAdapter;
import com.suikajy.recyclerviewhelper.adapter.BaseViewHolder;

import java.util.List;

/**
 * Created by suikajy on 2017/4/16.
 *
 */

public class WelfarePhotoAdapter extends BaseQuickAdapter<WelfarePhotoInfo>{
    // 图片的宽度
    private int mPhotoWidth;


    public WelfarePhotoAdapter(Context context) {
        super(context);
        int widthPixels = context.getResources().getDisplayMetrics().widthPixels;
        int marginPixels = context.getResources().getDimensionPixelOffset(R.dimen.photo_margin_width);
        mPhotoWidth = widthPixels / 2 - marginPixels;
    }

    public WelfarePhotoAdapter(Context context, List<WelfarePhotoInfo> data) {
        super(context, data);
    }

    @Override
    protected int attachLayoutRes() {
        return R.layout.adapter_welfare_photo;
    }

    @Override
    protected void convert(final BaseViewHolder holder, final WelfarePhotoInfo item) {
        final ImageView ivPhoto = holder.getView(R.id.iv_photo);
        int photoHeight = StringUtils.calcPhotoHeight(item.getPixel(), mPhotoWidth);
        // 返回的数据有像素分辨率，根据这个来缩放图片大小
        final ViewGroup.LayoutParams params = ivPhoto.getLayoutParams();
        params.width = mPhotoWidth;
        params.height = photoHeight;
        ivPhoto.setLayoutParams(params);
        ImageLoader.loadFitCenter(mContext, item.getUrl(), ivPhoto, DefIconFactory.provideIcon());
        holder.setText(R.id.tv_title, item.getCreatedAt());

        holder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                App.showToast("这里没有做大图显示处理，请参考美图界面");
            }
        });
    }
}
