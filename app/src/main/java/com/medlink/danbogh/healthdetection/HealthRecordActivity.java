package com.medlink.danbogh.healthdetection;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.application.MyApplication;
import com.example.han.referralproject.network.NetworkApi;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class HealthRecordActivity extends BaseActivity {

    private static final String UrlFormat = NetworkApi.BasicUrl + "/ZZB/br/cl?bid=%s&temp=%d";

    @BindView(R.id.wv_health_record_content)
    WebView wvContent;
    @BindView(R.id.rg_health_record)
    RadioGroup rgHealthRecord;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_home)
    ImageView ivHome;
    public Unbinder mUnbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_record);
        mUnbinder = ButterKnife.bind(this);
        tvTitle.setText("健康档案");
        initWebView();
        initTabs();
    }

    private void initWebView() {
        showLoadingDialog(getString(R.string.do_loading));
        wvContent.loadUrl(String.format(UrlFormat, MyApplication.getInstance().userId, 2));
        //webView.addJavascriptInterface(new JSHook(context), "androidClient");
        wvContent.getSettings().setJavaScriptEnabled(true);
        wvContent.getSettings().setSupportZoom(false);
        wvContent.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        wvContent.getSettings().setAllowUniversalAccessFromFileURLs(true);
        wvContent.getSettings().setAllowFileAccessFromFileURLs(true);
        wvContent.getSettings().setDomStorageEnabled(true);
        wvContent.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        wvContent.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
            }

            @Override
            public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
                return true;
            }
        });
        wvContent.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                hideLoadingDialog();
            }
        });
    }

    private void initTabs() {
        rgHealthRecord.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                int temp = 0;
                switch (checkedId) {
                    case R.id.rb_record_temperature:
                        temp = 1;
                        break;
                    case R.id.rb_record_blood_pressure:
                        temp = 2;
                        break;
                    case R.id.rb_record_blood_glucose:
                        temp = 4;
                        break;
                    case R.id.rb_record_blood_oxygen:
                        temp = 5;
                        break;
                }
                showLoadingDialog(getString(R.string.do_loading));
                wvContent.loadUrl(String.format(UrlFormat, MyApplication.getInstance().userId, temp));
            }
        });
        rgHealthRecord.check(R.id.rb_record_temperature);
    }

    @OnClick(R.id.iv_back)
    public void onIvBackClicked() {
        finish();
    }

    @OnClick(R.id.iv_home)
    public void onIvHomeClicked() {

    }

    @Override
    protected void onDestroy() {
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
        super.onDestroy();
    }
}
