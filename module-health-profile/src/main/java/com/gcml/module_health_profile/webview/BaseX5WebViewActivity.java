package com.gcml.module_health_profile.webview;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gcml.common.utils.base.ToolbarBaseActivity;
import com.gcml.common.widget.dialog.LoadingDialog;
import com.gcml.module_health_profile.R;
import com.gcml.module_health_profile.utils.WebResourceRequestAdapter;
import com.gcml.module_health_profile.utils.WebResourceResponseAdapter;
import com.tencent.smtt.export.external.interfaces.WebResourceError;
import com.tencent.smtt.export.external.interfaces.WebResourceRequest;
import com.tencent.smtt.export.external.interfaces.WebResourceResponse;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import ren.yale.android.cachewebviewlib.WebViewCacheInterceptorInst;
import timber.log.Timber;

public abstract class BaseX5WebViewActivity extends ToolbarBaseActivity implements View.OnClickListener {
    private WebView mX5Webview;
    private boolean isPageFinished;
    private long time;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_x5_webview);
        getIntentParam(getIntent());
        initView();
        initWebView();
    }

    protected void getIntentParam(Intent intent) {
    }

    protected void initView() {
        mX5Webview = (WebView) findViewById(R.id.x5_webview);
        mX5Webview.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });
        mTitleText.setText(setTitle());
    }

    protected void initWebView() {
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
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webSettings.setDomStorageEnabled(true);
        webSettings.setDatabaseEnabled(true);
        String cacheDirPath = getFilesDir().getAbsolutePath() + "/xwebview";
        webSettings.setAppCachePath(cacheDirPath);
        webSettings.setAppCacheMaxSize(20 * 1024 * 1024);
        webSettings.setAppCacheEnabled(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setLoadsImagesAutomatically(true);
        webSettings.setDefaultTextEncodingName("utf-8");


        addJavascriptInterface(mX5Webview);
        mX5Webview.addJavascriptInterface(new JsEventBus(), "JsEventBus");
        mX5Webview.setWebViewClient(new WebViewClient() {
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView webView, String s) {
                return WebResourceResponseAdapter.adapter(WebViewCacheInterceptorInst.getInstance().
                        interceptRequest(s));
            }

            @Override
            public WebResourceResponse shouldInterceptRequest(WebView webView, WebResourceRequest webResourceRequest) {

                return WebResourceResponseAdapter.adapter(WebViewCacheInterceptorInst.getInstance().
                        interceptRequest(WebResourceRequestAdapter.adapter(webResourceRequest)));
            }


            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                showLoading("正在加载页面");
                time = System.currentTimeMillis();
                Timber.i("X5WebView loading start>>>" + url);
                onWebViewPageStart(view);
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onLoadResource(WebView webView, String s) {
                Timber.i("X5WebView loading resource:::::cost time>>" + (System.currentTimeMillis() - time) + ">>>" + s);
                time = System.currentTimeMillis();
                super.onLoadResource(webView, s);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                dismissLoading();
                Timber.i("X5WebView loading end:::::cost time>>" + (System.currentTimeMillis() - time) + ">>>" + url);
                time = System.currentTimeMillis();
                if (!isPageFinished) {
                    isPageFinished = true;
                    onWebViewPageFinished(view);
                }
                super.onPageFinished(view, url);
            }

            @Override
            public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
                super.onReceivedHttpError(view, request, errorResponse);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                onWebViewPageReceivedError(view);
                super.onReceivedError(view, request, error);
            }
        });

        mX5Webview.setWebChromeClient(new WebChromeClient() {
            //获取加载进度
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100 && !isPageFinished) {
                    Timber.i("X5WebView loading end:::::cost time>>" + (System.currentTimeMillis() - time) + ">>>" + newProgress);
                    time = System.currentTimeMillis();
                }
                onPageLoadingProgress(view, newProgress);
                super.onProgressChanged(view, newProgress);
            }


        });
        String url = loadUrl(mX5Webview);
        WebViewCacheInterceptorInst.getInstance().loadUrl(url, mX5Webview.getSettings().getUserAgentString());
    }


    @CallSuper
    @Override
    public void onClick(View v) {
        super.onClick(v);
        int i = v.getId();
        if (i == com.gcml.common.business.R.id.ll_back) {
            backLastActivity();
        } else if (i == com.gcml.common.business.R.id.iv_top_right) {
            backMainActivity();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        removeJavascriptInterface(mX5Webview);
        mX5Webview.addJavascriptInterface(this, "JsEventBus");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dismissLoading();
        if (mX5Webview != null) {
//            mX5Webview.clearCache(true);
//            mX5Webview.clearHistory();
//            mX5Webview.clearFormData();
//            mX5Webview.clearMatches();
//            mX5Webview.clearSslPreferences();
            mX5Webview.destroy();
        }
    }

    protected String setTitle() {
        return "";
    }

    protected void backLastActivity() {
        if (mX5Webview.canGoBack()) {
            mX5Webview.goBack();
        } else {
            finish();
        }
    }


    public class JsEventBus {
        @JavascriptInterface
        public void onJsEvent(String event) {
            if ("PageReady".equals(event)) {
                dismissLoading();
            }
        }
    }

    protected abstract String loadUrl(WebView webView);

    protected abstract void addJavascriptInterface(WebView webView);

    protected abstract void removeJavascriptInterface(WebView webView);

    protected abstract void onWebViewPageStart(WebView webView);

    protected abstract void onWebViewPageFinished(WebView webView);

    protected abstract void onWebViewPageReceivedError(WebView webView);

    protected abstract void onPageLoadingProgress(WebView webView, int progress);

    private LoadingDialog mLoadingDialog;

    public void showLoading(String tips) {
        if (mLoadingDialog != null) {
            LoadingDialog loadingDialog = mLoadingDialog;
            mLoadingDialog = null;
            loadingDialog.dismiss();
        }
        mLoadingDialog = new LoadingDialog.Builder(this)
                .setIconType(LoadingDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord(tips)
                .create();
        mLoadingDialog.show();
    }

    public void dismissLoading() {
        if (mLoadingDialog != null) {
            LoadingDialog loadingDialog = mLoadingDialog;
            mLoadingDialog = null;
            loadingDialog.dismiss();
        }
    }
}
