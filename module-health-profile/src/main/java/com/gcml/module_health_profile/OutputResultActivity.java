package com.gcml.module_health_profile;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gcml.common.utils.UtilsManager;
import com.gcml.common.utils.base.ToolbarBaseActivity;
import com.gcml.common.utils.display.ToastUtils;
import com.gcml.lib_printer_8003dd.ConnectPrinterHelper;
import com.gcml.lib_printer_8003dd.IPrinterView;
import com.gcml.lib_widget.EclipseLinearLayout;
import com.iflytek.synthetize.MLVoiceSynthetize;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class OutputResultActivity extends ToolbarBaseActivity implements View.OnClickListener, IPrinterView {
    private ImageView mIvQr;
    /**
     * 扫描查看体检报告
     */
    private TextView mTvQr;
    private EclipseLinearLayout mLl1;
    /**
     * 体检报告打印
     */
    private TextView mTvPrinter;
    private EclipseLinearLayout mLl2;
    /**
     * 回到公卫首页
     */
    private TextView mTvGohome;
    private ConnectPrinterHelper printerHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_output_result);
        initView();
        initPrinter();
    }

    private void initPrinter() {
        printerHelper = new ConnectPrinterHelper(this);
        printerHelper.start();
    }

    private void initView() {
        mIvQr = (ImageView) findViewById(R.id.iv_qr);
        mTvQr = (TextView) findViewById(R.id.tv_qr);
        mLl1 = (EclipseLinearLayout) findViewById(R.id.ll_1);
        mLl1.setOnClickListener(this);
        mTvPrinter = (TextView) findViewById(R.id.tv_printer);
        mLl2 = (EclipseLinearLayout) findViewById(R.id.ll_2);
        mLl2.setOnClickListener(this);
        mTvGohome = (TextView) findViewById(R.id.tv_gohome);
        mTvGohome.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        int i = v.getId();
        if (i == R.id.ll_1) {
        } else if (i == R.id.ll_2) {
            if (printerHelper == null) {
                ToastUtils.showShort("连接打印机出错");
                MLVoiceSynthetize.startSynthesize(UtilsManager.getApplication(), "连接打印机出错");
                return;
            }
            if (printerHelper != null && !printerHelper.isConnected()) {
                ToastUtils.showShort("请先连接打印机");
                MLVoiceSynthetize.startSynthesize(UtilsManager.getApplication(), "请先连接打印机");
                return;
            }
            try {
                printResult();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } else if (i == R.id.tv_gohome) {

        } else {
        }
    }

    private void printResult() throws UnsupportedEncodingException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        Date date = new Date();
        String sDate = format.format(date);
        String sDate1 = format1.format(date);
        printerHelper.initPrinter();
        printerHelper.printTitle("健康智能管家检测数据单\n\n\n\n");
        printerHelper.printContent("患者：郭志强\n" +
                "检测日期:" + sDate + "\n" +
                "打印时间：" + sDate1 + "\n" +
                "检测项目      检测结果      检测单位    参考标准\n" +
                "血压          134/85        mmHg      90~140\n" +
                "血氧          98            %         >94%\n" +
                "体温          35.8          ℃        36.1~37.1\n" +
                "血糖          5.6           mmol/L    3.61~7.0\n" +
                "体重          67            Kg        --\n" +
                "尿酸          0.36          mmol/L    0.21~0.44\n" +
                "胆固醇        4.6           mmol/L    2.9~6.0\n");
        printerHelper.printBottom();
    }

    @Override
    public void updateState(String state) {
        ToastUtils.showShort(state);
        MLVoiceSynthetize.startSynthesize(this, state);
    }
}
