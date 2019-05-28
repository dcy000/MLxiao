package com.gcml.module_auth_hospital.ui.profile;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gcml.common.LazyFragment;
import com.gcml.common.data.UserEntity;
import com.gcml.module_auth_hospital.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserInfoAccountFragment extends LazyFragment {


    private TextView tvPhone;

    public UserInfoAccountFragment() {
        // Required empty public constructor
    }

    private UserEntity user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_info_account, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvPhone = view.findViewById(R.id.tv_phone);
        view.findViewById(R.id.cl_item_phone).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentActivity activity = getActivity();
                if (activity != null) {
                    ((UserInfoActivity) activity).updatePhone();
                }
            }
        });
    }

    @Override
    protected void onPageResume() {
        super.onPageResume();
        showUser(user);
    }

    void showUser(UserEntity user) {
        this.user = user;
        if (!isPageResume() || user == null) {
           return;
        }
        tvPhone.setText(TextUtils.isEmpty(user.phone) ? "暂未填写" : user.phone);
    }
}
