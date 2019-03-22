package com.gcml.module_inquiry.inquiry.ui.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gcml.common.utils.display.ToastUtils;
import com.gcml.module_inquiry.R;
import com.gcml.module_inquiry.inquiry.ui.fragment.base.InquiryBaseFrament;

/**
 * Created by lenovo on 2019/3/21.
 */

public class GuoMinFragment extends InquiryBaseFrament implements View.OnClickListener {
    private TextView title1;
    private TextView qingmeisu;
    private TextView toubao;
    private TextView huanganlei;
    private TextView qita;
    private LinearLayout ll1;
    private TextView wu1;
    private TextView title2;
    private TextView gaouxeya;
    private TextView shexian;
    private TextView tangniaobing;
    private TextView xiaochuan;
    private LinearLayout ll2;
    private TextView tvSignUpGoBack;
    private TextView tvSignUpGoForward;
    private TextView qita2;
    private TextView wu2;
    private String result;

    @Override
    protected int layoutId() {
        return R.layout.fragment_guomin;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        title1 = view.findViewById(R.id.title1);
        qingmeisu = view.findViewById(R.id.qingmeisu);
        toubao = view.findViewById(R.id.toubao);
        huanganlei = view.findViewById(R.id.huanganlei);
        qita = view.findViewById(R.id.qita);
        ll1 = view.findViewById(R.id.ll_1);
        wu1 = view.findViewById(R.id.wu11);
        title2 = view.findViewById(R.id.title2);
        gaouxeya = view.findViewById(R.id.gaouxeya);
        tangniaobing = view.findViewById(R.id.tangniaobing);
        shexian = view.findViewById(R.id.shexian);
        xiaochuan = view.findViewById(R.id.xiaochuan);
        ll2 = view.findViewById(R.id.ll_2);
        tvSignUpGoBack = view.findViewById(R.id.tv_sign_up_go_back);
        tvSignUpGoForward = view.findViewById(R.id.tv_sign_up_go_forward);
        qita2 = view.findViewById(R.id.qita2);
        wu2 = view.findViewById(R.id.wu2);

        qingmeisu.setOnClickListener(this);
        toubao.setOnClickListener(this);
        huanganlei.setOnClickListener(this);
        qita.setOnClickListener(this);
        ll1.setOnClickListener(this);
        wu1.setOnClickListener(this);
        gaouxeya.setOnClickListener(this);
        tangniaobing.setOnClickListener(this);
        shexian.setOnClickListener(this);
        xiaochuan.setOnClickListener(this);
        tvSignUpGoBack.setOnClickListener(this);
        tvSignUpGoForward.setOnClickListener(this);
        qita2.setOnClickListener(this);
        wu2.setOnClickListener(this);

    }

    private void nextStep() {
        if (TextUtils.isEmpty(getGuoMin())) {
            ToastUtils.showShort("主人,您是否有过敏史");
            return;
        }

        if (TextUtils.isEmpty(getJiBing())) {
            ToastUtils.showShort("主人,您是否有疾病史");
            return;
        }

        listenerAdapter.onNext("7", getGuoMin(), getJiBing());
    }

    public String getGuoMin() {
        String guomin = "";
        if (wu1.isSelected()) {
            return "0";
        }

        if (qingmeisu.isSelected()) {
            guomin += "1,";
        }

        if (toubao.isSelected()) {
            guomin += "2,";
        }

        if (huanganlei.isSelected()) {
            guomin += "3,";
        }
        if (qita.isSelected()) {
            guomin += "4";
        }


        return guomin;
    }

    private String getJiBing() {
        String jibing = "";
        if (wu2.isSelected()) {
            return "0";
        }

        if (gaouxeya.isSelected()) {
            jibing += "1,";
        }

        if (tangniaobing.isSelected()) {
            jibing += "2,";
        }

        if (shexian.isSelected()) {
            jibing += "3,";
        }

        if (xiaochuan.isSelected()) {
            jibing += "4,";
        }

        if (qita2.isSelected()) {
            jibing += "5,";
        }
        return jibing;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.qingmeisu) {
            qingmeisu.setSelected(!qingmeisu.isSelected());
            wu1.setSelected(false);
        } else if (id == R.id.toubao) {
            toubao.setSelected(!toubao.isSelected());
            wu1.setSelected(false);
        } else if (id == R.id.huanganlei) {
            huanganlei.setSelected(!huanganlei.isSelected());
            wu1.setSelected(false);
        } else if (id == R.id.qita) {
            qita.setSelected(!qita.isSelected());
            wu1.setSelected(false);
        } else if (id == R.id.wu11) {
            wu1.setSelected(!wu1.isSelected());
            qingmeisu.setSelected(false);
            toubao.setSelected(false);
            huanganlei.setSelected(false);
            qita.setSelected(false);
        } else if (id == R.id.gaouxeya) {
            gaouxeya.setSelected(!gaouxeya.isSelected());
            wu2.setSelected(false);
        } else if (id == R.id.tangniaobing) {
            tangniaobing.setSelected(!tangniaobing.isSelected());
            wu2.setSelected(false);
        } else if (id == R.id.shexian) {
            shexian.setSelected(!shexian.isSelected());
            wu2.setSelected(false);
        } else if (id == R.id.xiaochuan) {
            xiaochuan.setSelected(!xiaochuan.isSelected());
            wu2.setSelected(false);
        } else if (id == R.id.tv_sign_up_go_back) {
            if (listenerAdapter != null) {
                listenerAdapter.onBack("7", null, null);
            }
        } else if (id == R.id.tv_sign_up_go_forward) {
            if (listenerAdapter != null) {
                nextStep();
            }
        } else if (id == R.id.qita2) {
            qita2.setSelected(!qita2.isSelected());
            wu2.setSelected(false);
        } else if (id == R.id.wu2) {
            wu2.setSelected(!wu2.isSelected());

            gaouxeya.setSelected(false);
            tangniaobing.setSelected(false);
            shexian.setSelected(false);
            xiaochuan.setSelected(false);
            qita2.setSelected(false);
        }
    }

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public static GuoMinFragment newInstance(String param1, String param2) {
        GuoMinFragment fragment = new GuoMinFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

}
