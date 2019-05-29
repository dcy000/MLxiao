package com.gcml.module_yzn.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.gcml.common.data.UserSpHelper;
import com.gcml.common.utils.DefaultObserver;
import com.gcml.common.utils.RxUtils;
import com.gcml.common.utils.display.ToastUtils;
import com.gcml.common.widget.dialog.LoadingDialog;
import com.gcml.module_yzn.R;
import com.gcml.module_yzn.bean.CaseInputBean;
import com.gcml.module_yzn.bean.RegisterInputBean;
import com.gcml.module_yzn.bean.WenJuanInputBean;
import com.gcml.module_yzn.repository.YZNRepository;
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

    private TextView tvRegister, tvBingli, tvWenJuan, tvZinXunFenLei;
    private LoadingDialog dialog;
    private YZNRepository repository = new YZNRepository();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_api_test);
        tvRegister = findViewById(R.id.tv_register);
        tvBingli = findViewById(R.id.tv_bingli);
        tvWenJuan = findViewById(R.id.tv_wenjuan);
        tvZinXunFenLei = findViewById(R.id.tv_zixun_fenlei);

        tvRegister.setOnClickListener(this);
        tvBingli.setOnClickListener(this);
        tvWenJuan.setOnClickListener(this);
        tvZinXunFenLei.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == tvRegister) {
            RegisterInputBean registerInputBean = new RegisterInputBean();
            registerInputBean.name = "zhanghui";
            registerInputBean.userId = UserSpHelper.getUserId();
            regist(registerInputBean);
        } else if (v == tvBingli) {
            CaseInputBean inputBean = new CaseInputBean();
            inputBean.userId = UserSpHelper.getUserId();
            bingLi(inputBean);
        } else if (v == tvWenJuan) {
            WenJuanInputBean inputBean = new WenJuanInputBean();
            inputBean.userId = UserSpHelper.getUserId();
            wenJuan(inputBean);
        } else if (v == tvZinXunFenLei) {
            CaseInputBean inputBean = new CaseInputBean();
            inputBean.userId = UserSpHelper.getUserId();
            inputBean.url = "news";
            bingLi(inputBean);
        }
    }

    private void wenJuan(WenJuanInputBean inputBean) {
        dialog = new LoadingDialog.Builder(this)
                .setIconType(LoadingDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord("正在加载")
                .create();

        String inputJson = new Gson().toJson(inputBean);
        String param = BASE64Encoder.encodeString(inputJson);
        String currentTime = System.currentTimeMillis() / 1000 + "";

        String tokenTemp = APP_KEY + currentTime + param;
        String token = MD5Util.md5Encrypt32Upper(tokenTemp);

        repository.wenJuan(APP_ID, currentTime, param, token)
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

    /**
     * 病例入口
     */
    private void bingLi(CaseInputBean inputBean) {
        dialog = new LoadingDialog.Builder(this)
                .setIconType(LoadingDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord("正在加载")
                .create();
        String inputJson = new Gson().toJson(inputBean);
        String param = BASE64Encoder.encodeString(inputJson);
        String currentTime = System.currentTimeMillis() / 1000 + "";

        String tokenTemp = APP_KEY + currentTime + param;
        String token = MD5Util.md5Encrypt32Upper(tokenTemp);

        repository.bingLi(APP_ID, currentTime, param, token)
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


    /**
     * 注册
     */
    private void regist(RegisterInputBean inputBean) {

        dialog = new LoadingDialog.Builder(this)
                .setIconType(LoadingDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord("正在加载")
                .create();
        String inputJson = new Gson().toJson(inputBean);
        String param = BASE64Encoder.encodeString(inputJson);
        String currentTime = System.currentTimeMillis() / 1000 + "";

        String tokenTemp = APP_KEY + currentTime + param;
        String token = MD5Util.md5Encrypt32Upper(tokenTemp);

        repository.regiter(APP_ID, currentTime, param, token)
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
