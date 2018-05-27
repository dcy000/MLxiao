package com.example.han.referralproject.intelligent_system.intelligent_diagnosis;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.application.MyApplication;
import com.example.han.referralproject.bean.UserInfo;
import com.example.han.referralproject.constant.ConstantData;
import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.network.NetworkManager;
import com.example.han.referralproject.personal.PersonActivity;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by lenovo on 2018/3/10.
 */

public class DietAdviceFragment extends Fragment {
    @BindView(R.id.tv_person)
    TextView tvPerson;
    Unbinder unbinder;

    @Nullable
    @Override

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_diet_advice, container, false);
        unbinder = ButterKnife.bind(this, view);
        SharedPreferences sp = getActivity().getSharedPreferences(ConstantData.PERSON_MSG, Context.MODE_PRIVATE);
        tvPerson.setText("使用人:" + sp.getString("userName", ""));
        return view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


}
