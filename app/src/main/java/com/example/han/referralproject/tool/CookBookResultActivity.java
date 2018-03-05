package com.example.han.referralproject.tool;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.han.referralproject.R;
import com.example.han.referralproject.tool.adapter.CookBookRVAdapter;
import com.example.han.referralproject.tool.xfparsebean.CookbookBean;

import java.io.Serializable;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CookBookResultActivity extends AppCompatActivity {

    @BindView(R.id.rv)
    RecyclerView rv;
    private List<CookbookBean> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycle_view_demo);
        ButterKnife.bind(this);
        data = (List<CookbookBean>) getIntent().getSerializableExtra("data");
        initView();
    }

    public static void StartMe(Context context,List<CookbookBean> data){
        Intent intent = new Intent(context, CookBookResultActivity.class);
        intent.putExtra("data",(Serializable)data);
        context.startActivity(intent);
    }

    private void initView() {
        LinearLayoutManager layout = new LinearLayoutManager(this);
        layout.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(layout);
        rv.setAdapter(new CookBookRVAdapter(R.layout.cook_item, data));
    }
}
