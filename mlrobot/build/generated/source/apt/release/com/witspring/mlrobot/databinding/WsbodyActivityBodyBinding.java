package com.witspring.mlrobot.databinding;
import android.databinding.Bindable;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
public abstract class WsbodyActivityBodyBinding extends ViewDataBinding {
    public final android.widget.CheckedTextView ctvAll;
    public final android.widget.CheckedTextView ctvSwitch;
    public final android.widget.ImageView ivCheck;
    public final com.witspring.view.pageflow.PageFlowView pageView;
    public final android.view.View toolbar;
    public final com.witspring.view.TouchImageView touchView;
    public final android.widget.TextView wsbodyBodytxt1;
    public final android.widget.TextView wsbodyBodytxt2;
    public final android.support.constraint.Guideline wsbodyGuideline1;
    public final android.widget.TextView wsbodyTextview;
    public final android.widget.TextView wsbodyTextview2;
    // variables
    protected WsbodyActivityBodyBinding(android.databinding.DataBindingComponent bindingComponent, android.view.View root_, int localFieldCount
        , android.widget.CheckedTextView ctvAll
        , android.widget.CheckedTextView ctvSwitch
        , android.widget.ImageView ivCheck
        , com.witspring.view.pageflow.PageFlowView pageView
        , android.view.View toolbar
        , com.witspring.view.TouchImageView touchView
        , android.widget.TextView wsbodyBodytxt1
        , android.widget.TextView wsbodyBodytxt2
        , android.support.constraint.Guideline wsbodyGuideline1
        , android.widget.TextView wsbodyTextview
        , android.widget.TextView wsbodyTextview2
    ) {
        super(bindingComponent, root_, localFieldCount);
        this.ctvAll = ctvAll;
        this.ctvSwitch = ctvSwitch;
        this.ivCheck = ivCheck;
        this.pageView = pageView;
        this.toolbar = toolbar;
        this.touchView = touchView;
        this.wsbodyBodytxt1 = wsbodyBodytxt1;
        this.wsbodyBodytxt2 = wsbodyBodytxt2;
        this.wsbodyGuideline1 = wsbodyGuideline1;
        this.wsbodyTextview = wsbodyTextview;
        this.wsbodyTextview2 = wsbodyTextview2;
    }
    //getters and abstract setters
    public static WsbodyActivityBodyBinding inflate(android.view.LayoutInflater inflater, android.view.ViewGroup root, boolean attachToRoot) {
        return inflate(inflater, root, attachToRoot, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    public static WsbodyActivityBodyBinding inflate(android.view.LayoutInflater inflater) {
        return inflate(inflater, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    public static WsbodyActivityBodyBinding bind(android.view.View view) {
        return null;
    }
    public static WsbodyActivityBodyBinding inflate(android.view.LayoutInflater inflater, android.view.ViewGroup root, boolean attachToRoot, android.databinding.DataBindingComponent bindingComponent) {
        return null;
    }
    public static WsbodyActivityBodyBinding inflate(android.view.LayoutInflater inflater, android.databinding.DataBindingComponent bindingComponent) {
        return null;
    }
    public static WsbodyActivityBodyBinding bind(android.view.View view, android.databinding.DataBindingComponent bindingComponent) {
        return null;
    }
}