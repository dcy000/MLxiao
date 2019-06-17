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
import com.gcml.common.data.UserSpHelper;
import com.gcml.common.utils.DefaultObserver;
import com.gcml.common.utils.RxUtils;
import com.gcml.common.utils.Utils;
import com.gcml.common.utils.data.TimeUtils;
import com.gcml.module_health_profile.R;
import com.gcml.module_health_profile.bean.WarnBean;
import com.gcml.module_health_profile.bracelet.bean.FalseServiceItemBean;
import com.gcml.module_health_profile.data.HealthProfileRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static android.support.v7.widget.LinearLayoutManager.*;


public class ServiceHistoryFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private RecyclerView recyclerView;
    private List<WarnBean> data = new ArrayList<>();
    private BaseQuickAdapter<WarnBean, BaseViewHolder> adapter;

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
        adapter = new BaseQuickAdapter<WarnBean, BaseViewHolder>(R.layout.layout_item_service_item, data) {
            @Override
            protected void convert(BaseViewHolder helper, WarnBean item) {
                TextView people = helper.getView(R.id.tv_service_people);
                TextView type = helper.getView(R.id.tv_warn_type);
                TextView time = helper.getView(R.id.tv_warn_time);

                people.setText(item.handlerName);
                type.setText(item.warningType);
                time.setText(TimeUtils.long2StringDateWithTimeZone(item.warningTime, "GMT+8"));
            }
        };
        recyclerView.setAdapter(adapter);
        return inflate;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getData();
    }

    HealthProfileRepository repository = new HealthProfileRepository();

    private void getData() {
        repository.getWannings()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new DefaultObserver<List<WarnBean>>() {
                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                    }

                    @Override
                    public void onNext(List<WarnBean> warnBeans) {
                        super.onNext(warnBeans);
                        data.clear();
                        data.addAll(warnBeans);
                        adapter.notifyDataSetChanged();
                    }
                });
    }


}
