package com.gcml.module_yzn.repository;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.gcml.common.utils.DefaultObserver;
import com.gcml.common.utils.RxUtils;
import com.gcml.common.utils.display.ToastUtils;
import com.gcml.common.widget.dialog.LoadingDialog;
import com.gcml.module_yzn.R;
import com.gcml.module_yzn.bean.InputBean;
import com.gcml.module_yzn.util.BASE64Encoder;
import com.gcml.module_yzn.util.MD5Util;
import com.google.gson.Gson;
import com.sjtu.yifei.annotation.Route;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.gcml.module_yzn.constant.Global.APP_ID;
import static com.gcml.module_yzn.constant.Global.APP_KEY;

@Route(path = "/module/yzn/zenduan/activity")
public class ApiTestActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvRegister;
    private TextView tvBingli;
    private LoadingDialog dialog;
    private YZNRepository repository = new YZNRepository();
    ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_api_test);
        tvRegister = findViewById(R.id.tv_register);
        tvBingli = findViewById(R.id.tv_bingli);

        tvRegister.setOnClickListener(this);
        tvBingli.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == tvRegister) {

            InputBean inputBean = new InputBean();
            inputBean.name = "zhanghui";
            inputBean.userId = "user007";
            regist(inputBean);
        } else if (v == tvBingli) {

        }
    }

    private void regist(InputBean inputBean) {

        dialog = new LoadingDialog.Builder(this)
                .setIconType(LoadingDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord("正在加载")
                .create();
        String inputJson = new Gson().toJson(inputBean);
        String param = BASE64Encoder.encodeString(inputJson);
        String currentTime = System.currentTimeMillis() / 1000 + "";

        String tokenTemp = APP_KEY + currentTime + param;
        String token = MD5Util.md5Encrypt32Upper(tokenTemp);

        repository.regiter
                (APP_ID, currentTime, param, token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> dialog.show())
                .doOnTerminate(() -> dialog.dismiss())
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new DefaultObserver<Object>() {
                    @Override
                    public void onNext(Object data) {
                        super.onNext(data);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        dialog.dismiss();
                        ToastUtils.showShort(throwable.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                        dialog.dismiss();
                    }
                });
    }
}
