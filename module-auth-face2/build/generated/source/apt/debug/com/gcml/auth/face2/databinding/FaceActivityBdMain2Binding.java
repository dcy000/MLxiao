package com.gcml.auth.face2.databinding;
import android.databinding.Bindable;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
@javax.annotation.Generated("Android Data Binding")
public abstract class FaceActivityBdMain2Binding extends ViewDataBinding {
    @NonNull
    public final android.widget.ImageView ivAnimation;
    @NonNull
    public final android.widget.ImageView ivBack;
    @NonNull
    public final android.widget.TextView ivTips;
    @NonNull
    public final android.widget.ImageView ivWifiState;
    @NonNull
    public final android.view.View previewMask;
    @NonNull
    public final android.view.SurfaceView svPreview;
    // variables
    protected com.gcml.auth.face2.ui.FaceBdMainActivity mPresenter;
    protected com.gcml.auth.face2.ui.FaceBdMainViewModel mViewModel;
    protected FaceActivityBdMain2Binding(@Nullable android.databinding.DataBindingComponent bindingComponent, @Nullable android.view.View root_, int localFieldCount
        , android.widget.ImageView ivAnimation1
        , android.widget.ImageView ivBack1
        , android.widget.TextView ivTips1
        , android.widget.ImageView ivWifiState1
        , android.view.View previewMask1
        , android.view.SurfaceView svPreview1
    ) {
        super(bindingComponent, root_, localFieldCount);
        this.ivAnimation = ivAnimation1;
        this.ivBack = ivBack1;
        this.ivTips = ivTips1;
        this.ivWifiState = ivWifiState1;
        this.previewMask = previewMask1;
        this.svPreview = svPreview1;
    }
    //getters and abstract setters
    public abstract void setPresenter(@Nullable com.gcml.auth.face2.ui.FaceBdMainActivity Presenter);
    @Nullable
    public com.gcml.auth.face2.ui.FaceBdMainActivity getPresenter() {
        return mPresenter;
    }
    public abstract void setViewModel(@Nullable com.gcml.auth.face2.ui.FaceBdMainViewModel ViewModel);
    @Nullable
    public com.gcml.auth.face2.ui.FaceBdMainViewModel getViewModel() {
        return mViewModel;
    }
    @NonNull
    public static FaceActivityBdMain2Binding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.view.ViewGroup root, boolean attachToRoot) {
        return inflate(inflater, root, attachToRoot, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static FaceActivityBdMain2Binding inflate(@NonNull android.view.LayoutInflater inflater) {
        return inflate(inflater, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static FaceActivityBdMain2Binding bind(@NonNull android.view.View view) {
        return null;
    }
    @NonNull
    public static FaceActivityBdMain2Binding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.view.ViewGroup root, boolean attachToRoot, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        return null;
    }
    @NonNull
    public static FaceActivityBdMain2Binding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        return null;
    }
    @NonNull
    public static FaceActivityBdMain2Binding bind(@NonNull android.view.View view, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        return null;
    }
}