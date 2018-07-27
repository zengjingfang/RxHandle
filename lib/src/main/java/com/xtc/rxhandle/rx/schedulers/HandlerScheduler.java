package com.xtc.rxhandle.rx.schedulers;

import android.os.Handler;
import android.os.Looper;

import com.xtc.rxhandle.rx.Scheduler;

/**
 *
 * Created by ZengJingFang on 2018/6/28.
 */

public class HandlerScheduler extends Scheduler {

    private final Handler mHandler;

    public HandlerScheduler(Looper looper) {
        mHandler = new Handler(looper);
    }

    @Override
    public Worker createWorker() {
        return null;
    }
}
