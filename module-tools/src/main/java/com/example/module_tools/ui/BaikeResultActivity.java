package com.example.module_tools.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.module_tools.R;
import com.example.module_tools.R2;
import com.example.module_tools.bean.BaiKeBean;
import com.gcml.lib_widget.ToolbarBaseActivity;
import com.gcml.lib_widget.textview.MixtureTextView;
import com.gzq.lib_core.base.ui.BasePresenter;
import com.gzq.lib_core.base.ui.IPresenter;
import com.iflytek.synthetize.MLVoiceSynthetize;

import java.io.Serializable;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BaikeResultActivity extends ToolbarBaseActivity {


    @BindView(R2.id.tv_title)
    TextView tvTitle;
    @BindView(R2.id.tv_question)
    TextView tvQuestion;
    @BindView(R2.id.img_pic)
    ImageView imgPic;
    @BindView(R2.id.mt_result)
    MixtureTextView mtResult;
    @BindView(R2.id.tv_back)
    TextView tvBack;
    private List<BaiKeBean> data;

    @Override
    public int layoutId(Bundle savedInstanceState) {
        return R.layout.activity_baike_result;
    }

    @Override
    public void initParams(Intent intentArgument) {
        data = (List<BaiKeBean>) intentArgument.getSerializableExtra("data");
        MLVoiceSynthetize.startSynthesize(data.get(0).summary);
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        Glide.with(this).load(data.get(0).img).into(imgPic);
        tvQuestion.setText(getIntent().getStringExtra("question"));
        mtResult.setText(data.get(0).summary);
    }

    @Override
    public IPresenter obtainPresenter() {
        return new BasePresenter(this) {};
    }


    public static void startMe(Context context, List<BaiKeBean> data, String question) {
        Intent intent = new Intent(context, BaikeResultActivity.class);
        intent.putExtra("data", (Serializable) data);
        intent.putExtra("question", question);
        context.startActivity(intent);
    }

    @OnClick(R2.id.tv_back)
    public void onViewClicked() {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MLVoiceSynthetize.stop();
    }
}
