package com.gcml.module_health_profile.webview;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.widget.ImageView;
import android.widget.TextView;

import com.gcml.common.data.UserEntity;
import com.gcml.common.data.UserSpHelper;
import com.gcml.common.router.AppRouter;
import com.gcml.common.utils.UM;
import com.gcml.common.utils.base.ToolbarBaseActivity;
import com.gcml.common.utils.display.ToastUtils;
import com.gcml.common.utils.qrcode.QRCodeUtils;
import com.gcml.common.widget.EclipseLinearLayout;
import com.gcml.common.widget.dialog.AlertDialog;
import com.gcml.lib_printer_8003dd.ConnectPrinterHelper;
import com.gcml.lib_printer_8003dd.IPrinterView;
import com.gcml.module_health_profile.R;
import com.gcml.module_health_profile.bean.OutputMeasureBean;
import com.gcml.module_health_profile.data.HealthProfileRepository;
import com.iflytek.synthetize.MLVoiceSynthetize;
import com.sjtu.yifei.annotation.Route;
import com.sjtu.yifei.route.Routerfit;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DefaultObserver;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;
@Route(path = "/health/profile/wenzen/output")
public class WenZenOutputActivity extends ToolbarBaseActivity implements View.OnClickListener, IPrinterView {
    private ImageView mIvQr;
    /**
     * 扫描查看体检报告
     */
    private TextView mTvQr;
    private EclipseLinearLayout mLl1;
    /**
     * 体检报告打印
     */
    private TextView mTvPrinter;
    private EclipseLinearLayout mLl2;
    /**
     * 回到公卫首页
     */
    private TextView mTvGohome;
    private ConnectPrinterHelper printerHelper;
    private String rdRecordId;
    private String userRecordId;
    private WebView mX5Webview;
    private String typeString;
    private String rdRecordIdString;
    private String userIdString;
    private String healthRecordIdString;
    private MutableLiveData<String> imgLivedata = new MutableLiveData<>();
    private String highPressure, lowPressure, weight, height, bloodoxygen, bloodsugar, temperature, uac, cholesterol;
    private String userName;
    private String userSex;
    private String userAge;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wenzen_output_result);
        initView();
        initWebView();
    }

    private void initWebView() {
        WebSettings webSettings = mX5Webview.getSettings();
        webSettings.setJavaScriptEnabled(true);

        //设置自适应屏幕，两者合用
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // chromium, enable hardware acceleration
            mX5Webview.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else {
            // older android version, disable hardware acceleration
            mX5Webview.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        //提高渲染的优先级
        webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        //图片放最后渲染
        webSettings.setBlockNetworkImage(true);
        //缩放操作
        webSettings.setSupportZoom(false);
        webSettings.setBuiltInZoomControls(false);
        webSettings.setDisplayZoomControls(false);

        //不显示滚动条
        mX5Webview.setVerticalScrollBarEnabled(false);

        //安全漏洞问题
        webSettings.setAllowFileAccessFromFileURLs(false);

        //缓存
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        webSettings.setDomStorageEnabled(false);
        webSettings.setDatabaseEnabled(false);
        String cacheDirPath = getFilesDir().getAbsolutePath() + "/xwebview";
        webSettings.setDatabasePath(cacheDirPath);
        webSettings.setAppCachePath(cacheDirPath);
        webSettings.setAppCacheMaxSize(20 * 1024 * 1024);
        webSettings.setAppCacheEnabled(false);

        webSettings.setAllowFileAccess(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setLoadsImagesAutomatically(true);
        webSettings.setDefaultTextEncodingName("utf-8");
        mX5Webview.addJavascriptInterface(this, "addSubmit");

        mX5Webview.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView webView, String s) {
                super.onPageFinished(webView, s);
                webView.loadUrl("javascript:uniqueMark(" + typeString + "," + rdRecordIdString + "," + userIdString + "," + healthRecordIdString + ")");
            }

            @Override
            public void onLoadResource(WebView webView, String s) {
                super.onLoadResource(webView, s);
                Timber.i("X5WebView loading resource:::::cost time>>" + ">>>" + s);
            }
        });
        mX5Webview.loadUrl(getString(R.string.web_path));

    }

    private void initView() {
        mTitleText.setText("报 告 打 印");
        mRightView.setImageResource(R.drawable.health_profile_ic_bluetooth_disconnected);
        mIvQr = (ImageView) findViewById(R.id.iv_qr);
        mTvQr = (TextView) findViewById(R.id.tv_qr);
        mLl1 = (EclipseLinearLayout) findViewById(R.id.ll_1);
        mLl1.setOnClickListener(this);
        mTvPrinter = (TextView) findViewById(R.id.tv_printer);
        mLl2 = (EclipseLinearLayout) findViewById(R.id.ll_2);
        mLl2.setOnClickListener(this);
        mTvGohome = (TextView) findViewById(R.id.tv_gohome);
        mTvGohome.setOnClickListener(this);
        mX5Webview = (WebView) findViewById(R.id.x5_webview);
        rdRecordId = getIntent().getStringExtra("rdRecordId");
        userRecordId = getIntent().getStringExtra("userRecordId");
        typeString = "'" + getIntent().getStringExtra("typeString") + "'";
        rdRecordIdString = "'" + rdRecordId + "'";
        userIdString = "'" + UserSpHelper.getUserId() + "'";
        healthRecordIdString = "'" + userRecordId + "'";

        imgLivedata.observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String img) {
                Timber.e("二维码地址：" + img);
                mIvQr.setImageBitmap(QRCodeUtils.creatQRCode(img, 600, 600));
            }
        });

    }

    @JavascriptInterface
    public void qrImage(String img) {
        imgLivedata.postValue(img);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
        initPrinter();
    }

    private void getData() {

        Routerfit.register(AppRouter.class)
                .getUserProvider()
                .getUserEntity()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new com.gcml.common.utils.DefaultObserver<UserEntity>() {
                    @Override
                    public void onNext(UserEntity user) {
                        userName = user.name;
                        userAge = user.age;
                        userSex = user.sex;
                    }
                });
        HealthProfileRepository repository = new HealthProfileRepository();
        repository.getHealthRecordMeasureResult(rdRecordId, userRecordId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<List<OutputMeasureBean>>() {
                    @Override
                    public void onNext(List<OutputMeasureBean> outputMeasureBeans) {
                        dealResult(outputMeasureBeans);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void dealResult(List<OutputMeasureBean> outputMeasureBeans) {
        for (OutputMeasureBean bean : outputMeasureBeans) {
            if (bean.getName().contains("高压")) {
                highPressure = bean.getValue();
            }
            if (bean.getName().contains("低压")) {
                lowPressure = bean.getValue();
            }
            if (bean.getName().contains("体重")) {
                weight = bean.getValue();
            }
            if (bean.getName().contains("身高")) {
                height = bean.getValue();
            }
            if (bean.getName().contains("温")) {
                temperature = bean.getValue();
            }
            if (bean.getName().contains("氧")) {
                bloodoxygen = bean.getValue();
            }
            if (bean.getName().contains("糖")) {
                bloodsugar = bean.getValue();
            }
            if (bean.getName().contains("尿酸")) {
                uac = bean.getValue();
            }
            if (bean.getName().contains("胆固醇")) {
                cholesterol = bean.getValue();
            }
        }
    }

    private void initPrinter() {
        printerHelper = new ConnectPrinterHelper(this);
        printerHelper.start();
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        int i = v.getId();
        if (i == R.id.ll_1) {
        } else if (i == R.id.ll_2) {
            if (printerHelper == null) {
                ToastUtils.showShort("连接打印机出错");
                MLVoiceSynthetize.startSynthesize(UM.getApp(), "连接打印机出错");
                return;
            }
            if (printerHelper != null && !printerHelper.isConnected()) {
                ToastUtils.showShort("请先连接打印机");
                MLVoiceSynthetize.startSynthesize(UM.getApp(), "请先连接打印机");
                return;
            }
            try {
                printResult();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } else if (i == R.id.tv_gohome) {
            //回到公卫首页首页
            //CC.obtainBuilder("health.profile").build().call();

            //回到首页
            Routerfit.register(AppRouter.class).skipMainActivity();
        }
    }

    private void printResult() throws UnsupportedEncodingException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        Date date = new Date();
        String sDate = format.format(date);
        String sDate1 = format1.format(date);
        printerHelper.initPrinter();
        printerHelper.printTitle("健康智能管家检测数据单\n\n\n");
        printerHelper.printContent(
                "姓名：" + userName + "\n" +
                        "年龄：" + userAge + "\n" +
                        "性别：" + userSex + "\n" +
                        "打印时间：" + sDate1 + "\n" +
                        String.format("%-8s", "检测项目") + String.format("%-8s", "检测结果") + String.format("%-8s", "检测单位") + String.format("%-8s", "参考标准") + "\n" +
                        String.format("%1$-" + 13 + "s", "血压") + String.format("%1$-" + 12 + "s", (TextUtils.isEmpty(highPressure) ? "-/-" : lowPressure + "/" + highPressure)) + String.format("%1$-" + 10 + "s", "mmHg") + String.format("%1$-" + 6 + "s", "90~140") + "\n" +
                        String.format("%1$-" + 13 + "s", "血氧") + String.format("%1$-" + 12 + "s", (TextUtils.isEmpty(bloodoxygen) ? "--" : bloodoxygen)) + String.format("%1$-" + 10 + "s", "%") + String.format("%1$-" + 6 + "s", ">94%") + "\n" +
                        String.format("%1$-" + 13 + "s", "体温") + String.format("%1$-" + 12 + "s", (TextUtils.isEmpty(temperature) ? "--" : temperature)) + String.format("%1$-" + 10 + "s", "℃") + String.format("%1$-" + 6 + "s", "36.1~37.1") + "\n" +
                        String.format("%1$-" + 13 + "s", "血糖") + String.format("%1$-" + 12 + "s", (TextUtils.isEmpty(bloodsugar) ? "--" : bloodsugar)) + String.format("%1$-" + 10 + "s", "mmol/L") + String.format("%1$-" + 6 + "s", "3.61~7.0") + "\n" +
                        String.format("%1$-" + 13 + "s", "体重") + String.format("%1$-" + 12 + "s", (TextUtils.isEmpty(weight) ? "--" : weight)) + String.format("%1$-" + 10 + "s", "Kg") + String.format("%1$-" + 6 + "s", "--") + "\n" +
                        String.format("%1$-" + 13 + "s", "身高") + String.format("%1$-" + 12 + "s", (TextUtils.isEmpty(height) ? "--" : height)) + String.format("%1$-" + 10 + "s", "cm") + String.format("%1$-" + 6 + "s", "--") + "\n" +
                        String.format("%1$-" + 13 + "s", "尿酸") + String.format("%1$-" + 12 + "s", (TextUtils.isEmpty(uac) ? "--" : uac)) + String.format("%1$-" + 10 + "s", "mmol/L") + String.format("%1$-" + 6 + "s", "0.21~0.44") + "\n" +
                        String.format("%1$-" + 12 + "s", "胆固醇") + String.format("%1$-" + 12 + "s", (TextUtils.isEmpty(cholesterol) ? "--" : cholesterol)) + String.format("%1$-" + 10 + "s", "mmol/L") + String.format("%1$-" + 6 + "s", "2.9~6.0") + "\n");
        printerHelper.printBottom();
    }

    @Override
    public void updateState(String state) {
        ToastUtils.showShort(state);
        MLVoiceSynthetize.startSynthesize(this, state);
        if ("连接打印机成功".equals(state)) {
            mRightView.setImageResource(R.drawable.health_profile_ic_bluetooth_connected);
        } else if ("打印机蓝牙已断开".equals(state)) {
            mRightView.setImageResource(R.drawable.health_profile_ic_bluetooth_disconnected);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        mX5Webview.removeJavascriptInterface("addSubmit");
    }

    @Override
    protected void backMainActivity() {
        showRefreshBluetoothDialog();
    }

    /**
     * 展示刷新
     */
    private void showRefreshBluetoothDialog() {
        new AlertDialog(WenZenOutputActivity.this)
                .builder()
                .setMsg("您确定解绑之前的设备，重新连接新设备吗？")
                .setNegativeButton("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                })
                .setPositiveButton("确认", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mRightView.setImageResource(R.drawable.health_profile_ic_bluetooth_disconnected);
                        initPrinter();
                    }
                }).show();
    }
}
