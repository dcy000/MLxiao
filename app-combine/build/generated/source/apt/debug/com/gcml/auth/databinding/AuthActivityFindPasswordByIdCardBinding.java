package com.gcml.auth.databinding;
import com.gcml.auth.R;
import com.gcml.auth.BR;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
@javax.annotation.Generated("Android Data Binding")
public class AuthActivityFindPasswordByIdCardBinding extends android.databinding.ViewDataBinding implements android.databinding.generated.callback.OnClickListener.Listener {

    @Nullable
    private static final android.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.cl_auth_items, 5);
        sViewsWithIds.put(R.id.tv_title, 6);
        sViewsWithIds.put(R.id.et_phone, 7);
        sViewsWithIds.put(R.id.et_code, 8);
        sViewsWithIds.put(R.id.tv_copyright, 9);
    }
    // views
    @NonNull
    public final android.support.constraint.ConstraintLayout clAuthItems;
    @NonNull
    public final android.widget.EditText etCode;
    @NonNull
    public final android.widget.EditText etPhone;
    @NonNull
    public final android.widget.ImageView ivBack;
    @NonNull
    public final android.widget.ImageView ivWifiState;
    @NonNull
    private final android.support.constraint.ConstraintLayout mboundView0;
    @NonNull
    public final android.widget.TextView tvCode;
    @NonNull
    public final android.widget.TextView tvCopyright;
    @NonNull
    public final android.widget.TextView tvNext;
    @NonNull
    public final android.widget.TextView tvTitle;
    // variables
    @Nullable
    private com.gcml.auth.ui.findpassword.FindPasswordByIdCardActivity mPresenter;
    @Nullable
    private com.gcml.auth.ui.findpassword.FindPasswordByIdCardViewModel mViewModel;
    @Nullable
    private final android.view.View.OnClickListener mCallback19;
    @Nullable
    private final android.view.View.OnClickListener mCallback17;
    @Nullable
    private final android.view.View.OnClickListener mCallback21;
    @Nullable
    private final android.view.View.OnClickListener mCallback20;
    @Nullable
    private final android.view.View.OnClickListener mCallback18;
    // values
    // listeners
    // Inverse Binding Event Handlers

    public AuthActivityFindPasswordByIdCardBinding(@NonNull android.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        super(bindingComponent, root, 0);
        final Object[] bindings = mapBindings(bindingComponent, root, 10, sIncludes, sViewsWithIds);
        this.clAuthItems = (android.support.constraint.ConstraintLayout) bindings[5];
        this.etCode = (android.widget.EditText) bindings[8];
        this.etPhone = (android.widget.EditText) bindings[7];
        this.ivBack = (android.widget.ImageView) bindings[3];
        this.ivBack.setTag(null);
        this.ivWifiState = (android.widget.ImageView) bindings[4];
        this.ivWifiState.setTag(null);
        this.mboundView0 = (android.support.constraint.ConstraintLayout) bindings[0];
        this.mboundView0.setTag(null);
        this.tvCode = (android.widget.TextView) bindings[1];
        this.tvCode.setTag(null);
        this.tvCopyright = (android.widget.TextView) bindings[9];
        this.tvNext = (android.widget.TextView) bindings[2];
        this.tvNext.setTag(null);
        this.tvTitle = (android.widget.TextView) bindings[6];
        setRootTag(root);
        // listeners
        mCallback19 = new android.databinding.generated.callback.OnClickListener(this, 3);
        mCallback17 = new android.databinding.generated.callback.OnClickListener(this, 1);
        mCallback21 = new android.databinding.generated.callback.OnClickListener(this, 5);
        mCallback20 = new android.databinding.generated.callback.OnClickListener(this, 4);
        mCallback18 = new android.databinding.generated.callback.OnClickListener(this, 2);
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
            setPresenter((com.gcml.auth.ui.findpassword.FindPasswordByIdCardActivity) variable);
        }
        else if (BR.viewModel == variableId) {
            setViewModel((com.gcml.auth.ui.findpassword.FindPasswordByIdCardViewModel) variable);
        }
        else {
            variableSet = false;
        }
            return variableSet;
    }

    public void setPresenter(@Nullable com.gcml.auth.ui.findpassword.FindPasswordByIdCardActivity Presenter) {
        this.mPresenter = Presenter;
        synchronized(this) {
            mDirtyFlags |= 0x1L;
        }
        notifyPropertyChanged(BR.presenter);
        super.requestRebind();
    }
    @Nullable
    public com.gcml.auth.ui.findpassword.FindPasswordByIdCardActivity getPresenter() {
        return mPresenter;
    }
    public void setViewModel(@Nullable com.gcml.auth.ui.findpassword.FindPasswordByIdCardViewModel ViewModel) {
        this.mViewModel = ViewModel;
    }
    @Nullable
    public com.gcml.auth.ui.findpassword.FindPasswordByIdCardViewModel getViewModel() {
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
        com.gcml.auth.ui.findpassword.FindPasswordByIdCardActivity presenter = mPresenter;
        // batch finished
        if ((dirtyFlags & 0x4L) != 0) {
            // api target 1

            this.ivBack.setOnClickListener(mCallback20);
            this.ivWifiState.setOnClickListener(mCallback21);
            this.mboundView0.setOnClickListener(mCallback17);
            this.tvCode.setOnClickListener(mCallback18);
            this.tvNext.setOnClickListener(mCallback19);
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
                com.gcml.auth.ui.findpassword.FindPasswordByIdCardActivity presenter = mPresenter;



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
                com.gcml.auth.ui.findpassword.FindPasswordByIdCardActivity presenter = mPresenter;



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
                com.gcml.auth.ui.findpassword.FindPasswordByIdCardActivity presenter = mPresenter;



                presenterJavaLangObjectNull = (presenter) != (null);
                if (presenterJavaLangObjectNull) {


                    presenter.goWifi();
                }
                break;
            }
            case 4: {
                // localize variables for thread safety
                // presenter != null
                boolean presenterJavaLangObjectNull = false;
                // presenter
                com.gcml.auth.ui.findpassword.FindPasswordByIdCardActivity presenter = mPresenter;



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
                com.gcml.auth.ui.findpassword.FindPasswordByIdCardActivity presenter = mPresenter;



                presenterJavaLangObjectNull = (presenter) != (null);
                if (presenterJavaLangObjectNull) {


                    presenter.fetchCode();
                }
                break;
            }
        }
    }
    // dirty flag
    private  long mDirtyFlags = 0xffffffffffffffffL;

    @NonNull
    public static AuthActivityFindPasswordByIdCardBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.view.ViewGroup root, boolean attachToRoot) {
        return inflate(inflater, root, attachToRoot, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static AuthActivityFindPasswordByIdCardBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.view.ViewGroup root, boolean attachToRoot, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        return android.databinding.DataBindingUtil.<AuthActivityFindPasswordByIdCardBinding>inflate(inflater, com.gcml.auth.R.layout.auth_activity_find_password_by_id_card, root, attachToRoot, bindingComponent);
    }
    @NonNull
    public static AuthActivityFindPasswordByIdCardBinding inflate(@NonNull android.view.LayoutInflater inflater) {
        return inflate(inflater, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static AuthActivityFindPasswordByIdCardBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        return bind(inflater.inflate(com.gcml.auth.R.layout.auth_activity_find_password_by_id_card, null, false), bindingComponent);
    }
    @NonNull
    public static AuthActivityFindPasswordByIdCardBinding bind(@NonNull android.view.View view) {
        return bind(view, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static AuthActivityFindPasswordByIdCardBinding bind(@NonNull android.view.View view, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        if (!"layout/auth_activity_find_password_by_id_card_0".equals(view.getTag())) {
            throw new RuntimeException("view tag isn't correct on view:" + view.getTag());
        }
        return new AuthActivityFindPasswordByIdCardBinding(bindingComponent, view);
    }
    /* flag mapping
        flag 0 (0x1L): presenter
        flag 1 (0x2L): viewModel
        flag 2 (0x3L): null
    flag mapping end*/
    //end
}