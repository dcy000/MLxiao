package com.gcml.health.measure;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.gcml.health.measure.first_diagnosis.HealthIntelligentDetectionActivity;
import com.gcml.health.measure.single_measure.MeasureChooseDeviceActivity;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void firstMeasure(View view) {
        startActivity(new Intent(this, HealthIntelligentDetectionActivity.class));
    }

    public void singleMeasure(View view) {
        startActivity(new Intent(this, MeasureChooseDeviceActivity.class));
    }
}
