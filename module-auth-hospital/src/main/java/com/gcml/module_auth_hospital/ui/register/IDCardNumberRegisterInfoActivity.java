package com.gcml.module_auth_hospital.ui.register;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.gcml.common.utils.RxUtils;
import com.gcml.common.widget.toolbar.ToolBarClickListener;
import com.gcml.common.widget.toolbar.TranslucentToolBar;
import com.gcml.module_auth_hospital.R;

import java.util.Arrays;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


public class IDCardNumberRegisterInfoActivity extends AppCompatActivity implements View.OnClickListener {
    private TranslucentToolBar translucentToolBar;
    private String number;
    private EditText etRegisterName;
    /**
     * 请输入您的民族
     */
    private EditText etRegisterMinzu;
    /**
     * 身份证号码
     */
    private TextView etRegisterIdcrad;
    private EditText etRegisterNowAddress;
    /**
     * 请输入您的详细地址
     */
    private EditText etRegisterDetailAddress;
    /**
     * 下一步
     */
    private TextView tvAuthNext;
    private TextView authTvMan;
    private TextView authTvWoman;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_idcard_number_register_info);
        initExtra();
        initView();
    }

    private void initExtra() {
        Intent data = getIntent();
        if (data != null) {
            number = data.getStringExtra(ScanIdCardRegisterActivity.REGISTER_IDCARD_NUMBER);
        }
    }

    private void initView() {
        translucentToolBar = (TranslucentToolBar) findViewById(R.id.auth_idcard_numer_register_info_tb);
        etRegisterName = findViewById(R.id.et_register_name);
        etRegisterMinzu = (EditText) findViewById(R.id.et_register_minzu);
        etRegisterMinzu.setOnClickListener(this);
        authTvMan = (TextView) findViewById(R.id.auth_tv_man);
        authTvMan.setOnClickListener(this);
        authTvWoman = (TextView) findViewById(R.id.auth_tv_woman);
        authTvWoman.setOnClickListener(this);
        etRegisterIdcrad = findViewById(R.id.et_register_idcrad);
        etRegisterNowAddress = findViewById(R.id.et_register_now_address);
        etRegisterNowAddress.setOnClickListener(this);
        etRegisterDetailAddress = (EditText) findViewById(R.id.et_register_detail_address);
        tvAuthNext = (TextView) findViewById(R.id.tv_auth_next);
        tvAuthNext.setOnClickListener(this);

        etRegisterIdcrad.setText(number);


        translucentToolBar.setData("账 号 注 册",
                R.drawable.common_btn_back, "返回",
                R.drawable.common_ic_wifi_state, null, new ToolBarClickListener() {
                    @Override
                    public void onLeftClick() {
                        finish();
                    }

                    @Override
                    public void onRightClick() {

                    }
                });

        RxUtils.rxWifiLevel(getApplication(), 4)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        translucentToolBar.setImageLevel(integer);
                    }
                });
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.et_register_minzu) {
            updateHeight();

        } else if (i == R.id.auth_tv_man) {
        } else if (i == R.id.auth_tv_woman) {
        } else if (i == R.id.et_register_now_address) {
        } else if (i == R.id.tv_auth_next) {
        } else {
        }
    }

    private void updateHeight() {
        OptionsPickerView pvOptions = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
                String nation = (String) getNationItems().get(options1);
                etRegisterMinzu.setText(nation);
            }
        })
                .setCancelText("取消")
                .setSubmitText("确认")
                .setLineSpacingMultiplier(1.5f)
                .setSubCalSize(30)
                .setContentTextSize(40)
                .setSubmitColor(Color.parseColor("#FF108EE9"))
                .setCancelColor(Color.parseColor("#FF999999"))
                .setTextColorOut(Color.parseColor("#FF999999"))
                .setTextColorCenter(Color.parseColor("#FF333333"))
                .setBgColor(Color.WHITE)
                .setTitleBgColor(Color.parseColor("#F5F5F5"))
                .setDividerColor(Color.TRANSPARENT)
                .isCenterLabel(false)
                .setOutSideCancelable(true)
                .build();

        pvOptions.setPicker(getNationItems());
        pvOptions.setSelectOptions(125);
        pvOptions.show();
    }

    private List getNationItems() {
        return Arrays.asList(
                "汉族", "蒙古族", "回族", "藏族", "维吾尔族", "苗族", "彝族", "壮族",
                "布依族", "朝鲜族", "满族", "侗族", "瑶族", "白族", "土家族", "哈尼族", "哈萨克族",
                "傣族", "黎族", "傈僳族", "佤族", "畲族", "高山族", "拉祜族", "水族", "东乡族", "纳西族",
                "景颇族", "柯尔克孜族", "土族", "达斡尔族", "仫佬族", "羌族", "布朗族", "撒拉族", "毛难族",
                "仡佬族", "锡伯族", "阿昌族", "普米族", "塔吉克族", "怒族", "乌孜别克族", "俄罗斯族", "鄂温克族",
                "崩龙族", "保安族", "裕固族", "京族", "塔塔尔族", "独龙族", "鄂伦春族", "赫哲族", "门巴族", "珞巴族", "基诺族"
        );
    }


}
