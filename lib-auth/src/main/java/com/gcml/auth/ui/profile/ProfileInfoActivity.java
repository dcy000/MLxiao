package com.gcml.auth.ui.profile;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnDismissListener;
import com.bigkoo.pickerview.listener.OnOptionsSelectChangeListener;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bigkoo.pickerview.view.TimePickerView;
import com.billy.cc.core.component.CC;
import com.billy.cc.core.component.CCResult;
import com.billy.cc.core.component.IComponentCallback;
import com.gcml.auth.BR;
import com.gcml.auth.R;
import com.gcml.auth.databinding.AuthActivityProfileInfoBinding;
import com.gcml.auth.ui.profile.update.AlertAddressActivity;
import com.gcml.auth.ui.profile.update.AlertIDCardActivity;
import com.gcml.auth.ui.profile.update.AlertMHActivity;
import com.gcml.auth.ui.profile.update.AlertNameActivity;
import com.gcml.common.data.HealthInfo;
import com.gcml.common.data.UserEntity;
import com.gcml.common.mvvm.BaseActivity;
import com.gcml.common.repository.imageloader.ImageLoader;
import com.gcml.common.repository.utils.DefaultObserver;
import com.gcml.common.utils.RxUtils;
import com.gcml.common.utils.Utils;
import com.gcml.common.widget.dialog.LoadingDialog;
import com.gcml.common.widget.dialog.SMSVerificationDialog;
import com.gcml.common.widget.toolbar.ToolBarClickListener;
import com.gcml.lib_utils.display.ToastUtils;
import com.iflytek.synthetize.MLVoiceSynthetize;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

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
        mSexes = Arrays.asList(getResources().getStringArray(R.array.common_sexes));

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
                selectBirthday();
            }
        });
        binding.clItemSex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectSex();
//                startActivity(new Intent(ProfileInfoActivity.this, AlertSexActivity.class));
            }
        });
        binding.clItemHeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(ProfileInfoActivity.this, AlertHeightActivity.class)
//                        .putExtra("data", mUser));
                selectHeight();
            }
        });
        binding.clItemWeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(ProfileInfoActivity.this, AlertWeightActivity.class)
//                        .putExtra("data", mUser));
                selectWeight();
            }
        });
        binding.clItemBlood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(ProfileInfoActivity.this, AlertBloodTypeActivity.class));
                selectBloodType();
            }
        });
        binding.clItemWc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(ProfileInfoActivity.this, AlertBloodTypeActivity.class)
//                .putExtra("data", mUser));
                selectWaist();
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
//                startActivity(new Intent(ProfileInfoActivity.this, AlertSportActivity.class)
//                        .putExtra("data", mUser));
                selectSport();
            }
        });
        binding.clItemSmoke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(ProfileInfoActivity.this, AlertSmokeActivity.class)
//                        .putExtra("data", mUser));
                selectSmoke();
            }
        });
        binding.clItemEat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(ProfileInfoActivity.this, AlertEatingActivity.class)
//                        .putExtra("data", mUser));
                selectEat();
            }
        });
        binding.clItemDrink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(ProfileInfoActivity.this, AlertDrinkingActivity.class)
//                        .putExtra("data", mUser));
                selectDrink();
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


    private void selectBirthday() {
        Calendar selectedDate = Calendar.getInstance();
        Calendar startDate = Calendar.getInstance();
        Calendar endDate = Calendar.getInstance();
        startDate.set(1900, 0, 1);
//        endDate.set(2099, 11, 31);

        OnTimeSelectListener listener = new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                SimpleDateFormat birth = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                String birthString = birth.format(date);
                UserEntity user = new UserEntity();
                user.birthday = birthString;
                updateUser(user);
            }
        };
        TimePickerView pvTime = new TimePickerBuilder(this, listener)
                .setType(new boolean[]{true, true, true, false, false, false})// 默认全部显示
                .setCancelText("取消")
                .setSubmitText("确认")
                .setLineSpacingMultiplier(1.5f)
                .setSubCalSize(30)
                .setContentTextSize(40)
                .setTextColorOut(Color.parseColor("#FF999999"))
                .setTextColorCenter(Color.parseColor("#FF333333"))
                .setSubmitText("确认")
                .setOutSideCancelable(false)
                .setDividerColor(Color.WHITE)
                .isCyclic(true)
                .setSubmitColor(Color.parseColor("#FF108EE9"))
                .setCancelColor(Color.parseColor("#FF999999"))
                .setTitleBgColor(Color.parseColor("#F5F5F5"))
                .setBgColor(Color.WHITE)
                .setDate(selectedDate)
                .setRangDate(startDate, endDate)
                .setLabel("年", "月", "日", "时", "分", "秒")//默认设置为年月日时分秒
                .isCenterLabel(false)
                .build();
        pvTime.show();
    }

    private void selectHeight() {
        OnOptionsSelectListener listener = new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                Timber.i("options1=%s, options2=%s, options3=%s, view =%s", options1, options2, options3, v);
                UserEntity user = new UserEntity();
                String item = getHeights().get(options1);
                user.height = item.replace("cm", "");
                updateUser(user);
            }

        };
        selectItems(getHeights(), listener);
    }

    private void selectWeight() {
        OnOptionsSelectListener listener = new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                Timber.i("options1=%s, options2=%s, options3=%s, view =%s", options1, options2, options3, v);
                UserEntity user = new UserEntity();
                String item = getWeights().get(options1);
                user.weight = item.replace("kg", "");
                updateUser(user);
            }

        };
        selectItems(getWeights(), listener);
    }

    private void selectBloodType() {
        OnOptionsSelectListener listener = new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                Timber.i("options1=%s, options2=%s, options3=%s, view =%s", options1, options2, options3, v);
                UserEntity user = new UserEntity();
                user.bloodType = getBloodTypes().get(options1);
                updateUser(user);
            }

        };
        selectItems(getBloodTypes(), listener);
    }

    private OptionsPickerView<String> mWaistPickerView;

    private void selectWaist() {
        OnOptionsSelectListener listener = new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                Timber.i("options1=%s, options2=%s, options3=%s, view =%s", options1, options2, options3, v);
                UserEntity user = new UserEntity();
                String item = getWaists().get(options1).replace("尺", "");
                user.waist = String.valueOf(Math.floor(Float.valueOf(item) * 33.33f + 0.5f));
                updateUser(user);
            }

        };
        OnDismissListener onDismissListener = new OnDismissListener() {
            @Override
            public void onDismiss(Object o) {
                mWaistPickerView = null;
            }
        };
        OnOptionsSelectChangeListener onSelectChangelistener = new OnOptionsSelectChangeListener() {
            @Override
            public void onOptionsSelectChanged(int options1, int options2, int options3) {
                if (mWaistPickerView != null) {
                    String item = getWaists().get(options1).replace("尺", "");
                    String waist = String.valueOf(Math.floor(Float.valueOf(item) * 33.33f + 0.5f));
                    mWaistPickerView.setTitleText(String.format("约等于%scm", waist));
                }
            }
        };
        mWaistPickerView = new OptionsPickerBuilder(this, listener)
                .setOptionsSelectChangeListener(onSelectChangelistener)
                .setCancelText("取消")
                .setSubmitText("确认")
                .setLineSpacingMultiplier(1.5f)
                .setSubCalSize(30)
                .setContentTextSize(40)
                .setSubmitColor(Color.parseColor("#FF108EE9"))
                .setCancelColor(Color.parseColor("#FF999999"))
                .setTextColorOut(Color.parseColor("#FF999999"))
                .setTextColorCenter(Color.parseColor("#FF333333"))
                .setBgColor(Color.WHITE)
                .setTitleSize(30)
                .setTitleColor(Color.parseColor("#FF333333"))
                .setTitleBgColor(Color.parseColor("#F5F5F5"))
                .setDividerColor(Color.TRANSPARENT)
                .isCenterLabel(false)
                .setOutSideCancelable(false)
                .build();
        mWaistPickerView.setPicker(getWaists());
        mWaistPickerView.setOnDismissListener(onDismissListener);
        mWaistPickerView.show();
    }

    private void selectSport() {
        OnOptionsSelectListener listener = new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                Timber.i("options1=%s, options2=%s, options3=%s, view =%s", options1, options2, options3, v);
                UserEntity user = new UserEntity();
                user.sportsHabits = String.valueOf(options1 + 1);
                updateUser(user);
            }

        };
        selectItems(getSports(), listener);
    }

    private void selectSmoke() {
        OnOptionsSelectListener listener = new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                Timber.i("options1=%s, options2=%s, options3=%s, view =%s", options1, options2, options3, v);
                UserEntity user = new UserEntity();
                user.smokeHabits = String.valueOf(options1 + 1);
                updateUser(user);
            }

        };
        selectItems(getSmokes(), listener);
    }


    private void selectEat() {
        OnOptionsSelectListener listener = new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                Timber.i("options1=%s, options2=%s, options3=%s, view =%s", options1, options2, options3, v);
                UserEntity user = new UserEntity();
                user.eatingHabits = String.valueOf(options1 + 1);
                updateUser(user);
            }

        };
        selectItems(getEats(), listener);
    }

    private void selectDrink() {
        OnOptionsSelectListener listener = new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                Timber.i("options1=%s, options2=%s, options3=%s, view =%s", options1, options2, options3, v);
                UserEntity user = new UserEntity();
                user.drinkHabits = String.valueOf(options1 + 1);
                updateUser(user);
            }

        };
        selectItems(getDrinks(), listener);
    }

    private List<String> mSexes;
    private List<String> mHeights;
    private List<String> mWeights;
    private List<String> mBloodTypes;
    private List<String> mWaists;
    private List<String> mSports;
    private List<String> mSmokes;
    private List<String> mEats;
    private List<String> mDrinks;

    protected List<String> getDrinks() {
        if (mDrinks == null) {
            mDrinks = Arrays.asList(getResources().getStringArray(R.array.common_drinks));
        }
        return mDrinks;
    }

    protected List<String> getEats() {
        if (mEats == null) {
            mEats = Arrays.asList(getResources().getStringArray(R.array.common_eats));
        }
        return mEats;
    }

    protected List<String> getSmokes() {
        if (mSmokes == null) {
            mSmokes = Arrays.asList(getResources().getStringArray(R.array.common_smokes));
        }
        return mSmokes;
    }

    protected List<String> getSports() {
        if (mSports == null) {
            mSports = Arrays.asList(getResources().getStringArray(R.array.common_sports));
        }
        return mSports;
    }

    protected List<String> getWaists() {
        if (mWaists == null) {
            mWaists = new ArrayList<>();
        }
        if (mWaists.isEmpty()) {
            for (float i = 0.1f; i < 5.0f; i = i + 0.1f) {
                mWaists.add(String.format(Locale.getDefault(), "%.1f", i) + "尺");
            }
        }
        return mWaists;
    }

    protected List<String> getBloodTypes() {
        if (mBloodTypes == null) {
            mBloodTypes = Arrays.asList(getResources().getStringArray(R.array.common_blood_types));
        }
        return mBloodTypes;
    }

    protected List<String> getWeights() {
        if (mWeights == null) {
            mWeights = new ArrayList<>();
        }
        if (mWeights.isEmpty()) {
            for (int i = 30; i < 150; i++) {
                mWeights.add(i + "kg");
            }
        }
        return mWeights;
    }

    protected List<String> getHeights() {
        if (mHeights == null) {
            mHeights = new ArrayList<>();
        }
        if (mHeights.isEmpty()) {
            for (int i = 60; i < 260; i++) {
                mHeights.add(i + "cm");
            }
        }
        return mHeights;
    }

    protected List<String> getSexes() {
        if (mSexes == null) {
            mSexes = Arrays.asList(getResources().getStringArray(R.array.common_sexes));
        }
        return mSexes;
    }

    private void selectSex() {
        OnOptionsSelectListener listener = new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                Timber.i("options1=%s, options2=%s, options3=%s, view =%s", options1, options2, options3, v);
                UserEntity user = new UserEntity();
                user.sex = getSexes().get(options1);
                updateUser(user);
            }

        };
        selectItems(getSexes(), listener);
    }

    private void updateUser(UserEntity user) {
        viewModel.updateUser(user)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        showLoading("修改中...");
                    }
                })
                .doOnTerminate(new Action() {
                    @Override
                    public void run() throws Exception {
                        dismissLoading();
                    }
                })
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new DefaultObserver<UserEntity>() {
                    @Override
                    public void onNext(UserEntity o) {
                        showUser(o);
                        speak("修改成功");
                        ToastUtils.showShort("修改成功");
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        ToastUtils.showShort("修改失败");
                        speak("修改失败");
                    }
                });
    }

    private void selectItems(List<String> items, OnOptionsSelectListener listener) {
        OptionsPickerView<String> pickerView = new OptionsPickerBuilder(this, listener)
                .setCancelText("取消")
                .setSubmitText("确认")
                .setLineSpacingMultiplier(1.5f)
                .setSubCalSize(30)
                .setContentTextSize(40)
                .setSubmitColor(Color.parseColor("#FF108EE9"))
                .setCancelColor(Color.parseColor("#FF999999"))
                .setTextColorOut(Color.parseColor("#FF999999"))
                .setTextColorCenter(Color.parseColor("#FF333333"))
                .setBgColor(Color.WHITE)
                .setTitleBgColor(Color.parseColor("#F5F5F5"))
                .setDividerColor(Color.TRANSPARENT)
                .isCenterLabel(false)
                .setOutSideCancelable(false)
                .build();
        pickerView.setPicker(items);
        pickerView.show();
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
        if (!TextUtils.isEmpty(user.birthday)) {
            binding.tvAge.setText(String.format(Locale.getDefault(), "%d岁", Utils.ageByBirthday(user.birthday)));
        } else if (!TextUtils.isEmpty(user.idCard)
                && user.idCard.length() == 18) {
            binding.tvAge.setText(String.format(Locale.getDefault(), "%d岁", Utils.age(user.idCard)));
        } else {
            binding.tvAge.setText(TextUtils.isEmpty(user.age) ? "暂未填写" : user.age + "岁");
        }
        binding.tvSex.setText(TextUtils.isEmpty(user.sex) ? "暂未填写" : user.sex);
        binding.tvHeight.setText(TextUtils.isEmpty(user.height) ? "暂未填写" : user.height + "cm");
        binding.tvWeight.setText(TextUtils.isEmpty(user.weight) ? "暂未填写" : user.weight + "kg");
        binding.tvBlood.setText(TextUtils.isEmpty(user.bloodType) ? "暂未填写" : user.bloodType + "型");
        binding.tvWc.setText(TextUtils.isEmpty(user.waist) ? "暂未填写" : user.waist + "cm");

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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dismissLoading();
    }

    private LoadingDialog mLoadingDialog;

    private void showLoading(String tips) {
        if (mLoadingDialog != null) {
            LoadingDialog loadingDialog = mLoadingDialog;
            mLoadingDialog = null;
            loadingDialog.dismiss();
        }
        mLoadingDialog = new LoadingDialog.Builder(this)
                .setIconType(LoadingDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord(tips)
                .create();
        mLoadingDialog.show();
    }

    private void dismissLoading() {
        if (mLoadingDialog != null) {
            LoadingDialog loadingDialog = mLoadingDialog;
            mLoadingDialog = null;
            loadingDialog.dismiss();
        }
    }

    private void speak(String text) {
        MLVoiceSynthetize.startSynthesize(this, text, false);
    }
}
