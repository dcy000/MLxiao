package com.gcml.module_hypertension_manager.ui;


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

import com.gcml.common.utils.UM;
import com.gcml.module_hypertension_manager.R;
import com.iflytek.synthetize.MLVoiceSynthetize;

import java.io.Serializable;
import java.util.List;


/**
 * Created by lenovo on 2018/7/24.
 */

public class MultipleChoiceStringFragment extends Fragment implements View.OnClickListener {
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
    TextView tvTitle;
    TextView tvTipContent;
    TextView tvButton;
    GridView gridView;
    public List<String> items;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.multiple_choice_fragment, null);
        tvTitle=view.findViewById(R.id.tv_title);
        tvTipContent=view.findViewById(R.id.tv_tip_content);
        tvButton=view.findViewById(R.id.tv_button);
        tvButton.setOnClickListener(this);
        gridView=view.findViewById(R.id.grid_view);
        Bundle arguments = getArguments();
        tvTitle.setText(arguments.getString(TIP_CONTENT));
        tvTipContent.setText(arguments.getString(WARM_TIP));
        items = (List<String>) arguments.getSerializable(CONTENT_STRINGS);
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

    public static MultipleChoiceStringFragment getInstance(String tipContent, String warmTip, List<String> items, boolean isMultiple) {
        MultipleChoiceStringFragment fragment = new MultipleChoiceStringFragment();
        Bundle bundle = new Bundle();
        bundle.putString(TIP_CONTENT, tipContent);
        bundle.putString(WARM_TIP, warmTip);
        bundle.putBoolean(IS_MULTIPLE_CHOOIC, isMultiple);
        bundle.putSerializable(CONTENT_STRINGS, (Serializable) items);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.tv_button){
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
            if (checked.length == 0) {
                MLVoiceSynthetize.startSynthesize(UM.getApp(), "您未选择任何选项", false);
                return;
            }
            listener.onNextStep(checked);
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
            cbSymptom = itemView.findViewById(R.id.multiple_choice_tv_item);
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
        void onNextStep(int[] checked);
    }

    public void setListener(MultipleChoiceStringFragment.OnButtonClickListener listener) {
        this.listener = listener;
    }

    private MultipleChoiceStringFragment.OnButtonClickListener listener;


}
