package com.example.han.referralproject.yiyuan.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.han.referralproject.R;
import com.example.han.referralproject.Test_mainActivity;
import com.example.han.referralproject.activity.MyBaseDataActivity;
import com.example.han.referralproject.application.MyApplication;
import com.example.han.referralproject.bean.DiseaseUser;
import com.example.han.referralproject.bean.UserInfo;
import com.example.han.referralproject.building_record.BuildingRecordActivity;
import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.network.NetworkManager;
import com.example.han.referralproject.olderhealthmanagement.OlderHealthManagementSerciveActivity;
import com.example.han.referralproject.recyclerview.CheckContractActivity;
import com.example.han.referralproject.recyclerview.DoctorappoActivity;
import com.example.han.referralproject.recyclerview.OnlineDoctorListActivity;
import com.example.han.referralproject.require2.HealthDetecteActivity;
import com.example.han.referralproject.require2.dialog.DialogTypeEnum;
import com.example.han.referralproject.require2.dialog.FllowUpTimesDialog;
import com.example.han.referralproject.require2.dialog.SomeCommonDialog;
import com.example.han.referralproject.require2.login.ChoiceLoginTypeActivity;
import com.example.han.referralproject.require2.register.activtiy.InputFaceActivity;
import com.example.han.referralproject.util.LocalShared;
import com.example.han.referralproject.yiyuan.activity.InquiryAndFileActivity;
import com.example.han.referralproject.yiyuan.bean.HealthDetectQualificationBean;
import com.example.han.referralproject.yiyuan.bean.MainTiZHiDialogBean;
import com.example.han.referralproject.yiyuan.bean.WenZhenReultBean;
import com.google.gson.Gson;
import com.iflytek.synthetize.MLVoiceSynthetize;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.medlink.danbogh.call2.NimAccountHelper;
import com.medlink.danbogh.healthdetection.HealthRecordActivity;
import com.medlink.danbogh.register.SignUp7HeightActivity;
import com.medlink.danbogh.utils.T;
import com.squareup.picasso.Picasso;
import com.umeng.analytics.MobclickAgent;
import com.witspring.unitbody.ChooseMemberActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.example.han.referralproject.require2.register.activtiy.InputFaceActivity.OVERHEAD_INFORMATION;

/**
 * Created by lenovo on 2018/5/17.
 */

public class Main1Fragment extends Fragment implements TiZhiJianCeDialog.DialogItemClickListener, JianKangJianCheDialog.ClickItemListener {
    private static final String JIANKANG_TIJIAN = "健康体检";
    private static final String GAOXUEYA_TIJIAN = "高血压体检";
    private static final String TANGNIAOBING_TIJIAN = "糖尿病体检";
    public static final String ZHONGYI_TIZHI = "中医体质";
    public static final String ZHENGZHUANG_ZICHA = "症状自查";
    public static final String GEREN_XINXI = "个人信息";
    public static final String CELIANG_JILU = "测量记录";
    @BindView(R.id.iv_head)
    ImageView ivHead;
    @BindView(R.id.iv_qianyue)
    ImageView ivQianyue;
    @BindView(R.id.celianglishi)
    ImageView celianglishi;
    @BindView(R.id.tijian)
    ImageView tijian;
    @BindView(R.id.zhengzhuangzichan)
    ImageView zhengzhuangzichan;
    @BindView(R.id.yishengjianyi)
    ImageView yishengjianyi;
    @BindView(R.id.jiankangketang)
    ImageView jiankangketang;
    Unbinder unbinder;
    @BindView(R.id.tv_name)
    TextView tvName;
    private TiZhiJianCeDialog dialog;
    private JianKangJianCheDialog jianCheialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main1_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);
        initData();
        return view;
    }

    private void initData() {
        NetworkApi.PersonInfo(MyApplication.getInstance().userId, new NetworkManager.SuccessCallback<UserInfo>() {
            @Override
            public void onSuccess(final UserInfo response) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Picasso.with(getActivity())
                                .load(response.getuser_photo())
                                .placeholder(R.drawable.avatar_placeholder)
                                .error(R.drawable.avatar_placeholder)
                                .tag(this)
                                .fit()
                                .into(ivQianyue);
                        tvName.setText(response.bname);

                    }
                });
            }

        }, new NetworkManager.FailedCallback() {
            @Override
            public void onFailed(String message) {


            }
        });
    }

    @OnClick({R.id.iv_head, R.id.iv_qianyue, R.id.celianglishi, R.id.tijian, R.id.zhengzhuangzichan, R.id.yishengjianyi, R.id.jiankangketang})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_head:
                //签约
                gotoQianyueYiSheng();
                break;
            case R.id.iv_qianyue:
                break;
            case R.id.celianglishi:
//                showDialog(data = getZiChaData());
                gotoTizhiJianCe();
                break;
            case R.id.tijian:
//                showJianCheDialog(getTiJianData());
                startActivity(new Intent(getActivity(), HealthDetecteActivity.class));
                break;
            case R.id.zhengzhuangzichan:
                showDialog(getJiankangDangAnData());
                break;
            case R.id.yishengjianyi:
                //问诊及建档
                showWenZhenJianDangDialog();
                break;
            case R.id.jiankangketang:
                //医生建议
                tuiChu();
                break;
        }
    }

    /**
     * 问诊建档 数据
     */
    private void showWenZhenJianDangDialog() {
        data.clear();
        MainTiZHiDialogBean bean1 = new MainTiZHiDialogBean();
        bean1.name = "问诊";
        bean1.iconId = R.drawable.main_dialog_wenzhen_set;

        MainTiZHiDialogBean bean2 = new MainTiZHiDialogBean();
        bean2.name = "建档";
        bean2.iconId = R.drawable.main_dialog_jiandang_set;

        data.add(bean1);
        data.add(bean2);


        if (dialog == null) {
            dialog = new TiZhiJianCeDialog();
        }
        dialog.setListener(this, data);
        dialog.show(getChildFragmentManager(), "dialog");

    }

    private void showJianCheDialog(List<MainTiZHiDialogBean> data) {
        if (jianCheialog == null) {
            jianCheialog = new JianKangJianCheDialog();
        }
        jianCheialog.setListener(this, data);
        jianCheialog.show(getChildFragmentManager(), "dialog");

    }

    /**
     * 健康档案 数据
     *
     * @return
     */
    private List<MainTiZHiDialogBean> getJiankangDangAnData() {
        data.clear();
        MainTiZHiDialogBean bean1 = new MainTiZHiDialogBean();
        bean1.name = GEREN_XINXI;
        bean1.iconId = R.drawable.main_dialog_gere_xinxi_set;

        MainTiZHiDialogBean bean2 = new MainTiZHiDialogBean();
        bean2.name = CELIANG_JILU;
        bean2.iconId = R.drawable.main_dialog_celiang_jilu_set;

        data.add(bean1);
        data.add(bean2);
        return data;
    }

    private void gotoWenZhenJianDang() {
        startActivity(new Intent(getActivity(), InquiryAndFileActivity.class));
    }

    /**
     * 症状自查 数据
     *
     * @return
     */
    private List<MainTiZHiDialogBean> getZiChaData() {
        data.clear();
        MainTiZHiDialogBean bean1 = new MainTiZHiDialogBean();
        bean1.name = ZHENGZHUANG_ZICHA;
        bean1.iconId = R.drawable.main_dialog_zicha_set;

        MainTiZHiDialogBean bean2 = new MainTiZHiDialogBean();
        bean2.name = ZHONGYI_TIZHI;
        bean2.iconId = R.drawable.main_dialog_tizhi_set;

        data.add(bean1);
        data.add(bean2);
        return data;
    }

    /**
     * 健康监测 数据
     *
     * @return
     */
    private List<MainTiZHiDialogBean> getTiJianData() {
        data.clear();
        MainTiZHiDialogBean bean1 = new MainTiZHiDialogBean();
        bean1.name = JIANKANG_TIJIAN;
        bean1.iconId = R.drawable.main_dialog_tijianliucheng;

        MainTiZHiDialogBean bean2 = new MainTiZHiDialogBean();
        bean2.name = GAOXUEYA_TIJIAN;
        bean2.iconId = R.drawable.main_dialog_xueya_tijian;

        MainTiZHiDialogBean bean3 = new MainTiZHiDialogBean();
        bean3.name = TANGNIAOBING_TIJIAN;
        bean3.iconId = R.drawable.main_dialog_tangniaobing;

        MainTiZHiDialogBean bean4 = new MainTiZHiDialogBean();
        bean4.name = "单项体检";
        bean4.iconId = R.drawable.main_dialog_danxiangtijian;

        data.add(bean1);
        data.add(bean2);
        data.add(bean3);
        data.add(bean4);
        return data;
    }

    private List<MainTiZHiDialogBean> data = new ArrayList<>();

    private void showDialog(List<MainTiZHiDialogBean> data) {
        if (dialog == null) {
            dialog = new TiZhiJianCeDialog();
        }
        dialog.setListener(this, data);
        dialog.show(getFragmentManager(), "dialog");

    }

    private void gotoTizhiJianCe() {
        //大于65取头像==上传头像
        NetworkApi.PersonInfo(MyApplication.getInstance().userId, new NetworkManager.SuccessCallback<UserInfo>() {
            @Override
            public void onSuccess(UserInfo response) {
                if (response != null && response.age != null) {
                    if (Integer.parseInt(response.age) >= 65) {
                        startActivity(new Intent(getActivity(), InputFaceActivity.class).putExtra(OVERHEAD_INFORMATION, "zhongYiTiZhi"));
                    }
                }
            }

        }, new NetworkManager.FailedCallback() {
            @Override
            public void onFailed(String message) {
                T.show(message);
            }
        });


        startActivity(new Intent(getActivity(), OlderHealthManagementSerciveActivity.class));
    }

    private void gotoPersonInfo() {
        startActivity(new Intent(getActivity(), MyBaseDataActivity.class));
    }


    private void gotoZhengZhuangZiCha() {
        DiseaseUser diseaseUser = new DiseaseUser(
                LocalShared.getInstance(getActivity()).getUserName(),
                LocalShared.getInstance(getActivity()).getSex().equals("男") ? 1 : 2,
                Integer.parseInt(LocalShared.getInstance(getActivity()).getUserAge()) * 12,
                LocalShared.getInstance(getActivity()).getUserPhoto()
        );
        String currentUser = new Gson().toJson(diseaseUser);
        Intent intent = new Intent(getActivity(), ChooseMemberActivity.class);
        intent.putExtra("currentUser", currentUser);
        startActivity(intent);
    }

    private void gotoCeLiangLiShi() {
        startActivity(new Intent(getActivity(), HealthRecordActivity.class));
    }

    private void gotoQianyueYiSheng() {
        NetworkApi.PersonInfo(MyApplication.getInstance().userId, new NetworkManager.SuccessCallback<UserInfo>() {
            @Override
            public void onSuccess(UserInfo response) {
                if ("1".equals(response.getState())) {
                    //已签约
                    startActivity(new Intent(getActivity(),
                            DoctorappoActivity.class));
                } else if (TextUtils.isEmpty(response.getState()) || "0".equals(response.getState())
                        && (TextUtils.isEmpty(response.getDoctername()))) {
                    //未签约
                    Intent intent = new Intent(getActivity(),
                            OnlineDoctorListActivity.class);
                    intent.putExtra("flag", "contract");
                    startActivity(intent);
                } else {
                    // 待审核
                    Intent intent = new Intent(getActivity(),
                            CheckContractActivity.class);
                    startActivity(intent);
                }
            }

        }, new NetworkManager.FailedCallback() {
            @Override
            public void onFailed(String message) {
                T.show(message);
            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    @Override
    public void onItemClick(String name) {
        if (ZHONGYI_TIZHI.equals(name)) {
            gotoTizhiJianCe();
        } else if (ZHENGZHUANG_ZICHA.equals(name)) {
            gotoZhengZhuangZiCha();
        } else if (CELIANG_JILU.equals(name)) {
            gotoCeLiangLiShi();
        } else if (GEREN_XINXI.equals(name)) {
            gotoPersonInfo();
        } else if ("问诊".equals(name)) {
            blankSomeWZInfo();
            Intent intent = new Intent(getActivity(), SignUp7HeightActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else if ("建档".equals(name)) {
            gotoFiled(name);
        }

    }

    private void gotoFiled(final String name) {
        NetworkApi.PersonInfo(MyApplication.getInstance().userId, new NetworkManager.SuccessCallback<UserInfo>() {
            @Override
            public void onSuccess(UserInfo response) {
                if (response == null) {
                    return;
                }
                String state = response.getState();
                if ("1".equals(state) || ("0".equals(state) && !TextUtils.isEmpty(response.getDoctername()))) {
                    //请求接口 判断时候建档
                    isNotFile(true, name);
                } else {
                    isNotFile(false, name);
                }

            }

        }, new NetworkManager.FailedCallback() {
            @Override
            public void onFailed(String message) {
                T.show(message);
            }
        });
    }

    private void isNotFile(final boolean isBindDoctor, final String name) {
        NetworkApi.getFiledIsOrNot(getActivity()
                , NetworkApi.FILE_URL
                , LocalShared.getInstance(getActivity()).getUserId()
                , new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        if (response == null) {
                            onGetFileStateFailed();
                            return;
                        }
                        WenZhenReultBean reultBean = new Gson().fromJson(response.body(), WenZhenReultBean.class);
                        if (reultBean.tag) {
//                            if (JIANKANG_TIJIAN.equals(name)) {
//                                //--"0" 健康体检次数
//                                JianKangJianCe("0");
//                            } else if (GAOXUEYA_TIJIAN.equals(name)) {
//                                JianKangJianCe("1");
//                            } else if (TANGNIAOBING_TIJIAN.equals(name)) {
//                                JianKangJianCe("2");
////                                showFllowUpTimesDialog("07月-08月");
//                            }

                            T.show("您已建档完毕");
                            MLVoiceSynthetize.startSynthesize(getActivity(),"您已经建档完毕",false);
                        } else {
//                            ShowToFiledDialog(isBindDoctor);
                            startActivity(new Intent(getActivity(), BuildingRecordActivity.class).putExtra("bind", isBindDoctor));
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        T.show("网络繁忙");
                    }
                });
    }

    private void JianKangJianCe(final String examinationType) {
        NetworkApi.getUseredQualification(LocalShared.getInstance(getActivity()).getUserId(), examinationType, new StringCallback() {
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
                                        startActivity(new Intent(getActivity(), InputFaceActivity.class)
                                                .putExtra("overHeadInformation", "healthDetect"));
                                        break;
                                    case "1":
                                        //可以去做健康体检
                                        startActivity(new Intent(getActivity(), InputFaceActivity.class)
                                                .putExtra("overHeadInformation", "hypertensionFollowUp"));
                                        break;
                                    case "2":
                                        //可以去做健康体检
                                        startActivity(new Intent(getActivity(), InputFaceActivity.class)
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
                                                startActivity(new Intent(getActivity(), Test_mainActivity.class));
                                            }
                                        });
                                        dialog.show(getFragmentManager(), "noHealthTime");
                                        break;
                                    case "1":
                                    case "2":
                                        showFllowUpTimesDialog(data.nextRecordDate);
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
        dialog.show(getFragmentManager(), "floowUpTimes");
    }


    private void ShowToFiledDialog(final boolean isBindDoctor) {
        SomeCommonDialog dialog = new SomeCommonDialog(DialogTypeEnum.noDocument);
        dialog.setListener(new SomeCommonDialog.OnDialogClickListener() {
            @Override
            public void onClickConfirm(DialogTypeEnum type) {
                startActivity(new Intent(getActivity(), BuildingRecordActivity.class).putExtra("bind", isBindDoctor));
            }
        });
        dialog.show(getFragmentManager(), "dialog");
    }

    /**
     * 怀孕 月经时间
     */
    private void blankSomeWZInfo() {
        LocalShared.getInstance(getActivity()).setYueJingDate("");
        LocalShared.getInstance(getActivity()).setHuaiYun("");
    }

    private void onGetFileStateFailed() {
        T.show("网络繁忙,请稍后重试~");
    }

    private void gotoDanXianTiJian() {
        startActivity(new Intent(getActivity(), Test_mainActivity.class));
    }


    @Override
    public void onJianceItemClick(String name) {
        if ("单项体检".equals(name)) {
            gotoDanXianTiJian();
        } else {
            gotoFiled(name);
        }


    }


    public void tuiChu() {
        FragmentActivity activity = getActivity();
        if (activity == null) {
            return;
        }
        MobclickAgent.onProfileSignOff();
        NimAccountHelper.getInstance().logout();
        LocalShared.getInstance(activity).loginOut();
        activity.startActivity(new Intent(activity, ChoiceLoginTypeActivity.class));
        activity.finish();
    }


}
