package com.example.han.referralproject.physicalexamination.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.network.NetworkManager;
import com.example.han.referralproject.physicalexamination.adapter.FragAdapter;
import com.example.han.referralproject.physicalexamination.bean.QuestionnaireBean;
import com.example.han.referralproject.physicalexamination.constant.ConstitutionJudgmentEnum;
import com.example.han.referralproject.physicalexamination.fragment.MonitorItemFragment;
import com.example.han.referralproject.physicalexamination.util.ConstitutionJudgmentUtil;
import com.example.han.referralproject.physicalexamination.util.JsonUtil;
import com.example.han.referralproject.util.LocalShared;
import com.google.gson.Gson;
import com.medlink.danbogh.utils.T;

import java.util.ArrayList;


public class ChineseMedicineMonitorActivity extends BaseActivity implements View.OnClickListener {

    public QuestionnaireBean data = new QuestionnaireBean();
    public static int count;
    private ArrayList<Fragment> fragments;
    private ViewPager vp;
    private QuestionnaireBean.QuestionListBean bean;


    private TextView previousItem;
    private TextView nextItem;
    private TextView cunrrentItem;
    private int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chinese_media_nonitor);
        initTitle();
        initData();
        initView();
        initOperaterEvent();
    }

    private void initTitle() {
        mToolbar.setVisibility(View.VISIBLE);
        mTitleText.setText("中医体质检测");
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
        data = gson.fromJson(jsonData, QuestionnaireBean.class);
        if (data != null && data.questionList != null && data.questionList.size() != 0)
            count = data.questionList.size();
    }

    private void initView() {
        fragments = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            fragments.add(MonitorItemFragment.getInstance((i + 1) + "", data.questionList.get(i)));
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
            data.questionList.get(index).score = score;
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
            getNetWorkData();
        } else if (view == nextItem) {

            if (!data.questionList.get(index).isSelected) {
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

    private void getNetWorkData() {
        String sex = LocalShared.getInstance(this).getSex();
        NetworkApi.getQuestionnaire((sex == null || sex.equals("")) ? sex : "0", new NetworkManager.SuccessCallback<QuestionnaireBean>() {
            @Override
            public void onSuccess(QuestionnaireBean response) {
                T.show(response.toString());
            }
        }, new NetworkManager.FailedCallback() {
            @Override
            public void onFailed(String message) {
                T.show(message);
            }
        });
    }

    private void submit() {
        String monitorResult = ConstitutionJudgmentUtil.getMonitorResult(data.questionList);
        Toast.makeText(this, monitorResult, Toast.LENGTH_LONG).show();
        ConstitutionJudgmentEnum element = ConstitutionJudgmentEnum.getElement(monitorResult);
        MonitorResultActivity.starMe(this,
                ConstitutionJudgmentUtil.getResultScores(data.questionList) + "\n" + element.getFeatrue() + element.getMaintenance(),
                monitorResult);
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
