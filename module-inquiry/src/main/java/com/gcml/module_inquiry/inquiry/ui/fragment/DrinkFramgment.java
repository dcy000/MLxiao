package com.gcml.module_inquiry.inquiry.ui.fragment;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gcml.common.utils.display.ToastUtils;
import com.gcml.module_inquiry.R;
import com.gcml.module_inquiry.inquiry.ui.fragment.base.InquiryBaseFrament;

/**
 * Created by lenovo on 2019/3/21.
 */

public class DrinkFramgment extends InquiryBaseFrament implements View.OnClickListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private TextView tvDrinkYes;
    private TextView tvDrinkNot;
    private LinearLayout llDrinkYesNot;
    private TextView tvBaijiu;
    private TextView tvLiaojiu;
    private TextView tvPijiu;
    private TextView tvMijiu;
    private LinearLayout llDrinkWhat;
    private TextView tvSignUpGoBack;
    private TextView tvSignUpGoForward;

    @Override
    protected int layoutId() {
        return R.layout.fragment_drink;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        tvDrinkYes = view.findViewById(R.id.tv_drink_yes);
        tvDrinkNot = view.findViewById(R.id.tv_drink_not);
        llDrinkYesNot = view.findViewById(R.id.ll_drink_yes_not);
        tvBaijiu = view.findViewById(R.id.tv_baijiu);
        tvLiaojiu = view.findViewById(R.id.tv_liaojiu);
        tvPijiu = view.findViewById(R.id.tv_pijiu);
        tvMijiu = view.findViewById(R.id.tv_mijiu);
        llDrinkWhat = view.findViewById(R.id.ll_drink_what);

        tvSignUpGoBack = view.findViewById(R.id.tv_sign_up_go_back);
        tvSignUpGoForward = view.findViewById(R.id.tv_sign_up_go_forward);

        tvDrinkYes.setOnClickListener(this);
        tvDrinkNot.setOnClickListener(this);
        tvBaijiu.setOnClickListener(this);
        tvLiaojiu.setOnClickListener(this);
        tvPijiu.setOnClickListener(this);
        tvMijiu.setOnClickListener(this);

        tvSignUpGoBack.setOnClickListener(this);
        tvSignUpGoForward.setOnClickListener(this);


    }


    private void reverse(TextView view) {
        view.setSelected(!view.isSelected());

        tvDrinkYes.setSelected(true);
        tvDrinkNot.setSelected(false);

        setSelect(tvDrinkYes, true);
        setSelect(tvDrinkNot, false);
        setSelect(view, view.isSelected());

    }

    private void getResult() {
        String drink = tvDrinkYes.isSelected() ? "1" : "0";
        String drinkWhat = "";

        if (tvBaijiu.isSelected()) {
            drinkWhat += "0" + ",";
        }

        if (tvLiaojiu.isSelected()) {
            drinkWhat += "1" + ",";
        }

        if (tvPijiu.isSelected()) {
            drinkWhat += "2" + ",";
        }

        if (tvMijiu.isSelected()) {
            drinkWhat += "3" + ",";
        }

        if ((!tvDrinkNot.isSelected()) && (!tvDrinkYes.isSelected())) {
            ToastUtils.showShort("主人,您本周内是否有饮酒");
            return;
        }

        if (listenerAdapter != null) {
            listenerAdapter.onNext("4", drink, drinkWhat);
        }

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.tv_drink_yes) {
            tvDrinkYes.setSelected(!tvDrinkYes.isSelected());
            tvDrinkNot.setSelected(false);

            setSelect(tvDrinkYes, tvDrinkYes.isSelected());
            setSelect(tvDrinkNot, false);
        } else if (id == R.id.tv_drink_not) {
            tvDrinkNot.setSelected(!tvDrinkNot.isSelected());
            tvDrinkYes.setSelected(false);

            setSelect(tvDrinkNot, tvDrinkNot.isSelected());
            setSelect(tvDrinkYes, false);

            for (int i = 0; i < llDrinkWhat.getChildCount(); i++) {
                llDrinkWhat.getChildAt(i).setSelected(false);
                setSelect((TextView) llDrinkWhat.getChildAt(i), false);
            }
        } else if (id == R.id.tv_baijiu) {
            reverse(tvBaijiu);
        } else if (id == R.id.tv_liaojiu) {
            reverse(tvLiaojiu);
        } else if (id == R.id.tv_pijiu) {
            reverse(tvPijiu);
        } else if (id == R.id.tv_mijiu) {
            reverse(tvMijiu);
        } else if (id == R.id.tv_sign_up_go_back) {
            if (listenerAdapter != null) {
                listenerAdapter.onBack("4", null, null);
            }
        } else if (id == R.id.tv_sign_up_go_forward) {
            getResult();
        }

    }

    private void setSelect(TextView view, boolean select) {
        Drawable drawable = getResources().getDrawable(R.drawable.icon_huaiyun);
        if (select) {
        } else {
            drawable = getResources().getDrawable(R.drawable.icon_huaiyun_not);
        }
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        view.setCompoundDrawables(drawable, null, null, null);
    }

    public static DrinkFramgment newInstance(String param1, String param2) {
        DrinkFramgment fragment = new DrinkFramgment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

}
