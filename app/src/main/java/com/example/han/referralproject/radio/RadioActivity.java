package com.example.han.referralproject.radio;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.han.referralproject.R;
import com.example.han.referralproject.measure.fragment.MeasureXuetangFragment;

public class RadioActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_radio);
        MeasureXuetangFragment measureXuetangFragment=new MeasureXuetangFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.container,measureXuetangFragment).commit();
    }
}
