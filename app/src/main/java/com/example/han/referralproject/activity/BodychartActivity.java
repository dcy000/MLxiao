package com.example.han.referralproject.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.example.han.referralproject.R;

import butterknife.ButterKnife;

public class BodychartActivity extends BaseActivity implements View.OnClickListener {
    private ImageView mHead;
    private ImageView mChest;
    private ImageView mLeftHand;
    private ImageView mRightHand;
    private ImageView mAbdomen;
    private ImageView mLegs;
    private ImageView mImgHead;
    private View mViewHead;
    private ImageView mImgAbdomen;
    private View mViewAbdomen;
    private ImageView mImgChest;
    private View mViewChest;
    private ImageView mImgLegs;
    private View mViewLegs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bodychart);
        ButterKnife.bind(this);
        mToolbar.setVisibility(View.VISIBLE);
        mTitleText.setText(R.string.body_guide);
        initView();
        speak(getResources().getString(R.string.bodyguide));
    }

    private void initView() {
        mHead = findViewById(R.id.head);
        mHead.setOnClickListener(this);
        mChest = findViewById(R.id.chest);
        mChest.setOnClickListener(this);
        mLeftHand = findViewById(R.id.left_hand);
        mLeftHand.setOnClickListener(this);
        mRightHand = findViewById(R.id.right_hand);
        mRightHand.setOnClickListener(this);
        mAbdomen = findViewById(R.id.abdomen);
        mAbdomen.setOnClickListener(this);
        mLegs = findViewById(R.id.legs);
        mLegs.setOnClickListener(this);
        mImgHead = findViewById(R.id.imgHead);
        mViewHead = findViewById(R.id.viewHead);
        mViewHead.setOnClickListener(this);
        mImgAbdomen = findViewById(R.id.imgAbdomen);
        mViewAbdomen = findViewById(R.id.viewAbdomen);
        mViewAbdomen.setOnClickListener(this);
        mImgChest = findViewById(R.id.imgChest);
        mViewChest = findViewById(R.id.viewChest);
        mViewChest.setOnClickListener(this);
        mImgLegs = findViewById(R.id.imgLegs);
        mViewLegs = findViewById(R.id.viewLegs);
        mViewLegs.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head:
                startActivity(new Intent(this, SymptomAnalyseActivity.class)
                        .putExtra("title", "头部"));
                break;
            case R.id.chest:
                startActivity(new Intent(this, SymptomAnalyseActivity.class)
                        .putExtra("title", "胸部"));
                break;
            case R.id.left_hand:
                startActivity(new Intent(this, SymptomAnalyseActivity.class)
                        .putExtra("title", "四肢"));
                break;
            case R.id.right_hand:
                startActivity(new Intent(this, SymptomAnalyseActivity.class)
                        .putExtra("title", "四肢"));
                break;
            case R.id.abdomen:
                startActivity(new Intent(this, SymptomAnalyseActivity.class)
                        .putExtra("title", "腹部"));
                break;
            case R.id.legs:
                startActivity(new Intent(this, SymptomAnalyseActivity.class)
                        .putExtra("title", "四肢"));
                break;
            case R.id.linearlayou:
                break;
            case R.id.viewHead:
                mImgHead.setImageDrawable(getResources().getDrawable(R.drawable.img_head_click));
                startActivity(new Intent(this, SymptomAnalyseActivity.class)
                        .putExtra("title", "头部"));
                break;
            case R.id.viewAbdomen:
                mImgAbdomen.setImageDrawable(getResources().getDrawable(R.drawable.img_abdomen_click));
                startActivity(new Intent(this, SymptomAnalyseActivity.class)
                        .putExtra("title", "腹部"));
                break;
            case R.id.viewChest:
                mImgChest.setImageDrawable(getResources().getDrawable(R.drawable.img_chest_click));
                startActivity(new Intent(this, SymptomAnalyseActivity.class)
                        .putExtra("title", "胸部"));
                break;
            case R.id.viewLegs:
                mImgLegs.setImageDrawable(getResources().getDrawable(R.drawable.img_all_fours_click));
                startActivity(new Intent(this, SymptomAnalyseActivity.class)
                        .putExtra("title", "四肢"));
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mImgHead.setImageDrawable(getResources().getDrawable(R.drawable.img_head));
        mImgAbdomen.setImageDrawable(getResources().getDrawable(R.drawable.img_abdomen));
        mImgChest.setImageDrawable(getResources().getDrawable(R.drawable.img_chest));
        mImgLegs.setImageDrawable(getResources().getDrawable(R.drawable.img_all_fours));
    }

    @Override
    protected void onSpeakListenerResult(String result) {
        if (!TextUtils.isEmpty(result)) {
            if (result.matches(".*(头|头部|脑袋).*")) {
                startActivity(new Intent(this, SymptomAnalyseActivity.class)
                        .putExtra("title", "头部"));
                return;
            }
            if (result.matches(".*(胸部|胸|胸膛).*")) {
                startActivity(new Intent(this, SymptomAnalyseActivity.class)
                        .putExtra("title", "胸部"));
                return;
            }
            if (result.matches(".*(腹部|肚子).*")) {
                startActivity(new Intent(this, SymptomAnalyseActivity.class)
                        .putExtra("title", "腹部"));
                return;
            }
            if (result.matches(".*(四肢|手|脚|腿|胳膊|躯干).*")) {

                startActivity(new Intent(this, SymptomAnalyseActivity.class)
                        .putExtra("title", "四肢"));
                return;
            }
        }
    }
}
