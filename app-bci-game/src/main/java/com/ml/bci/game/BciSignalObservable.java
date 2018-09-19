package com.ml.bci.game;

import android.database.Observable;
import android.os.Message;

/**
 * Created by afirez on 2018/6/12.
 */

public class BciSignalObservable extends Observable<BciSignalObservable.Observer> {

    public void notifyMessageChanged(Message message) {
        synchronized (mObservers) {
            for (Observer observer : mObservers) {
                observer.onMessageChanged(message);
            }
        }
    }

    public interface Observer {
        void onMessageChanged(Message message);

        void onAttentionChanged(int intensity);

        void onBlinkChanged(int intensity);

        void onPoorSignalChanged(int intensity);
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

    public void notifyPoorSignalChanged(int intensity) {
        synchronized (mObservers) {
            for (Observer observer : mObservers) {
                observer.onPoorSignalChanged(intensity);
            }
        }
    }
}
