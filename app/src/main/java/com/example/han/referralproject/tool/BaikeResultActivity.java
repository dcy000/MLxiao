package com.example.han.referralproject.tool;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.han.referralproject.R;
import com.example.han.referralproject.tool.xfparsebean.BaiKeBean;

import java.io.Serializable;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BaikeResultActivity extends AppCompatActivity {


    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_question)
    TextView tvQuestion;
    @BindView(R.id.img_pic)
    ImageView imgPic;
    @BindView(R.id.mt_result)
    MixtureTextView mtResult;
    private List<BaiKeBean> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baike_result);
        ButterKnife.bind(this);
        data = (List<BaiKeBean>) getIntent().getSerializableExtra("data");
        initView();

    }

    private void initView() {
        Glide.with(this).load(data.get(0).img).into(imgPic);
        tvQuestion.setText(getIntent().getStringExtra("question"));
        mtResult.setText(data.get(0).summary);
    }


    public static void startMe(Context context, List<BaiKeBean> data,String question) {
        Intent intent = new Intent(context, BaikeResultActivity.class);
        intent.putExtra("data", (Serializable) data);
        intent.putExtra("question", question);
        context.startActivity(intent);
    }

    @OnClick(R.id.tv_title)
    public void onViewClicked() {
        finish();
    }
}
