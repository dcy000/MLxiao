package com.example.han.referralproject.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.LoginActivity;
import com.example.han.referralproject.adapter.ChangeAccountAdapter;
import com.example.han.referralproject.util.LocalShared;


public class ChangeAccountDialog extends Dialog implements View.OnClickListener{
    private RecyclerView mRecyclerView;
    private Context mContext;

    public ChangeAccountDialog(Context context){
        super(context, R.style.XDialog);
        mContext = context;
        String[] mAccountIds = LocalShared.getInstance(context).getAccounts();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_change_account);
        mRecyclerView = (RecyclerView) findViewById(R.id.rl_account);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        mRecyclerView.setAdapter(new ChangeAccountAdapter(mContext));
        findViewById(R.id.view_login).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.view_login:
                mContext.startActivity(new Intent(mContext, LoginActivity.class));
                ((Activity)mContext).finish();
                break;
        }
    }

}
