package com.witspring.unitdisease.databinding;
import com.witspring.unitdisease.R;
import com.witspring.unitdisease.BR;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
@javax.annotation.Generated("Android Data Binding")
public class WsdiseFragmentDiseaseInfoBinding extends android.databinding.ViewDataBinding  {

    @Nullable
    private static final android.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.wsdise_ll_head, 1);
        sViewsWithIds.put(R.id.wsdise_tv_arr_left, 2);
        sViewsWithIds.put(R.id.wsdise_hsv_photos, 3);
        sViewsWithIds.put(R.id.wsdise_ll_photos, 4);
        sViewsWithIds.put(R.id.wsdise_tv_arr_right, 5);
        sViewsWithIds.put(R.id.wsdise_tv_info, 6);
    }
    // views
    @NonNull
    private final android.widget.ScrollView mboundView0;
    @NonNull
    public final android.widget.HorizontalScrollView wsdiseHsvPhotos;
    @NonNull
    public final android.widget.LinearLayout wsdiseLlHead;
    @NonNull
    public final android.widget.LinearLayout wsdiseLlPhotos;
    @NonNull
    public final android.widget.TextView wsdiseTvArrLeft;
    @NonNull
    public final android.widget.TextView wsdiseTvArrRight;
    @NonNull
    public final android.widget.TextView wsdiseTvInfo;
    // variables
    // values
    // listeners
    // Inverse Binding Event Handlers

    public WsdiseFragmentDiseaseInfoBinding(@NonNull android.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        super(bindingComponent, root, 0);
        final Object[] bindings = mapBindings(bindingComponent, root, 7, sIncludes, sViewsWithIds);
        this.mboundView0 = (android.widget.ScrollView) bindings[0];
        this.mboundView0.setTag(null);
        this.wsdiseHsvPhotos = (android.widget.HorizontalScrollView) bindings[3];
        this.wsdiseLlHead = (android.widget.LinearLayout) bindings[1];
        this.wsdiseLlPhotos = (android.widget.LinearLayout) bindings[4];
        this.wsdiseTvArrLeft = (android.widget.TextView) bindings[2];
        this.wsdiseTvArrRight = (android.widget.TextView) bindings[5];
        this.wsdiseTvInfo = (android.widget.TextView) bindings[6];
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
    public static WsdiseFragmentDiseaseInfoBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.view.ViewGroup root, boolean attachToRoot) {
        return inflate(inflater, root, attachToRoot, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static WsdiseFragmentDiseaseInfoBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.view.ViewGroup root, boolean attachToRoot, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        return android.databinding.DataBindingUtil.<WsdiseFragmentDiseaseInfoBinding>inflate(inflater, com.witspring.unitdisease.R.layout.wsdise_fragment_disease_info, root, attachToRoot, bindingComponent);
    }
    @NonNull
    public static WsdiseFragmentDiseaseInfoBinding inflate(@NonNull android.view.LayoutInflater inflater) {
        return inflate(inflater, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static WsdiseFragmentDiseaseInfoBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        return bind(inflater.inflate(com.witspring.unitdisease.R.layout.wsdise_fragment_disease_info, null, false), bindingComponent);
    }
    @NonNull
    public static WsdiseFragmentDiseaseInfoBinding bind(@NonNull android.view.View view) {
        return bind(view, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static WsdiseFragmentDiseaseInfoBinding bind(@NonNull android.view.View view, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        if (!"layout/wsdise_fragment_disease_info_0".equals(view.getTag())) {
            throw new RuntimeException("view tag isn't correct on view:" + view.getTag());
        }
        return new WsdiseFragmentDiseaseInfoBinding(bindingComponent, view);
    }
    /* flag mapping
        flag 0 (0x1L): null
    flag mapping end*/
    //end
}