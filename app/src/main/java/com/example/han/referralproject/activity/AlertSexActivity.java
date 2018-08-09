package com.example.han.referralproject.activity;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.bean.PUTUserBean;
import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.util.LocalShared;
import com.google.gson.Gson;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.medlink.danbogh.register.SelectAdapter;
import com.medlink.danbogh.utils.T;
import com.qiniu.android.utils.Json;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import github.hellocsl.layoutmanager.gallery.GalleryLayoutManager;

public class AlertSexActivity extends BaseActivity {
    @BindView(R.id.rv_sign_up_content)
    RecyclerView rvSignUpContent;
    @BindView(R.id.tv_sign_up_go_back)
    TextView tvSignUpGoBack;
    @BindView(R.id.tv_sign_up_go_forward)
    TextView tvSignUpGoForward;
    List<String> strings = new ArrayList<>();
    private int currentPositon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert_sex);
        ButterKnife.bind(this);
        initTitle();
        initData();
        initView();
    }


    private void initTitle() {
        mToolbar.setVisibility(View.VISIBLE);
        mTitleText.setText("修改性别");

        tvSignUpGoBack.setText("取消");
        tvSignUpGoForward.setText("确定");

    }

    private void initData() {
        strings = Arrays.asList("男", "女");
    }

    private void initView() {
        GalleryLayoutManager manager = new GalleryLayoutManager(1);
        manager.attach(rvSignUpContent, 1);
        manager.setCallbackInFling(true);
        manager.setOnItemSelectedListener(new GalleryLayoutManager.OnItemSelectedListener() {
            @Override
            public void onItemSelected(RecyclerView recyclerView, View view, int positon) {
                currentPositon = positon;
                T.show(strings.get(positon));
            }
        });

        SelectAdapter adapter = new SelectAdapter();
        adapter.setStrings(strings);
        adapter.setOnItemClickListener(new SelectAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                rvSignUpContent.smoothScrollToPosition(position);
            }
        });

        rvSignUpContent.setAdapter(adapter);
    }

    @OnClick({R.id.tv_sign_up_go_back, R.id.tv_sign_up_go_forward})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_sign_up_go_back:
                finish();
                break;
            case R.id.tv_sign_up_go_forward:
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
                break;
        }
    }

}
