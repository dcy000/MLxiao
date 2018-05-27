package com.example.han.referralproject.health;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.GridView;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.medlink.danbogh.utils.T;

import java.util.ArrayList;

public class DetectHealthDrinkActivity extends BaseActivity {

    private GridView gvItems;
    private MyAdapter adapter;
    private TextView tvGoback;
    private TextView tvGoForward;
    private String detectCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        detectCategory = getIntent().getStringExtra("detectCategory");
        setContentView(R.layout.detect_health_activity_drink);
        mToolbar.setVisibility(View.VISIBLE);
        mTitleText.setText("饮酒情况");
        mRightText.setVisibility(View.GONE);
        mRightView.setVisibility(View.GONE);
        gvItems = (GridView) findViewById(R.id.detect_health_gv_items);
        tvGoback = (TextView) findViewById(R.id.tv_sign_up_go_back);
        tvGoForward = (TextView) findViewById(R.id.tv_sign_up_go_forward);
        tvGoback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Class<? extends Activity> cls = detectCategory.equals("detectHealth")
                        ? DetectHealthSaltActivity.class
                        : DetectHealthSportsActivity.class;
                Intent intent = new Intent(DetectHealthDrinkActivity.this, cls);
                intent.putExtras(getIntent());
                intent.putExtra("drink", "中");
                startActivity(intent);
                finish();
            }
        });
        tvGoForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetectHealthDrinkActivity.this, DetectHealthSmokeActivity.class);
                intent.putExtras(getIntent());
                int checkedItemPosition = gvItems.getCheckedItemPosition();
                String value = checkedItemPosition >= 0 && checkedItemPosition < items.size()
                        ? items.get(checkedItemPosition)
                        : "中";
                intent.putExtra("drink", value);
                startActivity(intent);
                finish();
            }
        });
        prepareData();
        adapter = new MyAdapter();
        gvItems.setChoiceMode(GridView.CHOICE_MODE_SINGLE);
        gvItems.setAdapter(adapter);
        gvItems.setItemChecked(1, true);
    }

    private void prepareData() {
        items.add("轻");
        items.add("中");
        items.add("重");
    }

    private ArrayList<String> items = new ArrayList<>();

    private View.OnClickListener symptomOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            VH vh = (VH) v.getTag();
            gvItems.setItemChecked(vh.position, vh.cbSymptom.isChecked());
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
            cbSymptom.setChecked(gvItems.isItemChecked(position));
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
        tvGoback.performLongClick();
    }

    @Override
    protected void onResume() {
        speak("请选择饮酒情况？");
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
                gvItems.setItemChecked(i, true);
                return;
            }
        }
    }
}
