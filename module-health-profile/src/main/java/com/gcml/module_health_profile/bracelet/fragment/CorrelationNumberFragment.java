package com.gcml.module_health_profile.bracelet.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gcml.module_health_profile.R;
import com.gcml.module_health_profile.bracelet.bean.FalseCorrelationDataBean;
import com.gcml.module_health_profile.bracelet.wrap.CorrelationNumberItemLayout;
import com.gcml.module_health_profile.bracelet.wrap.CorrelationNumberLayout;
import com.iflytek.cloud.util.ResourceUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class CorrelationNumberFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    private TextView deviceName;
    private TextView deviceImei;
    private TextView deviceOwner;
    private TextView bindPhone;
    private LinearLayout viewGroup;

    public CorrelationNumberFragment() {
    }

    public static CorrelationNumberFragment newInstance(String param1, String param2) {
        CorrelationNumberFragment fragment = new CorrelationNumberFragment();
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
        View inflate = inflater.inflate(R.layout.fragment_device_correalation, container, false);
        viewGroup = inflate.findViewById(R.id.ll_container);
        return inflate;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getData();
    }

    ArrayList<FalseCorrelationDataBean> data = new ArrayList<>();

    private List<FalseCorrelationDataBean> getData() {

        FalseCorrelationDataBean item = new FalseCorrelationDataBean();
        item.name = "关联医生";
        FalseCorrelationDataBean.itemBean e = new FalseCorrelationDataBean.itemBean();
        e.itemName = "张辉";
        e.itemPhone = "18655212892";
        item.items.add(e);
        data.add(item);

        FalseCorrelationDataBean item2 = new FalseCorrelationDataBean();
        item2.name = "关联社工";
        FalseCorrelationDataBean.itemBean e1 = new FalseCorrelationDataBean.itemBean();
        e1.itemName = "张话";
        e1.itemPhone = "18655555555";
        FalseCorrelationDataBean.itemBean e2 = new FalseCorrelationDataBean.itemBean();
        e2.itemName = "张发";
        e2.itemPhone = "18655219999";
        item2.items.add(e1);
        item2.items.add(e2);
        data.add(item2);


        bindView(data);

        return data;
    }

    private void bindView(ArrayList<FalseCorrelationDataBean> data) {
        if (data == null) {
            return;
        }
        for (int i = 0; i < data.size(); i++) {
            CorrelationNumberLayout layout = new CorrelationNumberLayout(getContext());
            layout.setRelationName(data.get(i).name);
            List<FalseCorrelationDataBean.itemBean> items = data.get(i).items;
            if (items != null) {
                for (int j = 0; j < items.size(); j++) {
                    CorrelationNumberItemLayout itemView = new CorrelationNumberItemLayout(getContext());
                    itemView.setItemName(items.get(i).itemName);
                    itemView.setItemPhone(items.get(i).itemPhone);
                    layout.addItemView(itemView);
                }
            }
            viewGroup.addView(layout);
        }
    }

}
