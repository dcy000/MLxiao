package com.example.han.referralproject.building_record;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.han.referralproject.R;

import java.util.ArrayList;
import java.util.List;

public class PaymentmethodFragment extends Fragment implements View.OnClickListener {
    private View view;
    private TextView title;
    private TextView chengzhenzhigong;
    private TextView chengzhenjuming;
    private TextView xinxingnongcun;
    private TextView pinkujiuzhu;
    private TextView shangyeyiliao;
    private TextView chuzhong;
    private TextView quanzifei;
    private TextView qita;
    private TextView tvSignUpGoBack;
    private TextView tvSignUpGoForward;
    private IFragmentChange iFragmentChange;
    private String result;

    public void setOnFragmentChange(IFragmentChange iFragmentChange) {
        this.iFragmentChange = iFragmentChange;
    }

    private boolean[] switch_pay = new boolean[8];
    private String[] pay = new String[]{"城镇职工基本医疗保险", "城镇居民基本医疗保险", "新型农村合作医疗 ", "贫困救助", "商业医疗保险", "全公费", "全自费", "其他"};
    private List<TextView> textViews;
    private String[] index=new String[]{"1","2","3","4","5","6","7","8"};
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_payment_method, container, false);
            initView(view);
        }
        return view;
    }

    private void initView(View view) {
        title = (TextView) view.findViewById(R.id.title);
        chengzhenzhigong = (TextView) view.findViewById(R.id.chengzhenzhigong);
        chengzhenzhigong.setOnClickListener(this);
        chengzhenjuming = (TextView) view.findViewById(R.id.chengzhenjuming);
        chengzhenjuming.setOnClickListener(this);
        xinxingnongcun = (TextView) view.findViewById(R.id.xinxingnongcun);
        xinxingnongcun.setOnClickListener(this);
        pinkujiuzhu = (TextView) view.findViewById(R.id.pinkujiuzhu);
        pinkujiuzhu.setOnClickListener(this);
        shangyeyiliao = (TextView) view.findViewById(R.id.shangyeyiliao);
        shangyeyiliao.setOnClickListener(this);
        chuzhong = (TextView) view.findViewById(R.id.chuzhong);
        chuzhong.setOnClickListener(this);
        quanzifei = (TextView) view.findViewById(R.id.quanzifei);
        quanzifei.setOnClickListener(this);
        qita = (TextView) view.findViewById(R.id.qita);
        qita.setOnClickListener(this);
        tvSignUpGoBack = (TextView) view.findViewById(R.id.tv_sign_up_go_back);
        tvSignUpGoBack.setOnClickListener(this);
        tvSignUpGoForward = (TextView) view.findViewById(R.id.tv_sign_up_go_forward);
        tvSignUpGoForward.setOnClickListener(this);
        textViews = new ArrayList<>();
        textViews.add(chengzhenzhigong);
        textViews.add(chengzhenjuming);
        textViews.add(xinxingnongcun);
        textViews.add(pinkujiuzhu);
        textViews.add(shangyeyiliao);
        textViews.add(chuzhong);
        textViews.add(quanzifei);
        textViews.add(qita);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((BuildingRecordActivity) getActivity()). setDisableGlobalListen(true);
        ((BuildingRecordActivity) getActivity()).speak("主人,请选择您的医疗支付方式");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.chengzhenzhigong:
                result = MyArraysUtils.resetSwitch(switch_pay, pay, textViews, 0);
                break;
            case R.id.chengzhenjuming:
                result = MyArraysUtils.resetSwitch(switch_pay, pay, textViews, 1);
                break;
            case R.id.xinxingnongcun:
                result = MyArraysUtils.resetSwitch(switch_pay, pay, textViews, 2);
                break;
            case R.id.pinkujiuzhu:
                result = MyArraysUtils.resetSwitch(switch_pay, pay, textViews, 3);
                break;
            case R.id.shangyeyiliao:
                result = MyArraysUtils.resetSwitch(switch_pay, pay, textViews, 4);
                break;
            case R.id.chuzhong:
                result = MyArraysUtils.resetSwitch(switch_pay, pay, textViews, 5);
                break;
            case R.id.quanzifei:
                result = MyArraysUtils.resetSwitch(switch_pay, pay, textViews, 6);
                break;
            case R.id.qita:
                result = MyArraysUtils.resetSwitch(switch_pay, pay, textViews, 7);
                break;
            case R.id.tv_sign_up_go_back:
                if (iFragmentChange != null) {
                    iFragmentChange.lastStep(this);
                }
                break;
            case R.id.tv_sign_up_go_forward:
                if (!MyArraysUtils.isContainTrue(switch_pay)) {
                    ((BuildingRecordActivity) getActivity()).speak(R.string.select_least_one);
                    return;
                }
                ((BuildingRecordActivity) getActivity()).buildingRecordBean.setMedicalPayments(MyArraysUtils.getIndex(result,pay,index));
                if (iFragmentChange != null) {
                    iFragmentChange.nextStep(this);
                }
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (view != null) {
            ((ViewGroup) view.getParent()).removeView(view);
        }
    }
}
