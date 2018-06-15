package com.ml.bci.game;

import android.database.Observable;

/**
 * Created by afirez on 2018/6/12.
 */

public class AttentionObservable extends Observable<AttentionObservable.Observer> {

    public interface Observer {
        void onAttentionChanged(int intensity);

        void onBlink();
    }

    public void notifyAttentionChanged(int intensity) {
        synchronized (mObservers) {
            for (Observer observer : mObservers) {
                observer.onAttentionChanged(intensity);
            }
        }
    }

    public void notifyBlink() {
        synchronized (mObservers) {
            for (Observer observer : mObservers) {
                observer.onBlink();
            }
        }
    }
}
