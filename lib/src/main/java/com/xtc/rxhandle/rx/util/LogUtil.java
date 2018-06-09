package com.xtc.rxhandle.rx.util;

import android.util.Log;

/**
 *
 * Created by ZengJingFang on 2018/6/9.
 */

public class LogUtil{

    private static String getCurrentThread() {
        return " [" + Thread.currentThread() + "] ";
    }
    public static void v(String message) {
        Log.v(getCurrentThread(), message);
    }

    public static void d(String message) {
        Log.d(getCurrentThread(), message);
    }

    public static void i(String message) {
        Log.i(getCurrentThread(), message);
    }

    public static void w(String message) {
        Log.w(getCurrentThread(), message);
    }

    public static void e(String message) {
        Log.e(getCurrentThread(), message);
    }

    public static void wtf(String message) {
        Log.wtf(getCurrentThread(), message);
    }

    public static void e(Throwable throwable) {
        Log.e(getCurrentThread(), "", throwable);
    }

    public static void v(String tag, String message) {
        Log.v(getCurrentThread()+tag, message);
    }

    public static void d(String tag, String message) {
        Log.d(getCurrentThread()+tag, message);
    }

    public static void i(String tag, String message) {
        Log.i(getCurrentThread()+tag, message);
    }

    public static void w(String tag, String message) {
        Log.w(getCurrentThread()+tag, message);
    }

    public static void e(String tag, String message) {
        Log.e(getCurrentThread()+tag, message);
    }

    public static void wtf(String tag, String message) {
        Log.wtf(getCurrentThread()+tag, message);
    }

    public static void w(String tag, Throwable throwable) {
        Log.w(getCurrentThread()+tag, "", throwable);
    }

    public static void e(String tag, Throwable throwable) {
        Log.e(getCurrentThread()+tag, "", throwable);
    }

    public static void v(String tag, String message, Throwable throwable) {
        Log.v(getCurrentThread()+tag, message, throwable);
    }

    public static void d(String tag, String message, Throwable throwable) {
        Log.d(getCurrentThread()+tag, message, throwable);
    }

    public static void i(String tag, String message, Throwable throwable) {
        Log.i(getCurrentThread()+tag, message, throwable);
    }

    public static void w(String tag, String message, Throwable throwable) {
        Log.w(getCurrentThread()+tag, message, throwable);
    }

    public static void e(String tag, String message, Throwable throwable) {
        Log.e(getCurrentThread()+tag, message, throwable);
    }

    public static void wtf(String tag, String message, Throwable throwable) {
        Log.wtf(getCurrentThread()+tag, message, throwable);
    }
}
