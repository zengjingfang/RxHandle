package com.xtc.rxhandle.rx.schedulers;


import com.xtc.rxhandle.rx.Scheduler;

/**
 *
 * Created by ZengJingFang on 2018/4/27.
 */

public class AndroidSchedulers {
    public static Scheduler mainThread() {
        return MainScheduler.getMainScheduler();
    }
}
