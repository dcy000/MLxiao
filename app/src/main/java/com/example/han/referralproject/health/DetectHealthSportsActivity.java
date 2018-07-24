package com.example.han.referralproject.health;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
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
import com.example.han.referralproject.activity.DetectActivity;
import com.medlink.danbogh.register.SelectAdapter;
import com.medlink.danbogh.utils.T;

import java.util.ArrayList;

import github.hellocsl.layoutmanager.gallery.GalleryLayoutManager;

import static org.litepal.crud.DataSupport.select;

public class DetectHealthSportsActivity extends BaseActivity {

    private String detectCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        detectCategory = getIntent().getStringExtra("detectCategory");
        setContentView(R.layout.detect_health_activity_sports);
        mToolbar.setVisibility(View.VISIBLE);
        mTitleText.setText("选择运动时间");
        mRightText.setVisibility(View.GONE);
        mRightView.setVisibility(View.GONE);
        tvGoback = (TextView) findViewById(R.id.tv_sign_up_go_back);
        tvGoForward = (TextView) findViewById(R.id.tv_sign_up_go_forward);
        rvTimes = (RecyclerView) findViewById(R.id.detect_health_rv_times);
        rvMinutes = (RecyclerView) findViewById(R.id.detect_health_rv_minutes);
        tvGoback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetectHealthSportsActivity.this, DetectHealthSymptomsActivity.class);
                intent.putExtras(getIntent());
                intent.putExtra("times", "0");
                intent.putExtra("minutes", "0");
                startActivity(intent);
                finish();
            }
        });
        tvGoForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Class<? extends Activity> cls = detectCategory.equals("detectSugar")
                        ? DetectHealthDrinkActivity.class
                        :DetectHealthSaltActivity.class;
                Intent intent = new Intent(DetectHealthSportsActivity.this, cls);
                intent.putExtras(getIntent());
                intent.putExtra("times",  times.get(timeSelected % times.size()));
                intent.putExtra("minutes",  minutes.get(minutesSelected % times.size()));
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
    private RecyclerView rvMinutes;
    protected SelectAdapter minutesAdapter;
    public ArrayList<String> minutes;
    private int minutesSelected;

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
        for (int i = 0; i < 19; i++) {
            times.add(String.valueOf(i));
        }
        timesAdapter.setStrings(times);
        timesAdapter.setOnItemClickListener(new SelectAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                rvTimes.smoothScrollToPosition(position);
            }
        });
        rvTimes.setAdapter(timesAdapter);

        GalleryLayoutManager layoutManager1 = new GalleryLayoutManager(GalleryLayoutManager.VERTICAL);
        layoutManager1.attach(rvMinutes, minutesSelected);
        layoutManager1.setCallbackInFling(true);
        layoutManager1.setOnItemSelectedListener(new GalleryLayoutManager.OnItemSelectedListener() {
            @Override
            public void onItemSelected(RecyclerView recyclerView, View item, int position) {
                minutesSelected = position;
                select((String) (minutes == null ? String.valueOf(position) : minutes.get(position)));
            }
        });
        minutesAdapter = new SelectAdapter();
        minutes = new ArrayList<>();
        for (int i = 10; i < 181; i++) {
            minutes.add(String.valueOf(i));
        }
        minutesAdapter.setStrings(minutes);
        minutesAdapter.setOnItemClickListener(new SelectAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                rvMinutes.smoothScrollToPosition(position);
            }
        });
        rvMinutes.setAdapter(minutesAdapter);
    }

    @Override
    protected void backLastActivity() {
        tvGoback.performClick();
    }

    @Override
    protected void onResume() {
        speak("请选择运动时间");
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
