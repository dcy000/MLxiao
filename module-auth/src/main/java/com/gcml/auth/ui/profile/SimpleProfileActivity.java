package com.gcml.auth.ui.profile;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.gcml.auth.R;
import com.gcml.auth.databinding.AuthActivitySimpleProfileBinding;
import com.gcml.common.data.UserEntity;
import com.gcml.common.mvvm.BaseActivity;
import com.gcml.common.router.AppRouter;
import com.gcml.common.utils.DefaultObserver;
import com.gcml.common.utils.RxUtils;
import com.gcml.common.utils.Utils;
import com.gcml.common.utils.display.KeyboardUtils;
import com.gcml.common.utils.display.ToastUtils;
import com.gcml.common.widget.dialog.AlertDialog;
import com.gcml.common.widget.dialog.LoadingDialog;
import com.gcml.common.widget.toolbar.ToolBarClickListener;
import com.iflytek.synthetize.MLVoiceSynthetize;
import com.sjtu.yifei.annotation.Route;
import com.sjtu.yifei.route.Routerfit;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
@Route(path = "/auth/simple/profile/activity")
public class SimpleProfileActivity extends BaseActivity<AuthActivitySimpleProfileBinding, SimpleProfileViewModel> {

    private String signUpType;
    private String signUpIdCard;
    private String name;
    private String idCard;

    @Override
    protected int layoutId() {
        return R.layout.auth_activity_simple_profile;
    }

    protected void init(Bundle savedInstanceState) {
        if (getIntent() != null) {
            signUpType = getIntent().getStringExtra("signUpType");
            if (TextUtils.equals("idcard", signUpType)) {
                binding.tvName.setVisibility(View.GONE);
                binding.etName.setVisibility(View.GONE);
                binding.tvIdCard.setVisibility(View.GONE);
                binding.etIdCard.setVisibility(View.GONE);
            }
        }
        callId = getIntent().getStringExtra("callId");
        binding.setPresenter(this);
        binding.setViewModel(viewModel);
        binding.tbSimpleProfile.setData(
                "完 善 信 息（1／2）",
                0, null,
                R.drawable.common_icon_home, null,
                barClickListener);
        binding.tvMan.setSelected(manSelected);
        binding.tvWomen.setSelected(!manSelected);
//        binding.spHeight.setAdapter(new ArrayAdapter<String>(
//                this,
//                R.layout.common_item_spinner,
//                getHeights()
//        ));
//        binding.spHeight.setSelection(148);
    }

    private List<String> heights = new ArrayList<>();

    public List<String> getHeights() {
        if (heights.isEmpty()) {
            for (int i = 30; i < 260; i++) {
                heights.add(i + "cm");
            }
        }
        return heights;
    }

    private ToolBarClickListener barClickListener = new ToolBarClickListener() {
        @Override
        public void onLeftClick() {
            finish();
        }

        @Override
        public void onRightClick() {
            new AlertDialog(SimpleProfileActivity.this).builder()
                    .setMsg("您正在完善信息，是否要离开当前页面？")
                    .setPositiveButton("完善信息", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    })
                    .setNegativeButton("确认离开", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Routerfit.register(AppRouter.class).skipMainActivity();
                            finish();
                        }
                    }).show();
        }
    };

    public void rootOnClick() {
        View view = getCurrentFocus();
        if (view != null) {
            KeyboardUtils.hideKeyboard(this, view);
        }
    }


    private boolean manSelected = true;

    public void selectMan() {
        if (manSelected) {
            return;
        }
        manSelected = true;
        binding.tvMan.setSelected(manSelected);
        binding.tvWomen.setSelected(!manSelected);
    }

    public void selectWoman() {
        if (!manSelected) {
            return;
        }
        manSelected = false;
        binding.tvMan.setSelected(manSelected);
        binding.tvWomen.setSelected(!manSelected);
    }

    public void goNext() {
        if (!TextUtils.equals("idcard", signUpType)) {
            name = binding.etName.getText().toString().trim();
            if (TextUtils.isEmpty(name)) {
                MLVoiceSynthetize.startSynthesize(
                        getApplicationContext(),
                        getString(R.string.auth_empty_name),
                        false);
                ToastUtils.showShort(R.string.auth_empty_name);
                return;
            }

            idCard = binding.etIdCard.getText().toString().trim();
            if (!Utils.checkIdCard1(idCard)) {
                MLVoiceSynthetize.startSynthesize(
                        getApplicationContext(),
                        getString(R.string.auth_id_card_tip),
                        false);
                ToastUtils.showShort(R.string.auth_id_card_tip);
                return;
            }
        }
        String sex = manSelected ? "男" : "女";

//        int index = binding.spHeight.getSelectedItemPosition();
//        String height = getHeights().get(index);
//        if (!TextUtils.isEmpty(height)) {
//            height = height.replaceAll("cm", "");
//        }

        UserEntity user = new UserEntity();
        user.name = name;
        user.sex = sex;
        if (getIntent() != null) {
            signUpIdCard = getIntent().getStringExtra("signUpIdCard");
            if (TextUtils.equals("idcard", signUpType)) {
                user.idCard = signUpIdCard;
                putUserInfo(user);
            } else {
                user.idCard = idCard;
                checkIdCard(user);
            }
        }

    }

    private void putUserInfo(UserEntity user) {
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

    private void checkIdCard(final UserEntity user) {
        Routerfit.register(AppRouter.class)
                .getBusinessControllerProvider()
                .isIdCardNotExit(user.idCard)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new DefaultObserver<Object>() {
                    @Override
                    public void onNext(Object o) {
                        putUserInfo(user);
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
        if (error){
            Routerfit.setResult(Activity.RESULT_OK,"failed");
        }else{
            Routerfit.setResult(Activity.RESULT_OK,"success");
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