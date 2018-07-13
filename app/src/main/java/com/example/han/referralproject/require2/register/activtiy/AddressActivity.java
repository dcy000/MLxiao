package com.example.han.referralproject.require2.register.activtiy;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.activity.WifiConnectActivity;
import com.example.han.referralproject.require2.wrap.CanClearEditText;
import com.lljjcoder.Interface.OnCityItemClickListener;
import com.lljjcoder.bean.CityBean;
import com.lljjcoder.bean.DistrictBean;
import com.lljjcoder.bean.ProvinceBean;
import com.lljjcoder.citywheel.CityConfig;
import com.lljjcoder.style.citylist.Toast.ToastUtils;
import com.lljjcoder.style.citypickerview.CityPickerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.han.referralproject.require2.register.activtiy.IDCardNumberRegisterActivity.REGISTER_ADDRESS;

public class AddressActivity extends BaseActivity {

    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.tv_province_info)
    TextView tvProvinceInfo;
    @BindView(R.id.tv_city_info)
    TextView tvCityInfo;
    @BindView(R.id.tv_block_info)
    TextView tvBlockInfo;
    @BindView(R.id.tv_province)
    TextView tvProvince;
    @BindView(R.id.tv_city)
    TextView tvCity;
    @BindView(R.id.tv_city2)
    TextView tvCity2;
    @BindView(R.id.canClearEditText)
    CanClearEditText canClearEditText;
    @BindView(R.id.tv_next)
    TextView tvNext;

    CityPickerView cityPicker = new CityPickerView();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        ButterKnife.bind(this);
        initTile();
        initCityPicker();

    }

    private void initCityPicker() {
        cityPicker.init(this);
        //添加默认的配置，不需要自己定义
        CityConfig cityConfig = new CityConfig.Builder()
                .title("选择城市").title("选择城市")//标题
                .titleTextSize(26)//标题文字大小
                .titleTextColor("#585858")//标题文字颜  色
                .titleBackgroundColor("#E9E9E9")//标题栏背景色

                .confirTextColor("#585858")//确认按钮文字颜色
                .confirmText("确定")//确认按钮文字
                .confirmTextSize(20)//确认按钮文字大小

                .cancelTextColor("#585858")//取消按钮文字颜色
                .cancelText("取消")//取消按钮文字
                .cancelTextSize(20)//取消按钮文字大小

                .setCityWheelType(CityConfig.WheelType.PRO_CITY_DIS)//显示类，只显示省份一级，显示省市两级还是显示省市区三级
                .showBackground(true)//是否显示半透明背景
                .visibleItemsCount(5)//显示item的数量
                .province("浙江省")//默认显示的省份
                .city("杭州市")//默认显示省份下面的城市
                .district("滨江区")//默认显示省市下面的区县数据
                .setLineColor("#03a9f4")//中间横线的颜色
                .setLineHeigh(2)//中间横线的高度
                .setShowGAT(true)//是否显示港澳台数据，默认不显示
                .build();
        cityPicker.setConfig(cityConfig);
        cityPicker.setOnCityItemClickListener(new OnCityItemClickListener() {
            @Override
            public void onSelected(ProvinceBean province, CityBean city, DistrictBean district) {

                //省份
                if (province != null) {
                    tvProvinceInfo.setText(province.getName());
                }

                //城市
                if (city != null) {
                    tvCityInfo.setText(city.getName());
                }

                //地区
                if (district != null) {
                    tvBlockInfo.setText(district.getName());
                }

            }

            @Override
            public void onCancel() {
            }
        });

    }

    private void initTile() {
        mToolbar.setVisibility(View.VISIBLE);
        mTitleText.setText("身 份 证 号 码 注 册");

        mLeftText.setVisibility(View.VISIBLE);
        mLeftView.setVisibility(View.VISIBLE);

        mRightText.setVisibility(View.GONE);
        mRightView.setVisibility(View.VISIBLE);
        mRightView.setImageResource(R.drawable.yiyua_wifi_icon);
        mRightView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AddressActivity.this, WifiConnectActivity.class));
            }
        });

        mlSpeak("请输入您的户籍地址");

        canClearEditText.setIsChinese(true);
        canClearEditText.setHintText("请输入详细地址");
    }

    @OnClick({R.id.tv_province_info, R.id.tv_city_info, R.id.tv_block_info, R.id.tv_next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_province_info:
            case R.id.tv_city_info:
            case R.id.tv_block_info:
                cityPicker.showCityPicker();
                break;
            case R.id.tv_next:
                String detailAddress = canClearEditText.getPhone();
                if (TextUtils.isEmpty(detailAddress)) {
                    mlSpeak("请输入详细地址");
                }
                String address = tvProvinceInfo.getText().toString() + tvCity.getText().toString() + tvBlockInfo.getText().toString() + detailAddress;
                startActivity(new Intent(this, InputFaceActivity.class)
                        .putExtras(getIntent())
                        .putExtra(REGISTER_ADDRESS, address));
                break;
        }
    }
}
