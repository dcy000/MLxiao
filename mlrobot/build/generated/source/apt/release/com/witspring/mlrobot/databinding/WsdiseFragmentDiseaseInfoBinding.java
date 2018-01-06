package com.witspring.mlrobot.databinding;
import android.databinding.Bindable;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
public abstract class WsdiseFragmentDiseaseInfoBinding extends ViewDataBinding {
    public final android.widget.HorizontalScrollView wsdiseHsvPhotos;
    public final android.widget.LinearLayout wsdiseLlHead;
    public final android.widget.LinearLayout wsdiseLlPhotos;
    public final android.widget.TextView wsdiseTvArrLeft;
    public final android.widget.TextView wsdiseTvArrRight;
    public final android.widget.TextView wsdiseTvInfo;
    // variables
    protected WsdiseFragmentDiseaseInfoBinding(android.databinding.DataBindingComponent bindingComponent, android.view.View root_, int localFieldCount
        , android.widget.HorizontalScrollView wsdiseHsvPhotos
        , android.widget.LinearLayout wsdiseLlHead
        , android.widget.LinearLayout wsdiseLlPhotos
        , android.widget.TextView wsdiseTvArrLeft
        , android.widget.TextView wsdiseTvArrRight
        , android.widget.TextView wsdiseTvInfo
    ) {
        super(bindingComponent, root_, localFieldCount);
        this.wsdiseHsvPhotos = wsdiseHsvPhotos;
        this.wsdiseLlHead = wsdiseLlHead;
        this.wsdiseLlPhotos = wsdiseLlPhotos;
        this.wsdiseTvArrLeft = wsdiseTvArrLeft;
        this.wsdiseTvArrRight = wsdiseTvArrRight;
        this.wsdiseTvInfo = wsdiseTvInfo;
    }
    //getters and abstract setters
    public static WsdiseFragmentDiseaseInfoBinding inflate(android.view.LayoutInflater inflater, android.view.ViewGroup root, boolean attachToRoot) {
        return inflate(inflater, root, attachToRoot, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    public static WsdiseFragmentDiseaseInfoBinding inflate(android.view.LayoutInflater inflater) {
        return inflate(inflater, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    public static WsdiseFragmentDiseaseInfoBinding bind(android.view.View view) {
        return null;
    }
    public static WsdiseFragmentDiseaseInfoBinding inflate(android.view.LayoutInflater inflater, android.view.ViewGroup root, boolean attachToRoot, android.databinding.DataBindingComponent bindingComponent) {
        return null;
    }
    public static WsdiseFragmentDiseaseInfoBinding inflate(android.view.LayoutInflater inflater, android.databinding.DataBindingComponent bindingComponent) {
        return null;
    }
    public static WsdiseFragmentDiseaseInfoBinding bind(android.view.View view, android.databinding.DataBindingComponent bindingComponent) {
        return null;
    }
}