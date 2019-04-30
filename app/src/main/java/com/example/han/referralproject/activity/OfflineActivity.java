package com.example.han.referralproject.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.example.han.referralproject.R;
import com.gcml.common.router.AppRouter;
import com.gcml.common.utils.PinYinUtils;
import com.sjtu.yifei.annotation.Route;
import com.sjtu.yifei.route.Routerfit;
@Route(path = "/app/activity/offline/activity")
public class OfflineActivity extends BaseActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline);
        mToolbar.setVisibility(View.VISIBLE);
        mLeftText.setText("线下绑定");
        mRightText.setText("暂不绑定");
        mRightView.setVisibility(View.GONE);
        mRightText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Routerfit.register(AppRouter.class).skipMainActivity();
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        setDisableGlobalListen(true);
        speak(getString(R.string.user_help));
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
