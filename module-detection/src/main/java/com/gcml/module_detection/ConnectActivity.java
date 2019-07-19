package com.gcml.module_detection;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.gcml.common.recommend.bean.post.DetectionData;
import com.gcml.common.router.AppRouter;
import com.gcml.common.utils.RxUtils;
import com.gcml.common.utils.UM;
import com.gcml.common.utils.base.ToolbarBaseActivity;
import com.gcml.common.utils.data.DataUtils;
import com.gcml.common.utils.display.ToastUtils;
import com.gcml.common.utils.qrcode.QRCodeUtils;
import com.gcml.common.widget.base.dialog.DialogImage;
import com.gcml.common.widget.dialog.AlertDialog;
import com.gcml.common.widget.fdialog.BaseNiceDialog;
import com.gcml.common.widget.fdialog.NiceDialog;
import com.gcml.common.widget.fdialog.ViewConvertListener;
import com.gcml.common.widget.fdialog.ViewHolder;
import com.gcml.module_blutooth_devices.base.BaseBluetooth;
import com.gcml.module_blutooth_devices.base.BluetoothBaseFragment;
import com.gcml.module_blutooth_devices.base.DetectionDataBean;
import com.gcml.module_blutooth_devices.base.FragmentChanged;
import com.gcml.module_blutooth_devices.base.IBleConstants;
import com.gcml.module_blutooth_devices.base.IBluetoothView;
import com.gcml.module_blutooth_devices.base.IUploadData;
import com.gcml.module_blutooth_devices.bloodoxygen.BloodOxygenPresenter;
import com.gcml.module_blutooth_devices.bloodpressure.BloodPressurePresenter;
import com.gcml.module_blutooth_devices.bloodsugar.BloodSugarPresenter;
import com.gcml.module_blutooth_devices.ecg.ECGPresenter;
import com.gcml.module_blutooth_devices.ecg.ECG_PDF_Fragment;
import com.gcml.module_blutooth_devices.temperature.TemperaturePresenter;
import com.gcml.module_blutooth_devices.three.ThreeInOnePresenter;
import com.gcml.module_blutooth_devices.weight.WeightPresenter;
import com.gcml.module_detection.fragment.BloodOxygenFragment;
import com.gcml.module_detection.fragment.BloodSugarFragment;
import com.gcml.module_detection.fragment.BloodpressureFragment;
import com.gcml.module_detection.fragment.BloodsugarSearchFragment;
import com.gcml.module_detection.fragment.CholesterolFragment;
import com.gcml.module_detection.fragment.ECGFragment;
import com.gcml.module_detection.fragment.IDCardReadFragment;
import com.gcml.module_detection.fragment.SearchAnimFragment;
import com.gcml.module_detection.fragment.TemperatureFragment;
import com.gcml.module_detection.fragment.UricAcidFragment;
import com.gcml.module_detection.fragment.WeightFragment;
import com.gcml.module_detection.idcard.IDCardPresenter;
import com.iflytek.synthetize.MLVoiceSynthetize;
import com.kaer.sdk.IDCardItem;
import com.sjtu.yifei.annotation.Route;
import com.sjtu.yifei.route.Routerfit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DefaultObserver;
import timber.log.Timber;

@Route(path = "/module/detection/connect/activity")
public class ConnectActivity extends ToolbarBaseActivity
        implements IBluetoothView,
        DialogControlBluetooth,
        IDCardPresenter.IDCardRead,
        IDCardReadFragment.ClickPage,
        ECGFragment.AnalysisData,
        FragmentChanged,
        IUploadData {

    private BaseBluetooth baseBluetooth;
    private BluetoothListDialog dialog;
    private int detectionType;
    private BluetoothBaseFragment baseFragment;
    private boolean isAfterPause;
    private boolean isTimeCountDownOver;
    private AlertDialog retryDialog;
    private boolean onShowingEcgPDF;
    private String pdfUrl;
    private Bundle bundle;
    private boolean isSingleDetection;
    private TextView mTvNext;
    private DetectionDataBean dataBean;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect1);

        initView();
        dealSearchFragment();
    }

    @Override
    protected void onResume() {
        super.onResume();
        isAfterPause = false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        isAfterPause = true;
    }

    private void dealSearchFragment() {
        detectionType = getIntent().getIntExtra("detectionType", 0);

        switch (detectionType) {
            case IBleConstants.MEASURE_BLOOD_PRESSURE:
                //血压
                mTitleText.setText("血 压 检 测");
                baseBluetooth = new BloodPressurePresenter(this);
                initSearchFragment("将血压仪佩戴好后按下测量键", "测量的同时机器人会自动连接蓝牙", R.drawable.searching_bloodpressure);
                break;
            case IBleConstants.MEASURE_BLOOD_OXYGEN:
                //血氧
                mTitleText.setText("血 氧 检 测");
                baseBluetooth = new BloodOxygenPresenter(this);
                initSearchFragment("将血氧仪夹在手指上", "将血氧仪器夹在手指上，机器人会自动连接蓝牙", R.drawable.searching_bloodoxygen);
                break;
            case IBleConstants.MEASURE_WEIGHT:
                //体重
                mTitleText.setText("体 重 检 测");
                baseBluetooth = new WeightPresenter(this);
                initSearchFragment("站在体重秤上", "站上体重秤后，机器人会自动连接蓝牙", R.drawable.searching_weight);
                break;
            case IBleConstants.MEASURE_TEMPERATURE:
                //耳温
                mTitleText.setText("耳 温 检 测");
                baseBluetooth = new TemperaturePresenter(this);
                initSearchFragment("按下耳温枪START键开机，连接蓝牙", "连接成功后，将探头尽可能的伸入耳道内，按下START扫描键开始测量", R.drawable.searching_temperature);
                break;
            case IBleConstants.MEASURE_ECG:
                //心电
                mTitleText.setText("心 电 检 测");
                baseBluetooth = new ECGPresenter(this);
                initSearchFragment("将心电仪夹在两只手指中间", "将心电仪夹在两只手指中间，机器人会自动连接蓝牙", R.drawable.searching_ecg_bosheng);
                break;
            case IBleConstants.MEASURE_CHOLESTEROL:
                //胆固醇
                mTitleText.setText("胆 固 醇 检 测");
                baseBluetooth = new ThreeInOnePresenter(this);
                initSearchFragment("给三合一插上检测试纸", "插上试纸后，机器人会自动连接蓝牙", R.drawable.searching_three);
                break;
            case IBleConstants.MEASURE_URIC_ACID:
                //血尿酸
                mTitleText.setText("血 尿 酸 检 测");
                baseBluetooth = new ThreeInOnePresenter(this);
                initSearchFragment("给三合一插上检测试纸", "插上试纸后，机器人会自动连接蓝牙", R.drawable.searching_three);
                break;
            case IBleConstants.MEASURE_BLOOD_SUGAR:
                //测量血糖
                mTitleText.setText("血 糖 检 测");
                baseFragment = (BluetoothBaseFragment) Routerfit.register(AppRouter.class).getBloodsugarTimeFragmentProvider().getHealthSelectSugarDetectionTimeFragment();
                baseFragment.setOnFragmentChangedListener(this);
                getSupportFragmentManager().beginTransaction().replace(R.id.fl_container, baseFragment, "BloodsugarTime").commitAllowingStateLoss();
                break;
            case IBleConstants.SCAN_ID_CARD:
                //身份证
                mTitleText.setText("身 份 证 扫 描");
                baseBluetooth = new IDCardPresenter(this);
                ((IDCardPresenter) baseBluetooth).setCardOnReadListener(this);
                initSearchFragment("正在连接设备", "正在搜索蓝牙信号，请确保身份证阅读器已打开", R.drawable.searching_robot);
                break;
        }
    }


    private void setTimeCountDown() {
        //如果是血糖测量则10s之后再连接失败的弹窗
        RxUtils.rxCountDown(1, 10)
                .observeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new DefaultObserver<Integer>() {
                    @Override
                    public void onNext(Integer integer) {
                        Timber.i(">>>countdown" + integer);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        isTimeCountDownOver = true;
                    }
                });
    }

    private void initBloodsugarSearchFragment() {
        baseFragment = new BloodsugarSearchFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.fragment_enter, R.anim.fragment_exit, R.anim.fragment_pop_enter, R.anim.fragment_pop_exit)
                .replace(R.id.fl_container, baseFragment).commitAllowingStateLoss();
    }

    private void dealMeasureFragment() {
        switch (detectionType) {
            case IBleConstants.MEASURE_BLOOD_PRESSURE:
                //血压
                baseFragment = new BloodpressureFragment();
                break;
            case IBleConstants.MEASURE_BLOOD_OXYGEN:
                //血氧
                baseFragment = new BloodOxygenFragment();
                break;
            case IBleConstants.MEASURE_WEIGHT:
                //体重
                baseFragment = new WeightFragment();
                break;
            case IBleConstants.MEASURE_TEMPERATURE:
                //耳温
                baseFragment = new TemperatureFragment();
                break;
            case IBleConstants.MEASURE_ECG:
                //心电
                baseFragment = new ECGFragment();
                ((ECGFragment) baseFragment).setOnAnalysisDataListener(this);
                break;
            case IBleConstants.MEASURE_CHOLESTEROL:
                //胆固醇
                baseFragment = new CholesterolFragment();
                break;
            case IBleConstants.MEASURE_URIC_ACID:
                //血尿酸
                baseFragment = new UricAcidFragment();
                break;
            case IBleConstants.MEASURE_BLOOD_SUGAR:
                //血糖
                baseFragment = new BloodSugarFragment();
                if (bundle != null) {
                    baseFragment.setArguments(bundle);
                }
                break;
            case IBleConstants.SCAN_ID_CARD:
                //身份证
                baseFragment = new IDCardReadFragment();
                ((IDCardReadFragment) baseFragment).setClickPageListener(this);
                break;
        }
        if (baseFragment != null) {
            baseFragment.setOnUploadStateListener(this);
            getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(R.anim.fragment_enter, R.anim.fragment_exit, R.anim.fragment_pop_enter, R.anim.fragment_pop_exit)
                    .addToBackStack(null)
                    .replace(R.id.fl_container, baseFragment).commitAllowingStateLoss();
        }
    }

    private void initSearchFragment(String mainTitle, String subTitle, int imgRes) {

        Bundle bundle = new Bundle();
        bundle.putString("mainTitle", mainTitle);
        bundle.putString("subTitle", subTitle);
        bundle.putInt("imgRes", imgRes);
        baseFragment = new SearchAnimFragment();
        baseFragment.setArguments(bundle);
        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.fragment_enter, R.anim.fragment_exit, R.anim.fragment_pop_enter, R.anim.fragment_pop_exit)
                .replace(R.id.fl_container, baseFragment).commitAllowingStateLoss();
    }

    private void initView() {
        mTvNext = findViewById(R.id.tv_next);
        mTvNext.setEnabled(false);
        mTvNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Routerfit.setResult(Activity.RESULT_OK, dataBean);
                finish();
            }
        });
        mRightView.setImageResource(R.drawable.ic_bluetooth_disconnected);
        isSingleDetection = isSingleDetection();

//        if (!isSingleDetection) {
//            mTvNext.setVisibility(View.VISIBLE);
//            setBtnClickableState(false, null);
//        } else {
//            mTvNext.setVisibility(View.GONE);
//        }
    }

    public boolean isSingleDetection() {
        return getIntent().getBooleanExtra("isSingleDetection", false);
    }

    @Override
    protected void backMainActivity() {
        if (onShowingEcgPDF) {
            if (DataUtils.isNullString(pdfUrl)) {
                return;
            }
            Bitmap bitmap = QRCodeUtils.creatQRCode(pdfUrl, 600, 600);
            DialogImage dialogImage = new DialogImage(this);
            dialogImage.setImage(bitmap);
            dialogImage.setDescription("扫一扫，下载该报告");
            dialogImage.setCanceledOnTouchOutside(true);
            dialogImage.show();
        } else {
            showBluetoothListDialog();
        }
    }

    private void showBluetoothListDialog() {
        if (dialog == null) {
            dialog = new BluetoothListDialog();
            dialog.setConnected(connected);
            dialog.setControlBluetoothListener(this);
            dialog.show(getSupportFragmentManager());
        } else {
            dialog.setConnected(connected);
            if (!dialog.isShow()) {
                dialog.show(getSupportFragmentManager());
            }
        }
    }

    @Override
    public void updateData(DetectionData detectionData) {

    }

    @Override
    public void updateState(String state) {
        ToastUtils.showShort(state);
        MLVoiceSynthetize.startSynthesize(UM.getApp(), state);
    }

    @Override
    public void discoveryStarted() {

    }

    @Override
    public void discoveryNewDevice(BluetoothDevice device) {
        if (dialog != null) {
            dialog.addNewDevice(device);
        }
    }

    @Override
    public void discoveryFinished(boolean isConnected) {
        if (dialog != null) {
            dialog.stopDevicesListAnim();
        }

    }

    @Override
    public void unFindTargetDevice() {
        showUnSearchedDeviceDialog();
    }

    private boolean connected;

    @Override
    public void connectSuccess(BluetoothDevice device, String bluetoothName) {
        connected = true;
        mRightView.setImageResource(R.drawable.ic_bluetooth_connected);
        dealMeasureFragment();
        if (dialog != null) {
            dialog.setConnected(connected);
            dialog.showConnectedUI(device);
        }

        if (connectFailedTipsDialog != null) {
            connectFailedTipsDialog.dismiss();
        }

        if (deviceNotFoundTipsDialog != null) {
            deviceNotFoundTipsDialog.dismiss();
        }

        if (retryDialog != null) {
            retryDialog.dismiss();
        }
    }

    @Override
    public void disConnected() {
        connected = false;
        if (!(baseFragment instanceof ECG_PDF_Fragment)) {
            mRightView.setImageResource(R.drawable.ic_bluetooth_disconnected);
            showRetryConnectDialog();
        }
        if (dialog != null) {
            dialog.setConnected(connected);
            dialog.hideConnectedUI();
        }
    }

    private void popSearchFragment() {
        if (baseFragment instanceof BloodSugarFragment) return;
        if (!(baseFragment instanceof SearchAnimFragment) && !isAfterPause) {
            getSupportFragmentManager().popBackStack();
        }
    }

    @Override
    public void connectFailed() {
        connected = false;
        if (!(baseFragment instanceof ECG_PDF_Fragment)) {
            mRightView.setImageResource(R.drawable.ic_bluetooth_disconnected);
        }
        //连接失败后，提示他主动连接
        if (baseFragment instanceof BloodsugarSearchFragment && isTimeCountDownOver) {
            connectedFailedTips();
        } else if (baseFragment instanceof SearchAnimFragment) {
            connectedFailedTips();
        }

        if (dialog != null) {
            dialog.setConnected(connected);
            dialog.hideConnectedUI();
        }
    }

    @Override
    public void search() {
        if (baseBluetooth != null) {
            baseBluetooth.startDiscovery(null);
        }
    }

    @Override
    public void connect(BluetoothDevice device) {
        if (baseBluetooth != null) {
            baseBluetooth.startConnect(device, false);
        }
    }

    @Override
    public void dialogDismissed() {
        if (baseBluetooth != null) {
            baseBluetooth.stopDiscovery();
        }
    }

    private void showRetryConnectDialog() {
        baseBluetooth.onStop();

        if (retryDialog != null) {
            retryDialog.dismiss();
        }

        retryDialog = new AlertDialog(this)
                .builder()
                .setMsg("蓝牙设备已断开连接！是否进行重连？")
                .setNegativeButton("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                })
                .setPositiveButton("重新连接", ContextCompat.getColor(this, R.color.common_toolbar_bg), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //同时回到尝试搜索的页面
                        popSearchFragment();
                        search();
                    }
                });
        if (!isFinishing() && !isDestroyed()) {
            retryDialog.show();
        }
    }

    BaseNiceDialog deviceNotFoundTipsDialog = null;

    private void showUnSearchedDeviceDialog() {
        if (deviceNotFoundTipsDialog != null) {
            deviceNotFoundTipsDialog.dismiss();
        }
        deviceNotFoundTipsDialog = NiceDialog.init()
                .setLayoutId(R.layout.dialog_bluetooth_unsearched)
                .setConvertListener(new ViewConvertListener() {
                    @Override
                    protected void convertView(ViewHolder holder, BaseNiceDialog dialog) {
                        switch (detectionType) {
                            case IBleConstants.MEASURE_BLOOD_PRESSURE:
                                holder.setText(R.id.tv_msg, "未搜索到设备，\n请确认是否已打开血压计开关");
                                break;
                            case IBleConstants.MEASURE_BLOOD_OXYGEN:
                                holder.setText(R.id.tv_msg, "未搜索到设备，\n请确认是否已打开血氧仪开关");
                                break;
                            case IBleConstants.MEASURE_BLOOD_SUGAR:
                                holder.setText(R.id.tv_msg, "未搜索到设备，\n请确认是否已打开血糖仪开关");
                                break;
                            default:
                                holder.setText(R.id.tv_msg, "未搜索到设备，\n请确认是否已打开设备开关");
                                break;
                        }
                        holder.getView(R.id.btn_click).setOnClickListener(v -> {
                            dialog.dismiss();
                            //点击确定按钮后，再次进行搜索
                            search();
                        });

                    }
                })
                .setWidth(600)
                .setHeight(330)
                .show(getSupportFragmentManager());
    }

    BaseNiceDialog connectFailedTipsDialog = null;

    private void connectedFailedTips() {
        if (connectFailedTipsDialog != null) {
            connectFailedTipsDialog.dismiss();
        }
        connectFailedTipsDialog = NiceDialog.init()
                .setLayoutId(R.layout.dialog_bluetooth_unsearched)
                .setConvertListener(new ViewConvertListener() {
                    @Override
                    protected void convertView(ViewHolder holder, BaseNiceDialog dialog) {
                        holder.setText(R.id.tv_msg, "连接蓝牙失败！\n请点击右上角蓝牙图标，尝试手动连接");
                        holder.getView(R.id.btn_click).setOnClickListener(v -> {
                            dialog.dismiss();
                        });

                    }
                })
                .setWidth(600)
                .setHeight(330)
                .show(getSupportFragmentManager());
    }

    public BaseBluetooth getPresenter() {
        return baseBluetooth;
    }

    @Override
    public void onReadSuccess(IDCardItem idCardItem) {
        //身份证读取成功
        if (detectionType == IBleConstants.SCAN_ID_CARD) {
            Routerfit.setResult(Activity.RESULT_OK, idCardItem);
            finish();
        }
    }

    @Override
    public void onReadFailed() {
        //身份证读取失败
    }

    @Override
    public void onClick() {
        //点击了重新读取身份证阅读
        if (baseBluetooth != null && baseBluetooth instanceof IDCardPresenter) {
            ((IDCardPresenter) baseBluetooth).readFailed();
        }
    }

    @Override
    public void onSuccess(String fileAddress, int flag, String result, int heartRate) {
        if (!isSingleDetection()) {
            return;
        }
        pdfUrl = fileAddress;
        //心电测量结束后展示分析报告
        baseFragment = new ECG_PDF_Fragment();
        Bundle pdfBundle = new Bundle();
        pdfBundle.putString(ECG_PDF_Fragment.KEY_BUNDLE_PDF_URL, fileAddress);
        baseFragment.setArguments(pdfBundle);
        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.fragment_enter, R.anim.fragment_exit, R.anim.fragment_pop_enter, R.anim.fragment_pop_exit)
                .replace(R.id.fl_container, baseFragment).commitAllowingStateLoss();
        //右上角的图标换成二维码的，支持扫描下载报告
        onShowingEcgPDF = true;
        mRightView.setImageResource(R.drawable.health_measure_icon_qrcode);
    }

    @Override
    public void onError() {
        ToastUtils.showShort("心电报告分析出现异常");
    }

    @Override
    public void onFragmentChanged(Fragment fragment, Bundle bundle) {
        this.bundle = bundle;
        String tag = fragment.getTag();
        if (TextUtils.isEmpty(tag)) return;
        if (TextUtils.equals(tag, "BloodsugarTime")) {
            //血糖时间选择
            baseBluetooth = new BloodSugarPresenter(ConnectActivity.this);
            initBloodsugarSearchFragment();
            setTimeCountDown();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        baseFragment = null;
    }

    public void setBtnNextVisible(boolean visible) {
        mTvNext.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    private void setBtnClickableState(boolean enableClick, DetectionDataBean dataBean) {
        if (enableClick) {
            mTvNext.setClickable(true);
            mTvNext.setBackgroundResource(R.drawable.bluetooth_btn_health_history_set);
            mTvNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Routerfit.setResult(Activity.RESULT_OK, dataBean);
                    finish();
                }
            });
        } else {
            mTvNext.setBackgroundResource(R.drawable.bluetooth_btn_unclick_set);
            mTvNext.setClickable(false);
        }
    }

    @Override
    public void onSuccess(DetectionDataBean dataBean) {
        this.dataBean = dataBean;
        //流程化测量数据上传成功
        if (!isSingleDetection) {
            mTvNext.setEnabled(true);
//            setBtnClickableState(true, dataBean);
        }
    }

    @Override
    public void onError(DetectionDataBean dataBean) {
        //流程化测量数据上传失败
        if (!isSingleDetection) {
            mTvNext.setEnabled(false);
//            setBtnClickableState(true, dataBean);
        }
    }
}
