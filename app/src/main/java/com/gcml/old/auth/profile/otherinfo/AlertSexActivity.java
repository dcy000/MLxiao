package com.gcml.old.auth.profile.otherinfo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.han.referralproject.R;
import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.util.LocalShared;
import com.gcml.common.widget.toolbar.ToolBarClickListener;
import com.gcml.common.widget.toolbar.TranslucentToolBar;
import com.gcml.old.auth.profile.otherinfo.bean.PUTUserBean;
import com.gcml.old.auth.register.SelectAdapter;
import com.google.gson.Gson;
import com.iflytek.synthetize.MLVoiceSynthetize;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import github.hellocsl.layoutmanager.gallery.GalleryLayoutManager;

public class AlertSexActivity extends AppCompatActivity implements View.OnClickListener {

    private TranslucentToolBar mTbSex;
    /**
     * 您的性别
     */
    private TextView mTvSignUpHeight;
    private RecyclerView mRvSignUpContent;
    /**
     * 上一步
     */
    private TextView mTvSignUpGoBack;
    /**
     * 下一步
     */
    private TextView mTvSignUpGoForward;
    private int currentPositon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert_sex);
        initView();
        initData();
        initRV();
    }

    private List<String> strings = new ArrayList<>();

    private void initData() {
        strings = Arrays.asList("男", "女");
    }

    private void initView() {
        mTbSex = (TranslucentToolBar) findViewById(R.id.tb_sex_title);
        mTvSignUpHeight = (TextView) findViewById(R.id.tv_sign_up_height);
        mRvSignUpContent = (RecyclerView) findViewById(R.id.rv_sign_up_content);
        mTvSignUpGoBack = (TextView) findViewById(R.id.tv_sign_up_go_back);
        mTvSignUpGoBack.setOnClickListener(this);
        mTvSignUpGoForward = (TextView) findViewById(R.id.tv_sign_up_go_forward);
        mTvSignUpGoForward.setOnClickListener(this);

        mTvSignUpGoBack.setText("取消");
        mTvSignUpGoForward.setText("确定");
        mTbSex.setData("修改性别", R.drawable.common_icon_back, "返回",
                R.drawable.common_icon_home, null, new ToolBarClickListener() {
                    @Override
                    public void onLeftClick() {


                    }

                    @Override
                    public void onRightClick() {

                    }
                });


    }

    private void initRV() {
        GalleryLayoutManager manager = new GalleryLayoutManager(1);
        manager.attach(mRvSignUpContent, 1);
        manager.setCallbackInFling(true);
        manager.setOnItemSelectedListener((recyclerView, view, positon) -> {
            currentPositon = positon;
            Toast.makeText(AlertSexActivity.this, strings.get(positon), Toast.LENGTH_SHORT).show();
        });

        SelectAdapter adapter = new SelectAdapter();
        adapter.setStrings(strings);
        adapter.setOnItemClickListener(new SelectAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                mRvSignUpContent.smoothScrollToPosition(position);
            }
        });

        mRvSignUpContent.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.tv_sign_up_go_back:
                finish();
                break;
            case R.id.tv_sign_up_go_forward:
                nextStep();
                break;
        }
    }

    private void nextStep() {
        String seletedSex = strings.get(currentPositon);

        PUTUserBean bean = new PUTUserBean();
        bean.bid = Integer.parseInt(LocalShared.getInstance(this).getUserId());
        bean.sex = seletedSex;

        NetworkApi.putUserInfo(bean.bid, new Gson().toJson(bean), new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                String body = response.body();
                try {
                    JSONObject json = new JSONObject(body);
                    boolean tag = json.getBoolean("tag");
                    if (tag) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                speak("修改成功");
                            }
                        });
                        finish();
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                speak("修改失败");
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    private void speak(String text) {
        MLVoiceSynthetize.startSynthesize(this, text, false);
    }
}
