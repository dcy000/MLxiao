package com.gzq.lib_bluetooth.common;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.gzq.lib_core.base.ui.BaseFragment;

public abstract class BaseBluetoothFragment extends BaseFragment {
    protected StateUpdate2Activity stateUpdate2Activity;
    protected FragmentReplaced fragmentReplaced;
    public void setStateUpdate2ActivityListener(StateUpdate2Activity stateUpdate2Activity){
        this.stateUpdate2Activity=stateUpdate2Activity;
    }

    public void setFragmentReplacedListener(FragmentReplaced fragmentReplaced) {
        this.fragmentReplaced = fragmentReplaced;
    }

    public interface StateUpdate2Activity {
        void onStateChanged(String state);
    }

    public interface FragmentReplaced {
        void onFragmentChanged(Fragment fragment, Bundle bundle);
    }
}
