package com.medlink.danbogh.register;

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

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.speechsynthesis.PinYinUtils;
import com.example.han.referralproject.util.LocalShared;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.medlink.danbogh.register.entity.City;
import com.medlink.danbogh.register.entity.Province;
import com.medlink.danbogh.utils.Handlers;
import com.medlink.danbogh.utils.T;
import com.medlink.danbogh.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    Spinner spProvince;
    @BindView(R.id.sp_city)
    Spinner spCity;
    @BindView(R.id.sp_county)
    Spinner spCounty;
    @BindView(R.id.et_sign_up_address)
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up3_address);
        setDisableGlobalListen(true);
        mUnbinder = ButterKnife.bind(this);
        initData();
    }

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
        speak(R.string.sign_up3_address_tip);
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
        Intent intent = SignUp4IdCardActivity.newIntent(this);
        startActivity(intent);
    }

    private List<String> mProvinceNames = new ArrayList<>();
    private Map<String, List<String>> mCityNameMap = new HashMap<>();
    private Map<String, List<String>> mCountyNameMap = new HashMap<>();
    private List<String> mCityNames;
    private List<String> mCountyNames;

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
                mCountyNames = mCountyNameMap.get(mCityNames.get(position));
                mCountyAdapter.clear();
                mCountyAdapter.addAll(mCountyNames);
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
        builder.append(spCounty.getSelectedItem().toString())
                .append(spCity.getSelectedItem().toString())
                .append(spCounty.getSelectedItem().toString())
                .append(etAddress.getText().toString().trim());
        return builder.toString();
    }

    public static final String REGEX_IN_DEL = ".*(quxiao|qingchu|sandiao|shandiao|sancu|shancu|sanchu|shanchu|budui|cuole|cuole).*";
    public static final String REGEX_IN_DEL_ALL = ".*(chongxin|quanbu|suoyou|shuoyou).*";
    public static final String REGEX_IN_GO_BACK = ".*(上一步|上一部|后退|返回).*";
    public static final String REGEX_IN_GO_FORWARD = ".*(下一步|下一部|确定|完成).*";

    @Override
    protected void onSpeakListenerResult(String result) {
        T.show(result);

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
        etAddress.setText(text);
        etAddress.setSelection(text.length());
    }
}
