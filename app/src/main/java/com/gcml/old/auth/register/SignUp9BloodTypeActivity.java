package com.gcml.old.auth.register;

import android.content.Intent;

import com.example.han.referralproject.R;
import com.example.han.referralproject.util.LocalShared;
import com.gcml.lib_utils.display.ToastUtils;

import java.util.ArrayList;
import java.util.List;

public class SignUp9BloodTypeActivity extends SignUp7HeightActivity {

    public List<String> mStrings;

    @Override
    protected void initView() {
        selectedPosition = 0;
        super.initView();
        tvTitle.setText("您的血型");
        tvUnit.setText("型");
    }

    @Override
    protected List<String> getStrings() {
        mStrings = new ArrayList<>();
        mStrings.add("AB");
        mStrings.add("A");
        mStrings.add("B");
        mStrings.add("O");
        return mStrings;
    }

    @Override
    public void onTvGoForwardClicked() {
        String bloodType = mStrings.get(selectedPosition);
        LocalShared.getInstance(this.getApplicationContext()).setSignUpBloodType(bloodType);
        Intent intent = new Intent(this, SignUp10EatActivity.class);
        startActivity(intent);
    }

    @Override
    protected int geTip() {
        return R.string.sign_up_blood_type_tip;
    }

    public static final String REGEX_IN_GO_BACK = ".*(上一步|上一部|后退|返回).*";
    public static final String REGEX_IN_GO_FORWARD = ".*(下一步|下一部|确定|完成).*";

    @Override
    protected void onSpeakListenerResult(String result) {
        ToastUtils.showShort(result);

        if (result.matches(REGEX_IN_GO_BACK)) {
            onTvGoBackClicked();
            return;
        }
        if (result.matches(REGEX_IN_GO_FORWARD)) {
            onTvGoForwardClicked();
            return;
        }

        int size = mStrings == null ? 0 : mStrings.size();
        String in = result.toLowerCase();
        for (int i = 0; i < size; i++) {
            String bloodType = mStrings.get(i);
            if (in.contains(bloodType.toLowerCase())) {
                rvContent.smoothScrollToPosition(i);
                return;
            }
        }
    }
}
