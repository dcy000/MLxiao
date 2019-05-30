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

import com.gcml.common.router.AppRouter;
import com.gcml.common.utils.DefaultObserver;
import com.gcml.common.utils.RxUtils;
import com.gcml.common.utils.Utils;
import com.gcml.common.utils.display.ToastUtils;
import com.gcml.module_auth_hospital.R;
import com.gcml.module_auth_hospital.ui.findPassWord.CodeRepository;
import com.iflytek.synthetize.MLVoiceSynthetize;
import com.sjtu.yifei.route.Routerfit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class SetPassWord2Fragment extends Fragment implements View.OnClickListener {
    private static final String ARG_PARAM1 = "phoneNumber";
    private static final String ARG_PARAM2 = "param2";
    private String phoneNumber;
    private String mParam2;


    public SetPassWord2Fragment() {
    }

    public static SetPassWord2Fragment newInstance(String param1, String param2) {
        SetPassWord2Fragment fragment = new SetPassWord2Fragment();
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
            phoneNumber = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private TextView tvNext;
    private EditText etPsw;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_set_pass_word2, container, false);
        tvNext = view.findViewById(R.id.tv_next);
        etPsw = view.findViewById(R.id.et_psw);
        etPsw.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                onTextChange(s);
            }
        });
        tvNext.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.tv_next) {
            setPassWord();
        }
    }

    private CodeRepository codeRepository = new CodeRepository();

    private void setPassWord() {
        String passWord = etPsw.getText().toString().trim();
        if (TextUtils.isEmpty(passWord)) {
            speak("请输入6位数字密码");
            return;
        }
        codeRepository.updatePassword(phoneNumber, passWord)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new DefaultObserver<Object>() {
                    @Override
                    public void onNext(Object o) {
                        Routerfit.register(AppRouter.class).skipUserLogins2Activity();
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        ToastUtils.showShort(throwable.getMessage());
                    }
                });

    }

    private void speak(String content) {
        MLVoiceSynthetize.startSynthesize(getContext(), content);
    }

    public void onTextChange(Editable phone) {
        if (TextUtils.isEmpty(phone.toString()) && Utils.checkIdCard1(phone.toString())) {
            tvNext.setEnabled(false);
        } else {
            tvNext.setEnabled(true);
        }
    }
}
