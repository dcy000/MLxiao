package com.example.han.referralproject.settting.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.bean.NDialog1;
import com.example.han.referralproject.settting.wrap.SignView;

import butterknife.ButterKnife;

public class ScreenTouchActivity extends BaseActivity {

    SignView signView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_touch);
        ButterKnife.bind(this);
        signView = findViewById(R.id.signView);

//        signView.setOnDoubleClickListener(new SignView.OnDoubleClickListener() {
//            @Override
//            public void onDoubleClick(View v) {
//                backActivity();
//            }
//        });

        signView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                backActivity();
                return false;
            }
        });
    }

    private void backActivity() {
        NDialog1 dialog = new NDialog1(this);
        dialog.setMessageCenter(true)
                .setMessage("是否退出当前页面?")
                .setMessageSize(35)
                .setCancleable(false)
                .setButtonCenter(true)
                .setPositiveTextColor(getResources().getColor(R.color.toolbar_bg))
                .setNegativeTextColor(Color.parseColor("#999999"))
                .setButtonSize(40)
                .setOnConfirmListener(new NDialog1.OnConfirmListener() {
                    @Override
                    public void onClick(int which) {
                        if (which == 1) {
                            finish();
                        }
                    }
                }).create(NDialog1.CONFIRM).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
