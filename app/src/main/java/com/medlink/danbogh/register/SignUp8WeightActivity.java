package com.medlink.danbogh.register;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.han.referralproject.R;
import com.medlink.danbogh.utils.T;
import com.medlink.danbogh.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class SignUp8WeightActivity extends SignUp7HeightActivity {

    public List<String> mStrings;

    @Override
    protected void initView() {
        super.initView();
        tvTitle.setText("您的体重");
        tvUnit.setText("kg");
    }

    @Override
    protected List<String> getStrings() {
        mStrings = new ArrayList<>();
        for (int i = 35; i < 150; i++) {
            mStrings.add(String.valueOf(i));
        }
        return mStrings;
    }

    @Override
    public void onTvGoForwardClicked() {
        Intent intent = new Intent(this, SignUp9BloodTypeActivity.class);
        startActivity(intent);
    }

    @Override
    protected int geTip() {
        return R.string.sign_up_weight_tip;
    }

    public static final String REGEX_IN_GO_BACK = ".*(上一步|上一部|后退|返回).*";
    public static final String REGEX_IN_GO_FORWARD = ".*(下一步|下一部|确定|完成).*";

    @Override
    protected void onSpeakListenerResult(String result) {
        T.show(result);

        if (result.matches(REGEX_IN_GO_BACK)) {
            onTvGoBackClicked();
            return;
        }
        if (result.matches(REGEX_IN_GO_FORWARD)) {
            onTvGoForwardClicked();
            return;
        }

        int size = mStrings == null ? 0 : mStrings.size();
        String in = Utils.chineseToNumber(result);
        for (int i = 0; i < size; i++) {
            String weight = mStrings.get(i);
            if (in.contains(weight)) {
                rvContent.smoothScrollToPosition(i);
                return;
            }
        }
    }
}
