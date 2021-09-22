package com.cxy.im4cxy.util;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;


public class BmobUtils {
    public static final String SMS_TEMPLATE = "smscode";


    public static void toast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }


    public static void log(String log) {
        Log.i("BmobLogin", log);
    }

    public static void wtf(Throwable throwable) {
        Log.wtf("BmobLogin", throwable);
    }










    public static long generateRandomNumber(int n) {
        if (n < 1) {
            throw new IllegalArgumentException("随机数位数必须大于0");
        }
        return (long) (Math.random() * 9 * Math.pow(10, n - 1)) + (long) Math.pow(10, n - 1);
    }

    /**
     * 随机账号
     *
     * @return
     */
    public static String getRandomAccount() {
        return "U" + generateRandomNumber(10);
    }
}
