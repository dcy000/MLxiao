package com.gcml.module_health_profile.fragments;

import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.View;


import com.gcml.common.data.UserSpHelper;
import com.gcml.common.utils.base.RecycleBaseFragment;
import com.gcml.common.widget.dialog.LoadingDialog;
import com.gcml.module_health_profile.R;
import com.tencent.smtt.export.external.interfaces.WebResourceError;
import com.tencent.smtt.export.external.interfaces.WebResourceRequest;
import com.tencent.smtt.export.external.interfaces.WebResourceResponse;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import timber.log.Timber;

public class PersonalnforFragment extends RecycleBaseFragment {
    private String recordId;
    private WebView mX5Webview;
    private boolean isPageFinished;
    private String rdRecordIdString;
    private String userIdString;
    private String typeString;

    public static PersonalnforFragment instance(String recordId) {
        Bundle bundle = new Bundle();
        bundle.putString("recordId", recordId);
        PersonalnforFragment personalnforFragment = new PersonalnforFragment();
        personalnforFragment.setArguments(bundle);
        return personalnforFragment;
    }

    @Override
    protected int initLayout() {
        return R.layout.fragment_personal_info;
    }

    @Override
    protected void initView(View view, Bundle bundle) {
        recordId = bundle.getString("recordId");
        rdRecordIdString = "'" + recordId + "'";
        userIdString = "'" + UserSpHelper.getUserId() + "'";
        mX5Webview = view.findViewById(R.id.web_person_info);
        typeString = "'公卫表格添加'";
        initWebView();
    }

    private void initWebView() {
        WebSettings webSettings = mX5Webview.getSettings();
        webSettings.setJavaScriptEnabled(true);

        //设置自适应屏幕，两者合用
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // chromium, enable hardware acceleration
            mX5Webview.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else {
            // older android version, disable hardware acceleration
            mX5Webview.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        //提高渲染的优先级
        webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        //图片放最后渲染
        webSettings.setBlockNetworkImage(true);
        //缩放操作
        webSettings.setSupportZoom(false);
        webSettings.setBuiltInZoomControls(false);
        webSettings.setDisplayZoomControls(false);

        //不显示滚动条
        mX5Webview.setVerticalScrollBarEnabled(false);

        //安全漏洞问题
        webSettings.setAllowFileAccessFromFileURLs(false);

        //缓存
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        webSettings.setDomStorageEnabled(false);
        webSettings.setDatabaseEnabled(false);
        String cacheDirPath = getActivity().getFilesDir().getAbsolutePath() + "/xwebview";
        webSettings.setDatabasePath(cacheDirPath);
        webSettings.setAppCachePath(cacheDirPath);
        webSettings.setAppCacheMaxSize(20 * 1024 * 1024);
        webSettings.setAppCacheEnabled(false);

        webSettings.setAllowFileAccess(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setLoadsImagesAutomatically(true);
        webSettings.setDefaultTextEncodingName("utf-8");


        addJavascriptInterface(mX5Webview);
        mX5Webview.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(com.tencent.smtt.sdk.WebView view, String url, Bitmap favicon) {
                Timber.i("X5WebView loading start>>>" + url);
                onWebViewPageStart(view);
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onLoadResource(com.tencent.smtt.sdk.WebView webView, String s) {
                super.onLoadResource(webView, s);
            }

            @Override
            public void onPageFinished(com.tencent.smtt.sdk.WebView view, String url) {
                if (!isPageFinished) {
                    isPageFinished = true;
                    onWebViewPageFinished(view);
                }
                super.onPageFinished(view, url);
            }

            @Override
            public void onReceivedHttpError(com.tencent.smtt.sdk.WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
                super.onReceivedHttpError(view, request, errorResponse);
            }

            @Override
            public void onReceivedError(com.tencent.smtt.sdk.WebView view, WebResourceRequest request, WebResourceError error) {
                onWebViewPageReceivedError(view);
                super.onReceivedError(view, request, error);
            }
        });


        mX5Webview.setWebChromeClient(new WebChromeClient() {
            //获取加载进度
            @Override
            public void onProgressChanged(com.tencent.smtt.sdk.WebView view, int newProgress) {
                onPageLoadingProgress(view, newProgress);
                super.onProgressChanged(view, newProgress);
            }


        });
        loadUrl(mX5Webview);

    }


    protected void loadUrl(com.tencent.smtt.sdk.WebView webView) {
        webView.loadUrl("http://47.96.98.60:8630/#/");
    }

    protected void addJavascriptInterface(com.tencent.smtt.sdk.WebView webView) {
        webView.addJavascriptInterface(this, "addSubmit");
    }

    protected void removeJavascriptInterface(com.tencent.smtt.sdk.WebView webView) {
        webView.removeJavascriptInterface("addSubmit");
    }

    protected void onWebViewPageStart(com.tencent.smtt.sdk.WebView webView) {

    }

    protected void onWebViewPageFinished(com.tencent.smtt.sdk.WebView webView) {
        webView.loadUrl("javascript:uniqueMark(" + typeString + "," + rdRecordIdString + "," + userIdString + ")");
    }

    protected void onWebViewPageReceivedError(com.tencent.smtt.sdk.WebView webView) {

    }

    protected void onPageLoadingProgress(com.tencent.smtt.sdk.WebView webView, int progress) {

    }

    @Override
    public void onStop() {
        super.onStop();
        removeJavascriptInterface(mX5Webview);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mX5Webview != null) {
            mX5Webview.clearCache(true);
            mX5Webview.clearHistory();
            mX5Webview.clearFormData();
            mX5Webview.clearMatches();
            mX5Webview.clearSslPreferences();
            mX5Webview.destroy();
        }
    }


}
