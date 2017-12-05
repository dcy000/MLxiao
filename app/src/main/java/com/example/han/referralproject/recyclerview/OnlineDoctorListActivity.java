package com.example.han.referralproject.recyclerview;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.han.referralproject.MainActivity;
import com.example.han.referralproject.R;
import com.example.han.referralproject.bean.AllDoctor;
import com.example.han.referralproject.bean.Doctor;
import com.example.han.referralproject.bean.Doctors;
import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.network.NetworkManager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class OnlineDoctorListActivity extends Activity implements View.OnClickListener {

    private ImageView mIconBack;
    private LinearLayout mLinearlayou;
    private ImageView mIconHome;
    private RecyclerView mList;
    private List<AllDoctor> mData;
    private int start=0,end=9,limit=10;
    private AllDoctorAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_doctor_list);
        initView();
        setAdapter();
        getData();
    }

    private void getData() {
        NetworkApi.getAllDoctor(null, start + "", end + "",
                new NetworkManager.SuccessCallback<ArrayList<AllDoctor>>() {
                    @Override
                    public void onSuccess(ArrayList<AllDoctor> response) {
                        start+=limit;
                        end+=limit;
                        mData.addAll(response);
                        adapter.notifyDataSetChanged();

                    }
                });
    }

    private void setAdapter() {
        mList.setLayoutManager(new GridLayoutManager(this,3));
        mList.setAdapter(adapter=new AllDoctorAdapter(mData,this));
        adapter.setOnItemClistListener(new AllDoctorAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int postion) {
//                startActivity(new Intent(OnlineDoctorListActivity.this,DoctorMesActivity.class)
//                        .putExtra("docMsg", (Serializable) mData.get(postion)));
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
