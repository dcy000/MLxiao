package com.example.han.referralproject.building_record;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.example.han.referralproject.R;
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

public class AddressFragment extends Fragment implements View.OnClickListener {
    private View view;
    private TextView tvSignUpAddress;
    private Spinner spProvince;
    private TextView tvSignUpProvince;
    private Spinner spCity;
    private TextView tvSignUpCity;
    private Spinner spCounty;
    private TextView tvSignUpCounty;
    private EditText etSignUpAddress;
    private TextView tvSignUpGoBack;
    private TextView tvSignUpGoForward;
    private ConstraintLayout clSignUpRootAddress;
    private int provinceIndex = 0;
    private int cityIndex = 0;
    private int countyIndex = 0;
    private List<String> mProvinceNames = new ArrayList<>();
    private Map<String, List<String>> mCityNameMap = new HashMap<>();
    private Map<String, List<String>> mCountyNameMap = new HashMap<>();
    private List<String> mCityNames;
    private List<String> mCountyNames;
    private ArrayAdapter<String> provinceAdapter;
    private ArrayAdapter<String> mCityAdapter;
    private ArrayAdapter<String> mCountyAdapter;
    private LocationClient mLocationClient;
    private int finalI = -1;
    private int finalJ = -1;
    private int finalK = -1;
    private String finalDetailAddress = "";
    private IFragmentChange iFragmentChange;

    public void setOnFragmentChange(IFragmentChange iFragmentChange) {
        this.iFragmentChange = iFragmentChange;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_address, container, false);
            initView(view);
            initData();
            initLocation();
        }
        return view;
    }

    private void initLocation() {
        mLocationClient = new LocationClient(getContext().getApplicationContext());
        LocationClientOption locOption = new LocationClientOption();
        locOption.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        locOption.setCoorType("bd09ll");
        locOption.setIsNeedAddress(true);
        locOption.setOpenGps(true);
        locOption.setScanSpan(3000);
        mLocationClient.setLocOption(locOption);
    }

    @Override
    public void onResume() {
        super.onResume();
        startLocation();
        ((BuildingRecordActivity) getActivity()). setDisableGlobalListen(true);
        ((BuildingRecordActivity) getActivity()).speak("主人,请输入您的住址");
    }

    private void startLocation() {
        if (mListener != null && mLocationClient != null) {
            mLocationClient.registerLocationListener(mListener);
        }
        if (mLocationClient != null && !mLocationClient.isStarted()) {
            mLocationClient.start();
        }
    }

    private void stopLocation() {
        if (mLocationClient != null && mLocationClient.isStarted()) {
            mLocationClient.stop();
        }
        if (mListener != null && mLocationClient != null) {
            mLocationClient.unRegisterLocationListener(mListener);
        }
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
                            etSignUpAddress.setText(finalDetailAddress);
                            etSignUpAddress.setSelection(finalDetailAddress.length());
                        }
                    });
                }
            }
        }
    };

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
                loadCities();
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
        FragmentActivity activity = getActivity();
        if (activity == null) {
            return new ArrayList<>();
        }
        String provincesArr = Utils.readTextFromAssetFile(this.getActivity().getApplicationContext(), "cities.json");
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

    private void showCities() {
        provinceAdapter = new ArrayAdapter<>(getActivity(), R.layout.item_spinner_layout);
        spProvince.setAdapter(provinceAdapter);
        provinceAdapter.clear();
        provinceAdapter.addAll(mProvinceNames);
        mCityAdapter = new ArrayAdapter<>(getActivity(), R.layout.item_spinner_layout);
        spCity.setAdapter(mCityAdapter);
        if (mProvinceNames.size() > 0 && mCityNameMap.size() > 0) {
            mCityNames = mCityNameMap.get(mProvinceNames.get(0));
        }
        if (mCityNames != null) {
            mCityAdapter.clear();
            mCityAdapter.addAll(mCityNames);
            mCountyAdapter = new ArrayAdapter<>(getActivity(), R.layout.item_spinner_layout);
            spCounty.setAdapter(mCountyAdapter);
            mCountyNames = mCountyNameMap.get(mCityNames.get(0));
            mCountyAdapter.clear();
            mCountyAdapter.addAll(mCityNames);
        }

    }

    private void initView(View view) {
        tvSignUpAddress = (TextView) view.findViewById(R.id.tv_sign_up_address);
        spProvince = (Spinner) view.findViewById(R.id.sp_province);
        tvSignUpProvince = (TextView) view.findViewById(R.id.tv_sign_up_province);
        spCity = (Spinner) view.findViewById(R.id.sp_city);
        tvSignUpCity = (TextView) view.findViewById(R.id.tv_sign_up_city);
        spCounty = (Spinner) view.findViewById(R.id.sp_county);
        tvSignUpCounty = (TextView) view.findViewById(R.id.tv_sign_up_county);
        etSignUpAddress = (EditText) view.findViewById(R.id.et_sign_up_address);
        tvSignUpGoBack = (TextView) view.findViewById(R.id.tv_sign_up_go_back);
        tvSignUpGoBack.setOnClickListener(this);
        tvSignUpGoForward = (TextView) view.findViewById(R.id.tv_sign_up_go_forward);
        tvSignUpGoForward.setOnClickListener(this);
        clSignUpRootAddress = (ConstraintLayout) view.findViewById(R.id.cl_sign_up_root_address);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.tv_sign_up_go_back:
                if (iFragmentChange != null) {
                    iFragmentChange.lastStep(this);
                }
                break;
            case R.id.tv_sign_up_go_forward:
                String address = etSignUpAddress.getText().toString().trim();
                if (TextUtils.isEmpty(address)) {
                    T.show(R.string.sign_up3_address_tip);
                    ((BuildingRecordActivity) getActivity()).speak(R.string.sign_up3_address_tip);
                    return;
                }
                ((BuildingRecordActivity) getActivity()).buildingRecordBean.setAddress(getAddress());
                if (iFragmentChange != null) {
                    iFragmentChange.nextStep(this);
                }
                break;
        }
    }

    private String getAddress() {
        StringBuilder builder = new StringBuilder();
        builder.append(mProvinceNames.get(provinceIndex))
                .append(mCityNames.get(cityIndex))
                .append(mCountyNames.get(countyIndex))
                .append(etSignUpAddress.getText().toString().trim());
        return builder.toString();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (view != null) {
            stopLocation();
            ((ViewGroup) view.getParent()).removeView(view);
        }
    }
}
