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

public class HereditaryDiseaseFragment extends Fragment implements View.OnClickListener {
    private View view;
    private TextView mTitle1;
    private TextView mYes;
    private TextView mNo;
    private TextView mTvSignUpGoBack;
    private TextView mTvSignUpGoForward;
    private IFragmentChange iFragmentChange;
    private String result = "";

    public void setOnFragmentChange(IFragmentChange iFragmentChange) {
        this.iFragmentChange = iFragmentChange;
    }

    private String[] hereditary = new String[]{"是", "否"};
    private boolean[] switch_hereditary = new boolean[2];
    private List<TextView> textViews;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_hereditary, container, false);
        }
        initView(view);
        return view;
    }

    private void initView(View view) {
        mTitle1 = (TextView) view.findViewById(R.id.title1);
        mYes = (TextView) view.findViewById(R.id.yes);
        mYes.setOnClickListener(this);
        mNo = (TextView) view.findViewById(R.id.no);
        mNo.setOnClickListener(this);
        mTvSignUpGoBack = (TextView) view.findViewById(R.id.tv_sign_up_go_back);
        mTvSignUpGoBack.setOnClickListener(this);
        mTvSignUpGoForward = (TextView) view.findViewById(R.id.tv_sign_up_go_forward);
        mTvSignUpGoForward.setOnClickListener(this);
        textViews = new ArrayList<>();
        textViews.add(mYes);
        textViews.add(mNo);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((BuildingRecordActivity) getActivity()). setDisableGlobalListen(true);
        ((BuildingRecordActivity) getActivity()).speak("主人,您是否有遗传病史？");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.yes:
                result = MyArraysUtils.resetSwitch(switch_hereditary, hereditary, textViews, 0);
                break;
            case R.id.no:
                result = MyArraysUtils.resetSwitch(switch_hereditary, hereditary, textViews, 1);
                break;
            case R.id.tv_sign_up_go_back:
                if (iFragmentChange != null) {
                    iFragmentChange.lastStep(this);
                }
                break;
            case R.id.tv_sign_up_go_forward:
                if (!MyArraysUtils.isContainTrue(switch_hereditary)) {
                    ((BuildingRecordActivity) getActivity()).speak(R.string.select_least_one);
                    return;
                }
                ((BuildingRecordActivity) getActivity()).buildingRecordBean.setGeneticHistory(result);
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
