package com.gcml.mall.ui;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.gcml.common.widget.dialog.AlertDialog;
import com.gcml.common.widget.dialog.SheetDialog;
import com.gcml.common.widget.toolbar.ToolBarClickListener;
import com.gcml.common.widget.toolbar.TranslucentToolBar;
import com.gcml.mall.R;


public class RechargeActivity extends AppCompatActivity implements View.OnClickListener {

    TranslucentToolBar mToolBar;
    public Button mButton50;
    public Button mButton100;
    public Button mButton200;
    public Button mButton500;
    public Button mButton1000;
    public Button mButtonOther;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge);

        mToolBar = findViewById(R.id.tb_recharge);
        mButton50 = findViewById(R.id.btn_recharge_50);
        mButton100 = findViewById(R.id.btn_recharge_100);
        mButton200 = findViewById(R.id.btn_recharge_200);
        mButton500 = findViewById(R.id.btn_recharge_500);
        mButton1000 = findViewById(R.id.btn_recharge_1000);
        mButtonOther = findViewById(R.id.btn_recharge_other);

        mButton50.setOnClickListener(this);
        mButton100.setOnClickListener(this);
        mButton200.setOnClickListener(this);
        mButton500.setOnClickListener(this);
        mButton1000.setOnClickListener(this);
        mButtonOther.setOnClickListener(this);
        initView();
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(getApplicationContext(), RechargeActivity.class);
        int i = view.getId();
        if (i == R.id.btn_recharge_50) {//                intent.putExtra("number", "5000");
            new SheetDialog(RechargeActivity.this).builder()
                    .setLeftData(R.drawable.ic_alipay, "确认", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    }).setMiddleData(R.drawable.ic_alipay, "确认", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    }).setRightData(R.drawable.ic_alipay, "确认", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
            }).show();
        } else if (i == R.id.btn_recharge_100) {
//            intent.putExtra("number", "10000");
//            startActivity(intent);
            new SheetDialog(RechargeActivity.this).builder()
                    .setLeftData(R.drawable.ic_alipay, "确认", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    }).setMiddleData(R.drawable.ic_alipay, "确认", new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            }).show();
        } else if (i == R.id.btn_recharge_200) {
//            intent.putExtra("number", "20000");
//            startActivity(intent);
            new SheetDialog(RechargeActivity.this).builder()
                    .setLeftData(R.drawable.ic_alipay, "确认", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    }).setRightData(R.drawable.ic_alipay, "确认", new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            }).show();

        } else if (i == R.id.btn_recharge_500) {
//            intent.putExtra("number", "50000");
//            startActivity(intent);
//                startActivity(intent);
            new AlertDialog(RechargeActivity.this).builder()
                    .setMsg("请登录账号，体验更多精彩操作,体验更多精彩操作,体验更多精彩操作。")
                    .setPositiveButton("确认", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    }).setNegativeButton("取消", new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            }).show();
        } else if (i == R.id.btn_recharge_1000) {
            intent.putExtra("number", "100000");
            startActivity(intent);

        } else if (i == R.id.btn_recharge_other) {//                Intent inten = new Intent(getApplicationContext(), RechargeDefineActivity.class);
//                startActivity(inten);

        }
    }

    private void initView() {
        mToolBar.setData("健 康 商 城", R.drawable.common_icon_back, "返回", R.drawable.common_icon_home, null, new ToolBarClickListener() {
            @Override
            public void onLeftClick() {

            }

            @Override
            public void onRightClick() {

            }
        });

        GradientDrawable normal50 = getDrawable(10, getResources().getColor(R.color.color_normal_btn50), 1, getResources().getColor(R.color.color_normal_btn50));
        GradientDrawable press50 = getDrawable(10, getResources().getColor(R.color.color_press_btn50), 1, getResources().getColor(R.color.color_press_btn50));
        StateListDrawable selector50 = getSelector(normal50, press50);
        mButton50.setBackground(selector50);

        GradientDrawable normal100 = getDrawable(10, getResources().getColor(R.color.color_normal_btn100), 1, getResources().getColor(R.color.color_normal_btn100));
        GradientDrawable press100 = getDrawable(10, getResources().getColor(R.color.color_press_btn100), 1, getResources().getColor(R.color.color_press_btn100));
        StateListDrawable selector100 = getSelector(normal100, press100);
        mButton100.setBackground(selector100);

        GradientDrawable normal200 = getDrawable(10, getResources().getColor(R.color.color_normal_btn200), 1, getResources().getColor(R.color.color_normal_btn200));
        GradientDrawable press200 = getDrawable(10, getResources().getColor(R.color.color_press_btn200), 1, getResources().getColor(R.color.color_press_btn200));
        StateListDrawable selector200 = getSelector(normal200, press200);
        mButton200.setBackground(selector200);

        GradientDrawable normal500 = getDrawable(10, getResources().getColor(R.color.color_normal_btn500), 1, getResources().getColor(R.color.color_normal_btn500));
        GradientDrawable press500 = getDrawable(10, getResources().getColor(R.color.color_press_btn500), 1, getResources().getColor(R.color.color_press_btn500));
        StateListDrawable selector500 = getSelector(normal500, press500);
        mButton500.setBackground(selector500);

        GradientDrawable normal1000 = getDrawable(10, getResources().getColor(R.color.color_normal_btn1000), 1, getResources().getColor(R.color.color_normal_btn1000));
        GradientDrawable press1000 = getDrawable(10, getResources().getColor(R.color.color_press_btn1000), 1, getResources().getColor(R.color.color_press_btn1000));
        StateListDrawable selector1000 = getSelector(normal1000, press1000);
        mButton1000.setBackground(selector1000);

        GradientDrawable normalOther = getDrawable(10, getResources().getColor(R.color.color_normal_btnother), 1, getResources().getColor(R.color.color_normal_btnother));
        GradientDrawable pressOther = getDrawable(10, getResources().getColor(R.color.color_press_btnother), 1, getResources().getColor(R.color.color_press_btnother));
        StateListDrawable selectorOther = getSelector(normalOther, pressOther);
        mButtonOther.setBackground(selectorOther);
    }

    /**
     * 返回上一页
     */
    private void backLastActivity() {
        finish();
    }

    /**
     * 返回到主页面
     */
    private void backMainActivity() {
//        startActivity(new Intent(mContext, MainActivity.class));
        finish();
    }

    /**
     * 设置selecter
     *
     * @param pressedDraw
     * @param normalDraw
     * @return
     */
    private StateListDrawable getSelector(Drawable normalDraw, Drawable pressedDraw) {
        StateListDrawable stateListDrawable = new StateListDrawable();
        stateListDrawable.addState(new int[]{android.R.attr.state_selected}, pressedDraw);
        stateListDrawable.addState(new int[]{}, normalDraw);
        return stateListDrawable;
    }

    /**
     * 设置shape
     *
     * @param radius
     * @param fillColor
     * @param width
     * @param strokeColor
     * @return
     */
    private GradientDrawable getDrawable(int radius, int fillColor, int width, int strokeColor) {
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setCornerRadius(radius);
        gradientDrawable.setColor(fillColor);
        gradientDrawable.setStroke(width, strokeColor);
        return gradientDrawable;
    }
}
