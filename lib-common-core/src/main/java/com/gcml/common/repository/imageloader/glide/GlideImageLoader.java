package com.gcml.common.repository.imageloader.glide;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import com.gcml.common.repository.imageloader.IImageLoader;
import com.gcml.common.repository.imageloader.ImageLoader;
import com.gcml.common.repository.imageloader.glide.transformation.BlurTransformation;
import com.gcml.common.repository.imageloader.glide.transformation.CircleTransformation;

import java.util.ArrayList;

public class GlideImageLoader implements IImageLoader {
    @Override
    public int id() {
        return ImageLoader.GLIDE;
    }

    @SuppressLint("CheckResult")
    @Override
    public void load(ImageLoader.Options options) {
        RequestOptions requestOptions = new RequestOptions();
        if (options.placeholder() != -1) {
            requestOptions.placeholder(options.placeholder());
        }
        if (options.error() != -1) {
            requestOptions.fallback(options.error());
        }
        if (options.diskCacheStrategy() != ImageLoader.DiskCacheStrategy.DEFAULT) {
            switch (options.diskCacheStrategy()) {
                case All:
                    requestOptions.diskCacheStrategy(com.bumptech.glide.load.engine.DiskCacheStrategy.ALL);
                    break;
                case NONE:
                    requestOptions.diskCacheStrategy(com.bumptech.glide.load.engine.DiskCacheStrategy.NONE);
                    break;
                case SOURCE:
                    requestOptions.diskCacheStrategy(com.bumptech.glide.load.engine.DiskCacheStrategy.RESOURCE);
                    break;
                case RESULT:
                    requestOptions.diskCacheStrategy(com.bumptech.glide.load.engine.DiskCacheStrategy.DATA);
                    break;
                default:
                    break;
            }
        }
        requestOptions.skipMemoryCache(options.skipMemoryCache());
        if (options.width() > 0 && options.height() >= 0) {
            requestOptions.override(options.width(), options.height());
        }
        ArrayList<Transformation> transformations = new ArrayList<>();
        if (options.blur()) {
            transformations.add(new BlurTransformation(options.blurValue()));
        }
        if (options.radius() > 0) {
            transformations.add(new RoundedCorners(options.radius()));
        }
        if (options.circle()) {
            transformations.add(new CircleTransformation());
        }
        if (transformations.size() > 0) {
            Transformation[] transformationArray = transformations.toArray(new Transformation[transformations.size()]);
            requestOptions.transforms(transformationArray);
        }

        RequestBuilder builder = requestBuilder(options);
        builder.listener(new RequestListener() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target target, boolean isFirstResource) {
                if (options.callback() != null) {
                    options.callback().onFailed();
                }
                return false;
            }

            @Override
            public boolean onResourceReady(Object resource, Object model, Target target, DataSource dataSource, boolean isFirstResource) {
                if (options.callback() != null) {
                    options.callback().onSuccess();
                }
                return false;
            }
        });
        builder.apply(requestOptions).into((ImageView) options.target());
    }

    @SuppressLint("CheckResult")
    private RequestBuilder requestBuilder(ImageLoader.Options options) {
        RequestBuilder builder;
        if (options.asGif()) {
            builder = requestManager(options.target()).asGif();
        } else {
            builder = requestManager(options.target()).asBitmap();
        }

        if (!TextUtils.isEmpty(options.url())) {
            builder.load(options.url());
        } else if (options.model() != null){
            builder.load(options.model());
        } else {
            builder.load(options.resource());
        }
        return builder;
    }

    private RequestManager requestManager(View target) {
        return Glide.with(target);
    }

    @Override
    public void clearMemory(Context context) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            Glide.get(context).clearMemory();
        }
    }

    @Override
    public void pause(Context context) {
        Glide.with(context).pauseRequests();
    }

    @Override
    public void resume(Context context) {
        Glide.with(context).resumeRequests();
    }
}
