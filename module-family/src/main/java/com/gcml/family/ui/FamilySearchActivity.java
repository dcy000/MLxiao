package com.gcml.family.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.billy.cc.core.component.CC;
import com.gcml.common.utils.display.KeyboardUtils;
import com.gcml.common.widget.toolbar.ToolBarClickListener;
import com.gcml.common.widget.toolbar.TranslucentToolBar;
import com.gcml.family.R;
import com.gcml.family.adapter.FamilyFriendAdapter;
import com.gcml.family.bean.FamilyBean;

import java.util.ArrayList;
import java.util.List;

public class FamilySearchActivity extends AppCompatActivity implements View.OnClickListener {

    TranslucentToolBar mToolBar;
    RecyclerView mRecycler;
    FamilyFriendAdapter mAdapter;
    EditText mSearchEdit;
    TextView mSearchText, mSrarchHint;
    RelativeLayout mFamilyContent;
    LinearLayout mSearchContent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_family_search);

        bindView();
        bindData();
    }

    private void bindView() {
        mToolBar = findViewById(R.id.tb_family_search);
        mRecycler = findViewById(R.id.rv_search_list);
        mSearchEdit = findViewById(R.id.et_family_search);
        mSearchText = findViewById(R.id.tv_family_search);
        mSrarchHint = findViewById(R.id.tv_family_search_hint);
        mFamilyContent = findViewById(R.id.rl_content);
        mSearchContent = findViewById(R.id.ll_search_content);
    }

    private void bindData() {
        mToolBar.setData("添 加 联 系 人", R.drawable.common_btn_back, "返回", R.drawable.common_btn_home, null, new ToolBarClickListener() {
            @Override
            public void onLeftClick() {
                finish();
            }

            @Override
            public void onRightClick() {
                CC.obtainBuilder("app").setActionName("ToMainActivity").build().callAsync();
                finish();
            }
        });
        mSearchEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
//                    mSrarchHint.setVisibility(View.VISIBLE);
                    mSrarchHint.setText(mSearchEdit.getText().toString());
                } else {
//                    mSrarchHint.setVisibility(View.GONE);
                    mSrarchHint.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mSearchEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // 获得焦点
                    if (mSearchContent.getVisibility() == View.VISIBLE) {
                        TranslateAnimation hideAnim = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                                Animation.RELATIVE_TO_SELF, 0.0f,
                                Animation.RELATIVE_TO_SELF, 0.0f,
                                Animation.RELATIVE_TO_SELF, 1.0f);
                        hideAnim.setDuration(500);
                        mSearchContent.startAnimation(hideAnim);
                        mSearchContent.setVisibility(View.GONE);
                    }
                    mSrarchHint.setVisibility(View.VISIBLE);
                } else {
                    // 失去焦点
                    if (mSearchContent.getVisibility() == View.GONE) {
                        TranslateAnimation showAnim = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                                Animation.RELATIVE_TO_SELF, 0.0f,
                                Animation.RELATIVE_TO_SELF, 1.0f,
                                Animation.RELATIVE_TO_SELF, 0.0f);
                        showAnim.setDuration(500);
                        mSearchContent.startAnimation(showAnim);
                        mSearchContent.setVisibility(View.VISIBLE);
                    }
                    mSrarchHint.setVisibility(View.GONE);
                    KeyboardUtils.hideSoftInput(FamilySearchActivity.this);
                }
            }
        });
        mRecycler.setLayoutManager(new GridLayoutManager(FamilySearchActivity.this, 2));
        mAdapter = new FamilyFriendAdapter(R.layout.item_family_search, getFamilyList());
        mRecycler.setAdapter(mAdapter);
        mSearchText.setOnClickListener(this);
        mSrarchHint.setOnClickListener(this);
    }

    private List<FamilyBean> getFamilyList() {
        List<FamilyBean> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            if (i < 5) {
                list.add(new FamilyBean("郭志强", "添加"));
            } else {
                list.add(new FamilyBean("曾庆森", "添加"));
            }
        }
        return list;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_family_search) {
            if (mSrarchHint.getVisibility() == View.VISIBLE) {
                mSrarchHint.setVisibility(View.GONE);
            }
            mSearchEdit.clearFocus();
        } else if (v.getId() == R.id.tv_family_search_hint) {
            if (mSrarchHint.getVisibility() == View.VISIBLE) {
                mSrarchHint.setVisibility(View.GONE);
            }
            mSearchEdit.clearFocus();
        }
    }
}
