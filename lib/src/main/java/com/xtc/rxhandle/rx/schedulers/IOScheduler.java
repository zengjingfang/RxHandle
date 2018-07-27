package com.xtc.rxhandle.rx.schedulers;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;

import com.xtc.rxhandle.rx.Scheduler;
import com.xtc.rxhandle.rx.Subscription;
import com.xtc.rxhandle.rx.functions.Action0;
import com.xtc.rxhandle.rx.util.LogUtil;

import java.util.concurrent.TimeUnit;


/**
 * IO 任务 线程
 * Created by ZengJingFang on 2018/4/27.
 */

public class IOScheduler extends Scheduler {

    private static  Scheduler sScheduler;
    private static Handler ioHandler = null;

    public static Scheduler getIOScheduler() {
        synchronized (IOScheduler.class) {
            if (sScheduler == null) {
                synchronized (IOScheduler.class) {
                    sScheduler = new IOScheduler();
                }
            }
        }
        return sScheduler;
    }

    private IOScheduler() {
        HandlerThread handlerThread = new HandlerThread("io-handler-thread");
        handlerThread.start();
        ioHandler = new Handler(handlerThread.getLooper()){
            @Override
            public void handleMessage(Message msg) {
                ScheduledAction run = (ScheduledAction) msg.obj;

                if (run != null && run.action != null) {
                    LogUtil.d("handle call " + run.action);
                    run.action.call();
                }
            }
        };
    }



    @Override
    public Worker createWorker() {
        return new IOWork();
    }

    static final class IOWork extends Scheduler.Worker implements Subscription {
        volatile boolean isUnsubscribed;

        @Override
        public Subscription schedule(Action0 action) {
            return schedule(action, 0, null);
        }

        @Override
        public Subscription schedule(Action0 action, long delayTime, TimeUnit unit) {

            return scheduleActual(action, delayTime, unit);
        }

        @Override
        public void unsubscribe() {
            isUnsubscribed = true;
            ioHandler.removeMessages(1);
        }

        @Override
        public boolean isUnsubscribed() {

            return false;
        }

        Subscription scheduleActual(Action0 action, long delayTime, TimeUnit unit) {
            LogUtil.d("scheduleActual: "+ ioHandler);
            ScheduledAction run = new ScheduledAction(action);
            ioHandler.sendMessageDelayed(ioHandler.obtainMessage(1, 1, 1, run), delayTime);
            return run;
        }
    }



}
