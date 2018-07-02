package com.ml.bci.game;

import android.database.Observable;

/**
 * Created by afirez on 2018/6/12.
 */

public class BciSignalObservable extends Observable<BciSignalObservable.Observer> {

    public interface Observer {
        void onAttentionChanged(int intensity);

        void onBlinkChanged(int intensity);
    }

    public void notifyAttentionChanged(int intensity) {
        synchronized (mObservers) {
            for (Observer observer : mObservers) {
                observer.onAttentionChanged(intensity);
            }
        }
    }

    public void notifyBlinkChanged(int intensity) {
        synchronized (mObservers) {
            for (Observer observer : mObservers) {
                observer.onBlinkChanged(intensity);
            }
        }
    }
}
