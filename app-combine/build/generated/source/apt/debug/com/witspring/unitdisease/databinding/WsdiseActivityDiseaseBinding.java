package com.witspring.unitdisease.databinding;
import com.witspring.unitdisease.R;
import com.witspring.unitdisease.BR;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
@javax.annotation.Generated("Android Data Binding")
public class WsdiseActivityDiseaseBinding extends android.databinding.ViewDataBinding  {

    @Nullable
    private static final android.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.toolbar, 1);
        sViewsWithIds.put(R.id.wsdise_rg_left, 2);
        sViewsWithIds.put(R.id.rl_tab1, 3);
        sViewsWithIds.put(R.id.wsdise_rb1, 4);
        sViewsWithIds.put(R.id.rl_tab2, 5);
        sViewsWithIds.put(R.id.wsdise_rb2, 6);
        sViewsWithIds.put(R.id.rl_tab3, 7);
        sViewsWithIds.put(R.id.wsdise_rb3, 8);
        sViewsWithIds.put(R.id.rl_tab4, 9);
        sViewsWithIds.put(R.id.wsdise_rb4, 10);
        sViewsWithIds.put(R.id.wsdise_fl_content, 11);
    }
    // views
    @NonNull
    private final android.widget.RelativeLayout mboundView0;
    @NonNull
    public final android.widget.RelativeLayout rlTab1;
    @NonNull
    public final android.widget.RelativeLayout rlTab2;
    @NonNull
    public final android.widget.RelativeLayout rlTab3;
    @NonNull
    public final android.widget.RelativeLayout rlTab4;
    @Nullable
    public final android.view.View toolbar;
    @NonNull
    public final android.widget.FrameLayout wsdiseFlContent;
    @NonNull
    public final com.witspring.view.CenterRadioButton wsdiseRb1;
    @NonNull
    public final com.witspring.view.CenterRadioButton wsdiseRb2;
    @NonNull
    public final com.witspring.view.CenterRadioButton wsdiseRb3;
    @NonNull
    public final com.witspring.view.CenterRadioButton wsdiseRb4;
    @NonNull
    public final android.widget.RadioGroup wsdiseRgLeft;
    // variables
    // values
    // listeners
    // Inverse Binding Event Handlers

    public WsdiseActivityDiseaseBinding(@NonNull android.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        super(bindingComponent, root, 0);
        final Object[] bindings = mapBindings(bindingComponent, root, 12, sIncludes, sViewsWithIds);
        this.mboundView0 = (android.widget.RelativeLayout) bindings[0];
        this.mboundView0.setTag(null);
        this.rlTab1 = (android.widget.RelativeLayout) bindings[3];
        this.rlTab2 = (android.widget.RelativeLayout) bindings[5];
        this.rlTab3 = (android.widget.RelativeLayout) bindings[7];
        this.rlTab4 = (android.widget.RelativeLayout) bindings[9];
        this.toolbar = (android.view.View) bindings[1];
        this.wsdiseFlContent = (android.widget.FrameLayout) bindings[11];
        this.wsdiseRb1 = (com.witspring.view.CenterRadioButton) bindings[4];
        this.wsdiseRb2 = (com.witspring.view.CenterRadioButton) bindings[6];
        this.wsdiseRb3 = (com.witspring.view.CenterRadioButton) bindings[8];
        this.wsdiseRb4 = (com.witspring.view.CenterRadioButton) bindings[10];
        this.wsdiseRgLeft = (android.widget.RadioGroup) bindings[2];
        setRootTag(root);
        // listeners
        invalidateAll();
    }

    @Override
    public void invalidateAll() {
        synchronized(this) {
                mDirtyFlags = 0x1L;
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
            return variableSet;
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
        // batch finished
    }
    // Listener Stub Implementations
    // callback impls
    // dirty flag
    private  long mDirtyFlags = 0xffffffffffffffffL;

    @NonNull
    public static WsdiseActivityDiseaseBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.view.ViewGroup root, boolean attachToRoot) {
        return inflate(inflater, root, attachToRoot, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static WsdiseActivityDiseaseBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.view.ViewGroup root, boolean attachToRoot, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        return android.databinding.DataBindingUtil.<WsdiseActivityDiseaseBinding>inflate(inflater, com.witspring.unitdisease.R.layout.wsdise_activity_disease, root, attachToRoot, bindingComponent);
    }
    @NonNull
    public static WsdiseActivityDiseaseBinding inflate(@NonNull android.view.LayoutInflater inflater) {
        return inflate(inflater, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static WsdiseActivityDiseaseBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        return bind(inflater.inflate(com.witspring.unitdisease.R.layout.wsdise_activity_disease, null, false), bindingComponent);
    }
    @NonNull
    public static WsdiseActivityDiseaseBinding bind(@NonNull android.view.View view) {
        return bind(view, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static WsdiseActivityDiseaseBinding bind(@NonNull android.view.View view, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        if (!"layout/wsdise_activity_disease_0".equals(view.getTag())) {
            throw new RuntimeException("view tag isn't correct on view:" + view.getTag());
        }
        return new WsdiseActivityDiseaseBinding(bindingComponent, view);
    }
    /* flag mapping
        flag 0 (0x1L): null
    flag mapping end*/
    //end
}