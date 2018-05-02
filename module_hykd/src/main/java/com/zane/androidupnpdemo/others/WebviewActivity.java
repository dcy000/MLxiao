package com.zane.androidupnpdemo.others;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebView;

import com.gzq.administrator.lib_common.base.CommonBaseActivity;
import com.zane.androidupnpdemo.R;
import com.zane.androidupnpdemo.utils.WebViewTool;

/**
 * Created by Administrator on 2018/4/28.
 */

public class WebviewActivity extends CommonBaseActivity {
    private WebView mHyWeb;
    private String url;
    private String title;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hy_activity_webview);
        initView();

    }
    private void initView() {
        title=getIntent().getStringExtra("title");
        mTitleText.setText(TextUtils.isEmpty(title)?"":title);
        url=getIntent().getStringExtra("url");
        mHyWeb = (WebView) findViewById(R.id.hy_web);
        WebViewTool.loadData(mHyWeb,url);
        WebViewTool.initWebView(this,mHyWeb);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        WebViewTool.destroy();
    }

    @Override
    protected void backLastActivity() {
        if (mHyWeb.canGoBack()) {
            mHyWeb.goBack();
        }else{
            finish();
        }
    }
}
