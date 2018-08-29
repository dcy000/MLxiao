package com.gcml.old.auth.profile.otherinfo.dialog;

import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.network.NetworkApi;
import com.gcml.lib_utils.display.ToastUtils;
import com.iflytek.synthetize.MLVoiceSynthetize;
import com.medlink.danbogh.utils.Handlers;

/**
 * Created by lenovo on 2018/8/29.
 */

public class SMSVerificationDialog extends DialogFragment implements View.OnClickListener {

    private Bundle arguments;
    /**
     * 请输入验证码
     */
    private EditText etCode;
    /**
     * 发送验证码
     */
    private TextView tvSendCode;
    /**
     * 下一步
     */
    private TextView tvNext;
    /**
     * 请输入手机158****1914收到的验证码 验证完成后即可修改头像
     */
    private TextView tvPhone;
    public static final String KEY_PHONE = "phone";
    private String phoneNumber;
    private String code;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(android.support.v4.app.DialogFragment.STYLE_NO_TITLE, R.style.XDialog);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sms_vertification_dialog, container, false);
        arguments = getArguments();
        initView(view);
        initPhoneNoticeInfo();
        return view;
    }

    private void initView(View view) {
        etCode = view.findViewById(R.id.et_code);
        tvSendCode = view.findViewById(R.id.tv_send_code);
        tvSendCode.setOnClickListener(this);
        tvNext = view.findViewById(R.id.tv_next);
        tvNext.setOnClickListener(this);
        tvPhone = view.findViewById(R.id.tv_phone);
    }

    private void initPhoneNoticeInfo() {
        phoneNumber = arguments.getString(KEY_PHONE);
        String phoneStar = phoneNumber.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
        tvPhone.setText("请输入手机" + phoneStar + "收到的验证码验证完成后即可修改头像");
        tvSendCode.setSelected(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.tv_send_code:
                sendCode();
                break;
            case R.id.tv_next:
                next();
                break;
        }
    }

    private void next() {
        final String phoneCode = etCode.getText().toString();
        if (TextUtils.isEmpty(phoneCode)) {
            speak("请输入手机验证码");
            return;
        }
        if (phoneCode.equals(code)) {
            if (listener != null) {
                listener.onClickNext();
                dismiss();
            }

        } else {
            speak("验证码错误");
        }
    }

    public interface onNextClickListener {
        void onClickNext();
    }

    public void setListener(onNextClickListener listener) {
        this.listener = listener;
    }

    private onNextClickListener listener;

    private void speak(String text) {
        MLVoiceSynthetize.startSynthesize(getContext(), text, false);
    }

    private void sendCode() {
        //请求验证码
        NetworkApi.getCode(phoneNumber, codeJson -> {
            SMSVerificationDialog.this.code = codeJson;
            if (codeJson != null) {
                updateCountDownUi();
                ToastUtils.showShort("获取验证码成功");
            } else {
                ToastUtils.showShort("获取验证码失败");
            }

        }, message -> ToastUtils.showShort("获取验证码失败"));

    }


    private void updateCountDownUi() {
        tvSendCode.setSelected(false);
        Handlers.ui().postDelayed(new Runnable() {
            @Override
            public void run() {
                count--;
                if (count <= 0) {
                    tvSendCode.setSelected(true);
                    tvSendCode.setText("发送验证码");
                    count = 60;
                    return;
                }
                tvSendCode.setText(count + "秒重发");
                Handlers.ui().postDelayed(this, 1000);
            }
        }, 1000);
    }

    private int count = 60;

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        Handlers.ui().removeCallbacksAndMessages(null);
    }


}
