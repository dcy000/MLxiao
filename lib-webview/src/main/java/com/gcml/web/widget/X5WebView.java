package com.gcml.web.widget;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.gcml.common.widget.dialog.LoadingDialog;
import com.gcml.web.R;
import com.tencent.smtt.export.external.interfaces.WebResourceRequest;
import com.tencent.smtt.export.external.interfaces.WebResourceResponse;
import com.tencent.smtt.sdk.CookieManager;
import com.tencent.smtt.sdk.CookieSyncManager;
import com.tencent.smtt.sdk.DownloadListener;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import java.util.ArrayList;
import java.util.List;

public class X5WebView extends WebView {

    private static final String TAG = "X5WebView";
    private static final int MAX_LENGTH = 8;

    ProgressBar progressBar;
    private TextView tvTitle;
    //    public ImageView imageView;
    private List<String> newList;

    private LoadingDialog mLoadingDialog;

    private void showLoading(String tips) {
        if (mLoadingDialog != null) {
            LoadingDialog loadingDialog = mLoadingDialog;
            mLoadingDialog = null;
            loadingDialog.dismiss();
        }
        mLoadingDialog = new LoadingDialog.Builder(getContext())
                .setIconType(LoadingDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord(tips)
                .create();
        mLoadingDialog.show();
    }

    private void dismissLoading() {
        if (mLoadingDialog != null) {
            LoadingDialog loadingDialog = mLoadingDialog;
            mLoadingDialog = null;
            loadingDialog.dismiss();
        }

    }

    public X5WebView(Context context) {
        super(context);
        initUI();
    }

    public X5WebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initUI();
    }

    public X5WebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initUI();
    }

    public void setShowProgress(boolean showProgress) {
        if (showProgress) {
            progressBar.setVisibility(VISIBLE);
        } else {
            progressBar.setVisibility(GONE);
        }
    }


    private void initUI() {

//        getX5WebViewExtension().setScrollBarFadingEnabled(false);
        setHorizontalScrollBarEnabled(false);//水平不显示小方块
        setVerticalScrollBarEnabled(false); //垂直不显示小方块

//      setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);//滚动条在WebView内侧显示
//      setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);//滚动条在WebView外侧显示


        progressBar = new ProgressBar(getContext(), null, android.R.attr.progressBarStyleHorizontal);
        progressBar.setMax(100);
        progressBar.setProgressDrawable(this.getResources().getDrawable(R.drawable.color_progressbar));

        addView(progressBar, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 6));
//        imageView = new ImageView(getContext());
//        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
////      加载图 根据自己的需求去集成使用
//        imageView.setImageResource(R.mipmap.ic_launcher);
//        imageView.setVisibility(VISIBLE);
//        addView(imageView, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        showLoading("加载中");
        initWebViewSettings();
    }

    //   基本的WebViewSetting
    public void initWebViewSettings() {
        setBackgroundColor(getResources().getColor(android.R.color.white));
        setWebViewClient(client);
        setWebChromeClient(chromeClient);
        setDownloadListener(downloadListener);
        setClickable(true);
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
        WebSettings webSetting = getSettings();
        webSetting.setJavaScriptEnabled(true);
        webSetting.setBuiltInZoomControls(true);
        webSetting.setJavaScriptCanOpenWindowsAutomatically(true);
        webSetting.setDomStorageEnabled(true);
        webSetting.setAllowFileAccess(true);
        webSetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSetting.setSupportZoom(true);
        webSetting.setUseWideViewPort(true);
        webSetting.setSupportMultipleWindows(true);
        webSetting.setAppCacheEnabled(true);
        webSetting.setGeolocationEnabled(true);
        webSetting.setAppCacheMaxSize(Long.MAX_VALUE);
        webSetting.setPluginState(WebSettings.PluginState.ON_DEMAND);
        webSetting.setRenderPriority(WebSettings.RenderPriority.HIGH);
        //android 默认是可以打开_bank的，是因为它默认设置了WebSettings.setSupportMultipleWindows(false)
        //在false状态下，_bank也会在当前页面打开……
        //而x5浏览器，默认开启了WebSettings.setSupportMultipleWindows(true)，
        // 所以打不开……主动设置成false就可以打开了
        //需要支持多窗体还需要重写WebChromeClient.onCreateWindow
        webSetting.setSupportMultipleWindows(false);
//        webSetting.setCacheMode(WebSettings.LOAD_NORMAL);
//        getSettingsExtension().setPageCacheCapacity(IX5WebSettings.DEFAULT_CACHE_CAPACITY);//extension

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && this.canGoBack()) {
            this.goBack(); // goBack()表示返回WebView的上一页面
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private WebChromeClient chromeClient = new WebChromeClient() {
        @Override
        public void onReceivedTitle(WebView view, String title) {
            if (tvTitle == null || TextUtils.isEmpty(title)) {
                return;
            }
            if (title != null && title.length() > MAX_LENGTH) {
                tvTitle.setText(title.subSequence(0, MAX_LENGTH) + "...");
            } else {
                tvTitle.setText(title);
            }
        }

        //监听进度
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            progressBar.setProgress(newProgress);
            if (progressBar != null && newProgress != 100) {
                progressBar.setVisibility(VISIBLE);
            } else if (progressBar != null) {
                progressBar.setVisibility(GONE);
//                imageView.setVisibility(GONE);
                dismissLoading();
            }
        }
    };

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        dismissLoading();
    }

    private WebViewClient client = new WebViewClient() {

        @Override
        public void onPageFinished(WebView webView, String url) {
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.setAcceptCookie(true);
            String endCookie = cookieManager.getCookie(url);
            Log.i(TAG, "onPageFinished: endCookie : " + endCookie);
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                CookieSyncManager.getInstance().sync();
            } else {
                CookieManager.getInstance().flush();
            }
            super.onPageFinished(webView, url);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url.startsWith("http") || url.startsWith("https") || url.startsWith("ftp")) {
                return false;
            } else {
                try {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(url));
                    view.getContext().startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(view.getContext(), "还没有安装支持打开此网页的应用！", Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        }

        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
            return super.shouldInterceptRequest(view, request);
        }

        @Override
        public void onLoadResource(WebView webView, String s) {
            super.onLoadResource(webView, s);
            String reUrl = webView.getUrl() + "";
            List<String> urlList = new ArrayList<>();
            urlList.add(reUrl);
            newList = new ArrayList<>();
            for (String cd : urlList) {
                if (!newList.contains(cd)) {
                    newList.add(cd);
                }
            }
        }


    };

    public void syncCookie(String url, String cookie) {
        CookieSyncManager.createInstance(getContext());
        if (!TextUtils.isEmpty(url)) {
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.setAcceptCookie(true);
            cookieManager.removeSessionCookie();// 移除
            cookieManager.removeAllCookie();

            //这里的拼接方式是伪代码
            String[] split = cookie.split(";");
            for (String string : split) {
                //为url设置cookie
                // ajax方式下  cookie后面的分号会丢失
                cookieManager.setCookie(url, string);
            }
            String newCookie = cookieManager.getCookie(url);
            Log.i(TAG, "syncCookie: newCookie == " + newCookie);
            //sdk21之后CookieSyncManager被抛弃了，换成了CookieManager来进行管理。
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                CookieSyncManager.getInstance().sync();//同步cookie
            } else {
                CookieManager.getInstance().flush();
            }
        } else {

        }

    }

    private void removeCookie() {
        CookieSyncManager.createInstance(getContext());
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeSessionCookie();
        cookieManager.removeAllCookie();
        if (Build.VERSION.SDK_INT < 21) {
            CookieSyncManager.getInstance().sync();
        } else {
            CookieManager.getInstance().flush();
        }
    }

    public String getDomain(String url) {
        String domain = "";
        int start = url.indexOf(".");
        if (start >= 0) {
            int end = url.indexOf("/", start);
            if (end < 0) {
                domain = url.substring(start);
            } else {
                domain = url.substring(start, end);
            }
        }
        return domain;
    }

    DownloadListener downloadListener = new DownloadListener() {
        @Override
        public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            getContext().startActivity(intent);
        }
    };
}