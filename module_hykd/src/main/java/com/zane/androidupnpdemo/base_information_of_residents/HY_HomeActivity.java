package com.zane.androidupnpdemo.base_information_of_residents;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.gzq.administrator.lib_common.base.CommonBaseActivity;
import com.gzq.administrator.lib_common.utils.ToastTool;
import com.gzq.administrator.lib_common.utils.UiUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.zane.androidupnpdemo.R;
import com.zane.androidupnpdemo.net.HYKD_API;
import com.zane.androidupnpdemo.utils.AesUtil;
import com.zane.androidupnpdemo.utils.Utils;

import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Created by gzq on 2018/4/12.
 */

public class HY_HomeActivity extends CommonBaseActivity{
    private Button mBtnTest;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hy_activity_home);
        initView();
        getData();
    }

    private void getData() {
        String param="";
        try {
            param=AesUtil.urlEncode(Utils.parseCurrentTime2String()+"_"+HYKD_API.RESIDENT_NAME+"_"+HYKD_API.RESIDENT_IDCARD,AesUtil.KeyCode);
//            param=AesUtil.urlEncode(Utils.parseCurrentTime2String()+"_"+"3102",AesUtil.KeyCode);
            Log.e("加密前", "加密前的参数："+ Utils.parseCurrentTime2String()+"_"+HYKD_API.RESIDENT_NAME+"_"+HYKD_API.RESIDENT_IDCARD);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.e("参数", "请求的参数"+param );
        OkGo.<String>post(HYKD_API.SIGN_TEAM_WITH_SERVER)
                .params("param",param)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Log.e("返回的数据",response.body());
                    }
                });
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.btn_test:
                ToastTool.showShort("测试点击");
                break;
        }
    }

    private void initView() {
        mBtnTest = (Button) findViewById(R.id.btn_test);
        mBtnTest.setOnClickListener(this);
    }
}
