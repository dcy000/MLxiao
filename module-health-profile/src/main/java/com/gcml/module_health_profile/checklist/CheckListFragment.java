package com.gcml.module_health_profile.checklist;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.gcml.common.utils.DefaultObserver;
import com.gcml.common.utils.RxUtils;
import com.gcml.common.widget.dialog.LoadingDialog;
import com.gcml.module_health_profile.R;
import com.gcml.module_health_profile.checklist.bean.CheckListInfoBean;
import com.gcml.module_health_profile.checklist.config.ChoiceInputLayoutHelper;
import com.gcml.module_health_profile.checklist.config.EntryBoxHelper;
import com.gcml.module_health_profile.checklist.config.OutLayoutHelper;
import com.gcml.module_health_profile.checklist.wrap.EntryBoxLinearLayout;
import com.gcml.module_health_profile.checklist.wrap.OutLayout;
import com.gcml.module_health_profile.checklist.wrap.SingleChoiceLayout;
import com.gcml.module_health_profile.data.HealthProfileRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class CheckListFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private LinearLayout llContainer;


    public static CheckListFragment newInstance(String param1, String param2) {
        CheckListFragment fragment = new CheckListFragment();
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
        View view = inflater.inflate(R.layout.fragment_check_list, container, false);
        llContainer = view.findViewById(R.id.ll_container);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
    }

    private LoadingDialog dialog;

    HealthProfileRepository repository = new HealthProfileRepository();

    private void initData() {
        dialog = new LoadingDialog.Builder(getContext())
                .setIconType(LoadingDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord("正在加载")
                .create();
        repository.getHealthCheckList("bff445ce1280473391df8eee45d0999b")
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
                .subscribe(new DefaultObserver<CheckListInfoBean>() {
                    @Override
                    public void onNext(CheckListInfoBean checkListInfoBean) {
                        super.onNext(checkListInfoBean);
                        simulation(checkListInfoBean);
                    }

                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        dialog.dismiss();
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                        dialog.dismiss();
                    }
                });

    }

    /***
     * 问题类型 01标题 11填空 21单选 22多选 90其他 ,
     * 数据类型 1文字 2时间 3数字 4地址
     */
    private void simulation(CheckListInfoBean checkListInfoBean) {
        List<CheckListInfoBean.TRdQuestion> questionList = checkListInfoBean.questionList;
        int size = questionList.size();
        for (int i = 0; i < size; i++) {
            //最外层
            CheckListInfoBean.TRdQuestion tRdQuestion = questionList.get(i);
            switch (tRdQuestion.questionType) {
                case "01":
                    EntryBoxLinearLayout input01 = new EntryBoxLinearLayout(getContext());
                    new EntryBoxHelper.Builder(input01).unit(tRdQuestion.dataUnit).build();

                    OutLayout layout = new OutLayout(getContext());
                    new OutLayoutHelper.Builder(layout).name(tRdQuestion.questionName).rightView(input01).build();
                    llContainer.addView(layout);
                    break;
                case "11":

                    EntryBoxLinearLayout input11 = new EntryBoxLinearLayout(getContext());
                    new EntryBoxHelper.Builder(input11).unit(tRdQuestion.dataUnit).build();

                    OutLayout layout11 = new OutLayout(getContext());
                    new OutLayoutHelper.Builder(layout11).name(tRdQuestion.questionName).rightView(input11).build();
                    llContainer.addView(layout11);
                    break;
                case "21":

                case "22":
                    List<CheckListInfoBean.TRdQuestion.TRdOption> optionList = tRdQuestion.optionList;
                        if (optionList == null) {
                            return;
                    }

                    SingleChoiceLayout choices = new SingleChoiceLayout(getContext());
                    new ChoiceInputLayoutHelper.Builder(choices).choices(optionList).build();

                    OutLayout choiceOut = new OutLayout(getContext());
                    new OutLayoutHelper.Builder(choiceOut).name(tRdQuestion.questionName).rightView(choices).build();
                    llContainer.addView(choiceOut);
                    break;
                case "90":
                    break;
                default:
            }
        }
    }
}
