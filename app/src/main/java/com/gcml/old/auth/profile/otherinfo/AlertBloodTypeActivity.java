package com.gcml.old.auth.profile.otherinfo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.util.LocalShared;
import com.gcml.common.widget.toolbar.ToolBarClickListener;
import com.gcml.common.widget.toolbar.TranslucentToolBar;
import com.gcml.lib_utils.display.ToastUtils;
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

public class AlertBloodTypeActivity extends AppCompatActivity implements View.OnClickListener {

    private TranslucentToolBar tbBloodTypeTitle;
    /**
     * 您的血型
     */
    private TextView tvSignUpHeight;
    private RecyclerView rvSignUpContent;
    /**
     * 上一步
     */
    private TextView tvSignUpGoBack;
    /**
     * 下一步
     */
    private TextView tvSignUpGoForward;
    private List<String> strings = new ArrayList<>();
    private int currentPositon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert_blood_type);
        initView();
        initData();
        initRV();
    }

    private void initRV() {
        GalleryLayoutManager manager = new GalleryLayoutManager(1);
        manager.attach(rvSignUpContent, 1);
        manager.setCallbackInFling(true);
        manager.setOnItemSelectedListener(new GalleryLayoutManager.OnItemSelectedListener() {
            @Override
            public void onItemSelected(RecyclerView recyclerView, View view, int positon) {
                currentPositon = positon;
                ToastUtils.showShort(strings.get(positon));
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

    private void initData() {
        strings = Arrays.asList("AB", "A", "B", "O");
    }

    private void initView() {
        tbBloodTypeTitle = (TranslucentToolBar) findViewById(R.id.tb_blood_type_title);
        tvSignUpHeight = (TextView) findViewById(R.id.tv_sign_up_height);
        rvSignUpContent = (RecyclerView) findViewById(R.id.rv_sign_up_content);
        tvSignUpGoBack = (TextView) findViewById(R.id.tv_sign_up_go_back);
        tvSignUpGoBack.setOnClickListener(this);
        tvSignUpGoForward = (TextView) findViewById(R.id.tv_sign_up_go_forward);
        tvSignUpGoForward.setOnClickListener(this);


        tvSignUpGoBack.setText("取消");
        tvSignUpGoForward.setText("确定");
        tbBloodTypeTitle.setData("修 改 血 型", R.drawable.common_icon_back, "返回",
                0, null, new ToolBarClickListener() {
                    @Override
                    public void onLeftClick() {
                        finish();
                    }

                    @Override
                    public void onRightClick() {

                    }
                });

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
        String seletedType = strings.get(currentPositon);
        PUTUserBean bean = new PUTUserBean();
        bean.bid = Integer.parseInt(LocalShared.getInstance(this).getUserId());
        bean.bloodType = seletedType;

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
