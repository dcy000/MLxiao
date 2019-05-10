package com.witspring.unitbody.databinding;
import com.witspring.unitbody.R;
import com.witspring.unitbody.BR;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
@javax.annotation.Generated("Android Data Binding")
public class WsbodyActivityChooseMemberBinding extends android.databinding.ViewDataBinding  {

    @Nullable
    private static final android.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.toolbar, 1);
        sViewsWithIds.put(R.id.wsbody_imageview, 2);
        sViewsWithIds.put(R.id.wsbody_textview3, 3);
        sViewsWithIds.put(R.id.flowlayout, 4);
        sViewsWithIds.put(R.id.llOther, 5);
        sViewsWithIds.put(R.id.tvHistory, 6);
    }
    // views
    @NonNull
    public final com.nex3z.flowlayout.FlowLayout flowlayout;
    @NonNull
    public final android.widget.LinearLayout llOther;
    @NonNull
    private final android.support.constraint.ConstraintLayout mboundView0;
    @Nullable
    public final android.view.View toolbar;
    @NonNull
    public final android.widget.TextView tvHistory;
    @NonNull
    public final android.widget.ImageView wsbodyImageview;
    @NonNull
    public final android.widget.TextView wsbodyTextview3;
    // variables
    // values
    // listeners
    // Inverse Binding Event Handlers

    public WsbodyActivityChooseMemberBinding(@NonNull android.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        super(bindingComponent, root, 0);
        final Object[] bindings = mapBindings(bindingComponent, root, 7, sIncludes, sViewsWithIds);
        this.flowlayout = (com.nex3z.flowlayout.FlowLayout) bindings[4];
        this.llOther = (android.widget.LinearLayout) bindings[5];
        this.mboundView0 = (android.support.constraint.ConstraintLayout) bindings[0];
        this.mboundView0.setTag(null);
        this.toolbar = (android.view.View) bindings[1];
        this.tvHistory = (android.widget.TextView) bindings[6];
        this.wsbodyImageview = (android.widget.ImageView) bindings[2];
        this.wsbodyTextview3 = (android.widget.TextView) bindings[3];
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
    public static WsbodyActivityChooseMemberBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.view.ViewGroup root, boolean attachToRoot) {
        return inflate(inflater, root, attachToRoot, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static WsbodyActivityChooseMemberBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.view.ViewGroup root, boolean attachToRoot, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        return android.databinding.DataBindingUtil.<WsbodyActivityChooseMemberBinding>inflate(inflater, com.witspring.unitbody.R.layout.wsbody_activity_choose_member, root, attachToRoot, bindingComponent);
    }
    @NonNull
    public static WsbodyActivityChooseMemberBinding inflate(@NonNull android.view.LayoutInflater inflater) {
        return inflate(inflater, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static WsbodyActivityChooseMemberBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        return bind(inflater.inflate(com.witspring.unitbody.R.layout.wsbody_activity_choose_member, null, false), bindingComponent);
    }
    @NonNull
    public static WsbodyActivityChooseMemberBinding bind(@NonNull android.view.View view) {
        return bind(view, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static WsbodyActivityChooseMemberBinding bind(@NonNull android.view.View view, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        if (!"layout/wsbody_activity_choose_member_0".equals(view.getTag())) {
            throw new RuntimeException("view tag isn't correct on view:" + view.getTag());
        }
        return new WsbodyActivityChooseMemberBinding(bindingComponent, view);
    }
    /* flag mapping
        flag 0 (0x1L): null
    flag mapping end*/
    //end
}