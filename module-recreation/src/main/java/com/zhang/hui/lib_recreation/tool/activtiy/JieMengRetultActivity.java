package com.zhang.hui.lib_recreation.tool.activtiy;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.iflytek.synthetize.MLVoiceSynthetize;
import com.zhang.hui.lib_recreation.R;
import com.zhang.hui.lib_recreation.tool.adapter.DreamRVadapter;
import com.zhang.hui.lib_recreation.tool.wrapview.CenterAlignImageSpan;
import com.zhang.hui.lib_recreation.tool.xfparsebean.DreamBean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class JieMengRetultActivity extends AppCompatActivity {

    private RecyclerView rvDreamResult;
    private TextView tvDreamTitle;
    private TextView tvDreamYuyi;
    private ConstraintLayout clDreamResult;
    /**
     * 周公解梦
     */
    private TextView tvTitle;
    /**
     * 返回
     */
    private TextView tvBack;

    private List<DreamBean> data = new ArrayList<>();
    private DreamRVadapter adapter;
    private String question;
    private String answer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jie_meng_retult);
        Intent intent = getIntent();
        data = (List<DreamBean>) intent.getSerializableExtra("data");
        question = intent.getStringExtra("question");
        answer = intent.getStringExtra("anwser");
        MLVoiceSynthetize.startSynthesize(getApplicationContext(), question + "," + answer, false);
        initView();
    }

    private void initView() {
        rvDreamResult = (RecyclerView) findViewById(R.id.rv_dream_result);
        tvDreamTitle = (TextView) findViewById(R.id.tv_dream_title);
        tvDreamYuyi = (TextView) findViewById(R.id.tv_dream_yuyi);
        clDreamResult = (ConstraintLayout) findViewById(R.id.cl_dream_result);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvBack = (TextView) findViewById(R.id.tv_back);

        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        LinearLayoutManager layout = new LinearLayoutManager(this);
        layout.setOrientation(LinearLayoutManager.VERTICAL);
        rvDreamResult.setLayoutManager(layout);
        adapter = new DreamRVadapter(R.layout.item_dream, data);
        rvDreamResult.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                MLVoiceSynthetize.stop();
                MLVoiceSynthetize.startSynthesize(view.getContext(), "梦见" + data.get(i).name + "," + data.get(i).content, false);
            }
        });

        //标题,寓意
        tvDreamTitle.setText(question);
//        tvDreamYuyi.setText(answer);
        addImageSpan();
    }

    private void addImageSpan() {
        SpannableString spanString = new SpannableString("   " + answer);
        Drawable d = getResources().getDrawable(R.drawable.span_bg_yuyi);
        d.setBounds(0, 0, 106, 64);
        ImageSpan span = new CenterAlignImageSpan(d);
        spanString.setSpan(span, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvDreamYuyi.append(spanString);
    }

    public static void startMe(Context context, List<DreamBean> data, String question, String anwser) {
        Intent intent = new Intent(context, JieMengRetultActivity.class);
        intent.putExtra("data", (Serializable) data);
        intent.putExtra("question", question);
        intent.putExtra("anwser", anwser);
        context.startActivity(intent);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        MLVoiceSynthetize.stop();
    }

}
