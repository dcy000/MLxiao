package com.example.han.referralproject.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.example.han.referralproject.R;
import com.example.han.referralproject.speechsynthesis.PinYinUtils;

public class OfflineActivity extends BaseActivity implements View.OnClickListener{

    public View mBack;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline);
        setDisableGlobalListen(true);
        mBack = findViewById(R.id.view_back);
        mBack.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        speak(getString(R.string.user_help));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.view_back:
                finish();
                break;
        }
    }

    public static final String REGEX_BACK = ".*(fanhui|shangyibu).*";

    @Override
    protected void onSpeakListenerResult(String result) {
        Toast.makeText(mContext, result, Toast.LENGTH_SHORT).show();
        String inSpell = PinYinUtils.converterToSpell(result);

        if (!TextUtils.isEmpty(inSpell) && inSpell.matches(REGEX_BACK)) {
            mBack.performClick();
        }
    }
}
