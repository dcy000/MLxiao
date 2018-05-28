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

public class BloodTypeFragment extends Fragment implements View.OnClickListener {
    private View view;
    private TextView title1;
    private TextView aType;
    private TextView bType;
    private TextView oType;
    private TextView abType;
    private TextView buxiang;
    private TextView title2;
    private TextView yinxing;
    private TextView yangxing;
    private TextView buxiang2;
    private TextView tvSignUpGoBack;
    private TextView tvSignUpGoForward;
    private IFragmentChange iFragmentChange;
    private List<TextView> bloodTypes;
    private List<TextView> rhTextViews;

    public void setOnFragmentChange(IFragmentChange iFragmentChange) {
        this.iFragmentChange = iFragmentChange;
    }

    private String[] bloodType = new String[]{"A型", "B型", "O型", "AB型", "不详"};
    private boolean[] switch_bloodType = new boolean[5];
    private String[] rh = new String[]{"阴性", "阳性", "不详"};
    private boolean[] switch_rh = new boolean[3];
    private String string_bloodType = "";
    private String string_rh = "";
    private String[] index1=new String[]{"1","2","3","4","0"};
    private String[] index2=new String[]{"1","2","0"};
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_blood_type, container, false);
            initView(view);
        }
        return view;
    }

    private void initView(View view) {
        title1 = (TextView) view.findViewById(R.id.title1);
        aType = (TextView) view.findViewById(R.id.a_type);
        aType.setOnClickListener(this);
        bType = (TextView) view.findViewById(R.id.b_type);
        bType.setOnClickListener(this);
        oType = (TextView) view.findViewById(R.id.o_type);
        oType.setOnClickListener(this);
        abType = (TextView) view.findViewById(R.id.ab_type);
        abType.setOnClickListener(this);
        buxiang = (TextView) view.findViewById(R.id.buxiang);
        buxiang.setOnClickListener(this);
        title2 = (TextView) view.findViewById(R.id.title2);
        yinxing = (TextView) view.findViewById(R.id.yinxing);
        yinxing.setOnClickListener(this);
        yangxing = (TextView) view.findViewById(R.id.yangxing);
        yangxing.setOnClickListener(this);
        buxiang2 = (TextView) view.findViewById(R.id.buxiang2);
        buxiang2.setOnClickListener(this);
        tvSignUpGoBack = (TextView) view.findViewById(R.id.tv_sign_up_go_back);
        tvSignUpGoBack.setOnClickListener(this);
        tvSignUpGoForward = (TextView) view.findViewById(R.id.tv_sign_up_go_forward);
        tvSignUpGoForward.setOnClickListener(this);
        bloodTypes = new ArrayList<>();
        bloodTypes.add(aType);
        bloodTypes.add(bType);
        bloodTypes.add(oType);
        bloodTypes.add(abType);
        bloodTypes.add(buxiang);
        rhTextViews = new ArrayList<>();
        rhTextViews.add(yinxing);
        rhTextViews.add(yangxing);
        rhTextViews.add(buxiang2);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((BuildingRecordActivity) getActivity()). setDisableGlobalListen(true);
        ((BuildingRecordActivity) getActivity()).speak("主人,请输入您的血型");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.a_type:
                string_bloodType = MyArraysUtils.resetSwitch(switch_bloodType, bloodType, bloodTypes, 0);
                break;
            case R.id.b_type:
                string_bloodType = MyArraysUtils.resetSwitch(switch_bloodType, bloodType, bloodTypes, 1);
                break;
            case R.id.o_type:
                string_bloodType = MyArraysUtils.resetSwitch(switch_bloodType, bloodType, bloodTypes, 2);
                break;
            case R.id.ab_type:
                string_bloodType = MyArraysUtils.resetSwitch(switch_bloodType, bloodType, bloodTypes, 3);
                break;
            case R.id.buxiang:
                string_bloodType = MyArraysUtils.resetSwitch(switch_bloodType, bloodType, bloodTypes, 4);
                break;
            case R.id.yinxing:
                string_rh = MyArraysUtils.resetSwitch(switch_rh, rh, rhTextViews, 0);
                break;
            case R.id.yangxing:
                string_rh = MyArraysUtils.resetSwitch(switch_rh, rh, rhTextViews, 1);
                break;
            case R.id.buxiang2:
                string_rh = MyArraysUtils.resetSwitch(switch_rh, rh, rhTextViews, 2);
                break;
            case R.id.tv_sign_up_go_back:
                if (iFragmentChange != null) {
                    iFragmentChange.lastStep(this);
                }
                break;
            case R.id.tv_sign_up_go_forward:
                if (!MyArraysUtils.isContainTrue(switch_bloodType) || !MyArraysUtils.isContainTrue(switch_rh)) {
                    ((BuildingRecordActivity) getActivity()).speak(R.string.select_least_one);
                    return;
                }

                ((BuildingRecordActivity) getActivity()).buildingRecordBean.setBloodType( MyArraysUtils.getIndex(string_bloodType,bloodType,index1));
                ((BuildingRecordActivity) getActivity()).buildingRecordBean.setRhBlood(MyArraysUtils.getIndex(string_rh,rh,index2));

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
