package com.witspring.mlrobot.databinding;
import android.databinding.Bindable;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
public abstract class WsdiseActivityDiseaseBinding extends ViewDataBinding {
    public final android.widget.RelativeLayout rlTab1;
    public final android.widget.RelativeLayout rlTab2;
    public final android.widget.RelativeLayout rlTab3;
    public final android.widget.RelativeLayout rlTab4;
    public final android.view.View toolbar;
    public final android.widget.FrameLayout wsdiseFlContent;
    public final com.witspring.view.CenterRadioButton wsdiseRb1;
    public final com.witspring.view.CenterRadioButton wsdiseRb2;
    public final com.witspring.view.CenterRadioButton wsdiseRb3;
    public final com.witspring.view.CenterRadioButton wsdiseRb4;
    public final android.widget.RadioGroup wsdiseRgLeft;
    // variables
    protected WsdiseActivityDiseaseBinding(android.databinding.DataBindingComponent bindingComponent, android.view.View root_, int localFieldCount
        , android.widget.RelativeLayout rlTab1
        , android.widget.RelativeLayout rlTab2
        , android.widget.RelativeLayout rlTab3
        , android.widget.RelativeLayout rlTab4
        , android.view.View toolbar
        , android.widget.FrameLayout wsdiseFlContent
        , com.witspring.view.CenterRadioButton wsdiseRb1
        , com.witspring.view.CenterRadioButton wsdiseRb2
        , com.witspring.view.CenterRadioButton wsdiseRb3
        , com.witspring.view.CenterRadioButton wsdiseRb4
        , android.widget.RadioGroup wsdiseRgLeft
    ) {
        super(bindingComponent, root_, localFieldCount);
        this.rlTab1 = rlTab1;
        this.rlTab2 = rlTab2;
        this.rlTab3 = rlTab3;
        this.rlTab4 = rlTab4;
        this.toolbar = toolbar;
        this.wsdiseFlContent = wsdiseFlContent;
        this.wsdiseRb1 = wsdiseRb1;
        this.wsdiseRb2 = wsdiseRb2;
        this.wsdiseRb3 = wsdiseRb3;
        this.wsdiseRb4 = wsdiseRb4;
        this.wsdiseRgLeft = wsdiseRgLeft;
    }
    //getters and abstract setters
    public static WsdiseActivityDiseaseBinding inflate(android.view.LayoutInflater inflater, android.view.ViewGroup root, boolean attachToRoot) {
        return inflate(inflater, root, attachToRoot, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    public static WsdiseActivityDiseaseBinding inflate(android.view.LayoutInflater inflater) {
        return inflate(inflater, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    public static WsdiseActivityDiseaseBinding bind(android.view.View view) {
        return null;
    }
    public static WsdiseActivityDiseaseBinding inflate(android.view.LayoutInflater inflater, android.view.ViewGroup root, boolean attachToRoot, android.databinding.DataBindingComponent bindingComponent) {
        return null;
    }
    public static WsdiseActivityDiseaseBinding inflate(android.view.LayoutInflater inflater, android.databinding.DataBindingComponent bindingComponent) {
        return null;
    }
    public static WsdiseActivityDiseaseBinding bind(android.view.View view, android.databinding.DataBindingComponent bindingComponent) {
        return null;
    }
}