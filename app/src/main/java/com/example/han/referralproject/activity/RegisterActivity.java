package com.example.han.referralproject.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.han.referralproject.R;
import com.example.han.referralproject.bean.UserInfoBean;
import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.network.NetworkManager;
import com.example.han.referralproject.speechsynthesis.PinYinUtils;
import com.example.han.referralproject.util.LocalShared;
import com.mob.MobSDK;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

public class RegisterActivity extends BaseActivity implements View.OnClickListener {
    private EditText mNameEt, mAddressEt, mTelephoneEt, mPwdEt, mIdCardEt;
    private Spinner sexSpinner, yearSpinner, monthSpinner;
    public Spinner mSpProvince;
    private Spinner mSpCity;
    private Spinner mSpCounty;

    private TextView mTextView;
    private EditText mEditText;

    String APPKEY = "21298843aea72";
    String APPSECRETE = "bd0b6925735818c881252c6245d9ea9d";
    int i = 30;
    public String phoneNums;


    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == -9) {
                mTextView.setText("重新发送(" + i + ")");
            } else if (msg.what == -8) {
                mTextView.setText("获取验证码");
                mTextView.setClickable(true);
                i = 30;
            } else {
                int event = msg.arg1;
                int result = msg.arg2;
                Object data = msg.obj;
                Log.e("event", "event=" + event);
                if (result == SMSSDK.RESULT_COMPLETE) {
                    if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                        Toast.makeText(getApplicationContext(), "验证码正确", Toast.LENGTH_SHORT).show();

                        if (!checkInputInfo()) {
                            return;
                        }
//                        showLoadingDialog(getString(R.string.do_register));
//                        NetworkApi.registerUser(mNameEt.getText().toString(), sexSpinner.getSelectedItem().toString(),
//                                mAddressEt.getText().toString(), mTelephoneEt.getText().toString(), mPwdEt.getText().toString(),
//                                mIdCardEt.getText().toString(),
//                                new NetworkManager.SuccessCallback<UserInfoBean>() {
//                                    @Override
//                                    public void onSuccess(UserInfoBean response) {
//                                        hideLoadingDialog();
//                                        LocalShared.getInstance(mContext).setUserInfo(response);
//                                        startActivity(new Intent(mContext, PreviousHistoryActivity.class));
//                                        finish();
//                                    }
//                                }, new NetworkManager.FailedCallback() {
//                                    @Override
//                                    public void onFailed(String message) {
//                                        hideLoadingDialog();
//                                        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
//                                        speak("主人," + message);
//                                    }
//                                });

                    } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                        Toast.makeText(getApplicationContext(), "正在获取验证码", Toast.LENGTH_SHORT).show();
                    } else {
                        ((Throwable) data).printStackTrace();
                    }
                } else if (result == SMSSDK.RESULT_ERROR) {
                    Toast.makeText(getApplicationContext(), "验证码错误", Toast.LENGTH_SHORT).show();
                    speak("验证码错误,请重新输入");
                }
            }
        }
    };
    public EditText mEtIdCard;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();

        mTextView = (TextView) findViewById(R.id.yanzheng);
        mEditText = (EditText) findViewById(R.id.text_yanzheng);
        mTextView.setOnClickListener(this);
        mEditText.setOnClickListener(this);

        mEtIdCard = (EditText) findViewById(R.id.et_id_card);

        MobSDK.init(this, APPKEY, APPSECRETE);
        EventHandler eventHandler = new EventHandler() {
            @Override
            public void afterEvent(int event, int result, Object data) {
                Message msg = new Message();
                msg.arg1 = event;
                msg.arg2 = result;
                msg.obj = data;
                handler.sendMessage(msg);
            }
        };
        //注册回调监听接口
        SMSSDK.registerEventHandler(eventHandler);
    }

    @Override
    protected void onResume() {
        super.onResume();
        speak(R.string.tips_register);
    }

    public static final String REGEX_IN_PHONE = ".*(shouji|dianhua)(hao|haoma)?(shi)?(\\d+)";
    public static final String REGEX_IN_NAME = ".*(我|名字|姓名)(是|叫)(.*)";
    public static final String REGEX_IN_ADDRESS = ".*(地址|住)(是|在)?(.*)";
    public static final String REGEX_IN_ID_CARD = ".*shenfenzheng(haoma)?(shi)?(\\d+).*";
    public static final String REGEX_FETCH_CODE = ".*(fasong|huoqu)yanzhengma.*";
    public static final String REGEX_IN_CODE = ".*yanzhengma.*(\\d{4}).*";
    public static final String REGEX_IN_PASSWORD = ".*mima(shi)?(.*)";
    public static final String REGEX_GO_NEXT = ".*xiayibu.*";

    @Override
    protected void onSpeakListenerResult(String result) {
//        Toast.makeText(mContext, result, Toast.LENGTH_SHORT).show();
        String in = PinYinUtils.converterToSpell(result);

        Pattern patternInName = Pattern.compile(REGEX_IN_NAME);
        Matcher matcherInName = patternInName.matcher(result);
        if (matcherInName.find()) {
            mNameEt.setText(matcherInName.group(matcherInName.groupCount()));
            return;
        }

        Pattern patternInAddress = Pattern.compile(REGEX_IN_ADDRESS);
        Matcher matcherInAddress = patternInAddress.matcher(result);
        if (matcherInAddress.find()) {
            mAddressEt.setText(matcherInAddress.group(matcherInAddress.groupCount()));
            return;
        }

        Pattern patternInIdCard = Pattern.compile(REGEX_IN_ID_CARD);
        Matcher matcherInIdCard = patternInIdCard.matcher(in);
        if (matcherInIdCard.find()) {
            mEtIdCard.setText(matcherInIdCard.group(matcherInIdCard.groupCount()));
            return;
        }

        Pattern patternInPhone = Pattern.compile(REGEX_IN_PHONE);
        Matcher matcherInPhone = patternInPhone.matcher(in);
        if (matcherInPhone.find()) {
            mTelephoneEt.setText(matcherInPhone.group(matcherInPhone.groupCount()));
            return;
        }

        if (in.matches(REGEX_FETCH_CODE)) {
            findViewById(R.id.yanzheng).performClick();
            return;
        }

        Pattern patternInCode = Pattern.compile(REGEX_IN_CODE);
        Matcher matcherInCode = patternInCode.matcher(in);
        if (matcherInCode.find()) {
            mEditText.setText(matcherInCode.group(matcherInCode.groupCount()));
            return;
        }

        Pattern patternInPassword = Pattern.compile(REGEX_IN_PASSWORD);
        Matcher matcherInPassword = patternInPassword.matcher(in);
        if (matcherInPassword.find()) {
            mPwdEt.setText(matcherInPassword.group(matcherInPassword.groupCount()));
            return;
        }

        if (in.matches(REGEX_GO_NEXT)) {
            findViewById(R.id.tv_next).performClick();
        }
    }

    private void initView() {
        mSpProvince = ((Spinner) findViewById(R.id.sp_province));
        mSpProvince.setAdapter(new ArrayAdapter<>(this, R.layout.item_spinner_layout, mResources.getStringArray(R.array.provinces)));
        mSpCity = ((Spinner) findViewById(R.id.sp_city));
        mSpCity.setAdapter(new ArrayAdapter<>(this, R.layout.item_spinner_layout, mResources.getStringArray(R.array.cities)));
        mSpCounty = ((Spinner) findViewById(R.id.sp_county));
        mSpCounty.setAdapter(new ArrayAdapter<>(this, R.layout.item_spinner_layout, mResources.getStringArray(R.array.counties)));
        mNameEt = (EditText) findViewById(R.id.et_name);
        mAddressEt = (EditText) findViewById(R.id.et_address);
        mTelephoneEt = (EditText) findViewById(R.id.et_telephone);
        mPwdEt = (EditText) findViewById(R.id.et_pwd);
        mIdCardEt = (EditText) findViewById(R.id.et_id_card);
        findViewById(R.id.tv_next).setOnClickListener(this);
        findViewById(R.id.iv_back).setOnClickListener(this);
        sexSpinner = (Spinner) findViewById(R.id.sp_sex);
        sexSpinner.setAdapter(new ArrayAdapter<String>(mContext, R.layout.item_spinner_layout, mResources.getStringArray(R.array.sex_array)));

        yearSpinner = (Spinner) findViewById(R.id.sp_year);
        ArrayList<String> yearList = new ArrayList<>();
        for (int i = 1900; i < 2018; i++) {
            yearList.add(String.valueOf(i));
        }
        yearSpinner.setAdapter(new ArrayAdapter<String>(mContext, R.layout.item_spinner_layout, yearList));
        yearSpinner.setSelection(90);
        monthSpinner = (Spinner) findViewById(R.id.sp_month);
        ArrayList<String> monthList = new ArrayList<>();
        for (int i = 1; i < 13; i++) {
            monthList.add(String.valueOf(i));
        }
        monthSpinner.setAdapter(new ArrayAdapter<String>(mContext, R.layout.item_spinner_layout, monthList));
    }

    @Override
    public void onClick(View v) {
        phoneNums = mTelephoneEt.getText().toString().replaceAll(" ", "");
        switch (v.getId()) {
            case R.id.yanzheng:

                // 1. 通过规则判断手机号
                if (!judgePhoneNums(phoneNums)) {
                    speak("主人，手机号码输入有误，请重新输入");
                    return;
                } // 2. 通过sdk发送短信验证
                SMSSDK.getVerificationCode("86", phoneNums);

                // 3. 把按钮变成不可点击，并且显示倒计时（正在获取）
                mTextView.setClickable(false);
                mTextView.setText("重新发送(" + i + ")");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        for (; i > 0; i--) {
                            handler.sendEmptyMessage(-9);
                            if (i <= 0) {
                                break;
                            }
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        handler.sendEmptyMessage(-8);
                    }
                }).start();


                break;
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_next:
                //将收到的验证码和手机号提交再次核对
                SMSSDK.submitVerificationCode("86", phoneNums, mEditText.getText().toString().replaceAll(" ", ""));


                break;
        }
    }


    /**
     * 判断手机号码是否合理
     *
     * @param phoneNums
     */
    private boolean judgePhoneNums(String phoneNums) {
        if (isMatchLength(phoneNums, 11) && isMobileNO(phoneNums)) {
            return true;
        }
        Toast.makeText(this, "手机号码输入有误！", Toast.LENGTH_SHORT).show();
        return false;
    }

    /**
     * 判断一个字符串的位数
     *
     * @param str
     * @param length
     * @return
     */
    public static boolean isMatchLength(String str, int length) {
        if (str.isEmpty()) {
            return false;
        } else {
            return str.length() == length ? true : false;
        }
    }

    /**
     * 验证手机格式
     */
    public static boolean isMobileNO(String mobileNums) {
        /*
         * 移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
         * 联通：130、131、132、152、155、156、185、186 电信：133、153、180、189、（1349卫通）
         * 总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
         */
        String telRegex = "[1][3578]\\d{9}";// "[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(mobileNums))
            return false;
        else
            return mobileNums.matches(telRegex);
    }


    private boolean checkInputInfo() {
        if (TextUtils.isEmpty(mNameEt.getText())) {
            Toast.makeText(mContext, R.string.empty_name, Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(mAddressEt.getText())) {
            Toast.makeText(mContext, R.string.empty_name, Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(mTelephoneEt.getText())) {
            Toast.makeText(mContext, R.string.empty_name, Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(mPwdEt.getText())) {
            Toast.makeText(mContext, R.string.empty_name, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterAllEventHandler();
    }
}
