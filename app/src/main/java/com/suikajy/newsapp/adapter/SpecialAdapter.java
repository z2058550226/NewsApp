package com.suikajy.newsapp.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;

import com.flyco.labelview.LabelView;
import com.suikajy.newsapp.R;
import com.suikajy.newsapp.adapter.item.SpecialItem;
import com.suikajy.newsapp.module.news.article.NewsArticleActivity;
import com.suikajy.newsapp.module.news.photoset.PhotoSetActivity;
import com.suikajy.newsapp.module.news.special.SpecialActivity;
import com.suikajy.newsapp.utils.DefIconFactory;
import com.suikajy.newsapp.utils.ImageLoader;
import com.suikajy.newsapp.utils.NewsUtils;
import com.suikajy.newsapp.utils.StringUtils;
import com.suikajy.recyclerviewhelper.adapter.BaseSectionQuickAdapter;
import com.suikajy.recyclerviewhelper.adapter.BaseViewHolder;

import java.util.List;

/**
 * Created by suikajy on 2017/4/27.
 */

public class SpecialAdapter extends BaseSectionQuickAdapter<SpecialItem> {
    public SpecialAdapter(Context context, List<SpecialItem> data) {
        super(context, data);
    }

    public SpecialAdapter(Context context) {
        super(context);
    }

    @Override
    protected int attachLayoutRes() {
        return R.layout.adapter_news_list;
    }

    @Override
    protected int attachHeadLayoutRes() {
        return R.layout.adapter_special_head;
    }

    @Override
    protected void convertHead(BaseViewHolder holder, SpecialItem item) {
        holder.setText(R.id.tv_head, item.header);
    }

    @Override
    protected void convert(BaseViewHolder holder, final SpecialItem item) {
        ImageView newsIcon = holder.getView(R.id.iv_icon);
        ImageLoader.loadCenterCrop(mContext, item.t.getImgsrc(), newsIcon, DefIconFactory.provideIcon());
        holder.setText(R.id.tv_title, item.t.getTitle())
                .setText(R.id.tv_source, StringUtils.clipNewsSource(item.t.getSource()))
                .setText(R.id.tv_time, item.t.getPtime());

        if (NewsUtils.isNewsSpecial(item.t.getSkipType())) {
            LabelView labelView = holder.getView(R.id.label_view);
            labelView.setVisibility(View.VISIBLE);
            labelView.setBgColor(ContextCompat.getColor(mContext, R.color.item_special_bg));
            labelView.setText("专题");
        } else if (NewsUtils.isNewsPhotoSet(item.t.getSkipType())) {
            LabelView labelView = holder.getView(R.id.label_view);
            labelView.setVisibility(View.VISIBLE);
            labelView.setBgColor(ContextCompat.getColor(mContext, R.color.item_photo_set_bg));
            labelView.setText("图集");
        } else {
            holder.setVisible(R.id.label_view, false);
        }

        holder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NewsUtils.isNewsSpecial(item.t.getSkipType())) {
                    SpecialActivity.launch(mContext, item.t.getSpecialID());
                } else if (NewsUtils.isNewsPhotoSet(item.t.getSkipType())) {
                    PhotoSetActivity.launch(mContext, item.t.getPhotosetID());
                } else {
//                    NewsDetailActivity.launch(mContext, item.t.getPostid());
                    NewsArticleActivity.launch(mContext, item.t.getPostid());
                }
            }
        });
    }
}
