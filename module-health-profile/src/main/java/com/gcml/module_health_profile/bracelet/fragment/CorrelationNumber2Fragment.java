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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gcml.common.data.UserSpHelper;
import com.gcml.common.imageloader.ImageLoader;
import com.gcml.common.utils.DefaultObserver;
import com.gcml.common.utils.RxUtils;
import com.gcml.module_health_profile.R;
import com.gcml.module_health_profile.bean.GuardianInfo;
import com.gcml.module_health_profile.bracelet.bean.FalseCorrelationDataBean;
import com.gcml.module_health_profile.bracelet.wrap.CorrelationNumberItemLayout;
import com.gcml.module_health_profile.bracelet.wrap.CorrelationNumberLayout;
import com.gcml.module_health_profile.data.HealthProfileRepository;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;


public class CorrelationNumber2Fragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    private TextView deviceName;
    private TextView deviceImei;
    private TextView deviceOwner;
    private TextView bindPhone;
    private RecyclerView recycleView;
    private BaseQuickAdapter<GuardianInfo, BaseViewHolder> adapter;

    public CorrelationNumber2Fragment() {
    }

    public static CorrelationNumber2Fragment newInstance(String param1, String param2) {
        CorrelationNumber2Fragment fragment = new CorrelationNumber2Fragment();
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

    List<GuardianInfo> data = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_device_correalation2, container, false);
        recycleView = inflate.findViewById(R.id.rv_item_guardian);

        recycleView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new BaseQuickAdapter<GuardianInfo, BaseViewHolder>(R.layout.item_guardian, data) {
            @Override
            protected void convert(BaseViewHolder helper, GuardianInfo item) {
                ImageView head = (ImageView) helper.getView(R.id.item_iv_head);
                ImageLoader.with(getActivity())
                        .load(item.guardianPhoto)
                        .placeholder(R.drawable.common_ic_avatar_placeholder)
                        .error(R.drawable.common_ic_avatar_placeholder)
                        .into(head);

                ((TextView) helper.getView(R.id.item_tv_name)).setText(item.guardianName);
                ((TextView) helper.getView(R.id.tv_item_type)).setText(item.guardianType);
                ((TextView) helper.getView(R.id.tv_item_phoe)).setText(item.mobileNum);
            }

        };
        recycleView.setAdapter(adapter);
        return inflate;
    }

    HealthProfileRepository repository = new HealthProfileRepository();

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        repository.getGuardians()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new DefaultObserver<List<GuardianInfo>>() {
                    @Override
                    public void onNext(List<GuardianInfo> guardianInfos) {
                        super.onNext(guardianInfos);
                        data.clear();
                        data.addAll(guardianInfos);
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                    }
                });

    }
}
