package com.gcml.module_health_profile.webview;

import android.content.Intent;
import android.os.Build;
import android.webkit.JavascriptInterface;

import com.billy.cc.core.component.CC;
import com.gcml.common.data.UserSpHelper;
import com.gcml.common.widget.BaseX5WebViewActivity;
import com.gcml.module_health_profile.R;
import com.tencent.smtt.sdk.WebView;

public class EditHealthProfileActivity extends BaseX5WebViewActivity {
    private String healthRecordId;
    private String rdRecordId;
    private String healthRecordIdString;
    private String rdRecordIdString;
    private String userIdString;
    private String typeString;

    @Override
    protected String setTitle() {
        return "健 康 档 案 编 辑";
    }

    @Override
    protected void getIntentParam(Intent intent) {
        healthRecordId = intent.getStringExtra("HealthRecordId");
        rdRecordId = intent.getStringExtra("RdCordId");
        healthRecordIdString = "'" + healthRecordId + "'";
        rdRecordIdString = "'" + rdRecordId + "'";
        userIdString = "'" + UserSpHelper.getUserId() + "'";
        typeString = "'公卫表格编辑'";
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

    @JavascriptInterface
    public void move2Next(String healthRecordId) {
        if ("22d594369d8246ad9542f462d6f0f4ce".equals(rdRecordId)) {
            finish();
        } else {
            CC.obtainBuilder("health.profile.add.followup")
                    .addParam("rdRecordId", rdRecordId)
                    .addParam("healthRecordId", healthRecordId)
                    .build().call();
            finish();
        }
    }
}
