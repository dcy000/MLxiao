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

public class DisabilityFragment extends Fragment implements View.OnClickListener {
    private View view;
    private TextView title1;
    private TextView shili;
    private TextView tingli;
    private TextView yanyu;
    private TextView zhiti;
    private LinearLayout ll1;
    private TextView zhili;
    private TextView jingshen;
    private TextView qita;
    private TextView wu;
    private LinearLayout ll2;
    private TextView tvSignUpGoBack;
    private TextView tvSignUpGoForward;
    private IFragmentChange iFragmentChange;

    public void setOnFragmentChange(IFragmentChange iFragmentChange) {
        this.iFragmentChange = iFragmentChange;
    }

    private String[] disability = new String[]{"视力残疾", "听力残疾", "言语残疾", "肢体残疾", "智力残疾", "精神残疾", "其他", "无"};
    private boolean[] switch_disability = new boolean[8];
    private String[] index = new String[]{"2", "3", "4", "5", "6", "7", "8", "1"};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_disability, container, false);
            initView(view);
        }
        return view;
    }

    private void initView(View view) {
        title1 = (TextView) view.findViewById(R.id.title1);
        shili = (TextView) view.findViewById(R.id.shili);
        shili.setOnClickListener(this);
        tingli = (TextView) view.findViewById(R.id.tingli);
        tingli.setOnClickListener(this);
        yanyu = (TextView) view.findViewById(R.id.yanyu);
        yanyu.setOnClickListener(this);
        zhiti = (TextView) view.findViewById(R.id.zhiti);
        zhiti.setOnClickListener(this);
        ll1 = (LinearLayout) view.findViewById(R.id.ll_1);
        zhili = (TextView) view.findViewById(R.id.zhili);
        zhili.setOnClickListener(this);
        jingshen = (TextView) view.findViewById(R.id.jingshen);
        jingshen.setOnClickListener(this);
        qita = (TextView) view.findViewById(R.id.qita);
        qita.setOnClickListener(this);
        wu = (TextView) view.findViewById(R.id.wu);
        wu.setOnClickListener(this);
        ll2 = (LinearLayout) view.findViewById(R.id.ll_2);
        tvSignUpGoBack = (TextView) view.findViewById(R.id.tv_sign_up_go_back);
        tvSignUpGoBack.setOnClickListener(this);
        tvSignUpGoForward = (TextView) view.findViewById(R.id.tv_sign_up_go_forward);
        tvSignUpGoForward.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((BuildingRecordActivity) getActivity()).setDisableGlobalListen(true);
        ((BuildingRecordActivity) getActivity()).speak("主人,您是否有残疾？");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.shili:
                if (shili.isSelected()) {
                    shili.setSelected(false);
                    switch_disability[0] = false;
                } else {
                    shili.setSelected(true);
                    switch_disability[0] = true;
                    setWuNot();
                }
                break;
            case R.id.tingli:
                if (tingli.isSelected()) {
                    tingli.setSelected(false);
                    switch_disability[1] = false;
                } else {
                    tingli.setSelected(true);
                    switch_disability[1] = true;
                    setWuNot();
                }
                break;
            case R.id.yanyu:
                if (yanyu.isSelected()) {
                    yanyu.setSelected(false);
                    switch_disability[2] = false;
                } else {
                    yanyu.setSelected(true);
                    switch_disability[2] = true;
                    setWuNot();
                }
                break;
            case R.id.zhiti:
                if (zhiti.isSelected()) {
                    zhiti.setSelected(false);
                    switch_disability[3] = false;
                } else {
                    zhiti.setSelected(true);
                    switch_disability[3] = true;
                    setWuNot();
                }
                break;
            case R.id.zhili:
                if (zhili.isSelected()) {
                    zhili.setSelected(false);
                    switch_disability[4] = false;
                } else {
                    zhili.setSelected(true);
                    switch_disability[4] = true;
                    setWuNot();
                }
                break;
            case R.id.jingshen:
                if (jingshen.isSelected()) {
                    jingshen.setSelected(false);
                    switch_disability[5] = false;
                } else {
                    jingshen.setSelected(true);
                    switch_disability[5] = true;
                    setWuNot();
                }
                break;
            case R.id.qita:
                if (qita.isSelected()) {
                    qita.setSelected(false);
                    switch_disability[6] = false;
                } else {
                    qita.setSelected(true);
                    switch_disability[6] = true;
                    setWuNot();
                }
                break;
            case R.id.wu:
                if (wu.isSelected()) {
                    setWuNot();
                } else {
                    wu.setSelected(true);
                    switch_disability[7] = true;
                    setOthersNot();
                }
                break;
            case R.id.tv_sign_up_go_back:
                if (iFragmentChange != null) {
                    iFragmentChange.lastStep(this);
                }
                break;
            case R.id.tv_sign_up_go_forward:
                if (!MyArraysUtils.isContainTrue(switch_disability)) {
                    ((BuildingRecordActivity) getActivity()).speak(R.string.select_least_one);
                    return;
                }
                String result = MyArraysUtils.jointString(switch_disability, disability);
                ((BuildingRecordActivity) getActivity()).buildingRecordBean.setDisabilitySituation(MyArraysUtils.jointIndex(result, disability, index));
                if (iFragmentChange != null) {
                    iFragmentChange.nextStep(this);
                }
                break;
        }
    }

    private void setWuNot() {
        wu.setSelected(false);
        switch_disability[7] = false;
    }


    private void setOthersNot() {
        shili.setSelected(false);
        switch_disability[0] = false;
        tingli.setSelected(false);
        switch_disability[1] = false;
        yanyu.setSelected(false);
        switch_disability[2] = false;
        zhiti.setSelected(false);
        switch_disability[3] = false;
        zhili.setSelected(false);
        switch_disability[4] = false;
        jingshen.setSelected(false);
        switch_disability[5] = false;
        qita.setSelected(false);
        switch_disability[6] = false;

    }
}
