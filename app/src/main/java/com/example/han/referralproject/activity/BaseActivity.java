package com.example.han.referralproject.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

import com.example.han.referralproject.R;

public class BaseActivity extends FragmentActivity {
    protected Context mContext;
    protected Resources mResources;
    private ProgressDialog mDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        mResources = getResources();
    }

    protected void showLoadingDialog(String message){
        if (mDialog == null){
            mDialog = new ProgressDialog(mContext);
            mDialog.setCanceledOnTouchOutside(false);
            mDialog.setIndeterminate(true);
            mDialog.setMessage(message);
        }
        mDialog.show();
    }


    protected void hideLoadingDialog(){
        if (mDialog == null){
            return;
        }
        mDialog.dismiss();
    }
}
