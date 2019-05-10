package com.gcml.auth.face2.databinding;
import com.gcml.auth.face2.R;
import com.gcml.auth.face2.BR;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
@javax.annotation.Generated("Android Data Binding")
public class FaceActivityBdSignUpBinding extends android.databinding.ViewDataBinding implements android.databinding.generated.callback.OnClickListener.Listener {

    @Nullable
    private static final android.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.sv_preview, 3);
        sViewsWithIds.put(R.id.preview_mask, 4);
        sViewsWithIds.put(R.id.iv_animation, 5);
        sViewsWithIds.put(R.id.iv_tips, 6);
    }
    // views
    @NonNull
    public final android.widget.ImageView ivAnimation;
    @NonNull
    public final android.widget.ImageView ivBack;
    @NonNull
    public final android.widget.TextView ivTips;
    @NonNull
    public final android.widget.ImageView ivWifiState;
    @NonNull
    private final android.support.constraint.ConstraintLayout mboundView0;
    @NonNull
    public final android.view.View previewMask;
    @NonNull
    public final android.view.SurfaceView svPreview;
    // variables
    @Nullable
    private com.gcml.auth.face2.ui.FaceBdSignUpActivity mPresenter;
    @Nullable
    private com.gcml.auth.face2.ui.FaceBdSignUpViewModel mViewModel;
    @Nullable
    private final android.view.View.OnClickListener mCallback43;
    @Nullable
    private final android.view.View.OnClickListener mCallback44;
    // values
    // listeners
    // Inverse Binding Event Handlers

    public FaceActivityBdSignUpBinding(@NonNull android.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        super(bindingComponent, root, 0);
        final Object[] bindings = mapBindings(bindingComponent, root, 7, sIncludes, sViewsWithIds);
        this.ivAnimation = (android.widget.ImageView) bindings[5];
        this.ivBack = (android.widget.ImageView) bindings[1];
        this.ivBack.setTag(null);
        this.ivTips = (android.widget.TextView) bindings[6];
        this.ivWifiState = (android.widget.ImageView) bindings[2];
        this.ivWifiState.setTag(null);
        this.mboundView0 = (android.support.constraint.ConstraintLayout) bindings[0];
        this.mboundView0.setTag(null);
        this.previewMask = (android.view.View) bindings[4];
        this.svPreview = (android.view.SurfaceView) bindings[3];
        setRootTag(root);
        // listeners
        mCallback43 = new android.databinding.generated.callback.OnClickListener(this, 1);
        mCallback44 = new android.databinding.generated.callback.OnClickListener(this, 2);
        invalidateAll();
    }

    @Override
    public void invalidateAll() {
        synchronized(this) {
                mDirtyFlags = 0x4L;
        }
        requestRebind();
    }

    @Override
    public boolean hasPendingBindings() {
        synchronized(this) {
            if (mDirtyFlags != 0) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean setVariable(int variableId, @Nullable Object variable)  {
        boolean variableSet = true;
        if (BR.presenter == variableId) {
            setPresenter((com.gcml.auth.face2.ui.FaceBdSignUpActivity) variable);
        }
        else if (BR.viewModel == variableId) {
            setViewModel((com.gcml.auth.face2.ui.FaceBdSignUpViewModel) variable);
        }
        else {
            variableSet = false;
        }
            return variableSet;
    }

    public void setPresenter(@Nullable com.gcml.auth.face2.ui.FaceBdSignUpActivity Presenter) {
        this.mPresenter = Presenter;
        synchronized(this) {
            mDirtyFlags |= 0x1L;
        }
        notifyPropertyChanged(BR.presenter);
        super.requestRebind();
    }
    @Nullable
    public com.gcml.auth.face2.ui.FaceBdSignUpActivity getPresenter() {
        return mPresenter;
    }
    public void setViewModel(@Nullable com.gcml.auth.face2.ui.FaceBdSignUpViewModel ViewModel) {
        this.mViewModel = ViewModel;
    }
    @Nullable
    public com.gcml.auth.face2.ui.FaceBdSignUpViewModel getViewModel() {
        return mViewModel;
    }

    @Override
    protected boolean onFieldChange(int localFieldId, Object object, int fieldId) {
        switch (localFieldId) {
        }
        return false;
    }

    @Override
    protected void executeBindings() {
        long dirtyFlags = 0;
        synchronized(this) {
            dirtyFlags = mDirtyFlags;
            mDirtyFlags = 0;
        }
        com.gcml.auth.face2.ui.FaceBdSignUpActivity presenter = mPresenter;
        // batch finished
        if ((dirtyFlags & 0x4L) != 0) {
            // api target 1

            this.ivBack.setOnClickListener(mCallback43);
            this.ivWifiState.setOnClickListener(mCallback44);
        }
    }
    // Listener Stub Implementations
    // callback impls
    public final void _internalCallbackOnClick(int sourceId , android.view.View callbackArg_0) {
        switch(sourceId) {
            case 1: {
                // localize variables for thread safety
                // presenter != null
                boolean presenterJavaLangObjectNull = false;
                // presenter
                com.gcml.auth.face2.ui.FaceBdSignUpActivity presenter = mPresenter;



                presenterJavaLangObjectNull = (presenter) != (null);
                if (presenterJavaLangObjectNull) {


                    presenter.goBack();
                }
                break;
            }
            case 2: {
                // localize variables for thread safety
                // presenter != null
                boolean presenterJavaLangObjectNull = false;
                // presenter
                com.gcml.auth.face2.ui.FaceBdSignUpActivity presenter = mPresenter;



                presenterJavaLangObjectNull = (presenter) != (null);
                if (presenterJavaLangObjectNull) {


                    presenter.goWifi();
                }
                break;
            }
        }
    }
    // dirty flag
    private  long mDirtyFlags = 0xffffffffffffffffL;

    @NonNull
    public static FaceActivityBdSignUpBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.view.ViewGroup root, boolean attachToRoot) {
        return inflate(inflater, root, attachToRoot, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static FaceActivityBdSignUpBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.view.ViewGroup root, boolean attachToRoot, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        return android.databinding.DataBindingUtil.<FaceActivityBdSignUpBinding>inflate(inflater, com.gcml.auth.face2.R.layout.face_activity_bd_sign_up, root, attachToRoot, bindingComponent);
    }
    @NonNull
    public static FaceActivityBdSignUpBinding inflate(@NonNull android.view.LayoutInflater inflater) {
        return inflate(inflater, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static FaceActivityBdSignUpBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        return bind(inflater.inflate(com.gcml.auth.face2.R.layout.face_activity_bd_sign_up, null, false), bindingComponent);
    }
    @NonNull
    public static FaceActivityBdSignUpBinding bind(@NonNull android.view.View view) {
        return bind(view, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static FaceActivityBdSignUpBinding bind(@NonNull android.view.View view, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        if (!"layout/face_activity_bd_sign_up_0".equals(view.getTag())) {
            throw new RuntimeException("view tag isn't correct on view:" + view.getTag());
        }
        return new FaceActivityBdSignUpBinding(bindingComponent, view);
    }
    /* flag mapping
        flag 0 (0x1L): presenter
        flag 1 (0x2L): viewModel
        flag 2 (0x3L): null
    flag mapping end*/
    //end
}