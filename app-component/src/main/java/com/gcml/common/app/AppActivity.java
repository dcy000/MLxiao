package com.gcml.common.app;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.gcml.common.component.R;


public class AppActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app);
        Singleton.getInstance(this);
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
            return;
        }
        super.onBackPressed();
    }

    public void onLeaks(View view) {
        Toast.makeText(this, "旋转屏幕试试", Toast.LENGTH_SHORT).show();
    }

    public void onBlocks(View view) {
        Toast.makeText(this, "点我就对了", Toast.LENGTH_SHORT).show();
        SystemClock.sleep(2000);
    }

    public void onJavaCrash(View view) {
        Toast.makeText(this, "10 秒中后去撩撩 Bugly 呗", Toast.LENGTH_SHORT).show();
    }

    public void onNdkCrash(View view) {
        Toast.makeText(this, "10 秒中后去撩撩 Bugly 呗", Toast.LENGTH_SHORT).show();
    }

    public void onAnr(View view) {
        Toast.makeText(this, "10 秒中后去撩撩 Bugly 呗", Toast.LENGTH_SHORT).show();
    }

}
