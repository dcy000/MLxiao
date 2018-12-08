package com.example.module_register.ui.normal;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.module_register.R;
import com.example.module_register.R2;
import com.gcml.lib_location.adapter.SelectAdapter;
import com.example.module_register.ui.base.VoiceToolBarActivity;
import com.gzq.lib_core.base.Box;
import com.gzq.lib_core.base.ui.BasePresenter;
import com.gzq.lib_core.base.ui.IPresenter;
import com.gzq.lib_core.bean.UserInfoBean;
import com.gzq.lib_core.utils.DataUtils;
import com.gzq.lib_core.utils.ToastUtils;
import com.iflytek.synthetize.MLVoiceSynthetize;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import github.hellocsl.layoutmanager.gallery.GalleryLayoutManager;

public class SignUp7HeightActivity extends VoiceToolBarActivity {

    @BindView(R2.id.tv_sign_up_height)
    TextView tvTitle;
    @BindView(R2.id.rv_sign_up_content)
    RecyclerView rvContent;
    @BindView(R2.id.tv_sign_up_unit)
    TextView tvUnit;
    @BindView(R2.id.tv_sign_up_go_back)
    TextView tvGoBack;
    @BindView(R2.id.tv_sign_up_go_forward)
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
    public int layoutId(Bundle savedInstanceState) {
        return R.layout.activity_sign_up7_height;
    }

    @Override
    public void initParams(Intent intentArgument) {

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
        super.onResume();
        setDisableWakeup(true);
        robotStartListening();
        MLVoiceSynthetize.startSynthesize(geTip());
    }

    protected int geTip() {
        return R.string.sign_up_height_tip;
    }

    protected int selectedPosition = 20;

    @Override
    public void initView() {
        unbinder = ButterKnife.bind(this);
        tvUnit.setText("cm");
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
    }

    @Override
    public boolean isShowVoiceView() {
        return true;
    }

    @Override
    protected boolean isShowToolbar() {
        return false;
    }

    @Override
    public IPresenter obtainPresenter() {
        return new BasePresenter(this) {};
    }

    protected List<String> getStrings() {
        mStrings = new ArrayList<>();
        for (int i = 150; i < 200; i++) {
            mStrings.add(String.valueOf(i));
        }
        return mStrings;
    }

    @OnClick(R2.id.tv_sign_up_go_back)
    public void onTvGoBackClicked() {
        finish();
    }

    @OnClick(R2.id.tv_sign_up_go_forward)
    public void onTvGoForwardClicked() {
        String height = mStrings.get(selectedPosition);
        cacheUserInfo(height);
        Intent intent = new Intent(this, SignUp8WeightActivity.class);
        startActivity(intent);
    }

    private void cacheUserInfo(String height) {
        UserInfoBean user = Box.getSessionManager().getUser();
        if (user == null) {
            user = new UserInfoBean();
        }
        user.height = height;
        Box.getSessionManager().setUser(user);
    }

    public void select(String text) {
        ToastUtils.showShort(text);
    }

    public static final String REGEX_IN_GO_BACK = ".*(上一步|上一部|后退|返回).*";
    public static final String REGEX_IN_GO_FORWARD = ".*(下一步|下一部|确定|完成).*";

    @Override
    protected void onSpeakListenerResult(String result) {
        ToastUtils.showShort(result);

        if (result.matches(REGEX_IN_GO_BACK)) {
            onTvGoBackClicked();
            return;
        }
        if (result.matches(REGEX_IN_GO_FORWARD)) {
            onTvGoForwardClicked();
            return;
        }

        String in = DataUtils.isInteger(result) ? result : DataUtils.removeNonnumeric(DataUtils.chineseMapToNumber(result));
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
