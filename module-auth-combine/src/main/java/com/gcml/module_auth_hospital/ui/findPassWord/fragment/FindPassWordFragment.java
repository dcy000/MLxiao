package com.gcml.module_auth_hospital.ui.findPassWord.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.gcml.common.http.ApiException;
import com.gcml.common.utils.DefaultObserver;
import com.gcml.common.utils.RxUtils;
import com.gcml.common.utils.Utils;
import com.gcml.common.utils.display.ToastUtils;
import com.gcml.common.widget.toolbar.FilterClickListener;
import com.gcml.common.widget.toolbar.TranslucentToolBar;
import com.gcml.module_auth_hospital.R;
import com.gcml.module_auth_hospital.model.UserRepository;
import com.gcml.module_auth_hospital.ui.findPassWord.CodeRepository;
import com.gcml.module_auth_hospital.ui.findPassWord.ForgetPassWordActivity;
import com.iflytek.synthetize.MLVoiceSynthetize;

import java.util.Locale;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.disposables.Disposables;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class FindPassWordFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;


    public FindPassWordFragment() {
    }

    public static FindPassWordFragment newInstance(String param1, String param2) {
        FindPassWordFragment fragment = new FindPassWordFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private TextView notice;
    private TextView next;
    private TextView sendCode;
    private EditText phone;
    private EditText code;
    private TranslucentToolBar tb;
    private String codeNumer = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_find_pass_word, container, false);
        notice = view.findViewById(R.id.tv_notice);
        tb = view.findViewById(R.id.tb_find_password);
        phone = view.findViewById(R.id.et_phone);
        code = view.findViewById(R.id.et_code);
        next = view.findViewById(R.id.tv_next);
        next.setOnClickListener(new FilterClickListener(v -> {
            toSetPassWord();
        }));
        sendCode = view.findViewById(R.id.tv_send_code);


        phone.addTextChangedListener(watcher);
        sendCode.setOnClickListener(new FilterClickListener(v -> sendCode()));
        return view;
    }


    private void toSetPassWord() {
        if (TextUtils.isEmpty(code.getText().toString())) {
            ToastUtils.showShort("请输入验证码");
            return;
        }
        if (!this.codeNumer.equals(code.getText().toString())) {
            ToastUtils.showShort("验证码错误");
            return;
        }
        /*startActivity(new Intent(getActivity(), SetPassWord2Activity.class)
                .putExtra("phoneNumber", phone.getText().toString()));*/
        ForgetPassWordActivity activity = (ForgetPassWordActivity) getActivity();
        activity.liveData.setValue("findPswNext");

    }

    private CodeRepository codeRepository = new CodeRepository();
    private UserRepository userRepository = new UserRepository();
    Disposable countDownDisposable = Disposables.empty();

    String thePhone = "";

    private void sendCode() {
        thePhone = phone.getText().toString().trim();
        if (!Utils.isValidPhone(thePhone)) {
            MLVoiceSynthetize.startSynthesize(getContext().getApplicationContext(), "请输入正确的手机号码", false);
            ToastUtils.showShort("请输入正确的手机号码");
            return;
        }

        userRepository.isPhoneNotRegistered(thePhone)
                .onErrorResumeNext(new Function<Throwable, ObservableSource<Object>>() {
                    @Override
                    public ObservableSource<Object> apply(Throwable throwable) throws Exception {
                        if (throwable instanceof ApiException) {
                            if (((ApiException) throwable).code() == 600) {
                                return Observable.just("已注册的手机号");
                            } else if (((ApiException) throwable).code() == 200) {
                                return Observable.error(new ApiException("手机号未注册", 200));
                            }
                        }
                        return Observable.error(throwable);
                    }
                })
                .flatMap(new Function<Object, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(Object o) throws Exception {
                        return codeRepository
                                .fetchCode(thePhone);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        startTimer();
                    }
                })
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new DefaultObserver<String>() {
                    @Override
                    public void onNext(String code) {
                        FindPassWordFragment.this.codeNumer = code;
                        ToastUtils.showShort("获取验证码成功");
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        thePhone = "";
                        countDownDisposable.dispose();
                        ToastUtils.showShort(throwable.getMessage());
                    }
                });

    }

    private void startTimer() {
        countDownDisposable = RxUtils.rxCountDown(1, 60)
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        sendCode.setEnabled(false);
                    }
                })
                .doOnTerminate(new Action() {
                    @Override
                    public void run() throws Exception {
                        sendCode.setText("获取验证码");
                        sendCode.setEnabled(true);
                    }
                })
                .doOnDispose(new Action() {
                    @Override
                    public void run() throws Exception {
                        sendCode.setText("获取验证码");
                        sendCode.setEnabled(true);
                    }
                })
                .as(RxUtils.autoDisposeConverter(this))
                .subscribeWith(new DefaultObserver<Integer>() {
                    @Override
                    public void onNext(Integer integer) {
                        sendCode.setText(
                                String.format(Locale.getDefault(), "已发送（%d）", integer));
                    }
                });
    }


    private TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (TextUtils.isEmpty(phone.getText().toString()) && TextUtils.isEmpty(code.getText().toString())) {
                next.setEnabled(false);
            } else {
                next.setEnabled(true);
            }

        }
    };
}
