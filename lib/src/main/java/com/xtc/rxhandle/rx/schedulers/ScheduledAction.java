package com.xtc.rxhandle.rx.schedulers;

import com.xtc.rxhandle.rx.Subscription;
import com.xtc.rxhandle.rx.functions.Action0;
import com.xtc.rxhandle.rx.subscriptions.CompositeSubscription;
import com.xtc.rxhandle.rx.util.SubscriptionList;

import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/**

import java.util.concurrent.Future;
import java.util.concurrent.atomic.*;

import rx.Subscription;
import rx.exceptions.OnErrorNotImplementedException;
import rx.functions.Action0;
import rx.internal.util.SubscriptionList;
import rx.plugins.RxJavaHooks;
import rx.subscriptions.CompositeSubscription;

/**
 * A {@code Runnable} that executes an {@code Action0} and can be cancelled. The analog is the
 * {@code Subscriber} in respect of an {@code Observer}.
 */
public final class ScheduledAction extends AtomicReference<Thread> implements Runnable, Subscription {
    /** */
    private static final long serialVersionUID = -3962399486978279857L;
    final SubscriptionList cancel;
    final Action0 action;

    public ScheduledAction(Action0 action) {
        this.action = action;
        this.cancel = new SubscriptionList();
    }
    public ScheduledAction(Action0 action, CompositeSubscription parent) {
        this.action = action;
        this.cancel = new SubscriptionList(new Remover(this, parent));
    }
    public ScheduledAction(Action0 action, SubscriptionList parent) {
        this.action = action;
        this.cancel = new SubscriptionList(new Remover2(this, parent));
    }

    @Override
    public void run() {
        try {
            lazySet(Thread.currentThread());
            action.call();
        }
        // TODO: 2018/6/9 注释
        /*catch (Throwable e) {
            signalError(new IllegalStateException("Exception thrown on Scheduler.Worker thread. Add `onError` handling.", e));
        } */catch (Throwable e) {
            signalError(new IllegalStateException("Fatal Exception thrown on Scheduler.Worker thread.", e));
        } finally {
            unsubscribe();
        }
    }

    void signalError(Throwable ie) {
        Thread thread = Thread.currentThread();
        thread.getUncaughtExceptionHandler().uncaughtException(thread, ie);
    }

    @Override
    public boolean isUnsubscribed() {
        return cancel.isUnsubscribed();
    }

    @Override
    public void unsubscribe() {
        if (!cancel.isUnsubscribed()) {
            cancel.unsubscribe();
        }
    }
    public void add(Subscription s) {
        cancel.add(s);
    }

    public void add(final Future<?> f) {
        cancel.add(new FutureCompleter(f));
    }

    public void addParent(CompositeSubscription parent) {
        cancel.add(new Remover(this, parent));
    }

    public void addParent(SubscriptionList parent) {
        cancel.add(new Remover2(this, parent));
    }

    final class FutureCompleter implements Subscription {
        private final Future<?> f;

        FutureCompleter(Future<?> f) {
            this.f = f;
        }

        @Override
        public void unsubscribe() {
            if (ScheduledAction.this.get() != Thread.currentThread()) {
                f.cancel(true);
            } else {
                f.cancel(false);
            }
        }
        @Override
        public boolean isUnsubscribed() {
            return f.isCancelled();
        }
    }

    static final class Remover extends AtomicBoolean implements Subscription {
        /** */
        private static final long serialVersionUID = 247232374289553518L;
        final ScheduledAction s;
        final CompositeSubscription parent;

        public Remover(ScheduledAction s, CompositeSubscription parent) {
            this.s = s;
            this.parent = parent;
        }

        @Override
        public boolean isUnsubscribed() {
            return s.isUnsubscribed();
        }

        @Override
        public void unsubscribe() {
            if (compareAndSet(false, true)) {
                parent.remove(s);
            }
        }

    }
    static final class Remover2 extends AtomicBoolean implements Subscription {
        /** */
        private static final long serialVersionUID = 247232374289553518L;
        final ScheduledAction s;
        final SubscriptionList parent;

        public Remover2(ScheduledAction s, SubscriptionList parent) {
            this.s = s;
            this.parent = parent;
        }

        @Override
        public boolean isUnsubscribed() {
            return s.isUnsubscribed();
        }

        @Override
        public void unsubscribe() {
            if (compareAndSet(false, true)) {
                parent.remove(s);
            }
        }

    }
}
