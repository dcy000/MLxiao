package com.example.han.referralproject.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.han.referralproject.R;
import com.example.han.referralproject.bean.UserInfoBean;
import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.network.NetworkManager;
import com.example.han.referralproject.util.LocalShared;

import java.util.ArrayList;

public class RegisterActivity extends BaseActivity implements View.OnClickListener{
    private EditText mNameEt, mAddressEt, mTelephoneEt, mPwdEt;
    private Spinner sexSpinner, yearSpinner, monthSpinner;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
    }

    private void initView() {
        mNameEt = (EditText) findViewById(R.id.et_name);
        mAddressEt = (EditText) findViewById(R.id.et_address);
        mTelephoneEt = (EditText) findViewById(R.id.et_telephone);
        mPwdEt = (EditText) findViewById(R.id.et_pwd);
        findViewById(R.id.tv_next).setOnClickListener(this);
        findViewById(R.id.iv_back).setOnClickListener(this);
        sexSpinner = (Spinner) findViewById(R.id.sp_sex);
        sexSpinner.setAdapter(new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_dropdown_item, mResources.getStringArray(R.array.sex_array)));
//        sexSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
        yearSpinner = (Spinner) findViewById(R.id.sp_year);
        ArrayList<String> yearList = new ArrayList<>();
        for (int i = 1900 ; i < 2018; i ++){
            yearList.add(String.valueOf(i));
        }
        yearSpinner.setAdapter(new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_dropdown_item, yearList));
        yearSpinner.setSelection(90);
        monthSpinner = (Spinner) findViewById(R.id.sp_month);
        ArrayList<String> monthList = new ArrayList<>();
        for (int i = 1 ; i < 13; i ++){
            monthList.add(String.valueOf(i));
        }
        monthSpinner.setAdapter(new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_dropdown_item, monthList));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_next:
                if (!checkInputInfo()){
                    return;
                }
                showLoadingDialog(getString(R.string.do_register));
                NetworkApi.registerUser(mNameEt.getText().toString(), sexSpinner.getSelectedItem().toString(),
                        mAddressEt.getText().toString(), mTelephoneEt.getText().toString(), mPwdEt.getText().toString(),
                        new NetworkManager.SuccessCallback<UserInfoBean>() {
                            @Override
                            public void onSuccess(UserInfoBean response) {
                                hideLoadingDialog();
                                LocalShared.getInstance(mContext).setUserInfo(response);
                                startActivity(new Intent(mContext, PreviousHistoryActivity.class));
                            }
                        }, new NetworkManager.FailedCallback() {
                            @Override
                            public void onFailed(String message) {
                                hideLoadingDialog();
                                Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                            }
                        });
                break;
        }
    }

    private boolean checkInputInfo() {
        if (TextUtils.isEmpty(mNameEt.getText())){
            Toast.makeText(mContext, R.string.empty_name, Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(mAddressEt.getText())){
            Toast.makeText(mContext, R.string.empty_name, Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(mTelephoneEt.getText())){
            Toast.makeText(mContext, R.string.empty_name, Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(mPwdEt.getText())){
            Toast.makeText(mContext, R.string.empty_name, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
