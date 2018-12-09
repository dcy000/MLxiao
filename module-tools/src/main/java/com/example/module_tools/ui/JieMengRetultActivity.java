package com.example.module_tools.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.module_tools.R;
import com.example.module_tools.R2;
import com.example.module_tools.adapter.DreamRVadapter;
import com.example.module_tools.bean.DreamBean;
import com.gcml.lib_widget.ToolbarBaseActivity;
import com.gcml.lib_widget.textview.CenterAlignImageSpan;
import com.gzq.lib_core.base.ui.BasePresenter;
import com.gzq.lib_core.base.ui.IPresenter;
import com.iflytek.synthetize.MLVoiceSynthetize;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class JieMengRetultActivity extends ToolbarBaseActivity {

    @BindView(R2.id.rv_dream_result)
    RecyclerView rvDreamResult;
    @BindView(R2.id.tv_dream_title)
    TextView tvDreamTitle;
    @BindView(R2.id.tv_dream_yuyi)
    TextView tvDreamYuyi;
    @BindView(R2.id.tv_title)
    TextView tvTitle;
    @BindView(R2.id.tv_back)
    TextView tvBack;

    private List<DreamBean> data = new ArrayList<>();
    private DreamRVadapter adapter;
    private String question;
    private String answer;



    @Override
    public int layoutId(Bundle savedInstanceState) {
        return R.layout.activity_jie_meng_retult;
    }

    @Override
    protected boolean isShowToolbar() {
        return false;
    }

    @Override
    public void initParams(Intent intentArgument) {
        data = (List<DreamBean>) intentArgument.getSerializableExtra("data");
        question = intentArgument.getStringExtra("question");
        answer = intentArgument.getStringExtra("anwser");
        MLVoiceSynthetize.startSynthesize(question + "," + answer);
    }

    private void initEvent() {
        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        LinearLayoutManager layout = new LinearLayoutManager(this);
        layout.setOrientation(LinearLayoutManager.VERTICAL);
        rvDreamResult.setLayoutManager(layout);
        adapter = new DreamRVadapter(R.layout.item_dream, data);
        rvDreamResult.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                MLVoiceSynthetize.stop();
                MLVoiceSynthetize.startSynthesize("梦见" + data.get(i).name + "," + data.get(i).content);
            }
        });

        //标题,寓意
        tvDreamTitle.setText(question);
//        tvDreamYuyi.setText(answer);
        addImageSpan();
        initEvent();
    }

    @Override
    public IPresenter obtainPresenter() {
        return new BasePresenter(this) {};
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

    private void addImageSpan() {
        SpannableString spanString = new SpannableString("   " + answer);
        Drawable d = getResources().getDrawable(R.drawable.span_bg_yuyi);
        d.setBounds(0, 0, 106, 64);
        ImageSpan span = new CenterAlignImageSpan(d);
        spanString.setSpan(span, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvDreamYuyi.append(spanString);
    }

}
