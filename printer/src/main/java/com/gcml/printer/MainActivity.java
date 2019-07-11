package com.gcml.printer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.gcml.common.router.AppRouter;
import com.gcml.common.utils.UM;
import com.gcml.common.utils.ui.UiUtils;
import com.gcml.lib_printer_8003dd.ConnectPrinterHelper;
import com.gcml.lib_printer_8003dd.IPrinterView;
import com.sjtu.yifei.route.Routerfit;

import java.io.UnsupportedEncodingException;

public class MainActivity extends AppCompatActivity implements IPrinterView {

    private ConnectPrinterHelper printerHelper;
    private TextView mTvView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        UM.init(getApplication());
        Routerfit.init(getApplication());
        UiUtils.init(this,1920,1200);
        initView();
        printerHelper = new ConnectPrinterHelper(this);
    }

    public void onConnect(View view) {
//        printerHelper.start();
        Routerfit.register(AppRouter.class).skipChooseDevicesActivity();
    }

    @Override
    public void updateState(String state) {
        mTvView.append(state + "\n");
    }

    private void initView() {
        mTvView = (TextView) findViewById(R.id.tv_view);
    }

    public void onPrint(View view) {
        if (!printerHelper.isConnected()) return;
        printerHelper.initPrinter();
        try {
            printerHelper.printTitle("健康智能管家检测数据单\n\n\n");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public void onDis(View view) {
        printerHelper.onStop();
    }

    public void unbind(View view) {
//        BluetoothUnpairUtils.unpairDevice();
    }

    public void onFinish(View view) {
        finish();
    }
}
