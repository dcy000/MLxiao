package com.example.han.referralproject.activity;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.GridView;
import android.widget.Toast;

import com.example.han.referralproject.R;
import com.example.han.referralproject.adapter.DiseaseShowAdapter;
import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.network.NetworkManager;

public class PreviousHistoryActivity extends BaseActivity implements View.OnClickListener{
    private DiseaseShowAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_previous_history);
        GridView mGridView = (GridView) findViewById(R.id.gv_content);
        mAdapter = new DiseaseShowAdapter(mContext);
        mGridView.setAdapter(mAdapter);
        findViewById(R.id.tv_next).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_next:
                showLoadingDialog(getString(R.string.do_uploading));
                NetworkApi.setUserMh(mAdapter.getMh(), new NetworkManager.SuccessCallback<String>() {
                    @Override
                    public void onSuccess(String response) {
                        hideLoadingDialog();
                    }
                }, new NetworkManager.FailedCallback() {
                    @Override
                    public void onFailed(String message) {
                        hideLoadingDialog();
                    }
                });
                Toast.makeText(mContext, mAdapter.getMh(), Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
