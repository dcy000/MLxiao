package com.example.han.referralproject.require2;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.Test_mainActivity;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.activity.DetectActivity;
import com.example.han.referralproject.application.MyApplication;
import com.example.han.referralproject.bean.UserInfo;
import com.example.han.referralproject.building_record.BuildingRecordActivity;
import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.network.NetworkManager;
import com.example.han.referralproject.require2.dialog.DialogTypeEnum;
import com.example.han.referralproject.require2.dialog.FllowUpTimesDialog;
import com.example.han.referralproject.require2.dialog.SomeCommonDialog;
import com.example.han.referralproject.require2.register.activtiy.InputFaceActivity;
import com.example.han.referralproject.require4.bean.FllowUpResponseBean;
import com.example.han.referralproject.util.LocalShared;
import com.example.han.referralproject.util.Utils;
import com.example.han.referralproject.yiyuan.bean.HealthDetectQualificationBean;
import com.example.han.referralproject.yiyuan.bean.WenZhenReultBean;
import com.google.gson.Gson;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.medlink.danbogh.utils.T;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HealthDetecteActivity extends BaseActivity {

    @BindView(R.id.im_pay_item)
    ImageView imPayItem;
    @BindView(R.id.im_pressure_fllow_up)
    ImageView imPressureFllowUp;
    @BindView(R.id.im_health_detecte)
    ImageView imHealthDetecte;
    @BindView(R.id.im_sugar_fllow_up)
    ImageView imSugarFllowUp;
    @BindView(R.id.textView21)
    TextView textView21;
    @BindView(R.id.textView22)
    TextView textView22;
    @BindView(R.id.health_times)
    TextView healthTimes;
    @BindView(R.id.pressrue_times)
    TextView pressrueTimes;
    @BindView(R.id.sugar_times)
    TextView sugarTimes;
    @BindView(R.id.sugar_detection_time)
    TextView sugarDetectionTime;
    @BindView(R.id.health_detection_time)
    TextView healthDetectionTime;
    @BindView(R.id.pressure_deteion_time)
    TextView pressureDeteionTime;
    private WenZhenReultBean reultBean = new WenZhenReultBean();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_detecte);
        ButterKnife.bind(this);
        initTitle();
        gotoBindInfo();
    }

    boolean bind = false;

    private void gotoBindInfo() {
//        showLoadingDialog("");
        NetworkApi.PersonInfo(MyApplication.getInstance().userId, new NetworkManager.SuccessCallback<UserInfo>() {
            @Override
            public void onSuccess(UserInfo response) {
//                hideLoadingDialog();
                if (response == null) {
                    return;
                }
                String state = response.getState();
                if ("1".equals(state) || ("0".equals(state) && !TextUtils.isEmpty(response.getDoctername()))) {
                    //请求接口 判断时候建档
                    bind = true;
                } else {
                    bind = false;
                }
                getFileInfo();
            }
        }, new NetworkManager.FailedCallback() {
            @Override
            public void onFailed(String message) {
                T.show(message);
//                hideLoadingDialog();
            }
        });
    }

    private void initTitle() {
        mToolbar.setVisibility(View.VISIBLE);
        mTitleText.setText("健 康 检 测");
    }

    @Override
    protected void onResume() {
        super.onResume();
        getFllowUpInfo();
        //关闭唤醒
        setDisableGlobalListen(true);
        //关闭语音实时识别
        setEnableListeningLoop(false);
    }

    @OnClick({R.id.im_pay_item, R.id.im_pressure_fllow_up, R.id.im_health_detecte, R.id.im_sugar_fllow_up})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.im_pay_item:
                gotoDanXianTiJian();
                break;
            default:
                jump(view.getId());
                break;

        }
    }

    private void gotoDanXianTiJian() {
        startActivity(new Intent(this, Test_mainActivity.class));
    }

    private void gotoFiled(final int id) {
        NetworkApi.PersonInfo(MyApplication.getInstance().userId, new NetworkManager.SuccessCallback<UserInfo>() {
            @Override
            public void onSuccess(UserInfo response) {
                if (response == null) {
                    return;
                }
                String state = response.getState();
                if ("1".equals(state) || ("0".equals(state) && !TextUtils.isEmpty(response.getDoctername()))) {
                    //请求接口 判断时候建档
//                    isNotFile(true, id);
                    getFileInfo();
                } else {
//                    isNotFile(false, id);
                    getFileInfo();
                }

            }

        }, new NetworkManager.FailedCallback() {
            @Override
            public void onFailed(String message) {
                T.show(message);
            }
        });
    }

    private void jump(final int id) {
        if (reultBean.tag) {
            switch (id) {
                case R.id.im_health_detecte:
                    //健康体检
//                                    JianKangJianCe("0");
                    Intent intent3 = new Intent(HealthDetecteActivity.this, DetectActivity.class);
                    intent3.putExtra("type", "wendu");
                    intent3.putExtra("isDetect", true);
                    intent3.putExtra("detectCategory", "detectHealth");
                    startActivity(intent3);
                    break;
                case R.id.im_pressure_fllow_up:
                    //血压随访
//                                    JianKangJianCe("1");
                    Intent intent = new Intent(HealthDetecteActivity.this, DetectActivity.class);
                    intent.putExtra("type", "xueya");
                    intent.putExtra("isDetect", true);
                    intent.putExtra("detectCategory", "detectPressure");
                    startActivity(intent);
                    break;
                //血糖随访
                case R.id.im_sugar_fllow_up:
//                                    JianKangJianCe("2");
                    Intent intent1 = new Intent(HealthDetecteActivity.this, DetectActivity.class);
                    intent1.putExtra("type", "xueya");
                    intent1.putExtra("isDetect", true);
                    intent1.putExtra("detectCategory", "detectSugar");
                    startActivity(intent1);
                    break;

            }
        } else {
            ShowToFiledDialog(bind, id);
        }

    }

    private void getFileInfo() {
        showLoadingDialog("");
        NetworkApi.getFiledIsOrNot(this
                , NetworkApi.FILE_URL
                , LocalShared.getInstance(this).getUserId()
                , new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        hideLoadingDialog();
                        if (response == null) {
                            return;
                        }
                        reultBean = new Gson().fromJson(response.body(), WenZhenReultBean.class);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        hideLoadingDialog();
                        T.show("网络繁忙");
                    }
                });

    }

    private void ShowToFiledDialog(final boolean isBindDoctor, final int id) {
        try {
            SomeCommonDialog dialog = null;
            switch (id) {
                case R.id.im_health_detecte:
                    dialog = new SomeCommonDialog(DialogTypeEnum.noDocument);
                    break;
                case R.id.im_pressure_fllow_up:
                    dialog = new SomeCommonDialog(DialogTypeEnum.noDocumentForPresure);
                    break;
                case R.id.im_sugar_fllow_up:
                    dialog = new SomeCommonDialog(DialogTypeEnum.noDocumentForPresure);
                    break;

            }
            dialog.setListener(new SomeCommonDialog.OnDialogClickListener() {
                @Override
                public void onClickConfirm(DialogTypeEnum type) {
                    startActivity(new Intent(HealthDetecteActivity.this, BuildingRecordActivity.class).putExtra("bind", isBindDoctor));
                }
            });
            dialog.show(getSupportFragmentManager(), "dialog");
        } catch (Exception e) {
        }
    }

    private void JianKangJianCe(final String examinationType) {
        NetworkApi.getUseredQualification(LocalShared.getInstance(this).getUserId(), examinationType, new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                if (response == null) {
                    return;
                }
                String body = response.body();
                HealthDetectQualificationBean bean = new Gson().fromJson(body, HealthDetectQualificationBean.class);
                if (bean != null) {
                    if (bean.tag) {
                        if (bean.data != null) {
                            HealthDetectQualificationBean.DataBean data = bean.data;
                            if (data.qualification) {
                                switch (examinationType) {
                                    case "0":
                                        //可以去做健康体检
                                        startActivity(new Intent(HealthDetecteActivity.this, InputFaceActivity.class)
                                                .putExtra("overHeadInformation", "healthDetect"));
                                        break;
                                    case "1":
                                        //可以去做健康体检
                                        startActivity(new Intent(HealthDetecteActivity.this, InputFaceActivity.class)
                                                .putExtra("overHeadInformation", "hypertensionFollowUp"));
                                        break;
                                    case "2":
                                        //可以去做健康体检
                                        startActivity(new Intent(HealthDetecteActivity.this, InputFaceActivity.class)
                                                .putExtra("overHeadInformation", "hyperglycemiaFollowUp"));
                                        break;
                                }


                            } else {
                                switch (examinationType) {
                                    case "0":
                                        SomeCommonDialog dialog = new SomeCommonDialog(DialogTypeEnum.noHealtCheckTime);
                                        dialog.setListener(new SomeCommonDialog.OnDialogClickListener() {
                                            @Override
                                            public void onClickConfirm(DialogTypeEnum type) {
                                                startActivity(new Intent(HealthDetecteActivity.this, Test_mainActivity.class));
                                            }
                                        });
                                        dialog.show(getSupportFragmentManager(), "noHealthTime");
                                        break;
                                    case "1":
                                    case "2":
                                        if (data != null && data.yearFreeState) {
                                            SomeCommonDialog dialog1 = new SomeCommonDialog(DialogTypeEnum.noSuiFangTime);
                                            dialog1.setListener(new SomeCommonDialog.OnDialogClickListener() {
                                                @Override
                                                public void onClickConfirm(DialogTypeEnum type) {
                                                    startActivity(new Intent(HealthDetecteActivity.this, Test_mainActivity.class));
                                                }
                                            });
                                            dialog1.show(getSupportFragmentManager(), "noFllowUpTime");

                                        } else {

                                            showFllowUpTimesDialog(data.nextRecordDate);
                                        }
                                        break;
                                }
                            }
                        }

                    }
                }

            }
        });
    }

    private void showFllowUpTimesDialog(String timeDecription) {
        FllowUpTimesDialog dialog = new FllowUpTimesDialog(timeDecription);
        dialog.show(getSupportFragmentManager(), "floowUpTimes");
    }
    public void getFllowUpInfo() {
//        showFllowUpTimesDialog("");
//        showLoadingDialog("");
        NetworkApi.getFllowUpInfo(MyApplication.getInstance().userId, new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                try {
                    String body = response.body();
                    FllowUpResponseBean fllowUpResponseBean = new Gson().fromJson(body, FllowUpResponseBean.class);
                    if (fllowUpResponseBean == null) {
                        return;
                    }

                    if (fllowUpResponseBean.tag) {
                        if (fllowUpResponseBean.data == null) {
                            return;
                        }
//
                        if (fllowUpResponseBean.data.examinationDate != null) {
                            healthDetectionTime.setText(Utils.stampTodate(fllowUpResponseBean.data.examinationDate));
                        }

                        if (fllowUpResponseBean.data.hypertensionDate != null) {
                            pressureDeteionTime.setText(Utils.stampTodate(fllowUpResponseBean.data.hypertensionDate));
                        }

                        if (fllowUpResponseBean.data.diabetesDate == null) {
                            sugarDetectionTime.setText(Utils.stampTodate(fllowUpResponseBean.data.diabetesDate));
                        }

                        if (fllowUpResponseBean.data.examinationNum == null) {
                            healthTimes.setText("已使用0次");
                        } else {
                            healthTimes.setText(String.format("已使用%d次", fllowUpResponseBean.data.examinationNum));
                        }

                        if (fllowUpResponseBean.data.hypertensionNum == null) {
                            pressrueTimes.setText("已使用0次");
                        } else {
                            pressrueTimes.setText(String.format("已使用%d次", fllowUpResponseBean.data.hypertensionNum));
                        }

                        if (fllowUpResponseBean.data.diabetesNum == null) {
                            sugarTimes.setText("已使用0次");
                        } else {
                            sugarTimes.setText(String.format("已使用%d次", fllowUpResponseBean.data.diabetesNum));
                        }
                    }
                } catch (Exception e) {

                }
            }

            @Override
            public void onError(Response<String> response) {
                super.onError(response);

            }

            @Override
            public void onFinish() {
                super.onFinish();
//                hideLoadingDialog();
            }
        });
    }
}
