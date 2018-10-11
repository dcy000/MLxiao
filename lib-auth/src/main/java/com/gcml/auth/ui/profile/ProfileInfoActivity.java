package com.gcml.auth.ui.profile;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.billy.cc.core.component.CC;
import com.billy.cc.core.component.CCResult;
import com.billy.cc.core.component.IComponentCallback;
import com.gcml.auth.BR;
import com.gcml.auth.R;
import com.gcml.auth.databinding.AuthActivityProfileInfoBinding;
import com.gcml.auth.ui.profile.update.AlertAddressActivity;
import com.gcml.auth.ui.profile.update.AlertAgeActivity;
import com.gcml.auth.ui.profile.update.AlertBloodTypeActivity;
import com.gcml.auth.ui.profile.update.AlertDrinkingActivity;
import com.gcml.auth.ui.profile.update.AlertEatingActivity;
import com.gcml.auth.ui.profile.update.AlertHeightActivity;
import com.gcml.auth.ui.profile.update.AlertIDCardActivity;
import com.gcml.auth.ui.profile.update.AlertMHActivity;
import com.gcml.auth.ui.profile.update.AlertNameActivity;
import com.gcml.auth.ui.profile.update.AlertSexActivity;
import com.gcml.auth.ui.profile.update.AlertSmokeActivity;
import com.gcml.auth.ui.profile.update.AlertSportActivity;
import com.gcml.auth.ui.profile.update.AlertWeightActivity;
import com.gcml.common.data.HealthInfo;
import com.gcml.common.data.UserEntity;
import com.gcml.common.mvvm.BaseActivity;
import com.gcml.common.repository.imageloader.ImageLoader;
import com.gcml.common.repository.utils.DefaultObserver;
import com.gcml.common.utils.RxUtils;
import com.gcml.common.utils.Utils;
import com.gcml.common.widget.dialog.SMSVerificationDialog;
import com.gcml.common.widget.toolbar.ToolBarClickListener;
import com.gcml.lib_utils.display.ToastUtils;

import java.util.Locale;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ProfileInfoActivity extends BaseActivity<AuthActivityProfileInfoBinding, ProfileInfoViewModel> {

    @Override
    protected int layoutId() {
        return R.layout.auth_activity_profile_info;
    }

    @Override
    protected int variableId() {
        return BR.viewModel;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        binding.tbProfileInfo.setData("健 康 档 案", R.drawable.common_icon_back, "返回",
                R.drawable.common_icon_home, null, new ToolBarClickListener() {
                    @Override
                    public void onLeftClick() {
                        finish();
                    }

                    @Override
                    public void onRightClick() {
                        CC.obtainBuilder("com.gcml.old.home")
                                .build().callAsync();
                        finish();
                    }
                });
        binding.ivAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserEntity user = mUser;
                if (user == null
                        || TextUtils.isEmpty(user.phone)
                        || !TextUtils.isDigitsOnly(user.phone)
                        || user.phone.length() != 11) {
                    ToastUtils.showShort("请重新登陆");
                    return;
                }
                SMSVerificationDialog dialog = new SMSVerificationDialog();
                Bundle bundle = new Bundle();
                bundle.putString("phone", user.phone);
                dialog.setArguments(bundle);
                dialog.setListener(() -> {
                    modifyHead();
                });
                dialog.show(getFragmentManager(), "phoneCode");
            }
        });
        binding.clItemName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileInfoActivity.this, AlertNameActivity.class));
            }
        });
        binding.clItemAge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.tvIdCard.getText().toString().equals("暂未填写")) {
                    startActivity(new Intent(ProfileInfoActivity.this, AlertAgeActivity.class));
                } else {
                    ToastUtils.showShort("年龄与身份证号关联,不可更改");
                }
            }
        });
        binding.clItemSex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                selectSex();
                startActivity(new Intent(ProfileInfoActivity.this, AlertSexActivity.class));
            }
        });
        binding.clItemHeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileInfoActivity.this, AlertHeightActivity.class)
                        .putExtra("data", mUser));
            }
        });
        binding.clItemWeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileInfoActivity.this, AlertWeightActivity.class)
                        .putExtra("data", mUser));
            }
        });
        binding.clItemBlood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileInfoActivity.this, AlertBloodTypeActivity.class));
            }
        });
        binding.clItemWc.setVisibility(View.GONE);
        binding.clItemWc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(ProfileInfoActivity.this, AlertBloodTypeActivity.class)
//                .putExtra("data", mUser));
            }
        });
        binding.clItemIdCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileInfoActivity.this, AlertIDCardActivity.class));
            }
        });
        binding.clItemAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileInfoActivity.this, AlertAddressActivity.class)
                        .putExtra("data", mUser));
            }
        });
        binding.clItemSports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileInfoActivity.this, AlertSportActivity.class)
                        .putExtra("data", mUser));
            }
        });
        binding.clItemSmoke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileInfoActivity.this, AlertSmokeActivity.class)
                        .putExtra("data", mUser));
            }
        });
        binding.clItemEat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileInfoActivity.this, AlertEatingActivity.class)
                        .putExtra("data", mUser));
            }
        });
        binding.clItemDrink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileInfoActivity.this, AlertDrinkingActivity.class)
                        .putExtra("data", mUser));
            }
        });
        binding.clItemDesease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileInfoActivity.this, AlertMHActivity.class)
                        .putExtra("data", mUser));
            }
        });
    }

    private void selectSex() {
        OnOptionsSelectListener listener = new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {

            }
        };
        new OptionsPickerBuilder(this, listener)
                .setLineSpacingMultiplier(3f)
                .setSubCalSize(30)
                .setContentTextSize(40)
                .setSubmitColor(Color.parseColor("#FF108EE9"))
                .setCancelColor(Color.parseColor("#FF999999"))
                .setTextColorOut(Color.parseColor("#FF999999"))
                .setTextColorCenter(Color.parseColor("#FF333333"))
                .setLineSpacingMultiplier(Color.WHITE)
                .setDividerColor(Color.WHITE)
                .setBgColor(Color.WHITE)
                .setTitleBgColor(Color.WHITE)
                .build();
    }

    private void modifyHead() {
        CC.obtainBuilder("com.gcml.auth.face.signup")
                .build()
                .callAsyncCallbackOnMainThread(new IComponentCallback() {
                    @Override
                    public void onResult(CC cc, CCResult result) {
                        if (result.isSuccess()) {
                            ToastUtils.showShort("更换人脸成功");
                        }
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }

    private UserEntity mUser;

    private void getData() {
        Observable<UserEntity> data = CC.obtainBuilder("com.gcml.auth.getUser")
                .build()
                .call()
                .getDataItem("data");
        data.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new DefaultObserver<UserEntity>() {
                    @Override
                    public void onNext(UserEntity user) {
                        showUser(user);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                    }
                });
    }

    private void showUser(UserEntity user) {
        mUser = user;
        ImageLoader.with(this)
                .load(mUser.avatar)
                .circle()
                .placeholder(R.drawable.avatar_placeholder)
                .error(R.drawable.avatar_placeholder)
                .into(binding.ivAvatar);
        binding.tvName.setText(TextUtils.isEmpty(user.name) ? "暂未填写" : user.name);
        if (TextUtils.isEmpty(user.idCard)
                || user.idCard.length() != 18) {
            binding.tvAge.setText(TextUtils.isEmpty(user.age) ? "暂未填写" : user.age + "岁");
        } else {
            binding.tvAge.setText(String.format(Locale.getDefault(), "%d岁", Utils.age(user.idCard)));
        }
        binding.tvSex.setText(TextUtils.isEmpty(user.sex) ? "暂未填写" : user.sex);
        binding.tvHeight.setText(TextUtils.isEmpty(user.height) ? "暂未填写" : user.height + "cm");
        binding.tvWeight.setText(TextUtils.isEmpty(user.weight) ? "暂未填写" : user.weight + "Kg");
        binding.tvBlood.setText(TextUtils.isEmpty(user.bloodType) ? "暂未填写" : user.bloodType + "型");

        binding.tvPhone.setText(TextUtils.isEmpty(user.phone) ? "暂未填写" : user.phone);
        binding.tvDeviceId.setText(user.deviceId);
        binding.tvAddress.setText(TextUtils.isEmpty(user.address) ? "暂未填写" : user.address);
        if (!TextUtils.isEmpty(user.idCard) && user.idCard.length() == 18) {
            String shenfen = user.idCard.substring(0, 6)
                    + "********"
                    + user.idCard.substring(user.idCard.length() - 4, user.idCard.length());
            binding.tvIdCard.setText(shenfen);
        } else {
            binding.tvIdCard.setText("暂未填写");
        }

        String sports = HealthInfo.SPORTS_MAP.get(user.sportsHabits);
        binding.tvSports.setText(TextUtils.isEmpty(sports) ? "暂未填写" : sports);
        String smoke = HealthInfo.SMOKE_MAP.get(user.smokeHabits);
        binding.tvSmoke.setText(TextUtils.isEmpty(smoke) ? "暂未填写" : smoke);
        String eat = HealthInfo.EAT_MAP.get(user.eatingHabits);
        binding.tvEat.setText(TextUtils.isEmpty(eat) ? "暂未填写" : eat);
        String drink = HealthInfo.DRINK_MAP.get(user.drinkHabits);
        binding.tvDrink.setText(TextUtils.isEmpty(drink) ? "暂未填写" : drink);
        String deseaseHistory = HealthInfo.getDeseaseHistory(user.deseaseHistory);
        binding.tvDesease.setText(TextUtils.isEmpty(deseaseHistory)
                ? "无" : deseaseHistory.replaceAll(",", "/"));

    }

}
