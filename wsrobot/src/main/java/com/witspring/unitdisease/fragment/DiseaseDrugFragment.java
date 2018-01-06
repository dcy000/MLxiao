package com.witspring.unitdisease.fragment;


import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.witspring.base.BaseFragment;
import com.witspring.mlrobot.R;
import com.witspring.mlrobot.databinding.WsdiseFragmentDiseaseDrugBinding;
import com.witspring.model.entity.Medicine;
import com.witspring.model.entity.Result;
import com.witspring.unitdisease.DiseaseActivity;
import com.witspring.unitdisease.MedicineDetailActivity;
import com.witspring.unitdisease.adapter.MedicineItem;
import com.witspring.unitdisease.contract.MedicineContract;
import com.witspring.util.CommUtil;

import java.util.ArrayList;

import kale.adapter.CommonAdapter;
import kale.adapter.item.AdapterItem;

/**
 * @author Created by wu_zf on 1/3/2018.
 * @email :wuzf1234@gmail.com
 */

public class DiseaseDrugFragment extends BaseFragment implements MedicineContract.View{
    private WsdiseFragmentDiseaseDrugBinding binding;
    private DiseaseActivity mActivity;
    private MedicineContract.Presenter presenter;
    private ArrayList<Medicine> mMedicines;
    private CommonAdapter<Medicine> drugAdapter;// 药品adapter

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = (DiseaseActivity) getActivity();
        presenter = new MedicineContract.Presenter(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.wsdise_fragment_disease_drug, container, false);
        View view = binding.getRoot();
        initViews();
        return view;
    }

    private void initViews() {
        mMedicines = new ArrayList<>();
        drugAdapter = new CommonAdapter<Medicine>(mMedicines,1) {
            @NonNull
            @Override
            public AdapterItem createItem(Object o) {
                return new MedicineItem();
            }
        };

        binding.wsdiseGvContent.setAdapter(drugAdapter);
        binding.wsdiseGvContent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if (CommUtil.notEmpty(mMedicines)) {
                    presenter.getMedicineDetailById(mMedicines.get(position).getId(),-1,-1);
                }
            }
        });

        presenter.getMedicines(mActivity.getmDisease().getId(),"",-1,-1);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void showMedicines(ArrayList<Medicine> medicines) {
        if (CommUtil.notEmpty(medicines)) {
            mMedicines.clear();
            mMedicines.addAll(medicines);
            drugAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void showMedicineDetail(Medicine medicine) {
        Intent intent = new Intent(mActivity.getContext(), MedicineDetailActivity.class);
        intent.putExtra("medicine", medicine);
        mActivity.startActivity(intent);
    }

    @Override
    public void showNoData() {
        showToastShort("请求数据失败！");
    }

    @Override
    public void startLoadingAlway() {

    }

    @Override
    public void warningUnknow(Result result) {

    }
}
