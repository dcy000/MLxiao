package com.gcml.module_health_profile.webview;

import android.content.Intent;
import android.webkit.JavascriptInterface;

import com.gcml.common.data.UserSpHelper;
import com.gcml.common.router.AppRouter;
import com.sjtu.yifei.annotation.Route;
import com.sjtu.yifei.route.Routerfit;
import com.tencent.smtt.sdk.WebView;

/**
 * 创建健康档案
 */
@Route(path = "/health/profile/add/health/profile")
public class AddHealthProfileActivity extends BaseX5WebViewActivity {
    private String rdRecordId;
    private String rdRecordIdString;
    private String userIdString;
    private String typeString;
    private String type;
    private String title;

    @Override
    protected String setTitle() {
        return title;
    }

    @Override
    protected void getIntentParam(Intent intent) {
        rdRecordId = intent.getStringExtra("RdCordId");
        type = intent.getStringExtra("type");
        title = intent.getStringExtra("title");
        rdRecordIdString = "'" + rdRecordId + "'";
        userIdString = "'" + UserSpHelper.getUserId() + "'";
        typeString = "'公卫表格添加'";
    }

    @Override
    protected String loadUrl(WebView webView) {
//        String url = getString(R.string.web_path);
        String url = "https://robot-lib-achieve.zuoshouyisheng.com/?app_id=5cc197e8b60c48171066f0e7";
        webView.loadUrl(url);
        return url;
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
        //个人健康档案、个人基本信息
        if ("22d594369d8246ad9542f462d6f0f4ce".equals(rdRecordId) || "76e9139bf448430bbcb98d5998db05c4".equals(rdRecordId)) {
            finish();
        } else {
            Routerfit.register(AppRouter.class).skipAddFollowupActivity(healthRecordId, rdRecordId, type + "二维码扫描");
            finish();
        }
    }
}
