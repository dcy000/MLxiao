package com.example.han.referralproject.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.example.han.referralproject.R;


public class ChangeAccountDialog extends Dialog {
    private RecyclerView mRecyclerView;


    public ChangeAccountDialog(Context context){
        super(context, R.style.XDialog);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_change_account);
        mRecyclerView = (RecyclerView) findViewById(R.id.rl_account);
    }
}
