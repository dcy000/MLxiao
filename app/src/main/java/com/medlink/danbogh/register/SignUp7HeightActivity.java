package com.medlink.danbogh.register;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.util.LocalShared;
import com.example.han.referralproject.yiyuan.util.ActivityHelper;
import com.medlink.danbogh.utils.T;
import com.medlink.danbogh.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import github.hellocsl.layoutmanager.gallery.GalleryLayoutManager;

public class SignUp7HeightActivity extends BaseActivity {

    @BindView(R.id.tv_sign_up_height)
    TextView tvTitle;
    @BindView(R.id.rv_sign_up_content)
    RecyclerView rvContent;
    @BindView(R.id.tv_sign_up_unit)
    TextView tvUnit;
    @BindView(R.id.tv_sign_up_go_back)
    TextView tvGoBack;
    @BindView(R.id.tv_sign_up_go_forward)
    TextView tvGoForward;
    protected Unbinder unbinder;
    protected SelectAdapter adapter;
    public ArrayList<String> mStrings;

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, SignUp7HeightActivity.class);
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setShowVoiceView(true);
        setContentView(R.layout.activity_height_wen);
        mToolbar.setVisibility(View.GONE);
        unbinder = ButterKnife.bind(this);
        initView();
        initTitle();
        ActivityHelper.addActivity(this);
    }

    private void initTitle() {
        mTitleText.setText("问诊");
        mToolbar.setVisibility(View.VISIBLE);
        mRightText.setVisibility(View.GONE);
        mRightView.setVisibility(View.GONE);
    }

    @Override
    protected void onDestroy() {
        if (unbinder != null) {
            unbinder.unbind();
        }
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        setDisableGlobalListen(true);
        super.onResume();
        speak(geTip());
    }

    protected int geTip() {
        return R.string.sign_up_height_tip;
    }

    protected int selectedPosition = 20;

    protected void initView() {
        tvUnit.setText("cm");
        GalleryLayoutManager layoutManager = new GalleryLayoutManager(GalleryLayoutManager.VERTICAL);
        adapter = new SelectAdapter();
        adapter.setStrings(getStrings());
        adapter.setOnItemClickListener(new SelectAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                rvContent.smoothScrollToPosition(position);
            }
        });
        selectedPosition();
        layoutManager.attach(rvContent, selectedPosition);
        layoutManager.setCallbackInFling(true);
        layoutManager.setOnItemSelectedListener(new GalleryLayoutManager.OnItemSelectedListener() {
            @Override
            public void onItemSelected(RecyclerView recyclerView, View item, int position) {
                selectedPosition = position;
                select((String) (mStrings == null ? String.valueOf(position) : mStrings.get(position)));
            }
        });

        rvContent.setAdapter(adapter);
    }

    protected void selectedPosition() {
        if (getIntent() != null && getIntent().getIntExtra("wheight", 0) != 0) {
            selectedPosition = findIndex(getIntent().getIntExtra("wheight", 0));
        }
    }

    private int findIndex(int value) {
        int size = mStrings.size();
        for (int i = 0; i < size; i++) {
            if (Integer.parseInt(mStrings.get(i)) == value) {
                return i;
            }
        }
        return selectedPosition;
    }


    protected List<String> getStrings() {
        mStrings = new ArrayList<>();
        for (int i = 150; i < 200; i++) {
            mStrings.add(String.valueOf(i));
        }
        return mStrings;
    }

    @OnClick(R.id.tv_sign_up_go_back)
    public void onTvGoBackClicked() {
        finish();
    }

    @OnClick(R.id.tv_sign_up_go_forward)
    public void onTvGoForwardClicked() {
        String height = mStrings.get(selectedPosition);
        LocalShared.getInstance(this.getApplicationContext()).setSignUpHeight(Integer.valueOf(height));

        if (getIntent() != null) {
            int weightModify = getIntent().getIntExtra("weightModify", 0);
            int weight = getIntent().getIntExtra("weight", 0);
            if (weightModify >= 90) {
                Intent intent = new Intent(this, SignUp8WeightActivity.class)
                        .putExtras(getIntent());
                startActivity(intent);
            } else {
                LocalShared.getInstance(this.getApplicationContext()).setSignUpWeight(weight);
                Intent intent = new Intent(this, SignUp3AddressActivity.class);
                startActivity(intent);
            }
        }
    }

    public void select(String text) {
        T.show(text);
    }

    public static final String REGEX_IN_GO_BACK = ".*(上一步|上一部|后退|返回).*";
    public static final String REGEX_IN_GO_FORWARD = ".*(下一步|下一部|确定|完成).*";

    @Override
    protected void onSpeakListenerResult(String result) {
        T.show(result);

        if (result.matches(REGEX_IN_GO_BACK)) {
            onTvGoBackClicked();
            return;
        }
        if (result.matches(REGEX_IN_GO_FORWARD)) {
            onTvGoForwardClicked();
            return;
        }

        String in = Utils.isNumeric(result) ? result : Utils.removeNonnumeric(Utils.chineseMapToNumber(result));
        selectItem(in);
    }

    protected void selectItem(String in) {
        int size = mStrings.size();
        for (int i = 0; i < size; i++) {
            String height = mStrings.get(i);
            if (in.equals(height)) {
                rvContent.smoothScrollToPosition(i);
                return;
            }
        }
    }
}
