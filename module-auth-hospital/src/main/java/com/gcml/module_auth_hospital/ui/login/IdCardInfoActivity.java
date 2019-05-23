package com.gcml.module_auth_hospital.ui.login;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gcml.common.utils.base.ToolbarBaseActivity;
import com.gcml.module_auth_hospital.R;

public class IdCardInfoActivity extends ToolbarBaseActivity implements View.OnClickListener {

    ConstraintLayout mIdCardClRoot;
    TextView mIdCardTvNameInfo;
    TextView mIdCardTvGenderInfo;
    TextView mIdCardTvNationInfo;
    TextView mIdCardTvBirthYearInfo;
    TextView mIdCardTvBirthMonthInfo;
    TextView mIdCardTvBirthDayInfo;
    TextView mIdCardTvAddressInfo;
    ImageView mIdCardIvProfile;
    TextView mIdCardTvNumberInfo;
    TextView mIdCardTvAction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_id_crad_info);
        initView();
        mToolbar.setVisibility(View.VISIBLE);
        mTitleText.setText("身  份  证  确  认");
        mRightView.setVisibility(View.GONE);
        mTitleText.setVisibility(View.GONE);
        Intent intent = getIntent();
        if (intent != null) {
            String name = intent.getStringExtra("name");
            String gender = intent.getStringExtra("gender");
            String nation = intent.getStringExtra("nation");
            String address = intent.getStringExtra("address");
            Bitmap profile = intent.getParcelableExtra("profile");
            String idCard = intent.getStringExtra("idCard");
            if (TextUtils.isEmpty(name)
                    || TextUtils.isEmpty(gender)
                    || TextUtils.isEmpty(nation)
                    || TextUtils.isEmpty(address)
                    || profile == null
                    || TextUtils.isEmpty(idCard)
                    || idCard.length() != 18
            ) {
                mIdCardClRoot.setVisibility(View.GONE);
                return;
            }
            mIdCardClRoot.setVisibility(View.VISIBLE);
            mIdCardTvNameInfo.setText(name);
            mIdCardTvGenderInfo.setText(gender);
            mIdCardTvNationInfo.setText(nation);
            mIdCardTvBirthYearInfo.setText(idCard.substring(6, 10));
            mIdCardTvBirthMonthInfo.setText(idCard.substring(10, 12));
            mIdCardTvBirthDayInfo.setText(idCard.substring(12, 14));
            mIdCardTvAddressInfo.setText(address);
            mIdCardIvProfile.setImageBitmap(profile);
            StringBuilder numberBuilder = new StringBuilder();
            int length = idCard.length();
            for (int i = 0; i < length; i++) {
                numberBuilder.append(idCard.charAt(i));
                if (i < length - 1) {
                    numberBuilder.append(" ");
                }
            }
            mIdCardTvNumberInfo.setText(numberBuilder.toString());
        }
    }

    private void initView() {
        mIdCardClRoot = findViewById(R.id.id_card_cl_root);
        mIdCardTvNameInfo = findViewById(R.id.id_card_tv_name_info);
        mIdCardTvGenderInfo = findViewById(R.id.id_card_tv_gender_info);
        mIdCardTvNationInfo = findViewById(R.id.id_card_tv_nation_info);
        mIdCardTvBirthYearInfo = findViewById(R.id.id_card_tv_birth_year_info);
        mIdCardTvBirthMonthInfo = findViewById(R.id.id_card_tv_birth_month_info);
        mIdCardTvBirthDayInfo = findViewById(R.id.id_card_tv_birth_day_info);
        mIdCardTvAddressInfo = findViewById(R.id.id_card_tv_address_info);
        mIdCardIvProfile = findViewById(R.id.id_card_iv_profile);
        mIdCardTvNumberInfo = findViewById(R.id.id_card_tv_number_info);
        mIdCardTvAction = findViewById(R.id.id_card_tv_action);
        mIdCardTvAction.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.id_card_tv_action) {

        }
    }
}
