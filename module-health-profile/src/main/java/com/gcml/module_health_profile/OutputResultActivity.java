package com.gcml.module_health_profile;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.widget.ImageView;
import android.widget.TextView;

import com.gcml.common.data.UserSpHelper;
import com.gcml.common.utils.UtilsManager;
import com.gcml.common.utils.base.ToolbarBaseActivity;
import com.gcml.common.utils.display.ToastUtils;
import com.gcml.common.utils.qrcode.QRCodeUtils;
import com.gcml.common.widget.base.dialog.DialogImage;
import com.gcml.lib_printer_8003dd.ConnectPrinterHelper;
import com.gcml.lib_printer_8003dd.IPrinterView;
import com.gcml.lib_widget.EclipseLinearLayout;
import com.gcml.module_health_profile.bean.OutputMeasureBean;
import com.gcml.module_health_profile.data.HealthProfileRepository;
import com.iflytek.synthetize.MLVoiceSynthetize;
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

public class OutputResultActivity extends ToolbarBaseActivity implements View.OnClickListener, IPrinterView {
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_output_result);
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
        });
        mX5Webview.loadUrl("http://192.168.0.116:8080/#/");

    }

    private void initView() {
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
                Timber.e("二维码地址："+img);
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
        HealthProfileRepository repository = new HealthProfileRepository();
        repository.getHealthRecordMeasureResult(rdRecordId, userRecordId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<List<OutputMeasureBean>>() {
                    @Override
                    public void onNext(List<OutputMeasureBean> outputMeasureBeans) {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
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
                MLVoiceSynthetize.startSynthesize(UtilsManager.getApplication(), "连接打印机出错");
                return;
            }
            if (printerHelper != null && !printerHelper.isConnected()) {
                ToastUtils.showShort("请先连接打印机");
                MLVoiceSynthetize.startSynthesize(UtilsManager.getApplication(), "请先连接打印机");
                return;
            }
            try {
                printResult();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } else if (i == R.id.tv_gohome) {

        } else {
        }
    }

    private void printResult() throws UnsupportedEncodingException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        Date date = new Date();
        String sDate = format.format(date);
        String sDate1 = format1.format(date);
        printerHelper.initPrinter();
        printerHelper.printTitle("健康智能管家检测数据单\n\n\n\n");
        printerHelper.printContent("患者：郭志强\n" +
                "检测日期:" + sDate + "\n" +
                "打印时间：" + sDate1 + "\n" +
                "检测项目      检测结果      检测单位    参考标准\n" +
                "血压          134/85        mmHg      90~140\n" +
                "血氧          98            %         >94%\n" +
                "体温          35.8          ℃        36.1~37.1\n" +
                "血糖          5.6           mmol/L    3.61~7.0\n" +
                "体重          67            Kg        --\n" +
                "尿酸          0.36          mmol/L    0.21~0.44\n" +
                "胆固醇        4.6           mmol/L    2.9~6.0\n");
        printerHelper.printBottom();
    }

    @Override
    public void updateState(String state) {
        ToastUtils.showShort(state);
        MLVoiceSynthetize.startSynthesize(this, state);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mX5Webview.removeJavascriptInterface("addSubmit");
    }
}
