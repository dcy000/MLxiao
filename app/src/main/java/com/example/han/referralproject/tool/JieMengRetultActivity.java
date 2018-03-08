package com.example.han.referralproject.tool;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.tool.adapter.DreamRVadapter;
import com.example.han.referralproject.tool.xfparsebean.CookbookBean;
import com.example.han.referralproject.tool.xfparsebean.DreamBean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class JieMengRetultActivity extends BaseActivity {

    @BindView(R.id.rv_dream_result)
    RecyclerView rvDreamResult;
    @BindView(R.id.tv_dream_title)
    TextView tvDreamTitle;
    @BindView(R.id.tv_dream_yuyi)
    TextView tvDreamYuyi;
    @BindView(R.id.tv_title)
    TextView tvTitle;

    private List<DreamBean> data = new ArrayList<>();
    private DreamRVadapter adapter;
    private String question;
    private String answer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jie_meng_retult);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        data = (List<DreamBean>) intent.getSerializableExtra("data");
        question = intent.getStringExtra("question");
        answer = intent.getStringExtra("anwser");
        speak(question+","+answer);
        initView();
        initEvent();
    }

    private void initEvent() {
        tvTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initView() {
        LinearLayoutManager layout = new LinearLayoutManager(this);
        layout.setOrientation(LinearLayoutManager.VERTICAL);
        rvDreamResult.setLayoutManager(layout);
        adapter = new DreamRVadapter(R.layout.item_dream, data);
        rvDreamResult.setAdapter(adapter);

        //标题,寓意
        tvDreamTitle.setText(question);
        tvDreamYuyi.setText(answer);
    }

    public static void startMe(Context context, List<DreamBean> data, String question,String anwser) {
        Intent intent = new Intent(context, JieMengRetultActivity.class);
        intent.putExtra("data", (Serializable) data);
        intent.putExtra("question",question);
        intent.putExtra("anwser",anwser);
        context.startActivity(intent);
    }

}
