package com.example.han.referralproject.measure;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.han.referralproject.R;
import com.example.han.referralproject.measure.fragment.MeasureXuetangFragment;

/**
 * Created by gzq on 2018/2/8.
 */

public class TestActivity extends AppCompatActivity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_ex);
        MeasureXuetangFragment warningFragme=new MeasureXuetangFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.container,warningFragme).commit();
    }
}
