package com.gcml.common;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import com.gcml.common.component.R;
import com.gcml.module_blutooth_devices.bloodoxygen.OxygenFragment;

public class TestNewBluetoothActivity extends AppCompatActivity {
    private OxygenFragment oxygenFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_new_bluetooth);
        oxygenFragment = new OxygenFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.frame, oxygenFragment).commit();
    }

    public void showDialog(View view) {
        if (oxygenFragment != null) {
            oxygenFragment.showBluetoothDialog();
        }
    }
}
