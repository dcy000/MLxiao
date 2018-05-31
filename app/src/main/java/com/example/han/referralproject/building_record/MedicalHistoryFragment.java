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

public class MedicalHistoryFragment extends Fragment implements View.OnClickListener {
    private View view;
    private TextView title;
    private TextView gaoxueya;
    private TextView tangniaobing;
    private TextView guanxinbing;
    private TextView zhongliu;
    private TextView feibing;
    private TextView jingshenzhangai;
    private TextView naocuzhong;
    private TextView jiehe;
    private TextView feiyan;
    private TextView zhiyebing;
    private TextView qitachuanranbing;
    private TextView qita;
    private TextView wu;
    private TextView tvSignUpGoBack;
    private TextView tvSignUpGoForward;
    private IFragmentChange iFragmentChange;

    public void setOnFragmentChange(IFragmentChange iFragmentChange) {
        this.iFragmentChange = iFragmentChange;
    }

    private boolean[] switch_medica = new boolean[13];
    private String[] medica = new String[]{"高血压", "糖尿病", "冠心病", "恶性肿瘤", "慢性阻塞性肺疾病", "严重精神障碍", "脑卒中", "结核病", "肝炎", "职业病", "其他法定传染病", "其他", "无"};
    private String[] index = new String[]{"2", "3", "4", "6", "5", "8", "7", "9", "10", "12", "11", "13", "1"};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_medical_history, container, false);
            initView(view);
        }
        return view;
    }

    private void initView(View view) {
        title = (TextView) view.findViewById(R.id.title);
        gaoxueya = (TextView) view.findViewById(R.id.gaoxueya);
        gaoxueya.setOnClickListener(this);
        tangniaobing = (TextView) view.findViewById(R.id.tangniaobing);
        tangniaobing.setOnClickListener(this);
        guanxinbing = (TextView) view.findViewById(R.id.guanxinbing);
        guanxinbing.setOnClickListener(this);
        zhongliu = (TextView) view.findViewById(R.id.zhongliu);
        zhongliu.setOnClickListener(this);
        feibing = (TextView) view.findViewById(R.id.feibing);
        feibing.setOnClickListener(this);
        jingshenzhangai = (TextView) view.findViewById(R.id.jingshenzhangai);
        jingshenzhangai.setOnClickListener(this);
        naocuzhong = (TextView) view.findViewById(R.id.naocuzhong);
        naocuzhong.setOnClickListener(this);
        jiehe = (TextView) view.findViewById(R.id.jiehe);
        jiehe.setOnClickListener(this);
        feiyan = (TextView) view.findViewById(R.id.feiyan);
        feiyan.setOnClickListener(this);
        zhiyebing = (TextView) view.findViewById(R.id.zhiyebing);
        zhiyebing.setOnClickListener(this);
        qitachuanranbing = (TextView) view.findViewById(R.id.qitachuanranbing);
        qitachuanranbing.setOnClickListener(this);
        qita = (TextView) view.findViewById(R.id.qita);
        qita.setOnClickListener(this);
        wu = (TextView) view.findViewById(R.id.wu);
        wu.setOnClickListener(this);
        tvSignUpGoBack = (TextView) view.findViewById(R.id.tv_sign_up_go_back);
        tvSignUpGoBack.setOnClickListener(this);
        tvSignUpGoForward = (TextView) view.findViewById(R.id.tv_sign_up_go_forward);
        tvSignUpGoForward.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((BuildingRecordActivity) getActivity()).setDisableGlobalListen(true);
        ((BuildingRecordActivity) getActivity()).speak("主人,请选择您的既往病史");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.gaoxueya:
                if (gaoxueya.isSelected()) {
                    gaoxueya.setSelected(false);
                    switch_medica[0] = false;
                } else {
                    gaoxueya.setSelected(true);
                    switch_medica[0] = true;
                    setWuSelectedNot();
                }
                break;
            case R.id.tangniaobing:
                if (tangniaobing.isSelected()) {
                    tangniaobing.setSelected(false);
                    switch_medica[1] = false;
                } else {
                    tangniaobing.setSelected(true);
                    switch_medica[1] = true;
                    setWuSelectedNot();
                }
                break;
            case R.id.guanxinbing:
                if (guanxinbing.isSelected()) {
                    guanxinbing.setSelected(false);
                    switch_medica[2] = false;
                } else {
                    guanxinbing.setSelected(true);
                    switch_medica[2] = true;
                    setWuSelectedNot();
                }
                break;
            case R.id.zhongliu:
                if (zhongliu.isSelected()) {
                    zhongliu.setSelected(false);
                    switch_medica[3] = false;
                } else {
                    zhongliu.setSelected(true);
                    switch_medica[3] = true;
                    setWuSelectedNot();
                }
                break;
            case R.id.feibing:
                if (feibing.isSelected()) {
                    feibing.setSelected(false);
                    switch_medica[4] = false;
                } else {
                    feibing.setSelected(true);
                    switch_medica[4] = true;
                    setWuSelectedNot();
                }
                break;
            case R.id.jingshenzhangai:
                if (jingshenzhangai.isSelected()) {
                    jingshenzhangai.setSelected(false);
                    switch_medica[5] = false;
                } else {
                    jingshenzhangai.setSelected(true);
                    switch_medica[5] = true;
                    setWuSelectedNot();
                }
                break;
            case R.id.naocuzhong:
                if (naocuzhong.isSelected()) {
                    naocuzhong.setSelected(false);
                    switch_medica[6] = false;
                } else {
                    naocuzhong.setSelected(true);
                    switch_medica[6] = true;
                    setWuSelectedNot();
                }
                break;
            case R.id.jiehe:
                if (jiehe.isSelected()) {
                    jiehe.setSelected(false);
                    switch_medica[7] = false;
                } else {
                    jiehe.setSelected(true);
                    switch_medica[7] = true;
                    setWuSelectedNot();
                }
                break;
            case R.id.feiyan:
                if (feiyan.isSelected()) {
                    feiyan.setSelected(false);
                    switch_medica[8] = false;
                } else {
                    feiyan.setSelected(true);
                    switch_medica[8] = true;
                    setWuSelectedNot();
                }
                break;
            case R.id.zhiyebing:
                if (zhiyebing.isSelected()) {
                    zhiyebing.setSelected(false);
                    switch_medica[9] = false;
                } else {
                    zhiyebing.setSelected(true);
                    switch_medica[9] = true;
                    setWuSelectedNot();
                }
                break;
            case R.id.qitachuanranbing:
                if (qitachuanranbing.isSelected()) {
                    qitachuanranbing.setSelected(false);
                    switch_medica[10] = false;
                } else {
                    qitachuanranbing.setSelected(true);
                    switch_medica[10] = true;
                    setWuSelectedNot();
                }
                break;
            case R.id.qita:
                if (qita.isSelected()) {
                    qita.setSelected(false);
                    switch_medica[11] = false;
                } else {
                    qita.setSelected(true);
                    switch_medica[11] = true;
                    setWuSelectedNot();
                }
                break;
            case R.id.wu:
                if (wu.isSelected()) {
                    setWuSelectedNot();
                } else {
                    wu.setSelected(true);
                    switch_medica[12] = true;
                    setOthersNot();
                }
                break;
            case R.id.tv_sign_up_go_back:
                if (iFragmentChange != null) {
                    iFragmentChange.lastStep(this);
                }
                break;
            case R.id.tv_sign_up_go_forward:
                if (!MyArraysUtils.isContainTrue(switch_medica)) {
                    ((BuildingRecordActivity) getActivity()).speak(R.string.select_least_one);
                    return;
                }
                String allergic = MyArraysUtils.jointString(switch_medica, medica);
                ((BuildingRecordActivity) getActivity()).buildingRecordBean.setDiseasesHistory(MyArraysUtils.jointIndex(allergic, medica, index));
                if (iFragmentChange != null) {
                    iFragmentChange.nextStep(this);
                }
                break;
        }
    }

    private void setWuSelectedNot() {
        wu.setSelected(false);
        switch_medica[12] = false;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (view != null) {
            ((ViewGroup) view.getParent()).removeView(view);
        }
    }

    public void setOthersNot() {
        gaoxueya.setSelected(false);
        switch_medica[0] = false;
        tangniaobing.setSelected(false);
        switch_medica[1] = false;
        guanxinbing.setSelected(false);
        switch_medica[2] = false;
        zhongliu.setSelected(false);
        switch_medica[3] = false;
        feibing.setSelected(false);
        switch_medica[4] = false;
        jingshenzhangai.setSelected(false);
        switch_medica[5] = false;
        naocuzhong.setSelected(false);
        switch_medica[6] = false;
        jiehe.setSelected(false);
        switch_medica[7] = false;
        feiyan.setSelected(false);
        switch_medica[8] = false;
        zhiyebing.setSelected(false);
        switch_medica[9] = false;
        qitachuanranbing.setSelected(false);
        switch_medica[10] = false;
        qita.setSelected(false);
        switch_medica[11] = false;
    }
}
