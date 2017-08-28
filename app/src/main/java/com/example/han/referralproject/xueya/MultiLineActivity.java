package com.example.han.referralproject.xueya;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;


import com.example.han.referralproject.R;
import com.linheimx.app.library.adapter.IValueAdapter;
import com.linheimx.app.library.charts.LineChart;
import com.linheimx.app.library.data.Entry;
import com.linheimx.app.library.data.Line;
import com.linheimx.app.library.data.Lines;
import com.linheimx.app.library.model.HighLight;
import com.linheimx.app.library.model.XAxis;
import com.linheimx.app.library.model.YAxis;
import com.linheimx.app.library.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class MultiLineActivity extends AppCompatActivity {

    private final static int LINE_NUM = 2;

    LineChart _lineChart;

    Line.CallBack_OnEntryClick onEntryClick = new Line.CallBack_OnEntryClick() {
        @Override
        public void onEntry(Line line, Entry entry) {
            Toast.makeText(MultiLineActivity.this, entry.toString(), Toast.LENGTH_SHORT).show();
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_line);

        _lineChart = (LineChart) findViewById(R.id.chart);


        setChartData(_lineChart, LINE_NUM);

        CheckBox cb = (CheckBox) findViewById(R.id.cb_cb);

        // 2. 点击折线上的点 ，回调
        cb.setChecked(true);
        cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                HighLight highLight = _lineChart.get_HighLight();
                highLight.setEnable(isChecked);// 启用高亮显示  默认为启用状态
                _lineChart.invalidate();

                Lines lines = _lineChart.getlines();
                for (Line line : lines.getLines()) {
                    if (isChecked) {
                        line.setOnEntryClick(onEntryClick);
                    } else {
                        line.setOnEntryClick(null);
                    }
                }
            }
        });

    }

    private void setChartData(LineChart lineChart, int lineCount) {

        // 高亮
        HighLight highLight = lineChart.get_HighLight();
        highLight.setEnable(true);// 启用高亮显示  默认为启用状态，每条折线图想要获取点击回调，highlight需要启用
        highLight.setxValueAdapter(new IValueAdapter() {
            @Override
            public String value2String(double value) {
                return "日期: " + value;
            }
        });
        highLight.setyValueAdapter(new IValueAdapter() {
            @Override
            public String value2String(double value) {
                return "血压: " + Math.round(value);
            }
        });

        // x,y轴上的单位
        XAxis xAxis = lineChart.get_XAxis();
        xAxis.set_unit("L");

        YAxis yAxis = lineChart.get_YAxis();
        yAxis.set_unit("mmol");

        Lines lines = new Lines();


        // 线的颜色
        int color = Color.parseColor("#FF0000");
        //#228B22
        Line line = createLine(1, color);
        lines.addLine(line);


        // 线的颜色
        int color1 = Color.parseColor("#228B22");
        //#228B22
        Line line1 = createLine(2, color1);
        lines.addLine(line1);


        lineChart.setLines(lines);
    }


    private Line createLine(int order, int color) {

        final Line line = new Line();
        List<Entry> list = new ArrayList<>();

        if (order == 1) {

            double x = 3.15;
            double y = 140;
            list.add(new Entry(x, y));

            double x1 = 3.16;
            double y1 = 143;
            list.add(new Entry(x1, y1));

            double x2 = 3.17;
            double y2 = 138;
            list.add(new Entry(x2, y2));

            double x3 = 3.18;
            double y3 = 145;
            list.add(new Entry(x3, y3));

            double x4 = 3.20;
            double y4 = 148;
            list.add(new Entry(x4, y4));

        } else if (order == 2) {

            double x = 3.15;
            double y = 80;
            list.add(new Entry(x, y));

            double x1 = 3.16;
            double y1 = 88;
            list.add(new Entry(x1, y1));

            double x2 = 3.17;
            double y2 = 85;
            list.add(new Entry(x2, y2));

            double x3 = 3.18;
            double y3 = 83;
            list.add(new Entry(x3, y3));

            double x4 = 3.20;
            double y4 = 82;
            list.add(new Entry(x4, y4));

        }


        line.setEntries(list);
        line.setDrawLegend(true);//设置启用绘制图例
        //   line.setLegendWidth((int) Utils.dp2px(60));//设置图例的宽
        //    line.setLegendHeight((int)Utils.dp2px(60));//设置图例的高
        line.setLegendTextSize((int) Utils.dp2px(10));//设置图例上的字体大小
        if (order == 1) {
            line.setName("高压");
        } else {
            line.setName("低压");

        }
        line.setLineColor(color);
        line.setOnEntryClick(onEntryClick);

        return line;
    }


}
