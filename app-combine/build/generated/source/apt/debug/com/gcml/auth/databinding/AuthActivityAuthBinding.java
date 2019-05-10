package com.gcml.auth.databinding;
import com.gcml.auth.R;
import com.gcml.auth.BR;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
@javax.annotation.Generated("Android Data Binding")
public class AuthActivityAuthBinding extends android.databinding.ViewDataBinding implements android.databinding.generated.callback.OnClickListener.Listener {

    @Nullable
    private static final android.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.cl_auth_items, 7);
        sViewsWithIds.put(R.id.tv_choose_sign_in, 8);
        sViewsWithIds.put(R.id.tv_no_account, 9);
        sViewsWithIds.put(R.id.cb_agree_protocol, 10);
        sViewsWithIds.put(R.id.tv_agree, 11);
        sViewsWithIds.put(R.id.tv_copyright, 12);
        sViewsWithIds.put(R.id.tv_app_version, 13);
    }
    // views
    @NonNull
    public final android.widget.CheckBox cbAgreeProtocol;
    @NonNull
    public final android.support.constraint.ConstraintLayout clAuthItems;
    @NonNull
    public final android.widget.ImageView ivWifiState;
    @NonNull
    private final android.support.constraint.ConstraintLayout mboundView0;
    @NonNull
    private final android.widget.TextView mboundView2;
    @NonNull
    private final android.widget.TextView mboundView3;
    @NonNull
    public final android.widget.TextView tvAgree;
    @NonNull
    public final android.widget.TextView tvAppVersion;
    @NonNull
    public final android.widget.TextView tvChooseSignIn;
    @NonNull
    public final android.widget.TextView tvCopyright;
    @NonNull
    public final android.widget.TextView tvNetworkMode;
    @NonNull
    public final android.widget.TextView tvNoAccount;
    @NonNull
    public final android.widget.TextView tvProtocol;
    @NonNull
    public final android.widget.TextView tvSignUp;
    // variables
    @Nullable
    private com.gcml.auth.ui.AuthActivity mPresenter;
    @Nullable
    private com.gcml.auth.ui.AuthViewModel mAuthViewModel;
    @Nullable
    private final android.view.View.OnClickListener mCallback9;
    @Nullable
    private final android.view.View.OnClickListener mCallback13;
    @Nullable
    private final android.view.View.OnClickListener mCallback8;
    @Nullable
    private final android.view.View.OnClickListener mCallback11;
    @Nullable
    private final android.view.View.OnClickListener mCallback12;
    @Nullable
    private final android.view.View.OnClickListener mCallback10;
    // values
    // listeners
    // Inverse Binding Event Handlers

    public AuthActivityAuthBinding(@NonNull android.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        super(bindingComponent, root, 0);
        final Object[] bindings = mapBindings(bindingComponent, root, 14, sIncludes, sViewsWithIds);
        this.cbAgreeProtocol = (android.widget.CheckBox) bindings[10];
        this.clAuthItems = (android.support.constraint.ConstraintLayout) bindings[7];
        this.ivWifiState = (android.widget.ImageView) bindings[5];
        this.ivWifiState.setTag(null);
        this.mboundView0 = (android.support.constraint.ConstraintLayout) bindings[0];
        this.mboundView0.setTag(null);
        this.mboundView2 = (android.widget.TextView) bindings[2];
        this.mboundView2.setTag(null);
        this.mboundView3 = (android.widget.TextView) bindings[3];
        this.mboundView3.setTag(null);
        this.tvAgree = (android.widget.TextView) bindings[11];
        this.tvAppVersion = (android.widget.TextView) bindings[13];
        this.tvChooseSignIn = (android.widget.TextView) bindings[8];
        this.tvCopyright = (android.widget.TextView) bindings[12];
        this.tvNetworkMode = (android.widget.TextView) bindings[4];
        this.tvNetworkMode.setTag(null);
        this.tvNoAccount = (android.widget.TextView) bindings[9];
        this.tvProtocol = (android.widget.TextView) bindings[6];
        this.tvProtocol.setTag(null);
        this.tvSignUp = (android.widget.TextView) bindings[1];
        this.tvSignUp.setTag(null);
        setRootTag(root);
        // listeners
        mCallback9 = new android.databinding.generated.callback.OnClickListener(this, 2);
        mCallback13 = new android.databinding.generated.callback.OnClickListener(this, 6);
        mCallback8 = new android.databinding.generated.callback.OnClickListener(this, 1);
        mCallback11 = new android.databinding.generated.callback.OnClickListener(this, 4);
        mCallback12 = new android.databinding.generated.callback.OnClickListener(this, 5);
        mCallback10 = new android.databinding.generated.callback.OnClickListener(this, 3);
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
            setPresenter((com.gcml.auth.ui.AuthActivity) variable);
        }
        else if (BR.authViewModel == variableId) {
            setAuthViewModel((com.gcml.auth.ui.AuthViewModel) variable);
        }
        else {
            variableSet = false;
        }
            return variableSet;
    }

    public void setPresenter(@Nullable com.gcml.auth.ui.AuthActivity Presenter) {
        this.mPresenter = Presenter;
        synchronized(this) {
            mDirtyFlags |= 0x1L;
        }
        notifyPropertyChanged(BR.presenter);
        super.requestRebind();
    }
    @Nullable
    public com.gcml.auth.ui.AuthActivity getPresenter() {
        return mPresenter;
    }
    public void setAuthViewModel(@Nullable com.gcml.auth.ui.AuthViewModel AuthViewModel) {
        this.mAuthViewModel = AuthViewModel;
    }
    @Nullable
    public com.gcml.auth.ui.AuthViewModel getAuthViewModel() {
        return mAuthViewModel;
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
        com.gcml.auth.ui.AuthActivity presenter = mPresenter;
        // batch finished
        if ((dirtyFlags & 0x4L) != 0) {
            // api target 1

            this.ivWifiState.setOnClickListener(mCallback12);
            this.mboundView2.setOnClickListener(mCallback9);
            this.mboundView3.setOnClickListener(mCallback10);
            this.tvNetworkMode.setOnClickListener(mCallback11);
            this.tvProtocol.setOnClickListener(mCallback13);
            this.tvSignUp.setOnClickListener(mCallback8);
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
                com.gcml.auth.ui.AuthActivity presenter = mPresenter;



                presenterJavaLangObjectNull = (presenter) != (null);
                if (presenterJavaLangObjectNull) {


                    presenter.goSignInByPhone();
                }
                break;
            }
            case 6: {
                // localize variables for thread safety
                // presenter != null
                boolean presenterJavaLangObjectNull = false;
                // presenter
                com.gcml.auth.ui.AuthActivity presenter = mPresenter;



                presenterJavaLangObjectNull = (presenter) != (null);
                if (presenterJavaLangObjectNull) {


                    presenter.goUserProtocol();
                }
                break;
            }
            case 1: {
                // localize variables for thread safety
                // presenter != null
                boolean presenterJavaLangObjectNull = false;
                // presenter
                com.gcml.auth.ui.AuthActivity presenter = mPresenter;



                presenterJavaLangObjectNull = (presenter) != (null);
                if (presenterJavaLangObjectNull) {


                    presenter.goSignUp();
                }
                break;
            }
            case 4: {
                // localize variables for thread safety
                // presenter != null
                boolean presenterJavaLangObjectNull = false;
                // presenter
                com.gcml.auth.ui.AuthActivity presenter = mPresenter;



                presenterJavaLangObjectNull = (presenter) != (null);
                if (presenterJavaLangObjectNull) {


                    presenter.goNoNetwork();
                }
                break;
            }
            case 5: {
                // localize variables for thread safety
                // presenter != null
                boolean presenterJavaLangObjectNull = false;
                // presenter
                com.gcml.auth.ui.AuthActivity presenter = mPresenter;



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
                com.gcml.auth.ui.AuthActivity presenter = mPresenter;



                presenterJavaLangObjectNull = (presenter) != (null);
                if (presenterJavaLangObjectNull) {


                    presenter.goSignInByFace();
                }
                break;
            }
        }
    }
    // dirty flag
    private  long mDirtyFlags = 0xffffffffffffffffL;

    @NonNull
    public static AuthActivityAuthBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.view.ViewGroup root, boolean attachToRoot) {
        return inflate(inflater, root, attachToRoot, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static AuthActivityAuthBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.view.ViewGroup root, boolean attachToRoot, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        return android.databinding.DataBindingUtil.<AuthActivityAuthBinding>inflate(inflater, com.gcml.auth.R.layout.auth_activity_auth, root, attachToRoot, bindingComponent);
    }
    @NonNull
    public static AuthActivityAuthBinding inflate(@NonNull android.view.LayoutInflater inflater) {
        return inflate(inflater, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static AuthActivityAuthBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        return bind(inflater.inflate(com.gcml.auth.R.layout.auth_activity_auth, null, false), bindingComponent);
    }
    @NonNull
    public static AuthActivityAuthBinding bind(@NonNull android.view.View view) {
        return bind(view, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static AuthActivityAuthBinding bind(@NonNull android.view.View view, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        if (!"layout/auth_activity_auth_0".equals(view.getTag())) {
            throw new RuntimeException("view tag isn't correct on view:" + view.getTag());
        }
        return new AuthActivityAuthBinding(bindingComponent, view);
    }
    /* flag mapping
        flag 0 (0x1L): presenter
        flag 1 (0x2L): authViewModel
        flag 2 (0x3L): null
    flag mapping end*/
    //end
}