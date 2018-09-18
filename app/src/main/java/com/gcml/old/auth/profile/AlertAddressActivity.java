package com.gcml.old.auth.profile;

import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.billy.cc.core.component.CC;
import com.billy.cc.core.component.CCResult;
import com.example.han.referralproject.R;
import com.example.han.referralproject.homepage.MainActivity;
import com.example.han.referralproject.network.NetworkApi;
import com.gcml.common.data.UserEntity;
import com.gcml.common.data.UserSpHelper;
import com.gcml.common.repository.utils.DefaultObserver;
import com.gcml.common.utils.RxUtils;
import com.gcml.common.widget.toolbar.ToolBarClickListener;
import com.gcml.common.widget.toolbar.TranslucentToolBar;
import com.gcml.lib_utils.display.ToastUtils;
import com.gcml.old.auth.entity.City;
import com.gcml.old.auth.entity.Province;
import com.gcml.old.auth.entity.UserInfoBean;
import com.gcml.old.auth.profile.otherinfo.bean.PUTUserBean;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.iflytek.synthetize.MLVoiceSynthetize;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.medlink.danbogh.utils.Handlers;
import com.medlink.danbogh.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class AlertAddressActivity extends AppCompatActivity {

    TextView tvGoBack;

    TextView tvGoForward;

    Spinner spProvince;

    Spinner spCity;

    Spinner spCounty;

    EditText etAddress;

    private UserInfoBean data;
    private StringBuffer buffer;
    protected String eat = "", smoke = "", drink = "", exercise = "";
    private ConstraintLayout clRoot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert_address_new);
        clRoot = (ConstraintLayout) findViewById(R.id.cl_sign_up_root_address);
        tvGoBack = (TextView) findViewById(R.id.tv_sign_up_go_back);
        tvGoForward = (TextView) findViewById(R.id.tv_sign_up_go_forward);
        spProvince = (Spinner) findViewById(R.id.sp_province);
        spCity = (Spinner) findViewById(R.id.sp_city);
        spCounty = (Spinner) findViewById(R.id.sp_county);
        etAddress = (EditText) findViewById(R.id.et_sign_up_address);

        TranslucentToolBar toolBar = findViewById(R.id.tb_address_title);
        toolBar.setData("修改住址", R.drawable.common_icon_back, "返回",
                R.drawable.common_icon_home, null, new ToolBarClickListener() {
                    @Override
                    public void onLeftClick() {
                        finish();
                    }

                    @Override
                    public void onRightClick() {
                        startActivity(new Intent(AlertAddressActivity.this, MainActivity.class));
                        finish();
                    }
                });

        tvGoBack.setText("取消");
        tvGoForward.setText("确定");
        data = (UserInfoBean) getIntent().getSerializableExtra("data");
        clRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClRootClicked();
            }
        });
        tvGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onTvGoBackClicked();
            }
        });
        tvGoForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onTvGoForwardClicked();
            }
        });
        buffer = new StringBuffer();
        initData();
        initLocation();
    }

    private void initLocation() {
        if (!TextUtils.isEmpty(data.eatingHabits)) {
            switch (data.eatingHabits) {
                case "荤素搭配":
                    eat = "1";
                    break;
                case "偏好吃荤":
                    eat = "2";
                    break;
                case "偏好吃素":
                    eat = "3";
                    break;
                case "偏好吃咸":
                    break;
                case "偏好油腻":
                    break;
                case "偏好甜食":
                    break;
            }
        }
        if (!TextUtils.isEmpty(data.smoke)) {
            switch (data.smoke) {
                case "经常抽烟":
                    smoke = "1";
                    break;
                case "偶尔抽烟":
                    smoke = "2";
                    break;
                case "从不抽烟":
                    smoke = "3";
                    break;
            }
        }
        if (!TextUtils.isEmpty(data.drink)) {
            switch (data.drink) {
                case "经常喝酒":
                    smoke = "1";
                    break;
                case "偶尔喝酒":
                    smoke = "2";
                    break;
                case "从不喝酒":
                    smoke = "3";
                    break;
            }
        }
        if (!TextUtils.isEmpty(data.exerciseHabits)) {
            switch (data.exerciseHabits) {
                case "每天一次":
                    exercise = "1";
                    break;
                case "每周几次":
                    exercise = "2";
                    break;
                case "偶尔运动":
                    exercise = "3";
                    break;
                case "从不运动":
                    exercise = "4";
                    break;
            }
        }
        if ("尚未填写".equals(data.mh)) {
            buffer = null;
        } else {
            String[] mhs = data.mh.split("\\s+");
            for (int i = 0; i < mhs.length; i++) {
                if (mhs[i].equals("高血压"))
                    buffer.append(1 + ",");
                else if (mhs[i].equals("糖尿病"))
                    buffer.append(2 + ",");
                else if (mhs[i].equals("冠心病"))
                    buffer.append(3 + ",");
                else if (mhs[i].equals("慢阻肺"))
                    buffer.append(4 + ",");
                else if (mhs[i].equals("孕产妇"))
                    buffer.append(5 + ",");
                else if (mhs[i].equals("痛风"))
                    buffer.append(6 + ",");
                else if (mhs[i].equals("甲亢"))
                    buffer.append(7 + ",");
                else if (mhs[i].equals("高血脂"))
                    buffer.append(8 + ",");
                else if (mhs[i].equals("其他"))
                    buffer.append(9 + ",");
            }
        }
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

    private int finalI = -1;
    private int finalJ = -1;
    private int finalK = -1;
    private String finalDetailAddress = "";

    private BDLocationListener mListener = new BDLocationListener() {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            stopLocation();

            String province = bdLocation.getProvince();
            String city = bdLocation.getCity();
            String county = bdLocation.getDistrict();
            String street = bdLocation.getStreet();
            String streetNumber = bdLocation.getStreetNumber();
            finalDetailAddress = street + streetNumber;
            if (mProvinceNames != null) {
                int size = mProvinceNames.size();
                for (int i = 0; i < size; i++) {
                    String provinceName = mProvinceNames.get(i);
                    if (province.contains(provinceName)
                            || provinceName.contains(province)) {
                        finalI = i;
                        List<String> cityNames = mCityNameMap.get(provinceName);
                        if (cityNames != null) {
                            mCityNames = cityNames;
                            int size1 = mCityNames.size();
                            for (int j = 0; j < size1; j++) {
                                String cityName = mCityNames.get(j);
                                if (cityName.contains(city)
                                        || city.contains(cityName)) {
                                    finalJ = j;
                                    List<String> countyNames = mCountyNameMap.get(cityName);
                                    if (countyNames != null) {
                                        mCountyNames = countyNames;
                                        int size2 = mCountyNames.size();
                                        for (int k = 0; k < size2; k++) {
                                            String countyName = mCountyNames.get(k);
                                            if (countyName.contains(county)
                                                    || county.contains(countyName)) {
                                                finalK = k;
                                                break;
                                            }
                                        }
                                    }
                                    break;
                                }
                            }
                        }
                        break;
                    }
                }

                if (finalI >= 0 && finalI < mProvinceNames.size() &&
                        finalJ >= 0 && finalJ < mCityNames.size() &&
                        finalK >= 0 && finalK < mCountyNames.size()) {
                    spProvince.post(new Runnable() {
                        @Override
                        public void run() {
                            spProvince.setSelection(finalI);
                            spCounty.setSelection(finalJ);
                            spCounty.setSelection(finalK);
                            etAddress.setText(finalDetailAddress);
                            etAddress.setSelection(finalDetailAddress.length());
                        }
                    });
                }
            }
        }
    };

    @Override
    protected void onResume() {
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

    public void onClRootClicked() {
        View view = getCurrentFocus();
        if (view != null) {
            Utils.hideKeyBroad(view);
        }
    }

    public void onTvGoBackClicked() {
        finish();
    }

    public void onTvGoForwardClicked() {
        String address = etAddress.getText().toString().trim();
        if (TextUtils.isEmpty(address)) {
            ToastUtils.showShort(R.string.sign_up3_address_tip);
            speak("主人,请输入您的住址");
            return;
        }
//        NetworkApi.alertBasedata(MyApplication.getInstance().userId, data.height, data.weight, eat, smoke, drink, exercise,
//                TextUtils.isEmpty(buffer) ? "" : buffer.substring(0, buffer.length() - 1), getAddress(), new NetworkManager.SuccessCallback<Object>() {
//                    @Override
//                    public void onSuccess(Object response) {
//                        ToastUtils.showShort("修改成功");
//                        speak("修改成功");
//                        finish();
//                    }
//                }, new NetworkManager.FailedCallback() {
//                    @Override
//                    public void onFailed(String message) {
//                        ToastUtils.showShort("修改失败");
//                        finish();
//                    }
//                });
        PUTUserBean bean = new PUTUserBean();
        bean.bid = Integer.parseInt(UserSpHelper.getUserId());
        bean.dz = address;

        NetworkApi.putUserInfo(bean.bid, new Gson().toJson(bean), new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                String body = response.body();
                try {
                    JSONObject json = new JSONObject(body);
                    boolean tag = json.getBoolean("tag");
                    if (tag) {
                        runOnUiThread(() -> speak("修改成功"));
                        finish();
                    } else {
                        runOnUiThread(() -> speak("修改失败"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
//
//        UserEntity user = new UserEntity();
//        user.address = address;
//        CCResult result = CC.obtainBuilder("com.gcml.auth.putUser")
//                .addParam("user", user)
//                .build()
//                .call();
//        Observable<Object> data = result.getDataItem("data");
//        data.subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .as(RxUtils.autoDisposeConverter(this))
//                .subscribe(new DefaultObserver<Object>() {
//                    @Override
//                    public void onNext(Object o) {
//                        super.onNext(o);
//                        runOnUiThread(() -> speak("修改成功"));
//                    }
//
//                    @Override
//                    public void onError(Throwable throwable) {
//                        super.onError(throwable);
//                        runOnUiThread(() -> speak("修改失败"));
//                    }
//                });


//        LocalShared.getInstance(this.getApplicationContext()).setSignUpAddress(getAddress());
//        Intent intent = SignUp4IdCardActivity.newIntent(this);
//        startActivityForResult(intent);
    }

    private void speak(String text) {
        MLVoiceSynthetize.startSynthesize(getApplicationContext(), text);
    }

    private List<String> mProvinceNames = new ArrayList<>();
    private Map<String, List<String>> mCityNameMap = new HashMap<>();
    private Map<String, List<String>> mCountyNameMap = new HashMap<>();
    private List<String> mCityNames;
    private List<String> mCountyNames;

    int provinceIndex = 0;
    int cityIndex = 0;
    int countyIndex = 0;

    private ArrayAdapter<String> provinceAdapter;
    private ArrayAdapter<String> mCityAdapter;
    private ArrayAdapter<String> mCountyAdapter;

    private void showCities() {
        provinceAdapter = new ArrayAdapter<>(this, R.layout.item_spinner_layout);
        spProvince.setAdapter(provinceAdapter);
        provinceAdapter.clear();
        provinceAdapter.addAll(mProvinceNames);
        mCityAdapter = new ArrayAdapter<>(this, R.layout.item_spinner_layout);
        spCity.setAdapter(mCityAdapter);
        mCityNames = mCityNameMap.get(mProvinceNames.get(0));
        mCityAdapter.clear();
        mCityAdapter.addAll(mCityNames);
        mCountyAdapter = new ArrayAdapter<>(this, R.layout.item_spinner_layout);
        spCounty.setAdapter(mCountyAdapter);
        mCountyNames = mCountyNameMap.get(mCityNames.get(0));
        mCountyAdapter.clear();
        mCountyAdapter.addAll(mCityNames);

    }

    private List<Province> mProvinces;

    private void initData() {
        spProvince.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //update relative city | county
                provinceIndex = position;
                mCityNames = mCityNameMap.get(mProvinceNames.get(position));
                mCityAdapter.clear();
                mCityAdapter.addAll(mCityNames);
                mCountyNames = mCountyNameMap.get(mCityNames.get(0));
                mCountyAdapter.clear();
                mCountyAdapter.addAll(mCountyNames);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //update relative county
                cityIndex = position;
                mCountyNames = mCountyNameMap.get(mCityNames.get(position));
                mCountyAdapter.clear();
                mCountyAdapter.addAll(mCountyNames);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spCounty.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                countyIndex = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Handlers.bg().post(new Runnable() {

            @Override
            public void run() {
                mProvinces = loadCities();
                Handlers.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showCities();
                    }
                });
            }
        });
    }

    private List<Province> loadCities() {
        String provincesArr = Utils.readTextFromAssetFile(this.getApplicationContext(), "cities.json");
        Gson gson = new Gson();
        List<Province> provinces = gson.fromJson(provincesArr, new TypeToken<List<Province>>() {
        }.getType());
        if (provinces != null && provinces.size() != 0) {
            for (Province province : provinces) {
                mProvinceNames.add(province.getName());
                List<String> cityNames = new ArrayList<>();
                List<City> cities = province.getCities();
                for (City city : cities) {
                    cityNames.add(city.getName());
                    mCountyNameMap.put(city.getName(), city.getCounties());
                }
                mCityNameMap.put(province.getName(), cityNames);
            }
        }
        return provinces;
    }

    private String getAddress() {
        StringBuilder builder = new StringBuilder();
        builder.append(mProvinceNames.get(provinceIndex))
                .append(mCityNames.get(cityIndex))
                .append(mCountyNames.get(countyIndex))
                .append(etAddress.getText().toString().trim());
        return builder.toString();
    }

    public static final String REGEX_IN_DEL = ".*(quxiao|qingchu|sandiao|shandiao|sancu|shancu|sanchu|shanchu|budui|cuole|cuole).*";
    public static final String REGEX_IN_DEL_ALL = ".*(chongxin|quanbu|suoyou|shuoyou).*";
    public static final String REGEX_IN_GO_BACK = ".*(上一步|上一部|后退|返回).*";
    public static final String REGEX_IN_GO_FORWARD = ".*(下一步|下一部|确定|完成).*";

//    @Override
//    protected void onSpeakListenerResult(String result) {
//        ToastUtils.showShort(result);
//
//        if (result.matches(REGEX_IN_GO_BACK)) {
//            onTvGoBackClicked();
//            return;
//        }
//
//        if (result.matches(REGEX_IN_GO_FORWARD)) {
//            onTvGoForwardClicked();
//            return;
//        }
//
//        String inSpell = PinYinUtils.converterToSpell(result);
//        if (inSpell.matches(REGEX_IN_DEL_ALL)) {
//            etAddress.setText("");
//            return;
//        }
//
//        String target = etAddress.getText().toString().trim();
//        if (inSpell.matches(REGEX_IN_DEL)) {
//            if (!TextUtils.isEmpty(target)) {
//                etAddress.setText(target.substring(0, target.length() - 1));
//                etAddress.setSelection(target.length() - 1);
//            }
//            return;
//        }
//
//        if (mProvinceNames != null) {
//            int size = mProvinceNames.size();
//            for (int i = 0; i < size; i++) {
//                String provinceName = mProvinceNames.get(i);
//                String provinceSpell = PinYinUtils.converterToSpell(provinceName);
//                if (inSpell.equals(provinceSpell)) {
//                    spProvince.setSelection(i);
//                    return;
//                }
//            }
//        }
//
//        if (mCityNames != null) {
//            int size = mCityNames.size();
//            for (int i = 0; i < size; i++) {
//                String cityName = mCityNames.get(i);
//                String citySpell = PinYinUtils.converterToSpell(cityName);
//                if (inSpell.equals(citySpell)) {
//                    spCity.setSelection(i);
//                    return;
//                }
//            }
//        }
//
//        if (mCountyNames != null) {
//            int size = mCountyNames.size();
//            for (int i = 0; i < size; i++) {
//                String countyName = mCountyNames.get(i);
//                String countySpell = PinYinUtils.converterToSpell(countyName);
//                if (inSpell.equals(countySpell)) {
//                    spCounty.setSelection(i);
//                    return;
//                }
//            }
//        }
//
//        String text = target + result;
//        if (text.length() < 30) {
//            etAddress.setText(text);
//            etAddress.setSelection(text.length());
//        }
//    }


}