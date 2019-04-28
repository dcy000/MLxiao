package com.gcml.task.ui;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.billy.cc.core.component.CC;
import com.billy.cc.core.component.CCResult;
import com.gcml.common.data.UserEntity;
import com.gcml.common.data.UserSpHelper;
import com.gcml.common.router.AppRouter;
import com.gcml.common.utils.DefaultObserver;
import com.gcml.common.utils.RxUtils;
import com.gcml.common.widget.dialog.LoadingDialog;
import com.gcml.common.widget.toolbar.ToolBarClickListener;
import com.gcml.common.widget.toolbar.TranslucentToolBar;
import com.gcml.task.R;
import com.gcml.task.bean.TargetModel;
import com.gcml.task.bean.get.TaskReportBean;
import com.gcml.task.network.TaskRepository;
import com.sjtu.yifei.route.Routerfit;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * desc: 周目标差距列表页（包括食盐、运动、饮酒和体重） .
 * author: wecent .
 * date: 2018/8/20 .
 */

public class TaskWeekReportFragment extends Fragment {

    private TranslucentToolBar mToolBar;
    private RecyclerView mRecycler;
    private TextView mButton;
    private ArrayList<TargetModel> mTargetModels = new ArrayList<>();
    private RecyclerView.Adapter<TargetHolder> mAdapter;
    TaskRepository mTaskRepository = new TaskRepository();
    Handler mHandler = new Handler();
    private UserEntity mUser;

    public static TaskWeekReportFragment newInstance() {
        Bundle args = new Bundle();
        TaskWeekReportFragment fragment = new TaskWeekReportFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Routerfit.register(AppRouter.class)
                .getUserProvider()
                .getUserEntity()
                .subscribeOn(Schedulers.io())
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new DefaultObserver<UserEntity>() {
                    @Override
                    public void onNext(UserEntity o) {
                        super.onNext(o);
                        mUser = o;
                    }
                });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_task_week_report, container, false);

        bindView(view);
        bindData();
        return view;
    }

    private void bindView(View view) {
        mToolBar = view.findViewById(R.id.tb_task_dialy_report);
        mRecycler = view.findViewById(R.id.rv_task_dialy_report);
        mButton = view.findViewById(R.id.tv_task_dialy_action);
    }

    private void bindData() {
        mToolBar.setData("周 目 标 差 距", R.drawable.common_btn_back, "返回", 0, null, new ToolBarClickListener() {
            @Override
            public void onLeftClick() {
                getActivity().finish();
            }

            @Override
            public void onRightClick() {

            }
        });
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() != null) {
                    getActivity().finish();
                }
            }
        });
        GridLayoutManager lm = new GridLayoutManager(getContext(), 2);
        lm.setOrientation(LinearLayoutManager.VERTICAL);
        mRecycler.setLayoutManager(lm);
        mAdapter = new RecyclerView.Adapter<TargetHolder>() {
            @Override
            public TargetHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
                LayoutInflater inflater1 = LayoutInflater.from(viewGroup.getContext());
                View view1 = inflater1.inflate(R.layout.item_task_dialy_target, viewGroup, false);
                return new TargetHolder(view1);
            }

            @Override
            public void onBindViewHolder(TargetHolder viewHolder, int position) {
                viewHolder.onBind(position);
            }

            @Override
            public int getItemCount() {
                return mTargetModels == null ? 0 : mTargetModels.size();
            }
        };
        mRecycler.setAdapter(mAdapter);
    }

    @SuppressLint("CheckResult")
    @Override
    public void onResume() {
        super.onResume();
        LoadingDialog tipDialog = new LoadingDialog.Builder(getContext())
                .setIconType(LoadingDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord("正在加载")
                .create();
        mTaskRepository.taskReportListFromApi(UserSpHelper.getUserId(), "1")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) {
                        tipDialog.show();
                    }
                })
                .doOnTerminate(new Action() {
                    @Override
                    public void run() {
                        tipDialog.dismiss();
                    }
                })
                .as(RxUtils.autoDisposeConverter(this))
                .subscribeWith(new DefaultObserver<TaskReportBean>() {
                    @Override
                    public void onNext(TaskReportBean body) {
                        super.onNext(body);
                        if (body == null || getActivity() == null || getActivity().isFinishing() || getActivity().isDestroyed()) {
                            return;
                        }
                        setWeekReport(body);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        if (getActivity() == null || getActivity().isFinishing() || getActivity().isDestroyed()) {
                            return;
                        }
                        setWeekReport(null);
                        LoadingDialog errorDialog = new LoadingDialog.Builder(getContext())
                                .setIconType(LoadingDialog.Builder.ICON_TYPE_FAIL)
                                .setTipWord("请求失败")
                                .create();
                        errorDialog.show();
                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                errorDialog.dismiss();
                            }
                        }, 500);
                    }
                });
    }

    private void setWeekReport(TaskReportBean response) {
        if (response == null) {
            response = new TaskReportBean();
        }
        if (response.currentWeek == null) {
            response.currentWeek = new TaskReportBean.CurrentWeek();
        }
        if (response.lastWeek == null) {
            response.lastWeek = new TaskReportBean.LastWeek();
        }
        mTargetModels.clear();
        TargetModel targetModel = new TargetModel();
        targetModel.title = "本周盐摄入量";
        if (response.lastWeek.nam == null || response.lastWeek.nam.equals("-1")) {
            response.lastWeek.nam = "42.00";
        }
        if (response.currentWeek.na == null || response.currentWeek.na.equals("-1")) {
            response.currentWeek.na = "0.00";
        }
        String target = Float.parseFloat(response.currentWeek.na) < Float.parseFloat(response.lastWeek.nam) ? "少于" : "少于";
        targetModel.target = target + response.lastWeek.nam + "克";
        targetModel.targetLength = response.lastWeek.nam.length();
        targetModel.sourceLength = response.currentWeek.na == null ? 0 : response.currentWeek.na.length();
        targetModel.source = "摄入" + response.currentWeek.na + "克";
        mTargetModels.add(targetModel);
        targetModel = new TargetModel();
        targetModel.title = "本周酒精摄入量";
        if (response.lastWeek.drinkm == null || response.lastWeek.drinkm.equals("-1")) {
            response.lastWeek.drinkm = "0.00";
        }
        if (response.currentWeek.drink == null || response.currentWeek.drink.equals("-1")) {
            response.currentWeek.drink = "0.00";
        }
        target = Float.parseFloat(response.currentWeek.drink) < Float.parseFloat(response.lastWeek.drinkm) ? "少于" : "少于";
        targetModel.target = target + response.lastWeek.drinkm + "ml";
        targetModel.targetLength = response.lastWeek.drinkm.length();
        targetModel.sourceLength = response.lastWeek.drinkm.length();
        targetModel.source = "已饮" + response.currentWeek.drink + "ml";
        mTargetModels.add(targetModel);
        targetModel = new TargetModel();
        if (response.lastWeek.sportsm == null || response.lastWeek.sportsm.equals("-1")) {
            response.lastWeek.sportsm = "100.00";
        }
        if (response.currentWeek.sports == null || response.currentWeek.sports.equals("-1")) {
            response.currentWeek.sports = "0.00";
        }
        target = Float.parseFloat(response.currentWeek.sports) < Float.parseFloat(response.lastWeek.sportsm) ? "大于" : "大于";
        targetModel.title = "本周运动时间";
        targetModel.target = target + response.lastWeek.sportsm + "分钟";
        targetModel.targetLength = response.lastWeek.sportsm.length();
        targetModel.sourceLength = response.currentWeek.sports.length();
        targetModel.source = "运动" + response.currentWeek.sports + "分钟";
        mTargetModels.add(targetModel);
        targetModel = new TargetModel();
        targetModel.title = "体重";
        float targetWeight = 0.00f;
        float weight = 0.00f;
        if (response.lastWeek.bmim == null || response.lastWeek.bmim.equals("-1")) {
            response.lastWeek.bmim = "0.00";
        }
        if (response.lastWeek.bmis == null || response.lastWeek.bmis.equals("-1")) {
            response.lastWeek.bmis = "0.00";
        }
        float bmi = Float.parseFloat(response.lastWeek.bmis);
        float targetBmi = Float.parseFloat(response.lastWeek.bmim);
        float height = (mUser == null || TextUtils.isEmpty(mUser.height)) ? 0.0f : Float.parseFloat(mUser.height);
        if (height > 0) {
            targetWeight = targetBmi / height / height * 10000;
            weight = bmi / height / height * 10000;
        }
        target = bmi < targetBmi ? "少于" : "少于";
        String s = String.valueOf(targetWeight);
        String s1 = String.valueOf(weight);
        targetModel.target = target + s + "kg";
        targetModel.targetLength = s.length();
        targetModel.sourceLength = s1.length();
        targetModel.source = "体重" + s1 + "kg";
        mTargetModels.add(targetModel);
        mAdapter.notifyDataSetChanged();
    }

    private int[] backgroudReses = new int[]{
            R.drawable.health_ic_salt,
            R.drawable.health_ic_drink,
            R.drawable.health_ic_sports,
            R.drawable.health_ic_weight,
    };

    public class TargetHolder extends RecyclerView.ViewHolder {

        private TextView tvTarget;
        private TextView tvSource;
        private TextView tvTitle;

        public TargetHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.health_diary_tv_analysis_title);
            tvTarget = itemView.findViewById(R.id.health_diary_tv_target);
            tvSource = itemView.findViewById(R.id.health_diary_tv_source);
        }

        public void onBind(int position) {
            itemView.setBackgroundResource(backgroudReses[position % 4]);
            TargetModel model = mTargetModels.get(position);
            int startSource = 2;
            tvTitle.setText(model.title);
            SpannableString targetStyle = new SpannableString(model.target);
            targetStyle.setSpan(new ForegroundColorSpan(Color.parseColor("#333333")),
                    2, 2 + model.targetLength, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            targetStyle.setSpan(new AbsoluteSizeSpan(120),
                    2, 2 + model.targetLength, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            tvTarget.setText(targetStyle);
            SpannableString sourceStyle = new SpannableString(model.source);
            sourceStyle.setSpan(new ForegroundColorSpan(Color.parseColor("#FF5747")),
                    startSource, startSource + model.sourceLength, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            tvSource.setText(sourceStyle);
        }
    }
}
