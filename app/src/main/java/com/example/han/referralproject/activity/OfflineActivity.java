package com.example.han.referralproject.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.example.han.referralproject.MainActivity;
import com.example.han.referralproject.R;
import com.example.han.referralproject.util.PinYinUtils;
import com.iflytek.synthetize.MLVoiceSynthetize;

public class OfflineActivity extends BaseActivity{


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline);
        mToolbar.setVisibility(View.VISIBLE);
        mLeftText.setText("线下签约");
        mRightText.setText("暂不签约");
        mRightView.setVisibility(View.GONE);
        mRightText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OfflineActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        setDisableWakeup(true);
        MLVoiceSynthetize.startSynthesize(getString(R.string.user_help));
    }


    public static final String REGEX_BACK = ".*(fanhui|shangyibu).*";

    @Override
    protected void onSpeakListenerResult(String result) {
        Toast.makeText(mContext, result, Toast.LENGTH_SHORT).show();
        String inSpell = PinYinUtils.converterToSpell(result);

        if (!TextUtils.isEmpty(inSpell) && inSpell.matches(REGEX_BACK)) {
            finish();
        }
    }
}
