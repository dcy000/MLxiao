package com.gcml.module_health_profile.bracelet.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gcml.module_health_profile.R;
import com.gcml.module_health_profile.bracelet.bean.FalseServiceItemBean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.support.v7.widget.LinearLayoutManager.*;


public class ServiceHistoryFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private RecyclerView recyclerView;

    public ServiceHistoryFragment() {
    }

    public static ServiceHistoryFragment newInstance(String param1, String param2) {
        ServiceHistoryFragment fragment = new ServiceHistoryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_service_history, container, false);
        recyclerView = inflate.findViewById(R.id.rv_service_items);
        LinearLayoutManager layout = new LinearLayoutManager(getContext());
        layout.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layout);
        return inflate;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getData();
    }

    private List<FalseServiceItemBean> getData() {
        ArrayList<FalseServiceItemBean> data = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            FalseServiceItemBean e = new FalseServiceItemBean();
            e.servicePeople = "服务人员" + i;
            e.warnType = "预警方式" + i;
            e.warnTime = "报警时间" + i;
            data.add(e);
        }

        recyclerView.setAdapter(new BaseQuickAdapter<FalseServiceItemBean, BaseViewHolder>(R.layout.layout_item_service_item, data) {
            @Override
            protected void convert(BaseViewHolder helper, FalseServiceItemBean item) {
                TextView people = helper.getView(R.id.tv_service_people);
                TextView type = helper.getView(R.id.tv_warn_type);
                TextView time = helper.getView(R.id.tv_warn_time);

                people.setText(item.servicePeople);
                type.setText(item.warnType);
                time.setText(item.warnTime);
            }
        });

        return data;
    }


}
