package com.xtc.rxhandle.rx;


import com.xtc.rxhandle.rx.observer.Observer;
import com.xtc.rxhandle.rx.util.SubscriptionList;

/**
 *
 * Created by ZengJingFang on 2018/4/25.
 */

public abstract class Subscriber<T> implements Observer<T>, Subscription {

    // represents requested not set yet
    private static final long NOT_SET = Long.MIN_VALUE;

    private final SubscriptionList subscriptions;
    private final Subscriber<?> subscriber;
    /* protected by `this` */
    private Producer producer;
    /* protected by `this` */
    private long requested = NOT_SET; // default to not set

    protected Subscriber() {
        this(null);
    }

    protected Subscriber(Subscriber<?> subscriber) {
        this.subscriber = subscriber;
        this.subscriptions = subscriber != null ? subscriber.subscriptions : new SubscriptionList();
    }



    public final void add(Subscription s) {
        subscriptions.add(s);
    }

    public void onStart() {
        // do nothing by default
    }

    @Override
    public final void unsubscribe() {
//        subscriptions.unsubscribe();
    }

    /**
     * Indicates whether this Subscriber has unsubscribed from its list of subscriptions.
     *
     * @return {@code true} if this Subscriber has unsubscribed from its subscriptions, {@code false} otherwise
     */
    @Override
    public final boolean isUnsubscribed() {
//        return subscriptions.isUnsubscribed();
        return false;
    }
    public void setProducer(Producer p) {
        long toRequest;
        boolean passToSubscriber = false;
        synchronized (this) {
            toRequest = requested;
            producer = p;
            if (subscriber != null) {
                // middle operator ... we pass through unless a request has been made
                if (toRequest == NOT_SET) {
                    // we pass through to the next producer as nothing has been requested
                    passToSubscriber = true;
                }
            }
        }
        // do after releasing lock
        if (passToSubscriber) {
            subscriber.setProducer(producer);
        } else {
            // we execute the request with whatever has been requested (or Long.MAX_VALUE)
            if (toRequest == NOT_SET) {
                producer.request(Long.MAX_VALUE);
            } else {
                producer.request(toRequest);
            }
        }
    }



    protected final void request(long n) {
        if (n < 0) {
            throw new IllegalArgumentException("number requested cannot be negative: " + n);
        }

        // if producer is set then we will request from it
        // otherwise we increase the requested count by n
        Producer producerToRequestFrom;
        synchronized (this) {
            if (producer != null) {
                producerToRequestFrom = producer;
            } else {
                addToRequested(n);
                return;
            }
        }
        // after releasing lock (we should not make requests holding a lock)
        producerToRequestFrom.request(n);
    }



    private void addToRequested(long n) {
        if (requested == NOT_SET) {
            requested = n;
        } else {
            final long total = requested + n;
            // check if overflow occurred
            if (total < 0) {
                requested = Long.MAX_VALUE;
            } else {
                requested = total;
            }
        }
    }

}
