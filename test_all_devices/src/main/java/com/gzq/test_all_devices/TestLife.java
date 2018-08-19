package com.gzq.test_all_devices;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.gzq.test_all_devices.net.ILife;

/**
 * Created by gzq on 2018/8/19.
 */

public class TestLife extends AppCompatActivity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_measure);
        ILife iLife = new ILife();
        getLifecycle().addObserver(iLife);
    }
}
