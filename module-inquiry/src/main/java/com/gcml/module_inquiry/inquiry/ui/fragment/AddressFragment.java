package com.gcml.module_inquiry.inquiry.ui.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.gcml.common.AppDelegate;
import com.gcml.common.FilterClickListener;
import com.gcml.common.data.Province;
import com.gcml.common.utils.DefaultObserver;
import com.gcml.common.utils.RxUtils;
import com.gcml.common.utils.Utils;
import com.gcml.common.utils.display.ToastUtils;
import com.gcml.module_inquiry.R;
import com.gcml.module_inquiry.inquiry.ui.fragment.base.InquiryBaseFrament;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by lenovo on 2019/3/21.
 */

public class AddressFragment extends InquiryBaseFrament implements View.OnClickListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private TextView tvGoBack;
    private TextView tvGoForward;
    private TextView spProvince;
    private TextView spCity;
    private TextView spCounty;
    private EditText etAddress;

    private LocationClient mLocationClient;
    private String finalDetailAddress = "";

    @Override
    protected int layoutId() {
        return R.layout.fragment_address;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        tvGoBack = view.findViewById(R.id.tv_sign_up_go_back);
        tvGoForward = view.findViewById(R.id.tv_sign_up_go_forward);
        spProvince = view.findViewById(R.id.sp_province);
        spCity = view.findViewById(R.id.sp_city);
        spCounty = view.findViewById(R.id.sp_county);
        etAddress = view.findViewById(R.id.et_sign_up_address);

        tvGoBack.setOnClickListener(new FilterClickListener(this));
        tvGoForward.setOnClickListener(new FilterClickListener(this));

        spCity.setOnClickListener(new FilterClickListener(this));
        spCounty.setOnClickListener(new FilterClickListener(this));
        spProvince.setOnClickListener(new FilterClickListener(this));
        initLocation();
        initJsonData();
    }

    @Override
    public void onResume() {
        super.onResume();
        startLocation();
    }

    private void startLocation() {
        if (mListener != null && mLocationClient != null) {
            mLocationClient.registerLocationListener(mListener);
        }
        if (mLocationClient != null && !mLocationClient.isStarted()) {
            mLocationClient.start();
        }
    }

    public static AddressFragment newInstance(String param1, String param2) {
        AddressFragment fragment = new AddressFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    private List<Province> options1Items = new ArrayList<>();
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<String>>> options3Items = new ArrayList<>();

    private void showPickerView() {// 弹出选择器

        OptionsPickerView pvOptions = new OptionsPickerBuilder(getContext(), (OnOptionsSelectListener) (options1, options2, options3, v) -> {
            //返回的分别是三个级别的选中位置
            String opt1tx = options1Items.size() > 0 ?
                    options1Items.get(options1).getPickerViewText() : "";

            String opt2tx = options2Items.size() > 0
                    && options2Items.get(options1).size() > 0 ?
                    options2Items.get(options1).get(options2) : "";

            String opt3tx = options2Items.size() > 0
                    && options3Items.get(options1).size() > 0
                    && options3Items.get(options1).get(options2).size() > 0 ?
                    options3Items.get(options1).get(options2).get(options3) : "";

            spProvince.setText(opt1tx);
            spCity.setText(opt2tx);
            spCounty.setText(opt3tx);

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

        /*pvOptions.setPicker(options1Items);//一级选择器
        pvOptions.setPicker(options1Items, options2Items);//二级选择器*/
        pvOptions.setPicker(options1Items, options2Items, options3Items);//三级选择器
        pvOptions.show();
    }

    private Observable<String> jsonData() {
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                String provincesArr = Utils.readTextFromAssetFile(AppDelegate.INSTANCE.app(), "cities.json");
                emitter.onNext(provincesArr);
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io());
    }

    private void initJsonData() {//解析数据
        jsonData().observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> {
                })
                .map((Function<String, List<Province>>) provincesArr -> {
                    Gson gson = new Gson();
                    return gson.fromJson(provincesArr, new TypeToken<List<Province>>() {
                    }.getType());
                })
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new DefaultObserver<List<Province>>() {

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                    }

                    @Override
                    public void onNext(List<Province> provinces) {
                        super.onNext(provinces);
                        options1Items = provinces;
                        for (int i = 0; i < options1Items.size(); i++) {//遍历省份
                            ArrayList<String> cityList = new ArrayList<>();//该省的城市列表（第二级）
                            ArrayList<ArrayList<String>> province_AreaList = new ArrayList<>();//该省的所有地区列表（第三极）

                            for (int c = 0; c < options1Items.get(i).getCities().size(); c++) {//遍历该省份的所有城市
                                String cityName = options1Items.get(i).getCities().get(c).getName();
                                cityList.add(cityName);//添加城市
                                ArrayList<String> city_AreaList = new ArrayList<>();//该城市的所有地区列表

                                city_AreaList.addAll(options1Items.get(i).getCities().get(c).getCounties());
                                province_AreaList.add(city_AreaList);//添加该省所有地区数据
                            }

                            options2Items.add(cityList);
                            options3Items.add(province_AreaList);
                        }
                    }
                });


    }

    private void initLocation() {
        mLocationClient = new LocationClient(AppDelegate.INSTANCE.app());
        LocationClientOption locOption = new LocationClientOption();
        locOption.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        locOption.setCoorType("bd09ll");
        locOption.setIsNeedAddress(true);
        locOption.setOpenGps(true);
        locOption.setScanSpan(3000);
        mLocationClient.setLocOption(locOption);
    }

    @Override
    public void onPause() {
        stopLocation();
        super.onPause();
    }

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

    private void stopLocation() {
        if (mLocationClient != null && mLocationClient.isStarted()) {
            mLocationClient.stop();
        }
        if (mListener != null && mLocationClient != null) {
            mLocationClient.unRegisterLocationListener(mListener);
        }
    }

    private String getAddress() {

        String address = "";
        address = address + spProvince.getText().toString().trim();
//        if (!address.contains("省")) {
//            address = address + "省";
//        }

        address = address + spCity.getText().toString().trim();
//        if (!address.contains("市")) {
//            address = address + "市";
//        }

        address = address + spCounty.getText().toString().trim();
//        if (!address.contains("区")) {
//            address = address + "区";
//        }
        return address + etAddress.getText().toString().trim();

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.tv_sign_up_go_back) {
            if (listenerAdapter != null) {
                listenerAdapter.onBack("3", null);
            }
        } else if (id == R.id.tv_sign_up_go_forward) {
            if (TextUtils.isEmpty(getAddress().replaceAll(" ", ""))) {
                ToastUtils.showShort("请完善地址信息");
                return;
            }

            if (TextUtils.isEmpty(etAddress.getText().toString().trim())) {
                ToastUtils.showShort("请完善地址信息");
                return;
            }
            if (listenerAdapter != null) {
                listenerAdapter.onNext("3", getAddress());
            }
        } else if (id == R.id.sp_province) {
            showCityPikcker();
        } else if (id == R.id.sp_city) {
            showCityPikcker();
        } else if (id == R.id.sp_county) {
            showCityPikcker();
        }
    }

    private void showCityPikcker() {
        showPickerView();
    }


}
