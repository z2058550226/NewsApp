package com.suikajy.newsapp.utils;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.suikajy.core.utils.NetUtil;
import com.suikajy.core.utils.PreferencesUtils;
import com.suikajy.newsapp.module.manage.setting.SettingsFragment;

import java.io.File;
import java.util.concurrent.ExecutionException;

/**
 * Created by suikajy on 2017/4/9.
 */

public class ImageLoader {

    private ImageLoader() {
        throw new RuntimeException("ImageLoader cannot be initialized");
    }

    public static void loadFit(Context context, String url, ImageView view, int defaultResId) {
        if (_isShowImageAlways(context) || NetUtil.isWifiConnected(context)) {
            view.setScaleType(ImageView.ScaleType.FIT_XY);
            Glide.with(context).load(url).fitCenter().dontAnimate().placeholder(defaultResId).into(view);
        } else {
            view.setImageResource(defaultResId);
        }
    }

    public static void loadCenterCrop(Context context, String url, ImageView view, int defaultResId){
        if (_isShowImageAlways(context) || NetUtil.isWifiConnected(context)) {
            Glide.with(context).load(url).centerCrop().dontAnimate().placeholder(defaultResId).into(view);
        } else {
            view.setImageResource(defaultResId);
        }
    }

    public static void loadFitCenter(Context context, String url, ImageView view, int defaultResId) {
        if (_isShowImageAlways(context) || NetUtil.isWifiConnected(context)) {
            Glide.with(context).load(url).fitCenter().dontAnimate().placeholder(defaultResId).into(view);
        } else {
            view.setImageResource(defaultResId);
        }
    }

    /**
     * 带监听处理
     *
     * @param context
     * @param url
     * @param view
     * @param listener
     */
    public static void loadFitCenter(Context context, String url, ImageView view, RequestListener listener) {
        Glide.with(context).load(url).fitCenter().dontAnimate().listener(listener).into(view);
    }

    public static void loadCenterCrop(Context context, String url, ImageView view, RequestListener listener) {
        Glide.with(context).load(url).centerCrop().dontAnimate().listener(listener).into(view);
    }

    /**
     * 设置图片大小处理
     *
     * @param context
     * @param url
     * @param view
     * @param defaultResId
     * @param width
     * @param height
     */
    public static void loadFitOverride(Context context, String url, ImageView view, int defaultResId,
                                       int width, int height) {
        if (_isShowImageAlways(context) || NetUtil.isWifiConnected(context)) {
            Glide.with(context).load(url).fitCenter().dontAnimate().override(width, height)
                    .placeholder(defaultResId).into(view);
        } else {
            view.setImageResource(defaultResId);
        }
    }

    /**
     * 计算图片分辨率
     *
     * @param context
     * @param url
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public static String calePhotoSize(Context context, String url) throws ExecutionException, InterruptedException {
        File file = Glide.with(context).load(url)
                .downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).get();
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(file.getAbsolutePath(), options);
        return options.outWidth + "*" + options.outHeight;
    }

    /**
     * 是否非Wifi下不加载图片
     *
     * @param context
     * @return
     */
    public static boolean _isShowImageAlways(Context context) {
        // TODO: 2017/4/9 app设置页面的功能
        return PreferencesUtils.getBoolean(context, SettingsFragment.NO_IMAGE_KEY, false);
    }
}
