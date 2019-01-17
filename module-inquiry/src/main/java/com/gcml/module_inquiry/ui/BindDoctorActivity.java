package com.gcml.module_inquiry.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gcml.common.utils.display.ToastUtils;
import com.gcml.common.widget.dialog.AlertDialog;
import com.gcml.common.widget.toolbar.ToolBarClickListener;
import com.gcml.common.widget.toolbar.TranslucentToolBar;
import com.gcml.module_inquiry.R;
import com.gcml.module_inquiry.adapter.BindDoctorAdapter;
import com.gcml.module_inquiry.model.Docter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by lenovo on 2019/1/16.
 */

public class BindDoctorActivity extends AppCompatActivity {

    private TranslucentToolBar tb;
    private RecyclerView rvDoctors;
    ArrayList<Docter> doctors = new ArrayList();
    private BindDoctorAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_doctor);
        initTitle();
        initRV();
        getData();

    }

    private void initRV() {
        rvDoctors = findViewById(R.id.rv_doctor_list);
        rvDoctors.setLayoutManager(new GridLayoutManager(this, 2));
        adapter = new BindDoctorAdapter(R.layout.item_doctor_info, doctors);
        rvDoctors.setAdapter(adapter);
//        rvDoctors.setAdapter(new BaseQuickAdapter<Docter,BaseViewHolder>(R.layout.item_doctor_info, doctors) {
//            @Override
//            protected void convert(BaseViewHolder helper, Docter item) {
//
//            }
//        });

        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                showBindDialog(doctors.get(position).doctername);
            }
        });

    }

    private void showBindDialog(String doctorName) {
        new AlertDialog(this).builder()
                .setMsg(doctorName)
                .setPositiveButton("确认", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ToastUtils.showShort("签约");
                        startActivity(new Intent(BindDoctorActivity.this, UserSignActivity.class));

                    }
                })
                .setNegativeButton("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ToastUtils.showShort("取消");
                    }
                }).show();
    }

    private void initTitle() {
        tb = findViewById(R.id.tb_bind_doctor);
        tb.setData("签 约 医 生",
                R.drawable.common_btn_back, "返回",
                R.drawable.common_ic_wifi_state, null, new ToolBarClickListener() {
                    @Override
                    public void onLeftClick() {
                        finish();
                    }

                    @Override
                    public void onRightClick() {

                    }
                });
    }

    public ArrayList<Docter> getData() {
        for (int i = 0; i < 21; i++) {
            Docter e = new Docter();
            e.doctername = "医生" + i;
            doctors.add(e);
        }
        adapter.addData(doctors);
        return doctors;
    }
}
