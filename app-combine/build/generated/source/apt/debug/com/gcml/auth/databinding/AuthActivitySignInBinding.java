package com.gcml.auth.databinding;
import com.gcml.auth.R;
import com.gcml.auth.BR;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
@javax.annotation.Generated("Android Data Binding")
public class AuthActivitySignInBinding extends android.databinding.ViewDataBinding implements android.databinding.generated.callback.OnClickListener.Listener {

    @Nullable
    private static final android.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.cl_auth_items, 7);
        sViewsWithIds.put(R.id.et_phone, 8);
        sViewsWithIds.put(R.id.et_password, 9);
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
    public final android.widget.EditText etPassword;
    @NonNull
    public final android.widget.EditText etPhone;
    @NonNull
    public final android.widget.ImageView ivBack;
    @NonNull
    public final android.widget.ImageView ivWifiState;
    @NonNull
    private final android.support.constraint.ConstraintLayout mboundView0;
    @NonNull
    public final android.widget.TextView tvAgree;
    @NonNull
    public final android.widget.TextView tvAppVersion;
    @NonNull
    public final android.widget.TextView tvCopyright;
    @NonNull
    public final android.widget.TextView tvFaceAuth;
    @NonNull
    public final android.widget.TextView tvForgetPassword;
    @NonNull
    public final android.widget.TextView tvProtocol;
    @NonNull
    public final android.widget.TextView tvSignIn;
    // variables
    @Nullable
    private com.gcml.auth.ui.signin.SignInActivity mPresenter;
    @Nullable
    private com.gcml.auth.ui.signin.SignInViewModel mSignInViewModel;
    @Nullable
    private final android.view.View.OnClickListener mCallback39;
    @Nullable
    private final android.view.View.OnClickListener mCallback42;
    @Nullable
    private final android.view.View.OnClickListener mCallback37;
    @Nullable
    private final android.view.View.OnClickListener mCallback40;
    @Nullable
    private final android.view.View.OnClickListener mCallback38;
    @Nullable
    private final android.view.View.OnClickListener mCallback41;
    @Nullable
    private final android.view.View.OnClickListener mCallback36;
    // values
    // listeners
    // Inverse Binding Event Handlers

    public AuthActivitySignInBinding(@NonNull android.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        super(bindingComponent, root, 0);
        final Object[] bindings = mapBindings(bindingComponent, root, 14, sIncludes, sViewsWithIds);
        this.cbAgreeProtocol = (android.widget.CheckBox) bindings[10];
        this.clAuthItems = (android.support.constraint.ConstraintLayout) bindings[7];
        this.etPassword = (android.widget.EditText) bindings[9];
        this.etPhone = (android.widget.EditText) bindings[8];
        this.ivBack = (android.widget.ImageView) bindings[4];
        this.ivBack.setTag(null);
        this.ivWifiState = (android.widget.ImageView) bindings[5];
        this.ivWifiState.setTag(null);
        this.mboundView0 = (android.support.constraint.ConstraintLayout) bindings[0];
        this.mboundView0.setTag(null);
        this.tvAgree = (android.widget.TextView) bindings[11];
        this.tvAppVersion = (android.widget.TextView) bindings[13];
        this.tvCopyright = (android.widget.TextView) bindings[12];
        this.tvFaceAuth = (android.widget.TextView) bindings[2];
        this.tvFaceAuth.setTag(null);
        this.tvForgetPassword = (android.widget.TextView) bindings[3];
        this.tvForgetPassword.setTag(null);
        this.tvProtocol = (android.widget.TextView) bindings[6];
        this.tvProtocol.setTag(null);
        this.tvSignIn = (android.widget.TextView) bindings[1];
        this.tvSignIn.setTag(null);
        setRootTag(root);
        // listeners
        mCallback39 = new android.databinding.generated.callback.OnClickListener(this, 4);
        mCallback42 = new android.databinding.generated.callback.OnClickListener(this, 7);
        mCallback37 = new android.databinding.generated.callback.OnClickListener(this, 2);
        mCallback40 = new android.databinding.generated.callback.OnClickListener(this, 5);
        mCallback38 = new android.databinding.generated.callback.OnClickListener(this, 3);
        mCallback41 = new android.databinding.generated.callback.OnClickListener(this, 6);
        mCallback36 = new android.databinding.generated.callback.OnClickListener(this, 1);
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
            setPresenter((com.gcml.auth.ui.signin.SignInActivity) variable);
        }
        else if (BR.signInViewModel == variableId) {
            setSignInViewModel((com.gcml.auth.ui.signin.SignInViewModel) variable);
        }
        else {
            variableSet = false;
        }
            return variableSet;
    }

    public void setPresenter(@Nullable com.gcml.auth.ui.signin.SignInActivity Presenter) {
        this.mPresenter = Presenter;
        synchronized(this) {
            mDirtyFlags |= 0x1L;
        }
        notifyPropertyChanged(BR.presenter);
        super.requestRebind();
    }
    @Nullable
    public com.gcml.auth.ui.signin.SignInActivity getPresenter() {
        return mPresenter;
    }
    public void setSignInViewModel(@Nullable com.gcml.auth.ui.signin.SignInViewModel SignInViewModel) {
        this.mSignInViewModel = SignInViewModel;
    }
    @Nullable
    public com.gcml.auth.ui.signin.SignInViewModel getSignInViewModel() {
        return mSignInViewModel;
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
        com.gcml.auth.ui.signin.SignInActivity presenter = mPresenter;
        // batch finished
        if ((dirtyFlags & 0x4L) != 0) {
            // api target 1

            this.ivBack.setOnClickListener(mCallback40);
            this.ivWifiState.setOnClickListener(mCallback41);
            this.mboundView0.setOnClickListener(mCallback36);
            this.tvFaceAuth.setOnClickListener(mCallback38);
            this.tvForgetPassword.setOnClickListener(mCallback39);
            this.tvProtocol.setOnClickListener(mCallback42);
            this.tvSignIn.setOnClickListener(mCallback37);
        }
    }
    // Listener Stub Implementations
    // callback impls
    public final void _internalCallbackOnClick(int sourceId , android.view.View callbackArg_0) {
        switch(sourceId) {
            case 4: {
                // localize variables for thread safety
                // presenter != null
                boolean presenterJavaLangObjectNull = false;
                // presenter
                com.gcml.auth.ui.signin.SignInActivity presenter = mPresenter;



                presenterJavaLangObjectNull = (presenter) != (null);
                if (presenterJavaLangObjectNull) {


                    presenter.goForgetPassword();
                }
                break;
            }
            case 7: {
                // localize variables for thread safety
                // presenter != null
                boolean presenterJavaLangObjectNull = false;
                // presenter
                com.gcml.auth.ui.signin.SignInActivity presenter = mPresenter;



                presenterJavaLangObjectNull = (presenter) != (null);
                if (presenterJavaLangObjectNull) {


                    presenter.goUserProtocol();
                }
                break;
            }
            case 2: {
                // localize variables for thread safety
                // presenter != null
                boolean presenterJavaLangObjectNull = false;
                // presenter
                com.gcml.auth.ui.signin.SignInActivity presenter = mPresenter;



                presenterJavaLangObjectNull = (presenter) != (null);
                if (presenterJavaLangObjectNull) {


                    presenter.signIn();
                }
                break;
            }
            case 5: {
                // localize variables for thread safety
                // presenter != null
                boolean presenterJavaLangObjectNull = false;
                // presenter
                com.gcml.auth.ui.signin.SignInActivity presenter = mPresenter;



                presenterJavaLangObjectNull = (presenter) != (null);
                if (presenterJavaLangObjectNull) {


                    presenter.goBack();
                }
                break;
            }
            case 3: {
                // localize variables for thread safety
                // presenter != null
                boolean presenterJavaLangObjectNull = false;
                // presenter
                com.gcml.auth.ui.signin.SignInActivity presenter = mPresenter;



                presenterJavaLangObjectNull = (presenter) != (null);
                if (presenterJavaLangObjectNull) {


                    presenter.goSignInByFace();
                }
                break;
            }
            case 6: {
                // localize variables for thread safety
                // presenter != null
                boolean presenterJavaLangObjectNull = false;
                // presenter
                com.gcml.auth.ui.signin.SignInActivity presenter = mPresenter;



                presenterJavaLangObjectNull = (presenter) != (null);
                if (presenterJavaLangObjectNull) {


                    presenter.goWifi();
                }
                break;
            }
            case 1: {
                // localize variables for thread safety
                // presenter != null
                boolean presenterJavaLangObjectNull = false;
                // presenter
                com.gcml.auth.ui.signin.SignInActivity presenter = mPresenter;



                presenterJavaLangObjectNull = (presenter) != (null);
                if (presenterJavaLangObjectNull) {


                    presenter.rootOnClick();
                }
                break;
            }
        }
    }
    // dirty flag
    private  long mDirtyFlags = 0xffffffffffffffffL;

    @NonNull
    public static AuthActivitySignInBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.view.ViewGroup root, boolean attachToRoot) {
        return inflate(inflater, root, attachToRoot, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static AuthActivitySignInBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.view.ViewGroup root, boolean attachToRoot, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        return android.databinding.DataBindingUtil.<AuthActivitySignInBinding>inflate(inflater, com.gcml.auth.R.layout.auth_activity_sign_in, root, attachToRoot, bindingComponent);
    }
    @NonNull
    public static AuthActivitySignInBinding inflate(@NonNull android.view.LayoutInflater inflater) {
        return inflate(inflater, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static AuthActivitySignInBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        return bind(inflater.inflate(com.gcml.auth.R.layout.auth_activity_sign_in, null, false), bindingComponent);
    }
    @NonNull
    public static AuthActivitySignInBinding bind(@NonNull android.view.View view) {
        return bind(view, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static AuthActivitySignInBinding bind(@NonNull android.view.View view, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        if (!"layout/auth_activity_sign_in_0".equals(view.getTag())) {
            throw new RuntimeException("view tag isn't correct on view:" + view.getTag());
        }
        return new AuthActivitySignInBinding(bindingComponent, view);
    }
    /* flag mapping
        flag 0 (0x1L): presenter
        flag 1 (0x2L): signInViewModel
        flag 2 (0x3L): null
    flag mapping end*/
    //end
}