package com.example.han.referralproject.intelligent_diagnosis;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.gzq.lib_core.base.Box;
import com.gzq.lib_core.bean.UserInfoBean;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by lenovo on 2018/3/10.
 */

public class SportAdviceFragment extends Fragment {
    @BindView(R.id.tv_person)
    TextView tvPerson;
    Unbinder unbinder;

    @Nullable
    @Override

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sport_advice, container, false);
        unbinder = ButterKnife.bind(this, view);
        tvPerson.setText("使用人:" + ((UserInfoBean) Box.getSessionManager().getUser()).bname);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
