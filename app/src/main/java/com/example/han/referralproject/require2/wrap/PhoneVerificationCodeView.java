package com.example.han.referralproject.require2.wrap;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.example.han.referralproject.R;
import com.medlink.danbogh.utils.Utils;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by lenovo on 2018/7/11.
 */

public class PhoneVerificationCodeView extends LinearLayout {
    @BindView(R.id.tv_phone)
    EditText tvPhone;
    @BindView(R.id.iv_delete)
    ImageView ivDelete;
    @BindView(R.id.et_code)
    EditText etCode;
    @BindView(R.id.tv_send_code)
    TextView tvSendCode;
    private Context context;

    public PhoneVerificationCodeView(Context context) {
        this(context, null);
    }

    public PhoneVerificationCodeView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initView();
    }

    private void initView() {
        View view = View.inflate(context, R.layout.phone_verification_view, null);
        ButterKnife.bind(this, view);
        initEvent();
        addView(view);
    }

    /**
     * 联动逻辑处理
     */
    private void initEvent() {
        tvPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(tvPhone.getText().toString())) {
                    ivDelete.setVisibility(GONE);
                } else {
                    ivDelete.setVisibility(VISIBLE);
                }

                if (Utils.isValidPhone(s.toString())) {
                    tvSendCode.setSelected(true);
                } else {
                    tvSendCode.setSelected(false);
                }

            }
        });
    }

    @OnClick({R.id.iv_delete, R.id.tv_send_code})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_delete:
                clearPhoneNumber();
                break;
            case R.id.tv_send_code:
                sendCode();
                break;
        }
    }

    private void sendCode() {
        if (listener == null) {
            return;
        }

        String phone = tvPhone.getText().toString();
        if (TextUtils.isEmpty(phone)) {
            Toast.makeText(context, "主人,请输入手机号", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!tvSendCode.isSelected()) {
            Toast.makeText(context, "主人,您输入的手机号有误", Toast.LENGTH_SHORT).show();
            return;
        }
        listener.onSendCode(phone);
        tvSendCode.setSelected(false);
        postDelayed(new Runnable() {
            @Override
            public void run() {
                count--;
                if (count <= 0) {
                    tvSendCode.setSelected(true);
                    tvSendCode.setText("发送验证码");
                    count = 5;
                    return;
                }
                tvSendCode.setText(count + "秒重发");
                postDelayed(this, 1000);
            }
        }, 1000);

    }

    private int count = 5;


    private void clearPhoneNumber() {
        tvPhone.setText("");
    }


    private OnSendClickListener listener;

    public String getPhone() {
        return tvPhone.getText().toString();
    }


    public interface OnSendClickListener {
        void onSendCode(String phone);
    }


    public String getCode(){
        return etCode.getText().toString().trim();
    }

    public void setListener(OnSendClickListener listener) {
        this.listener = listener;
    }
}
