package com.gcml.module_blutooth_devices.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gcml.lib_utils.click.ClickEventListener;

public abstract class BluetoothBaseFragment extends Fragment {
    protected View view=null;
    protected boolean isMeasureFinishedOfThisTime =false;
    protected Context mContext;
    protected Activity mActivity;
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        //全局控制上下文 子类统一使用该应用，避免异步任务结束后getActivity引起的空指针异常，但同时这种做法有内存泄漏的风险
        this.mActivity=activity;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext=context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(initLayout(), container, false);
            initView(view, getArguments());
        }
        return view;
    }

    protected abstract int initLayout();

    protected abstract void initView(View view, Bundle bundle);

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (view != null) {
            ((ViewGroup) view.getParent()).removeView(view);
        }
    }

    /**
     * Find出来的View，自带防抖功能
     */
    public <T extends View> T findClickView(int id) {

        T viewA = (T) view.findViewById(id);
        viewA.setOnClickListener(new ClickEventListener(this));
        return viewA;
    }

    protected DealVoiceAndJump dealVoiceAndJump;

    public void setOnDealVoiceAndJumpListener(DealVoiceAndJump dealVoiceAndJump) {
        this.dealVoiceAndJump = dealVoiceAndJump;
    }
    protected FragmentChanged fragmentChanged;
    public void setOnFragmentChangedListener(FragmentChanged fragmentChanged){
        this.fragmentChanged=fragmentChanged;
    }
    protected void clickHealthHistory(View view){}
    protected void clickVideoDemo(View view){}
    protected void onMeasureFinished(String...results){}
}
