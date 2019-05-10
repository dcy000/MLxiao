package com.gcml.auth.databinding;
import com.gcml.auth.R;
import com.gcml.auth.BR;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
@javax.annotation.Generated("Android Data Binding")
public class AuthActivitySimpleProfileBinding extends android.databinding.ViewDataBinding implements android.databinding.generated.callback.OnClickListener.Listener {

    @Nullable
    private static final android.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.tb_simple_profile, 4);
        sViewsWithIds.put(R.id.tv_name, 5);
        sViewsWithIds.put(R.id.et_name, 6);
        sViewsWithIds.put(R.id.tv_id_card, 7);
        sViewsWithIds.put(R.id.et_id_card, 8);
        sViewsWithIds.put(R.id.tv_sex, 9);
        sViewsWithIds.put(R.id.tv_height, 10);
        sViewsWithIds.put(R.id.sp_height, 11);
    }
    // views
    @NonNull
    public final android.widget.EditText etIdCard;
    @NonNull
    public final android.widget.EditText etName;
    @NonNull
    private final android.support.constraint.ConstraintLayout mboundView0;
    @NonNull
    public final android.widget.Spinner spHeight;
    @NonNull
    public final com.gcml.common.widget.toolbar.TranslucentToolBar tbSimpleProfile;
    @NonNull
    public final android.widget.TextView tvHeight;
    @NonNull
    public final android.widget.TextView tvIdCard;
    @NonNull
    public final android.widget.TextView tvMan;
    @NonNull
    public final android.widget.TextView tvName;
    @NonNull
    public final android.widget.TextView tvNext;
    @NonNull
    public final android.widget.TextView tvSex;
    @NonNull
    public final android.widget.TextView tvWomen;
    // variables
    @Nullable
    private com.gcml.auth.ui.profile.SimpleProfileActivity mPresenter;
    @Nullable
    private com.gcml.auth.ui.profile.SimpleProfileViewModel mViewModel;
    @Nullable
    private final android.view.View.OnClickListener mCallback52;
    @Nullable
    private final android.view.View.OnClickListener mCallback54;
    @Nullable
    private final android.view.View.OnClickListener mCallback53;
    @Nullable
    private final android.view.View.OnClickListener mCallback51;
    // values
    // listeners
    // Inverse Binding Event Handlers

    public AuthActivitySimpleProfileBinding(@NonNull android.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        super(bindingComponent, root, 0);
        final Object[] bindings = mapBindings(bindingComponent, root, 12, sIncludes, sViewsWithIds);
        this.etIdCard = (android.widget.EditText) bindings[8];
        this.etName = (android.widget.EditText) bindings[6];
        this.mboundView0 = (android.support.constraint.ConstraintLayout) bindings[0];
        this.mboundView0.setTag(null);
        this.spHeight = (android.widget.Spinner) bindings[11];
        this.tbSimpleProfile = (com.gcml.common.widget.toolbar.TranslucentToolBar) bindings[4];
        this.tvHeight = (android.widget.TextView) bindings[10];
        this.tvIdCard = (android.widget.TextView) bindings[7];
        this.tvMan = (android.widget.TextView) bindings[1];
        this.tvMan.setTag(null);
        this.tvName = (android.widget.TextView) bindings[5];
        this.tvNext = (android.widget.TextView) bindings[3];
        this.tvNext.setTag(null);
        this.tvSex = (android.widget.TextView) bindings[9];
        this.tvWomen = (android.widget.TextView) bindings[2];
        this.tvWomen.setTag(null);
        setRootTag(root);
        // listeners
        mCallback52 = new android.databinding.generated.callback.OnClickListener(this, 2);
        mCallback54 = new android.databinding.generated.callback.OnClickListener(this, 4);
        mCallback53 = new android.databinding.generated.callback.OnClickListener(this, 3);
        mCallback51 = new android.databinding.generated.callback.OnClickListener(this, 1);
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
            setPresenter((com.gcml.auth.ui.profile.SimpleProfileActivity) variable);
        }
        else if (BR.viewModel == variableId) {
            setViewModel((com.gcml.auth.ui.profile.SimpleProfileViewModel) variable);
        }
        else {
            variableSet = false;
        }
            return variableSet;
    }

    public void setPresenter(@Nullable com.gcml.auth.ui.profile.SimpleProfileActivity Presenter) {
        this.mPresenter = Presenter;
        synchronized(this) {
            mDirtyFlags |= 0x1L;
        }
        notifyPropertyChanged(BR.presenter);
        super.requestRebind();
    }
    @Nullable
    public com.gcml.auth.ui.profile.SimpleProfileActivity getPresenter() {
        return mPresenter;
    }
    public void setViewModel(@Nullable com.gcml.auth.ui.profile.SimpleProfileViewModel ViewModel) {
        this.mViewModel = ViewModel;
    }
    @Nullable
    public com.gcml.auth.ui.profile.SimpleProfileViewModel getViewModel() {
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
        com.gcml.auth.ui.profile.SimpleProfileActivity presenter = mPresenter;
        // batch finished
        if ((dirtyFlags & 0x4L) != 0) {
            // api target 1

            this.mboundView0.setOnClickListener(mCallback51);
            this.tvMan.setOnClickListener(mCallback52);
            this.tvNext.setOnClickListener(mCallback54);
            this.tvWomen.setOnClickListener(mCallback53);
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
                com.gcml.auth.ui.profile.SimpleProfileActivity presenter = mPresenter;



                presenterJavaLangObjectNull = (presenter) != (null);
                if (presenterJavaLangObjectNull) {


                    presenter.selectMan();
                }
                break;
            }
            case 4: {
                // localize variables for thread safety
                // presenter != null
                boolean presenterJavaLangObjectNull = false;
                // presenter
                com.gcml.auth.ui.profile.SimpleProfileActivity presenter = mPresenter;



                presenterJavaLangObjectNull = (presenter) != (null);
                if (presenterJavaLangObjectNull) {


                    presenter.goNext();
                }
                break;
            }
            case 3: {
                // localize variables for thread safety
                // presenter != null
                boolean presenterJavaLangObjectNull = false;
                // presenter
                com.gcml.auth.ui.profile.SimpleProfileActivity presenter = mPresenter;



                presenterJavaLangObjectNull = (presenter) != (null);
                if (presenterJavaLangObjectNull) {


                    presenter.selectWoman();
                }
                break;
            }
            case 1: {
                // localize variables for thread safety
                // presenter != null
                boolean presenterJavaLangObjectNull = false;
                // presenter
                com.gcml.auth.ui.profile.SimpleProfileActivity presenter = mPresenter;



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
    public static AuthActivitySimpleProfileBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.view.ViewGroup root, boolean attachToRoot) {
        return inflate(inflater, root, attachToRoot, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static AuthActivitySimpleProfileBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.view.ViewGroup root, boolean attachToRoot, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        return android.databinding.DataBindingUtil.<AuthActivitySimpleProfileBinding>inflate(inflater, com.gcml.auth.R.layout.auth_activity_simple_profile, root, attachToRoot, bindingComponent);
    }
    @NonNull
    public static AuthActivitySimpleProfileBinding inflate(@NonNull android.view.LayoutInflater inflater) {
        return inflate(inflater, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static AuthActivitySimpleProfileBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        return bind(inflater.inflate(com.gcml.auth.R.layout.auth_activity_simple_profile, null, false), bindingComponent);
    }
    @NonNull
    public static AuthActivitySimpleProfileBinding bind(@NonNull android.view.View view) {
        return bind(view, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static AuthActivitySimpleProfileBinding bind(@NonNull android.view.View view, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        if (!"layout/auth_activity_simple_profile_0".equals(view.getTag())) {
            throw new RuntimeException("view tag isn't correct on view:" + view.getTag());
        }
        return new AuthActivitySimpleProfileBinding(bindingComponent, view);
    }
    /* flag mapping
        flag 0 (0x1L): presenter
        flag 1 (0x2L): viewModel
        flag 2 (0x3L): null
    flag mapping end*/
    //end
}