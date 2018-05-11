package com.example.han.referralproject.bodytest.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.bodytest.adapter.FragAdapter;
import com.example.han.referralproject.bodytest.bean.MonitorRequestionBean;
import com.example.han.referralproject.bodytest.constant.ConstitutionJudgmentEnum;
import com.example.han.referralproject.bodytest.fragment.MonitorItemFragment;
import com.example.han.referralproject.bodytest.util.ConstitutionJudgmentUtil;
import com.example.han.referralproject.bodytest.util.JsonUtil;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chinese_media_nonitor);
        initData();
        initView();
        initOperaterEvent();
    }

    private void initData() {
//        MonitorRequestionBean.AnwserBean anwserBean = new MonitorRequestionBean.AnwserBean();
//        anwserBean.A = "没有";
//        anwserBean.B = "很少";
//        anwserBean.C = "有时";
//        anwserBean.D = "经常";
//        anwserBean.E = "总是";
//
//        bean = new MonitorRequestionBean();
//        bean.requesionTitle = "您喜欢安静懒得说话吗？";
//        bean.anwser = anwserBean;
//
//        MonitorRequestionBean bean1 = new MonitorRequestionBean();
//        bean1.requesionTitle = "您面色晦暗或容易出现褐斑吗？";
//        bean1.anwser = anwserBean;
//
//        MonitorRequestionBean bean2 = new MonitorRequestionBean();
//        bean2.requesionTitle = "您容易精神紧张、焦虑不安吗？";
//        bean2.anwser = anwserBean;
//
//        data.add(bean);
//        data.add(bean1);
//        data.add(bean2);
        String jsonData = JsonUtil.getJson(this, "monitor.json");
        Gson gson = new Gson();
        data = gson.fromJson(jsonData, new TypeToken<List<MonitorRequestionBean>>() {
        }.getType());

        if (data != null)
            count = data.size();
    }

    private void initView() {
        mToolbar.setVisibility(View.VISIBLE);
        mTitleText.setText("中医体质检测");
        mRightView.setVisibility(View.GONE);
        mRightText.setVisibility(View.GONE);
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
                }else {
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
        String monitorResult = ConstitutionJudgmentUtil.getMonitorResult(data);
        Toast.makeText(this, monitorResult, Toast.LENGTH_LONG).show();
        ConstitutionJudgmentEnum element = ConstitutionJudgmentEnum.getElement(monitorResult);
        MonitorResultActivity.starMe(this,
                ConstitutionJudgmentUtil.getResultScores(data)+ "\n" + element.getFeatrue() + element.getMaintenance(),
                monitorResult );
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
