package com.gcml.family.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.billy.cc.core.component.CC;
import com.gcml.family.R;
import com.gcml.family.widget.FamilyMenuPopup;
import com.iflytek.synthetize.MLVoiceSynthetize;

/**
 * desc: 拨号中心任务页面 .
 * author: wecent .
 * date: 2018/8/20 .
 */

public class FamilyActivity extends BaseFragmentActivity implements View.OnClickListener {

    RelativeLayout mLeftLay, mRightLay;
    RadioGroup mRadioGroup;
    FragmentManager fragmentManager;
    FamilyRelativeFragment relativeFragment;
    FamilyFriendFragment friendFragment;
    Fragment[] mFragments;
    int mIndex;
    String startType;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_family);
        startType = getIntent().getStringExtra("startType");

        bindView();
        bindData();
        bindFragment();
    }

    private void bindView() {
        mLeftLay = findViewById(R.id.rl_family_left);
        mRightLay = findViewById(R.id.rl_family_right);
        mRadioGroup = findViewById(R.id.rg_family);
        mLeftLay.setOnClickListener(this);
        mRightLay.setOnClickListener(this);
    }

    private void bindData() {
        MLVoiceSynthetize.startSynthesize(getApplicationContext(), "主人，欢迎来到拨号中心。", false);
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int arg1) {
                //遍历RadioGroup 里面所有的子控件。
                for (int index = 0; index < group.getChildCount(); index++) {
                    //获取到指定位置的RadioButton
                    RadioButton rb = (RadioButton)group.getChildAt(index);
                    //如果被选中
                    if (rb.isChecked()) {
                        setIndexSelected(index);
                        break;
                    }
                }

            }
        });
    }

    private void bindFragment() {
        relativeFragment =new FamilyRelativeFragment();
        friendFragment =new FamilyFriendFragment();
        //添加到数组
        mFragments = new Fragment[]{relativeFragment, friendFragment};
        //开启事务
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft= fragmentManager.beginTransaction();
        //添加首页
        ft.add(R.id.fl_family, relativeFragment).commit();
        //默认设置为第0个
        setIndexSelected(0);
    }

    private void setIndexSelected(int index) {

        if(mIndex == index){
            return;
        }
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction= fragmentManager.beginTransaction();

        //隐藏
        fragmentTransaction.hide(mFragments[mIndex]);
        //判断是否添加
        if(!mFragments[index].isAdded()){
            fragmentTransaction.add(R.id.fl_family, mFragments[index]).show(mFragments[index]);
        }else {
            fragmentTransaction.show(mFragments[index]);
        }

        fragmentTransaction.commit();
        //再次赋值
        mIndex = index;

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.rl_family_left) {
//            if (startType.equals("MLMain")) {
//                CC.obtainBuilder("app").setActionName("ToMainActivity").build().callAsync();
//                finish();
//            } else if (startType.equals("MLSpeech")) {
                finish();
//            }
        } else if (v.getId() == R.id.rl_family_right) {
            FamilyMenuPopup familyMenu = new FamilyMenuPopup(FamilyActivity.this);
            familyMenu.showPopupWindow();
            if (mIndex == 0) {
                familyMenu.setPopupText("添加家人", "消息中心");
            } else {
                familyMenu.setPopupText("添加好友", "消息中心");
            }
            familyMenu.setOnSelectListener(new FamilyMenuPopup.OnSelectListener() {
                @Override
                public void onSelected(int position) {
                    if (position == 0) {
                        CC.obtainBuilder("app.component.family.search").addParam("startType", position).build().callAsync();
                    } else if (position == 1) {
                        CC.obtainBuilder("app.component.family.news").build().callAsync();
                    }
                }
            });
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        MLVoiceSynthetize.stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MLVoiceSynthetize.destory();
    }
}
