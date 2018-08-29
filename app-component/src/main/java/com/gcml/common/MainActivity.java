package com.gcml.common;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.billy.cc.core.component.CC;
import com.billy.cc.core.component.CCResult;
import com.billy.cc.core.component.IComponentCallback;
import com.gcml.common.app.AppActivity;
import com.gcml.common.demo.R;
import com.gcml.common.dialog.DialogActivity;
import com.gcml.common.mvvm.MvvmActivity;
import com.gcml.common.repository.RepositoryActivity;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onApp(View view) {
        Intent intent = new Intent(this, AppActivity.class);
        startActivity(intent);
    }

    public void onRepository(View view) {
        Intent intent = new Intent(this, RepositoryActivity.class);
        startActivity(intent);
    }

    public void onDialog(View view) {
        Intent intent = new Intent(this, DialogActivity.class);
        startActivity(intent);
    }

    public void onComponentCall(View view) {
        CC.obtainBuilder("app.component.voice").build().callAsync();
//        CC.obtainBuilder("face_recognition").setActionName("To_RegisterHead2XunfeiActivity")
//                .addParam("xx","dd")
//                .build().callAsyncCallbackOnMainThread(new IComponentCallback() {
//            @Override
//            public void onResult(CC cc, CCResult result) {
//                Object key_xfid = result.getDataItem("key_xfid");
//
//
//            }
//        });
    }

    public void onRegisterHead2XunfeiActivity(View view) {
        CC.obtainBuilder("face_recognition")
                .setActionName("To_RegisterHead2XunfeiActivity")
                .addParam("key_xfid", "1234567anlcm")
                .build().callAsyncCallbackOnMainThread(new IComponentCallback() {
            @Override
            public void onResult(CC cc, CCResult result) {
                String keyCcCallback = result.getDataItem("key_cc_callback");
                switch (keyCcCallback) {
                    case "pressedBackButton":
                        Intent intent = new Intent(MainActivity.this,
                                RepositoryActivity.class);
                        startActivity(intent);
                        break;
                }
            }
        });
        CC.obtainBuilder("app.component.cc").build().callAsync();
    }

    public void onMvvm(View view) {
        Intent intent = new Intent(this, MvvmActivity.class);
        startActivity(intent);
    }
}
