package com.example.han.referralproject.activity;


import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RadioGroup;

import com.example.han.referralproject.R;

public class RecordActivity extends BaseActivity implements View.OnClickListener{
    private WebView webView;
    private final String UrlFormat = "http://192.168.200.103:8080/ZZB/br/cl?bid=100002&temp=%d";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        findViewById(R.id.iv_back).setOnClickListener(this);
        RadioGroup mRadioGroup = (RadioGroup) findViewById(R.id.rg_tab);
        mRadioGroup.setOnCheckedChangeListener(mCheckedChangeListener);
        initWebView();
    }

    private RadioGroup.OnCheckedChangeListener mCheckedChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
            int temp = 0;
            switch (checkedId){
                case R.id.rb_xueya:
                    temp = 2;
                    break;
                case R.id.rb_wendu:
                    temp = 1;
                    break;
                case R.id.rb_xuetang:
                    temp = 4;
                    break;
                case R.id.rb_xueyang:
                    temp = 5;
                    break;
            }
            showLoadingDialog(getString(R.string.do_loading));
            webView.loadUrl(String.format(UrlFormat, temp));
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                finish();
                break;
        }
    }

    private void initWebView() {
        webView = (WebView) findViewById(R.id.wv_content);
        showLoadingDialog(getString(R.string.do_loading));
        webView.loadUrl(String.format(UrlFormat, 2));
        //webView.addJavascriptInterface(new JSHook(context), "androidClient");
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setSupportZoom(false);
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView.getSettings().setAllowUniversalAccessFromFileURLs(true);
        webView.getSettings().setAllowFileAccessFromFileURLs(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
            }

            @Override
            public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
                return true;
            }
        });
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                hideLoadingDialog();
            }
        });
    }


}
