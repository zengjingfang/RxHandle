package com.xtc.rxhandle.rx.schedulers;


import com.xtc.rxhandle.rx.Scheduler;

/**
 * Android主线程
 *
 * Created by ZengJingFang on 2018/4/27.
 */

public class MainScheduler extends Scheduler {

    public static  Scheduler MAIN_SCHEDULER ;

    public static Scheduler getMainScheduler() {
        return MAIN_SCHEDULER;
    }

    @Override
    public Worker createWorker() {
        return null;
    }
}
