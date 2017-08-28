package com.example.han.referralproject.xueya;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.han.referralproject.R;

public class AttentionActivity extends AppCompatActivity {

    ImageView mImageView1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attention);

    }

    @Override
    protected void onStart() {
        super.onStart();

        mImageView1 = (ImageView) findViewById(R.id.start_test);
        mImageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),XueyaActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
