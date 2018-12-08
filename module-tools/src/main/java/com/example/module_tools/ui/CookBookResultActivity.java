package com.example.module_tools.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.module_tools.R;
import com.example.module_tools.R2;
import com.example.module_tools.adapter.CookBookRVAdapter;
import com.example.module_tools.bean.CookbookBean;
import com.iflytek.synthetize.MLVoiceSynthetize;

import java.io.Serializable;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CookBookResultActivity extends AppCompatActivity {

    @BindView(R2.id.rv)
    RecyclerView rv;
    @BindView(R2.id.tv_question)
    TextView tvQuestion;
    @BindView(R2.id.tv_title)
    TextView tvTitle;
    @BindView(R2.id.tv_back)
    TextView tvBack;
    private List<CookbookBean> data;
    private String question;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycle_view_demo);
        ButterKnife.bind(this);
        data = (List<CookbookBean>) getIntent().getSerializableExtra("data");
        question = getIntent().getStringExtra("question");
        initView();
        MLVoiceSynthetize.startSynthesize(question + "," + data.get(0).title + "," + data.get(0).steps);
    }

    public static void StartMe(Context context, List<CookbookBean> data, String question) {
        Intent intent = new Intent(context, CookBookResultActivity.class);
        intent.putExtra("data", (Serializable) data);
        intent.putExtra("question", question);
        context.startActivity(intent);
    }

    private void initView() {
        //
        tvQuestion.setText(question);
        LinearLayoutManager layout = new LinearLayoutManager(this);
        layout.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(layout);
        rv.setAdapter(new CookBookRVAdapter(R.layout.cook_item, data));
    }

    @OnClick({R2.id.tv_back})
    public void onViewClicked(View view) {
        int i = view.getId();
        if (i == R.id.tv_back) {
            finish();

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MLVoiceSynthetize.stop();
    }
}
