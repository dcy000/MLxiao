package com.gcml.module_yzn.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.gcml.common.router.AppRouter;
import com.gcml.common.utils.base.ToolbarBaseActivity;
import com.gcml.common.widget.toolbar.ToolBarClickListener;
import com.gcml.common.widget.toolbar.TranslucentToolBar;
import com.gcml.module_yzn.R;
import com.sjtu.yifei.route.Routerfit;

/**
 * Created by lenovo on 2019/5/6.
 */

public class YiZhiTangDetailActivity extends ToolbarBaseActivity {
    private TranslucentToolBar tb;
    private WebView webView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yi_zhi_nang_activity);

        mToolbar.setVisibility(View.GONE);
        tb = findViewById(R.id.tb_yizhitang);
        tb.setData("医智囊", R.drawable.common_icon_back, "返回",
                R.drawable.auth_hospital_ic_setting, null,
                new ToolBarClickListener() {
                    @Override
                    public void onLeftClick() {
                        finish();
                    }

                    @Override
                    public void onRightClick() {
                        Routerfit.register(AppRouter.class).skipSettingActivity();
                    }
                });
        initView();
    }

    private void initView() {
        webView = findViewById(R.id.webview);

//        webView.loadUrl("http://m.yuandaoshop.com/");
//        webView.loadUrl("http://www.baidu.com/");
        WebSettings webSettings = webView.getSettings();

        webSettings.setJavaScriptEnabled(true);

        //设置自适应屏幕，两者合用
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);

        //缩放操作
        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false);

        //不显示滚动条
        webView.setVerticalScrollBarEnabled(false);

        //安全漏洞问题
        webSettings.setAllowFileAccessFromFileURLs(false);

        //缓存
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        webSettings.setAllowFileAccess(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setLoadsImagesAutomatically(true);
        webSettings.setDefaultTextEncodingName("utf-8");
        //设置自适应h5  不设置页面显示空白
        webSettings.setDomStorageEnabled(true);

        /**缓存**webSettings.setDatabaseEnabled(true);
         String cacheDirPath = getFilesDir().getAbsolutePath() + APP_CACHE_DIRNAME;
         Log.i("cachePath", cacheDirPath);
         // 设置数据库缓存路径
         webSettings.setAppCachePath(cacheDirPath);
         webSettings.setAppCacheEnabled(true);**/

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
//                Log.d("WebViewClient", "------------------onPageStarted------------------");
//                Log.d("WebViewClient", "onPageStarted url>>>" + url);
//                Log.d("WebViewClient", "onPageStarted time>>>" + System.currentTimeMillis());
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
//                Log.d("WebViewClient", "------------------onPageFinished------------------");
//                Log.d("WebViewClient", "onPageFinished rul>>>" + url);
//                Log.d("WebViewClient", "onPageFinished time>>>" + System.currentTimeMillis());
//                webView.loadUrl("javascript:receiveMsgFromParent()");

                super.onPageFinished(view, url);
                dismissLoading();
            }

            @Override
            public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
//                Log.d("WebViewClient", "-----------------onReceivedHttpError------------------");
                super.onReceivedHttpError(view, request, errorResponse);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
//                Log.d("WebViewClient", "-----------------onReceivedError-------------------");
                super.onReceivedError(view, request, error);
                finish();
            }

        });


        webView.setWebChromeClient(new WebChromeClient() {

            //获取加载进度
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
//                Log.d("WebChromeClient", "-----------------onProgressChanged------------------");
//                Log.d("WebChromeClient", "progress>>>" + newProgress);
//                Log.d("WebChromeClient", "progress time>>>" + System.currentTimeMillis());
                super.onProgressChanged(view, newProgress);
            }


        });

//        webView.loadUrl("http://47.96.98.60:8630/");
//        webView.loadUrl("http://192.168.0.116:8081/");
//        webView.loadUrl("http://192.168.0.148:8081/");
        Intent intent = getIntent();
        if (intent != null) {
            String itemUrl = intent.getStringExtra("itemUrl");
            webView.loadUrl(itemUrl);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        showLoading("加载中");
    }

    @Override
    protected void onDestroy() {
        if (webView != null) {
            webView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            webView.clearHistory();

            ((ViewGroup) webView.getParent()).removeView(webView);
            webView.destroy();
            webView = null;
        }
        super.onDestroy();
    }


    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            finish();
        }
    }


}
