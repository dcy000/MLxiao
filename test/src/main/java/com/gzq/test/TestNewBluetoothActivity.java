package com.gzq.test;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.gcml.module_blutooth_devices.bloodoxygen.BloodOxygenFragment;
import com.gcml.module_health_profile.HealthProfileActivity;

public class TestNewBluetoothActivity extends AppCompatActivity {
    private BloodOxygenFragment oxygenFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_new_bluetooth);
//        oxygenFragment = new BloodOxygenFragment();
//        getSupportFragmentManager().beginTransaction().replace(R.id.frame, oxygenFragment).commit();
    }

    public void showDialog(View view) {
//        if (oxygenFragment != null) {
//            oxygenFragment.showBluetoothDialog();
//        }
        startActivity(new Intent(this, HealthProfileActivity.class));
    }
}
