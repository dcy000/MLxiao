package com.gcml.common.recommend.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.billy.cc.core.component.CC;
import com.gcml.common.business.R;
import com.gcml.common.data.UserSpHelper;
import com.gcml.common.recommend.adapter.RecommendAdapter;
import com.gcml.common.recommend.bean.get.GoodBean;
import com.gcml.common.recommend.network.RecommendRepository;
import com.gcml.common.repository.utils.DefaultObserver;
import com.gcml.common.utils.RxUtils;
import com.gcml.common.widget.dialog.LoadingDialog;

import java.util.Arrays;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class RencommendForUserFragment extends Fragment {
    private static final String ARG_PARAM1 = "detection";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private TextView tvLookMore;
    private TextView tvCommendText;
    private RecyclerView rvCommendGoods;
    private IChangToolbar iChangToolbar;
    private RecommendRepository recommendRepository = new RecommendRepository();
    private View noDataView;

    public void setOnChangToolbar(IChangToolbar iChangToolbar) {
        this.iChangToolbar = iChangToolbar;
    }

    public RencommendForUserFragment() {
    }

    public static RencommendForUserFragment newInstance(String mParam1, String param2) {
        RencommendForUserFragment fragment = new RencommendForUserFragment();

        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, mParam1);
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
        View view = inflater.inflate(R.layout.fragment_rencommend, container, false);
        bindView(view);
        return view;
    }


    private void bindView(View view) {
        tvCommendText = (TextView) view.findViewById(R.id.tv_commend_text);
        tvLookMore = (TextView) view.findViewById(R.id.tv_look_more);
        rvCommendGoods = (RecyclerView) view.findViewById(R.id.rv_commend_goods);
        noDataView = (RelativeLayout) view.findViewById(R.id.view_no_data);
        noDataView.setVisibility(View.GONE);
        tvLookMore.setOnClickListener(v -> {
            CC.obtainBuilder("com.gcml.market").build().call();
        });

        GridLayoutManager layout = new GridLayoutManager(getActivity(), 3);
//        rvCommendGoods.addItemDecoration(new GridDividerItemDecoration(UiUtils.pt(108), 0));
        rvCommendGoods.setLayoutManager(layout);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindData();
    }

    private void bindData() {
        LoadingDialog dialog = new LoadingDialog.Builder(getActivity())
                .setIconType(LoadingDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord("正在加载")
                .create();

        recommendRepository.recommendGoodsByUser(UserSpHelper.getUserId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        dialog.show();
                    }
                })
                .doOnTerminate(new Action() {
                    @Override
                    public void run() throws Exception {
                        dialog.dismiss();
                    }
                })
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new DefaultObserver<List<GoodBean>>() {
                               @Override
                               public void onNext(List<GoodBean> goodBeans) {
                                   rvCommendGoods.setAdapter(new RecommendAdapter(R.layout.layout_recommend_item, goodBeans));
                                   if (goodBeans == null || goodBeans.size() == 0) {
                                       noDataView.setVisibility(View.VISIBLE);
                                   }
                               }

                               @Override
                               public void onError(Throwable throwable) {
                                   super.onError(throwable);
                                   noDataView.setVisibility(View.VISIBLE);
                               }
                           }


                );

    }

    public List<Object> getData() {
        return Arrays.asList("药品名1", "药品名2", "药品名3");
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser) {
            if (iChangToolbar != null) {
                iChangToolbar.onChange(this);
            }
        }
    }
}
