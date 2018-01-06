package com.witspring.mlrobot.databinding;
import android.databinding.Bindable;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
public abstract class WsdiseFragmentDiseaseDiagBinding extends ViewDataBinding {
    public final android.widget.TextView wsdiseTvDiag;
    // variables
    protected WsdiseFragmentDiseaseDiagBinding(android.databinding.DataBindingComponent bindingComponent, android.view.View root_, int localFieldCount
        , android.widget.TextView wsdiseTvDiag
    ) {
        super(bindingComponent, root_, localFieldCount);
        this.wsdiseTvDiag = wsdiseTvDiag;
    }
    //getters and abstract setters
    public static WsdiseFragmentDiseaseDiagBinding inflate(android.view.LayoutInflater inflater, android.view.ViewGroup root, boolean attachToRoot) {
        return inflate(inflater, root, attachToRoot, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    public static WsdiseFragmentDiseaseDiagBinding inflate(android.view.LayoutInflater inflater) {
        return inflate(inflater, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    public static WsdiseFragmentDiseaseDiagBinding bind(android.view.View view) {
        return null;
    }
    public static WsdiseFragmentDiseaseDiagBinding inflate(android.view.LayoutInflater inflater, android.view.ViewGroup root, boolean attachToRoot, android.databinding.DataBindingComponent bindingComponent) {
        return null;
    }
    public static WsdiseFragmentDiseaseDiagBinding inflate(android.view.LayoutInflater inflater, android.databinding.DataBindingComponent bindingComponent) {
        return null;
    }
    public static WsdiseFragmentDiseaseDiagBinding bind(android.view.View view, android.databinding.DataBindingComponent bindingComponent) {
        return null;
    }
}