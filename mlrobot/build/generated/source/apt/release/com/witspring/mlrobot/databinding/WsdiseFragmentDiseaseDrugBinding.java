package com.witspring.mlrobot.databinding;
import android.databinding.Bindable;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
public abstract class WsdiseFragmentDiseaseDrugBinding extends ViewDataBinding {
    public final android.widget.GridView wsdiseGvContent;
    // variables
    protected WsdiseFragmentDiseaseDrugBinding(android.databinding.DataBindingComponent bindingComponent, android.view.View root_, int localFieldCount
        , android.widget.GridView wsdiseGvContent
    ) {
        super(bindingComponent, root_, localFieldCount);
        this.wsdiseGvContent = wsdiseGvContent;
    }
    //getters and abstract setters
    public static WsdiseFragmentDiseaseDrugBinding inflate(android.view.LayoutInflater inflater, android.view.ViewGroup root, boolean attachToRoot) {
        return inflate(inflater, root, attachToRoot, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    public static WsdiseFragmentDiseaseDrugBinding inflate(android.view.LayoutInflater inflater) {
        return inflate(inflater, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    public static WsdiseFragmentDiseaseDrugBinding bind(android.view.View view) {
        return null;
    }
    public static WsdiseFragmentDiseaseDrugBinding inflate(android.view.LayoutInflater inflater, android.view.ViewGroup root, boolean attachToRoot, android.databinding.DataBindingComponent bindingComponent) {
        return null;
    }
    public static WsdiseFragmentDiseaseDrugBinding inflate(android.view.LayoutInflater inflater, android.databinding.DataBindingComponent bindingComponent) {
        return null;
    }
    public static WsdiseFragmentDiseaseDrugBinding bind(android.view.View view, android.databinding.DataBindingComponent bindingComponent) {
        return null;
    }
}