package com.medlink.danbogh.register;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.speechsynthesis.PinYinUtils;
import com.example.han.referralproject.util.LocalShared;
import com.medlink.danbogh.utils.T;
import com.medlink.danbogh.utils.Utils;

import java.util.Arrays;

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
    private String[] mProvinceArray;
    private String[] mCityArray;
    private String[] mCountyArray;

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
        mUnbinder = ButterKnife.bind(this);
        initView();
    }

    @Override
    protected void onDestroy() {
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
        super.onDestroy();
    }

    private void initView() {
        mProvinceArray = getResources().getStringArray(R.array.provinces);
        mCityArray = getResources().getStringArray(R.array.cities);
        mCountyArray = getResources().getStringArray(R.array.counties);
        spProvince.setAdapter(new ArrayAdapter<>(this, R.layout.item_spinner_layout, mProvinceArray));
        spCity.setAdapter(new ArrayAdapter<>(this, R.layout.item_spinner_layout, mCityArray));
        spCounty.setAdapter(new ArrayAdapter<>(this, R.layout.item_spinner_layout, mCountyArray));
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

    private String getAddress() {
        StringBuilder builder = new StringBuilder();
        builder.append(spCounty.getSelectedItem().toString()).append("省")
                .append(spCity.getSelectedItem().toString()).append("市")
                .append(spCounty.getSelectedItem().toString()).append("区")
                .append(etAddress.getText().toString().trim());
        return builder.toString();
    }

    public static final String REGEX_IN_DEL = ".*(quxiao|qingchu|sandiao|shandiao|sancu|shancu|sanchu|shanchu|budui|cuole|cuole).*";
    public static final String REGEX_IN_DEL_ALL = ".*(chongxin|quanbu|suoyou|shuoyou).*";
    public static final String REGEX_IN_PROVINCE = ".*(sheng|shen|seng|sen)";
    public static final String REGEX_IN_CITY = ".*(shi|si)";
    public static final String REGEX_IN_COUNTY = ".*(qu|xian)";
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
        if (inSpell.matches(REGEX_IN_PROVINCE)) {
            String inProvince = result.substring(0, result.length() - 1);
            String province;
            for (int i = 0; i < mProvinceArray.length; i++) {
                province = mProvinceArray[i];
                if (province.equals(inProvince)) {
                    spProvince.setSelection(i);
                    return;
                }
            }
        }

        if (inSpell.matches(REGEX_IN_CITY)) {
            String inCity = result.substring(0, result.length() - 1);
            String city;
            for (int i = 0; i < mCityArray.length; i++) {
                city = mCityArray[i];
                if (city.equals(inCity)) {
                    spCity.setSelection(i);
                    return;
                }
            }
        }

        if (inSpell.matches(REGEX_IN_COUNTY)) {
            String inCounty = result.substring(0, result.length() - 1);
            String county;
            for (int i = 0; i < mCountyArray.length; i++) {
                county = mCountyArray[i];
                if (county.equals(inCounty)) {
                    spCounty.setSelection(i);
                    return;
                }
            }
        }

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

        String text = target + result;
        etAddress.setText(text);
        etAddress.setSelection(text.length());
    }
}
