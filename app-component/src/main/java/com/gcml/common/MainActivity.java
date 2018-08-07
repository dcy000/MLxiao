package com.gcml.common;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.billy.cc.core.component.CC;
import com.gcml.common.app.AppActivity;
import com.gcml.common.demo.R;
import com.gcml.common.repository.RepositoryActivity;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onApp(View view) {
        Intent intent = new Intent(this, AppActivity.class);
        startActivity(intent);
    }

    public void onRepository(View view) {
        Intent intent = new Intent(this, RepositoryActivity.class);
        startActivity(intent);
    }

    public void onComponentCall(View view) {
        CC.obtainBuilder("app.component.cc").build().callAsync();
    }
}
