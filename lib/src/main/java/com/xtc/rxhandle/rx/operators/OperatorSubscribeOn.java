package com.xtc.rxhandle.rx.operators;


import com.xtc.rxhandle.rx.Observable;
import com.xtc.rxhandle.rx.Producer;
import com.xtc.rxhandle.rx.Scheduler;
import com.xtc.rxhandle.rx.Subscriber;
import com.xtc.rxhandle.rx.functions.Action0;

/**
 *
 * Created by ZengJingFang on 2018/4/27.
 */

public class OperatorSubscribeOn<T> implements Observable.OnSubscribe<T> {

    final Scheduler scheduler;
    /**
     * Observable 源
     */
    final Observable<T> source;

    public OperatorSubscribeOn(Observable<T> source, Scheduler scheduler) {
        this.scheduler = scheduler;
        this.source = source;
    }


    @Override
    public void call(final Subscriber<? super T> subscriber) {
        /*外边来的subscriber
          通过下面的转换，构建出一个新的 Subscriber
         */

        final Scheduler.Worker inner = scheduler.createWorker();
        subscriber.add(inner);

        inner.schedule(new Action0() {
            @Override
            public void call() {
                final Thread t = Thread.currentThread();

                Subscriber<T> s = new Subscriber<T>(subscriber) {
                    @Override
                    public void onNext(T t) {
                        subscriber.onNext(t);
                    }

                    @Override
                    public void onError(Throwable e) {
                        try {
                            subscriber.onError(e);
                        } finally {
                            inner.unsubscribe();
                        }
                    }

                    @Override
                    public void onCompleted() {
                        try {
                            subscriber.onCompleted();
                        } finally {
                            inner.unsubscribe();
                        }
                    }

                    @Override
                    public void setProducer(final Producer p) {
                        subscriber.setProducer(new Producer() {
                            @Override
                            public void request(final long n) {
                                if (t == Thread.currentThread()) {
                                    p.request(n);
                                } else {
                                    inner.schedule(new Action0() {
                                        @Override
                                        public void call() {
                                            p.request(n);
                                        }
                                    });
                                }
                            }
                        });
                    }
                };

                // 源Observable 订阅了 新构建的 Subscriber
                source.unsafeSubscribe(s);
            }
        });
    }
}
