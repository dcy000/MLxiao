package com.gcml.module_health_profile.webview;

import android.webkit.JavascriptInterface;

import com.gcml.common.widget.BaseX5WebViewActivity;
import com.tencent.smtt.sdk.WebView;

public class EditHealthFileActivity extends BaseX5WebViewActivity {
    @Override
    protected void loadUrl(WebView webView) {
//        webView.loadUrl("http://47.96.98.60:8630/#/");
        webView.loadUrl("http://192.168.0.116:8080/#/index");
//        webView.loadUrl("http://192.168.0.116:8081/gerenxinxi");
    }

    @Override
    protected void addJavascriptInterface(WebView webView) {
        webView.addJavascriptInterface(this, "addSubmit");
    }

    @Override
    protected void onWebViewPageStart(WebView webView) {

    }

    @Override
    protected void onWebViewPageFinished(WebView webView) {
        webView.evaluateJavascript("javascript:receiveMsgFromParent('76e9139bf448430bbcb98d5998db05c4')",null);
//        webView.evaluateJavascript("javascript:receiveMsgFromParent()",null);
//        webView.loadUrl();
    }

    @Override
    protected void onWebViewPageReceivedError(WebView webView) {

    }

    @Override
    protected void onPageLoadingProgress(WebView webView, int progress) {

    }

    @JavascriptInterface
    @Override
    protected void backMainActivity() {
        super.backMainActivity();
    }
}
