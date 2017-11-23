package com.example.han.referralproject.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.han.referralproject.MainActivity;
import com.example.han.referralproject.R;
import com.example.han.referralproject.adapter.SymptomRecyclerAdapter;
import com.example.han.referralproject.bean.SymptomBean;
import com.example.han.referralproject.bean.SymptomResultBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gzq on 2017/11/23.
 */

public class SecondSymptomAnalyseActivity extends BaseActivity implements View.OnClickListener {
    private static String TAG="SecondSymptomAnalyseActivity";

    private ImageView mIconBack;
    private LinearLayout mLinearlayou;
    private ImageView mIconHome;
    /**
     * 提交
     */
    private TextView mBtnAnalyse;
    private LinearLayout mLlBottom;
    private RecyclerView mRvSymptom;

    private SymptomResultBean secondResult;
    private SymptomBean choose_one;//第一层选中的标签
    private ArrayList<SymptomBean> mList;
    private SymptomRecyclerAdapter mSymptomAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_symptom_analyse);
        initView();
        secondResult = (SymptomResultBean) getIntent().getSerializableExtra("result");
        choose_one = (SymptomBean) getIntent().getSerializableExtra("choose_one");
        Log.e(TAG,secondResult.toString());
        Log.e(TAG,choose_one.toString());
        //为了通用SymptomRecyclerAdapter，需要把zzs对象转成SymptomBean对象
        mList=new ArrayList<>();
        List<SymptomResultBean.zzs> zzss=secondResult.getZzs();
        for(int i=0;i<zzss.size();i++){
            mList.add(new SymptomBean(zzss.get(i).getId(),zzss.get(i).getName()));
        }
        setAdapter();

    }

    private void setAdapter() {
        mRvSymptom.setLayoutManager(new GridLayoutManager(this,3));
        mSymptomAdapter = new SymptomRecyclerAdapter(mContext, mList);
        mRvSymptom.setAdapter(mSymptomAdapter);
    }

    private void initView() {
        mIconBack = (ImageView) findViewById(R.id.icon_back);
        mIconBack.setOnClickListener(this);
        mLinearlayou = (LinearLayout) findViewById(R.id.linearlayou);
        mIconHome = (ImageView) findViewById(R.id.icon_home);
        mIconHome.setOnClickListener(this);
        mBtnAnalyse = (TextView) findViewById(R.id.btn_analyse);
        mLlBottom = (LinearLayout) findViewById(R.id.ll_bottom);
        mRvSymptom = (RecyclerView) findViewById(R.id.rv_symptom);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.icon_back://返回键
                finish();
                break;
            case R.id.icon_home://回退到主页面
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }
}
