package com.example.han.referralproject.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.han.referralproject.MainActivity;
import com.example.han.referralproject.R;

public class BodychartActivity extends BaseActivity implements View.OnClickListener {


    private ImageView mIconBack;
    private LinearLayout mLinearlayou;
    private ImageView mIconHome;
    private ImageView mHead;
    private ImageView mChest;
    private ImageView mLeftHand;
    private ImageView mRightHand;
    private ImageView mAbdomen;
    private ImageView mLegs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bodychart);
        initView();
    }

    private void initView() {
        mIconBack = (ImageView) findViewById(R.id.icon_back);
        mIconBack.setOnClickListener(this);
        mLinearlayou = (LinearLayout) findViewById(R.id.linearlayou);
        mIconHome = (ImageView) findViewById(R.id.icon_home);
        mIconHome.setOnClickListener(this);
        mHead = (ImageView) findViewById(R.id.head);
        mHead.setOnClickListener(this);
        mChest = (ImageView) findViewById(R.id.chest);
        mChest.setOnClickListener(this);
        mLeftHand = (ImageView) findViewById(R.id.left_hand);
        mLeftHand.setOnClickListener(this);
        mRightHand = (ImageView) findViewById(R.id.right_hand);
        mAbdomen = (ImageView) findViewById(R.id.abdomen);
        mAbdomen.setOnClickListener(this);
        mLegs = (ImageView) findViewById(R.id.legs);
        mLegs.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.icon_back:
                finish();
                break;
            case R.id.icon_home:
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.head:
                startActivity(new Intent(this,SymptomAnalyseActivity.class)
                .putExtra("title","头部"));
                break;
            case R.id.chest:
                startActivity(new Intent(this,SymptomAnalyseActivity.class)
                        .putExtra("title","胸部"));
                break;
            case R.id.left_hand:
                startActivity(new Intent(this,SymptomAnalyseActivity.class)
                        .putExtra("title","四肢"));
                break;
            case R.id.abdomen:
                startActivity(new Intent(this,SymptomAnalyseActivity.class)
                        .putExtra("title","腹部"));
                break;
            case R.id.legs:
                startActivity(new Intent(this,SymptomAnalyseActivity.class)
                        .putExtra("title","四肢"));
                break;
        }
    }
}
