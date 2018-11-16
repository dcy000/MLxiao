package com.example.han.referralproject.building_record;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.application.MyApplication;
import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.util.LocalShared;
import com.example.han.referralproject.yiyuan.activity.InquiryAndFileEndActivity;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

public class BuildingRecordActivity extends BaseActivity implements IFragmentChange {
    private FrameLayout frame;
    private SignDoctorFragment signDoctorFragment;
    private HeightFragment heightFragment;
    private WeightFragment weightFragment;
    private AddressFragment addressFragment;
    private EducationDegreeFragment educationDegreeFragment;
    private ProfessionalFragment professionalFragment;
    private PaymentmethodFragment paymentmethodFragment;
    private AllergicHistoryFragment allergicHistoryFragment;
    private MedicalHistoryFragment medicalHistoryFragment;
    private FamilyhistoryFragment familyhistoryFragment;
    private HereditaryDiseaseFragment hereditaryDiseaseFragment;
    private BloodTypeFragment bloodTypeFragment;
    private DisabilityFragment disabilityFragment;
    private EnvironmentFragment environmentFragment;
    public static BuildingRecordBean buildingRecordBean;
    private boolean bind;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_building_record);
        mToolbar.setVisibility(View.VISIBLE);
        mTitleText.setText("建档");
        mRightText.setVisibility(View.GONE);
        mRightView.setVisibility(View.GONE);
        initView();
        dealLogic();
    }


    private void dealLogic() {
        bind = getIntent().getBooleanExtra("bind", false);
        if (bind) {
            heightFragment = new HeightFragment();
            heightFragment.setOnFragmentChange(this);
            getSupportFragmentManager().beginTransaction().replace(R.id.frame, heightFragment).commit();
        } else {
            signDoctorFragment = new SignDoctorFragment();
            signDoctorFragment.setOnFragmentChange(this);
            mToolbar.setVisibility(View.GONE);
            getSupportFragmentManager().beginTransaction().replace(R.id.frame, signDoctorFragment).commit();
        }
    }

    private void initView() {
        //隐藏软键盘
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        frame = (FrameLayout) findViewById(R.id.frame);
        buildingRecordBean = new BuildingRecordBean();
        buildingRecordBean.setUserId(MyApplication.getInstance().userId);
        buildingRecordBean.setEquipmentId(MyApplication.getInstance().eqid);
        buildingRecordBean.setHiHealthRecordId("");


    }

    @Override
    public void lastStep(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (fragment instanceof HeightFragment) {//身高
            mToolbar.setVisibility(View.GONE);
            if (bind) {
                finish();
            } else {
                replaceSignDocFragment(fragmentTransaction);//签约医生
            }
        } else if (fragment instanceof WeightFragment) {//体重
            replaceHeightFragment(fragmentTransaction);//身高
        } else if (fragment instanceof AddressFragment) {//地址
            replaceWeightFragment(fragmentTransaction);//体重
        } else if (fragment instanceof BloodTypeFragment) {//血型
            replaceAddressFragment(fragmentTransaction);//地址
        } else if (fragment instanceof EducationDegreeFragment) {//文化程度
            replaceBloodTypeFragment(fragmentTransaction);//血型
        } else if (fragment instanceof ProfessionalFragment) {//职业
            replaceEducationFragment(fragmentTransaction);//文化程度
        } else if (fragment instanceof PaymentmethodFragment) {//支付方式
            replaceProfressionFragment(fragmentTransaction);//职业
        } else if (fragment instanceof AllergicHistoryFragment) {//过敏史
            replacePaymentFragment(fragmentTransaction);//支付方式
        } else if (fragment instanceof MedicalHistoryFragment) {//既往病史
            replaceAllergicFragment(fragmentTransaction);//过敏史
        } else if (fragment instanceof FamilyhistoryFragment) {//家族病史
            replaceMedicalFragment(fragmentTransaction);//既往病史
        } else if (fragment instanceof HereditaryDiseaseFragment) {//遗传病逝
            replaceFamilyFragment(fragmentTransaction);//家族病逝
        } else if (fragment instanceof DisabilityFragment) {//残疾病逝
            replaceHereditaryFragment(fragmentTransaction);//遗传病逝
        } else if (fragment instanceof EnvironmentFragment) {
            replaceDisabilityFragment(fragmentTransaction);
        }
        fragmentTransaction.commit();
    }

    @Override
    public void nextStep(Fragment fragment) {

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (fragment instanceof SignDoctorFragment) {//签约医生
            replaceHeightFragment(fragmentTransaction);//身高
        } else if (fragment instanceof HeightFragment) {//身高
            replaceWeightFragment(fragmentTransaction);//体重
        } else if (fragment instanceof WeightFragment) {//体重
            replaceAddressFragment(fragmentTransaction);//地址
        } else if (fragment instanceof AddressFragment) {//地址
            replaceBloodTypeFragment(fragmentTransaction);//血型
        } else if (fragment instanceof BloodTypeFragment) {//血型
            replaceEducationFragment(fragmentTransaction);//文化程度
        } else if (fragment instanceof EducationDegreeFragment) {//文化程度
            replaceProfressionFragment(fragmentTransaction);//职业
        } else if (fragment instanceof ProfessionalFragment) {//职业
            replacePaymentFragment(fragmentTransaction);//支付方式
        } else if (fragment instanceof PaymentmethodFragment) {//支付方式
            replaceAllergicFragment(fragmentTransaction);//过敏史
        } else if (fragment instanceof AllergicHistoryFragment) {//过敏史
            replaceMedicalFragment(fragmentTransaction);//既往病史
        } else if (fragment instanceof MedicalHistoryFragment) {//既往病史
            replaceFamilyFragment(fragmentTransaction);//家族史
        } else if (fragment instanceof FamilyhistoryFragment) {//家族史
            replaceHereditaryFragment(fragmentTransaction);//遗传病
        } else if (fragment instanceof HereditaryDiseaseFragment) {//遗传病
            replaceDisabilityFragment(fragmentTransaction);//残疾情况
        } else if (fragment instanceof DisabilityFragment) {//残疾情况
            replaceEnvironmentFragment(fragmentTransaction);
        } else if (fragment instanceof EnvironmentFragment) {//环境
            String json = new Gson().toJson(buildingRecordBean);
            //跳转到结束页面
            OkGo.<String>post(NetworkApi.Upload_BuildRecord)
                    .upJson(json)
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(Response<String> response) {
                            Log.d("上传成功", "onSuccess: " + response.body());
                            LocalShared.getInstance(mContext).setUserHeight(buildingRecordBean.height);
                            InquiryAndFileEndActivity.startMe(BuildingRecordActivity.this, "建档");
                        }

                        @Override
                        public void onError(Response<String> response) {
                            Log.e("上传失败", "onError: " + response.body());
                        }
                    });
        }
        fragmentTransaction.commit();
    }

    private void replaceEnvironmentFragment(FragmentTransaction fragmentTransaction) {
        if (environmentFragment == null) {
            environmentFragment = new EnvironmentFragment();
            environmentFragment.setOnFragmentChange(this);
        }
        fragmentTransaction.replace(R.id.frame, environmentFragment);
    }

    private void replaceDisabilityFragment(FragmentTransaction fragmentTransaction) {
        if (disabilityFragment == null) {
            disabilityFragment = new DisabilityFragment();
            disabilityFragment.setOnFragmentChange(this);
        }
        fragmentTransaction.replace(R.id.frame, disabilityFragment);
    }

    private void replaceBloodTypeFragment(FragmentTransaction fragmentTransaction) {
        if (bloodTypeFragment == null) {
            bloodTypeFragment = new BloodTypeFragment();
            bloodTypeFragment.setOnFragmentChange(this);
        }
        fragmentTransaction.replace(R.id.frame, bloodTypeFragment);
    }

    private void replaceHereditaryFragment(FragmentTransaction fragmentTransaction) {
        if (hereditaryDiseaseFragment == null) {
            hereditaryDiseaseFragment = new HereditaryDiseaseFragment();
            hereditaryDiseaseFragment.setOnFragmentChange(this);
        }
        fragmentTransaction.replace(R.id.frame, hereditaryDiseaseFragment);
    }

    private void replaceFamilyFragment(FragmentTransaction fragmentTransaction) {
        if (familyhistoryFragment == null) {
            familyhistoryFragment = new FamilyhistoryFragment();
            familyhistoryFragment.setOnFragmentChange(this);
        }
        fragmentTransaction.replace(R.id.frame, familyhistoryFragment);
    }

    private void replaceMedicalFragment(FragmentTransaction fragmentTransaction) {
        if (medicalHistoryFragment == null) {
            medicalHistoryFragment = new MedicalHistoryFragment();
            medicalHistoryFragment.setOnFragmentChange(this);
        }
        fragmentTransaction.replace(R.id.frame, medicalHistoryFragment);
    }

    private void replaceAllergicFragment(FragmentTransaction fragmentTransaction) {
        if (allergicHistoryFragment == null) {
            allergicHistoryFragment = new AllergicHistoryFragment();
            allergicHistoryFragment.setOnFragmentChange(this);
        }
        fragmentTransaction.replace(R.id.frame, allergicHistoryFragment);
    }

    private void replacePaymentFragment(FragmentTransaction fragmentTransaction) {
        if (paymentmethodFragment == null) {
            paymentmethodFragment = new PaymentmethodFragment();
            paymentmethodFragment.setOnFragmentChange(this);
        }
        fragmentTransaction.replace(R.id.frame, paymentmethodFragment);
    }

    private void replaceProfressionFragment(FragmentTransaction fragmentTransaction) {
        if (professionalFragment == null) {
            professionalFragment = new ProfessionalFragment();
            professionalFragment.setOnFragmentChange(this);
        }
        fragmentTransaction.replace(R.id.frame, professionalFragment);
    }

    private void replaceEducationFragment(FragmentTransaction fragmentTransaction) {
        if (educationDegreeFragment == null) {
            educationDegreeFragment = new EducationDegreeFragment();
            educationDegreeFragment.setOnFragmentChange(this);
        }
        fragmentTransaction.replace(R.id.frame, educationDegreeFragment);
    }

    private void replaceAddressFragment(FragmentTransaction fragmentTransaction) {
        if (addressFragment == null) {
            addressFragment = new AddressFragment();
            addressFragment.setOnFragmentChange(this);
        }
        fragmentTransaction.replace(R.id.frame, addressFragment);
    }


    private void replaceSignDocFragment(FragmentTransaction fragmentTransaction) {
        if (signDoctorFragment == null) {
            signDoctorFragment = new SignDoctorFragment();
            signDoctorFragment.setOnFragmentChange(this);
        }
        fragmentTransaction.replace(R.id.frame, signDoctorFragment);
    }

    private void replaceHeightFragment(FragmentTransaction fragmentTransaction) {
        if (heightFragment == null) {
            heightFragment = new HeightFragment();
            heightFragment.setOnFragmentChange(this);
        }
        fragmentTransaction.replace(R.id.frame, heightFragment);
    }

    private void replaceWeightFragment(FragmentTransaction fragmentTransaction) {
        if (weightFragment == null) {
            weightFragment = new WeightFragment();
            weightFragment.setOnFragmentChange(this);
        }
        fragmentTransaction.replace(R.id.frame, weightFragment);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == -1) {
            mToolbar.setVisibility(View.VISIBLE);
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            replaceHeightFragment(fragmentTransaction);
            fragmentTransaction.commit();
        }
    }
}
