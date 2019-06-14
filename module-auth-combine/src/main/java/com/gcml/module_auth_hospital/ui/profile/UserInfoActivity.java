package com.gcml.module_auth_hospital.ui.profile;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bigkoo.pickerview.view.TimePickerView;
import com.gcml.common.LazyFragment;
import com.gcml.common.data.UserEntity;
import com.gcml.common.data.UserSpHelper;
import com.gcml.common.imageloader.ImageLoader;
import com.gcml.common.router.AppRouter;
import com.gcml.common.utils.DefaultObserver;
import com.gcml.common.utils.RxUtils;
import com.gcml.common.utils.Utils;
import com.gcml.common.utils.base.ToolbarBaseActivity;
import com.gcml.common.utils.display.ToastUtils;
import com.gcml.common.widget.dialog.SMSVerificationDialog;
import com.gcml.common.widget.fdialog.BaseNiceDialog;
import com.gcml.common.widget.fdialog.NiceDialog;
import com.gcml.common.widget.fdialog.ViewConvertListener;
import com.gcml.common.widget.fdialog.ViewHolder;
import com.gcml.module_auth_hospital.R;
import com.gcml.module_auth_hospital.ui.profile.update.AlertAddressActivity;
import com.gcml.module_auth_hospital.ui.profile.update.AlertIDCardActivity;
import com.gcml.module_auth_hospital.ui.profile.update.AlertNameActivity;
import com.gcml.module_auth_hospital.ui.register.BindPhoneActivity;
import com.iflytek.synthetize.MLVoiceSynthetize;
import com.sjtu.yifei.annotation.Route;
import com.sjtu.yifei.route.ActivityCallback;
import com.sjtu.yifei.route.Routerfit;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

@Route(path = "/auth/hospital/user/info/activity")
public class UserInfoActivity extends ToolbarBaseActivity {

    private RadioGroup rgTabs;
    private ImageView ivAvatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        mToolbar.setVisibility(View.VISIBLE);
        mTitleText.setText("个  人  信  息");


        ivAvatar = findViewById(R.id.ivAvatar);
        ivAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserEntity user = mUser;
                if (user == null
                        || TextUtils.isEmpty(user.phone)
                        || !TextUtils.isDigitsOnly(user.phone)
                        || user.phone.length() != 11) {
                    //身份证注册的，是没有手机号码的，所以只能验证身份证号码
                    if (!TextUtils.isEmpty(user.idCard)) {
                        showVertifyIdCardDialog(user);
                        return;
                    }
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

        rgTabs = findViewById(R.id.rgTabs);


        rgTabs.setOnCheckedChangeListener(onCheckedChangeListener);
        rgTabs.check(R.id.rbUserBaseInfo);
        showFragment(0);


    }

    private void showVertifyIdCardDialog(UserEntity user) {
        NiceDialog.init()
                .setLayoutId(R.layout.auth_n_dialog_vertify_idcard)
                .setConvertListener(new ViewConvertListener() {
                    @Override
                    protected void convertView(ViewHolder viewHolder, BaseNiceDialog baseNiceDialog) {
                        //取消按钮
                        viewHolder.getView(R.id.iv_close).setOnClickListener(v1 ->
                                baseNiceDialog.dismiss());

                        //下一步
                        viewHolder.getView(R.id.tv_next).setOnClickListener(v12 -> {
                            EditText idCard = (EditText) viewHolder.getView(R.id.et_code);
                            String idCardString = idCard.getText().toString().trim();
                            if (TextUtils.isEmpty(idCardString) || !user.idCard.equals(idCardString)) {
                                ToastUtils.showShort("身份证号码不正确");
                                return;
                            }
                            baseNiceDialog.dismiss();
                            modifyHead();
                        });

                    }
                })
                .show(getSupportFragmentManager());
    }

    private void modifyHead() {
        Routerfit.register(AppRouter.class)
                .skipFaceBd3SignUpActivity(UserSpHelper.getUserId(), new ActivityCallback() {
                    @Override
                    public void onActivityResult(int result, Object data) {
                        if (result == Activity.RESULT_OK) {
                            String sResult = data.toString();
                            if (TextUtils.isEmpty(sResult)) return;
                            if (sResult.equals("success")) {
                                ToastUtils.showShort("更换人脸成功");
                            } else if (sResult.equals("failed")) {
                                ToastUtils.showShort("更换人脸失败");
                            }
                        }
                    }
                });
    }

    public void updateName() {
        if (mUser == null) {
            ToastUtils.showShort("请重新登陆");
            return;
        }
        startActivity(new Intent(this, AlertNameActivity.class)
                .putExtra("data", mUser));
    }

    public void selectAge() {
        Calendar birthDate = getBirtHCalendar(mUser);
        Calendar startDate = Calendar.getInstance();
        Calendar endDate = Calendar.getInstance();
        startDate.set(1900, 0, 1);

        OnTimeSelectListener listener = new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                SimpleDateFormat birth = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
                String birthString = birth.format(date);
//                UserEntity user = new UserEntity();
                if (mUser != null) {
                    mUser.age = String.valueOf(Utils.ageByBirthday(birthString));
                }
                updateUser(mUser);
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
                .setDate(birthDate)
                .setRangDate(startDate, endDate)
                .setLabel("年", "月", "日", "时", "分", "秒")//默认设置为年月日时分秒
                .isCenterLabel(false)
                .build();
        pvTime.show();
    }

    private Calendar getBirtHCalendar(UserEntity birth) {
        if (birth == null) {
            return Calendar.getInstance();
        }

        if (TextUtils.isEmpty(birth.birthday)) {
            return Calendar.getInstance();
        }

        String pattern = "yyyyMMdd";
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        try {
            Date date = dateFormat.parse(birth.birthday);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            return calendar;
        } catch (ParseException e) {
            e.printStackTrace();
            return Calendar.getInstance();
        }
    }

    public void selectBirthday() {
        Calendar selectedDate = getBirtHCalendar(mUser);
        Calendar startDate = Calendar.getInstance();
        Calendar endDate = Calendar.getInstance();
        startDate.set(1900, 0, 1);
//        endDate.set(2099, 11, 31);

        OnTimeSelectListener listener = new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                SimpleDateFormat birth = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
                String birthString = birth.format(date);
//                UserEntity user = new UserEntity();
                if (mUser != null) {
                    mUser.birthday = birthString;
                }
                updateUser(mUser);
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

    public void selectSex() {
        OnOptionsSelectListener listener = new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                Timber.i("options1=%s, options2=%s, options3=%s, view =%s", options1, options2, options3, v);
//                UserEntity user = new UserEntity();
                if (mUser != null) {
                    mUser.sex = getSexes().get(options1);
                }
                updateUser(mUser);
            }

        };
        selectItems(getSexes(), listener);
    }

    public void selectHeight() {
        OnOptionsSelectListener listener = new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                Timber.i("options1=%s, options2=%s, options3=%s, view =%s", options1, options2, options3, v);
//                UserEntity user = new UserEntity();
                String item = getHeights().get(options1);
                if (mUser != null) {
                    mUser.height = item.replace("cm", "");
                }
                updateUser(mUser);
            }

        };
        selectItemsWithDefIndex(getHeights(), listener, 110);
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

    /**
     * 默认选项
     *
     * @param items
     * @param listener
     * @param defIndex
     */
    private void selectItemsWithDefIndex(List<String> items, OnOptionsSelectListener listener, int defIndex) {
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
        pickerView.setSelectOptions(defIndex);//设置默认选中
        pickerView.show();
    }


    public void selectWeight() {
        OnOptionsSelectListener listener = new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                Timber.i("options1=%s, options2=%s, options3=%s, view =%s", options1, options2, options3, v);
//                UserEntity user = new UserEntity();
                String item = getWeights().get(options1);
                if (mUser != null) {
                    mUser.weight = item.replace("kg", "");
                }
                updateUser(mUser);
            }

        };
        selectItemsWithDefIndex(getWeights(), listener, 20);
    }

    public void selectBloodType() {
        OnOptionsSelectListener listener = new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                Timber.i("options1=%s, options2=%s, options3=%s, view =%s", options1, options2, options3, v);
//                UserEntity user = new UserEntity();

                if (mUser != null) {
                    mUser.bloodType = getBloodTypes().get(options1);
                }

                updateUser(mUser);
            }

        };
        selectItems(getBloodTypes(), listener);
    }

    public void updateIdCard() {
        if (mUser == null) {
            ToastUtils.showShort("请重新登陆");
            return;
        }
        startActivity(new Intent(this, AlertIDCardActivity.class)
                .putExtra("data", mUser));
    }

    public void updateAddress() {
        if (mUser == null) {
            ToastUtils.showShort("请重新登陆");
            return;
        }
        startActivity(new Intent(this, AlertAddressActivity.class)
                .putExtra("data", mUser));
    }

    public void updatePhone() {
        startActivity(new Intent(this, BindPhoneActivity.class)
                .putExtra("fromWhere", "updatePhone")
                .putExtra("data", mUser)
        );
    }

    private RadioGroup.OnCheckedChangeListener onCheckedChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            if (checkedId == R.id.rbUserBaseInfo) {
                showFragment(0);
            } else if (checkedId == R.id.rbAccountSettings) {
                showFragment(1);
            }
        }
    };

    private int i = 0;

    private LazyFragment userInfoBaseFragment;
    private LazyFragment userInfoAccountFragment;

    private void showFragment(int i) {
        this.i = i;
        FragmentManager fm = getSupportFragmentManager();
        String tag = null;
        LazyFragment fragmentToShow = null;
        LazyFragment lastFragment = null;
        if (i == 0) {
            tag = UserInfoBaseFragment.class.getName();
            if (userInfoBaseFragment == null) {
                fragmentToShow = (LazyFragment) fm.findFragmentByTag(tag);
                if (fragmentToShow == null) {
                    fragmentToShow = new UserInfoBaseFragment();
                }
                userInfoBaseFragment = fragmentToShow;
            } else {
                fragmentToShow = userInfoBaseFragment;
            }
            lastFragment = userInfoAccountFragment;
        } else if (i == 1) {
            tag = UserInfoAccountFragment.class.getName();
            if (userInfoAccountFragment == null) {
                fragmentToShow = (LazyFragment) fm.findFragmentByTag(tag);
                if (fragmentToShow == null) {
                    fragmentToShow = new UserInfoAccountFragment();
                }
                userInfoAccountFragment = fragmentToShow;
            } else {
                fragmentToShow = userInfoAccountFragment;
            }
            lastFragment = userInfoBaseFragment;
        }

        if (fragmentToShow != null) {
            FragmentTransaction transaction = fm.beginTransaction();
            if (lastFragment != null && lastFragment.isAdded()) {
                transaction.hide(lastFragment);
            }
            if (fragmentToShow.isAdded()) {
                transaction.show(fragmentToShow);
            } else {
                transaction.add(R.id.flContainer, fragmentToShow, tag);
            }
            transaction.commitAllowingStateLoss();
            fm.executePendingTransactions();
        }
    }

    private UserEntity mUser;

    public UserEntity getUser() {
        return mUser;
    }

    public void showUser(UserEntity user) {
        mUser = user;
        if (user != null) {
            ImageLoader.with(this)
                    .load(user.avatar)
                    .circle()
                    .placeholder(R.drawable.avatar_placeholder)
                    .error(R.drawable.avatar_placeholder)
                    .into(ivAvatar);
        }
        if (userInfoAccountFragment != null) {
            ((UserInfoAccountFragment) userInfoAccountFragment).showUser(user);
        }

        if (userInfoBaseFragment != null) {
            ((UserInfoBaseFragment) userInfoBaseFragment).showUser(user);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }

    private void getData() {
        Routerfit.register(AppRouter.class)
                .getUserProvider()
                .getUserEntity()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        showLoading("加载中...");
                    }
                })
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        dismissLoading();
                    }
                })
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

    private void updateUser(UserEntity user) {
        if (mUser == null) {
            ToastUtils.showShort("请重新登陆");
            return;
        }
        Routerfit.register(AppRouter.class)
                .getUserProvider()
                .updateUserEntity(user)
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

    private void speak(String text) {
        MLVoiceSynthetize.startSynthesize(this, text, false);
    }


    private List<String> mSexes;
    private List<String> mHeights;
    private List<String> mWeights;
    private List<String> mBloodTypes;

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


}
