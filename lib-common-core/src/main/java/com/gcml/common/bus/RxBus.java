package com.gcml.common.bus;

import com.jakewharton.rxrelay2.PublishRelay;
import com.jakewharton.rxrelay2.Relay;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

/**
 * Implementation of tagable EventBus powered by RxJava2
 * Created by afirez on 2017/2/16.
 */

public class RxBus {
    private static volatile RxBus sDefault;
    private static final Map<String, RxBus> BUS_MAP = new ConcurrentHashMap<>();

    private final Relay<Object> relay;
    private final Map<Class<?>, Object> stickyEventMap;

    public RxBus() {
        relay = PublishRelay.create().toSerialized();
        stickyEventMap = new ConcurrentHashMap<>();
    }

    /**
     * @return Default Instance of RxBus
     */
    public static RxBus getDefault() {
        if (sDefault == null) {
            synchronized (RxBus.class) {
                if (sDefault == null) {
                    sDefault = new RxBus();
                }
            }
        }
        return sDefault;
    }

    /**
     * @param tag Mapping of RxBus
     * @return Instance of RxBus with {@param tag}
     */
    private static RxBus with(String tag) {
        RxBus rxBus = BUS_MAP.get(tag);
        if (rxBus == null) {
            synchronized (BUS_MAP) {
                rxBus = BUS_MAP.get(tag);
                if (rxBus == null) {
                    rxBus = new RxBus();
                    BUS_MAP.put(tag, rxBus);
                }
            }
        }
        return rxBus;
    }

    public static <T> BusObserver<T> consumer(Consumer<T> onNext) {
        return new BusObserver<>(onNext);
    }

    /**
     * to obtain Test Normal Event in RxBus with Event Type{@param eventType}
     *
     * @param eventType Event Type
     * @param <T>       Event Type Parameter
     * @return Test Normal Event
     */
    public <T> Observable<T> on(Class<T> eventType) {
        return relay.ofType(eventType);
    }

    /**
     * to obtain Test Sticky Event in RxBus with event type{@param eventType}
     *
     * @param eventType sticky Event Type to subscribe
     * @param <T>       sticky Event Type Parameter to subscribe
     * @return Test Sticky Event to subscribe
     */
    public <T> Observable<T> onSticky(final Class<T> eventType) {
        Observable<T> rxEvent = relay.ofType(eventType);
        T stickyEvent = stickyEvent(eventType);
        return stickyEvent == null
                ? rxEvent
                : rxEvent.mergeWith(Observable.just(stickyEvent));
    }

    /**
     * to obtain the cached Sticky Event in RxBus with Event Type{@param eventType}
     *
     * @param eventType sticky Event Type to Obtain
     * @param <T>       sticky Event Type Parameter to Obtain
     * @return Obtained sticky Event
     */
    public <T> T stickyEvent(Class<T> eventType) {
        synchronized (stickyEventMap) {
            return eventType.cast(stickyEventMap.get(eventType));
        }
    }

    /**
     * to post Normal Event{@param event} to RxBus
     *
     * @param event Normal Event to post
     */
    public void post(Object event) {
        relay.accept(event);
    }

    /**
     * to post Sticky Event{@param event} to RxBus
     *
     * @param event Sticky Event to post
     */
    public void postSticky(Object event) {
        cacheStickyEvent(event);
        post(event);
    }

    /**
     * to cache the Sticky Event in RxBus
     *
     * @param stickyEvent the Sticky Event instance
     */
    private void cacheStickyEvent(Object stickyEvent) {
        synchronized (stickyEventMap) {
            stickyEventMap.put(stickyEvent.getClass(), stickyEvent);
        }
    }

    /**
     * Does has Observers in RxBus
     *
     * @return Does has Observers in RxBus
     */
    public boolean hasObservers() {
        return relay.hasObservers();
    }

    /**
     * to remove the Sticky Event with event type{@param eventType} in RxBus
     * with the removed Sticky Event returned
     *
     * @param eventType sticky Event Type to Remove
     * @param <T>       sticky Event Type Parameter to Remove
     * @return removed Sticky Event
     */
    public <T> T removeStickyEvent(Class<T> eventType) {
        synchronized (stickyEventMap) {
            return eventType.cast(stickyEventMap.remove(eventType));
        }
    }

    /**
     * remove All Sticky Events in RxBus
     */
    public void removeAllStickyEvents() {
        synchronized (stickyEventMap) {
            stickyEventMap.clear();
        }
    }

    /**
     * reset RxBus
     */
    public static void resetDefault() {
        sDefault = null;
    }

    /**
     * reset RxBus with tag
     */
    public static void reset(String tag) {
        BUS_MAP.remove(tag);
    }

    /**
     * reset all RxBus
     */
    public static void resetAll() {
        sDefault = null;
        BUS_MAP.clear();
    }
}