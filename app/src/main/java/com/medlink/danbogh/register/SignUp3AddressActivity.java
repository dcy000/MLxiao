package com.medlink.danbogh.register;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.application.MyApplication;
import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.require4.bean.InquiryInfoResponseBean;
import com.example.han.referralproject.util.LocalShared;
import com.example.han.referralproject.yiyuan.activity.DrinkInfoActivity;
import com.example.han.referralproject.yiyuan.activity.PregnancyWenActivity;
import com.example.han.referralproject.yiyuan.util.ActivityHelper;
import com.google.gson.Gson;
import com.lljjcoder.Interface.OnCityItemClickListener;
import com.lljjcoder.bean.CityBean;
import com.lljjcoder.bean.DistrictBean;
import com.lljjcoder.bean.ProvinceBean;
import com.lljjcoder.citywheel.CityConfig;
import com.lljjcoder.style.citypickerview.CityPickerView;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.medlink.danbogh.utils.T;
import com.medlink.danbogh.utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class SignUp3AddressActivity extends BaseActivity {
    @BindView(R.id.tv_sign_up_go_back)
    TextView tvGoBack;
    @BindView(R.id.tv_sign_up_go_forward)
    TextView tvGoForward;
    @BindView(R.id.sp_province)
    TextView spProvince;
    @BindView(R.id.sp_city)
    TextView spCity;
    @BindView(R.id.sp_county)
    TextView spCounty;
    @BindView(R.id.et_sign_up_address)
    EditText etAddress;

    private Unbinder mUnbinder;
    CityPickerView cityPicker = new CityPickerView();

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, SignUp3AddressActivity.class);
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setShowVoiceView(true);
        setContentView(R.layout.activity_sign_up3_address);
        mToolbar.setVisibility(View.GONE);
        mUnbinder = ButterKnife.bind(this);
        initLocation();
        initTitle();
        initCityPicker();
        ActivityHelper.addActivity(this);
        getAddressInfo();
    }

    private void getAddressInfo() {
        showLoadingDialog("");
        NetworkApi.getInquiryInfo(MyApplication.getInstance().userId, new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                try {
                    String body = response.body();
                    InquiryInfoResponseBean inquiryInfoResponseBean = new Gson().fromJson(body, InquiryInfoResponseBean.class);
                    if (inquiryInfoResponseBean.tag) {
                        InquiryInfoResponseBean.DataBean data = inquiryInfoResponseBean.data;
                        if (data != null && data.address != null) {
                            if (data.address.contains("省") && data.address.contains("市") && data.address.contains("区")) {
                                String[] province = data.address.split("省");
                                spProvince.setText(province[0]);
                                spCity.setText(province[1].split("市")[0]);
                                spCounty.setText(province[1].split("市")[1].split("区")[0]);
                                etAddress.setText(data.address.split("区")[1]);
                                Integer addressModifyDays = data.addressModifyDays;
                                if (addressModifyDays != null && addressModifyDays >= 90) {
                                    findViewById(R.id.tv_address_tip).setVisibility(View.VISIBLE);
                                }
                            } else {
                                startLocation();
                            }
                        } else {
                            startLocation();
                        }
                    }
                } catch (Exception e) {
                    startLocation();
                }
            }

            @Override
            public void onError(Response<String> response) {
                super.onError(response);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                hideLoadingDialog();
            }
        });
    }

    private void initTitle() {
        mTitleText.setText("问诊");
        mToolbar.setVisibility(View.VISIBLE);
        mRightText.setVisibility(View.GONE);
        mRightView.setVisibility(View.GONE);
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
                    spProvince.setText(province.getName());
                }

                //城市
                if (city != null) {
                    spCity.setText(city.getName());
                }

                //地区
                if (district != null) {
                    spCounty.setText(district.getName());
                }

            }

            @Override
            public void onCancel() {
            }
        });

    }

    private void initLocation() {
        mLocationClient = new LocationClient(getApplicationContext());
        LocationClientOption locOption = new LocationClientOption();
        locOption.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        locOption.setCoorType("bd09ll");
        locOption.setIsNeedAddress(true);
        locOption.setOpenGps(true);
        locOption.setScanSpan(3000);
        mLocationClient.setLocOption(locOption);
    }

    private LocationClient mLocationClient;

    private String finalDetailAddress = "";

    private BDAbstractLocationListener mListener = new BDAbstractLocationListener() {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            stopLocation();

            String province = bdLocation.getProvince();
            String city = bdLocation.getCity();
            String county = bdLocation.getDistrict();
            String street = bdLocation.getStreet();
            String streetNumber = bdLocation.getStreetNumber();

            finalDetailAddress = street + streetNumber;
            spProvince.setText(province);
            spCity.setText(city);
            spCounty.setText(county);
            etAddress.setText(finalDetailAddress);
            etAddress.setSelection(finalDetailAddress.length());
        }
    };

    @Override
    protected void onDestroy() {
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setDisableGlobalListen(true);
        speak(R.string.sign_up3_address_tip);
    }

    private void startLocation() {
        if (mListener != null && mLocationClient != null) {
            mLocationClient.registerLocationListener(mListener);
        }
        if (mLocationClient != null && !mLocationClient.isStarted()) {
            mLocationClient.start();
        }
    }

    @Override
    protected void onPause() {
        stopLocation();
        super.onPause();
    }

    private void stopLocation() {
        if (mLocationClient != null && mLocationClient.isStarted()) {
            mLocationClient.stop();
        }
        if (mListener != null && mLocationClient != null) {
            mLocationClient.unRegisterLocationListener(mListener);
        }
    }

    @OnClick(R.id.cl_sign_up_root_address)
    public void onClRootClicked() {
        View view = getCurrentFocus();
        if (view != null) {
            Utils.hideKeyBroad(view);
        }
    }

    @OnClick(R.id.tv_sign_up_go_back)
    public void onTvGoBackClicked() {
        finish();
    }

    @OnClick(R.id.tv_sign_up_go_forward)
    public void onTvGoForwardClicked() {
        String address = etAddress.getText().toString().trim();
        if (TextUtils.isEmpty(address)) {
            T.show(R.string.sign_up3_address_tip);
            speak(R.string.sign_up3_address_tip);
            return;
        }

        LocalShared.getInstance(this.getApplicationContext()).setSignUpAddress(getAddress());
//        Intent intent = SignUp7HeightActivity.newIntent(this);
//        startActivity(intent);

        if ("女".equals(LocalShared.getInstance(this).getSex())) {
            startActivity(new Intent(this, PregnancyWenActivity.class));
        } else {
            startActivity(new Intent(this, DrinkInfoActivity.class));
        }
    }

    private String getAddress() {

        String address = "";
        address = address + spProvince.getText().toString().trim();
        if (!address.contains("省")) {
            address = address + "省";
        }

        address = address + spCity.getText().toString().trim();
        if (!address.contains("市")) {
            address = address + "市";
        }

        address = address + spCounty.getText().toString().trim();
        if (!address.contains("区")) {
            address = address + "区";
        }
        return address + etAddress.getText().toString().trim();

      /*  StringBuilder builder = new StringBuilder();
        builder.append(spProvince.getText().toString().trim())
                .append(spCity.getText().toString().trim())
                .append(spCounty.getText().toString().trim())
                .append(etAddress.getText().toString().trim());
        return builder.toString();*/
    }

    @OnClick({R.id.sp_province, R.id.sp_city, R.id.sp_county})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.sp_province:
            case R.id.sp_city:
            case R.id.sp_county:
                cityPicker.showCityPicker();
                break;
        }
    }
}
