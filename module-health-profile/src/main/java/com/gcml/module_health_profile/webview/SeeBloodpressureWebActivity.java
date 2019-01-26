package com.gcml.module_health_profile.webview;

import android.content.Intent;
import android.webkit.JavascriptInterface;

import com.gcml.common.data.UserSpHelper;
import com.gcml.common.widget.BaseX5WebViewActivity;
import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebView;

public class SeeBloodpressureWebActivity extends BaseX5WebViewActivity {
    private String healthRecordId;
    private String rdRecordId;
    private String healthRecordIdString;
    private String rdRecordIdString;
    private String userIdString;
    private String typeString;

    @Override
    protected String setTitle() {
        return "测试H5页面";
    }

    @Override
    protected void getIntentParam(Intent intent) {
        rdRecordId = intent.getStringExtra("RdCordId");
        healthRecordId = intent.getStringExtra("HealthRecordId");
        rdRecordIdString = "'" + rdRecordId + "'";
        healthRecordIdString = "'" + healthRecordId + "'";
        userIdString = "'" + UserSpHelper.getUserId() + "'";
        typeString = "'高血压'";
    }

    @Override
    protected void loadUrl(WebView webView) {
        webView.loadUrl("http://192.168.0.116:8080/#/");
    }

    @Override
    protected void addJavascriptInterface(WebView webView) {
        webView.addJavascriptInterface(this, "addSubmit");
    }

    @Override
    protected void removeJavascriptInterface(WebView webView) {
        webView.removeJavascriptInterface("addSubmit");
    }

    @Override
    protected void onWebViewPageStart(WebView webView) {

    }

    @Override
    protected void onWebViewPageFinished(WebView webView) {
        webView.loadUrl("javascript:uniqueMark(" + typeString + "," + rdRecordIdString + "," + userIdString + "," + healthRecordIdString + ")");
    }

    @Override
    protected void onWebViewPageReceivedError(WebView webView) {

    }

    @Override
    protected void onPageLoadingProgress(WebView webView, int progress) {

    }

    @Override
    protected void backLastActivity() {
        finish();
    }

}
