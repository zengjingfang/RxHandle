package com.xtc.rxhandle.demo;

import android.os.Build;

import com.xtc.rxhandle.rx.Observable;
import com.xtc.rxhandle.rx.Scheduler;
import com.xtc.rxhandle.rx.Subscriber;
import com.xtc.rxhandle.rx.schedulers.Schedulers;

/**
 * 测试类
 * Created by ZengJingFang on 2018/6/9.
 */

public class RxHandleTest {
    private static final String TAG = "RxHandleTest";

    public static void main(String[] args) {
        testCreate();
    }


    public static void testCreate() {
        d(TAG, "xxx " + Build.SERIAL);
        Observable<String> observable1 = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {

                d(TAG, "===>>>> " + Thread.currentThread());

                subscriber.onStart();
                subscriber.onNext("--test-create--next" + Thread.currentThread());

                subscriber.onCompleted();

            }
        });

        Subscriber<String> stringSubscriber = new Subscriber<String>() {
            @Override
            public void onCompleted() {
                d(TAG, "===test=create=completed");

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {
                d(TAG, ">>> " + s);
                d(TAG, "===>>>> " + Thread.currentThread());
            }
        };

        observable1.subscribe(stringSubscriber);


        Scheduler ioScheduler = Schedulers.io();
        Observable<String> observer2 = observable1.subscribeOn(ioScheduler);
//
//        Scheduler mainScheduler = AndroidSchedulers.mainThread();
//        Observable<String> observer3 = observer2.observeOn(mainScheduler);
//
        observer2.subscribe(stringSubscriber);

    }

    public static void d(String tag,String s) {
        System.out.print("\n" + tag + s);
    }

}
