package com.example.han.referralproject.ecg;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.VideoView;

import com.example.han.referralproject.MainActivity;
import com.example.han.referralproject.R;
import com.example.han.referralproject.bean.DetectionData;
import com.example.han.referralproject.bean.DetectionResult;
import com.example.han.referralproject.service.API;
import com.example.han.referralproject.dialog.MyDialogFragment;
import com.gcml.lib_ecg.ChooseECGDeviceFragment;
import com.gcml.lib_ecg.ECG_Fragment;
import com.gcml.lib_ecg.ECG_PDF_Fragment;
import com.gcml.lib_ecg.SelfECGDetectionFragment;
import com.gcml.lib_ecg.base.BluetoothBaseFragment;
import com.gcml.module_health_record.HealthRecordActivity;
import com.gzq.lib_core.bean.UserInfoBean;
import com.gzq.lib_core.http.exception.ApiException;
import com.gzq.lib_core.http.observer.CommonObserver;
import com.gzq.lib_core.utils.DataUtils;
import com.gcml.lib_ecg.base.DealVoiceAndJump;
import com.gcml.lib_ecg.base.FragmentChanged;
import com.gcml.lib_ecg.ecg.Bluetooth_Constants;
import com.gzq.lib_core.base.Box;
import com.gzq.lib_core.utils.RxUtils;
import com.gzq.lib_core.utils.SPUtil;
import com.gzq.lib_core.utils.ToastUtils;
import com.iflytek.synthetize.MLVoiceSynthetize;

import java.util.ArrayList;
import java.util.List;

public class ECGCompatActivity extends ToolbarBaseActivity implements FragmentChanged, DealVoiceAndJump {
    private BluetoothBaseFragment baseFragment;
    private String pdfUrl;
    private VideoView mVideo;
    private AlertDialog dialog;

    private boolean isMeasure = true;
    private FrameLayout mViewOver;
    private boolean isChooseDevice = false;


    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, ECGCompatActivity.class));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contain);
        initView();
        playVideo();

    }

    private void playVideo() {
        mToolbar.setVisibility(View.GONE);
        mTitleText.setText("心 电 测 量");
        mVideo.setVisibility(View.VISIBLE);
        mViewOver.setVisibility(View.VISIBLE);
        mVideo.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.tips_xindian));
        mVideo.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();
            }
        });
        mVideo.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mVideo.setVisibility(View.GONE);
                mViewOver.setVisibility(View.GONE);
                mToolbar.setVisibility(View.VISIBLE);
                mTitleText.setText("心 电 测 量");
                int device = (int) SPUtil.get(Bluetooth_Constants.SP.SP_SAVE_DEVICE_ECG, 0);
                if (device == 0) {
                    showChooseDeviceFragment();
                } else {
                    showECGFragment(device);
                }

            }
        });
    }

    private void showChooseDeviceFragment() {
        isChooseDevice = true;
        mTitleText.setText("心 电 设 备 选 择");
        mRightView.setImageResource(R.drawable.common_icon_home);
        baseFragment = new ChooseECGDeviceFragment();
        baseFragment.setOnFragmentChangedListener(this);
        getSupportFragmentManager().beginTransaction().replace(R.id.frame, baseFragment).commitAllowingStateLoss();
    }

    private void showECGFragment(int device) {
        isChooseDevice = false;
        if (device == 1) {
            baseFragment = new SelfFragment();
        } else if (device == 2) {
            baseFragment = new BoShengFragment();
        }

        mTitleText.setText("心 电 测 量");
        mRightView.setImageResource(R.drawable.health_measure_ic_bluetooth_disconnected);

        baseFragment.setOnDealVoiceAndJumpListener(this);
        baseFragment.setOnFragmentChangedListener(this);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame, baseFragment).commit();
        setECGListener();
    }

    @Override
    public void onFragmentChanged(Fragment fragment, Bundle bundle) {
        if (fragment instanceof ChooseECGDeviceFragment) {
            if (bundle != null) {
                int bundleInt = bundle.getInt(Bluetooth_Constants.SP.SP_SAVE_DEVICE_ECG, 1);
                showECGFragment(bundleInt);
            }
        } else if (fragment instanceof ECG_Fragment || fragment instanceof SelfECGDetectionFragment) {
            //先清除本地的缓存
            SPUtil.remove(Bluetooth_Constants.SP.SP_SAVE_DEVICE_ECG);
            showChooseDeviceFragment();
        }
    }

    private void setECGListener() {
        if (baseFragment != null && baseFragment instanceof BoShengFragment) {
            ((BoShengFragment) baseFragment).setOnAnalysisDataListener(new BoShengFragment.AnalysisData() {
                @Override
                public void onSuccess(String fileNum, String result, String filePDF) {
                    isMeasure = false;
                    pdfUrl = filePDF;

                    //todo 上传数据
                    BoShengResultBean boShengResultBean = Box.getGson().fromJson(result, BoShengResultBean.class);

                    ArrayList<DetectionData> datas = new ArrayList<>();
                    DetectionData ecgData = new DetectionData();
                    //detectionType (string, optional): 检测数据类型 0血压 1血糖 2心电 3体重 4体温 6血氧 7胆固醇 8血尿酸 9脉搏 ,
                    ecgData.setDetectionType("2");
                    ecgData.setEcg(boShengResultBean.getStop_light() == 2 ? "1" : boShengResultBean.getStop_light() + "");
                    ecgData.setResult(boShengResultBean.getFindings());
                    ecgData.setHeartRate(boShengResultBean.getAvgbeats().get(0).getHR());
                    ecgData.setResultUrl(filePDF);
                    datas.add(ecgData);
                    String userId = ((UserInfoBean) Box.getSessionManager().getUser()).bid;
                    Box.getRetrofit(API.class)
                            .postMeasureData(userId, datas)
                            .compose(RxUtils.httpResponseTransformer())
                            .as(RxUtils.autoDisposeConverter(ECGCompatActivity.this))
                            .subscribe(new CommonObserver<List<DetectionResult>>() {
                                @Override
                                public void onNext(List<DetectionResult> detectionResults) {
                                    ToastUtils.showShort("上传数据成功");
                                }

                                @Override
                                protected void onError(ApiException ex) {
                                    super.onError(ex);
                                    ToastUtils.showShort(ex.message);
                                }
                            });

                    ECG_PDF_Fragment pdf_fragment = new ECG_PDF_Fragment();
                    Bundle pdfBundle = new Bundle();
                    pdfBundle.putString(ECG_PDF_Fragment.KEY_BUNDLE_PDF_URL, filePDF);
                    pdf_fragment.setArguments(pdfBundle);
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame, pdf_fragment).commit();
                    mRightView.setImageResource(R.drawable.health_measure_icon_qrcode);
                }

                @Override
                public void onError() {

                }
            });
        }
    }

    @Override
    public void updateVoice(String voice) {
        ToastUtils.showShort(voice);
        MLVoiceSynthetize.startSynthesize( voice);
        String connect = getString(R.string.bluetooth_device_connected);
        String disconnect = getString(R.string.bluetooth_device_disconnected);
        if (TextUtils.equals(voice, connect)) {
            mRightView.setImageResource(R.drawable.health_measure_ic_bluetooth_connected);
        }
        if (TextUtils.equals(voice, disconnect)) {
            mRightView.setImageResource(R.drawable.health_measure_ic_bluetooth_disconnected);
        }
    }

    @Override
    public void jump2HealthHistory(int measureType) {
        Intent intent = new Intent(this, HealthRecordActivity.class);
        intent.putExtra("position", 7);
        startActivity(intent);
    }

    @Override
    public void jump2DemoVideo(int measureType) {
        playVideo();
    }

    private void initView() {
        mVideo = (VideoView) findViewById(R.id.video);
        mViewOver = (FrameLayout) findViewById(R.id.view_over);
        mViewOver.setOnClickListener(this);
    }

    @Override
    protected void backMainActivity() {
        super.backMainActivity();
        if (isChooseDevice) {
            startActivity(new Intent(this, MainActivity.class));
            return;
        }
        if (isMeasure) {
            showDialog();
        } else {
            if (DataUtils.isNullString(pdfUrl)) {
                return;
            }
            MyDialogFragment.newInstance(pdfUrl).show(getSupportFragmentManager(), "ECGPDF");
        }
    }

    private void showDialog() {
        if (dialog == null) {
            dialog = new AlertDialog.Builder(this).setMessage("是否匹配新设备").setNegativeButton("取消", null).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    SPUtil.remove(Bluetooth_Constants.SP.SP_SAVE_ECG);

                    if (baseFragment instanceof BoShengFragment) {
                        ((ECG_Fragment) baseFragment).onStop();
                        ((ECG_Fragment) baseFragment).dealLogic();
                    } else if (baseFragment instanceof SelfFragment) {
                        ((SelfECGDetectionFragment) baseFragment).startDiscovery();
                    }
                }
            }).create();
        }
        if (!dialog.isShowing()) {
            dialog.show();
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.view_over:
                mVideo.setVisibility(View.GONE);
                mViewOver.setVisibility(View.GONE);
                if (mVideo.isPlaying()) {
                    mVideo.pause();
                }
                mToolbar.setVisibility(View.VISIBLE);
                mTitleText.setText("心 电 测 量");
                int device = (int) SPUtil.get(Bluetooth_Constants.SP.SP_SAVE_DEVICE_ECG, 0);
                if (device == 0) {
                    showChooseDeviceFragment();
                } else {
                    showECGFragment(device);
                }
                break;
            default:
                break;
        }
    }
}
