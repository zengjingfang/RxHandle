package com.xtc.rxhandle.rx;


import com.xtc.rxhandle.rx.functions.Action1;
import com.xtc.rxhandle.rx.functions.Func1;
import com.xtc.rxhandle.rx.operators.OnSubscribeLift;
import com.xtc.rxhandle.rx.operators.OperatorObserveOn;
import com.xtc.rxhandle.rx.operators.OperatorSubscribeOn;
import com.xtc.rxhandle.rx.util.RxRingBuffer;

/**
 * RxHandle
 * Created by ZengJingFang on 2018/4/25.
 */

public class Observable<T> {

    final Observable.OnSubscribe<T> onSubscribe;

    protected Observable(Observable.OnSubscribe<T> f) {
        this.onSubscribe = f;
    }

    public static <T> Observable<T> create(Observable.OnSubscribe<T> f) {
        return new Observable<T>(f);
    }

    public interface OnSubscribe<T> extends Action1<Subscriber<? super T>> {
        // cover for generics insanity
    }

    public final Subscription subscribe(Subscriber<? super T> subscriber) {
        return Observable.subscribe(subscriber, this);
    }

    public final Observable<T> subscribeOn(Scheduler scheduler) {

        return create(new OperatorSubscribeOn<T>(this, scheduler));
    }

    static <T> Subscription subscribe(Subscriber<? super T> subscriber, Observable<T> observable) {
        // validate and proceed
        if (subscriber == null) {
            throw new IllegalArgumentException("subscriber can not be null");
        }
        if (observable.onSubscribe == null) {
            throw new IllegalStateException("onSubscribe function can not be null.");
        }
        subscriber.onStart();

        try {
            observable.onSubscribe.call(subscriber);
//            return RxJavaHooks.onObservableReturn(subscriber);
        } catch (Throwable e) {

            subscriber.onError(e);
//            return Subscriptions.unsubscribed();
        }
        return null;//todo
    }

    public final Subscription unsafeSubscribe(Subscriber<? super T> subscriber) {
        try {
            // new Subscriber so onStart it
            subscriber.onStart();
            // allow the hook to intercept and/or decorate
            onSubscribe.call(subscriber);
            return subscriber;
        } catch (Throwable e) {
            try {
                subscriber.onError(e);
            } catch (Throwable e2) {
                throw e2;
            }
//            return Subscriptions.unsubscribed();
            return null;// TODO: 2018/4/27
        }
    }

    public interface Operator<R, T> extends Func1<Subscriber<? super R>, Subscriber<? super T>> {
        // cover for generics insanity
    }


    public final Observable<T> observeOn(Scheduler scheduler) {
        return observeOn(scheduler, RxRingBuffer.SIZE);
    }

    public final Observable<T> observeOn(Scheduler scheduler, int bufferSize) {
        return observeOn(scheduler, false, bufferSize);
    }
    public final Observable<T> observeOn(Scheduler scheduler, boolean delayError, int bufferSize) {
        return lift(new OperatorObserveOn<T>(scheduler, delayError, bufferSize));
    }
    public final <R> Observable<R> lift(final Operator<? extends R, ? super T> operator) {
        return create(new OnSubscribeLift<T, R>(onSubscribe, operator));
    }
}


