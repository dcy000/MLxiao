package com.gcml.common.repository.imageloader;

import android.content.Context;

public interface IImageLoader {

    int id();

    void load(ImageLoader.Options options);

    void clearMemory(Context context);

    void pause(Object host);

    void resume(Object host);

}
