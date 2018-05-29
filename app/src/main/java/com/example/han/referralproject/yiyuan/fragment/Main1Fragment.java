package com.example.han.referralproject.yiyuan.fragment;

import android.app.Application;
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

import com.example.han.referralproject.MainActivity;
import com.example.han.referralproject.R;
import com.example.han.referralproject.Test_mainActivity;
import com.example.han.referralproject.activity.DetectActivity;
import com.example.han.referralproject.activity.MessageActivity;
import com.example.han.referralproject.activity.MyBaseDataActivity;
import com.example.han.referralproject.application.MyApplication;
import com.example.han.referralproject.bean.DiseaseUser;
import com.example.han.referralproject.bean.UserInfo;
import com.example.han.referralproject.building_record.BuildingRecordActivity;
import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.network.NetworkManager;
import com.example.han.referralproject.questionair.activity.ChineseMedicineMonitorActivity;
import com.example.han.referralproject.recyclerview.CheckContractActivity;
import com.example.han.referralproject.recyclerview.DoctorappoActivity;
import com.example.han.referralproject.recyclerview.OnlineDoctorListActivity;
import com.example.han.referralproject.util.LocalShared;
import com.example.han.referralproject.video.VideoListActivity;
import com.example.han.referralproject.yiyuan.activity.InquiryAndFileActivity;
import com.example.han.referralproject.yiyuan.activity.YiYuanLoginActivity;
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
                showDialog(data = getZiChaData());
                break;
            case R.id.tijian:
                showJianCheDialog(getTiJianData());
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
        startActivity(new Intent(getActivity(), ChineseMedicineMonitorActivity.class));
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

    private void gotoYiShengJianYi() {
        startActivity(new Intent(getActivity(), MessageActivity.class));
    }

    private void gotoCeLiangLiShi() {
        startActivity(new Intent(getActivity(), HealthRecordActivity.class));
    }

    private void gotoJianKangJiangTang() {
        startActivity(new Intent(getActivity(), VideoListActivity.class));
    }

    private void gotoQianyueYiSheng() {
        NetworkApi.PersonInfo(MyApplication.getInstance().userId, new NetworkManager.SuccessCallback<UserInfo>() {
            @Override
            public void onSuccess(UserInfo response) {
                if ("1".equals(response.getState())) {
                    //已签约
                    startActivity(new Intent(getActivity(),
                            DoctorappoActivity.class));
                } else if ("0".equals(response.getState())
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
            Intent intent = new Intent(getActivity(), SignUp7HeightActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else if ("建档".equals(name)) {
            //请求接口 判断时候建档
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
                                T.show("您已建档完毕");
                                MLVoiceSynthetize.startSynthesize(getContext(),"您已建档完毕",false);
                            } else {
                                startActivity(new Intent(getActivity(), BuildingRecordActivity.class));
                            }
                        }
                    });

        }

    }

    private void onGetFileStateFailed() {
        T.show("网络繁忙,请稍后重试~");
    }

    private void gotoDanXianTiJian() {
        startActivity(new Intent(getActivity(), Test_mainActivity.class));
    }


    @Override
    public void onJianceItemClick(String name) {
        if (JIANKANG_TIJIAN.equals(name)) {
//            gotoDanXianTiJian();
            Intent intent = new Intent(getActivity(), DetectActivity.class);
            intent.putExtra("type", "wendu");
            intent.putExtra("isDetect", true);
            intent.putExtra("detectCategory", "detectHealth");
            startActivity(intent);

        } else if (GAOXUEYA_TIJIAN.equals(name)) {
            Intent intent = new Intent(getActivity(), DetectActivity.class);
            intent.putExtra("type", "xueya");
            intent.putExtra("isDetect", true);
            intent.putExtra("detectCategory", "detectPressure");
            startActivity(intent);
        } else if (TANGNIAOBING_TIJIAN.equals(name)) {
            Intent intent = new Intent(getActivity(), DetectActivity.class);
            intent.putExtra("type", "xueya");
            intent.putExtra("isDetect", true);
            intent.putExtra("detectCategory", "detectSugar");
            startActivity(intent);
        } else if ("单项体检".equals(name)) {
            gotoDanXianTiJian();
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
        activity.startActivity(new Intent(activity, YiYuanLoginActivity.class));
        activity.finish();
    }
}
