package com.gcml.auth.ui.profile;

import android.os.Bundle;
import android.text.TextUtils;

import com.billy.cc.core.component.CC;
import com.gcml.auth.R;
import com.gcml.auth.BR;
import com.gcml.auth.databinding.AuthActivitySimpleProfileBinding;
import com.gcml.common.data.UserEntity;
import com.gcml.common.mvvm.BaseActivity;
import com.gcml.common.repository.utils.DefaultObserver;
import com.gcml.common.utils.RxUtils;
import com.gcml.common.utils.Utils;
import com.gcml.common.widget.dialog.LoadingDialog;
import com.gcml.common.widget.toolbar.ToolBarClickListener;
import com.gcml.lib_utils.display.ToastUtils;
import com.iflytek.synthetize.MLVoiceSynthetize;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class SimpleProfileActivity extends BaseActivity<AuthActivitySimpleProfileBinding, SimpleProfileViewModel> {

    @Override
    protected int layoutId() {
        return R.layout.auth_activity_simple_profile;
    }

    @Override
    protected int variableId() {
        return BR.viewModel;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        binding.setPresenter(this);
        binding.tbSimpleProfile.setData(
                "完 善 信 息",
                R.drawable.common_icon_back, "返回",
                R.drawable.common_icon_home, null,
                barClickListener);
        binding.tvMan.setSelected(manSelected);
        binding.tvWomen.setSelected(!manSelected);
        binding.spHeight.attachDataSource(getHeights());
        binding.spHeight.setSelectedIndex(148);
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
            CC.obtainBuilder("com.gcml.old.home")
                    .build()
                    .callAsync();
            finish();
        }
    };

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
        String name = binding.etName.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            MLVoiceSynthetize.startSynthesize(
                    getApplicationContext(),
                    getString(R.string.auth_empty_name),
                    true);
            ToastUtils.showShort(R.string.auth_empty_name);
            return;
        }

        String idCard = binding.etIdCard.getText().toString().trim();
        if (!Utils.checkIdCard1(idCard)) {
            MLVoiceSynthetize.startSynthesize(
                    getApplicationContext(),
                    getString(R.string.auth_id_card_tip),
                    true);
            ToastUtils.showShort(R.string.auth_id_card_tip);
            return;
        }

        String sex = manSelected ? "男" : "女";

        int index = binding.spHeight.getSelectedIndex();
        String height = getHeights().get(index);
        if (!TextUtils.isEmpty(height)) {
            height = height.replaceAll("cm", "");
        }

        UserEntity user = new UserEntity();
        user.name = name;
        user.idCard = idCard;
        user.sex = sex;
        user.height = height;

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
                .subscribe(new DefaultObserver<Object>() {
                    @Override
                    public void onNext(Object o) {
                        ToastUtils.showShort("更新资料成功");
                        CC.obtainBuilder("com.gcml.old.home")
                                .build()
                                .callAsync();
                        finish();
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        ToastUtils.showShort("更新资料失败");
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
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
}