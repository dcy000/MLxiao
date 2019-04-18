package com.gcml.common.base;

import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.ImageView;

import com.gcml.common.business.R;
import com.gcml.common.utils.DefaultObserver;
import com.gcml.common.utils.RxUtils;
import com.gcml.common.utils.display.ToastUtils;
import com.gcml.common.widget.dialog.LoadingDialog;
import com.gcml.common.widget.toolbar.TranslucentToolBar;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by lenovo on 2019/1/18.
 */

public class BaseActivity extends AppCompatActivity {
    private PermissionDialog dialog;

    public interface IAction {
        void action();
    }

    public void onRightClickWithPermission(IAction action) {
        dialog = new PermissionDialog();
        dialog.setOnClickListener(passWord -> {
            if (TextUtils.equals("123456", passWord)) {
                if (action != null) {
                    action.action();
                }
                dialog.dismiss();
            } else {
                ToastUtils.showShort(getString(R.string.business_perimission_password_error));
            }
//            validata(passWord, action);
        });
        if (dialog.isAdded()) {
//            dialog.dismiss();
        } else {
            dialog.show(getFragmentManager(), "permission");
        }
    }

    ValidateAdminRepository adminRepository = new ValidateAdminRepository();

    private void validata(String passWord, IAction action) {
        adminRepository.validateAdmin(passWord)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        showLoading("");
                    }
                })
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new DefaultObserver<Object>() {
                    @Override
                    public void onNext(Object o) {
                        dismissLoading();
                        if (action != null) {
                            action.action();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtils.showShort(e.getMessage());
                        dismissLoading();
                    }
                });
    }

    public void onLeftClick() {
        finish();
    }

    public int leftRes() {
        return R.drawable.common_btn_back;
    }

    public int rightRes() {
        return R.drawable.common_ic_wifi_state;
    }

    public String leftString() {
        return "返回";
    }

    public String rightString() {
        return "";
    }

    protected LoadingDialog mLoadingDialog;

    protected void showLoading(String tips) {
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

    protected void dismissLoading() {
        if (mLoadingDialog != null) {
            LoadingDialog loadingDialog = mLoadingDialog;
            mLoadingDialog = null;
            loadingDialog.dismiss();
        }
    }

    protected void setWifiLevel(TranslucentToolBar view) {
        RxUtils.rxWifiLevel(getApplication(), 4)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer level) throws Exception {
                        view.setImageLevel(level);
                    }
                });
    }

    protected void setWifiLevel(ImageView view) {
        RxUtils.rxWifiLevel(getApplication(), 4)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer level) throws Exception {
                        view.setImageLevel(level);
                    }
                });
    }


}
