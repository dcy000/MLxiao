package com.gcml.module_health_profile.webview;

import com.gcml.module_health_profile.R;
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
    protected String loadUrl(WebView webView) {
        String url = getString(R.string.web_path);
        webView.loadUrl(url);
        return url;
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
