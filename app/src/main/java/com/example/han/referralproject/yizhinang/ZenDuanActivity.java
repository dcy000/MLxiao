package com.example.han.referralproject.yizhinang;

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
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.network.AppRepository;
import com.gcml.common.utils.DefaultObserver;
import com.gcml.common.utils.RxUtils;
import com.gcml.common.utils.display.ToastUtils;
import com.gcml.common.utils.ui.UiUtils;
import com.gcml.common.widget.dialog.LoadingDialog;
import com.google.gson.Gson;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ZenDuanActivity extends com.gcml.common.base.BaseActivity implements RadioGroup.OnCheckedChangeListener {

    private List<Fragment> fragments;
    private RadioGroup mRgMenu;
    private ViewPager mVpGoods;
    private LoadingDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zenduan);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        TextView input = findViewById(R.id.et_item_name);
        findViewById(R.id.rv_back).setOnClickListener(v -> finish());

        findViewById(R.id.iv_search_items).setOnClickListener(v -> {
            String inputText = input.getText().toString().trim().replaceAll(" ", "");
            if (TextUtils.isEmpty(inputText)) {
                ToastUtils.showShort("请输入关键词");
                return;
            }
            startActivity(new Intent(this, ZenDuanActivity.class).putExtra("inputText", inputText));
            finish();
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

    AppRepository repository = new AppRepository();
   /* AppId	g5NwRNJknq
    CurTime	1556594397
    Param	eyJ1c2VySWQiOiJ1c2VyMDAwMDEiLCJpbnB1dCI6ICLns5blsL/nl4UifQ==
    Token:	E55BEE155007EDFE4FE10DBCAA627B81*/

//    MD5(apiKey + curTime + param)

    private static final String APP_ID = "g5NwRNJknq";
    private static final String APP_KEY = "05539d97326d4f68aef161fec74d3087";

    private void requestData(AppRepository repository, String input) {
        mRgMenu.setOnCheckedChangeListener(this);
        mVpGoods.setOffscreenPageLimit(1);

        dialog = new LoadingDialog.Builder(this)
                .setIconType(LoadingDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord("正在加载")
                .create();


        InputBean src = new InputBean();
        src.input = input;
        String inputJson = new Gson().toJson(src);
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
                        if (data != null) {
                            List<OutBean.LinksBean> links = data.links;
                            initFragments(links);
                            initRadioGroup(links);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        ToastUtils.showShort(throwable.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                    }
                });
    }

    private void initFragments(List<OutBean.LinksBean> data) {
        fragments = new ArrayList<>();
        int size = data.size();
        for (int i = 0; i < size; i++) {
            fragments.add(ZenDuanItemsFragment.newInstance(data.get(i).data));
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
