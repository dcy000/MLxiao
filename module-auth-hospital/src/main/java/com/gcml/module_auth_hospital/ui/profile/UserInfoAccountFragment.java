package com.gcml.module_auth_hospital.ui.profile;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gcml.common.LazyFragment;
import com.gcml.common.data.UserEntity;
import com.gcml.module_auth_hospital.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserInfoAccountFragment extends LazyFragment {


    public UserInfoAccountFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_info_account, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    void showUser(UserEntity user) {

    }
}
