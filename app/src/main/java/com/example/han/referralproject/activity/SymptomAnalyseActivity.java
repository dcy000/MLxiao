package com.example.han.referralproject.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.han.referralproject.MainActivity;
import com.example.han.referralproject.R;
import com.example.han.referralproject.adapter.SymptomRecyclerAdapter;
import com.example.han.referralproject.bean.SymptomBean;
import com.example.han.referralproject.bean.SymptomResultBean;
import com.example.han.referralproject.music.ToastUtils;
import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.network.NetworkManager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 症状自查页面1
 */
public class SymptomAnalyseActivity extends BaseActivity implements View.OnClickListener {
    private RecyclerView mRecyclerView;
    private ArrayList<SymptomBean> mDataList = new ArrayList<>();
    private SymptomRecyclerAdapter mSymptomAdapter;

    private int flag = 0;//用于记录是第几层病症选择
    private LinearLayout llLeft;
    private TextView tv1, tv2, tv3;//选择的病症标签
    private TextView nextNotice;//下一层病症的提示
    private String buffer;//记录选择的标签
    private RelativeLayout rlTv1, rlTv2, rlTv3;
    private String b_1 = "", b_2 = "", b_3 = "";//记录每一层选择的病症id；
    private boolean isFirst = true;//防止快速点击
    private String title;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_symptom_analyse);
        mToolbar.setVisibility(View.VISIBLE);
        mTitleText.setText("症状自查");
        initView();

        mSymptomAdapter = new SymptomRecyclerAdapter(mContext, mDataList);
        mRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 3));
        mRecyclerView.setAdapter(mSymptomAdapter);


        //获取第一层的病症
        NetworkApi.getAllSym(mGetAllSymCallback);

        setTag();
    }

    private void initView() {
        title = getIntent().getStringExtra("title");
        llLeft = (LinearLayout) findViewById(R.id.ll_left);
        tv1 = (TextView) findViewById(R.id.tv_1);
        tv2 = (TextView) findViewById(R.id.tv_2);
        tv3 = (TextView) findViewById(R.id.tv_3);
        nextNotice = (TextView) findViewById(R.id.next_notice);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_symptom);
        rlTv1 = (RelativeLayout) findViewById(R.id.rl_tv1);
        rlTv2 = (RelativeLayout) findViewById(R.id.rl_tv2);
        rlTv3 = (RelativeLayout) findViewById(R.id.rl_tv3);
        findViewById(R.id.btn_analyse).setOnClickListener(this);

        tv1.setOnClickListener(this);
        tv2.setOnClickListener(this);
        tv3.setOnClickListener(this);
        head=new ArrayList<>();
        chest=new ArrayList<>();
        hand=new ArrayList<>();
        leg=new ArrayList<>();
        abdomen=new ArrayList<>();
        ((TextView)findViewById(R.id.tv_title)).setText(title);
    }

    private void setTag() {
        mSymptomAdapter.setOnSelectTagListenter(new SymptomRecyclerAdapter.SelectTag() {
            @Override
            public void getTag(SymptomBean symptomBean, final int position) {
                llLeft.setVisibility(View.VISIBLE);
                nextNotice.setVisibility(View.VISIBLE);

                if (flag < 3) {//防止快速点击四次导致不能直接跳转到结果页
                    flag++;
                    if (flag == 3) {
                        isFirst = false;
                    }
                } else {
                    ToastUtils.show("最多选择三个病症标签");
                    return;
                }

                if (flag == 1) {
                    tv1.setText(symptomBean.name);
                    b_1 = symptomBean.id;
                    rlTv1.setVisibility(View.VISIBLE);

                } else if (flag == 2) {
                    tv2.setText(symptomBean.name);
                    rlTv2.setVisibility(View.VISIBLE);
                    b_2 = symptomBean.id;

                } else if (flag == 3) {
                    tv3.setText(symptomBean.name);
                    rlTv3.setVisibility(View.VISIBLE);
                    b_3 = symptomBean.id;
                }
                if (flag < 3 || flag == 3 && !isFirst) {
                    getResultAndNext();
                }

            }
        });
    }

    //获得下一层病症和结果
    private void getResultAndNext() {
        if (flag == 1) {
            buffer = b_1;
        } else if (flag == 2) {
            buffer = b_1 + "," + b_2;
        } else if (flag == 3) {
            buffer = b_1 + "," + b_2 + "," + b_3;
        }

        NetworkApi.analyseSym(buffer, new NetworkManager.SuccessCallback<SymptomResultBean>() {
            @Override
            public void onSuccess(SymptomResultBean response) {
                if (response == null) {
                    return;
                }
                if (flag == 3) {//如果已经选中了三个标签则直接跳转到结果页
//                    startActivity(new Intent(mContext, SymptomAnalyseResultActivity.class)
//                            .putExtra("result", (Serializable) response.getBqs()));
                }
                List<SymptomResultBean.zzs> zzss = response.getZzs();
                mDataList.clear();
                //将zzs对象转换成SymptomBean对象，方便使用同一adapter
                for (int i = 0; i < zzss.size(); i++) {
                    SymptomBean symptomBean = new SymptomBean();
                    symptomBean.id = zzss.get(i).getId();
                    symptomBean.name = zzss.get(i).getName();
                    mDataList.add(symptomBean);
                }
                mSymptomAdapter.notifyDataSetChanged();

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_analyse://提交
                if(flag==0){
                    ToastUtils.show("至少选择一个病症标签");
                    return;
                }
                NetworkApi.analyseSym(buffer.substring(0, buffer.length() - 1), mAnalyseCallback);
                break;
            case R.id.tv_1://删除第一层病症标签
                flag--;
                //需要把后面两层的移动到前面去
                switch (flag){
                    case 0:
                        rlTv1.setVisibility(View.GONE);
                        rlTv2.setVisibility(View.GONE);
                        rlTv3.setVisibility(View.GONE);
                        b_1="";
                        b_2="";
                        b_3="";
                        break;
                    case 1:
                        rlTv2.setVisibility(View.GONE);
                        rlTv3.setVisibility(View.GONE);
                        b_1=b_2;
                        b_2="";
                        b_3="";
                        break;
                    case 2:
                        rlTv3.setVisibility(View.GONE);
                        b_1 = b_2;
                        b_2 = b_3;
                        b_3 = "";
                        break;
                }
                tv1.setText(tv2.getText().toString());
                tv2.setText(tv3.getText().toString());


                if (TextUtils.isEmpty(b_2) && TextUtils.isEmpty(b_3)&&TextUtils.isEmpty(b_1)) {
//                    llLeft.setVisibility(View.GONE);
                    nextNotice.setVisibility(View.GONE);
                    NetworkApi.getAllSym(mGetAllSymCallback);//获得第一层的病症
                } else {
                    getResultAndNext();
                }
                break;
            case R.id.tv_2://删除第二层病症标签
                flag--;
                if (flag == 1) {
                    rlTv2.setVisibility(View.GONE);
                    b_2 = "";
                } else if (flag == 2) {
                    rlTv3.setVisibility(View.GONE);
                    tv2.setText(tv3.getText().toString());
                    b_1 = b_2;
                    b_2 = b_3;
                    b_3 = "";
                }
                getResultAndNext();
                break;
            case R.id.tv_3://删除第三层病症标签
                flag--;
                rlTv3.setVisibility(View.GONE);
                b_3 = "";
                getResultAndNext();
                break;
        }
    }
    private ArrayList<SymptomBean> head,chest,hand,leg,abdomen;
    private NetworkManager.SuccessCallback mGetAllSymCallback = new NetworkManager.SuccessCallback<ArrayList<SymptomBean>>() {
        @Override
        public void onSuccess(ArrayList<SymptomBean> response) {
            if (response == null) {
                return;
            }
            speak(R.string.tips_symptom);
            head.clear();
            chest.clear();
            abdomen.clear();
            leg.clear();
            for(int i=0;i<response.size();i++){
                if(response.get(i).id.equals("6")||//头部
                        response.get(i).id.equals("7")||
                        response.get(i).id.equals("11")||
                        response.get(i).id.equals("12")||
                        response.get(i).id.equals("13")||
                        response.get(i).id.equals("22")||
                        response.get(i).id.equals("23")||
                        response.get(i).id.equals("28")||
                        response.get(i).id.equals("33")||
                        response.get(i).id.equals("40")||
                        response.get(i).id.equals("54")||
                        response.get(i).id.equals("55")||
                        response.get(i).id.equals("56")||
                        response.get(i).id.equals("57")||
                        response.get(i).id.equals("58")||
                        response.get(i).id.equals("59")||
                        response.get(i).id.equals("60")||
                        response.get(i).id.equals("61")||
                        response.get(i).id.equals("62")||
                        response.get(i).id.equals("63")||
                        response.get(i).id.equals("68")||
                        response.get(i).id.equals("69")||
                        response.get(i).id.equals("70")||
                        response.get(i).id.equals("71")||
                        response.get(i).id.equals("85")||
                        response.get(i).id.equals("86")||
                        response.get(i).id.equals("87")||
                        response.get(i).id.equals("91")||
                        response.get(i).id.equals("100")||
                        response.get(i).id.equals("101")||
                        response.get(i).id.equals("102")||
                        response.get(i).id.equals("103")||
                        response.get(i).id.equals("106")||
                        response.get(i).id.equals("107")||
                        response.get(i).id.equals("108")||
                        response.get(i).id.equals("113")
                        ){
                    head.add(response.get(i));
                }else if(response.get(i).id.equals("1")||
                        response.get(i).id.equals("2")||
                        response.get(i).id.equals("3")||
                        response.get(i).id.equals("4")||
                        response.get(i).id.equals("5")||
                        response.get(i).id.equals("8")||
                        response.get(i).id.equals("9")||
                        response.get(i).id.equals("10")||
                        response.get(i).id.equals("24")||
                        response.get(i).id.equals("25")||
                        response.get(i).id.equals("26")||
                        response.get(i).id.equals("27")||
                        response.get(i).id.equals("64")||
                        response.get(i).id.equals("67")||
                        response.get(i).id.equals("75")||
                        response.get(i).id.equals("93")||
                        response.get(i).id.equals("94")||
                        response.get(i).id.equals("95")||
                        response.get(i).id.equals("96")||
                        response.get(i).id.equals("97")||
                        response.get(i).id.equals("98")||
                        response.get(i).id.equals("104")||
                        response.get(i).id.equals("105")||
                        response.get(i).id.equals("110")||
                        response.get(i).id.equals("111")||
                        response.get(i).id.equals("112")
                        ){//胸部
                        chest.add(response.get(i));
                }else if(response.get(i).id.equals("29")||//腹部
                        response.get(i).id.equals("30")||
                        response.get(i).id.equals("31")||
                        response.get(i).id.equals("32")||
                        response.get(i).id.equals("34")||
                        response.get(i).id.equals("35")||
                        response.get(i).id.equals("36")||
                        response.get(i).id.equals("37")||
                        response.get(i).id.equals("38")||
                        response.get(i).id.equals("39")||
                        response.get(i).id.equals("41")||
                        response.get(i).id.equals("44")||
                        response.get(i).id.equals("45")||
                        response.get(i).id.equals("46")||
                        response.get(i).id.equals("47")||
                        response.get(i).id.equals("48")||
                        response.get(i).id.equals("49")||
                        response.get(i).id.equals("50")||
                        response.get(i).id.equals("51")||
                        response.get(i).id.equals("52")||
                        response.get(i).id.equals("53")||
                        response.get(i).id.equals("65")||
                        response.get(i).id.equals("66")||
                        response.get(i).id.equals("72")||
                        response.get(i).id.equals("73")||
                        response.get(i).id.equals("74")||
                        response.get(i).id.equals("76")||
                        response.get(i).id.equals("77")||
                        response.get(i).id.equals("78")||
                        response.get(i).id.equals("79")||
                        response.get(i).id.equals("80")||
                        response.get(i).id.equals("81")||
                        response.get(i).id.equals("82")||
                        response.get(i).id.equals("83")||
                        response.get(i).id.equals("84")||
                        response.get(i).id.equals("99")
                        ){
                        abdomen.add(response.get(i));
                }else if(response.get(i).id.equals("14")||//四肢
                        response.get(i).id.equals("15")||
                        response.get(i).id.equals("16")||
                        response.get(i).id.equals("17")||
                        response.get(i).id.equals("18")||
                        response.get(i).id.equals("19")||
                        response.get(i).id.equals("20")||
                        response.get(i).id.equals("21")||
                        response.get(i).id.equals("42")||
                        response.get(i).id.equals("43")||
                        response.get(i).id.equals("88")||
                        response.get(i).id.equals("89")||
                        response.get(i).id.equals("90")||
                        response.get(i).id.equals("92")||
                        response.get(i).id.equals("109")){
                        leg.add(response.get(i));
                }
            }
            mDataList.clear();
            switch (title){
                case "头部":
                    mDataList.addAll(head);
                    break;
                case "胸部":
                    mDataList.addAll(chest);
                    break;
                case "腹部":
                    mDataList.addAll(abdomen);
                    break;
                case "四肢":
                    mDataList.addAll(leg);
                    break;
            }
            mSymptomAdapter.notifyDataSetChanged();
        }
    };

    private NetworkManager.SuccessCallback mAnalyseCallback = new NetworkManager.SuccessCallback<SymptomResultBean>() {
        @Override
        public void onSuccess(SymptomResultBean response) {
            if (response == null) {
                return;
            }
            startActivity(new Intent(mContext, SymptomAnalyseResultActivity.class)
                    .putExtra("result", (Serializable) response.getBqs()));
        }
    };


}
