package com.xtc.rxhandle.rx.schedulers;


import com.xtc.rxhandle.rx.Scheduler;

/**
 *
 * Created by ZengJingFang on 2018/4/26.
 */

public class Schedulers {
    private final Scheduler ioScheduler;

    public Schedulers(Scheduler ioScheduler) {
        this.ioScheduler = ioScheduler;
    }


    public static Scheduler io() {
        return IOScheduler.getIOScheduler();
    }


}
