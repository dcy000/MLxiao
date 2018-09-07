package run;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.gcml.mall.R;
import com.gcml.mall.ui.RechargeActivity;

public class TestMallActivity extends AppCompatActivity implements View.OnClickListener {

    TextView mTitle, mAction;
    ImageView mWifi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_mall);
        mTitle = findViewById(R.id.tv_mall_title);
        mAction = findViewById(R.id.tv_mall_action);
        mWifi = findViewById(R.id.iv_mall_wifi);
        Animation rotateAnimation = AnimationUtils.loadAnimation(this, R.anim.common_wifi_refresh);
        mWifi.setAnimation(rotateAnimation);
        mWifi.postDelayed(new Runnable() {
            @Override
            public void run() {
                mWifi.clearAnimation();
            }
        }, 5000);
        mAction.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_mall_action:
                startActivity(new Intent(this, RechargeActivity.class));
        }
    }
}
