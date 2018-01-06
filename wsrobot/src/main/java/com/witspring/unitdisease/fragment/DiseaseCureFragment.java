package com.witspring.unitdisease.fragment;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.witspring.base.BaseFragment;
import com.witspring.mlrobot.R;
import com.witspring.mlrobot.databinding.WsdiseFragmentDiseaseCureBinding;
import com.witspring.unitdisease.DiseaseActivity;
import com.witspring.util.StringUtil;

/**
 * @author Created by wu_zf on 1/3/2018.
 * @email :wuzf1234@gmail.com
 */

public class DiseaseCureFragment extends BaseFragment {
    private WsdiseFragmentDiseaseCureBinding binding;
    private DiseaseActivity mActivity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = (DiseaseActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.wsdise_fragment_disease_cure, container, false);
        View view = binding.getRoot();
        initViews();
        return view;
    }

    private void initViews() {
        binding.wsdiseTvCure.setText(dealContentForShowClean(mActivity.getmDisease().getTherapy()));
    }

    public String dealContentForShowClean(String content){
        if (StringUtil.isTrimBlank(content)) {
            return "暂无数据";
        }
        String textContent = content.trim();
        while (textContent.startsWith("　")) {//这里判断是不是全角空格
            textContent = textContent.substring(1, textContent.length()).trim();
        }
        String strResult = "        " + textContent;
        return strResult;
    }

    @Override
    public void onStart() {
        super.onStart();
    }
}
