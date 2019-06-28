package com.gcml.module_auth_hospital.ui.profile;


import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.OnLifecycleEvent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
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
public class UserInfoBaseFragment extends LazyFragment {

    private TextView tv_name;
    private TextView tv_age;
    private TextView tv_gender;
    private TextView tv_height;
    private TextView tv_weight;
    private TextView tv_blood_type;
    private TextView tv_birthday;
    private TextView tv_id_card;
    private TextView tv_address;

    private TextView tvActionName;
    private TextView tvActionAge;
    private TextView tvActionGender;
    private TextView tvActionHeight;
    private TextView tvActionWeight;
    private TextView tvActionBloodType;
    private TextView tvActionBirthday;
    private TextView tvActionIdCard;
    private TextView tvActionAddress;

    private ConstraintLayout clItemName;
    private ConstraintLayout clItemAge;
    private ConstraintLayout clItemGender;
    private ConstraintLayout clItemHeight;
    private ConstraintLayout clItemWeight;
    private ConstraintLayout clItemBloodType;
    private ConstraintLayout clItemBirthday;
    private ConstraintLayout clItemIdCard;
    private ConstraintLayout clItemAddress;

    public UserInfoBaseFragment() {
        // Required empty public constructor
    }

    private UserEntity user;

    public void setUser(UserEntity user) {
        this.user = user;
        if (isPageResume()) {
            showUser(user);
        }
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_info_base, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tv_name = view.findViewById(R.id.tv_name);
        tv_age = view.findViewById(R.id.tv_age);
        tv_gender = view.findViewById(R.id.tv_gender);
        tv_height = view.findViewById(R.id.tv_height);
        tv_weight = view.findViewById(R.id.tv_weight);
        tv_blood_type = view.findViewById(R.id.tv_blood_type);
        tv_birthday = view.findViewById(R.id.tv_birthday);
        tv_id_card = view.findViewById(R.id.tv_ic_card);
        tv_address = view.findViewById(R.id.tv_address);

        tvActionName = view.findViewById(R.id.iv_action_name);
        tvActionAge = view.findViewById(R.id.iv_action_age);
        tvActionGender = view.findViewById(R.id.iv_action_gender);
        tvActionHeight = view.findViewById(R.id.iv_action_height);
        tvActionWeight = view.findViewById(R.id.iv_action_weight);
        tvActionBloodType = view.findViewById(R.id.iv_action_blood_type);
        tvActionBirthday = view.findViewById(R.id.iv_action_birthday);
        tvActionIdCard = view.findViewById(R.id.iv_action_ic_card);
        tvActionAddress = view.findViewById(R.id.iv_action_address);

        clItemName = view.findViewById(R.id.cl_item_name);
        tvActionName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentActivity activity = getActivity();
                if (activity == null) {
                    return;
                }
                ((UserInfoActivity) activity).updateName();
            }
        });
        clItemAge = view.findViewById(R.id.cl_item_age);
        tvActionAge.setVisibility(View.GONE);
        tvActionAge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentActivity activity = getActivity();
                if (activity == null) {
                    return;
                }
                ((UserInfoActivity) activity).selectAge();
            }
        });
        clItemGender = view.findViewById(R.id.cl_item_gender);
        tvActionGender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentActivity activity = getActivity();
                if (activity == null) {
                    return;
                }
                ((UserInfoActivity) activity).selectSex();
            }
        });
        clItemHeight = view.findViewById(R.id.cl_item_height);
        tvActionHeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentActivity activity = getActivity();
                if (activity == null) {
                    return;
                }
                ((UserInfoActivity) activity).selectHeight();
            }
        });
        clItemWeight = view.findViewById(R.id.cl_item_weight);
        tvActionWeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentActivity activity = getActivity();
                if (activity == null) {
                    return;
                }
                ((UserInfoActivity) activity).selectWeight();
            }
        });
        clItemBloodType = view.findViewById(R.id.cl_item_blood_type);
        tvActionBloodType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentActivity activity = getActivity();
                if (activity == null) {
                    return;
                }
                ((UserInfoActivity) activity).selectBloodType();
            }
        });
        clItemBirthday = view.findViewById(R.id.cl_item_birthday);
        tvActionBirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentActivity activity = getActivity();
                if (activity == null) {
                    return;
                }
                ((UserInfoActivity) activity).selectBirthday();
            }
        });

        clItemIdCard = view.findViewById(R.id.cl_item_ic_card);
//        tvActionIdCard.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                FragmentActivity activity = getActivity();
//                if (activity == null) {
//                    return;
//                }
//                ((UserInfoActivity) activity).updateIdCard();
//            }
//        });
        clItemAddress = view.findViewById(R.id.cl_item_address);
        tvActionAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentActivity activity = getActivity();
                if (activity == null) {
                    return;
                }
                ((UserInfoActivity) activity).updateAddress();
            }
        });
    }

    @Override
    protected void onPageResume() {
        super.onPageResume();
        showUser(user);
    }

    public void showUser(UserEntity user) {
        this.user = user;


        if (!isPageResume() || user == null) {
            return;
        }

//        ImageLoader.with(this)
//                .load(user.avatar)
//                .circle()
//                .placeholder(R.drawable.avatar_placeholder)
//                .error(R.drawable.avatar_placeholder)
//                .into(binding.ivAvatar);

        tv_name.setText(TextUtils.isEmpty(user.name) ? "暂未填写" : user.name);
        tv_age.setText(TextUtils.isEmpty(user.age) ? "暂未填写" : user.age + "岁");
        tv_gender.setText(TextUtils.isEmpty(user.sex) ? "暂未填写" : user.sex);
        tv_height.setText(TextUtils.isEmpty(user.height) ? "暂未填写" : user.height + "cm");
        tv_weight.setText(TextUtils.isEmpty(user.weight) ? "暂未填写" : user.weight + "kg");
        tv_blood_type.setText(TextUtils.isEmpty(user.bloodType) ? "暂未填写" : user.bloodType + "型");
//        binding.tvWc.setText(TextUtils.isEmpty(user.waist) ? "暂未填写" : user.waist + "cm");
//        binding.tvPhone.setText(TextUtils.isEmpty(user.phone) ? "暂未填写" : user.phone);
//        binding.tvDeviceId.setText(user.deviceId);
        tv_birthday.setText(TextUtils.isEmpty(user.birthday) ? "暂未填写" : user.birthday);
        if (!TextUtils.isEmpty(user.idCard) && user.idCard.length() == 18) {
            String shenfen = user.idCard.substring(0, 6)
                    + "********"
                    + user.idCard.substring(user.idCard.length() - 4, user.idCard.length());
            tv_id_card.setText(shenfen);
        } else {
            tv_id_card.setText("暂未填写");
        }
        tv_address.setText(TextUtils.isEmpty(user.address) ? "暂未填写" : user.address);
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
