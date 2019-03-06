package com.gcml.common.stateview;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;

/**
 * Created by lenovo on 2019/2/22.
 */

public class StateLayoutManager {


    final Context context;

    final ViewStub netWorkErrorVs;//网络错误的viewStub
    final int netWorkErrorRetryViewId;//网络错误可点击重试的 viewId

    final ViewStub emptyDataVs;//空布局的viewStub
    final int emptyDataRetryViewId;//空布局的可点击重试的 viewId

    final ViewStub errorVs;//数据错误的viewStub
    final int errorRetryViewId;//数据错误可点击重试的 viewId

    final int loadingLayoutResId;//loading的ResId
    final int contentLayoutResId;//内容的ResId

    final int retryViewId;//通用的 可点击重试的viewId

    final int emptyDataIconImageId;//空数据视图 图片viewId
    final int emptyDataTextTipId;//空数据视图 文字 viewId

    final int errorIconImageId;//错误视图 图片viewId
    final int errorTextTipId;//错误试图 文字viewId

    final AbsViewStubLayout errorLayout;//包装空viewStub的
    final AbsViewStubLayout emptyDataLayout;//包装错误viewStub的

    final StateFrameLayout rootFrameLayout;//5种布局的容器

    final OnShowHideViewListener onShowHideViewListener;//显示隐藏监听
    final OnRetryListener onRetryListener;//重试监听

    public StateLayoutManager(Builder builder) {
        this.context = builder.context;
        this.loadingLayoutResId = builder.loadingLayoutResId;
        this.netWorkErrorVs = builder.netWorkErrorVs;
        this.netWorkErrorRetryViewId = builder.netWorkErrorRetryViewId;
        this.emptyDataVs = builder.emptyDataVs;
        this.emptyDataRetryViewId = builder.emptyDataRetryViewId;
        this.errorVs = builder.errorVs;
        this.errorRetryViewId = builder.errorRetryViewId;
        this.contentLayoutResId = builder.contentLayoutResId;
        this.onShowHideViewListener = builder.onShowHideViewListener;
        this.retryViewId = builder.retryViewId;
        this.onRetryListener = builder.onRetryListener;
        this.emptyDataIconImageId = builder.emptyDataIconImageId;
        this.emptyDataTextTipId = builder.emptyDataTextTipId;
        this.errorIconImageId = builder.errorIconImageId;
        this.errorTextTipId = builder.errorTextTipId;
        this.errorLayout = builder.errorLayout;
        this.emptyDataLayout = builder.emptyDataLayout;

        //创建帧布局
        rootFrameLayout = new StateFrameLayout(this.context);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        rootFrameLayout.setLayoutParams(layoutParams);

        //设置状态管理器
        rootFrameLayout.setStatusLayoutManager(this);
    }

    public static Builder newBuilder(Context context) {
        return new Builder(context);
    }

    /**
     * 显示loading
     */
    public void showLoading() {
        rootFrameLayout.showLoading();
    }

    /**
     * 显示内容
     */
    public void showContent() {
        rootFrameLayout.showContent();
    }

    /**
     * 显示空数据
     */
    public void showEmptyData(int iconImage, String textTip) {
        rootFrameLayout.showEmptyData(iconImage, textTip);
    }

    /**
     * 显示空数据
     */
    public void showEmptyData() {
        showEmptyData(0, "");
    }

    /**
     * 显示空数据
     */
    public void showLayoutEmptyData(Object... objects) {
        rootFrameLayout.showLayoutEmptyData(objects);
    }

    /**
     * 显示网络异常
     */
    public void showNetWorkError() {
        rootFrameLayout.showNetWorkError();
    }

    /**
     * 显示异常
     */
    public void showError(int iconImage, String textTip) {
        rootFrameLayout.showError(iconImage, textTip);
    }

    /**
     * 显示异常
     */
    public void showError() {
        showError(0, "");
    }

    public void showLayoutError(Object... objects) {
        rootFrameLayout.showLayoutError(objects);
    }

    /**
     * 得到root 布局
     */
    public View getRootLayout() {
        return rootFrameLayout;
    }

    public static final class Builder {

        private Context context;
        private int loadingLayoutResId;
        private int contentLayoutResId;
        private ViewStub netWorkErrorVs;
        private int netWorkErrorRetryViewId;
        private ViewStub emptyDataVs;
        private int emptyDataRetryViewId;
        private ViewStub errorVs;
        private int errorRetryViewId;
        private int retryViewId;
        private int emptyDataIconImageId;
        private int emptyDataTextTipId;
        private int errorIconImageId;
        private int errorTextTipId;
        private AbsViewStubLayout errorLayout;
        private AbsViewStubLayout emptyDataLayout;
        private OnShowHideViewListener onShowHideViewListener;
        private OnRetryListener onRetryListener;

        Builder(Context context) {
            this.context = context;
        }

        /**
         * 自定义加载布局
         */
        public Builder loadingView(@LayoutRes int loadingLayoutResId) {
            this.loadingLayoutResId = loadingLayoutResId;
            return this;
        }

        /**
         * 自定义网络错误布局
         */
        public Builder netWorkErrorView(@LayoutRes int newWorkErrorId) {
            netWorkErrorVs = new ViewStub(context);
            netWorkErrorVs.setLayoutResource(newWorkErrorId);
            return this;
        }

        /**
         * 自定义加载空数据布局
         */
        public Builder emptyDataView(@LayoutRes int noDataViewId) {
            emptyDataVs = new ViewStub(context);
            emptyDataVs.setLayoutResource(noDataViewId);
            return this;
        }

        /**
         * 自定义加载错误布局
         */
        public Builder errorView(@LayoutRes int errorViewId) {
            errorVs = new ViewStub(context);
            errorVs.setLayoutResource(errorViewId);
            return this;
        }

        /**
         * 自定义加载内容正常布局
         */
        public Builder contentView(@LayoutRes int contentLayoutResId) {
            this.contentLayoutResId = contentLayoutResId;
            return this;
        }

        public Builder errorLayout(AbsViewStubLayout errorLayout) {
            this.errorLayout = errorLayout;
            this.errorVs = errorLayout.getLayoutVs();
            return this;
        }

        public Builder emptyDataLayout(AbsViewStubLayout emptyDataLayout) {
            this.emptyDataLayout = emptyDataLayout;
            this.emptyDataVs = emptyDataLayout.getLayoutVs();
            return this;
        }

        public Builder netWorkErrorRetryViewId(@LayoutRes int netWorkErrorRetryViewId) {
            this.netWorkErrorRetryViewId = netWorkErrorRetryViewId;
            return this;
        }

        public Builder emptyDataRetryViewId(@LayoutRes int emptyDataRetryViewId) {
            this.emptyDataRetryViewId = emptyDataRetryViewId;
            return this;
        }

        public Builder errorRetryViewId(@LayoutRes int errorRetryViewId) {
            this.errorRetryViewId = errorRetryViewId;
            return this;
        }

        public Builder retryViewId(@LayoutRes int retryViewId) {
            this.retryViewId = retryViewId;
            return this;
        }

        public Builder emptyDataIconImageId(@LayoutRes int emptyDataIconImageId) {
            this.emptyDataIconImageId = emptyDataIconImageId;
            return this;
        }

        public Builder emptyDataTextTipId(@LayoutRes int emptyDataTextTipId) {
            this.emptyDataTextTipId = emptyDataTextTipId;
            return this;
        }

        public Builder errorIconImageId(@LayoutRes int errorIconImageId) {
            this.errorIconImageId = errorIconImageId;
            return this;
        }

        public Builder errorTextTipId(@LayoutRes int errorTextTipId) {
            this.errorTextTipId = errorTextTipId;
            return this;
        }

        /**
         * 为状态View显示隐藏监听事件
         *
         * @param listener listener
         * @return
         */
        public Builder onShowHideViewListener(OnShowHideViewListener listener) {
            this.onShowHideViewListener = listener;
            return this;
        }

        /**
         * 为重试加载按钮的监听事件
         *
         * @param onRetryListener listener
         * @return
         */
        public Builder onRetryListener(OnRetryListener onRetryListener) {
            this.onRetryListener = onRetryListener;
            return this;
        }

        /**
         * 创建对象
         *
         * @return
         */
        public StateLayoutManager build() {
            return new StateLayoutManager(this);
        }
    }
}
