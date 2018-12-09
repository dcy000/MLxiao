package com.example.module_pay.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.module_pay.R;
import com.gcml.lib_widget.ToolbarBaseActivity;
import com.gzq.lib_core.base.ui.BasePresenter;
import com.gzq.lib_core.base.ui.IPresenter;
import com.iflytek.synthetize.MLVoiceSynthetize;

public class PayActivity extends ToolbarBaseActivity implements View.OnClickListener {

    public Button mButton1;
    public Button mButton2;
    public Button mButton3;
    public Button mButton4;
    public Button mButton5;
    public Button mButton6;

    @Override
    public int layoutId(Bundle savedInstanceState) {
        return R.layout.activity_pay;
    }

    @Override
    public void initParams(Intent intentArgument) {
        MLVoiceSynthetize.startSynthesize(R.string.chongzhi);
    }

    @Override
    public void initView() {
        mTitleText.setText(getString(R.string.pay));
        mButton1 = (Button) findViewById(R.id.pay_1);
        mButton2 = (Button) findViewById(R.id.pay_2);
        mButton3 = (Button) findViewById(R.id.pay_3);
        mButton4 = (Button) findViewById(R.id.pay_4);
        mButton5 = (Button) findViewById(R.id.pay_5);
        mButton6 = (Button) findViewById(R.id.pay_6);
        mButton1.setOnClickListener(this);
        mButton2.setOnClickListener(this);
        mButton3.setOnClickListener(this);
        mButton4.setOnClickListener(this);
        mButton5.setOnClickListener(this);
        mButton6.setOnClickListener(this);

    }

    @Override
    public IPresenter obtainPresenter() {
        return new BasePresenter(this) {};
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        Intent intent = new Intent(getApplicationContext(), PayInfoActivity.class);
        int i = view.getId();
        if (i == R.id.pay_1) {
            intent.putExtra("number", "5000");
            startActivity(intent);

        } else if (i == R.id.pay_2) {
            intent.putExtra("number", "10000");
            startActivity(intent);


        } else if (i == R.id.pay_3) {
            intent.putExtra("number", "20000");

            startActivity(intent);


        } else if (i == R.id.pay_4) {
            intent.putExtra("number", "50000");

            startActivity(intent);


        } else if (i == R.id.pay_5) {
            intent.putExtra("number", "100000");

            startActivity(intent);


        } else if (i == R.id.pay_6) {
            Intent inten = new Intent(getApplicationContext(), DefineActivity.class);
            startActivity(inten);
        }

    }
}
