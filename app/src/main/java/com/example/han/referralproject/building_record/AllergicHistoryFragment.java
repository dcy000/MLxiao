package com.example.han.referralproject.building_record;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.han.referralproject.R;

public class AllergicHistoryFragment extends Fragment implements View.OnClickListener {
    private View view;
    private TextView title1;
    private TextView qingmeisu;
    private TextView toubao;
    private TextView huanganlei;
    private TextView qita;
    private LinearLayout ll1;
    private TextView wu1;
    private TextView title2;
    private TextView huaxuepin;
    private TextView duwu;
    private TextView shexian;
    private TextView wu2;
    private LinearLayout ll2;
    private IFragmentChange iFragmentChange;
    /**
     * 上一步
     */
    private TextView mTvSignUpGoBack;
    /**
     * 下一步
     */
    private TextView mTvSignUpGoForward;
    private boolean isContainTrue;

    public void setOnFragmentChange(IFragmentChange iFragmentChange) {
        this.iFragmentChange = iFragmentChange;
    }

    private String[] anaphylaxis = new String[]{"青霉素", "头孢", "磺胺类", "其他", "无"};
    private boolean[] switch_anaphylaxis = new boolean[5];
    private String[] radiations = new String[]{"化学品", "毒物", "射线", "无"};
    private boolean[] switch_radiations = new boolean[4];
    private String[] index1 = new String[]{"2", "3", "4", "5", "1"};
    private String[] index2 = new String[]{"2", "3", "4", "1"};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_allergic_history, container, false);
            initView(view);
        }
        return view;
    }

    private void initView(View view) {
        title1 = (TextView) view.findViewById(R.id.title1);
        qingmeisu = (TextView) view.findViewById(R.id.qingmeisu);
        qingmeisu.setOnClickListener(this);
        toubao = (TextView) view.findViewById(R.id.toubao);
        toubao.setOnClickListener(this);
        huanganlei = (TextView) view.findViewById(R.id.huanganlei);
        huanganlei.setOnClickListener(this);
        qita = (TextView) view.findViewById(R.id.qita);
        qita.setOnClickListener(this);
        ll1 = (LinearLayout) view.findViewById(R.id.ll_1);
        wu1 = (TextView) view.findViewById(R.id.wu1);
        wu1.setOnClickListener(this);
        title2 = (TextView) view.findViewById(R.id.title2);
        huaxuepin = (TextView) view.findViewById(R.id.huaxuepin);
        huaxuepin.setOnClickListener(this);
        duwu = (TextView) view.findViewById(R.id.duwu);
        duwu.setOnClickListener(this);
        shexian = (TextView) view.findViewById(R.id.shexian);
        shexian.setOnClickListener(this);
        wu2 = (TextView) view.findViewById(R.id.wu2);
        wu2.setOnClickListener(this);
        ll2 = (LinearLayout) view.findViewById(R.id.ll_2);
        mTvSignUpGoBack = (TextView) view.findViewById(R.id.tv_sign_up_go_back);
        mTvSignUpGoBack.setOnClickListener(this);
        mTvSignUpGoForward = (TextView) view.findViewById(R.id.tv_sign_up_go_forward);
        mTvSignUpGoForward.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((BuildingRecordActivity) getActivity()).setDisableGlobalListen(true);
        ((BuildingRecordActivity) getActivity()).speak("主人,请输入您的药物过敏史和暴露史");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.qingmeisu:
                if (qingmeisu.isSelected()) {
                    qingmeisu.setSelected(false);
                    switch_anaphylaxis[0] = false;
                } else {
                    qingmeisu.setSelected(true);
                    switch_anaphylaxis[0] = true;

                    switch_anaphylaxis[4]=false;
                    wu1.setSelected(false);
                }
                break;
            case R.id.toubao:
                if (toubao.isSelected()) {
                    toubao.setSelected(false);
                    switch_anaphylaxis[1] = false;
                } else {
                    toubao.setSelected(true);
                    switch_anaphylaxis[1] = true;

                    switch_anaphylaxis[4]=false;
                    wu1.setSelected(false);
                }
                break;
            case R.id.huanganlei:
                if (huanganlei.isSelected()) {
                    huanganlei.setSelected(false);
                    switch_anaphylaxis[2] = false;
                } else {
                    huanganlei.setSelected(true);
                    switch_anaphylaxis[2] = true;

                    switch_anaphylaxis[4]=false;
                    wu1.setSelected(false);
                }
                break;
            case R.id.qita:
                if (qita.isSelected()) {
                    qita.setSelected(false);
                    switch_anaphylaxis[3] = false;
                } else {
                    qita.setSelected(true);
                    switch_anaphylaxis[3] = true;

                    switch_anaphylaxis[4]=false;
                    wu1.setSelected(false);
                }
                break;
            case R.id.wu1:
                if (wu1.isSelected()) {
                    wu1.setSelected(false);
                    switch_anaphylaxis[4] = false;
                } else {
                    wu1.setSelected(true);
                    switch_anaphylaxis[4] = true;

                    qingmeisu.setSelected(false);
                    toubao.setSelected(false);
                    huanganlei.setSelected(false);
                    qita.setSelected(false);
                    switch_anaphylaxis[0] = false;
                    switch_anaphylaxis[1] = false;
                    switch_anaphylaxis[2] = false;
                    switch_anaphylaxis[3] = false;

                }
                break;
            case R.id.huaxuepin:
                if (huaxuepin.isSelected()) {
                    huaxuepin.setSelected(false);
                    switch_radiations[0] = false;
                } else {
                    huaxuepin.setSelected(true);
                    switch_radiations[0] = true;

                    wu2.setSelected(false);
                    switch_radiations[3] = false;

                }
                break;
            case R.id.duwu:
                if (duwu.isSelected()) {
                    duwu.setSelected(false);
                    switch_radiations[1] = false;
                } else {
                    duwu.setSelected(true);
                    switch_radiations[1] = true;


                    wu2.setSelected(false);
                    switch_radiations[3] = false;
                }
                break;
            case R.id.shexian:
                if (shexian.isSelected()) {
                    shexian.setSelected(false);
                    switch_radiations[2] = false;
                } else {
                    shexian.setSelected(true);
                    switch_radiations[2] = true;

                    wu2.setSelected(false);
                    switch_radiations[3] = false;
                }
                break;
            case R.id.wu2:
                if (wu2.isSelected()) {
                    wu2.setSelected(false);
                    switch_radiations[3] = false;
                } else {
                    wu2.setSelected(true);
                    switch_radiations[3] = true;

                    huaxuepin.setSelected(false);
                    switch_radiations[0] = false;
                    duwu.setSelected(false);
                    switch_radiations[1] = false;


                    shexian.setSelected(false);
                    switch_radiations[2] = false;



                }
                break;
            case R.id.tv_sign_up_go_back:
                if (iFragmentChange != null) {
                    iFragmentChange.lastStep(this);
                }
                break;
            case R.id.tv_sign_up_go_forward:
                if (!MyArraysUtils.isContainTrue(switch_anaphylaxis) || !MyArraysUtils.isContainTrue(switch_radiations)) {
                    ((BuildingRecordActivity) getActivity()).speak(R.string.select_least_one);
                    return;
                }
                String allergic = MyArraysUtils.jointString(switch_anaphylaxis, anaphylaxis);
                String index_result = MyArraysUtils.jointIndex(allergic, anaphylaxis, index1);
                ((BuildingRecordActivity) getActivity()).buildingRecordBean.setMedicationAllergy(index_result);
                String radiction = MyArraysUtils.jointString(switch_radiations, radiations);
                String index2_result = MyArraysUtils.jointIndex(radiction, radiations, index2);
                ((BuildingRecordActivity) getActivity()).buildingRecordBean.setExposureHistory(index2_result);

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
