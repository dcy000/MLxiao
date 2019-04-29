package com.gcml.module_health_profile.checklist;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bigkoo.pickerview.view.TimePickerView;
import com.gcml.common.data.Province;
import com.gcml.common.utils.DefaultObserver;
import com.gcml.common.utils.RxUtils;
import com.gcml.common.utils.Utils;
import com.gcml.common.widget.dialog.LoadingDialog;
import com.gcml.module_health_profile.R;
import com.gcml.module_health_profile.checklist.bean.CheckListInfoBean;
import com.gcml.module_health_profile.checklist.layoutHelper.EntryBoxHelper;
import com.gcml.module_health_profile.checklist.layoutHelper.MultyChoiceInputLayoutHelper;
import com.gcml.module_health_profile.checklist.layoutHelper.OutLayoutHelper;
import com.gcml.module_health_profile.checklist.layoutHelper.SingleChoiceInputLayoutHelper;
import com.gcml.module_health_profile.checklist.wrap.EntryBoxLinearLayout;
import com.gcml.module_health_profile.checklist.wrap.MultipleChoiceLayout;
import com.gcml.module_health_profile.checklist.wrap.OutLayout;
import com.gcml.module_health_profile.checklist.wrap.SingleChoiceLayout;
import com.gcml.module_health_profile.data.HealthProfileRepository;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
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
        //地址选择
        initJsonData();
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
                        simulation(checkListInfoBean.questionList, false);
                   /*     TextView button = new TextView(getContext());
                        button.setOnClickListener(CheckListFragment.this);
                        llContainer.addView(button, llContainer.getChildCount() + 1);*/
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
    private void simulation(List<CheckListInfoBean.TRdQuestion> questionList, boolean childView) {
        if (questionList == null || questionList.size() == 0) {
            return;
        }
        int size = questionList.size();
        for (int i = 0; i < size; i++) {
            //最外层
            CheckListInfoBean.TRdQuestion tRdQuestion = questionList.get(i);
            OutLayout choiceOut;
            Boolean title;
            switch (tRdQuestion.questionType) {
                case "01":
                    title = true;
                    EntryBoxLinearLayout input01 = new EntryBoxLinearLayout(getContext());
                    new EntryBoxHelper
                            .Builder(input01)
                            .dateType(tRdQuestion.dataType)
                            .title(title)
                            .build();

                    choiceOut = new OutLayout(getContext());
                    new OutLayoutHelper
                            .Builder(choiceOut)
                            .name(tRdQuestion.questionName)
                            .rightView(input01)
                            .marginLeft(childView)
                            .build();
                    llContainer.addView(choiceOut);
                    break;
                case "11":
                    title = false;
                    EntryBoxLinearLayout input11 = new EntryBoxLinearLayout(getContext());
                    EntryBoxHelper EntryBoxHelper11 = new EntryBoxHelper
                            .Builder(input11)
                            .title(title)
                            .unit(tRdQuestion.dataUnit)
                            .inputListener(new EntryBoxLinearLayout.OnInputClickListener() {
                                @Override
                                public void onDateClick(EditText date) {
                                    CheckListFragment.this.date = date;
                                    selectBirthday();
                                }

                                @Override
                                public void onAddressClick(EditText address) {
                                    CheckListFragment.this.address = address;
                                    showAddressPicker();
                                }
                            })
                            .dateType(tRdQuestion.dataType)//此行写在inputListener后面(先赋值 后在dateTypezhong使用)
                            .questionId(tRdQuestion.questionId)
                            .build();

                    entryBoxHelpers.add(EntryBoxHelper11);

                    choiceOut = new OutLayout(getContext());
                    new OutLayoutHelper
                            .Builder(choiceOut)
                            .name(tRdQuestion.questionName)
                            .rightView(input11)
                            .marginLeft(childView)
                            .build();
                    llContainer.addView(choiceOut);
                    break;
                case "22":
                    List<CheckListInfoBean.TRdQuestion.TRdOption> optionListMulty = tRdQuestion.optionList;
                    if (optionListMulty == null) {
                        return;
                    }

                    MultipleChoiceLayout choicesMulty = new MultipleChoiceLayout(getContext());
                    multyChoiceInputLayoutHelpers
                            .add(new MultyChoiceInputLayoutHelper
                                    .Builder(choicesMulty)
                                    .choices(optionListMulty)
                                    .questionId(tRdQuestion.questionId)
                                    .build());

                    choiceOut = new OutLayout(getContext());
                    new OutLayoutHelper
                            .Builder(choiceOut)
                            .name(tRdQuestion.questionName)
                            .rightView(choicesMulty)
                            .marginLeft(childView)
                            .build();
                    llContainer.addView(choiceOut);
                    break;

                case "21":
                    List<CheckListInfoBean.TRdQuestion.TRdOption> optionList = tRdQuestion.optionList;
                    if (optionList == null) {
                        return;
                    }

                    SingleChoiceLayout choices = new SingleChoiceLayout(getContext());
                    singleChoiceInputLayoutHelpers.add(new SingleChoiceInputLayoutHelper
                            .Builder(choices)
                            .choices(optionList)
                            .questionId(tRdQuestion.questionId)
                            .build());

                    choiceOut = new OutLayout(getContext());
                    new OutLayoutHelper
                            .Builder(choiceOut)
                            .name(tRdQuestion.questionName)
                            .rightView(choices)
                            .marginLeft(childView)
                            .build();
                    llContainer.addView(choiceOut);
                    break;
                case "90":
                    break;
                default:
            }
            simulation(tRdQuestion.questionList, true);
        }
    }

    private List<Province> options1Items = new ArrayList<>();
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<String>>> options3Items = new ArrayList<>();

    private Observable<String> jsonData() {
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                String provincesArr = Utils.readTextFromAssetFile(getContext().getApplicationContext(), "cities.json");
                emitter.onNext(provincesArr);
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io());
    }

    private EditText address;
    private EditText date;

    private void showAddressPicker() {// 弹出选择器

        OptionsPickerView pvOptions = new OptionsPickerBuilder(getContext(), new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                String opt1tx = options1Items.size() > 0 ?
                        options1Items.get(options1).getPickerViewText() : "";

                String opt2tx = options2Items.size() > 0
                        && options2Items.get(options1).size() > 0 ?
                        options2Items.get(options1).get(options2) : "";

                String opt3tx = options2Items.size() > 0
                        && options3Items.get(options1).size() > 0
                        && options3Items.get(options1).get(options2).size() > 0 ?
                        options3Items.get(options1).get(options2).get(options3) : "";

                String tx = opt1tx + opt2tx + opt3tx;
                address.setText(tx);
            }
        })

                .setCancelText("取消")
                .setSubmitText("确认")
                .setLineSpacingMultiplier(1.5f)
                .setSubCalSize(30)
                .setContentTextSize(40)
                .setSubmitColor(Color.parseColor("#FF108EE9"))
                .setCancelColor(Color.parseColor("#FF999999"))
                .setTextColorOut(Color.parseColor("#FF999999"))
                .setTextColorCenter(Color.parseColor("#FF333333"))
                .setBgColor(Color.WHITE)
                .setTitleBgColor(Color.parseColor("#F5F5F5"))
                .setDividerColor(Color.TRANSPARENT)
                .isCenterLabel(false)
                .setOutSideCancelable(true)
                .build();

        /*pvOptions.setPicker(options1Items);//一级选择器
        pvOptions.setPicker(options1Items, options2Items);//二级选择器*/
        pvOptions.setPicker(options1Items, options2Items, options3Items);//三级选择器
        pvOptions.show();
    }

    private void initJsonData() {//解析数据
        jsonData().observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        showLoading("");
                    }
                })
                .map(new Function<String, List<Province>>() {
                    @Override
                    public List<Province> apply(String provincesArr) throws Exception {
                        Gson gson = new Gson();
                        return gson.fromJson(provincesArr, new TypeToken<List<Province>>() {
                        }.getType());
                    }
                })
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new DefaultObserver<List<Province>>() {

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        dismissLoading();
                    }

                    @Override
                    public void onNext(List<Province> provinces) {
                        super.onNext(provinces);
                        options1Items = provinces;
                        for (int i = 0; i < options1Items.size(); i++) {//遍历省份
                            ArrayList<String> cityList = new ArrayList<>();//该省的城市列表（第二级）
                            ArrayList<ArrayList<String>> province_AreaList = new ArrayList<>();//该省的所有地区列表（第三极）

                            for (int c = 0; c < options1Items.get(i).getCities().size(); c++) {//遍历该省份的所有城市
                                String cityName = options1Items.get(i).getCities().get(c).getName();
                                cityList.add(cityName);//添加城市
                                ArrayList<String> city_AreaList = new ArrayList<>();//该城市的所有地区列表

                                //如果无地区数据，建议添加空字符串，防止数据为null 导致三个选项长度不匹配造成崩溃
                /*if (jsonBean.get(i).getCityList().get(c).getArea() == null
                        || jsonBean.get(i).getCityList().get(c).getArea().size() == 0) {
                    city_AreaList.add("");
                } else {
                    city_AreaList.addAll(jsonBean.get(i).getCityList().get(c).getArea());
                }*/
                                city_AreaList.addAll(options1Items.get(i).getCities().get(c).getCounties());
                                province_AreaList.add(city_AreaList);//添加该省所有地区数据
                            }

                            options2Items.add(cityList);
                            options3Items.add(province_AreaList);
                        }
                        dismissLoading();
                    }
                });
    }

    private void selectBirthday() {
        Calendar selectedDate = Calendar.getInstance();
        Calendar startDate = Calendar.getInstance();
        Calendar endDate = Calendar.getInstance();
        startDate.set(1900, 0, 1);
//        endDate.set(2099, 11, 31);

        OnTimeSelectListener listener = new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                SimpleDateFormat birth = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                String birthString = birth.format(date);
                CheckListFragment.this.date.setText(birthString);
            }
        };
        TimePickerView pvTime = new TimePickerBuilder(getContext(), listener)
                .setType(new boolean[]{true, true, true, false, false, false})// 默认全部显示
                .setCancelText("取消")
                .setSubmitText("确认")
                .setLineSpacingMultiplier(1.5f)
                .setSubCalSize(30)
                .setContentTextSize(40)
                .setTextColorOut(Color.parseColor("#FF999999"))
                .setTextColorCenter(Color.parseColor("#FF333333"))
                .setSubmitText("确认")
                .setOutSideCancelable(false)
                .setDividerColor(Color.WHITE)
                .isCyclic(true)
                .setSubmitColor(Color.parseColor("#FF108EE9"))
                .setCancelColor(Color.parseColor("#FF999999"))
                .setTitleBgColor(Color.parseColor("#F5F5F5"))
                .setBgColor(Color.WHITE)
                .setDate(selectedDate)
                .setRangDate(startDate, endDate)
                .setLabel("年", "月", "日", "时", "分", "秒")//默认设置为年月日时分秒
                .isCenterLabel(false)
                .build();
        pvTime.show();
    }

    private void showLoading(String msg) {
        HealthCheckListActivity activity = (HealthCheckListActivity) getActivity();
        activity.showLoading(msg);
    }

    private void dismissLoading() {
        HealthCheckListActivity activity = (HealthCheckListActivity) getActivity();
        activity.dismissLoading();
    }

    List<CheckListInfoBean.TRdUserAnswer> tRdUserAnswers = new ArrayList<>();
    List<EntryBoxHelper> entryBoxHelpers = new ArrayList<>();
    List<MultyChoiceInputLayoutHelper> multyChoiceInputLayoutHelpers = new ArrayList<>();
    List<SingleChoiceInputLayoutHelper> singleChoiceInputLayoutHelpers = new ArrayList<>();

    public void postCheckList() {
        tRdUserAnswers.clear();
        int size = entryBoxHelpers.size();
        for (int i = 0; i < size; i++) {
            CheckListInfoBean.TRdUserAnswer answer = new CheckListInfoBean.TRdUserAnswer();
            EntryBoxHelper entryBoxHelper = entryBoxHelpers.get(i);
            answer.questionId = entryBoxHelper.questionId();
            answer.questionContent = entryBoxHelper.value();
            if (!TextUtils.isEmpty(entryBoxHelper.value())) {
                tRdUserAnswers.add(answer);
            }
        }

        int singletySize = singleChoiceInputLayoutHelpers.size();
        for (int i = 0; i < singletySize; i++) {
            CheckListInfoBean.TRdUserAnswer answer = new CheckListInfoBean.TRdUserAnswer();
            SingleChoiceInputLayoutHelper singleChoiceInputLayoutHelper = singleChoiceInputLayoutHelpers.get(i);
            answer.questionId = singleChoiceInputLayoutHelper.questionId();
            answer.optionId = singleChoiceInputLayoutHelper.optionId();
            if (!TextUtils.isEmpty(answer.optionId)) {
                tRdUserAnswers.add(answer);
            }
        }

        int multySize = multyChoiceInputLayoutHelpers.size();
        for (int i = 0; i < multySize; i++) {
            MultyChoiceInputLayoutHelper multyChoiceInputLayoutHelper = multyChoiceInputLayoutHelpers.get(i);
            if (multyChoiceInputLayoutHelper.options() != null && multyChoiceInputLayoutHelper.options().size() != 0) {
                tRdUserAnswers.addAll(multyChoiceInputLayoutHelper.options());
            }
        }
        repository
                .postHealthCheckList("bff445ce1280473391df8eee45d0999b", tRdUserAnswers)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnTerminate(new Action() {
                    @Override
                    public void run() throws Exception {

                    }
                })
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {

                    }
                })
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new DefaultObserver<Object>() {
                    @Override
                    public void onNext(Object o) {
                        super.onNext(o);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                    }
                });
    }

}
