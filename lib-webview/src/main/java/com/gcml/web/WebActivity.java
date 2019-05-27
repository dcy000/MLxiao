package com.gcml.web;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.gcml.web.widget.X5WebView;

public class WebActivity extends AppCompatActivity {

    private X5WebView webView;
    private Button btnBack;

    public static final String URL_DIAGNOSIS = "https://robot-lib-achieve.zuoshouyisheng.com/?app_id=5cc197e8b60c48171066f0e7";
    public static final String URL_MEDICAL = "https://robot-lib-achieve.zuoshouyisheng.com/?app_id=5cd3d5cbb60c48343fafe493";

    public static void start(Context context, String url) {
        Intent intent = new Intent(context, WebActivity.class);
        intent.putExtra("url", url);
        context.startActivity(intent);
    }

    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        enableFullScreen(true);
        url = getIntent().getStringExtra("url");

        initView();
        if (url != null && !url.isEmpty()) {
            webView.loadUrl(url);
        } else {
            webView.loadUrl(URL_DIAGNOSIS);
        }

        TbsInitHelper.setListener(new TbsInitHelper.Listener() {
            @Override
            public void onInitComplete() {
            }

            @Override
            public void onInitError() {
                Toast.makeText(WebActivity.this.getApplicationContext(), "error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initView() {
        setContentView(R.layout.activity_web);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        webView = findViewById(R.id.x5WebView);

        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void enableFullScreen(boolean enable) {
        if (enable) {
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
            getWindow().setAttributes(lp);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        } else {
            WindowManager.LayoutParams attr = getWindow().getAttributes();
            attr.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getWindow().setAttributes(attr);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
    }

    @Override
    protected void onDestroy() {
        TbsInitHelper.setListener(null);
        super.onDestroy();
    }
}
