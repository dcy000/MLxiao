package com.gcml.module_health_profile.webview;

import com.gcml.common.widget.BaseX5WebViewActivity;
import com.tencent.smtt.sdk.WebView;

public class PersionallWebActivity extends BaseX5WebViewActivity {
    @Override
    protected String setTitle() {
        return "测试H5页面";
    }
    //    String userAgent = "shixinzhang";
//    String js = "window.localStorage.setItem('userAgent','" + userAgent + "');";
//    String jsUrl = "javascript:(function({
//            + "var localStorage = window.localStorage;" +
//            +"localStorage.setItem('userAgent','" + userAgent + "')" +
//            "})()";

    @Override
    protected void loadUrl(WebView webView) {
//        webView.loadUrl("http://47.96.98.60:8630/#/");
//        webView.loadUrl("http://192.168.0.116:8080/#/index");
//        webView.loadUrl("http://192.168.0.116:8081/gerenxinxi");
        webView.loadUrl("http://192.168.0.116:8080/#/");
    }

    @Override
    protected void addJavascriptInterface(WebView webView) {
//        webView.addJavascriptInterface(this, "addSubmit");
    }

    @Override
    protected void removeJavascriptInterface(WebView webView) {

    }

    @Override
    protected void onWebViewPageStart(WebView webView) {

    }

    @Override
    protected void onWebViewPageFinished(WebView webView) {
        webView.loadUrl("javascript:uniqueMark('个人信息')");
        webView.loadUrl("javascript:receiveMsgFromParent()");
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
