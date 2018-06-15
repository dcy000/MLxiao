package com.medlink.danbogh.register;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.application.MyApplication;
import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.network.NetworkManager;
import com.example.han.referralproject.util.LocalShared;
import com.medlink.danbogh.utils.T;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import github.hellocsl.layoutmanager.gallery.GalleryLayoutManager;

public class AuthChangeBloodTypeActivity extends BaseActivity {

    @BindView(R.id.tv_sign_up_height)
    TextView tvTitle;
    @BindView(R.id.rv_sign_up_content)
    RecyclerView rvContent;
    @BindView(R.id.tv_sign_up_unit)
    TextView tvUnit;

    protected Unbinder unbinder;
    protected SelectAdapter adapter;
    public ArrayList<String> mStrings;

    protected int selectedPosition = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setShowVoiceView(false);
        setContentView(R.layout.auth_activity_change_blood_type);
        unbinder = ButterKnife.bind(this);
        mToolbar.setVisibility(View.VISIBLE);
        mRightView.setVisibility(View.GONE);
        mRightText.setText("确定");
        mTitleText.setText("修改血型");
        mRightText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onTvGoForwardClicked();
            }
        });
        initView();
    }


    protected void initView() {
        selectedPosition = 0;
        GalleryLayoutManager layoutManager = new GalleryLayoutManager(GalleryLayoutManager.VERTICAL);
        layoutManager.attach(rvContent, selectedPosition);
        layoutManager.setCallbackInFling(true);
        layoutManager.setOnItemSelectedListener(new GalleryLayoutManager.OnItemSelectedListener() {
            @Override
            public void onItemSelected(RecyclerView recyclerView, View item, int position) {
                selectedPosition = position;
                select((String) (mStrings == null ? String.valueOf(position) : mStrings.get(position)));
            }
        });
        adapter = new SelectAdapter();
        adapter.setStrings(getStrings());
        adapter.setOnItemClickListener(new SelectAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                rvContent.smoothScrollToPosition(position);
            }
        });
        rvContent.setAdapter(adapter);
        tvTitle.setText("您的血型");
        tvUnit.setText("型");
    }

    protected List<String> getStrings() {
        mStrings = new ArrayList<>();
        mStrings.add("AB");
        mStrings.add("A");
        mStrings.add("B");
        mStrings.add("O");
        return mStrings;
    }

    public void onTvGoForwardClicked() {
        String bloodType = mStrings.get(selectedPosition);
        LocalShared.getInstance(this.getApplicationContext()).setSignUpBloodType(bloodType);
        NetworkApi.updateBloodType(MyApplication.getInstance().userId, bloodType, new NetworkManager.SuccessCallback<Object>() {
            @Override
            public void onSuccess(Object response) {
                if (!isDestroyed() || !isFinishing()) {
                    finish();
                }
            }
        }, new NetworkManager.FailedCallback() {
            @Override
            public void onFailed(String message) {
                T.show("服务器繁忙");
            }
        });
    }

    @Override
    protected void onResume() {
        setDisableGlobalListen(true);
        setEnableListeningLoop(false);
        super.onResume();
        speak(geTip());
    }

    protected int geTip() {
        return R.string.sign_up_blood_type_tip;
    }

    public void select(String text) {
        T.show(text);
    }
}
