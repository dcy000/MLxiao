package com.gcml.module_inquiry.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.gcml.common.utils.AutoLoadMoreHelper;
import com.gcml.common.utils.DefaultObserver;
import com.gcml.common.utils.RxUtils;
import com.gcml.common.utils.app.ActivityHelper;
import com.gcml.common.utils.display.ToastUtils;
import com.gcml.common.widget.dialog.AlertDialog;
import com.gcml.common.widget.toolbar.ToolBarClickListener;
import com.gcml.common.widget.toolbar.TranslucentToolBar;
import com.gcml.module_inquiry.R;
import com.gcml.module_inquiry.adapter.BindDoctorAdapter;
import com.gcml.module_inquiry.model.Docter;
import com.gcml.module_inquiry.model.HealthFileRepostory;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

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
        ActivityHelper.addActivity(this);
        initTitle();
        initRV();
        getData();
    }

    private void initRV() {
        rvDoctors = findViewById(R.id.rv_doctor_list);
        rvDoctors.setLayoutManager(new GridLayoutManager(this, 2));

        AutoLoadMoreHelper loadMoreHelper = new AutoLoadMoreHelper();
        loadMoreHelper.attachToRecyclerView(rvDoctors);
        loadMoreHelper.setOnAutoLoadMoreListener(onAutoLoadMoreListener);

        adapter = new BindDoctorAdapter(R.layout.item_doctor_info, doctors);
        adapter.setListener(new BindDoctorAdapter.OnClickQianyueListener() {
            @Override
            public void onClick(Docter docter) {
                showBindDialog(docter);
            }
        });
        rvDoctors.setAdapter(adapter);
    }

    private void showBindDialog(Docter docter) {
        new AlertDialog(this).builder()
                .setMsg("确认与" + docter.doctername + "医生签约吗?")
                .setPositiveButton("确认", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(BindDoctorActivity.this, UserSignActivity.class).putExtra("doid", docter.docterid));
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

    int index = 0;
    int limit = 18;
    int count = -1;
    HealthFileRepostory fileRepostory = new HealthFileRepostory();

    public void getData() {
        count++;
        this.index = index + count * limit;

        fileRepostory.getDoctors(index, limit)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new DefaultObserver<List<Docter>>() {
                    @Override
                    public void onNext(List<Docter> docters) {
                        super.onNext(docters);
                        adapter.addData(docters);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        ToastUtils.showShort(throwable.getMessage());
                    }
                });


    }

    private AutoLoadMoreHelper.OnAutoLoadMoreListener onAutoLoadMoreListener = new AutoLoadMoreHelper.OnAutoLoadMoreListener() {
        @Override
        public void onAutoLoadMore(AutoLoadMoreHelper autoLoadMoreHelper) {
            getData();
        }
    };
}
