package com.gcml.module_inquiry.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.gcml.common.data.UserSpHelper;
import com.gcml.common.router.AppRouter;
import com.gcml.common.utils.DefaultObserver;
import com.gcml.common.utils.RxUtils;
import com.gcml.common.utils.base.ToolbarBaseActivity;
import com.gcml.common.utils.display.ToastUtils;
import com.gcml.common.widget.dialog.LoadingDialog;
import com.gcml.common.widget.toolbar.ToolBarClickListener;
import com.gcml.common.widget.toolbar.TranslucentToolBar;
import com.gcml.module_inquiry.R;
import com.gcml.module_inquiry.dialog.AffirmSignatureDialog;
import com.gcml.module_inquiry.model.HealthFileRepostory;
import com.gcml.module_inquiry.wrap.PainterView;
import com.sjtu.yifei.route.Routerfit;

import java.io.ByteArrayOutputStream;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by lenovo on 2019/1/17.
 */

public class UserSignActivity extends ToolbarBaseActivity implements AffirmSignatureDialog.ClickListener {
    private TextView tvSignatrueConfirm;
    private PainterView signature;
    private AffirmSignatureDialog signatureDialog;
    private byte[] bytes;
    private TextView cancel;
    TranslucentToolBar tb;
    private LoadingDialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_sign);
        mToolbar.setVisibility(View.GONE);
        initTitle();
        initView();
//        ActivityHelper.addActivity(this);
    }

    private void initTitle() {
        tb = findViewById(R.id.tb_user_sign);
        tb.setData("确 认 签 名",
                R.drawable.common_btn_back, "返回",
                R.drawable.common_ic_wifi_state, null, new ToolBarClickListener() {
                    @Override
                    public void onLeftClick() {
                        finish();
                    }

                    @Override
                    public void onRightClick() {
                        Routerfit.register(AppRouter.class).skipMainActivity();
//                        onRightClickWithPermission(new IAction() {
//                            @Override
//                            public void action() {
//                            }
//                        });
                    }

                });
        setWifiLevel(tb);
    }

    private void toUserInfo() {
        startActivity(new Intent(UserSignActivity.this, UserInfoListActivity.class));
    }

    private void initView() {
        tvSignatrueConfirm = (TextView) findViewById(R.id.tv_signatrue_confirm);
        signature = (PainterView) findViewById(R.id.signature);
        cancel = findViewById(R.id.tv_signatrue_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signature.clear();
            }
        });
        tvSignatrueConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap bitmap = signature.creatBitmap();
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 60, stream);
                bytes = stream.toByteArray();

                if (signatureDialog == null) {
                    signatureDialog = new AffirmSignatureDialog();
                }
                Bundle args = new Bundle();
                args.putByteArray("imageData", bytes);
                signatureDialog.setArguments(args);

                signatureDialog.setListener(UserSignActivity.this);
                signatureDialog.show(getFragmentManager(), "signature");

            }
        });
    }

    @Override
    public void onConfirm() {
        //取消
        signature.clear();
    }

    @Override
    public void onCancel() {
        //确认
        uploadHeadToSelf(bytes);
    }

    HealthFileRepostory fileRepostory = new HealthFileRepostory();

    private void uploadHeadToSelf(final byte[] faceData) {
        if (faceData == null) {
            return;
        }

        Intent intent = getIntent();
        if (intent == null) {
            return;
        }

        String doid = getIntent().getStringExtra("doid");
        if (TextUtils.isEmpty(doid)) {
            return;
        }

        dialog = new LoadingDialog.Builder(this)
                .setIconType(LoadingDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord("")
                .create();

        fileRepostory.uploadHeadData(faceData, UserSpHelper.getUserId())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        if (!disposable.isDisposed()) {
                            dialog.show();
                        }
                    }
                })
                .doOnTerminate(new Action() {
                    @Override
                    public void run() throws Exception {
                        dialog.dismiss();
                    }
                })
//                .flatMap(new Function<String, ObservableSource<Object>>() {
//                    @Override
//                    public ObservableSource<Object> apply(String headUrl) throws Exception {
//                        return fileRepostory.
//                                bindDoctor(doid, UserSpHelper.getUserId(), headUrl);
//                    }
//                })
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new DefaultObserver<String>() {
                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        ToastUtils.showShort(throwable.getMessage());
                        dialog.dismiss();
                    }

                    @Override
                    public void onNext(String url) {
                        super.onNext(url);
//                        verifyFace(doid, url);
                        bindDoctor(doid, url);
                        dialog.dismiss();
                    }
                });
    }

    private void verifyFace(String doid, String url) {
//        CC.obtainBuilder("com.gcml.auth.face2.signin")
//                .addParam("currentUser", false)
//                .build()
//                .callAsyncCallbackOnMainThread(new IComponentCallback() {
//                    @Override
//                    public void onResult(CC cc, CCResult result) {
//                        boolean skip = "skip".equals(result.getErrorMessage());
//                        if (result.isSuccess() || skip) {
//                            bindDoctor(doid, url);
//                        } else {
//                            ToastUtils.showShort(result.getErrorMessage());
//                        }
//                    }
//                });
    }

    public void bindDoctor(String doid, String headUrl) {
        fileRepostory.bindDoctor(doid, UserSpHelper.getUserId(), headUrl)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new DefaultObserver<Object>() {
                    @Override
                    public void onNext(Object o) {
                        super.onNext(o);
//                        ActivityHelper.finishAll();
                        ToastUtils.showShort("申请成功");
                        dialog.dismiss();
                        Routerfit.register(AppRouter.class).skipAddHealthProfileActivity("");
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        dialog.dismiss();
                        ToastUtils.showShort(throwable.getMessage());
                    }
                });

    }


}
