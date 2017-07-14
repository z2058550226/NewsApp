package com.suikajy.core.utils;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by suikajy on 2017/4/7.
 * assets帮助类
 */

public class AssetsHelper {
    /**
     * 读取 assets 文件
     *
     * @param context
     * @param fileName
     * @return
     */
    public static String readData(Context context, String fileName) {
        InputStream inStream = null;
        String data = null;
        try {
            Log.i("TAG", fileName);
            inStream = context.getAssets().open(fileName);     //打开assets目录中的文本文件
            byte[] bytes = new byte[inStream.available()];  //inStream.available()为文件中的总byte数
            inStream.read(bytes);
            inStream.close();
            data = new String(bytes, "utf-8");        //将bytes转为utf-8字符串
        } catch (IOException e) {
            Log.e("AssetsHelper : ", e.toString());
            e.printStackTrace();
        }
        Log.i("TAG", "data is " + data);
        return data;
    }
}
