package com.ml.edu;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class OldMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_old);
    }

    public void onOld(View view) {
        OldRouter.routeToOldHomeActivity(this);
    }

    public void onChild(View view) {

    }
}
