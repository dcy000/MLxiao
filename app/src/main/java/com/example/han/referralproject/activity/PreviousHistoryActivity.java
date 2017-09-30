package com.example.han.referralproject.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.GridView;
import android.widget.Toast;

import com.example.han.referralproject.R;
import com.example.han.referralproject.adapter.DiseaseShowAdapter;
import com.example.han.referralproject.facerecognition.RegisterVideoActivity;
import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.network.NetworkManager;
import com.example.han.referralproject.speechsynthesis.PinYinUtils;

public class PreviousHistoryActivity extends BaseActivity implements View.OnClickListener {
    private DiseaseShowAdapter mAdapter;
    public GridView mGridView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_previous_history);
        diseaseArray = getResources().getStringArray(R.array.disease_type);
        mGridView = (GridView) findViewById(R.id.gv_content);
        mAdapter = new DiseaseShowAdapter(mContext);
        mGridView.setAdapter(mAdapter);
        findViewById(R.id.tv_next).setOnClickListener(this);
        findViewById(R.id.iv_back).setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        speak(R.string.tips_disease);
    }

    private String[] diseaseArray;

    public static final String REGEX_GO_NEXT = ".*xiayibu.*";


    @Override
    protected void onSpeakListenerResult(String result) {
        Toast.makeText(mContext, result, Toast.LENGTH_SHORT).show();
        String inSpell = PinYinUtils.converterToSpell(result);

        for (int i = 0; i < diseaseArray.length; i++) {
            String spell = PinYinUtils.converterToSpell(diseaseArray[i]);
            if (inSpell.contains(spell)) {
                mGridView.getChildAt(i).performClick();
                return;
            }
        }

        if (inSpell.matches(REGEX_GO_NEXT)) {
            findViewById(R.id.tv_next).performClick();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_next:
                if (TextUtils.isEmpty(mAdapter.getMh())) {
                    return;
                }
                showLoadingDialog(getString(R.string.do_uploading));
                NetworkApi.setUserMh(mAdapter.getMh(), new NetworkManager.SuccessCallback<String>() {
                    @Override
                    public void onSuccess(String response) {
                        hideLoadingDialog();
                        startActivity(new Intent(mContext, RegisterVideoActivity.class));
//                        startActivity(new Intent(mContext, RecoDocActivity.class));
//                        finish();
                    }
                }, new NetworkManager.FailedCallback() {
                    @Override
                    public void onFailed(String message) {
                        hideLoadingDialog();
                    }
                });
                break;
        }
    }
}
