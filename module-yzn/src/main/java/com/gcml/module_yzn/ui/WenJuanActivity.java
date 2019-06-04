package com.gcml.module_yzn.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import com.gcml.common.data.UserSpHelper;
import com.gcml.common.router.AppRouter;
import com.gcml.common.utils.DefaultObserver;
import com.gcml.common.utils.RxUtils;
import com.gcml.common.utils.display.ToastUtils;
import com.gcml.common.widget.dialog.LoadingDialog;
import com.gcml.common.widget.toolbar.ToolBarClickListener;
import com.gcml.common.widget.toolbar.TranslucentToolBar;
import com.gcml.module_yzn.R;
import com.gcml.module_yzn.bean.WenJuanInputBean;
import com.gcml.module_yzn.bean.WenJuanOutBean;
import com.gcml.module_yzn.repository.YZNRepository;
import com.gcml.module_yzn.ui.fragment.WenJuanFragment;
import com.gcml.module_yzn.util.BASE64Encoder;
import com.gcml.module_yzn.util.MD5Util;
import com.google.gson.Gson;
import com.sjtu.yifei.route.Routerfit;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.gcml.module_yzn.constant.Global.APP_ID;
import static com.gcml.module_yzn.constant.Global.APP_KEY;

//@Route(path = "/module/yzn/zenduan/activity")
public class WenJuanActivity extends AppCompatActivity {

    private TranslucentToolBar tbWenjuan;
    private FrameLayout flContaniner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wen_juan);
        initView();
        initData();
    }

    private void initData() {
        WenJuanInputBean inputBean = new WenJuanInputBean();
        inputBean.userId = UserSpHelper.getUserId();
        RequstWenJuanData(inputBean);
    }

    YZNRepository repository = new YZNRepository();
    LoadingDialog dialog;

    private void RequstWenJuanData(WenJuanInputBean inputBean) {
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
                .subscribe(new DefaultObserver<List<WenJuanOutBean.ItemBean>>() {
                    @Override
                    public void onNext(List<WenJuanOutBean.ItemBean> data) {
                        super.onNext(data);
                        try {
                            getSupportFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.fl_container, WenJuanFragment.newInstance(data, null))
                                    .commitAllowingStateLoss();
                        } catch (Exception e) {

                        }
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

    private void initView() {
        tbWenjuan = findViewById(R.id.tv_wenjuan);
        flContaniner = findViewById(R.id.fl_container);

        tbWenjuan.setData("问卷", R.drawable.common_icon_back, "返回",
                R.drawable.auth_hospital_ic_setting, null,
                new ToolBarClickListener() {
                    @Override
                    public void onLeftClick() {
                        finish();
                    }

                    @Override
                    public void onRightClick() {
                        Routerfit.register(AppRouter.class).skipSettingActivity();
                    }
                });
    }

}