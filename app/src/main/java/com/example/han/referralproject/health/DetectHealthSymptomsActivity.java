package com.example.han.referralproject.health;

import android.content.Intent;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.GridView;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.activity.DetectActivity;
import com.medlink.danbogh.utils.T;

import java.util.ArrayList;

public class DetectHealthSymptomsActivity extends BaseActivity {

    private GridView gvItems;
    private MyAdapter adapter;
    private TextView tvGoback;
    private TextView tvGoForward;
    private String detectCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        detectCategory = getIntent().getStringExtra("detectCategory");
        setContentView(R.layout.detect_activity_health_symptoms);
        mToolbar.setVisibility(View.VISIBLE);
        mTitleText.setText("我的症状");
        mRightText.setVisibility(View.GONE);
        mRightView.setVisibility(View.GONE);
        gvItems = (GridView) findViewById(R.id.detect_health_gv_items);
        tvGoback = (TextView) findViewById(R.id.tv_sign_up_go_back);
        tvGoForward = (TextView) findViewById(R.id.tv_sign_up_go_forward);
        tvGoback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetectHealthSymptomsActivity.this, DetectActivity.class);
                intent.putExtras(getIntent());
                intent.putExtra("symptoms", new long[]{});
                intent.putExtra("type", "tizhong");
                startActivity(intent);
                finish();
            }
        });
        tvGoForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetectHealthSymptomsActivity.this, DetectHealthSportsActivity.class);
                intent.putExtras(getIntent());
                SparseBooleanArray checkedItemPositions = gvItems.getCheckedItemPositions();
                int count = gvItems.getCheckedItemCount();
                long[] checked = new long[count];
                int j = 0;
                for (int i = 0; i < items.size() ; i++) {
                    if (checkedItemPositions.get(i)) {
                        checked[j] = i;
                        checked[j]+=1;
                        j++;
                    }
                }
                intent.putExtra("symptoms", checked);
                startActivity(intent);
                finish();
            }
        });
        prepareData();
        adapter = new MyAdapter();
        gvItems.setAdapter(adapter);
        gvItems.setChoiceMode(GridView.CHOICE_MODE_MULTIPLE);
    }

    private void prepareData() {
        switch (detectCategory) {
            case "detectHealth":
                items.add("无症状");
                items.add("头痛");
                items.add("头晕");
                items.add("心悸");
                items.add("胸闷");
                items.add("胸痛");
                items.add("慢性咳嗽");
                items.add("咳痰");
                items.add("呼吸困难");
                items.add("多饮");
                items.add("多尿");
                items.add("体重下降");
                items.add("乏力");
                items.add("关节肿痛");
                items.add("视力模糊");
                items.add("手脚麻木");
                items.add("尿急");
                items.add("尿痛");
                items.add("便秘");
                items.add("腹泻");
                items.add("恶心呕吐");
                items.add("眼花");
                items.add("耳鸣");
                items.add("乳房胀痛");
                items.add("其他");
                break;
            case "detectPressure":
                items.add("无症状");
                items.add("头痛头晕");
                items.add("恶心呕吐");
                items.add("眼花耳鸣");
                items.add("呼吸困难");
                items.add("心悸胸闷");
                items.add("慢性咳嗽");
                items.add("咳痰");
                items.add("鼻衄出血不止");
                items.add("四肢发麻");
                items.add("下肢水肿");
                items.add("其他");
                break;
            case "detectSugar":
                items.add("无症状");
                items.add("多饮");
                items.add("多食");
                items.add("多尿");
                items.add("视力模糊");
                items.add("感染");
                items.add("手脚麻木");
                items.add("下肢浮肿");
                items.add("体重明显下降");
                items.add("其他");
                break;
        }
    }

    private ArrayList<String> items = new ArrayList<>();

    private View.OnClickListener symptomOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            VH vh = (VH) v.getTag();
            int size = items.size();
            int position = vh.position;
            if (position == 0 || position == size - 1) {
                gvItems.setItemChecked(position, vh.cbSymptom.isChecked());
                for (int i = 0; i < size; i++) {
                    if (i == position) {
                        continue;
                    }
                    gvItems.setItemChecked(i, false);
                }
            } else {
                gvItems.setItemChecked(0, false);
                gvItems.setItemChecked(size - 1, false);
                gvItems.setItemChecked(position, vh.cbSymptom.isChecked());
            }
        }
    };

    private class VH {
        public View itemView;
        private CheckBox cbSymptom;
        private int position;

        public VH(View itemView) {
            this.itemView = itemView;
            cbSymptom = (CheckBox) itemView.findViewById(R.id.detect_health_tv_item_item);
            itemView.setOnClickListener(symptomOnClickListener);
        }

        public void onBind(int position, String item) {
            this.position = position;
            boolean itemChecked = gvItems.isItemChecked(position);
            cbSymptom.setChecked(itemChecked);
            cbSymptom.setText(item);
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
                convertView = inflater.inflate(R.layout.detect_health_item_symptom, parent, false);
                vh = new VH(convertView);
                convertView.setTag(vh);
            } else {
                vh = (VH) convertView.getTag();
            }
            vh.onBind(position, getItem(position));
            return convertView;
        }
    }

    @Override
    protected void backLastActivity() {
        tvGoback.performClick();
    }

    @Override
    protected void onResume() {
        speak("您有没有以下症状？");
        setDisableGlobalListen(true);
        super.onResume();
    }

    public static final String REGEX_IN_GO_BACK = ".*(上一步|上一部|后退|返回).*";
    public static final String REGEX_IN_GO_FORWARD = ".*(下一步|下一部|确定|完成).*";

    @Override
    protected void onSpeakListenerResult(String result) {
        T.show(result);

        if (result.matches(REGEX_IN_GO_BACK)) {
            tvGoback.performLongClick();
            return;
        }
        if (result.matches(REGEX_IN_GO_FORWARD)) {
            tvGoForward.performLongClick();
            return;
        }

        //语音选择病史标签
        int size = items == null ? 0 : items.size();
        for (int i = 0; i < size; i++) {
            String item = items.get(i);
            if (result.contains(item)) {
                if (i == 0 || i == size - 1) {
                    gvItems.setItemChecked(i, !gvItems.isItemChecked(i));
                    for (int i1 = 0; i1 < size; i1++) {
                        gvItems.setItemChecked(i1, false);
                    }
                } else {
                    gvItems.setItemChecked(0, false);
                    gvItems.setItemChecked(size - 1, false);
                    gvItems.setItemChecked(i, !gvItems.isItemChecked(i));
                }
                gvItems.smoothScrollToPosition(i);
                return;
            }
        }
    }
}
