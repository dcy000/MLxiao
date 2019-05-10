package com.gcml.auth.databinding;
import com.gcml.auth.R;
import com.gcml.auth.BR;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
@javax.annotation.Generated("Android Data Binding")
public class AuthActivitySignUpByIdCardBinding extends android.databinding.ViewDataBinding implements android.databinding.generated.callback.OnClickListener.Listener {

    @Nullable
    private static final android.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.cl_auth_items, 7);
        sViewsWithIds.put(R.id.tv_title, 8);
        sViewsWithIds.put(R.id.et_phone, 9);
        sViewsWithIds.put(R.id.et_code, 10);
        sViewsWithIds.put(R.id.et_password, 11);
        sViewsWithIds.put(R.id.cb_agree_protocol, 12);
        sViewsWithIds.put(R.id.tv_agree, 13);
    }
    // views
    @NonNull
    public final android.widget.CheckBox cbAgreeProtocol;
    @NonNull
    public final android.support.constraint.ConstraintLayout clAuthItems;
    @NonNull
    public final android.widget.EditText etCode;
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
    public final android.widget.TextView tvCode;
    @NonNull
    public final android.widget.TextView tvNext;
    @NonNull
    public final android.widget.TextView tvNoPhone;
    @NonNull
    public final android.widget.TextView tvProtocol;
    @NonNull
    public final android.widget.TextView tvTitle;
    // variables
    @Nullable
    private com.gcml.auth.ui.signup.SignUpByIdCardActivity mPresenter;
    @Nullable
    private com.gcml.auth.ui.signup.SignUpByIdCardViewModel mViewModel;
    @Nullable
    private final android.view.View.OnClickListener mCallback30;
    @Nullable
    private final android.view.View.OnClickListener mCallback28;
    @Nullable
    private final android.view.View.OnClickListener mCallback32;
    @Nullable
    private final android.view.View.OnClickListener mCallback31;
    @Nullable
    private final android.view.View.OnClickListener mCallback33;
    @Nullable
    private final android.view.View.OnClickListener mCallback29;
    @Nullable
    private final android.view.View.OnClickListener mCallback34;
    // values
    // listeners
    // Inverse Binding Event Handlers

    public AuthActivitySignUpByIdCardBinding(@NonNull android.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        super(bindingComponent, root, 0);
        final Object[] bindings = mapBindings(bindingComponent, root, 14, sIncludes, sViewsWithIds);
        this.cbAgreeProtocol = (android.widget.CheckBox) bindings[12];
        this.clAuthItems = (android.support.constraint.ConstraintLayout) bindings[7];
        this.etCode = (android.widget.EditText) bindings[10];
        this.etPassword = (android.widget.EditText) bindings[11];
        this.etPhone = (android.widget.EditText) bindings[9];
        this.ivBack = (android.widget.ImageView) bindings[5];
        this.ivBack.setTag(null);
        this.ivWifiState = (android.widget.ImageView) bindings[6];
        this.ivWifiState.setTag(null);
        this.mboundView0 = (android.support.constraint.ConstraintLayout) bindings[0];
        this.mboundView0.setTag(null);
        this.tvAgree = (android.widget.TextView) bindings[13];
        this.tvCode = (android.widget.TextView) bindings[1];
        this.tvCode.setTag(null);
        this.tvNext = (android.widget.TextView) bindings[2];
        this.tvNext.setTag(null);
        this.tvNoPhone = (android.widget.TextView) bindings[4];
        this.tvNoPhone.setTag(null);
        this.tvProtocol = (android.widget.TextView) bindings[3];
        this.tvProtocol.setTag(null);
        this.tvTitle = (android.widget.TextView) bindings[8];
        setRootTag(root);
        // listeners
        mCallback30 = new android.databinding.generated.callback.OnClickListener(this, 3);
        mCallback28 = new android.databinding.generated.callback.OnClickListener(this, 1);
        mCallback32 = new android.databinding.generated.callback.OnClickListener(this, 5);
        mCallback31 = new android.databinding.generated.callback.OnClickListener(this, 4);
        mCallback33 = new android.databinding.generated.callback.OnClickListener(this, 6);
        mCallback29 = new android.databinding.generated.callback.OnClickListener(this, 2);
        mCallback34 = new android.databinding.generated.callback.OnClickListener(this, 7);
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
            setPresenter((com.gcml.auth.ui.signup.SignUpByIdCardActivity) variable);
        }
        else if (BR.viewModel == variableId) {
            setViewModel((com.gcml.auth.ui.signup.SignUpByIdCardViewModel) variable);
        }
        else {
            variableSet = false;
        }
            return variableSet;
    }

    public void setPresenter(@Nullable com.gcml.auth.ui.signup.SignUpByIdCardActivity Presenter) {
        this.mPresenter = Presenter;
        synchronized(this) {
            mDirtyFlags |= 0x1L;
        }
        notifyPropertyChanged(BR.presenter);
        super.requestRebind();
    }
    @Nullable
    public com.gcml.auth.ui.signup.SignUpByIdCardActivity getPresenter() {
        return mPresenter;
    }
    public void setViewModel(@Nullable com.gcml.auth.ui.signup.SignUpByIdCardViewModel ViewModel) {
        this.mViewModel = ViewModel;
    }
    @Nullable
    public com.gcml.auth.ui.signup.SignUpByIdCardViewModel getViewModel() {
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
        com.gcml.auth.ui.signup.SignUpByIdCardActivity presenter = mPresenter;
        // batch finished
        if ((dirtyFlags & 0x4L) != 0) {
            // api target 1

            this.ivBack.setOnClickListener(mCallback33);
            this.ivWifiState.setOnClickListener(mCallback34);
            this.mboundView0.setOnClickListener(mCallback28);
            this.tvCode.setOnClickListener(mCallback29);
            this.tvNext.setOnClickListener(mCallback30);
            this.tvNoPhone.setOnClickListener(mCallback32);
            this.tvProtocol.setOnClickListener(mCallback31);
        }
    }
    // Listener Stub Implementations
    // callback impls
    public final void _internalCallbackOnClick(int sourceId , android.view.View callbackArg_0) {
        switch(sourceId) {
            case 3: {
                // localize variables for thread safety
                // presenter != null
                boolean presenterJavaLangObjectNull = false;
                // presenter
                com.gcml.auth.ui.signup.SignUpByIdCardActivity presenter = mPresenter;



                presenterJavaLangObjectNull = (presenter) != (null);
                if (presenterJavaLangObjectNull) {


                    presenter.goNext();
                }
                break;
            }
            case 1: {
                // localize variables for thread safety
                // presenter != null
                boolean presenterJavaLangObjectNull = false;
                // presenter
                com.gcml.auth.ui.signup.SignUpByIdCardActivity presenter = mPresenter;



                presenterJavaLangObjectNull = (presenter) != (null);
                if (presenterJavaLangObjectNull) {


                    presenter.rootOnClick();
                }
                break;
            }
            case 5: {
                // localize variables for thread safety
                // presenter != null
                boolean presenterJavaLangObjectNull = false;
                // presenter
                com.gcml.auth.ui.signup.SignUpByIdCardActivity presenter = mPresenter;



                presenterJavaLangObjectNull = (presenter) != (null);
                if (presenterJavaLangObjectNull) {


                    presenter.goIdCardRegister();
                }
                break;
            }
            case 4: {
                // localize variables for thread safety
                // presenter != null
                boolean presenterJavaLangObjectNull = false;
                // presenter
                com.gcml.auth.ui.signup.SignUpByIdCardActivity presenter = mPresenter;



                presenterJavaLangObjectNull = (presenter) != (null);
                if (presenterJavaLangObjectNull) {


                    presenter.goUserProtocol();
                }
                break;
            }
            case 6: {
                // localize variables for thread safety
                // presenter != null
                boolean presenterJavaLangObjectNull = false;
                // presenter
                com.gcml.auth.ui.signup.SignUpByIdCardActivity presenter = mPresenter;



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
                com.gcml.auth.ui.signup.SignUpByIdCardActivity presenter = mPresenter;



                presenterJavaLangObjectNull = (presenter) != (null);
                if (presenterJavaLangObjectNull) {


                    presenter.fetchCode();
                }
                break;
            }
            case 7: {
                // localize variables for thread safety
                // presenter != null
                boolean presenterJavaLangObjectNull = false;
                // presenter
                com.gcml.auth.ui.signup.SignUpByIdCardActivity presenter = mPresenter;



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
    public static AuthActivitySignUpByIdCardBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.view.ViewGroup root, boolean attachToRoot) {
        return inflate(inflater, root, attachToRoot, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static AuthActivitySignUpByIdCardBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.view.ViewGroup root, boolean attachToRoot, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        return android.databinding.DataBindingUtil.<AuthActivitySignUpByIdCardBinding>inflate(inflater, com.gcml.auth.R.layout.auth_activity_sign_up_by_id_card, root, attachToRoot, bindingComponent);
    }
    @NonNull
    public static AuthActivitySignUpByIdCardBinding inflate(@NonNull android.view.LayoutInflater inflater) {
        return inflate(inflater, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static AuthActivitySignUpByIdCardBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        return bind(inflater.inflate(com.gcml.auth.R.layout.auth_activity_sign_up_by_id_card, null, false), bindingComponent);
    }
    @NonNull
    public static AuthActivitySignUpByIdCardBinding bind(@NonNull android.view.View view) {
        return bind(view, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static AuthActivitySignUpByIdCardBinding bind(@NonNull android.view.View view, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        if (!"layout/auth_activity_sign_up_by_id_card_0".equals(view.getTag())) {
            throw new RuntimeException("view tag isn't correct on view:" + view.getTag());
        }
        return new AuthActivitySignUpByIdCardBinding(bindingComponent, view);
    }
    /* flag mapping
        flag 0 (0x1L): presenter
        flag 1 (0x2L): viewModel
        flag 2 (0x3L): null
    flag mapping end*/
    //end
}