package com.gcml.auth.databinding;
import com.gcml.auth.R;
import com.gcml.auth.BR;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
@javax.annotation.Generated("Android Data Binding")
public class AuthActivityProfile2Binding extends android.databinding.ViewDataBinding implements android.databinding.generated.callback.OnClickListener.Listener {

    @Nullable
    private static final android.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.tb_simple_profile, 2);
        sViewsWithIds.put(R.id.tv_height, 3);
        sViewsWithIds.put(R.id.sp_height, 4);
        sViewsWithIds.put(R.id.tv_wc, 5);
        sViewsWithIds.put(R.id.sp_wc, 6);
        sViewsWithIds.put(R.id.tv_weight, 7);
        sViewsWithIds.put(R.id.sp_weight, 8);
        sViewsWithIds.put(R.id.iv_decorator_bottom, 9);
    }
    // views
    @NonNull
    public final android.widget.ImageView ivDecoratorBottom;
    @NonNull
    private final android.support.constraint.ConstraintLayout mboundView0;
    @NonNull
    public final android.widget.TextView spHeight;
    @NonNull
    public final android.widget.TextView spWc;
    @NonNull
    public final android.widget.TextView spWeight;
    @NonNull
    public final com.gcml.common.widget.toolbar.TranslucentToolBar tbSimpleProfile;
    @NonNull
    public final android.widget.TextView tvHeight;
    @NonNull
    public final android.widget.TextView tvNext;
    @NonNull
    public final android.widget.TextView tvWc;
    @NonNull
    public final android.widget.TextView tvWeight;
    // variables
    @Nullable
    private com.gcml.auth.ui.profile.Profile2Activity mPresenter;
    @Nullable
    private com.gcml.auth.ui.profile.Profile2ViewModel mViewModel;
    @Nullable
    private final android.view.View.OnClickListener mCallback35;
    // values
    // listeners
    // Inverse Binding Event Handlers

    public AuthActivityProfile2Binding(@NonNull android.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        super(bindingComponent, root, 0);
        final Object[] bindings = mapBindings(bindingComponent, root, 10, sIncludes, sViewsWithIds);
        this.ivDecoratorBottom = (android.widget.ImageView) bindings[9];
        this.mboundView0 = (android.support.constraint.ConstraintLayout) bindings[0];
        this.mboundView0.setTag(null);
        this.spHeight = (android.widget.TextView) bindings[4];
        this.spWc = (android.widget.TextView) bindings[6];
        this.spWeight = (android.widget.TextView) bindings[8];
        this.tbSimpleProfile = (com.gcml.common.widget.toolbar.TranslucentToolBar) bindings[2];
        this.tvHeight = (android.widget.TextView) bindings[3];
        this.tvNext = (android.widget.TextView) bindings[1];
        this.tvNext.setTag(null);
        this.tvWc = (android.widget.TextView) bindings[5];
        this.tvWeight = (android.widget.TextView) bindings[7];
        setRootTag(root);
        // listeners
        mCallback35 = new android.databinding.generated.callback.OnClickListener(this, 1);
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
            setPresenter((com.gcml.auth.ui.profile.Profile2Activity) variable);
        }
        else if (BR.viewModel == variableId) {
            setViewModel((com.gcml.auth.ui.profile.Profile2ViewModel) variable);
        }
        else {
            variableSet = false;
        }
            return variableSet;
    }

    public void setPresenter(@Nullable com.gcml.auth.ui.profile.Profile2Activity Presenter) {
        this.mPresenter = Presenter;
        synchronized(this) {
            mDirtyFlags |= 0x1L;
        }
        notifyPropertyChanged(BR.presenter);
        super.requestRebind();
    }
    @Nullable
    public com.gcml.auth.ui.profile.Profile2Activity getPresenter() {
        return mPresenter;
    }
    public void setViewModel(@Nullable com.gcml.auth.ui.profile.Profile2ViewModel ViewModel) {
        this.mViewModel = ViewModel;
    }
    @Nullable
    public com.gcml.auth.ui.profile.Profile2ViewModel getViewModel() {
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
        com.gcml.auth.ui.profile.Profile2Activity presenter = mPresenter;
        // batch finished
        if ((dirtyFlags & 0x4L) != 0) {
            // api target 1

            this.tvNext.setOnClickListener(mCallback35);
        }
    }
    // Listener Stub Implementations
    // callback impls
    public final void _internalCallbackOnClick(int sourceId , android.view.View callbackArg_0) {
        // localize variables for thread safety
        // presenter != null
        boolean presenterJavaLangObjectNull = false;
        // presenter
        com.gcml.auth.ui.profile.Profile2Activity presenter = mPresenter;



        presenterJavaLangObjectNull = (presenter) != (null);
        if (presenterJavaLangObjectNull) {


            presenter.goNext();
        }
    }
    // dirty flag
    private  long mDirtyFlags = 0xffffffffffffffffL;

    @NonNull
    public static AuthActivityProfile2Binding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.view.ViewGroup root, boolean attachToRoot) {
        return inflate(inflater, root, attachToRoot, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static AuthActivityProfile2Binding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.view.ViewGroup root, boolean attachToRoot, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        return android.databinding.DataBindingUtil.<AuthActivityProfile2Binding>inflate(inflater, com.gcml.auth.R.layout.auth_activity_profile2, root, attachToRoot, bindingComponent);
    }
    @NonNull
    public static AuthActivityProfile2Binding inflate(@NonNull android.view.LayoutInflater inflater) {
        return inflate(inflater, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static AuthActivityProfile2Binding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        return bind(inflater.inflate(com.gcml.auth.R.layout.auth_activity_profile2, null, false), bindingComponent);
    }
    @NonNull
    public static AuthActivityProfile2Binding bind(@NonNull android.view.View view) {
        return bind(view, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static AuthActivityProfile2Binding bind(@NonNull android.view.View view, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        if (!"layout/auth_activity_profile2_0".equals(view.getTag())) {
            throw new RuntimeException("view tag isn't correct on view:" + view.getTag());
        }
        return new AuthActivityProfile2Binding(bindingComponent, view);
    }
    /* flag mapping
        flag 0 (0x1L): presenter
        flag 1 (0x2L): viewModel
        flag 2 (0x3L): null
    flag mapping end*/
    //end
}