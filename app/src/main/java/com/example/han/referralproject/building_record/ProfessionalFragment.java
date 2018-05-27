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

public class ProfessionalFragment extends Fragment implements View.OnClickListener {
    private View view;
    private TextView title;
    private TextView guojiajiguan;
    private TextView junren;
    private TextView daxuezhuanke;
    private TextView zhuanyejishu;
    private TextView shangyefuwu;
    private TextView nonglinmuyu;
    private TextView shebeicaozuo;
    private TextView qita;
    private TextView wuzhiye;
    private TextView tvSignUpGoBack;
    private TextView tvSignUpGoForward;
    private IFragmentChange iFragmentChange;
    private String result;

    public void setOnFragmentChange(IFragmentChange iFragmentChange) {
        this.iFragmentChange = iFragmentChange;
    }
    private boolean[] switch_profress=new boolean[9];
    private String[] profress=new String[]{"国家机关","军人","办事人员和有关人员","专业技术人员","商业、服务业人员","农、林、牧、渔、水利业生产人员","生产、运输设备操作人员及有关人员","不便分类的其他从业人员","无职业"};
    private List<TextView> textViews;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_profession, container, false);
            initView(view);
        }
        return view;
    }

    private void initView(View view) {
        title = view.findViewById(R.id.title);
        guojiajiguan = view.findViewById(R.id.guojiajiguan);
        guojiajiguan.setOnClickListener(this);
        junren = view.findViewById(R.id.junren);
        junren.setOnClickListener(this);
        daxuezhuanke = view.findViewById(R.id.daxuezhuanke);
        daxuezhuanke.setOnClickListener(this);
        zhuanyejishu = view.findViewById(R.id.zhuanyejishu);
        zhuanyejishu.setOnClickListener(this);
        shangyefuwu = view.findViewById(R.id.shangyefuwu);
        shangyefuwu.setOnClickListener(this);
        nonglinmuyu = view.findViewById(R.id.nonglinmuyu);
        nonglinmuyu.setOnClickListener(this);
        shebeicaozuo = view.findViewById(R.id.shebeicaozuo);
        shebeicaozuo.setOnClickListener(this);
        qita = view.findViewById(R.id.qita);
        qita.setOnClickListener(this);
        wuzhiye = view.findViewById(R.id.wuzhiye);
        wuzhiye.setOnClickListener(this);
        tvSignUpGoBack = view.findViewById(R.id.tv_sign_up_go_back);
        tvSignUpGoBack.setOnClickListener(this);
        tvSignUpGoForward = view.findViewById(R.id.tv_sign_up_go_forward);
        tvSignUpGoForward.setOnClickListener(this);
        textViews=new ArrayList<>();
        textViews.add(guojiajiguan);
        textViews.add(junren);
        textViews.add(daxuezhuanke);
        textViews.add(zhuanyejishu);
        textViews.add(shangyefuwu);
        textViews.add(nonglinmuyu);
        textViews.add(shebeicaozuo);
        textViews.add(qita);
        textViews.add(wuzhiye);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((BuildingRecordActivity) getActivity()). setDisableGlobalListen(true);
        ((BuildingRecordActivity) getActivity()).speak("主人,请输入您目前从事的职业");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.guojiajiguan:
                result = MyArraysUtils.resetSwitch(switch_profress, profress, textViews, 0);
                break;
            case R.id.junren:
                result = MyArraysUtils.resetSwitch(switch_profress, profress, textViews, 1);
                break;
            case R.id.daxuezhuanke:
                result = MyArraysUtils.resetSwitch(switch_profress, profress, textViews, 2);
                break;
            case R.id.zhuanyejishu:
                result = MyArraysUtils.resetSwitch(switch_profress, profress, textViews, 3);
                break;
            case R.id.shangyefuwu:
                result = MyArraysUtils.resetSwitch(switch_profress, profress, textViews, 4);
                break;
            case R.id.nonglinmuyu:
                result = MyArraysUtils.resetSwitch(switch_profress, profress, textViews, 5);
                break;
            case R.id.shebeicaozuo:
                result = MyArraysUtils.resetSwitch(switch_profress, profress, textViews, 6);
                break;
            case R.id.qita:
                result = MyArraysUtils.resetSwitch(switch_profress, profress, textViews, 7);
                break;
            case R.id.wuzhiye:
                result = MyArraysUtils.resetSwitch(switch_profress, profress, textViews, 8);
                break;
            case R.id.tv_sign_up_go_back:
                if (iFragmentChange != null) {
                    iFragmentChange.lastStep(this);
                }
                break;
            case R.id.tv_sign_up_go_forward:
                if (!MyArraysUtils.isContainTrue(switch_profress)) {
                    ((BuildingRecordActivity) getActivity()).speak(R.string.select_least_one);
                    return;
                }
                ((BuildingRecordActivity) getActivity()).buildingRecordBean.setMaritalStatus(result);
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
