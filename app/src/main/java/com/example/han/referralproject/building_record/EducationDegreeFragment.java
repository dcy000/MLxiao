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
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class EducationDegreeFragment extends Fragment implements View.OnClickListener {
    private View view;
    private TextView title;
    private TextView yanjiusheng;
    private TextView daxuebenke;
    private TextView daxuezhuanke;
    private TextView zhongdengzhuanye;
    private TextView jigongxuexiao;
    private TextView gaozhong;
    private TextView chuzhong;
    private TextView xiaoxue;
    private TextView wenmang;
    private TextView buxiang;
    private TextView tvSignUpGoBack;
    private TextView tvSignUpGoForward;
    private IFragmentChange iFragmentChange;

    public void setOnFragmentChange(IFragmentChange iFragmentChange) {
        this.iFragmentChange = iFragmentChange;
    }

    private boolean[] switch_education = new boolean[10];
    private String[] education = new String[]{"研究生", "大学本科", "大学专科和专科学校", "中等专业学校", "技工学校", "高中", "初中", "小学", "文盲或者半文盲", "不详"};
    private List<TextView> textViews;
    private String result = "";
    private String[] index=new String[]{"0","1","2","3","4","5","6","7","8","9"};
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_education_degree, container, false);
            initView(view);
        }
        return view;
    }

    private void initView(View view) {
        title = (TextView) view.findViewById(R.id.title);
        yanjiusheng = (TextView) view.findViewById(R.id.yanjiusheng);
        yanjiusheng.setOnClickListener(this);
        daxuebenke = (TextView) view.findViewById(R.id.daxuebenke);
        daxuebenke.setOnClickListener(this);
        daxuezhuanke = (TextView) view.findViewById(R.id.daxuezhuanke);
        daxuezhuanke.setOnClickListener(this);
        zhongdengzhuanye = (TextView) view.findViewById(R.id.zhongdengzhuanye);
        zhongdengzhuanye.setOnClickListener(this);
        jigongxuexiao = (TextView) view.findViewById(R.id.jigongxuexiao);
        jigongxuexiao.setOnClickListener(this);
        gaozhong = (TextView) view.findViewById(R.id.gaozhong);
        gaozhong.setOnClickListener(this);
        chuzhong = (TextView) view.findViewById(R.id.chuzhong);
        chuzhong.setOnClickListener(this);
        xiaoxue = (TextView) view.findViewById(R.id.xiaoxue);
        xiaoxue.setOnClickListener(this);
        wenmang = (TextView) view.findViewById(R.id.wenmang);
        wenmang.setOnClickListener(this);
        buxiang = (TextView) view.findViewById(R.id.buxiang);
        buxiang.setOnClickListener(this);
        tvSignUpGoBack = (TextView) view.findViewById(R.id.tv_sign_up_go_back);
        tvSignUpGoBack.setOnClickListener(this);
        tvSignUpGoForward = (TextView) view.findViewById(R.id.tv_sign_up_go_forward);
        tvSignUpGoForward.setOnClickListener(this);
        textViews = new ArrayList<>();
        textViews.add(yanjiusheng);
        textViews.add(daxuebenke);

        textViews.add(daxuezhuanke);

        textViews.add(zhongdengzhuanye);

        textViews.add(jigongxuexiao);

        textViews.add(gaozhong);
        textViews.add(chuzhong);
        textViews.add(xiaoxue);
        textViews.add(wenmang);
        textViews.add(buxiang);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((BuildingRecordActivity) getActivity()). setDisableGlobalListen(true);
        ((BuildingRecordActivity) getActivity()).speak("主人,请输入您的文化程度");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.yanjiusheng:
                result = MyArraysUtils.resetSwitch(switch_education, education, textViews, 0);
                break;
            case R.id.daxuebenke:
                result = MyArraysUtils.resetSwitch(switch_education, education, textViews, 1);
                break;
            case R.id.daxuezhuanke:
                result = MyArraysUtils.resetSwitch(switch_education, education, textViews, 2);
                break;
            case R.id.zhongdengzhuanye:
                result = MyArraysUtils.resetSwitch(switch_education, education, textViews, 3);
                break;
            case R.id.jigongxuexiao:
                result = MyArraysUtils.resetSwitch(switch_education, education, textViews, 4);
                break;
            case R.id.gaozhong:
                result = MyArraysUtils.resetSwitch(switch_education, education, textViews, 5);
                break;
            case R.id.chuzhong:
                result = MyArraysUtils.resetSwitch(switch_education, education, textViews, 6);
                break;
            case R.id.xiaoxue:
                result = MyArraysUtils.resetSwitch(switch_education, education, textViews, 7);
                break;
            case R.id.wenmang:
                result = MyArraysUtils.resetSwitch(switch_education, education, textViews, 8);
                break;
            case R.id.buxiang:
                result = MyArraysUtils.resetSwitch(switch_education, education, textViews, 9);
                break;
            case R.id.tv_sign_up_go_back:
                if (iFragmentChange != null) {
                    iFragmentChange.lastStep(this);
                }
                break;
            case R.id.tv_sign_up_go_forward:
                if (!MyArraysUtils.isContainTrue(switch_education)) {
                    ((BuildingRecordActivity) getActivity()).speak(R.string.select_least_one);
                    return;
                }
                ((BuildingRecordActivity) getActivity()).buildingRecordBean.setEducationalLevel(MyArraysUtils.getIndex(result,education,index));
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
