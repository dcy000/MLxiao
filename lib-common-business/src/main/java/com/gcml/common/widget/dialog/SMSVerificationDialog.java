package com.gcml.common.widget.dialog;

import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.billy.cc.core.component.CC;
import com.gcml.common.business.R;
import com.gcml.common.repository.utils.DefaultObserver;
import com.gcml.common.utils.RxUtils;
import com.gcml.common.utils.display.KeyboardUtils;
import com.gcml.common.utils.display.ToastUtils;
import com.iflytek.synthetize.MLVoiceSynthetize;

import java.util.Locale;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.disposables.Disposables;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

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
    private ImageView close;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(android.support.v4.app.DialogFragment.STYLE_NO_TITLE, R.style.XDialog);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.common_sms_vertification_dialog, container, false);
        arguments = getArguments();
        initView(view);
        initPhoneNoticeInfo();
        return view;
    }

    private void initKeyBoard() {
        KeyboardUtils.hideKeyboard(getActivity(), etCode);
    }

    private void initView(View view) {
        etCode = view.findViewById(R.id.et_code);
        tvSendCode = view.findViewById(R.id.tv_send_code);
        tvSendCode.setOnClickListener(this);
        tvNext = view.findViewById(R.id.tv_next);
        tvNext.setOnClickListener(this);
        tvPhone = view.findViewById(R.id.tv_phone);
        close = view.findViewById(R.id.iv_close);
        close.setOnClickListener(v -> {
            dismiss();
        });

//        initKeyBoard();
    }

    private void initPhoneNoticeInfo() {
        phoneNumber = arguments.getString(KEY_PHONE);
        String phoneStar = phoneNumber.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
        tvPhone.setText("请输入手机" + phoneStar + "收到的验证码验证完成后即可修改头像");
        tvSendCode.setSelected(true);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.tv_send_code) {
            sendCode();
        } else if (i == R.id.tv_next) {
            next();
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
        MLVoiceSynthetize.startSynthesize(getActivity().getApplicationContext(), text, false);
    }

    private void sendCode() {
        tvSendCode.setEnabled(false);
        Observable<String> rxPhone = CC.obtainBuilder("com.gcml.auth.fetchCode")
                .addParam("phone", phoneNumber)
                .build()
                .call()
                .getDataItem("data");
        fetchCodeInternal(rxPhone, phoneNumber);
    }

    private void fetchCodeInternal(Observable<String> rxPhone, String phone) {
        fetchCodeDisposable.dispose();
        fetchCodeDisposable = rxPhone.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        startTimer();
                    }
                })
                .subscribeWith(new DefaultObserver<String>() {
                    @Override
                    public void onNext(String code) {
                        SMSVerificationDialog.this.code = code;
                        ToastUtils.showShort("获取验证码成功");
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        ToastUtils.showShort("获取验证码失败");
                        countDownDisposable.dispose();
                    }
                });
    }

    private Disposable fetchCodeDisposable = Disposables.empty();
    private Disposable countDownDisposable = Disposables.empty();

    private void startTimer() {
        countDownDisposable.dispose();
        countDownDisposable = RxUtils.rxCountDown(1, 60)
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        tvSendCode.setEnabled(false);
                    }
                })
                .doOnTerminate(new Action() {
                    @Override
                    public void run() throws Exception {
                        tvSendCode.setText("获取验证码");
                        tvSendCode.setEnabled(true);
                    }
                })
                .doOnDispose(new Action() {
                    @Override
                    public void run() throws Exception {
                        tvSendCode.setText("获取验证码");
                        tvSendCode.setEnabled(true);
                    }
                })
                .subscribeWith(new DefaultObserver<Integer>() {
                    @Override
                    public void onNext(Integer integer) {
                        tvSendCode.setText(
                                String.format(Locale.getDefault(), "已发送（%d）", integer));
                    }
                });
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        fetchCodeDisposable.dispose();
        countDownDisposable.dispose();
        listener = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        getDialog().setCancelable(false);
        getDialog().setCanceledOnTouchOutside(false);
    }
}
