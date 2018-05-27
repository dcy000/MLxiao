package com.example.han.referralproject.idcard;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class IdCardInfoActivity extends BaseActivity {

    Unbinder unbinder;
    @BindView(R.id.id_card_cl_root)
    ConstraintLayout mIdCardClRoot;
    @BindView(R.id.id_card_tv_name_info)
    TextView mIdCardTvNameInfo;
    @BindView(R.id.id_card_tv_gender_info)
    TextView mIdCardTvGenderInfo;
    @BindView(R.id.id_card_tv_nation_info)
    TextView mIdCardTvNationInfo;
    @BindView(R.id.id_card_tv_birth_year_info)
    TextView mIdCardTvBirthYearInfo;
    @BindView(R.id.id_card_tv_birth_month_info)
    TextView mIdCardTvBirthMonthInfo;
    @BindView(R.id.id_card_tv_birth_day_info)
    TextView mIdCardTvBirthDayInfo;
    @BindView(R.id.id_card_tv_address_info)
    TextView mIdCardTvAddressInfo;
    @BindView(R.id.id_card_iv_profile)
    ImageView mIdCardIvProfile;
    @BindView(R.id.id_card_tv_number_info)
    TextView mIdCardTvNumberInfo;
    @BindView(R.id.id_card_tv_action)
    TextView mIdCardTvAction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_id_crad_info);
        mToolbar.setVisibility(View.VISIBLE);
        mTitleText.setText("身  份  证  确  认");
        mRightView.setVisibility(View.GONE);
        mTitleText.setVisibility(View.GONE);
        unbinder = ButterKnife.bind(this);
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

    @OnClick(R.id.id_card_tv_action)
    public void onViewClicked() {
        setResult(Activity.RESULT_OK);
        finish();
    }

    @Override
    protected void onResume() {
        setDisableGlobalListen(true);
        setEnableListeningLoop(false);
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        if (unbinder != null) {
            unbinder.unbind();
        }
        super.onDestroy();
    }
}
