package com.gcml.auth.ui.profile;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnDismissListener;
import com.bigkoo.pickerview.listener.OnOptionsSelectChangeListener;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.gcml.auth.BR;
import com.gcml.auth.R;
import com.gcml.auth.databinding.AuthActivityProfile2Binding;
import com.gcml.common.data.UserEntity;
import com.gcml.common.mvvm.BaseActivity;
import com.gcml.common.router.AppRouter;
import com.gcml.common.utils.DefaultObserver;
import com.gcml.common.utils.RxUtils;
import com.gcml.common.utils.display.ToastUtils;
import com.gcml.common.widget.dialog.AlertDialog;
import com.gcml.common.widget.dialog.LoadingDialog;
import com.gcml.common.widget.toolbar.ToolBarClickListener;
import com.iflytek.synthetize.MLVoiceSynthetize;
import com.sjtu.yifei.annotation.Route;
import com.sjtu.yifei.route.Routerfit;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

@Route(path = "/auth/profile2/activity")
public class Profile2Activity extends BaseActivity<AuthActivityProfile2Binding, Profile2ViewModel> {

    @Override
    protected int layoutId() {
        return R.layout.auth_activity_profile2;
    }

    @Override
    protected int variableId() {
        return BR.viewModel;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        callId = getIntent().getStringExtra("callId");
        binding.setPresenter(this);
        binding.tbSimpleProfile.setData(
                "完 善 信 息（2／2）",
                0, null,
                R.drawable.common_icon_home, null,
                barClickListener);
//        binding.spHeight.setAdapter(new ArrayAdapter<String>(
//                this,
//                R.layout.common_item_spinner,
//                getHeights()
//        ));
//        binding.spHeight.setSelection(148);
//        binding.spWc.setAdapter(new ArrayAdapter<String>(
//                this,
//                R.layout.common_item_spinner,
//                getWaists()
//        ));
//        binding.spWc.setSelection(10);
//        binding.spWeight.setAdapter(new ArrayAdapter<String>(
//                this,
//                R.layout.common_item_spinner,
//                getWeights()
//        ));
//        binding.spWeight.setSelection(50);

        binding.spHeight.setText("160cm");
        binding.spHeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectHeight();
            }
        });
        binding.spWc.setText("2尺");
        binding.spWc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectWaist();
            }
        });
        binding.spWeight.setText("50kg");
        binding.spWeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectWeight();
            }
        });
    }

    private ToolBarClickListener barClickListener = new ToolBarClickListener() {
        @Override
        public void onLeftClick() {
            finish();
        }

        @Override
        public void onRightClick() {
            new AlertDialog(Profile2Activity.this).builder()
                    .setMsg("您正在完善信息，是否要离开当前页面？")
                    .setNegativeButton("完善信息", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    })
                    .setPositiveButton("确认离开", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Routerfit.register(AppRouter.class).skipMainActivity();
                            finish();
                        }
                    }).show();
        }
    };

    private void selectHeight() {
        OnOptionsSelectListener listener = new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                Timber.i("options1=%s, options2=%s, options3=%s, view =%s", options1, options2, options3, v);
                UserEntity user = new UserEntity();
                String item = getHeights().get(options1);
                binding.spHeight.setText(item);
                user.height = item.replace("cm", "");

            }

        };
        selectItems(getHeights(), listener, true);
    }

    private void selectWeight() {
        OnOptionsSelectListener listener = new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                Timber.i("options1=%s, options2=%s, options3=%s, view =%s", options1, options2, options3, v);
                String item = getWeights().get(options1);
                binding.spWeight.setText(item);
            }

        };
        selectItems(getWeights(), listener, false);
    }

    private void selectItems(List<String> items, OnOptionsSelectListener listener, boolean isHeight) {
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
        if (isHeight) {
            pickerView.setSelectOptions(100);
        } else {
            pickerView.setSelectOptions(20);
        }
        pickerView.show();
    }

    private OptionsPickerView<String> mWaistPickerView;

    private void selectWaist() {
        OnOptionsSelectListener listener = new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                Timber.i("options1=%s, options2=%s, options3=%s, view =%s", options1, options2, options3, v);
                binding.spWc.setText(getWaists().get(options1));
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
        mWaistPickerView.setSelectOptions(19);
        mWaistPickerView.setOnDismissListener(onDismissListener);
        mWaistPickerView.show();
    }

    private List<String> heights = new ArrayList<>();
    private List<String> waists = new ArrayList<>();
    private List<String> weights = new ArrayList<>();

    public List<String> getHeights() {
        if (heights.isEmpty()) {
            for (int i = 60; i < 260; i++) {
                heights.add(i + "cm");
            }
        }
        return heights;
    }

    public List<String> getWaists() {
        if (waists.isEmpty()) {
            for (float i = 0.1f; i < 5.0f; i = i + 0.1f) {
                waists.add(String.format(Locale.getDefault(), "%.1f", i) + "尺");
            }
        }
        return waists;
    }

    public List<String> getWeights() {
        if (weights.isEmpty()) {
            for (int i = 30; i < 150; i++) {
                weights.add(i + "kg");
            }
        }
        return weights;
    }

    public void goNext() {

//        int index = binding.spHeight.getSelectedItemPosition();
//        String height = getHeights().get(index);
//        if (!TextUtils.isEmpty(height)) {
//            height = height.replaceAll("cm", "");
//        }
//
//        index = binding.spWc.getSelectedItemPosition();
//        String waist = getWaists().get(index);
//        if (!TextUtils.isEmpty(waist)) {
//            waist = waist.replaceAll("cm", "");
//        }
//
//        index = binding.spWeight.getSelectedItemPosition();
//        String weight = getWeights().get(index);
//        if (!TextUtils.isEmpty(weight)) {
//            weight = weight.replaceAll("kg", "");
//        }

        String height = binding.spHeight.getText().toString().replace("cm", "");
        String weight = binding.spWeight.getText().toString().replace("kg", "");

        String item = binding.spWc.getText().toString().replace("尺", "");
        String waist = String.valueOf(Math.floor(Float.valueOf(item) * 33.33f + 0.5f));

        UserEntity user = new UserEntity();
        user.height = height;
        user.waist = waist;
        user.weight = weight;

        viewModel.updateProfile(user)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        showLoading("正在加载...");
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
                    public void onNext(UserEntity user) {
                        ToastUtils.showShort("更新资料成功");
                        error = false;
                        finish();
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        ToastUtils.showShort(throwable.getMessage());
                    }
                });

    }

    @Override
    protected void onPause() {
        super.onPause();
        dismissLoading();
        MLVoiceSynthetize.stop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MLVoiceSynthetize.startSynthesize(getApplicationContext(),
                "请完善您的个人信息");
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

    @Override
    public void finish() {
        if (error) {
            Routerfit.setResult(Activity.RESULT_OK, "failed");
        } else {
            Routerfit.setResult(Activity.RESULT_OK, "success");
        }
//        if (!TextUtils.isEmpty(callId)) {
//            CCResult result;
//            if (error) {
//                result = CCResult.error("");
//            } else {
//                result = CCResult.success();
//            }
//            //为确保不管登录成功与否都会调用CC.sendCCResult，在onDestroy方法中调用
//            CC.sendCCResult(callId, result);
//        }
        super.finish();
    }

    private String callId;
    private volatile boolean error = true;
}
