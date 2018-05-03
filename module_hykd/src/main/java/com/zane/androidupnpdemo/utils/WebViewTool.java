package com.zane.androidupnpdemo.utils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.gzq.administrator.lib_common.utils.UiUtils;
import com.zane.androidupnpdemo.R;

/**
 *
 * @author Vondear
 * @date 2017/4/1
 */

public class WebViewTool {
    private static WebView webView;
    private static WebSettings webSettings;
    private static Activity mContext;
    private static LottieAnimationView lottieAnimationView;

    public static void initWebView(final Activity context, final WebView webBase) {
        webView=webBase;
        webSettings = webView.getSettings();
        mContext=context;
        if (Build.VERSION.SDK_INT >= 19) {
            webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);//加载缓存否则网络
        }

        if (Build.VERSION.SDK_INT >= 19) {
            webSettings.setLoadsImagesAutomatically(true);//图片自动缩放 打开
        } else {
            webSettings.setLoadsImagesAutomatically(false);//图片自动缩放 关闭
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);//软件解码
        }
        webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);//硬件解码
        webSettings.setJavaScriptEnabled(true); // 设置支持javascript脚本
        webSettings.setSupportZoom(true);// 设置可以支持缩放
        webSettings.setBuiltInZoomControls(true);// 设置出现缩放工具 是否使用WebView内置的缩放组件，由浮动在窗口上的缩放控制和手势缩放控制组成，默认false

        webSettings.setDisplayZoomControls(false);//隐藏缩放工具
        webSettings.setUseWideViewPort(true);// 扩大比例的缩放

        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);//自适应屏幕
        webSettings.setLoadWithOverviewMode(true);

        webSettings.setDatabaseEnabled(true);//
        webSettings.setSavePassword(true);//保存密码
        webSettings.setDomStorageEnabled(true);//是否开启本地DOM存储  鉴于它的安全特性（任何人都能读取到它，尽管有相应的限制，将敏感数据存储在这里依然不是明智之举），Android 默认是关闭该功能的。
        webView.setSaveEnabled(true);
        webView.setKeepScreenOn(true);
        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                return super.onJsAlert(view, url, message, result);
            }

            //获取网页标题
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
            }

            //加载进度回调
            @Override
            public void onProgressChanged(WebView view, int newProgress) {

            }
        });

        //设置此方法可在WebView中打开链接，反之用浏览器打开
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                showLoadingView(false);
                if (!webView.getSettings().getLoadsImagesAutomatically()) {
                    webView.getSettings().setLoadsImagesAutomatically(true);
                }
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                // TODO Auto-generated method stub
                super.onPageStarted(view, url, favicon);
                showLoadingView(true);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                if (url.startsWith("http:") || url.startsWith("https:")) {
                    view.loadUrl(url);
                    return false;
                }
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                mContext.startActivity(intent);
                return true;
            }
        });
        webView.setDownloadListener(new DownloadListener() {
            public void onDownloadStart(String paramAnonymousString1, String paramAnonymousString2, String paramAnonymousString3, String paramAnonymousString4, long paramAnonymousLong) {
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                intent.setData(Uri.parse(paramAnonymousString1));
                mContext.startActivity(intent);
            }
        });
    }

    public static void loadData(WebView webView, String url) {
        webView.loadUrl(url);

    }
    public static void destroy(){
        webSettings=null;
        webView=null;
        mContext=null;
    }
    private static void showLoadingView(boolean isShow){
        AlertDialog alertDialog = null;
        FrameLayout parentView = mContext.findViewById(android.R.id.content);
        if (isShow) {
            lottieAnimationView = new LottieAnimationView(mContext);
            lottieAnimationView.setAnimation("loading_data.json");
            lottieAnimationView.loop(true);
            lottieAnimationView.playAnimation();
            FrameLayout.LayoutParams params=new FrameLayout.LayoutParams(UiUtils.pt(200),UiUtils.pt(200));
            params.gravity= Gravity.CENTER;
            parentView.addView(lottieAnimationView,params);
            parentView.bringChildToFront(lottieAnimationView);

//            AlertDialog.Builder builder = new AlertDialog.Builder(mContext,R.style.NoBackGroundDialog);
//            View inflate = LayoutInflater.from(mContext).inflate(R.layout.hy_loading_view, null, false);
//            builder.setView(inflate);
//            alertDialog = builder.create();
//            alertDialog.setCanceledOnTouchOutside(false);
//            alertDialog.show();
        }else{
            if (lottieAnimationView!=null){
                parentView.removeView(lottieAnimationView);
                lottieAnimationView.cancelAnimation();
            }
        }
    }
}
