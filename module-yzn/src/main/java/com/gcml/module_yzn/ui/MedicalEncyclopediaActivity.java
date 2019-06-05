package com.gcml.module_yzn.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import com.gcml.common.router.AppRouter;
import com.gcml.common.utils.base.ToolbarBaseActivity;
import com.gcml.common.widget.toolbar.ToolBarClickListener;
import com.gcml.common.widget.toolbar.TranslucentToolBar;
import com.gcml.module_yzn.R;
import com.sjtu.yifei.annotation.Route;
import com.sjtu.yifei.route.Routerfit;

@Route(path = "/module/yzn/zenduan/activity")
public class MedicalEncyclopediaActivity extends ToolbarBaseActivity {

    private EditText tvSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isShowToolbar = false;
        setContentView(R.layout.activity_medical_encyclopedia);
        initView();
    }

    private void initView() {
        tvSearch = findViewById(R.id.tv_search);
        tvSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                String quary = tvSearch.getText().toString().trim();
                if (TextUtils.isEmpty(quary)) {
                    return false;
                }
                startActivity(new Intent(this, ZenDuanActivity.class)
                        .putExtra("inputText", quary));
                return true;
            }
            return false;
        });

        TranslucentToolBar tb = findViewById(R.id.tb_medical_encyclopedia);
        tb.setData("医 疗 百 科", R.drawable.common_icon_back, "返回",
                R.drawable.common_ic_wifi_state, null,
                new ToolBarClickListener() {
                    @Override
                    public void onLeftClick() {
                        finish();
                    }

                    @Override
                    public void onRightClick() {
                        Routerfit.register(AppRouter.class).skipSettingActivity();
                    }
                });

        setWifiLevel(tb);

    }
}
