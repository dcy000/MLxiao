package com.ml.edu;

import dagger.android.AndroidInjector;
import dagger.android.support.DaggerApplication;

/**
 * Created by afirez on 18-2-2.
 */

public class App extends DaggerApplication{
    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        return null;
    }
}
