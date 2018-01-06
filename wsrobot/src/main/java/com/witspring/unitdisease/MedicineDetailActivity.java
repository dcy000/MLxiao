package com.witspring.unitdisease;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.witspring.base.BaseActivity;
import com.witspring.mlrobot.R;
import com.witspring.mlrobot.databinding.WsdiseActivityMedicineDetailBinding;
import com.witspring.model.entity.Medicine;
import com.witspring.util.StringUtil;

/**
 * @author Created by wu_zf on 1/3/2018.
 * @email :wuzf1234@gmail.com
 */
public class MedicineDetailActivity extends BaseActivity {
    private WsdiseActivityMedicineDetailBinding binding;
    private Medicine mMedicine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.wsdise_activity_medicine_detail);
        mMedicine = (Medicine) getIntent().getExtras().getSerializable("medicine");
        setToolbar(binding.toolbar, mMedicine.getName());
        initData();
    }

    private void initData() {
        binding.wsdiseIvDetail.setImageURI(mMedicine.getThumbnailImgUrl());
        binding.wsdiseTvMedicineName.setText(getTipStr(mMedicine.getName()));
        binding.wsdiseTvPrice.setText(getTipStr(mMedicine.getPrice()));
        binding.wsdiseTvSupport.setText(mMedicine.getSupportInsurance() == Medicine.SUPPORT_INSURANCE_YES ? "支持医保" : "不支持医保");
        binding.wsdiseTvSupport.setBackgroundResource(mMedicine.getSupportInsurance() == Medicine.SUPPORT_INSURANCE_YES ? R.mipmap.wsdise_bg_zcyb : R.mipmap.wsdise_bg_bzhyb);
        if (mMedicine.getIsPrescription() != Medicine.PRESCRIPTION_NULL) {
            binding.wsdiseTvCfy.setBackgroundResource(mMedicine.getIsPrescription() == Medicine.PRESCRIPTION_NO ? R.mipmap.wsdise_tag_no_prescription : R.mipmap.wsdise_tag_prescription);
        } else {
            binding.wsdiseTvCfy.setVisibility(View.INVISIBLE);
        }
        binding.wsdiseTvSpecification.setText(getTipStr(mMedicine.getSpecification()));
        binding.wsdiseTvComponent.setText(getTipStr(mMedicine.getComponent()));
        binding.wsdiseTvUsage.setText(getTipStr(mMedicine.getUsage()));
        binding.wsdiseTvIndications.setText(getTipStr(mMedicine.getIndications()));
        binding.wsdiseTvAdverseEffect.setText(getTipStr(mMedicine.getAdverseEffect()));
        binding.wsdiseTvTaboo.setText(getTipStr(mMedicine.getTaboo()));
        binding.wsdiseTvInteraction.setText(getTipStr(mMedicine.getDrugInteraction()));
        binding.wsdiseTvAttention.setText(getTipStr(mMedicine.getAttention()));
        binding.wsdiseTvApproval.setText(getTipStr(mMedicine.getApprovalDoc()));
    }

    private String getTipStr(String str){
        return StringUtil.isBlank(str) ? "暂无" : str;
    }


}
