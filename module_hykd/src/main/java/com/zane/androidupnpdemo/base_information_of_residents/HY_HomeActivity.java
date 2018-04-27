package com.zane.androidupnpdemo.base_information_of_residents;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gzq.administrator.lib_common.base.CommonBaseActivity;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.zane.androidupnpdemo.R;
import com.zane.androidupnpdemo.net.HYKD_API;
import com.zane.androidupnpdemo.utils.AesUtil;
import com.zane.androidupnpdemo.utils.Utils;

import java.util.Arrays;


/**
 * Created by gzq on 2018/4/12.
 */

public class HY_HomeActivity extends CommonBaseActivity {

    private RecyclerView mFunctionList;
    private String[] functions={"我的签约","我的基本信息","我的服务团队"};
    private BaseQuickAdapter<String, BaseViewHolder> adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hy_activity_home);
        initView();
        initRecycleview();
    }

    private void initRecycleview() {
        LinearLayoutManager manager=new LinearLayoutManager(this);
        mFunctionList.setLayoutManager(manager);
        mFunctionList.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        mFunctionList.setAdapter(adapter=new BaseQuickAdapter<String,BaseViewHolder>(android.R.layout.select_dialog_item, Arrays.asList(functions)) {
            @Override
            protected void convert(BaseViewHolder helper, String item) {
                helper.setText(android.R.id.text1,item);
            }
        });
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                switch (position){
                    case 0:
                        MySignInfomationActivity.startActivity(HY_HomeActivity.this,MySignInfomationActivity.class);
                        break;
//                    case 1:
//                        MyServicedHistoryActivity.startActivity(HY_HomeActivity.this,MyServicedHistoryActivity.class);
//                        break;
                    case 1:
                        MyBaseInformationActivity.startActivity(HY_HomeActivity.this,MyBaseInformationActivity.class);
                        break;
                    case 2:
                        HYServiceAgreementActivity.startActivity(HY_HomeActivity.this,HYServiceAgreementActivity.class);
                        break;
                }
            }
        });
    }



    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {

        }
    }

    private void initView() {
        mFunctionList = (RecyclerView) findViewById(R.id.function_list);
    }
}
