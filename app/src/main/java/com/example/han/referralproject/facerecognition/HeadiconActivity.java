package com.example.han.referralproject.facerecognition;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.imageview.CircleImageView;
import com.example.han.referralproject.recyclerview.RecoDocActivity;
import com.example.han.referralproject.speechsynthesis.PinYinUtils;
import com.example.han.referralproject.util.LocalShared;

public class HeadiconActivity extends BaseActivity {

    ImageView mCircleImageView;
    Button mButton;
    Button mButton1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_headicon);
        setDisableGlobalListen(true);
        mCircleImageView = (CircleImageView) findViewById(R.id.per_image);

        mButton = (Button) findViewById(R.id.trues);
        mButton1 = (Button) findViewById(R.id.cancel);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RecoDocActivity.class);
                startActivity(intent);
                finish();
            }
        });


        mButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RegisterVideoActivity.class);
                startActivity(intent);
                finish();
            }
        });


        String imageData1 = LocalShared.getInstance(getApplicationContext()).getUserImg();

        if (imageData1 != null) {
            byte[] bytes = Base64.decode(imageData1.getBytes(), 1);
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            mCircleImageView.setImageBitmap(bitmap);
        }

        speak(R.string.head_icon);

    }

    @Override
    protected void onResume() {
        super.onResume();
        startListening();
    }

    @Override
    protected void onSpeakListenerResult(String result) {
        Toast.makeText(mContext, result, Toast.LENGTH_SHORT).show();
        String inSpell = PinYinUtils.converterToSpell(result);

        if (inSpell.matches(".*(queding|wancheng|xiayibu).*")) {
            mButton.performClick();
            return;
        }
        if (inSpell.matches(".*(quxiao|chongxin|zhongxin|zhongpai|zaipai|chongpai|zhongpai).*")) {
            mButton1.performClick();
        }
    }
}
