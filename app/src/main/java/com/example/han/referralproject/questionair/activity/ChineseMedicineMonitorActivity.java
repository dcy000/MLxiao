package com.example.han.referralproject.questionair.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.questionair.adapter.FragAdapter;
import com.example.han.referralproject.questionair.bean.MonitorRequestionBean;
import com.example.han.referralproject.questionair.constant.ConstitutionJudgmentEnum;
import com.example.han.referralproject.questionair.constant.SexEnum;
import com.example.han.referralproject.questionair.fragment.MonitorItemFragment;
import com.example.han.referralproject.questionair.util.ConstitutionJudgmentUtil;
import com.example.han.referralproject.questionair.util.JsonUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;


public class ChineseMedicineMonitorActivity extends BaseActivity implements View.OnClickListener {

    public List<MonitorRequestionBean> data = new ArrayList<>();
    public static int count;
    private ArrayList<Fragment> fragments;
    private ViewPager vp;
    private MonitorRequestionBean bean;


    private TextView previousItem;
    private TextView nextItem;
    private TextView cunrrentItem;
    private int index;

    private SexEnum sex = SexEnum.woman;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chinese_media_nonitor);
        initData();
        initView();
        initTitle();
        initOperaterEvent();
    }

    private void initTitle() {
        mToolbar.setVisibility(View.VISIBLE);
        mTitleText.setText("中医体质检测");
    }

    private void initData() {
        String jsonData = JsonUtil.getJson(this, "monitor.json");
        Gson gson = new Gson();
        data = gson.fromJson(jsonData, new TypeToken<List<MonitorRequestionBean>>() {
        }.getType());
//
//        if (sex == SexEnum.man) {
//            data.remove(44);
//        } else {
//            data.remove(45);
//        }


        if (data != null)
            count = data.size();
    }

    private void initView() {
        fragments = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            fragments.add(MonitorItemFragment.getInstance((i + 1) + "", data.get(i)));
        }

        vp = findViewById(R.id.vp);
        FragAdapter adapter = new FragAdapter(getSupportFragmentManager(), fragments);
        vp.setAdapter(adapter);

        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                ChineseMedicineMonitorActivity.this.index = position;
                cunrrentItem.setText((index + 1) + "/" + count);

                if (isLastPager()) {
                    nextItem.setText("提交");
                } else {
                    nextItem.setText("下一题");
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public void nextCurrentPage() {
        vp.setCurrentItem(index + 1, true);
    }

    public void preCurrentPage() {
        vp.setCurrentItem(index - 1, true);
    }

    public void setScore(int index, int score) {
        if (data != null) {
            data.get(index).score = score;
        }
    }

    private void initOperaterEvent() {
        previousItem = findViewById(R.id.tv_previous_item);
        nextItem = findViewById(R.id.tv_next_item);

        previousItem.setOnClickListener(this);
        nextItem.setOnClickListener(this);

        cunrrentItem = findViewById(R.id.tv_current_item);
        cunrrentItem.setText(1 + "/" + count);
    }

    private boolean isLastPager() {
        return index == count - 1;
    }


    @Override
    public void onClick(View view) {
        if (view == previousItem) {
            preCurrentPage();
        } else if (view == nextItem) {

            if (!data.get(index).isSelected) {
                Toast.makeText(this, "请选择答案", Toast.LENGTH_LONG).show();
                return;
            }

            if (isLastPager()) {
                submit();
                return;
            }
            nextCurrentPage();

        }
    }

    private void submit() {
        String monitorResultTemp = getFilterString(ConstitutionJudgmentUtil.getMonitorResult(data));
//        Toast.makeText(this, monitorResult, Toast.LENGTH_LONG).show();

        String monitorResult = monitorResultTemp.replace("基本是", "").replace("偏", "");
        ConstitutionJudgmentEnum element = ConstitutionJudgmentEnum.getElement(monitorResult.substring(0, 3));

        MonitorResultActivity.starMe(this,
                ConstitutionJudgmentUtil.getResultScores(data) + "\n" + element.getFeatrue() + "\n" + element.getMaintenance(),
                monitorResultTemp);
    }

    @NonNull
    private String getFilterString(String string) {
        if (TextUtils.isEmpty(string)) {
            return "";
        }
        int length = string.length();
        if ((string.lastIndexOf(length) + "").equals(",")) {
            return string.substring(0, length - 1);
        }
        return string;
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        int currentItem = vp.getCurrentItem();
        if (keyCode == KeyEvent.KEYCODE_BACK && currentItem >= 1) {
            vp.setCurrentItem(vp.getCurrentItem() - 1);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
