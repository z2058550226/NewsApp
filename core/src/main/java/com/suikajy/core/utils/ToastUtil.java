package com.suikajy.core.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by suikajy on 2017/4/5.
 */

public class ToastUtil {

    private static Toast toast;
    private static boolean isInited = false;

    private ToastUtil(){}

    public static void showToast(Context context, String msg) {
        toast = Toast.makeText(context, msg, Toast.LENGTH_LONG);
        toast.show();
        isInited = true;
    }

    public static void cancelToast() {
        if (isInited)
            toast.cancel();
    }

}
