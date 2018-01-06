package com.witspring.unitbody;

import android.os.Bundle;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.Button;

import com.witspring.base.BaseActivity;
import com.witspring.mlrobot.R;
import com.witspring.model.Constants;
import com.witspring.model.entity.Member;
import com.witspring.view.WheelPicker;

import java.util.ArrayList;
import java.util.Arrays;

public class AddMemberActivity extends BaseActivity {

    private WheelPicker wpSex, wpYear, wpMonth;
    private Button btnEnsure;
    private int selectedSexIndex, selectedYearIndex, selectedMonthIndex;
    public static final String[] sexs = {"男", "女"};
    public static final String[] ages_not_limit = {"不设月份"};
    public static final int TYPE_INIT = 0, TYPE_SMALL_THREE = 1, TYPE_LARGER_THREE = 2;
    private int sex = 0, ageMonth = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wsbody_activity_add_member);
        wpSex = findViewById(R.id.wpSex);
        wpYear = findViewById(R.id.wpYear);
        wpMonth = findViewById(R.id.wpMonth);
        btnEnsure = findViewById(R.id.btnEnsure);
        initView();
    }

    private void initView() {
        wpSex.setData(new ArrayList<>(Arrays.asList(sexs)));
        wpSex.setMoveNumber(1);
        wpYear.setData(getAges(true, 0));
        wpYear.setMoveNumber(7);
        wpMonth.setData(new ArrayList<String>(Arrays.asList(ages_not_limit)));
        wpMonth.setMoveNumber(3);

        wpSex.setDefault(1);
        wpYear.setDefault(28);
        wpMonth.setDefault(0);
        selectedSexIndex = 1;
        selectedYearIndex = 28;

        wpSex.setOnSelectListener(new WheelPicker.OnSelectListener() {
            @Override
            public void endSelect(int id, String text) {
                selectedSexIndex = id;
            }

            @Override
            public void selecting(int id, String text) {
            }
        });
        wpYear.setOnSelectListener(new WheelPicker.OnSelectListener() {
            @Override
            public void endSelect(int id, String text) {
                if (id == 1 || id == 2 || id == 3) {
                    if (wpMonth.getData().size() == 1) {
                        wpMonth.setData(new ArrayList<String>(getAges(false, TYPE_SMALL_THREE)));
                    }
                    wpMonth.setDefault(0);
                    selectedMonthIndex = 0;
                    wpMonth.setEnable(true);
                } else {
                    //if(wpMonth.getData().size() > 1){
                    wpMonth.setData(new ArrayList<String>(getAges(false, TYPE_LARGER_THREE)));
                    //}
                    wpMonth.setDefault(0);
                    wpMonth.setEnable(false);
                }
                selectedYearIndex = id;
                setBtnBg(id);
            }

            @Override
            public void selecting(int id, String text) {

            }
        });
        wpMonth.setOnSelectListener(new WheelPicker.OnSelectListener() {
            @Override
            public void endSelect(int id, String text) {
//                CommUtil.logI(Constants.TEST_TAG, "endSelect text:" + text);
                selectedMonthIndex = id;
            }

            @Override
            public void selecting(int id, String text) {
//                CommUtil.logI(Constants.TEST_TAG, "selecting text:" + text);
            }
        });
        wpMonth.setEnable(false);
    }

    @UiThread
    void setBtnBg(int id){
        if (id == 0){
            btnEnsure.setBackgroundResource(R.drawable.ws_btn_disable);
        } else {
            btnEnsure.setBackgroundResource(R.drawable.ws_round_green);
        }
    }

    private ArrayList<String> getAges(boolean isYear, int type) {
        ArrayList<String> ages = new ArrayList<String>();
        if (isYear) {
            ages.add("设置年龄");
            for (int i = 0; i <= 99; i++) {
                ages.add(i + " 岁");
            }
        } else {
            if (type == TYPE_INIT) {
                ages.add("不设月份");
            } else if (type == TYPE_LARGER_THREE) {
                ages.add("不设月份");
            } else if (type == TYPE_SMALL_THREE) {
                for (int i = 0; i <= 11; i++) {
                    ages.add(i + " 月");
                }
            }

        }
        return ages;
    }

    public void btnEnsure(View view) {
        if (view.getId() == R.id.btnCancel) {
            finish();
            return;
        }
        if (selectedSexIndex == 0) {
            sex = Constants.GENDER_MAN;
        }
        if (selectedSexIndex == 1) {
            sex = Constants.GENDER_WOMEN;
        }
        if (selectedYearIndex == 0) {
            showToastShort("请先设置年龄");
            return;
        } else if (selectedYearIndex == 1 || selectedYearIndex == 2 || selectedYearIndex == 3) {
            if (selectedMonthIndex == 0) {
                ageMonth = 12 * (selectedYearIndex - 1);
            } else {
                ageMonth = 12 * (selectedYearIndex - 1) + selectedMonthIndex;
            }
        } else {
            ageMonth = 12 * (selectedYearIndex - 1);
        }

        Member mem = new Member();
        mem.setAgeMonths(ageMonth);
        mem.setSex(sex);
        mem.setTimestamp(System.currentTimeMillis());
        getIntent().putExtra("member", mem);
        setResult(RESULT_OK, getIntent());
        finish();
    }

}
