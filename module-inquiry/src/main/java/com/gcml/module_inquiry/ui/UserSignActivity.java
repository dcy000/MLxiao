package com.gcml.module_inquiry.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.gcml.common.data.UserSpHelper;
import com.gcml.common.utils.DefaultObserver;
import com.gcml.common.utils.RxUtils;
import com.gcml.common.utils.display.ToastUtils;
import com.gcml.common.widget.dialog.LoadingDialog;
import com.gcml.common.widget.toolbar.ToolBarClickListener;
import com.gcml.common.widget.toolbar.TranslucentToolBar;
import com.gcml.module_inquiry.R;
import com.gcml.module_inquiry.dialog.AffirmSignatureDialog;
import com.gcml.module_inquiry.model.HealthFileRepostory;
import com.gcml.module_inquiry.wrap.PainterView;

import java.io.ByteArrayOutputStream;

import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by lenovo on 2019/1/17.
 */

public class UserSignActivity extends AppCompatActivity implements AffirmSignatureDialog.ClickListener {
    private TextView tvSignatrueConfirm;
    private PainterView signature;
    private AffirmSignatureDialog signatureDialog;
    private byte[] bytes;
    private TextView cancel;
    TranslucentToolBar tb;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_sign);
        initTitle();
        initView();
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
                        toUserInfo();
                    }

                    private void toUserInfo() {
                        startActivity(new Intent(UserSignActivity.this, UserInfoListActivity.class));
                    }
                });
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

        LoadingDialog dialog = new LoadingDialog.Builder(this)
                .setIconType(LoadingDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord("")
                .create();

        fileRepostory.uploadHeadData(faceData, UserSpHelper.getUserId())
                .observeOn(AndroidSchedulers.mainThread())
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
                .flatMap(new Function<String, ObservableSource<?>>() {
                    @Override
                    public ObservableSource<?> apply(String headUrl) throws Exception {
                        return fileRepostory.
                                bindDoctor(doid, UserSpHelper.getUserId(), headUrl);
                    }
                })
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new DefaultObserver<Object>() {
                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        ToastUtils.showShort(throwable.getMessage());
                    }

                    @Override
                    public void onNext(Object url) {
                        super.onNext(url);
                        ToastUtils.showShort("申请成功");
                    }
                });
    }


}
