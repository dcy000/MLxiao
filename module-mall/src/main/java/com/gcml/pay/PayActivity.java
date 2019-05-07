package com.gcml.pay;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.gcml.common.utils.UM;
import com.gcml.common.utils.base.ToolbarBaseActivity;
import com.gcml.mall.R;
import com.iflytek.synthetize.MLVoiceSynthetize;
import com.sjtu.yifei.annotation.Route;

@Route(path = "/app/pay/money/activity")
public class PayActivity extends ToolbarBaseActivity implements View.OnClickListener {

    public Button mButton1;
    public Button mButton2;
    public Button mButton3;
    public Button mButton4;
    public Button mButton5;
    public Button mButton6;

    //public ImageView mImageView1;
    //public ImageView mImageView2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);

        MLVoiceSynthetize.startSynthesize(UM.getApp(),getString(R.string.chongzhi));

        mToolbar.setVisibility(View.VISIBLE);

        mTitleText.setText(getString(R.string.pay));

        mButton1 = findViewById(R.id.pay_1);
        mButton2 = findViewById(R.id.pay_2);
        mButton3 = findViewById(R.id.pay_3);
        mButton4 = findViewById(R.id.pay_4);
        mButton5 = findViewById(R.id.pay_5);
        mButton6 = findViewById(R.id.pay_6);

       /* mImageView1 = (ImageView) findViewById(R.id.health_record_icon_back);
        mImageView2 = (ImageView) findViewById(R.id.health_record_icon_home);
*/
        mButton1.setOnClickListener(this);
        mButton2.setOnClickListener(this);
        mButton3.setOnClickListener(this);
        mButton4.setOnClickListener(this);
        mButton5.setOnClickListener(this);
        mButton6.setOnClickListener(this);

      /*  mImageView1.setOnClickListener(this);
        mImageView2.setOnClickListener(this);*/
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


           /* case R.id.health_record_icon_back:
                finish();
                break;
            case R.id.health_record_icon_home:
                Intent intents = new Intent(getApplicationContext(), MainActivity.class);
                startActivityForResult(intents);
                finish();
                break;*/
        }

    }
}
