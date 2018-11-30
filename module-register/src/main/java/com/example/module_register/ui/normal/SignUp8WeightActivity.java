package com.example.module_register.ui.normal;

import android.content.Intent;

import com.example.module_register.R;
import com.gzq.lib_core.base.Box;
import com.gzq.lib_core.bean.UserInfoBean;
import com.gzq.lib_core.utils.DataUtils;
import com.gzq.lib_core.utils.PinYinUtils;
import com.gzq.lib_core.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

public class SignUp8WeightActivity extends SignUp7HeightActivity {

    public List<String> mStrings;

    @Override
    public void initView() {
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
        String weight = mStrings.get(selectedPosition);
        cacheUserInfo(weight);
        Intent intent = new Intent(this, SignUp9BloodTypeActivity.class);
        startActivity(intent);
    }

    private void cacheUserInfo(String weight) {
        UserInfoBean user = Box.getSessionManager().getUser();
        if (user == null) {
            user = new UserInfoBean();
        }
        user.weight = weight;
        Box.getSessionManager().setUser(user);
    }

    @Override
    protected int geTip() {
        return R.string.sign_up_weight_tip;
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

        String in = DataUtils.isInteger(result) ? result : DataUtils.removeNonnumeric(DataUtils.chineseMapToNumber(result));
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
