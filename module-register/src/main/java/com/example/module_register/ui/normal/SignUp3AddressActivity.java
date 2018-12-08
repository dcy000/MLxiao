package com.example.module_register.ui.normal;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.example.module_register.R;
import com.example.module_register.R2;
import com.gcml.lib_location.bean.City;
import com.gcml.lib_location.bean.Province;
import com.example.module_register.ui.base.VoiceToolBarActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.gzq.lib_core.base.Box;
import com.gzq.lib_core.base.ui.BasePresenter;
import com.gzq.lib_core.base.ui.IPresenter;
import com.gzq.lib_core.bean.UserInfoBean;
import com.gzq.lib_core.utils.DataUtils;
import com.gzq.lib_core.utils.Handlers;
import com.gzq.lib_core.utils.KeyboardUtils;
import com.gzq.lib_core.utils.PinYinUtils;
import com.gzq.lib_core.utils.ToastUtils;
import com.iflytek.cloud.SpeechError;
import com.iflytek.synthetize.MLSynthesizerListener;
import com.iflytek.synthetize.MLVoiceSynthetize;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class SignUp3AddressActivity extends VoiceToolBarActivity {

    @BindView(R2.id.tv_sign_up_go_back)
    TextView tvGoBack;
    @BindView(R2.id.tv_sign_up_go_forward)
    TextView tvGoForward;
    @BindView(R2.id.sp_province)
    Spinner spProvince;
    @BindView(R2.id.sp_city)
    Spinner spCity;
    @BindView(R2.id.sp_county)
    Spinner spCounty;
    @BindView(R2.id.et_sign_up_address)
    EditText etAddress;
    private Unbinder mUnbinder;

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, SignUp3AddressActivity.class);
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        return intent;
    }


    @Override
    public int layoutId(Bundle savedInstanceState) {
        return R.layout.activity_sign_up3_address;
    }

    @Override
    public void initParams(Intent intentArgument) {
        initLocation();
    }

    @Override
    public void initView() {
        mUnbinder = ButterKnife.bind(this);
        initData();
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
        return new BasePresenter(this) {
        };
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

    private int finalI = -1;
    private int finalJ = -1;
    private int finalK = -1;
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
    protected void onDestroy() {
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setDisableWakeup(true);
        MLVoiceSynthetize.startSynthesize(Box.getString(R.string.sign_up3_address_tip), new MLSynthesizerListener() {
            @Override
            public void onCompleted(SpeechError speechError) {
                robotStartListening();
            }
        });
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

    @OnClick(R2.id.cl_sign_up_root_address)
    public void onClRootClicked() {
        View view = getCurrentFocus();
        if (view != null) {
            KeyboardUtils.hideKeyboard(view);
        }
    }

    @OnClick(R2.id.tv_sign_up_go_back)
    public void onTvGoBackClicked() {
        finish();
    }

    @OnClick(R2.id.tv_sign_up_go_forward)
    public void onTvGoForwardClicked() {
        String address = etAddress.getText().toString().trim();
        if (TextUtils.isEmpty(address)) {
            ToastUtils.showShort(R.string.sign_up3_address_tip);
            MLVoiceSynthetize.startSynthesize(R.string.sign_up3_address_tip);
            return;
        }

        cacheUserInfo(getAddress());
        Intent intent = SignUp4IdCardActivity.newIntent(this);
        startActivity(intent);
    }

    private void cacheUserInfo(String address) {
        UserInfoBean user = Box.getSessionManager().getUser();
        if (user == null) {
            user = new UserInfoBean();
        }
        user.dz = address;
        Box.getSessionManager().setUser(user);
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
        String provincesArr = DataUtils.readTextFromAssetFile(this.getApplicationContext(), "cities.json");
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

        String inSpell = PinYinUtils.converterToSpell(result);
        if (inSpell.matches(REGEX_IN_DEL_ALL)) {
            etAddress.setText("");
            return;
        }

        String target = etAddress.getText().toString().trim();
        if (inSpell.matches(REGEX_IN_DEL)) {
            if (!TextUtils.isEmpty(target)) {
                etAddress.setText(target.substring(0, target.length() - 1));
                etAddress.setSelection(target.length() - 1);
            }
            return;
        }

        if (mProvinceNames != null) {
            int size = mProvinceNames.size();
            for (int i = 0; i < size; i++) {
                String provinceName = mProvinceNames.get(i);
                String provinceSpell = PinYinUtils.converterToSpell(provinceName);
                if (inSpell.equals(provinceSpell)) {
                    spProvince.setSelection(i);
                    return;
                }
            }
        }

        if (mCityNames != null) {
            int size = mCityNames.size();
            for (int i = 0; i < size; i++) {
                String cityName = mCityNames.get(i);
                String citySpell = PinYinUtils.converterToSpell(cityName);
                if (inSpell.equals(citySpell)) {
                    spCity.setSelection(i);
                    return;
                }
            }
        }

        if (mCountyNames != null) {
            int size = mCountyNames.size();
            for (int i = 0; i < size; i++) {
                String countyName = mCountyNames.get(i);
                String countySpell = PinYinUtils.converterToSpell(countyName);
                if (inSpell.equals(countySpell)) {
                    spCounty.setSelection(i);
                    return;
                }
            }
        }

        String text = target + result;
        if (text.length() < 30) {
            etAddress.setText(text);
            etAddress.setSelection(text.length());
        }
    }
}
