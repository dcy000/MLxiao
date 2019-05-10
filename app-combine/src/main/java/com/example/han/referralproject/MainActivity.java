package com.example.han.referralproject;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.gcml.common.service.ShowStateBar;
import com.sjtu.yifei.annotation.Route;

@Route(path = "/app/homepage/main/activity")
public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StatusBarFragment.show(getSupportFragmentManager(), R.id.fl_status_bar);
    }

    private ShowStateBar showStateBar;

    public void setShowStateBarListener(ShowStateBar showStateBar) {
        this.showStateBar = showStateBar;
    }

}
