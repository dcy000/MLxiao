package com.gcml.common.repository.imageloader;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.util.SparseArray;
import android.view.View;


import com.gcml.common.repository.RepositoryApp;
import com.gcml.common.utils.ManifestParser;

import java.util.List;


public class ImageLoader implements IImageLoader {

    public static final int GLIDE = 1;

    public static final int FRESCO = 2;

    private SparseArray<IImageLoader> loaders = new SparseArray<>();

    private int loaderId = GLIDE;

    private static class Holder {
        private static final ImageLoader INSTANCE = new ImageLoader();
    }

    public static ImageLoader instance() {
        return Holder.INSTANCE;
    }

    private ImageLoader() {
        List<IImageLoader> imageLoaders = new ManifestParser<IImageLoader>(
                RepositoryApp.INSTANCE.app(), "ImageLoader").parse();
        for (IImageLoader imageLoader : imageLoaders) {
            if (imageLoader != null) {
                loaders.put(imageLoader.id(), imageLoader);
            }
        }
    }

    public static Options.Builder newOptionsBuilder(View target, String url) {
        return new Options.Builder(target, url);
    }

    public static Options.Builder newOptionsBuilder(View target, @DrawableRes int resource) {
        return new Options.Builder(target, resource);
    }

    public static Options.Builder newOptionsBuilder(View target, Object model) {
        return new Options.Builder(target, model);
    }

    public static Options.Builder with(Object host) {
        return new Options.Builder().host(host);
    }

    @Override
    public int id() {
        return 0;
    }

    @Override
    public void load(Options options) {
        IImageLoader iImageLoader = loaders.get(loaderId);
        if (iImageLoader != null) {
            iImageLoader.load(options);
        }
    }

    @Override
    public void clearMemory(Context context) {
        IImageLoader iImageLoader = loaders.get(loaderId);
        if (iImageLoader != null) {
            iImageLoader.clearMemory(context);
        }
    }

    @Override
    public void pause(Context context) {
        IImageLoader iImageLoader = loaders.get(loaderId);
        if (iImageLoader != null) {
            iImageLoader.pause(context);
        }
    }

    @Override
    public void resume(Context context) {
        IImageLoader iImageLoader = loaders.get(loaderId);
        if (iImageLoader != null) {
            iImageLoader.resume(context);
        }
    }

    public ImageLoader loaderId(int loaderId) {
        this.loaderId = loaderId;
        return this;
    }

    public static ImageLoader glide() {
        return ImageLoader.instance().loaderId(GLIDE);
    }

    public static ImageLoader fresco() {
        return ImageLoader.instance().loaderId(FRESCO);
    }

    public static class Options {
        private Object host;
        private View target;
        private String url;
        private int resource;  // 图片地址
        private Object model;
        private int placeholder;// 占位符
        private int error;// 错误占位符
        private int width;
        private int height;
        private boolean asGif;
        private boolean crossFade; // 是否渐变平滑的显示图片
        private boolean skipMemoryCache;
        private DiskCacheStrategy diskCacheStrategy;
        private Callback callback;
        private boolean blur; // 是否使用高斯模糊
        private int blurValue;   // 高斯模糊参数，越大越模糊
        private int radius;
        private boolean circle;
        private int loaderId = GLIDE;

        public Options(Builder builder) {
            this.host = builder.host;
            this.target = builder.target;
            this.url = builder.url;
            this.resource = builder.resource;
            this.model = builder.model;
            this.placeholder = builder.placeholder;
            this.error = builder.error;
            this.width = builder.width;
            this.height = builder.height;
            this.asGif = builder.asGif;
            this.crossFade = builder.crossFade;
            this.skipMemoryCache = builder.skipMemoryCache;
            this.diskCacheStrategy = builder.diskCacheStrategy;
            this.callback = builder.callback;
            this.blur = builder.blur;
            this.blurValue = builder.blurValue;
            this.radius = builder.radius;
            this.circle = builder.circle;
            this.loaderId = builder.loaderId;
        }

        public Object host() {
            return host;
        }

        public View target() {
            return target;
        }

        public String url() {
            return url;
        }

        public Object model() {
            return model;
        }

        public int resource() {
            return resource;
        }

        @DrawableRes
        public int placeholder() {
            return placeholder;
        }

        public int error() {
            return error;
        }

        public int width() {
            return width;
        }

        public int height() {
            return height;
        }

        public boolean asGif() {
            return asGif;
        }

        public boolean crossFade() {
            return crossFade;
        }

        public boolean skipMemoryCache() {
            return skipMemoryCache;
        }

        public DiskCacheStrategy diskCacheStrategy() {
            return diskCacheStrategy;
        }

        public Callback callback() {
            return callback;
        }

        public boolean blur() {
            return blur;
        }

        public int blurValue() {
            return blurValue;
        }

        public int radius() {
            return radius;
        }

        public boolean circle() {
            return circle;
        }

        public static class Builder {
            private Object host;
            private View target;
            private String url;
            private Object model;
            private int resource = -1;  // 图片地址
            private int placeholder;// 占位符
            private int error;// 错误占位符
            private int width;
            private int height;
            private boolean asGif;
            private boolean crossFade = true; // 是否渐变平滑的显示图片
            private boolean skipMemoryCache = false;
            private DiskCacheStrategy diskCacheStrategy = DiskCacheStrategy.DEFAULT;
            private Callback callback;
            private boolean blur = false; // 是否使用高斯模糊
            private int blurValue = 15;   // 高斯模糊参数，越大越模糊
            private int radius = 0;
            private boolean circle = false;
            private int loaderId = GLIDE;

            public Builder() {
            }

            public Builder(View target, String url) {
                this.host = target;
                this.target = target;
                this.url = url;
            }

            public Builder(View target, int resource) {
                this.host = target;
                this.target = target;
                this.resource = resource;
            }

            public Builder(View target, Object model) {
                this.host = target;
                this.target = target;
                this.model = model;
            }

            public Builder host(Object host) {
                this.host = host;
                return this;
            }

            public Builder load(String url) {
                this.url = url;
                return this;
            }

            public Builder load(@DrawableRes int resource) {
                this.resource = resource;
                return this;
            }

            public Builder load(Object model) {
                this.model = model;
                return this;
            }

            public Builder placeholder(@DrawableRes int placeholder) {
                this.placeholder = placeholder;
                return this;
            }

            public Builder error(@DrawableRes int error) {
                this.error = error;
                return this;
            }

            public Builder asGif() {
                this.asGif = true;
                return this;
            }

            public Builder resize(int width, int height) {
                this.width = width;
                this.height = height;
                return this;
            }

            public Builder crossFade(boolean crossFade) {
                this.crossFade = crossFade;
                return this;
            }

            public Builder skipMemoryCache() {
                this.skipMemoryCache = true;
                return this;
            }

            public Builder diskCacheStrategy(DiskCacheStrategy diskCacheStrategy) {
                this.diskCacheStrategy = diskCacheStrategy;
                return this;
            }

            public Builder callback(Callback callback) {
                this.callback = callback;
                return this;
            }

            public Builder blur() {
                this.blur = true;
                return this;
            }

            public Builder blurValue(int blurValue) {
                this.blurValue = blurValue;
                return this;
            }

            public Builder radius(int radius) {
                this.radius = radius;
                return this;
            }

            public Builder circle() {
                this.circle = true;
                return this;
            }

            public Builder glide() {
                return loaderId(GLIDE);
            }

            public Builder fressco() {
                return loaderId(FRESCO);
            }

            public Builder loaderId(int loaderId) {
                this.loaderId = loaderId;
                return this;
            }

            public Options build() {
                return new Options(this);
            }

            public void into(View view) {
                this.target = view;
                Options options = new Options(this);
                ImageLoader.instance().loaderId(options.loaderId).load(options);
            }
        }
    }

    public enum DiskCacheStrategy {
        All, NONE, SOURCE, RESULT, DEFAULT
    }

    public interface Callback {
        void onSuccess();

        void onFailed();
    }
}