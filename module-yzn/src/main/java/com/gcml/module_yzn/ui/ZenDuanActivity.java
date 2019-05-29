package com.gcml.module_yzn.ui;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.gcml.common.utils.DefaultObserver;
import com.gcml.common.utils.RxUtils;
import com.gcml.common.utils.base.ToolbarBaseActivity;
import com.gcml.common.utils.display.ToastUtils;
import com.gcml.common.utils.ui.UiUtils;
import com.gcml.common.widget.dialog.LoadingDialog;
import com.gcml.module_yzn.R;
import com.gcml.module_yzn.bean.NewsInputBean;
import com.gcml.module_yzn.bean.OutBean;
import com.gcml.module_yzn.repository.YZNRepository;
import com.gcml.module_yzn.ui.fragment.ZenDuanItemsFragment;
import com.gcml.module_yzn.util.BASE64Encoder;
import com.gcml.module_yzn.util.MD5Util;
import com.google.gson.Gson;
import com.sjtu.yifei.annotation.Route;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@Route(path = "/module/yzn/zenduan/activity")
public class ZenDuanActivity extends ToolbarBaseActivity implements RadioGroup.OnCheckedChangeListener {

    private List<Fragment> fragments;
    private RadioGroup mRgMenu;
    private ViewPager mVpGoods;
    private LoadingDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zenduan);
        initView();
    }

    private void initView() {
        mToolbar.setVisibility(View.GONE);
        EditText input = findViewById(R.id.et_item_name);
        input.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    search(input);
                    return true;
                }
                return false;
            }
        });
        findViewById(R.id.rv_back).setOnClickListener(v -> finish());
        findViewById(R.id.iv_search_items).setOnClickListener(v -> {
            search(input);
        });

        mRgMenu = (RadioGroup) findViewById(R.id.rg_menu);
        mVpGoods = (ViewPager) findViewById(R.id.vp_goods);

        Intent intent = getIntent();
        if (intent != null) {
            String inputText = intent.getStringExtra("inputText");
            if (TextUtils.isEmpty(inputText)) {
                inputText = "糖尿病";
            }
            requestData(repository, inputText);
        }

    }

    private void search(EditText input) {
        String inputText = input.getText().toString().trim().replaceAll(" ", "");
        if (TextUtils.isEmpty(inputText)) {
            ToastUtils.showShort("请输入关键词");
            return;
        }
        startActivity(new Intent(this, ZenDuanActivity.class).putExtra("inputText", inputText));
        finish();
    }

    YZNRepository repository = new YZNRepository();
   /* AppId	g5NwRNJknq
    CurTime	1556594397
    Param	eyJ1c2VySWQiOiJ1c2VyMDAwMDEiLCJpbnB1dCI6ICLns5blsL/nl4UifQ==
    Token:	E55BEE155007EDFE4FE10DBCAA627B81*/

//    MD5(apiKey + curTime + param)

    private static final String APP_ID = "g5NwRNJknq";
    private static final String APP_KEY = "05539d97326d4f68aef161fec74d3087";

    private void requestData(YZNRepository repository, String input) {
        mRgMenu.setOnCheckedChangeListener(this);
        mVpGoods.setOffscreenPageLimit(1);

        dialog = new LoadingDialog.Builder(this)
                .setIconType(LoadingDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord("正在加载")
                .create();


        NewsInputBean newsInputBean = new NewsInputBean();
        newsInputBean.input = input;
        String inputJson = new Gson().toJson(newsInputBean);
        String param = BASE64Encoder.encodeString(inputJson);
//        String param = "eyJ1c2VySWQiOiJ1c2VyMDAwMDEiLCJpbnB1dCI6ICLns5blsL/nl4UifQ==";

        String currentTime = System.currentTimeMillis() / 1000 + "";

        String tokenTemp = APP_KEY + currentTime + param;
        String token = MD5Util.md5Encrypt32Upper(tokenTemp);

        repository.chat(APP_ID, currentTime, param, token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> dialog.show())
                .doOnTerminate(() -> dialog.dismiss())
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new DefaultObserver<OutBean>() {
                    @Override
                    public void onNext(OutBean data) {
                        super.onNext(data);
                        dialog.dismiss();
                        try {
                            if (data != null) {
                                List<OutBean.LinksBean> links = data.links;
                                if (data.links == null) {
                                    links = data.maps;
                                }
                                initFragments(links);
                                initRadioGroup(links);
                            }
                        } catch (Exception e) {
                            ToastUtils.showShort("暂无相关内容");
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        dialog.dismiss();
                        ToastUtils.showShort(throwable.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                        dialog.dismiss();
                    }
                });
    }

    private void initFragments(List<OutBean.LinksBean> data) {
        fragments = new ArrayList<>();
        int size = data.size();
        for (int i = 0; i < size; i++) {
            List<OutBean.LinksBean.DataBean> items = data.get(i).data;
            if (items == null) {
                items = new ArrayList<>();
                OutBean.LinksBean.DataBean bean = new OutBean.LinksBean.DataBean();
                bean.link = data.get(i).link;
                bean.title = data.get(i).name;
                bean.type = data.get(i).type;
                items.add(bean);
            }
            fragments.add(ZenDuanItemsFragment.newInstance(items));
        }

        mVpGoods.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public int getCount() {
                return fragments == null ? 0 : fragments.size();
            }

            @Override
            public Fragment getItem(int i) {
                return fragments.get(i);
            }
        });

    }

    /**
     * 商品列表
     *
     * @param data
     */
    private void initRadioGroup(List<OutBean.LinksBean> data) {
//        initFirstRadioButton();
        mRgMenu.removeAllViews();
        for (int i = 0; i < data.size(); i++) {
            RadioButton button = new RadioButton(this);
            button.setTextSize(28);
            button.setText(data.get(i).name + "");
            button.setButtonDrawable(android.R.color.transparent);
            ViewCompat.setBackground(button, ResourcesCompat.getDrawable(getResources(), R.drawable.bg_rb_history_record, getTheme()));
            button.setTextColor(getResources().getColorStateList(R.color.good_menu_text_color));

            Drawable drawableLeft = getResources().getDrawable(
                    R.drawable.bg_rb_history_record_shape);
            button.setCompoundDrawablesWithIntrinsicBounds(drawableLeft,
                    null, null, null);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, UiUtils.pt(160f));
            button.setGravity(Gravity.CENTER);
            mRgMenu.addView(button, lp);
        }
        mRgMenu.check(mRgMenu.getChildAt(0).getId());
    }

    private void initFirstRadioButton() {
        RadioButton button = new RadioButton(this);
        button.setTextSize(28);
        button.setText("小E推荐");
        button.setButtonDrawable(android.R.color.transparent);
        ViewCompat.setBackground(button, ResourcesCompat.getDrawable(getResources(), R.drawable.bg_rb_history_record, getTheme()));
        button.setTextColor(getResources().getColorStateList(R.color.good_menu_text_color));

        Drawable drawableLeft = getResources().getDrawable(
                R.drawable.bg_rb_history_record_shape);
        button.setCompoundDrawablesWithIntrinsicBounds(drawableLeft,
                null, null, null);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, UiUtils.pt(160f));
        button.setGravity(Gravity.CENTER);
        mRgMenu.addView(button, lp);
    }


    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        for (int i = 0; i < group.getChildCount(); i++) {
            RadioButton childAt = (RadioButton) group.getChildAt(i);
            if (childAt.getId() == checkedId) {
                mVpGoods.setCurrentItem(i);
                childAt.setTextSize(32);
            } else {
                childAt.setTextSize(28);
            }
        }
    }

}
