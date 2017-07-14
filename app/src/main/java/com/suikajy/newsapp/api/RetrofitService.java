package com.suikajy.newsapp.api;

import android.support.annotation.NonNull;

import com.orhanobut.logger.Logger;
import com.suikajy.core.utils.NetUtil;
import com.suikajy.newsapp.App;
import com.suikajy.newsapp.api.bean.NewsDetailInfo;
import com.suikajy.newsapp.api.bean.NewsInfo;
import com.suikajy.newsapp.api.bean.PhotoInfo;
import com.suikajy.newsapp.api.bean.PhotoSetInfo;
import com.suikajy.newsapp.api.bean.SpecialInfo;
import com.suikajy.newsapp.api.bean.WelfarePhotoInfo;
import com.suikajy.newsapp.api.bean.WelfarePhotoList;
import com.suikajy.newsapp.local.table.BeautyPhotoInfo;
import com.suikajy.newsapp.local.table.VideoInfo;
import com.suikajy.newsapp.utils.StringUtils;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by suikajy on 2017/4/11.
 */

public class RetrofitService {

    private static final String HEAD_LINE_NEWS = "T1348647909107";
    //设缓存有效期为1天
    static final long CACHE_STALE_SEC = 60 * 60 * 24 * 1;
    //查询缓存的Cache-Control设置，为if-only-cache时只查询缓存而不会请求服务器，max-stale可以配合设置缓存失效时间
    private static final String CACHE_CONTROL_CACHE = "only-if-cached, max-stale=" + CACHE_STALE_SEC;
    //查询网络的Cache-Control设置
    //(假如请求了服务器并在a时刻返回响应结果，则在max-age规定的秒数内，浏览器将不会发送对应的请求到服务器，数据由缓存直接返回)
    static final String CACHE_CONTROL_NETWORK = "Cache-Control: public, max-age=3600";
    // 避免出现 HTTP 403 Forbidden，参考：http://stackoverflow.com/questions/13670692/403-forbidden-with-java-but-not-web-browser
    static final String AVOID_HTTP403_FORBIDDEN = "User-Agent: Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11";

    private static final String NEWS_HOST = "http://c.3g.163.com/";
    private static final String WELFARE_HOST = "http://gank.io/";

    private static INewsApi sNewsService;
    private static IWelfareApi sWelfareService;

    // 递增页码
    private static final int INCREASE_PAGE = 20;

    private RetrofitService() {
        throw new AssertionError();
    }

    public static void init() {
        //制定缓存路径, 缓存大小100mb
        Cache cache = new Cache(new File(App.getAppComponent().getContext().getCacheDir(), "HttpCache"),
                1024 * 1024 * 100);
        OkHttpClient okHttpClient = new OkHttpClient.Builder().cache(cache)
                .retryOnConnectionFailure(true)
                .addInterceptor(sLoggingInterceptor)
                .addInterceptor(sRewriteCacheControlInterceptor)
                .addNetworkInterceptor(sRewriteCacheControlInterceptor)
                .connectTimeout(10, TimeUnit.SECONDS)
                .build();
        //创建新闻的网络获取接口代理类
        Retrofit retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(NEWS_HOST)
                .build();
        sNewsService = retrofit.create(INewsApi.class);
        //获取福利的网络接口代理类
        retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(WELFARE_HOST)
                .build();
        sWelfareService = retrofit.create(IWelfareApi.class);
    }


    //region Interceptor
    /**
     * 云端响应头拦截器，用来配置缓存策略
     *
     * Dangerous interceptor that rewrites the server's cache-control header.
     */
    private static final Interceptor sRewriteCacheControlInterceptor = new Interceptor() {

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            if (!NetUtil.isNetworkAvailable(App.getAppComponent().getContext())) {
                request = request.newBuilder().cacheControl(CacheControl.FORCE_CACHE).build();
                Logger.e("no network");
            }
            Response originalResponse = chain.proceed(request);

            if (NetUtil.isNetworkAvailable(App.getAppComponent().getContext())) {
                //有网的时候读接口上的@Headers里的配置，你可以在这里进行统一的设置
                String cacheControl = request.cacheControl().toString();
                return originalResponse.newBuilder()
                        .header("Cache-Control", cacheControl)
                        .removeHeader("Pragma")
                        .build();
            } else {
                return originalResponse.newBuilder()
                        .header("Cache-Control", "public, " + CACHE_CONTROL_CACHE)
                        .removeHeader("Pragma")
                        .build();
            }
        }
    };

    /**
     * 打印返回的json数据拦截器
     */
    private static final Interceptor sLoggingInterceptor = new Interceptor() {

        @Override
        public Response intercept(Chain chain) throws IOException {
            final Request request = chain.request();
            Buffer requestBuffer = new Buffer();
            if (request.body() != null) {
                request.body().writeTo(requestBuffer);
            } else {
                Logger.d("LogTAG", "request.body() == null");
            }
            //打印url信息
            Logger.w(request.url() + (request.body() != null ? "?" + _parseParams(request.body(), requestBuffer) : ""));
            final Response response = chain.proceed(request);

            return response;
        }
    };

    @NonNull
    private static String _parseParams(RequestBody body, Buffer requestBuffer) throws UnsupportedEncodingException {
        if (body.contentType() != null && !body.contentType().toString().contains("multipart")) {
            return URLDecoder.decode(requestBuffer.readUtf8(), "UTF-8");
        }
        return "null";
    }
    //endregion

    //region API

    /**
     * 获取新闻列表
     *
     * @return
     */
    public static Observable<NewsInfo> getNewsList(String newsId, int page) {
        String type;
        if (newsId.equals(HEAD_LINE_NEWS)) {
            type = "headline";
        } else {
            type = "list";
        }
        return sNewsService.getNewsList(type, newsId, page * INCREASE_PAGE)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(_flatMapNews(newsId));
    }

    /**
     * 获取专题数据
     * @param specialId
     * @return
     */
    public static Observable<SpecialInfo> getSpecial(String specialId) {
        return sNewsService.getSpecial(specialId)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(_flatMapSpecial(specialId));
    }

    /**
     * 获取新闻详情
     * @param newsId 新闻ID
     * @return
     */
    public static Observable<NewsDetailInfo> getNewsDetail(final String newsId) {
        return sNewsService.getNewsDetail(newsId)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Func1<Map<String, NewsDetailInfo>, Observable<NewsDetailInfo>>() {
                    @Override
                    public Observable<NewsDetailInfo> call(Map<String, NewsDetailInfo> newsDetailMap) {
                        return Observable.just(newsDetailMap.get(newsId));
                    }
                });
    }


    /**
     * 获取图集
     * @param photoId 图集ID
     * @return
     */
    public static Observable<PhotoSetInfo> getPhotoSet(String photoId) {
        return sNewsService.getPhotoSet(StringUtils.clipPhotoSetId(photoId))
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 获取图片列表
     * @return
     */
    public static Observable<List<PhotoInfo>> getPhotoList() {
        return sNewsService.getPhotoList()
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 获取更多图片列表
     * @return
     */
    public static Observable<List<PhotoInfo>> getPhotoMoreList(String setId) {
        return sNewsService.getPhotoMoreList(setId)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 获取美女图片
     * 注: 因为网易这个原接口参数一大堆，我只传了部分参数，返回的数据会出现图片重复的情况，请不要在意这个问题- -
     * @return
     */
    public static Observable<List<BeautyPhotoInfo>> getBeautyPhoto(int page) {
        return sNewsService.getBeautyPhoto(page * INCREASE_PAGE)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(_flatMapPhotos());
    }

    /**
     * 获取福利图片
     * @return
     */
    public static Observable<WelfarePhotoInfo> getWelfarePhoto(int page) {
        return sWelfareService.getWelfarePhoto(page)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(_flatMapWelfarePhotos());
    }


    /**
     * 获取视频列表
     * @return
     */
    public static Observable<List<VideoInfo>> getVideoList(String videoId, int page) {
        return sNewsService.getVideoList(videoId, page * INCREASE_PAGE / 2)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(_flatMapVideo(videoId));
    }

    //endregion

    //region Transformer 转换器


    /**
     * 类型转换
     * @param typeStr 新闻类型
     * @return
     */
    private static Func1<Map<String, List<NewsInfo>>, Observable<NewsInfo>> _flatMapNews(final String typeStr) {
        return new Func1<Map<String, List<NewsInfo>>, Observable<NewsInfo>>() {
            @Override
            public Observable<NewsInfo> call(Map<String, List<NewsInfo>> newsListMap) {
                return Observable.from(newsListMap.get(typeStr));
            }
        };
    }

    /**
     * 类型转换
     * @param typeStr 视频类型
     * @return
     */
    private static Func1<Map<String, List<VideoInfo>>, Observable<List<VideoInfo>>> _flatMapVideo(final String typeStr) {
        return new Func1<Map<String, List<VideoInfo>>, Observable<List<VideoInfo>>>() {
            @Override
            public Observable<List<VideoInfo>> call(Map<String, List<VideoInfo>> newsListMap) {
                return Observable.just(newsListMap.get(typeStr));
            }
        };
    }

    /**
     * 类型转换
     * @param specialId 专题id
     * @return
     */
    private static Func1<Map<String, SpecialInfo>, Observable<SpecialInfo>> _flatMapSpecial(final String specialId) {
        return new Func1<Map<String, SpecialInfo>, Observable<SpecialInfo>>() {
            @Override
            public Observable<SpecialInfo> call(Map<String, SpecialInfo> specialMap) {
                return Observable.just(specialMap.get(specialId));
            }
        };
    }

    /**
     * 类型转换
     * @return
     */
    private static Func1<Map<String, List<BeautyPhotoInfo>>, Observable<List<BeautyPhotoInfo>>> _flatMapPhotos() {
        return new Func1<Map<String, List<BeautyPhotoInfo>>, Observable<List<BeautyPhotoInfo>>>() {
            @Override
            public Observable<List<BeautyPhotoInfo>> call(Map<String, List<BeautyPhotoInfo>> newsListMap) {
                return Observable.just(newsListMap.get("美女"));
            }
        };
    }

    /**
     * 类型转换
     * @return
     */
    private static Func1<WelfarePhotoList, Observable<WelfarePhotoInfo>> _flatMapWelfarePhotos() {
        return new Func1<WelfarePhotoList, Observable<WelfarePhotoInfo>>() {
            @Override
            public Observable<WelfarePhotoInfo> call(WelfarePhotoList welfarePhotoList) {
                if (welfarePhotoList.getResults().size() == 0) {
                    return Observable.empty();
                }
                return Observable.from(welfarePhotoList.getResults());
            }
        };
    }
    //endregion

}
