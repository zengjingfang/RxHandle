package com.xtc.rxhandle.demo;

import com.xtc.rxhandle.rx.Observable;
import com.xtc.rxhandle.rx.Scheduler;
import com.xtc.rxhandle.rx.Subscriber;
import com.xtc.rxhandle.rx.schedulers.Schedulers;
import com.xtc.rxhandle.rx.util.LogUtil;

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
        Observable<String> observable1 = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {


                subscriber.onStart();
                subscriber.onNext("--test-create--next" + Thread.currentThread());

                subscriber.onCompleted();

            }
        });

        Subscriber<String> stringSubscriber = new Subscriber<String>() {
            @Override
            public void onCompleted() {
                LogUtil.d(TAG, "===test=create=completed");

            }


            @Override
            public void onError(Throwable e) {
                LogUtil.e(TAG, e);
            }

            @Override
            public void onNext(String s) {
                LogUtil.d(TAG, ">>> " + s);
            }
        };

//        observable1.subscribe(stringSubscriber);


        Scheduler ioScheduler = Schedulers.io();
        Observable<String> observer2 = observable1.subscribeOn(ioScheduler);
//
//        Scheduler mainScheduler = AndroidSchedulers.mainThread();
//        Observable<String> observer3 = observer2.observeOn(mainScheduler);
//
        observer2.subscribe(stringSubscriber);


    }


}
