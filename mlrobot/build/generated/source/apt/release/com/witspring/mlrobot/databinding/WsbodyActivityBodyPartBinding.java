package com.witspring.mlrobot.databinding;
import android.databinding.Bindable;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
public abstract class WsbodyActivityBodyPartBinding extends ViewDataBinding {
    public final android.widget.CheckedTextView ctvName;
    public final android.widget.ExpandableListView elvContent;
    public final com.nex3z.flowlayout.FlowLayout flowlayout;
    public final com.facebook.drawee.view.SimpleDraweeView ivIcon;
    public final android.widget.LinearLayout llBlank;
    public final android.widget.ScrollView scrollview;
    public final android.view.View toolbar;
    public final android.widget.TextView tvSearch;
    // variables
    protected WsbodyActivityBodyPartBinding(android.databinding.DataBindingComponent bindingComponent, android.view.View root_, int localFieldCount
        , android.widget.CheckedTextView ctvName
        , android.widget.ExpandableListView elvContent
        , com.nex3z.flowlayout.FlowLayout flowlayout
        , com.facebook.drawee.view.SimpleDraweeView ivIcon
        , android.widget.LinearLayout llBlank
        , android.widget.ScrollView scrollview
        , android.view.View toolbar
        , android.widget.TextView tvSearch
    ) {
        super(bindingComponent, root_, localFieldCount);
        this.ctvName = ctvName;
        this.elvContent = elvContent;
        this.flowlayout = flowlayout;
        this.ivIcon = ivIcon;
        this.llBlank = llBlank;
        this.scrollview = scrollview;
        this.toolbar = toolbar;
        this.tvSearch = tvSearch;
    }
    //getters and abstract setters
    public static WsbodyActivityBodyPartBinding inflate(android.view.LayoutInflater inflater, android.view.ViewGroup root, boolean attachToRoot) {
        return inflate(inflater, root, attachToRoot, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    public static WsbodyActivityBodyPartBinding inflate(android.view.LayoutInflater inflater) {
        return inflate(inflater, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    public static WsbodyActivityBodyPartBinding bind(android.view.View view) {
        return null;
    }
    public static WsbodyActivityBodyPartBinding inflate(android.view.LayoutInflater inflater, android.view.ViewGroup root, boolean attachToRoot, android.databinding.DataBindingComponent bindingComponent) {
        return null;
    }
    public static WsbodyActivityBodyPartBinding inflate(android.view.LayoutInflater inflater, android.databinding.DataBindingComponent bindingComponent) {
        return null;
    }
    public static WsbodyActivityBodyPartBinding bind(android.view.View view, android.databinding.DataBindingComponent bindingComponent) {
        return null;
    }
}