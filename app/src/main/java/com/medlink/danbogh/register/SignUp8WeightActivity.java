package com.medlink.danbogh.register;

import android.content.Intent;
import android.view.View;

import com.example.han.referralproject.R;
import com.example.han.referralproject.speechsynthesis.PinYinUtils;
import com.example.han.referralproject.util.LocalShared;
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
        initTitle();
    }

    private void initTitle() {
        mTitleText.setText("问诊");
        mToolbar.setVisibility(View.VISIBLE);
        mRightText.setVisibility(View.GONE);
        mRightView.setVisibility(View.GONE);
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
        String weight = mStrings.get(selectedPosition);
        LocalShared.getInstance(this.getApplicationContext()).setSignUpWeight(Integer.valueOf(weight));
        Intent intent = new Intent(this, SignUp3AddressActivity.class);
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

        String in = Utils.isNumeric(result) ? result : Utils.removeNonnumeric(Utils.chineseMapToNumber(result));
        int size = mStrings == null ? 0 : mStrings.size();
        for (int i = 0; i < size; i++) {
            String weight = mStrings.get(i);
            if (in.equals(weight)) {
                rvContent.smoothScrollToPosition(i);
                return;
            }
        }

        String inSpell = PinYinUtils.converterToSpell(result);
        if (inSpell.contains("qishi")) {
            if (i70 == -1) {
                for (int i = 0; i < size; i++) {
                    String weight = mStrings.get(i);
                    if (weight.equals("70")) {
                        i70 = i;
                        rvContent.smoothScrollToPosition(i70);
                        return;
                    }
                }
            } else {
                rvContent.smoothScrollToPosition(i70);
            }
        }
    }

    private int i70 = 25;
}
