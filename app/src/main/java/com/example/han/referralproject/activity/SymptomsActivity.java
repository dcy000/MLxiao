package com.example.han.referralproject.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.han.referralproject.MainActivity;
import com.example.han.referralproject.R;
import com.example.han.referralproject.adapter.SymptomAdapter;

import java.util.ArrayList;
import java.util.List;

public class SymptomsActivity extends BaseActivity implements View.OnClickListener {

    private ImageView mIconBack;
    private LinearLayout mLinearlayou;
    private ImageView mIconHome;
    private RecyclerView mList;
    private ArrayList<String> mData;
    private SymptomAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_symptoms);
        mToolbar.setVisibility(View.VISIBLE);
        initView();
        speak(getString(R.string.symptoms));
        setAdapter();
    }

    private void setAdapter() {
        mList.setLayoutManager(new GridLayoutManager(this,3));
        mData.add("头晕");
        mData.add("头痛");
        mData.add("烦躁");
        mData.add("肢体麻木");
        mData.add("注意力不集中，记忆力减退");
        mData.add("口干、口渴");
        mData.add("体重减轻");
        mData.add("肢端麻木");
        mData.add("焦躁");
        mData.add("焦虑");
        mData.add("冒冷汗");
        mList.setAdapter(adapter=new SymptomAdapter(this,mData));
        adapter.setOnItemClickListener(new SymptomAdapter.OnClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                switch (mData.get(position)){
                    case "头晕":
                        speak(getString(R.string.gaoxueya));
                        break;
                    case "头痛":
                        speak(getString(R.string.gaoxueya));
                        break;
                    case "烦躁":
                        speak(getString(R.string.gaoxueya));
                        break;
                    case "肢体麻木":
                        speak(getString(R.string.gaoxueya));
                        break;
                    case "注意力不集中，记忆力减退":
                        speak(getString(R.string.gaoxueya));
                        break;
                    case "口干、口渴":
                        speak(getString(R.string.tangniaobing));
                        break;
                    case "体重减轻":
                        speak(getString(R.string.tangniaobing));
                        break;
                    case "肢端麻木":
                        speak(getString(R.string.tangniaobing));
                        break;
                    case "焦躁":
                        speak(getString(R.string.dixuetang));
                        break;
                    case "焦虑":
                        speak(getString(R.string.dixuetang));
                        break;
                    case "冒冷汗":
                        speak(getString(R.string.dixuetang));
                        break;

                }
            }
        });
    }

    private void initView() {
        mIconBack = (ImageView) findViewById(R.id.icon_back);
        mIconBack.setOnClickListener(this);
        mLinearlayou = (LinearLayout) findViewById(R.id.linearlayou);
        mIconHome = (ImageView) findViewById(R.id.icon_home);
        mIconHome.setOnClickListener(this);
        mList = (RecyclerView) findViewById(R.id.list);
        mData=new ArrayList<>();

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
        }
    }
}
