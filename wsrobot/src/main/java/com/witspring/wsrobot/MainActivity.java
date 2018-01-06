package com.witspring.wsrobot;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.witspring.wsrobot.databinding.WsActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private WsActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.ws_activity_main);
        binding = DataBindingUtil.setContentView(this, R.layout.ws_activity_main);
    }
}
