package com.witspring.unitbody.databinding;
import com.witspring.unitbody.R;
import com.witspring.unitbody.BR;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
@javax.annotation.Generated("Android Data Binding")
public class WsbodyActivityBodyPartBinding extends android.databinding.ViewDataBinding  {

    @Nullable
    private static final android.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.toolbar, 1);
        sViewsWithIds.put(R.id.ctvName, 2);
        sViewsWithIds.put(R.id.elvContent, 3);
        sViewsWithIds.put(R.id.scrollview, 4);
        sViewsWithIds.put(R.id.ivIcon, 5);
        sViewsWithIds.put(R.id.flowlayout, 6);
        sViewsWithIds.put(R.id.tvSearch, 7);
        sViewsWithIds.put(R.id.llBlank, 8);
    }
    // views
    @NonNull
    public final android.widget.CheckedTextView ctvName;
    @NonNull
    public final android.widget.ExpandableListView elvContent;
    @NonNull
    public final com.nex3z.flowlayout.FlowLayout flowlayout;
    @NonNull
    public final com.facebook.drawee.view.SimpleDraweeView ivIcon;
    @NonNull
    public final android.widget.LinearLayout llBlank;
    @NonNull
    private final android.widget.LinearLayout mboundView0;
    @NonNull
    public final android.widget.ScrollView scrollview;
    @Nullable
    public final android.view.View toolbar;
    @NonNull
    public final android.widget.TextView tvSearch;
    // variables
    // values
    // listeners
    // Inverse Binding Event Handlers

    public WsbodyActivityBodyPartBinding(@NonNull android.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        super(bindingComponent, root, 0);
        final Object[] bindings = mapBindings(bindingComponent, root, 9, sIncludes, sViewsWithIds);
        this.ctvName = (android.widget.CheckedTextView) bindings[2];
        this.elvContent = (android.widget.ExpandableListView) bindings[3];
        this.flowlayout = (com.nex3z.flowlayout.FlowLayout) bindings[6];
        this.ivIcon = (com.facebook.drawee.view.SimpleDraweeView) bindings[5];
        this.llBlank = (android.widget.LinearLayout) bindings[8];
        this.mboundView0 = (android.widget.LinearLayout) bindings[0];
        this.mboundView0.setTag(null);
        this.scrollview = (android.widget.ScrollView) bindings[4];
        this.toolbar = (android.view.View) bindings[1];
        this.tvSearch = (android.widget.TextView) bindings[7];
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
    public static WsbodyActivityBodyPartBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.view.ViewGroup root, boolean attachToRoot) {
        return inflate(inflater, root, attachToRoot, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static WsbodyActivityBodyPartBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.view.ViewGroup root, boolean attachToRoot, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        return android.databinding.DataBindingUtil.<WsbodyActivityBodyPartBinding>inflate(inflater, com.witspring.unitbody.R.layout.wsbody_activity_body_part, root, attachToRoot, bindingComponent);
    }
    @NonNull
    public static WsbodyActivityBodyPartBinding inflate(@NonNull android.view.LayoutInflater inflater) {
        return inflate(inflater, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static WsbodyActivityBodyPartBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        return bind(inflater.inflate(com.witspring.unitbody.R.layout.wsbody_activity_body_part, null, false), bindingComponent);
    }
    @NonNull
    public static WsbodyActivityBodyPartBinding bind(@NonNull android.view.View view) {
        return bind(view, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static WsbodyActivityBodyPartBinding bind(@NonNull android.view.View view, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        if (!"layout/wsbody_activity_body_part_0".equals(view.getTag())) {
            throw new RuntimeException("view tag isn't correct on view:" + view.getTag());
        }
        return new WsbodyActivityBodyPartBinding(bindingComponent, view);
    }
    /* flag mapping
        flag 0 (0x1L): null
    flag mapping end*/
    //end
}