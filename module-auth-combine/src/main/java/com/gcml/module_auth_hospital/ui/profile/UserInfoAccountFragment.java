package com.gcml.module_auth_hospital.ui.profile;


import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.OnLifecycleEvent;
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

import timber.log.Timber;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserInfoAccountFragment extends LazyFragment {


    private TextView tvPhone;

    private TextView tvActionGender;

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
        view.findViewById(R.id.iv_action_phone).setOnClickListener(new View.OnClickListener() {
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
        FragmentActivity activity = getActivity();
        if (activity != null) {
            showUser(((UserInfoActivity) activity).getUser());
        }
    }

    void showUser(UserEntity user) {
        this.user = user;
        if (!isPageResume() || user == null) {
            return;
        }
        tvPhone.setText(TextUtils.isEmpty(user.phone) ? "暂未填写" : user.phone);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void onPauseEvent() {
        Timber.w("@OnLifecycleEvent(Lifecycle.Event.ON_PAUSE) isVisible = %s", isVisible());
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void onResumeEvent() {
        Timber.w("@OnLifecycleEvent(Lifecycle.Event.ON_RESUME) isVisible = %s", isVisible());
    }
}
