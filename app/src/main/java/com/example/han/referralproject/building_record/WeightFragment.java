package com.example.han.referralproject.building_record;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.medlink.danbogh.register.SelectAdapter;
import com.medlink.danbogh.utils.T;

import java.util.ArrayList;
import java.util.List;

import github.hellocsl.layoutmanager.gallery.GalleryLayoutManager;

public class WeightFragment extends Fragment implements View.OnClickListener {
    private View view;
    private TextView tvSignUpHeight;
    private RecyclerView rvSignUpContent;
    private TextView tvSignUpUnit;
    private TextView tvSignUpGoBack;
    private TextView tvSignUpGoForward;
    private int selectedPosition = 20;
    public ArrayList<String> mStrings;
    private SelectAdapter adapter;
    private IFragmentChange iFragmentChange;
    private String result="";

    public void setOnFragmentChange(IFragmentChange iFragmentChange) {
        this.iFragmentChange = iFragmentChange;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_height, container, false);
            initView(view);
            dealLogic();
        }
        initView(view);
        return view;
    }

    private void dealLogic() {
        GalleryLayoutManager layoutManager = new GalleryLayoutManager(GalleryLayoutManager.VERTICAL);
        layoutManager.attach(rvSignUpContent, selectedPosition);
        layoutManager.setCallbackInFling(true);
        layoutManager.setOnItemSelectedListener(new GalleryLayoutManager.OnItemSelectedListener() {
            @Override
            public void onItemSelected(RecyclerView recyclerView, View item, int position) {
                selectedPosition = position;
                select((String) (mStrings == null ? String.valueOf(position) : mStrings.get(position)));
                result=mStrings == null ? String.valueOf(position) : mStrings.get(position);
            }
        });
        adapter = new SelectAdapter();
        adapter.setStrings(getStrings());
        adapter.setOnItemClickListener(new SelectAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                rvSignUpContent.smoothScrollToPosition(position);
            }
        });
        rvSignUpContent.setAdapter(adapter);
    }
    public void select(String text) {
        T.show(text);
    }

    protected List<String> getStrings() {
        mStrings = new ArrayList<>();
        for (int i = 35; i < 150; i++) {
            mStrings.add(String.valueOf(i));
        }
        return mStrings;
    }

    private void initView(View view) {
        tvSignUpHeight = (TextView) view.findViewById(R.id.tv_sign_up_height);
        rvSignUpContent = (RecyclerView) view.findViewById(R.id.rv_sign_up_content);
        tvSignUpUnit = (TextView) view.findViewById(R.id.tv_sign_up_unit);
        tvSignUpGoBack = (TextView) view.findViewById(R.id.tv_sign_up_go_back);
        tvSignUpGoBack.setOnClickListener(this);
        tvSignUpGoForward = (TextView) view.findViewById(R.id.tv_sign_up_go_forward);
        tvSignUpGoForward.setOnClickListener(this);
        tvSignUpHeight.setText("您的体重");
        tvSignUpUnit.setText("kg");
    }

    @Override
    public void onResume() {
        super.onResume();
        ((BuildingRecordActivity) getActivity()). setDisableGlobalListen(true);
        ((BuildingRecordActivity) getActivity()).speak("主人,请输入您的体重");
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
                ((BuildingRecordActivity) getActivity()).buildingRecordBean.setWeight(result+"kg");
                iFragmentChange.nextStep(this);
                break;
        }
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (view != null) {
            ((ViewGroup) view.getParent()).removeView(view);
        }
    }
}
