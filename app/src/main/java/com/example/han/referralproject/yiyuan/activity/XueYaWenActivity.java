package com.example.han.referralproject.yiyuan.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.activity.DetectActivity;
import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.util.LocalShared;
import com.example.han.referralproject.yiyuan.bean.WenZhenBean;
import com.example.han.referralproject.yiyuan.bean.WenZhenReultBean;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.medlink.danbogh.utils.T;

import org.json.JSONObject;
import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class XueYaWenActivity extends BaseActivity {

    @BindView(R.id.tv_yueya_wen)
    TextView tvYueyaWen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xue_ya_wen);
        ButterKnife.bind(this);
        initTilte();
    }

    private void initTilte() {
        mToolbar.setVisibility(View.VISIBLE);
        mTitleText.setText("问诊");
    }

    @OnClick(R.id.tv_yueya_wen)
    public void onViewClicked() {
        Intent intent = new Intent();
        intent.putExtra("from", "Test");
        intent.putExtra("fromType", "xueya");
        intent.setClass(this, DetectActivity.class);
        startActivityForResult(intent, 119);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 119) {
                LocalShared.getInstance(this).setXueYa(data.getStringExtra("xueya"));
                postWenZhenData();
            }
        }
    }

    private void postWenZhenData() {
        WenZhenBean bean = new WenZhenBean();
        bean.address = LocalShared.getInstance(this).getSignUpAddress();
        bean.allergicHistory = LocalShared.getInstance(this).getGuoMin();
        bean.diseasesHistory = LocalShared.getInstance(this).getJiBingShi();
        bean.equipmentId = LocalShared.getInstance(this).getEqID();
        bean.height = LocalShared.getInstance(this).getSignUpHeight() + "";
        bean.hiUserInquiryId = "";
        String xueYa = LocalShared.getInstance(this).getXueYa();
        bean.highPressure = xueYa.split(",")[0];
        bean.lowPressure = xueYa.split(",")[1];
        bean.hypertensionState = "0";
        bean.lastMensesTime = LocalShared.getInstance(this).getYueJingDate();
        bean.pregnantState = LocalShared.getInstance(this).getHuaiYun();
        bean.userId = LocalShared.getInstance(this).getUserId();

        final Gson gson = new Gson();
        OkGo.<String>post(NetworkApi.Inquiry)
                .tag(this)
                .upJson(gson.toJson(bean))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        String result = response.body();
                        if (!TextUtils.isEmpty(result)) {
                            WenZhenReultBean reultBean = gson.fromJson(result, WenZhenReultBean.class);
                            if ("成功".equals(reultBean.message)) {
                                T.show("提交成功");
                            } else {
                                T.show("提交失败");
                            }
                            InquiryAndFileEndActivity.startMe(XueYaWenActivity.this,"问诊");
                            finish();
                        }
                    }
                });


    }
}
