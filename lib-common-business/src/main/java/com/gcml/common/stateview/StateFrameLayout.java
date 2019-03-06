package com.gcml.common.stateview;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by lenovo on 2019/2/22.
 */

public class StateFrameLayout extends FrameLayout {

    /**
     * loading 加载id
     */
    public static final int LAYOUT_LOADING_ID = 1;

    /**
     * 内容id
     */
    public static final int LAYOUT_CONTENT_ID = 2;

    /**
     * 异常id
     */
    public static final int LAYOUT_ERROR_ID = 3;

    /**
     * 网络异常id
     */
    public static final int LAYOUT_NETWORK_ERROR_ID = 4;

    /**
     * 空数据id
     */
    public static final int LAYOUT_EMPTY_DATA_ID = 5;

    /**
     * 存放布局集合
     */
    private SparseArray<View> layoutSparseArray = new SparseArray();

    /**
     * 布局管理器 装配了状态布局的参数 构建状态布局
     */
    private StateLayoutManager mStatusLayoutManager;

    public StateFrameLayout(@NonNull Context context) {
        super(context);
    }

    public StateFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public StateFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public void setStatusLayoutManager(StateLayoutManager statusLayoutManager) {
        mStatusLayoutManager = statusLayoutManager;
        //添加所有的布局到帧布局
        addAllLayoutToRootLayout();
    }

    private void addAllLayoutToRootLayout() {
        //添加内容布局
        if (mStatusLayoutManager.contentLayoutResId != 0) {
            addLayoutResId(mStatusLayoutManager.contentLayoutResId, StateFrameLayout.LAYOUT_CONTENT_ID);
        }
        //添加loading布局
        if (mStatusLayoutManager.loadingLayoutResId != 0) {
            addLayoutResId(mStatusLayoutManager.loadingLayoutResId, StateFrameLayout.LAYOUT_LOADING_ID);
        }

        if (mStatusLayoutManager.emptyDataVs != null) {
            addView(mStatusLayoutManager.emptyDataVs);
        }
        if (mStatusLayoutManager.errorVs != null) {
            addView(mStatusLayoutManager.errorVs);
        }
        if (mStatusLayoutManager.netWorkErrorVs != null) {
            addView(mStatusLayoutManager.netWorkErrorVs);
        }
    }

    /**
     * 通过resId装载布局 加到map集合中
     */
    private void addLayoutResId(@LayoutRes int layoutResId, int id) {
        View resView = LayoutInflater.from(mStatusLayoutManager.context).inflate(layoutResId, null);
        layoutSparseArray.put(id, resView);
        addView(resView);
    }

    /**
     * 显示loading
     */
    public void showLoading() {
        if (layoutSparseArray.get(LAYOUT_LOADING_ID) != null)
            showHideViewById(LAYOUT_LOADING_ID);
}

    /**
     * 显示内容
     */
    public void showContent() {
        if (layoutSparseArray.get(LAYOUT_CONTENT_ID) != null)
            showHideViewById(LAYOUT_CONTENT_ID);
    }

    /**
     * 显示空数据
     */
    public void showEmptyData(int iconImage, String textTip) {
        if (inflateLayout(LAYOUT_EMPTY_DATA_ID)) {//填充
            showHideViewById(LAYOUT_EMPTY_DATA_ID);//显示
            emptyDataViewAddData(iconImage, textTip);//设置布局 细节
        }
    }

    /**
     * 显示网络异常
     */
    public void showNetWorkError() {
        if (inflateLayout(LAYOUT_NETWORK_ERROR_ID))
            showHideViewById(LAYOUT_NETWORK_ERROR_ID);
    }

    /**
     * 显示异常
     */
    public void showError(int iconImage, String textTip) {
        if (inflateLayout(LAYOUT_ERROR_ID)) {
            showHideViewById(LAYOUT_ERROR_ID);
            errorViewAddData(iconImage, textTip);
        }
    }

    private void errorViewAddData(int iconImage, String textTip) {
        if (iconImage == 0 && TextUtils.isEmpty(textTip)) return;
        View errorView = layoutSparseArray.get(LAYOUT_ERROR_ID);
        View iconImageView = errorView.findViewById(mStatusLayoutManager.emptyDataIconImageId);
        View textView = errorView.findViewById(mStatusLayoutManager.emptyDataTextTipId);
        if (iconImageView != null && iconImageView instanceof ImageView) {
            ((ImageView) iconImageView).setImageResource(iconImage);
        }

        if (textView != null && textView instanceof TextView) {
            ((TextView) textView).setText(textTip);
        }
    }

    /**
     * 展示错误
     * @param objects
     */
    public void showLayoutError(Object... objects) {
        if (inflateLayout(LAYOUT_ERROR_ID)) {
            showHideViewById(LAYOUT_ERROR_ID);
            AbsViewStubLayout errorLayout = mStatusLayoutManager.errorLayout;
            if (errorLayout != null) {
                errorLayout.setData(objects);
            }
        }
    }

    /**
     * 根据ID显示隐藏布局
     *
     * @param id
     */
    private void showHideViewById(int id) {
        for (int i = 0; i < layoutSparseArray.size(); i++) {
            int key = layoutSparseArray.keyAt(i);
            View valueView = layoutSparseArray.valueAt(i);
            //显示该view
            if (key == id) {
                valueView.setVisibility(View.VISIBLE);
                if (mStatusLayoutManager.onShowHideViewListener != null)
                    mStatusLayoutManager.onShowHideViewListener.onShowView(valueView, key);
            } else {
                if (valueView.getVisibility() != View.GONE) {
                    valueView.setVisibility(View.GONE);
                    if (mStatusLayoutManager.onShowHideViewListener != null)
                        mStatusLayoutManager.onShowHideViewListener.onHideView(valueView, key);
                }
            }
        }
    }

    private boolean inflateLayout(int id) {
        boolean isShow = true;
        if (layoutSparseArray.get(id) != null) return isShow;
        switch (id) {
            case LAYOUT_NETWORK_ERROR_ID:
                if (mStatusLayoutManager.netWorkErrorVs != null) {
                    View view = mStatusLayoutManager.netWorkErrorVs.inflate();
                    retryLoad(view, mStatusLayoutManager.netWorkErrorRetryViewId);
                    layoutSparseArray.put(id, view);
                    isShow = true;
                } else {
                    isShow = false;
                }
                break;
            case LAYOUT_ERROR_ID:
                if (mStatusLayoutManager.errorVs != null) {
                    View view = mStatusLayoutManager.errorVs.inflate();
                    if (mStatusLayoutManager.errorLayout != null)
                        mStatusLayoutManager.errorLayout.setView(view);
                    retryLoad(view, mStatusLayoutManager.errorRetryViewId);
                    layoutSparseArray.put(id, view);
                    isShow = true;
                } else {
                    isShow = false;
                }
                break;
            case LAYOUT_EMPTY_DATA_ID:
                if (mStatusLayoutManager.emptyDataVs != null) {
                    View view = mStatusLayoutManager.emptyDataVs.inflate();
                    if (mStatusLayoutManager.emptyDataLayout != null)
                        mStatusLayoutManager.emptyDataLayout.setView(view);
                    retryLoad(view, mStatusLayoutManager.emptyDataRetryViewId);
                    layoutSparseArray.put(id, view);
                    isShow = true;
                } else {
                    isShow = false;
                }
                break;
            default:
                break;
        }
        return isShow;
    }

    /**
     * 重试加载
     */
    private void retryLoad(View view, int id) {
        View retryView = view.findViewById(mStatusLayoutManager.retryViewId != 0 ?
                mStatusLayoutManager.retryViewId : id);
        if (retryView == null || mStatusLayoutManager.onRetryListener == null) return;
        retryView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mStatusLayoutManager.onRetryListener.onRetry();
            }
        });
    }

    //设置空布局的 图片和文字
    private void emptyDataViewAddData(int iconImage, String textTip) {
        if (iconImage == 0 && TextUtils.isEmpty(textTip)) return;
        View emptyDataView = layoutSparseArray.get(LAYOUT_EMPTY_DATA_ID);
        View iconImageView = emptyDataView.findViewById(mStatusLayoutManager.emptyDataIconImageId);
        View textView = emptyDataView.findViewById(mStatusLayoutManager.emptyDataTextTipId);
        if (iconImageView != null && iconImageView instanceof ImageView) {
            ((ImageView) iconImageView).setImageResource(iconImage);
        }

        if (textView != null && textView instanceof TextView) {
            ((TextView) textView).setText(textTip);
        }
    }

    /**
     *显示空布局
     * @param objects
     */
    public void showLayoutEmptyData(Object... objects) {
        if (inflateLayout(LAYOUT_EMPTY_DATA_ID)) {
            showHideViewById(LAYOUT_EMPTY_DATA_ID);

            AbsViewStubLayout emptyDataLayout = mStatusLayoutManager.emptyDataLayout;
            if (emptyDataLayout != null) {
                emptyDataLayout.setData(objects);
            }
        }
    }

}
