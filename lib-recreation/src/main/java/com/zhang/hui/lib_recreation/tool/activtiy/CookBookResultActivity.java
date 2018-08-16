package com.zhang.hui.lib_recreation.tool.activtiy;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.iflytek.synthetize.MLVoiceSynthetize;
import com.zhang.hui.lib_recreation.R;
import com.zhang.hui.lib_recreation.tool.adapter.CookBookRVAdapter;
import com.zhang.hui.lib_recreation.tool.xfparsebean.CookbookBean;

import java.io.Serializable;
import java.util.List;

public class CookBookResultActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvQuestion;
    private RecyclerView rv;
    /**
     * 菜谱
     */
    private TextView tvTitle;
    /**
     * 返回
     */
    private TextView tvBack;

    private List<CookbookBean> data;
    private String question;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cook_book_result);
        initView();
        data = (List<CookbookBean>) getIntent().getSerializableExtra("data");
        question = getIntent().getStringExtra("question");
        initRV();
    }

    private void initRV() {
        tvQuestion.setText(question);
        LinearLayoutManager layout = new LinearLayoutManager(this);
        layout.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(layout);
        rv.setAdapter(new CookBookRVAdapter(R.layout.cook_item, data));
    }

    private void initView() {
        tvQuestion = (TextView) findViewById(R.id.tv_question);
        rv = (RecyclerView) findViewById(R.id.rv);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvBack = (TextView) findViewById(R.id.tv_back);
        tvBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.tv_back) {
            finish();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        MLVoiceSynthetize.stop();
    }

    public static void StartMe(Context context, List<CookbookBean> data, String question) {
        Intent intent = new Intent(context, CookBookResultActivity.class);
        intent.putExtra("data", (Serializable) data);
        intent.putExtra("question", question);
        context.startActivity(intent);
    }
}

