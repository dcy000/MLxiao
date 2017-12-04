package com.example.han.referralproject.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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
        initView();
        speak(getResources().getString(R.string.bodyguide));
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
        mRightHand.setOnClickListener(this);
        mAbdomen = (ImageView) findViewById(R.id.abdomen);
        mAbdomen.setOnClickListener(this);
        mLegs = (ImageView) findViewById(R.id.legs);
        mLegs.setOnClickListener(this);
        mLinearlayou.setOnClickListener(this);
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
//                mHead.setSelected(true);
//                mChest.setSelected(false);
//                mAbdomen.setSelected(false);
//                mLeftHand.setSelected(false);
//                mRightHand.setSelected(false);
//                mLegs.setSelected(false);
                startActivity(new Intent(this, SymptomAnalyseActivity.class)
                        .putExtra("title", "头部"));
                break;
            case R.id.chest:
//                mHead.setSelected(false);
//                mChest.setSelected(true);
//                mAbdomen.setSelected(false);
//                mLeftHand.setSelected(false);
//                mRightHand.setSelected(false);
//                mLegs.setSelected(false);
                startActivity(new Intent(this, SymptomAnalyseActivity.class)
                        .putExtra("title", "胸部"));
                break;
            case R.id.left_hand:
//                mHead.setSelected(false);
//                mChest.setSelected(false);
//                mAbdomen.setSelected(false);
//                mLeftHand.setSelected(true);
//                mRightHand.setSelected(true);
//                mLegs.setSelected(true);

                startActivity(new Intent(this, SymptomAnalyseActivity.class)
                        .putExtra("title", "四肢"));
                break;
            case R.id.right_hand:
//                mHead.setSelected(false);
//                mChest.setSelected(false);
//                mAbdomen.setSelected(false);
//                mLeftHand.setSelected(true);
//                mRightHand.setSelected(true);
//                mLegs.setSelected(true);
                startActivity(new Intent(this, SymptomAnalyseActivity.class)
                        .putExtra("title", "四肢"));
                break;
            case R.id.abdomen:
//                mHead.setSelected(false);
//                mChest.setSelected(false);
//                mAbdomen.setSelected(true);
//                mLeftHand.setSelected(false);
//                mRightHand.setSelected(false);
//                mLegs.setSelected(false);
                startActivity(new Intent(this, SymptomAnalyseActivity.class)
                        .putExtra("title", "腹部"));
                break;
            case R.id.legs:
//                mHead.setSelected(false);
//                mChest.setSelected(false);
//                mAbdomen.setSelected(false);
//                mLeftHand.setSelected(true);
//                mRightHand.setSelected(true);
//                mLegs.setSelected(true);
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
        if(!TextUtils.isEmpty(result)){
            if(result.matches(".*(头|头部|脑袋).*")){
                startActivity(new Intent(this, SymptomAnalyseActivity.class)
                        .putExtra("title", "头部"));
                return;
            }
            if (result.matches(".*(胸部|胸|胸膛).*")){
                startActivity(new Intent(this, SymptomAnalyseActivity.class)
                        .putExtra("title", "胸部"));
                return;
            }
            if (result.matches(".*(腹部|肚子).*")){
                startActivity(new Intent(this, SymptomAnalyseActivity.class)
                        .putExtra("title", "腹部"));
                return;
            }
            if (result.matches(".*(四肢|手|脚|腿|胳膊|躯干).*")){

                startActivity(new Intent(this, SymptomAnalyseActivity.class)
                        .putExtra("title", "四肢"));
                return;
            }
        }
    }
}
