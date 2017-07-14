package com.suikajy.newsapp.utils;

import com.suikajy.newsapp.R;

import java.util.Random;

/**
 * Created by suikajy on 2017/4/9.
 * 默认背景工厂类
 */

public class DefIconFactory {

    private final static int[] DEF_ICON_ID = new int[] {
            R.mipmap.ic_default_1,
            R.mipmap.ic_default_2,
            R.mipmap.ic_default_3,
            R.mipmap.ic_default_4,
            R.mipmap.ic_default_5
    };

    private static Random sRandom = new Random();

    private DefIconFactory() {
        throw new RuntimeException("DefIconFactory cannot be initialized!");
    }


    public static int provideIcon() {
        int index = sRandom.nextInt(DEF_ICON_ID.length);
        return DEF_ICON_ID[index];
    }
}
