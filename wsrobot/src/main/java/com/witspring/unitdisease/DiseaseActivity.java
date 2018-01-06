package com.witspring.unitdisease;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.CompoundButton;

import com.witspring.base.BaseActivity;
import com.witspring.mlrobot.R;
import com.witspring.mlrobot.databinding.WsdiseActivityDiseaseBinding;
import com.witspring.model.entity.Disease;
import com.witspring.unitdisease.contract.DiseaseContract;
import com.witspring.unitdisease.fragment.DiseaseCureFragment;
import com.witspring.unitdisease.fragment.DiseaseDiagFragment;
import com.witspring.unitdisease.fragment.DiseaseDrugFragment;
import com.witspring.unitdisease.fragment.DiseaseInfoFragment;

public class DiseaseActivity extends BaseActivity implements DiseaseContract.View{

    private WsdiseActivityDiseaseBinding binding;
    private DiseaseContract.Presenter presenter;
    private Disease mDisease;
    private FragmentManager fragmentManager;
    private DiseaseInfoFragment diseaseInfoFragment;
    private DiseaseDiagFragment diseaseDiagFragment;
    private DiseaseCureFragment diseaseCureFragment;
    private DiseaseDrugFragment diseaseDrugFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String name = getIntent().getStringExtra("diseaseName");
        this.binding = DataBindingUtil.setContentView(this, R.layout.wsdise_activity_disease);
        setToolbar(binding.toolbar, name);
        presenter = new DiseaseContract.Presenter(this);
        presenter.getDiseaseInfoByName(name,"",-1,-1);
        initViews();
    }

    private void initViews() {
        fragmentManager = getSupportFragmentManager();
        binding.wsdiseRb1.setOnCheckedChangeListener(changeListener);
        binding.wsdiseRb2.setOnCheckedChangeListener(changeListener);
        binding.wsdiseRb3.setOnCheckedChangeListener(changeListener);
        binding.wsdiseRb4.setOnCheckedChangeListener(changeListener);
    }

    CompoundButton.OnCheckedChangeListener changeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            if (b) {
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                int i = compoundButton.getId();
                if (i == R.id.wsdise_rb1) {
                    binding.wsdiseRb2.setChecked(false);
                    binding.wsdiseRb3.setChecked(false);
                    binding.wsdiseRb4.setChecked(false);
                    binding.rlTab1.setBackgroundColor(getResources().getColor(R.color.ws_white));
                    binding.rlTab2.setBackgroundColor(getResources().getColor(R.color.ws_main));
                    binding.rlTab3.setBackgroundColor(getResources().getColor(R.color.ws_main));
                    binding.rlTab4.setBackgroundColor(getResources().getColor(R.color.ws_main));
                    if (diseaseInfoFragment == null) {
                        hideFragment(transaction);
                        diseaseInfoFragment = new DiseaseInfoFragment();
                        transaction.add(R.id.wsdise_fl_content, diseaseInfoFragment);
                    } else {
                        showFragment(transaction, diseaseInfoFragment);
                    }

                } else if (i == R.id.wsdise_rb2) {
                    binding.wsdiseRb1.setChecked(false);
                    binding.wsdiseRb3.setChecked(false);
                    binding.wsdiseRb4.setChecked(false);
                    binding.rlTab1.setBackgroundColor(getResources().getColor(R.color.ws_main));
                    binding.rlTab2.setBackgroundColor(getResources().getColor(R.color.ws_white));
                    binding.rlTab3.setBackgroundColor(getResources().getColor(R.color.ws_main));
                    binding.rlTab4.setBackgroundColor(getResources().getColor(R.color.ws_main));
                    if (diseaseDiagFragment == null) {
                        hideFragment(transaction);
                        diseaseDiagFragment = new DiseaseDiagFragment();
                        transaction.add(R.id.wsdise_fl_content, diseaseDiagFragment);
                    } else {
                        showFragment(transaction, diseaseDiagFragment);
                    }

                } else if (i == R.id.wsdise_rb3) {
                    binding.wsdiseRb1.setChecked(false);
                    binding.wsdiseRb2.setChecked(false);
                    binding.wsdiseRb4.setChecked(false);
                    binding.rlTab1.setBackgroundColor(getResources().getColor(R.color.ws_main));
                    binding.rlTab2.setBackgroundColor(getResources().getColor(R.color.ws_main));
                    binding.rlTab3.setBackgroundColor(getResources().getColor(R.color.ws_white));
                    binding.rlTab4.setBackgroundColor(getResources().getColor(R.color.ws_main));
                    if (diseaseCureFragment == null) {
                        hideFragment(transaction);
                        diseaseCureFragment = new DiseaseCureFragment();
                        transaction.add(R.id.wsdise_fl_content, diseaseCureFragment);
                    } else {
                        showFragment(transaction, diseaseCureFragment);
                    }

                } else if (i == R.id.wsdise_rb4) {
                    binding.wsdiseRb1.setChecked(false);
                    binding.wsdiseRb2.setChecked(false);
                    binding.wsdiseRb3.setChecked(false);
                    binding.rlTab1.setBackgroundColor(getResources().getColor(R.color.ws_main));
                    binding.rlTab2.setBackgroundColor(getResources().getColor(R.color.ws_main));
                    binding.rlTab3.setBackgroundColor(getResources().getColor(R.color.ws_main));
                    binding.rlTab4.setBackgroundColor(getResources().getColor(R.color.ws_white));
                    if (diseaseDrugFragment == null) {
                        hideFragment(transaction);
                        diseaseDrugFragment = new DiseaseDrugFragment();
                        transaction.add(R.id.wsdise_fl_content, diseaseDrugFragment);
                    } else {
                        showFragment(transaction, diseaseDrugFragment);
                    }

                } else {
                }
                transaction.commitAllowingStateLoss();
            }
        }
    };

    private void showFragment(FragmentTransaction transaction, Fragment fragment) {
        if (diseaseInfoFragment != null && !diseaseInfoFragment.isHidden() && fragment != diseaseInfoFragment) {
            transaction.hide(diseaseInfoFragment);
        }
        if (diseaseDiagFragment != null && !diseaseDiagFragment.isHidden() && fragment != diseaseDiagFragment) {
            transaction.hide(diseaseDiagFragment);
        }
        if (diseaseCureFragment != null && !diseaseCureFragment.isHidden() && fragment != diseaseCureFragment) {
            transaction.hide(diseaseCureFragment);
        }
        if (diseaseDrugFragment != null && !diseaseDrugFragment.isHidden() && fragment != diseaseDrugFragment) {
            transaction.hide(diseaseDrugFragment);
        }
        if (fragment.isHidden()) {
            transaction.show(fragment);
        }
    }

    private void hideFragment(FragmentTransaction transaction) {
        if (diseaseInfoFragment != null) {
            transaction.hide(diseaseInfoFragment);
        }
        if (diseaseDiagFragment != null) {
            transaction.hide(diseaseDiagFragment);
        }
        if (diseaseCureFragment != null) {
            transaction.hide(diseaseCureFragment);
        }
        if (diseaseDrugFragment != null) {
            transaction.hide(diseaseDrugFragment);
        }
    }

    public Disease getmDisease(){
        return mDisease;
    }

    @Override
    public void showDiseaseInfo(Disease disease) {
        mDisease = disease;
        binding.wsdiseRgLeft.check(R.id.wsdise_rb1);
    }

    @Override
    public void showNoData() {
        showToastLong("请求网络失败！");
    }
}
