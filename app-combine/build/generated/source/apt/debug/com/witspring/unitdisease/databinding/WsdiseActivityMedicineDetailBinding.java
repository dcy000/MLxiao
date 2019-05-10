package com.witspring.unitdisease.databinding;
import com.witspring.unitdisease.R;
import com.witspring.unitdisease.BR;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
@javax.annotation.Generated("Android Data Binding")
public class WsdiseActivityMedicineDetailBinding extends android.databinding.ViewDataBinding  {

    @Nullable
    private static final android.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.toolbar, 1);
        sViewsWithIds.put(R.id.wsdise_iv_detail, 2);
        sViewsWithIds.put(R.id.wsdise_tv_medicine_name, 3);
        sViewsWithIds.put(R.id.wsdise_tv_cfy, 4);
        sViewsWithIds.put(R.id.wsdise_tv_support, 5);
        sViewsWithIds.put(R.id.wsdise_tv_price, 6);
        sViewsWithIds.put(R.id.wsdise_tv_title1, 7);
        sViewsWithIds.put(R.id.wsdise_tv_indications, 8);
        sViewsWithIds.put(R.id.wsdise_tv_title2, 9);
        sViewsWithIds.put(R.id.wsdise_tv_specification, 10);
        sViewsWithIds.put(R.id.wsdise_tv_title3, 11);
        sViewsWithIds.put(R.id.wsdise_tv_component, 12);
        sViewsWithIds.put(R.id.wsdise_tv_title4, 13);
        sViewsWithIds.put(R.id.wsdise_tv_adverse_effect, 14);
        sViewsWithIds.put(R.id.wsdise_tv_title5, 15);
        sViewsWithIds.put(R.id.wsdise_tv_taboo, 16);
        sViewsWithIds.put(R.id.wsdise_tv_title6, 17);
        sViewsWithIds.put(R.id.wsdise_tv_usage, 18);
        sViewsWithIds.put(R.id.wsdise_tv_title7, 19);
        sViewsWithIds.put(R.id.wsdise_tv_interaction, 20);
        sViewsWithIds.put(R.id.wsdise_tv_title8, 21);
        sViewsWithIds.put(R.id.wsdise_tv_attention, 22);
        sViewsWithIds.put(R.id.wsdise_tv_title9, 23);
        sViewsWithIds.put(R.id.wsdise_tv_approval, 24);
    }
    // views
    @NonNull
    private final android.widget.LinearLayout mboundView0;
    @Nullable
    public final android.view.View toolbar;
    @NonNull
    public final com.facebook.drawee.view.SimpleDraweeView wsdiseIvDetail;
    @NonNull
    public final android.widget.TextView wsdiseTvAdverseEffect;
    @NonNull
    public final android.widget.TextView wsdiseTvApproval;
    @NonNull
    public final android.widget.TextView wsdiseTvAttention;
    @NonNull
    public final android.widget.TextView wsdiseTvCfy;
    @NonNull
    public final android.widget.TextView wsdiseTvComponent;
    @NonNull
    public final android.widget.TextView wsdiseTvIndications;
    @NonNull
    public final android.widget.TextView wsdiseTvInteraction;
    @NonNull
    public final android.widget.TextView wsdiseTvMedicineName;
    @NonNull
    public final android.widget.TextView wsdiseTvPrice;
    @NonNull
    public final android.widget.TextView wsdiseTvSpecification;
    @NonNull
    public final android.widget.TextView wsdiseTvSupport;
    @NonNull
    public final android.widget.TextView wsdiseTvTaboo;
    @NonNull
    public final android.widget.TextView wsdiseTvTitle1;
    @NonNull
    public final android.widget.TextView wsdiseTvTitle2;
    @NonNull
    public final android.widget.TextView wsdiseTvTitle3;
    @NonNull
    public final android.widget.TextView wsdiseTvTitle4;
    @NonNull
    public final android.widget.TextView wsdiseTvTitle5;
    @NonNull
    public final android.widget.TextView wsdiseTvTitle6;
    @NonNull
    public final android.widget.TextView wsdiseTvTitle7;
    @NonNull
    public final android.widget.TextView wsdiseTvTitle8;
    @NonNull
    public final android.widget.TextView wsdiseTvTitle9;
    @NonNull
    public final android.widget.TextView wsdiseTvUsage;
    // variables
    // values
    // listeners
    // Inverse Binding Event Handlers

    public WsdiseActivityMedicineDetailBinding(@NonNull android.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        super(bindingComponent, root, 0);
        final Object[] bindings = mapBindings(bindingComponent, root, 25, sIncludes, sViewsWithIds);
        this.mboundView0 = (android.widget.LinearLayout) bindings[0];
        this.mboundView0.setTag(null);
        this.toolbar = (android.view.View) bindings[1];
        this.wsdiseIvDetail = (com.facebook.drawee.view.SimpleDraweeView) bindings[2];
        this.wsdiseTvAdverseEffect = (android.widget.TextView) bindings[14];
        this.wsdiseTvApproval = (android.widget.TextView) bindings[24];
        this.wsdiseTvAttention = (android.widget.TextView) bindings[22];
        this.wsdiseTvCfy = (android.widget.TextView) bindings[4];
        this.wsdiseTvComponent = (android.widget.TextView) bindings[12];
        this.wsdiseTvIndications = (android.widget.TextView) bindings[8];
        this.wsdiseTvInteraction = (android.widget.TextView) bindings[20];
        this.wsdiseTvMedicineName = (android.widget.TextView) bindings[3];
        this.wsdiseTvPrice = (android.widget.TextView) bindings[6];
        this.wsdiseTvSpecification = (android.widget.TextView) bindings[10];
        this.wsdiseTvSupport = (android.widget.TextView) bindings[5];
        this.wsdiseTvTaboo = (android.widget.TextView) bindings[16];
        this.wsdiseTvTitle1 = (android.widget.TextView) bindings[7];
        this.wsdiseTvTitle2 = (android.widget.TextView) bindings[9];
        this.wsdiseTvTitle3 = (android.widget.TextView) bindings[11];
        this.wsdiseTvTitle4 = (android.widget.TextView) bindings[13];
        this.wsdiseTvTitle5 = (android.widget.TextView) bindings[15];
        this.wsdiseTvTitle6 = (android.widget.TextView) bindings[17];
        this.wsdiseTvTitle7 = (android.widget.TextView) bindings[19];
        this.wsdiseTvTitle8 = (android.widget.TextView) bindings[21];
        this.wsdiseTvTitle9 = (android.widget.TextView) bindings[23];
        this.wsdiseTvUsage = (android.widget.TextView) bindings[18];
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
    public static WsdiseActivityMedicineDetailBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.view.ViewGroup root, boolean attachToRoot) {
        return inflate(inflater, root, attachToRoot, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static WsdiseActivityMedicineDetailBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.view.ViewGroup root, boolean attachToRoot, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        return android.databinding.DataBindingUtil.<WsdiseActivityMedicineDetailBinding>inflate(inflater, com.witspring.unitdisease.R.layout.wsdise_activity_medicine_detail, root, attachToRoot, bindingComponent);
    }
    @NonNull
    public static WsdiseActivityMedicineDetailBinding inflate(@NonNull android.view.LayoutInflater inflater) {
        return inflate(inflater, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static WsdiseActivityMedicineDetailBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        return bind(inflater.inflate(com.witspring.unitdisease.R.layout.wsdise_activity_medicine_detail, null, false), bindingComponent);
    }
    @NonNull
    public static WsdiseActivityMedicineDetailBinding bind(@NonNull android.view.View view) {
        return bind(view, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static WsdiseActivityMedicineDetailBinding bind(@NonNull android.view.View view, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        if (!"layout/wsdise_activity_medicine_detail_0".equals(view.getTag())) {
            throw new RuntimeException("view tag isn't correct on view:" + view.getTag());
        }
        return new WsdiseActivityMedicineDetailBinding(bindingComponent, view);
    }
    /* flag mapping
        flag 0 (0x1L): null
    flag mapping end*/
    //end
}