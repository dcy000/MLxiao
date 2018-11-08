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
import com.example.han.referralproject.util.LocalShared;
import com.medlink.danbogh.register.SelectAdapter;
import com.medlink.danbogh.utils.T;

import java.util.ArrayList;
import java.util.List;

import github.hellocsl.layoutmanager.gallery.GalleryLayoutManager;

public class HeightFragment extends Fragment implements View.OnClickListener {
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

    public void setOnFragmentChange(IFragmentChange iFragmentChange) {
        this.iFragmentChange = iFragmentChange;
    }

    private String result = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_height, container, false);
            initView(view);
            dealLogic();
        }
        return view;
    }

    private void dealLogic() {
        tvSignUpUnit.setText("cm");
        GalleryLayoutManager layoutManager = new GalleryLayoutManager(GalleryLayoutManager.VERTICAL);
        layoutManager.attach(rvSignUpContent, selectedPosition);
        layoutManager.setCallbackInFling(true);
        layoutManager.setOnItemSelectedListener(new GalleryLayoutManager.OnItemSelectedListener() {
            @Override
            public void onItemSelected(RecyclerView recyclerView, View item, int position) {
                selectedPosition = position;
                select((String) (mStrings == null ? String.valueOf(position) : mStrings.get(position)));
                result = mStrings == null ? String.valueOf(position) : mStrings.get(position);
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

    private List<String> getStrings() {
        mStrings = new ArrayList<>();
        for (int i = 150; i < 200; i++) {
            mStrings.add(String.valueOf(i));
        }
        return mStrings;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((BuildingRecordActivity) getActivity()).setDisableGlobalListen(true);
        ((BuildingRecordActivity) getActivity()).speak("主人,请输入您的身高");
    }

    private void initView(View view) {
        tvSignUpHeight = (TextView) view.findViewById(R.id.tv_sign_up_height);
        rvSignUpContent = (RecyclerView) view.findViewById(R.id.rv_sign_up_content);
        tvSignUpUnit = (TextView) view.findViewById(R.id.tv_sign_up_unit);
        tvSignUpGoBack = (TextView) view.findViewById(R.id.tv_sign_up_go_back);
        tvSignUpGoBack.setOnClickListener(this);
        tvSignUpGoForward = (TextView) view.findViewById(R.id.tv_sign_up_go_forward);
        tvSignUpGoForward.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.tv_sign_up_go_back:
                if (iFragmentChange != null) {
                    iFragmentChange.lastStep(this);
                    LocalShared.getInstance(getActivity()).setSignUpHeight(175);
                }
                break;
            case R.id.tv_sign_up_go_forward:
                ((BuildingRecordActivity) getActivity()).buildingRecordBean.setHeight(result);
                LocalShared.getInstance(getActivity()).setSignUpHeight((int) Float.parseFloat(result));
                if (iFragmentChange != null) {
                    iFragmentChange.nextStep(this);
                }
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
