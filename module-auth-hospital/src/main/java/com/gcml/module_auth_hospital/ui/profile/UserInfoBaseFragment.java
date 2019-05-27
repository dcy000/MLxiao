package com.gcml.module_auth_hospital.ui.profile;


import android.os.Bundle;
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
public class UserInfoBaseFragment extends LazyFragment {


    public UserInfoBaseFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_info_base, container, false);
    }

    void showUser(UserEntity user) {

    }
}
