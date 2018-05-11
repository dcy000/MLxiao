package com.example.han.referralproject.bodytest.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.bodytest.activity.ChineseMedicineMonitorActivity;
import com.example.han.referralproject.bodytest.bean.MonitorRequestionBean;


/**
 * Created by lenovo on 2018/5/8.
 */

public class MonitorItemFragment extends Fragment {
    public static final String KEY_INDEX = "key_index";
    public static final String KEY_DATA = "key_data";
    private String index;
    private View view;
    private MonitorRequestionBean data;
    private ChineseMedicineMonitorActivity activity;
    private int lastXChecedId;


    public static MonitorItemFragment getInstance(String index, MonitorRequestionBean data) {
        MonitorItemFragment fragment = new MonitorItemFragment();
        Bundle args = new Bundle();
        args.putString(KEY_INDEX, index);
        args.putSerializable(KEY_DATA, data);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (ChineseMedicineMonitorActivity) getActivity();
        if (getArguments() != null) {
            index = getArguments().getString(KEY_INDEX);
            data = (MonitorRequestionBean) getArguments().getSerializable(KEY_DATA);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_item_monitor, container, false);
        initRadiogroup();
        return view;
    }

    private void initRadiogroup() {
        TextView question = view.findViewById(R.id.tv_question);
        question.setText(index + "." + data.requesionTitle);
        RadioGroup rgAnwser = view.findViewById(R.id.rg_anwser);

       /* //因答案选项固定,此处可以不赋值
        ((RadioButton) rgAnwser.getChildAt(0)).setText(data.anwser.A);
        ((RadioButton) rgAnwser.getChildAt(1)).setText(data.anwser.B);
        ((RadioButton) rgAnwser.getChildAt(2)).setText(data.anwser.C);
        ((RadioButton) rgAnwser.getChildAt(3)).setText(data.anwser.D);
        ((RadioButton) rgAnwser.getChildAt(4)).setText(data.anwser.E);*/

        rgAnwser.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                handleRadioButtonClick(checkedId);
                if (lastXChecedId != checkedId) {
                    ((ChineseMedicineMonitorActivity) getActivity()).nextCurrentPage();
                }
                lastXChecedId = checkedId;

            }
        });
    }

    private void handleRadioButtonClick(int checkedId) {
        if (!data.isSelected) {
            data.isSelected = true;
        }
        switch (checkedId) {
            case R.id.a:
                //1分
                activity.setScore(Integer.parseInt(index) - 1, 1);
                break;
            case R.id.b:
                //2分
                activity.setScore(Integer.parseInt(index) - 1, 2);
                break;
            case R.id.c:
                //3分
                activity.setScore(Integer.parseInt(index) - 1, 3);
                break;
            case R.id.d:
                //4分
                activity.setScore(Integer.parseInt(index) - 1, 4);
                break;
            case R.id.e:
                //5分
                activity.setScore(Integer.parseInt(index) - 1, 5);
                break;
            default:
                break;
        }

    }
}
