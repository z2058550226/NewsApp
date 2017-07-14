package com.suikajy.newsapp.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.flyco.labelview.LabelView;
import com.suikajy.core.utils.ListUtils;
import com.suikajy.newsapp.R;
import com.suikajy.newsapp.adapter.item.NewsMultiItem;
import com.suikajy.newsapp.api.bean.NewsInfo;
import com.suikajy.newsapp.module.news.article.NewsArticleActivity;
import com.suikajy.newsapp.module.news.photoset.PhotoSetActivity;
import com.suikajy.newsapp.module.news.special.SpecialActivity;
import com.suikajy.newsapp.utils.DefIconFactory;
import com.suikajy.newsapp.utils.ImageLoader;
import com.suikajy.newsapp.utils.NewsUtils;
import com.suikajy.newsapp.utils.StringUtils;
import com.suikajy.newsapp.widget.RippleView;
import com.suikajy.recyclerviewhelper.adapter.BaseMultiItemQuickAdapter;
import com.suikajy.recyclerviewhelper.adapter.BaseViewHolder;

import java.util.List;

/**
 * Created by suikajy on 2017/4/9.
 * 新闻列表适配器, 继承多布局快速适配器
 * 目前使用界面为NewsList
 */

public class NewsMultiListAdapter extends BaseMultiItemQuickAdapter<NewsMultiItem> {

    public NewsMultiListAdapter(Context context, List<NewsMultiItem> data) {
        super(context, data);
    }

    public NewsMultiListAdapter(Context context) {
        super(context);
    }

    @Override
    protected void attachItemType() {
        addItemType(NewsMultiItem.ITEM_TYPE_NORMAL, R.layout.adapter_news_list);
        addItemType(NewsMultiItem.ITEM_TYPE_PHOTO_SET, R.layout.adapter_news_photo_set);
    }

    @Override
    protected void convert(BaseViewHolder holder, NewsMultiItem item) {
        switch (item.getItemType()) {
            case NewsMultiItem.ITEM_TYPE_NORMAL:
                _handleNewsNormal(holder, item.getNewsBean());
                break;
            case NewsMultiItem.ITEM_TYPE_PHOTO_SET:
                _handleNewsPhotoSet(holder, item.getNewsBean());
                break;
        }
    }

    /**
     * 处理正常的新闻
     *
     * @param holder
     * @param item
     */
    private void _handleNewsNormal(final BaseViewHolder holder, final NewsInfo item) {
        ImageView newsIcon = holder.getView(R.id.iv_icon);
        ImageLoader.loadCenterCrop(mContext, item.getImgsrc(), newsIcon, DefIconFactory.provideIcon());
        holder.setText(R.id.tv_title, item.getTitle())
                .setText(R.id.tv_source, item.getSource())
                .setText(R.id.tv_time, item.getPtime());
        //设置标签
        if (NewsUtils.isNewsSpecial(item.getSkipType())) {
            LabelView labelView = holder.getView(R.id.label_view);
            labelView.setVisibility(View.VISIBLE);
            labelView.setText("专题");
        } else {
            holder.setVisible(R.id.label_view, false);
        }
        //波纹效果
        RippleView rippleLayout = holder.getView(R.id.item_ripple);
        rippleLayout.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {
                if (NewsUtils.isNewsSpecial(item.getSkipType())) {
                    SpecialActivity.launch(mContext, item.getSpecialID());
                } else {
                    // 旧的实现方式和网易的比较相近，感兴趣的可以切换看看
                    //NewsDetailActivity.launch(mContext, item.getPostid());//旧版
                    NewsArticleActivity.launch(mContext, item.getPostid());//自定义版
                }
            }
        });

    }

    private void _handleNewsPhotoSet(BaseViewHolder holder, final NewsInfo item) {
        ImageView[] newsPhoto = new ImageView[3];
        newsPhoto[0] = holder.getView(R.id.iv_icon_1);
        newsPhoto[1] = holder.getView(R.id.iv_icon_2);
        newsPhoto[2] = holder.getView(R.id.iv_icon_3);

        holder.setVisible(R.id.iv_icon_2, false).setVisible(R.id.iv_icon_3, false);
        ImageLoader.loadCenterCrop(mContext, item.getImgsrc(), newsPhoto[0], DefIconFactory.provideIcon());
        if (!ListUtils.isEmpty(item.getImgextra())) {
            for (int i = 0; i < Math.min(2, item.getImgextra().size()); i++) {
                newsPhoto[i + 1].setVisibility(View.VISIBLE);
                ImageLoader.loadCenterCrop(mContext, item.getImgextra().get(i).getImgsrc(), newsPhoto[i + 1], DefIconFactory.provideIcon());
            }
        }
        holder.setText(R.id.tv_title, item.getTitle())
                .setText(R.id.tv_source, StringUtils.clipNewsSource(item.getSource()))
                .setText(R.id.tv_time, item.getPtime());

        // 波纹效果
        RippleView rippleLayout = holder.getView(R.id.item_ripple);
        rippleLayout.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {
                PhotoSetActivity.launch(mContext, item.getPhotosetID());
            }
        });

    }
}
