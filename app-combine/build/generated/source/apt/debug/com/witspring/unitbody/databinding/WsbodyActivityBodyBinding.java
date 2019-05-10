package com.witspring.unitbody.databinding;
import com.witspring.unitbody.R;
import com.witspring.unitbody.BR;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
@javax.annotation.Generated("Android Data Binding")
public class WsbodyActivityBodyBinding extends android.databinding.ViewDataBinding  {

    @Nullable
    private static final android.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.toolbar, 1);
        sViewsWithIds.put(R.id.wsbody_guideline1, 2);
        sViewsWithIds.put(R.id.wsbody_textview, 3);
        sViewsWithIds.put(R.id.wsbody_textview2, 4);
        sViewsWithIds.put(R.id.pageView, 5);
        sViewsWithIds.put(R.id.touchView, 6);
        sViewsWithIds.put(R.id.ivCheck, 7);
        sViewsWithIds.put(R.id.wsbody_bodytxt1, 8);
        sViewsWithIds.put(R.id.wsbody_bodytxt2, 9);
        sViewsWithIds.put(R.id.ctvSwitch, 10);
        sViewsWithIds.put(R.id.ctvAll, 11);
    }
    // views
    @NonNull
    public final android.widget.CheckedTextView ctvAll;
    @NonNull
    public final android.widget.CheckedTextView ctvSwitch;
    @NonNull
    public final android.widget.ImageView ivCheck;
    @NonNull
    private final android.support.constraint.ConstraintLayout mboundView0;
    @NonNull
    public final com.witspring.view.pageflow.PageFlowView pageView;
    @Nullable
    public final android.view.View toolbar;
    @NonNull
    public final com.witspring.view.TouchImageView touchView;
    @NonNull
    public final android.widget.TextView wsbodyBodytxt1;
    @NonNull
    public final android.widget.TextView wsbodyBodytxt2;
    @NonNull
    public final android.support.constraint.Guideline wsbodyGuideline1;
    @NonNull
    public final android.widget.TextView wsbodyTextview;
    @NonNull
    public final android.widget.TextView wsbodyTextview2;
    // variables
    // values
    // listeners
    // Inverse Binding Event Handlers

    public WsbodyActivityBodyBinding(@NonNull android.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        super(bindingComponent, root, 0);
        final Object[] bindings = mapBindings(bindingComponent, root, 12, sIncludes, sViewsWithIds);
        this.ctvAll = (android.widget.CheckedTextView) bindings[11];
        this.ctvSwitch = (android.widget.CheckedTextView) bindings[10];
        this.ivCheck = (android.widget.ImageView) bindings[7];
        this.mboundView0 = (android.support.constraint.ConstraintLayout) bindings[0];
        this.mboundView0.setTag(null);
        this.pageView = (com.witspring.view.pageflow.PageFlowView) bindings[5];
        this.toolbar = (android.view.View) bindings[1];
        this.touchView = (com.witspring.view.TouchImageView) bindings[6];
        this.wsbodyBodytxt1 = (android.widget.TextView) bindings[8];
        this.wsbodyBodytxt2 = (android.widget.TextView) bindings[9];
        this.wsbodyGuideline1 = (android.support.constraint.Guideline) bindings[2];
        this.wsbodyTextview = (android.widget.TextView) bindings[3];
        this.wsbodyTextview2 = (android.widget.TextView) bindings[4];
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
    public static WsbodyActivityBodyBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.view.ViewGroup root, boolean attachToRoot) {
        return inflate(inflater, root, attachToRoot, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static WsbodyActivityBodyBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.view.ViewGroup root, boolean attachToRoot, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        return android.databinding.DataBindingUtil.<WsbodyActivityBodyBinding>inflate(inflater, com.witspring.unitbody.R.layout.wsbody_activity_body, root, attachToRoot, bindingComponent);
    }
    @NonNull
    public static WsbodyActivityBodyBinding inflate(@NonNull android.view.LayoutInflater inflater) {
        return inflate(inflater, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static WsbodyActivityBodyBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        return bind(inflater.inflate(com.witspring.unitbody.R.layout.wsbody_activity_body, null, false), bindingComponent);
    }
    @NonNull
    public static WsbodyActivityBodyBinding bind(@NonNull android.view.View view) {
        return bind(view, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static WsbodyActivityBodyBinding bind(@NonNull android.view.View view, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        if (!"layout/wsbody_activity_body_0".equals(view.getTag())) {
            throw new RuntimeException("view tag isn't correct on view:" + view.getTag());
        }
        return new WsbodyActivityBodyBinding(bindingComponent, view);
    }
    /* flag mapping
        flag 0 (0x1L): null
    flag mapping end*/
    //end
}