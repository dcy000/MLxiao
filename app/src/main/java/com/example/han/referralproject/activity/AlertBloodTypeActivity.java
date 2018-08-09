package com.example.han.referralproject.activity;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.medlink.danbogh.register.SelectAdapter;
import com.medlink.danbogh.utils.T;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import github.hellocsl.layoutmanager.gallery.GalleryLayoutManager;

public class AlertBloodTypeActivity extends BaseActivity {

    @BindView(R.id.rv_sign_up_content)
    RecyclerView rvSignUpContent;
    @BindView(R.id.tv_sign_up_go_back)
    TextView tvSignUpGoBack;
    @BindView(R.id.tv_sign_up_go_forward)
    TextView tvSignUpGoForward;
    List<String> strings = new ArrayList<>();
    @BindView(R.id.tv_sign_up_height)
    TextView tvSignUpHeight;
    private int currentPositon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert_blood_type);
        ButterKnife.bind(this);
        initTitle();
        initData();
        initView();
    }

    private void initData() {
        strings = Arrays.asList("AB", "A", "B", "O");
    }

    private void initTitle() {
        mToolbar.setVisibility(View.VISIBLE);
        mTitleText.setText("修改血型");

        tvSignUpGoBack.setText("取消");
        tvSignUpGoForward.setText("确定");

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
                String seletedType = strings.get(currentPositon);
                // TODO: 2018/8/9
                break;
        }
    }
}
