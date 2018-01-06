package com.witspring.mlrobot.databinding;
import android.databinding.Bindable;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
public abstract class WsbodyActivityChooseMemberBinding extends ViewDataBinding {
    public final com.nex3z.flowlayout.FlowLayout flowlayout;
    public final android.widget.LinearLayout llOther;
    public final android.view.View toolbar;
    public final android.widget.ImageView wsbodyImageview;
    public final android.widget.TextView wsbodyTextview3;
    // variables
    protected WsbodyActivityChooseMemberBinding(android.databinding.DataBindingComponent bindingComponent, android.view.View root_, int localFieldCount
        , com.nex3z.flowlayout.FlowLayout flowlayout
        , android.widget.LinearLayout llOther
        , android.view.View toolbar
        , android.widget.ImageView wsbodyImageview
        , android.widget.TextView wsbodyTextview3
    ) {
        super(bindingComponent, root_, localFieldCount);
        this.flowlayout = flowlayout;
        this.llOther = llOther;
        this.toolbar = toolbar;
        this.wsbodyImageview = wsbodyImageview;
        this.wsbodyTextview3 = wsbodyTextview3;
    }
    //getters and abstract setters
    public static WsbodyActivityChooseMemberBinding inflate(android.view.LayoutInflater inflater, android.view.ViewGroup root, boolean attachToRoot) {
        return inflate(inflater, root, attachToRoot, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    public static WsbodyActivityChooseMemberBinding inflate(android.view.LayoutInflater inflater) {
        return inflate(inflater, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    public static WsbodyActivityChooseMemberBinding bind(android.view.View view) {
        return null;
    }
    public static WsbodyActivityChooseMemberBinding inflate(android.view.LayoutInflater inflater, android.view.ViewGroup root, boolean attachToRoot, android.databinding.DataBindingComponent bindingComponent) {
        return null;
    }
    public static WsbodyActivityChooseMemberBinding inflate(android.view.LayoutInflater inflater, android.databinding.DataBindingComponent bindingComponent) {
        return null;
    }
    public static WsbodyActivityChooseMemberBinding bind(android.view.View view, android.databinding.DataBindingComponent bindingComponent) {
        return null;
    }
}