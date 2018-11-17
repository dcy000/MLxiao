package com.example.han.referralproject.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.example.han.referralproject.R;
import com.iflytek.cloud.SpeechError;
import com.iflytek.recognition.MLRecognizerListener;
import com.iflytek.recognition.MLVoiceRecognize;
import com.iflytek.synthetize.MLVoiceSynthetize;

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
        MLVoiceSynthetize.startSynthesize(getString(R.string.bodyguide));
        listener();
    }

    private void listener() {
        MLVoiceRecognize.startRecognize(new MLRecognizerListener() {
            @Override
            public void onMLVolumeChanged(int i, byte[] bytes) {

            }

            @Override
            public void onMLBeginOfSpeech() {

            }

            @Override
            public void onMLEndOfSpeech() {

            }

            @Override
            public void onMLResult(String result) {
                if (!TextUtils.isEmpty(result)) {
                    if (result.matches(".*(头|头部|脑袋).*")) {
                        startActivity(new Intent(BodychartActivity.this, SymptomAnalyseActivity.class)
                                .putExtra("title", "头部"));
                        return;
                    }
                    if (result.matches(".*(胸部|胸|胸膛).*")) {
                        startActivity(new Intent(BodychartActivity.this, SymptomAnalyseActivity.class)
                                .putExtra("title", "胸部"));
                        return;
                    }
                    if (result.matches(".*(腹部|肚子).*")) {
                        startActivity(new Intent(BodychartActivity.this, SymptomAnalyseActivity.class)
                                .putExtra("title", "腹部"));
                        return;
                    }
                    if (result.matches(".*(四肢|手|脚|腿|胳膊|躯干).*")) {

                        startActivity(new Intent(BodychartActivity.this, SymptomAnalyseActivity.class)
                                .putExtra("title", "四肢"));
                        return;
                    }
                }
            }

            @Override
            public void onMLError(SpeechError error) {

            }
        });
    }

    private void initView() {
        mHead = (ImageView) findViewById(R.id.head);
        mHead.setOnClickListener(this);
        mChest = (ImageView) findViewById(R.id.chest);
        mChest.setOnClickListener(this);
        mLeftHand = (ImageView) findViewById(R.id.left_hand);
        mLeftHand.setOnClickListener(this);
        mRightHand = (ImageView) findViewById(R.id.right_hand);
        mRightHand.setOnClickListener(this);
        mAbdomen = (ImageView) findViewById(R.id.abdomen);
        mAbdomen.setOnClickListener(this);
        mLegs = (ImageView) findViewById(R.id.legs);
        mLegs.setOnClickListener(this);
        mImgHead = (ImageView) findViewById(R.id.imgHead);
        mViewHead = (View) findViewById(R.id.viewHead);
        mViewHead.setOnClickListener(this);
        mImgAbdomen = (ImageView) findViewById(R.id.imgAbdomen);
        mViewAbdomen = (View) findViewById(R.id.viewAbdomen);
        mViewAbdomen.setOnClickListener(this);
        mImgChest = (ImageView) findViewById(R.id.imgChest);
        mViewChest = (View) findViewById(R.id.viewChest);
        mViewChest.setOnClickListener(this);
        mImgLegs = (ImageView) findViewById(R.id.imgLegs);
        mViewLegs = (View) findViewById(R.id.viewLegs);
        mViewLegs.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
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
}
