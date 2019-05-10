package com.gcml.auth.face2.databinding;
import com.gcml.auth.face2.R;
import com.gcml.auth.face2.BR;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
@javax.annotation.Generated("Android Data Binding")
public class FaceActivityBdSignInBinding extends android.databinding.ViewDataBinding implements android.databinding.generated.callback.OnClickListener.Listener {

    @Nullable
    private static final android.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.sv_preview, 4);
        sViewsWithIds.put(R.id.preview_mask, 5);
        sViewsWithIds.put(R.id.iv_animation, 6);
        sViewsWithIds.put(R.id.iv_tips, 7);
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
    @NonNull
    public final android.widget.TextView tvSkip;
    // variables
    @Nullable
    private com.gcml.auth.face2.ui.FaceBdSignInActivity mPresenter;
    @Nullable
    private com.gcml.auth.face2.ui.FaceBdSignInViewModel mViewModel;
    @Nullable
    private final android.view.View.OnClickListener mCallback15;
    @Nullable
    private final android.view.View.OnClickListener mCallback16;
    @Nullable
    private final android.view.View.OnClickListener mCallback14;
    // values
    // listeners
    // Inverse Binding Event Handlers

    public FaceActivityBdSignInBinding(@NonNull android.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        super(bindingComponent, root, 0);
        final Object[] bindings = mapBindings(bindingComponent, root, 8, sIncludes, sViewsWithIds);
        this.ivAnimation = (android.widget.ImageView) bindings[6];
        this.ivBack = (android.widget.ImageView) bindings[1];
        this.ivBack.setTag(null);
        this.ivTips = (android.widget.TextView) bindings[7];
        this.ivWifiState = (android.widget.ImageView) bindings[2];
        this.ivWifiState.setTag(null);
        this.mboundView0 = (android.support.constraint.ConstraintLayout) bindings[0];
        this.mboundView0.setTag(null);
        this.previewMask = (android.view.View) bindings[5];
        this.svPreview = (android.view.SurfaceView) bindings[4];
        this.tvSkip = (android.widget.TextView) bindings[3];
        this.tvSkip.setTag(null);
        setRootTag(root);
        // listeners
        mCallback15 = new android.databinding.generated.callback.OnClickListener(this, 2);
        mCallback16 = new android.databinding.generated.callback.OnClickListener(this, 3);
        mCallback14 = new android.databinding.generated.callback.OnClickListener(this, 1);
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
            setPresenter((com.gcml.auth.face2.ui.FaceBdSignInActivity) variable);
        }
        else if (BR.viewModel == variableId) {
            setViewModel((com.gcml.auth.face2.ui.FaceBdSignInViewModel) variable);
        }
        else {
            variableSet = false;
        }
            return variableSet;
    }

    public void setPresenter(@Nullable com.gcml.auth.face2.ui.FaceBdSignInActivity Presenter) {
        this.mPresenter = Presenter;
        synchronized(this) {
            mDirtyFlags |= 0x1L;
        }
        notifyPropertyChanged(BR.presenter);
        super.requestRebind();
    }
    @Nullable
    public com.gcml.auth.face2.ui.FaceBdSignInActivity getPresenter() {
        return mPresenter;
    }
    public void setViewModel(@Nullable com.gcml.auth.face2.ui.FaceBdSignInViewModel ViewModel) {
        this.mViewModel = ViewModel;
    }
    @Nullable
    public com.gcml.auth.face2.ui.FaceBdSignInViewModel getViewModel() {
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
        com.gcml.auth.face2.ui.FaceBdSignInActivity presenter = mPresenter;
        // batch finished
        if ((dirtyFlags & 0x4L) != 0) {
            // api target 1

            this.ivBack.setOnClickListener(mCallback14);
            this.ivWifiState.setOnClickListener(mCallback15);
            this.tvSkip.setOnClickListener(mCallback16);
        }
    }
    // Listener Stub Implementations
    // callback impls
    public final void _internalCallbackOnClick(int sourceId , android.view.View callbackArg_0) {
        switch(sourceId) {
            case 2: {
                // localize variables for thread safety
                // presenter != null
                boolean presenterJavaLangObjectNull = false;
                // presenter
                com.gcml.auth.face2.ui.FaceBdSignInActivity presenter = mPresenter;



                presenterJavaLangObjectNull = (presenter) != (null);
                if (presenterJavaLangObjectNull) {


                    presenter.goWifi();
                }
                break;
            }
            case 3: {
                // localize variables for thread safety
                // presenter != null
                boolean presenterJavaLangObjectNull = false;
                // presenter
                com.gcml.auth.face2.ui.FaceBdSignInActivity presenter = mPresenter;



                presenterJavaLangObjectNull = (presenter) != (null);
                if (presenterJavaLangObjectNull) {


                    presenter.skip();
                }
                break;
            }
            case 1: {
                // localize variables for thread safety
                // presenter != null
                boolean presenterJavaLangObjectNull = false;
                // presenter
                com.gcml.auth.face2.ui.FaceBdSignInActivity presenter = mPresenter;



                presenterJavaLangObjectNull = (presenter) != (null);
                if (presenterJavaLangObjectNull) {


                    presenter.goBack();
                }
                break;
            }
        }
    }
    // dirty flag
    private  long mDirtyFlags = 0xffffffffffffffffL;

    @NonNull
    public static FaceActivityBdSignInBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.view.ViewGroup root, boolean attachToRoot) {
        return inflate(inflater, root, attachToRoot, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static FaceActivityBdSignInBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.view.ViewGroup root, boolean attachToRoot, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        return android.databinding.DataBindingUtil.<FaceActivityBdSignInBinding>inflate(inflater, com.gcml.auth.face2.R.layout.face_activity_bd_sign_in, root, attachToRoot, bindingComponent);
    }
    @NonNull
    public static FaceActivityBdSignInBinding inflate(@NonNull android.view.LayoutInflater inflater) {
        return inflate(inflater, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static FaceActivityBdSignInBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        return bind(inflater.inflate(com.gcml.auth.face2.R.layout.face_activity_bd_sign_in, null, false), bindingComponent);
    }
    @NonNull
    public static FaceActivityBdSignInBinding bind(@NonNull android.view.View view) {
        return bind(view, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static FaceActivityBdSignInBinding bind(@NonNull android.view.View view, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        if (!"layout/face_activity_bd_sign_in_0".equals(view.getTag())) {
            throw new RuntimeException("view tag isn't correct on view:" + view.getTag());
        }
        return new FaceActivityBdSignInBinding(bindingComponent, view);
    }
    /* flag mapping
        flag 0 (0x1L): presenter
        flag 1 (0x2L): viewModel
        flag 2 (0x3L): null
    flag mapping end*/
    //end
}