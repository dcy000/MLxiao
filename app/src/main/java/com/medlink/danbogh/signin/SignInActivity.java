package com.medlink.danbogh.signin;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.han.referralproject.MainActivity;
import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.AgreementActivity;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.activity.WifiConnectActivity;
import com.gzq.lib_core.bean.SessionBean;
import com.example.han.referralproject.service.API;
import com.gzq.lib_core.utils.PinYinUtils;
import com.gcml.auth.face.FaceConstants;
import com.gcml.auth.face.model.FaceRepository;
import com.gcml.auth.face.ui.FaceSignInActivity;
import com.gzq.lib_core.base.Box;
import com.gzq.lib_core.bean.UserInfoBean;
import com.gzq.lib_core.http.exception.ApiException;
import com.gzq.lib_core.http.model.HttpResult;
import com.gzq.lib_core.http.observer.CommonObserver;
import com.gzq.lib_core.room.UserDatabase;
import com.gzq.lib_core.utils.ActivityUtils;
import com.gzq.lib_core.utils.RxUtils;
import com.gzq.lib_core.utils.ToastUtils;
import com.iflytek.synthetize.MLVoiceSynthetize;
import com.medlink.danbogh.utils.Utils;
import com.ml.edu.common.base.DefaultObserver;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class SignInActivity extends BaseActivity {

    @BindView(R.id.et_sign_in_phone)
    EditText etPhone;
    @BindView(R.id.et_sign_in_password)
    EditText etPassword;
    @BindView(R.id.tv_sign_in_sign_in)
    TextView tvSignIn;
    @BindView(R.id.cb_sign_in_agree)
    CheckBox cbAgree;
    @BindView(R.id.tv_sign_in_agree)
    TextView tvAgree;
    @BindView(R.id.rl_back)
    RelativeLayout rlBack;
    private Unbinder mUnbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        mToolbar.setVisibility(View.GONE);
        mUnbinder = ButterKnife.bind(this);
        etPhone.addTextChangedListener(inputWatcher);
        etPassword.addTextChangedListener(inputWatcher);
        cbAgree.setOnCheckedChangeListener(onCheckedChangeListener);
        SpannableStringBuilder agreeBuilder = new SpannableStringBuilder("我同意用户协议");
        agreeBuilder.setSpan(new ForegroundColorSpan(Color.parseColor("#FF380000")),
                3, 7, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        agreeBuilder.setSpan(agreeClickableSpan, 3, 7, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        tvAgree.setMovementMethod(LinkMovementMethod.getInstance());
        tvAgree.setText(agreeBuilder);
        checkInput();
        ((TextView) findViewById(R.id.tv_version)).setText(getLocalVersionName());
        registerReceiver(wifiChangedReceiver, new IntentFilter(WifiManager.RSSI_CHANGED_ACTION));
    }

    private BroadcastReceiver wifiChangedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e("网络强度发生变化", "onReceive: ");
            int level = obtainWifiInfo();
            if (level <= 0 && level >= -50) {
                mRightView.setImageResource(R.drawable.dark_wifi_3);
            } else if (level < -50 && level >= -70) {
                mRightView.setImageResource(R.drawable.dark_wifi_2);
            } else if (level < -70) {
                mRightView.setImageResource(R.drawable.dark_wifi_1);
            }
        }
    };

    private int obtainWifiInfo() {
        // Wifi的连接速度及信号强度：
        int strength = 0;
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        WifiInfo info = wifiManager.getConnectionInfo();
        if (info.getBSSID() != null) {
            // 链接信号强度，5为获取的信号强度值在5以内
            strength = WifiManager.calculateSignalLevel(info.getRssi(), 5);
            // 链接速度
            int speed = info.getLinkSpeed();
            // 链接速度单位
            String units = WifiInfo.LINK_SPEED_UNITS;
            // Wifi源名称
            String ssid = info.getSSID();
        }
        return strength;
    }

    private CompoundButton.OnCheckedChangeListener onCheckedChangeListener =
            new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    checkInput();
                }
            };

    private TextWatcher inputWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            checkInput();
        }
    };

    private boolean checkInput() {
        boolean validPassword = false;
        boolean validPhone = false;
        boolean checked = cbAgree.isChecked();
        String phone = etPhone.toString().trim();
        if (!TextUtils.isEmpty(phone)) {
            validPassword = true;
        }
        String password = etPassword.toString().trim();
        if (!TextUtils.isEmpty(password)) {
            validPhone = true;
        }
        boolean enabled = validPassword && validPhone && checked;
        tvSignIn.setEnabled(enabled);
        return enabled;
    }

    private ClickableSpan agreeClickableSpan = new ClickableSpan() {
        @Override
        public void onClick(View widget) {
            startActivity(new Intent(SignInActivity.this, AgreementActivity.class));
        }
    };

    public String getLocalVersionName() {
        String localVersion = "";
        try {
            PackageInfo packageInfo = getApplicationContext()
                    .getPackageManager()
                    .getPackageInfo(getPackageName(), 0);
            localVersion = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return localVersion;
    }

    @OnClick(R.id.cl_root)
    public void onClRootClicked() {
        View view = getCurrentFocus();
        if (view != null) {
            Utils.hideKeyBroad(view);
        }
    }

    @OnClick(R.id.tv_sign_in_sign_in)
    public void onTvSignInClicked() {
        showLoadingDialog(getString(R.string.do_login));
        Box.getRetrofit(API.class)
                .login(etPhone.getText().toString(), etPassword.getText().toString())
                .compose(RxUtils.httpResponseTransformer(false))
                .compose(userTokenTransformer())
                .doOnNext(new Consumer<UserInfoBean>() {
                    @Override
                    public void accept(UserInfoBean userInfoBean) throws Exception {
                        checkGroup(userInfoBean.xfid);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .doOnTerminate(new Action() {
                    @Override
                    public void run() throws Exception {
                        hideLoadingDialog();
                    }
                })
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new CommonObserver<UserInfoBean>() {
                    @Override
                    public void onNext(UserInfoBean response) {
                        startActivity(new Intent(mContext, MainActivity.class));
                        finish();
                    }

                    @Override
                    protected void onError(ApiException ex) {
                        super.onError(ex);
                        ToastUtils.showShort(ex.getMessage());
                    }
                });
    }

    private void checkGroup(String xfid) {
        FaceRepository faceRepository = new FaceRepository();
        faceRepository.tryJoinGroup(xfid)
                .subscribeOn(Schedulers.io())
                .subscribe(new DefaultObserver<String>() {
                    @Override
                    public void onNext(String groupId) {

                    }

                    @Override
                    public void onError(Throwable throwable) {
                        Timber.e("人脸识别组异常：" + throwable.getMessage());
                    }
                });
    }

    private ObservableTransformer<SessionBean, UserInfoBean> userTokenTransformer() {
        return new ObservableTransformer<SessionBean, UserInfoBean>() {
            @Override
            public ObservableSource<UserInfoBean> apply(Observable<SessionBean> upstream) {
                return upstream
                        .flatMap(new Function<SessionBean, ObservableSource<UserInfoBean>>() {
                            @Override
                            public ObservableSource<UserInfoBean> apply(SessionBean userToken) throws Exception {
                                //保存Token
                                Box.getSessionManager().setUserToken(userToken);
                                return Box.getRetrofit(API.class)
                                        .queryUserInfo(userToken.getUserId() + "")
                                        .compose(RxUtils.httpResponseTransformer(false));
                            }
                        })
                        .doOnNext(new Consumer<UserInfoBean>() {
                            @Override
                            public void accept(UserInfoBean user) throws Exception {
                                //session管理、存入数据库
                                Box.getSessionManager().setUser(user);
                                UserDatabase userDatabase = Box.getRoomDataBase(UserDatabase.class);
                                userDatabase.userDao().insertUser(user);
                            }
                        });
            }
        };
    }


    @OnClick(R.id.tv_sign_in_sign_up)
    public void onTvSignUpClicked() {
        signInWithFace();
    }

    private void signInWithFace() {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                List<UserInfoBean> usersFromRoom = Box.getUsersFromRoom();

                if (usersFromRoom == null) {
                    emitter.onError(new Throwable("未检测到您的登录历史，请输入账号和密码登录"));
                    return;
                }
                StringBuilder userIds = new StringBuilder();
                for (UserInfoBean user : usersFromRoom) {
                    userIds.append(user.bid);
                }
                String substring = userIds.substring(0, userIds.length() - 1);
                emitter.onNext(substring);
            }
        }).flatMap(new Function<String, ObservableSource<HttpResult<ArrayList<UserInfoBean>>>>() {
            @Override
            public ObservableSource<HttpResult<ArrayList<UserInfoBean>>> apply(String s) throws Exception {
                return Box.getRetrofit(API.class)
                        .queryAllLocalUsers(s);
            }
        }).compose(RxUtils.httpResponseTransformer())
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new CommonObserver<ArrayList<UserInfoBean>>() {
                    @Override
                    public void onNext(ArrayList<UserInfoBean> users) {
                        Intent intent = new Intent();
                        intent.setClass(SignInActivity.this, FaceSignInActivity.class);
                        intent.putExtra("skip", false);
                        intent.putExtra("currentUser", false);
                        intent.putParcelableArrayListExtra("users", users);
                        startActivityForResult(intent, 1001);
                    }

                    @Override
                    protected void onError(ApiException ex) {
                        ToastUtils.showLong(ex.getMessage());
                        startActivity(new Intent(SignInActivity.this, SignInActivity.class));
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1001 && data != null) {
            int faceResult = data.getIntExtra(FaceConstants.KEY_AUTH_FACE_RESULT, 0);
            switch (faceResult) {
                case FaceConstants.AUTH_FACE_SUCCESS:
                    ActivityUtils.skipActivity(MainActivity.class);
                    break;
                case FaceConstants.AUTH_FACE_FAIL:

                    break;
            }
        }
    }

    @OnClick(R.id.tv_sign_in_forget_password)
    public void onTvForgetPasswordClicked() {
        String phone = etPhone.getText().toString().trim();
        Intent intent = new Intent(SignInActivity.this, FindPasswordActivity.class);
        if (Utils.isValidPhone(phone)) {
            intent.putExtra("phone", phone);
        }
        startActivity(intent);
        finish();
    }

    @Override
    protected void onResume() {
        setDisableWakeup(true);
        super.onResume();
        MLVoiceSynthetize.startSynthesize(R.string.tips_login);
    }

    @Override
    protected void onDestroy() {
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
        unregisterReceiver(wifiChangedReceiver);
        super.onDestroy();
    }

    public void onWifiClick(View view) {
        Intent intent = new Intent(this, WifiConnectActivity.class);
        startActivity(intent);
    }

    boolean inPhone = true;

    @Override
    protected void onSpeakListenerResult(String result) {
        ToastUtils.showShort(result);

        String inSpell = PinYinUtils.converterToSpell(result);

        if (inSpell.matches("zhuche|zhuce|zuce|zuche")) {
            onTvSignUpClicked();
            return;
        }

        if (inSpell.matches("denglu")) {
            onTvSignInClicked();
            return;
        }

        Pattern patternInNumber = Pattern.compile("(\\d+)");
        String in = Utils.isNumeric(result) ? result : Utils.removeNonnumeric(Utils.chineseMapToNumber(result));
        Matcher matcherInNumber = patternInNumber.matcher(in);
        if (matcherInNumber.find()) {
            EditText et = inPhone ? this.etPhone : this.etPassword;
            int length = inPhone ? 11 : 6;
            String s = et.getText().toString() + matcherInNumber.group(matcherInNumber.groupCount());
            if (s.length() > length) {
                s = s.substring(0, length);
            }
            et.setText(s);
            et.setSelection(s.length());
            return;
        }

        if (inSpell.matches(REGEX_IN_PHONE) && !inPhone) {
            inPhone = true;
            etPhone.requestFocus();
            MLVoiceSynthetize.startSynthesize(R.string.sign_up_phone_tip);
            return;
        }

        if (inSpell.matches(REGEX_IN_PASSWORD) && inPhone) {
            inPhone = false;
            MLVoiceSynthetize.startSynthesize("请输入密码");
            etPassword.requestFocus();
            return;
        }

        if (inSpell.matches(REGEX_DEL_ALL)) {
            EditText editText = inPhone ? etPhone : etPassword;
            editText.setText("");
            editText.setSelection(0);
            return;
        }

        if (inSpell.matches(REGEX_DEL)) {
            EditText editText = inPhone ? etPhone : etPassword;
            String target = editText.getText().toString().trim();
            if (!TextUtils.isEmpty(target)) {
                editText.setText(target.substring(0, target.length() - 1));
                editText.setSelection(target.length() - 1);
            }
        }
    }

    @OnClick(R.id.rl_back)
    public void backClick() {
        finish();
    }

    public static final String REGEX_DEL = "(quxiao|qingchu|sandiao|shandiao|sancu|shancu|sanchu|shanchu|budui|cuole)";
    public static final String REGEX_DEL_ALL = ".*(chongxin|quanbu|suoyou|shuoyou).*";
    public static final String REGEX_IN_PHONE = ".*(shu|su)(ru|lu|lv)(shou|sou)ji.*";
    public static final String REGEX_IN_PASSWORD = ".*(shu|su)(ru|lu|lv)(mima)";
}
