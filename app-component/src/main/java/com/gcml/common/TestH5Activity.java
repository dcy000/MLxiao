package com.gcml.common;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

public class TestH5Activity extends AppCompatActivity{

    private WebView web;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_h5);
        web = findViewById(R.id.web);
        web.loadUrl("http://47.96.98.60:8620/");
    }
}
