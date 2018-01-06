package com.witspring.mlrobot.databinding;
import android.databinding.Bindable;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
public abstract class WsdiseFragmentDiseaseCureBinding extends ViewDataBinding {
    public final android.widget.TextView wsdiseTvCure;
    // variables
    protected WsdiseFragmentDiseaseCureBinding(android.databinding.DataBindingComponent bindingComponent, android.view.View root_, int localFieldCount
        , android.widget.TextView wsdiseTvCure
    ) {
        super(bindingComponent, root_, localFieldCount);
        this.wsdiseTvCure = wsdiseTvCure;
    }
    //getters and abstract setters
    public static WsdiseFragmentDiseaseCureBinding inflate(android.view.LayoutInflater inflater, android.view.ViewGroup root, boolean attachToRoot) {
        return inflate(inflater, root, attachToRoot, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    public static WsdiseFragmentDiseaseCureBinding inflate(android.view.LayoutInflater inflater) {
        return inflate(inflater, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    public static WsdiseFragmentDiseaseCureBinding bind(android.view.View view) {
        return null;
    }
    public static WsdiseFragmentDiseaseCureBinding inflate(android.view.LayoutInflater inflater, android.view.ViewGroup root, boolean attachToRoot, android.databinding.DataBindingComponent bindingComponent) {
        return null;
    }
    public static WsdiseFragmentDiseaseCureBinding inflate(android.view.LayoutInflater inflater, android.databinding.DataBindingComponent bindingComponent) {
        return null;
    }
    public static WsdiseFragmentDiseaseCureBinding bind(android.view.View view, android.databinding.DataBindingComponent bindingComponent) {
        return null;
    }
}