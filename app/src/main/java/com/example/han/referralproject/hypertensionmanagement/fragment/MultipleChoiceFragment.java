package com.example.han.referralproject.hypertensionmanagement.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.hypertensionmanagement.bean.PrimaryHypertensionQuestionnaireBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


/**
 * Created by lenovo on 2018/7/24.
 */

public class MultipleChoiceFragment extends Fragment {
    /**
     * title
     */
    private static String TIP_CONTENT = "tipContent";
    /**
     * 灰色的温馨提示
     */
    private static String WARM_TIP = "warmTip";

    /**
     * 选项列表
     */
    private static String CONTENT_STRINGS = "contentStrings";
    private static String IS_MULTIPLE_CHOOIC = "isMultipleChooic";
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_tip_content)
    TextView tvTipContent;
    @BindView(R.id.tv_button)
    TextView tvButton;
    @BindView(R.id.grid_view)
    GridView gridView;
    Unbinder unbinder;
    public List<String> items;
    private PrimaryHypertensionQuestionnaireBean.DataBean.QuestionListBean questionBean;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.multiple_choice_fragment, null);
        unbinder = ButterKnife.bind(this, view);
        Bundle arguments = getArguments();
        tvTitle.setText(arguments.getString(TIP_CONTENT));
        tvTipContent.setText(arguments.getString(WARM_TIP));
        questionBean = (PrimaryHypertensionQuestionnaireBean.DataBean.QuestionListBean) arguments.getSerializable(CONTENT_STRINGS);
        items = getStrings(questionBean);
        initGV(arguments);
        return view;
    }

    private void initGV(Bundle arguments) {
        gridView.setAdapter(new MyAdapter());
        if (arguments.getBoolean(IS_MULTIPLE_CHOOIC))
            gridView.setChoiceMode(GridView.CHOICE_MODE_MULTIPLE);
        else
            gridView.setChoiceMode(GridView.CHOICE_MODE_SINGLE);

    }

    public static MultipleChoiceFragment getInstance(String tipContent, String warmTip, PrimaryHypertensionQuestionnaireBean.DataBean.QuestionListBean questionBean, boolean isMultiple) {
        MultipleChoiceFragment fragment = new MultipleChoiceFragment();
        Bundle bundle = new Bundle();
        bundle.putString(TIP_CONTENT, tipContent);
        bundle.putString(WARM_TIP, warmTip);
        bundle.putBoolean(IS_MULTIPLE_CHOOIC, isMultiple);
        bundle.putSerializable(CONTENT_STRINGS, questionBean);
        fragment.setArguments(bundle);
        return fragment;
    }

    private static List<String> getStrings(PrimaryHypertensionQuestionnaireBean.DataBean.QuestionListBean questionBean) {
        List<String> strings = new ArrayList<>();
        for (int i = 0; i < questionBean.answerList.size(); i++) {
            strings.add(questionBean.answerList.get(i).answerInfo);
        }
        return strings;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.tv_button)
    public void onViewClicked() {
        SparseBooleanArray checkedItemPositions = gridView.getCheckedItemPositions();
        int count = gridView.getCheckedItemCount();
        int[] checked = new int[count];
        int j = 0;
        for (int i = 0; i < items.size(); i++) {
            if (checkedItemPositions.get(i)) {
                checked[j] = i;
//                checked[j] += 1;
                j++;
            }
        }

        if (listener != null) {
            if (checked.length==0){
                return;
            }
            listener.onNextStep(checked,questionBean);
        }
    }

    private class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public String getItem(int position) {
            return items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            VH vh;
            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(parent.getContext());
                convertView = inflater.inflate(R.layout.multiple_choice_item, parent, false);
                vh = new VH(convertView);
                convertView.setTag(vh);
            } else {
                vh = (VH) convertView.getTag();
            }
            vh.onBind(position, getItem(position));
            return convertView;
        }
    }

    private class VH {
        public View itemView;
        private CheckBox cbSymptom;
        private int position;

        public VH(View itemView) {
            this.itemView = itemView;
            cbSymptom = (CheckBox) itemView.findViewById(R.id.multiple_choice_tv_item);
            itemView.setOnClickListener(symptomOnClickListener);
        }

        public void onBind(int position, String item) {
            this.position = position;
            boolean itemChecked = gridView.isItemChecked(position);
            cbSymptom.setChecked(itemChecked);
            cbSymptom.setText(item);
        }
    }

    /**
     * 索引 1 2 3 . . .
     * 最后一个条目和之前的条目互斥
     */
    private View.OnClickListener symptomOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            VH vh = (VH) v.getTag();
            int size = items.size();
            int position = vh.position;
            if (/*position == 0 ||*/ position == size - 1) {
                gridView.setItemChecked(position, vh.cbSymptom.isChecked());
                for (int i = 0; i < size; i++) {
                    if (i == position) {
                        continue;
                    }
                    gridView.setItemChecked(i, false);
                }
            } else {
//                gridView.setItemChecked(0, false);
                gridView.setItemChecked(size - 1, false);
                gridView.setItemChecked(position, vh.cbSymptom.isChecked());
            }
        }
    };

    public interface OnButtonClickListener {
        void onNextStep(int[] checked, PrimaryHypertensionQuestionnaireBean.DataBean.QuestionListBean questionBean);
    }

    public void setListener(MultipleChoiceFragment.OnButtonClickListener listener) {
        this.listener = listener;
    }

    private MultipleChoiceFragment.OnButtonClickListener listener;


}
