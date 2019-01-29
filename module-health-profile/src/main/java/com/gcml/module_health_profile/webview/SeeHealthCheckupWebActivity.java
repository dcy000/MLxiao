package com.gcml.module_health_profile.webview;

import android.content.Intent;

import com.gcml.common.data.UserSpHelper;
import com.gcml.common.widget.BaseX5WebViewActivity;
import com.gcml.module_health_profile.R;
import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebView;

public class SeeHealthCheckupWebActivity extends BaseX5WebViewActivity {
    private String healthRecordId;
    private String rdRecordId;
    private String healthRecordIdString;
    private String rdRecordIdString;
    private String userIdString;
    private String typeString;

    @Override
    protected String setTitle() {
        return "健 康 体 检 表";
    }

    @Override
    protected void getIntentParam(Intent intent) {
        rdRecordId=intent.getStringExtra("RdCordId");
        healthRecordId = intent.getStringExtra("HealthRecordId");
        rdRecordIdString = "'" + rdRecordId + "'";
        healthRecordIdString = "'" + healthRecordId + "'";
        userIdString = "'" + UserSpHelper.getUserId() + "'";
        typeString = "'健康体检'";
    }

    @Override
    protected void loadUrl(WebView webView) {
        webView.loadUrl(getString(R.string.web_path));
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
