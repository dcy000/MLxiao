package com.gcml.module_health_profile.webview;

import android.content.Intent;
import android.webkit.JavascriptInterface;

import com.billy.cc.core.component.CC;
import com.gcml.common.data.UserSpHelper;
import com.gcml.common.utils.display.ToastUtils;
import com.gcml.common.widget.BaseX5WebViewActivity;
import com.gcml.common.wifi.WeakHandler;
import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebView;

import timber.log.Timber;

/**
 * 创建健康档案
 */
public class AddHealthProfileActivity extends BaseX5WebViewActivity {
    private String rdRecordId;
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
        rdRecordIdString = "'" + rdRecordId + "'";
        userIdString = "'" + UserSpHelper.getUserId() + "'";
        typeString = "'公卫表格添加'";
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
        webView.loadUrl("javascript:uniqueMark(" + typeString + "," + rdRecordIdString + "," + userIdString + ")");
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
        }
    }
}
