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
    public ImageView ivBack;

    public ImageView ImageView1;
    public ImageView ImageView2;
    private int flag = 0;//用于记录是第几层病症选择
    private LinearLayout llLeft;
    private TextView tv1, tv2, tv3;//选择的病症标签
    private TextView nextNotice;//下一层病症的提示
    private String buffer;//记录选择的标签
    private RelativeLayout rlTv1, rlTv2, rlTv3;
    private String b_1 = "", b_2 = "", b_3 = "";//记录每一层选择的病症id；
    private boolean isFirst = true;//防止快速点击

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_symptom_analyse);
        /*ivBack = (ImageView) findViewById(R.id.iv_back);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });*/
        initView();

        mSymptomAdapter = new SymptomRecyclerAdapter(mContext, mDataList);
        mRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 3));
        mRecyclerView.setAdapter(mSymptomAdapter);


        //获取第一层的病症
        NetworkApi.getAllSym(mGetAllSymCallback);


        ImageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        ImageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        setTag();
    }

    private void initView() {
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
        ImageView1 = (ImageView) findViewById(R.id.icon_back);
        ImageView2 = (ImageView) findViewById(R.id.icon_home);

        tv1.setOnClickListener(this);
        tv2.setOnClickListener(this);
        tv3.setOnClickListener(this);
    }

    private void setTag() {
        mSymptomAdapter.setOnSelectTagListenter(new SymptomRecyclerAdapter.SelectTag() {
            @Override
            public void getTag(SymptomBean symptomBean, final int position) {
                llLeft.setVisibility(View.VISIBLE);
                nextNotice.setVisibility(View.VISIBLE);

                if (flag < 3) {//防止快速点击四次导致不能直接跳转到结果页
                    flag++;
                    if(flag==3){
                        isFirst=false;
                    }
                } else {
                    ToastUtils.show("最多选择三个病症标签");
                    return;
                }

                if (flag == 1) {
                    tv1.setText(symptomBean.name);
                    b_1 = symptomBean.id;

                } else if (flag == 2) {
                    tv2.setText(symptomBean.name);
                    rlTv2.setVisibility(View.VISIBLE);
                    b_2 = symptomBean.id;

                } else if (flag == 3) {
                    tv3.setText(symptomBean.name);
                    rlTv3.setVisibility(View.VISIBLE);
                    b_3 = symptomBean.id;
                }
                if(flag<3||flag==3&&!isFirst){
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
                    startActivity(new Intent(mContext, SymptomAnalyseResultActivity.class)
                            .putExtra("result", (Serializable) response.getBqs()));
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
                NetworkApi.analyseSym(buffer.substring(0, buffer.length() - 1), mAnalyseCallback);
                break;
            case R.id.tv_1://删除第一层病症标签
                flag--;
                //需要把后面两层的移动到前面去
                rlTv3.setVisibility(View.GONE);
                tv1.setText(tv2.getText().toString());
                tv2.setText(tv3.getText().toString());
                b_1 = b_2;
                b_2 = b_3;
                b_3 = "";

                if (TextUtils.isEmpty(b_2) && TextUtils.isEmpty(b_3)) {
                    llLeft.setVisibility(View.GONE);
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

    private NetworkManager.SuccessCallback mGetAllSymCallback = new NetworkManager.SuccessCallback<ArrayList<SymptomBean>>() {
        @Override
        public void onSuccess(ArrayList<SymptomBean> response) {
            if (response == null) {
                return;
            }
            speak(R.string.tips_symptom);
            mDataList.clear();
            mDataList.addAll(response);
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
