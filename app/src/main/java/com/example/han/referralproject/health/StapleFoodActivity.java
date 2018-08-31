package com.example.han.referralproject.health;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.medlink.danbogh.register.SelectAdapter;
import com.medlink.danbogh.utils.T;

import java.util.ArrayList;

import github.hellocsl.layoutmanager.gallery.GalleryLayoutManager;

import static org.litepal.crud.DataSupport.select;

public class StapleFoodActivity extends BaseActivity {
    private String detectCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        detectCategory = getIntent().getStringExtra("detectCategory");
        setContentView(R.layout.activity_staple_food);
        mToolbar.setVisibility(View.VISIBLE);
        mTitleText.setText("每日主食量");
        mRightText.setVisibility(View.GONE);
        mRightView.setVisibility(View.GONE);
        tvGoback = (TextView) findViewById(R.id.tv_sign_up_go_back);
        tvGoForward = (TextView) findViewById(R.id.tv_sign_up_go_forward);
        rvTimes = (RecyclerView) findViewById(R.id.detect_health_rv_times);
        tvGoback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StapleFoodActivity.this, HypoglycemiaReactionActivity.class);
                intent.putExtras(getIntent());
                intent.putExtra("zhushi", "0");
                startActivity(intent);
                finish();
            }
        });
        tvGoForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StapleFoodActivity.this, DetectResultActivity.class);
                intent.putExtras(getIntent());
                intent.putExtra("zhushi", times.get(timeSelected));
                startActivity(intent);
                finish();
            }
        });
        initView();
    }

    private TextView tvGoback;
    private TextView tvGoForward;

    private RecyclerView rvTimes;
    protected SelectAdapter timesAdapter;
    public ArrayList<String> times;
    private int timeSelected;

    protected void initView() {
        GalleryLayoutManager layoutManager = new GalleryLayoutManager(GalleryLayoutManager.VERTICAL);
        layoutManager.attach(rvTimes, timeSelected);
        layoutManager.setCallbackInFling(true);
        layoutManager.setOnItemSelectedListener(new GalleryLayoutManager.OnItemSelectedListener() {
            @Override
            public void onItemSelected(RecyclerView recyclerView, View item, int position) {
                timeSelected = position;
                select((String) (times == null ? String.valueOf(position) : times.get(position)));
            }
        });
        timesAdapter = new SelectAdapter();
        times = new ArrayList<>();
        for (int i = 0; i < 201; i++) {
            times.add(String.valueOf(i * 15));
        }
        timesAdapter.setStrings(times);
        timesAdapter.setOnItemClickListener(new SelectAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                rvTimes.smoothScrollToPosition(position);
            }
        });
        rvTimes.setAdapter(timesAdapter);
    }

    @Override
    protected void backLastActivity() {
        tvGoback.performClick();
    }

    @Override
    protected void onResume() {
        speak("请选择每日主食量！");
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
        }

//        //语音选择病史标签
//        int size = items == null ? 0 : items.size();
//        for (int i = 0; i < size; i++) {
//            String item = items.get(i);
//            if (result.contains(item)) {
//                gvItems.setItemChecked(i, true);
//                gvItems.smoothScrollToPosition(i);
//                return;
//            }
//        }
    }
}
