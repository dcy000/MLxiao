package com.ml.bci.game;

import android.content.Intent;
import android.database.*;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LeakThread leakThread = new LeakThread();
        leakThread.start();
    }

    public void helloOnClick(View view) {
        Intent intent = new Intent(this, BciGameActivity.class);
        startActivity(intent);
    }

    class LeakThread extends Thread {
        @Override
        public void run() {
            try {
                Thread.sleep(60 * 60 * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
