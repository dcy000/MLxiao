package com.example.han.referralproject.building_record;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.han.referralproject.R;

import java.util.ArrayList;
import java.util.List;

public class EnvironmentFragment extends Fragment implements View.OnClickListener {
    private View view;
    private IFragmentChange iFragmentChange;
    private TextView youyanji;
    private TextView huanqishan;
    private TextView yanchong;
    private TextView paiqiWu;
    private TextView yehuaqi;
    private TextView mei;
    private TextView tianranqi;
    private TextView zhaoqi;
    private TextView chaihuo;
    private TextView ranliaoWu;
    private TextView zilaishui;
    private TextView jingshui;
    private TextView hupanshui;
    private TextView tangshui;
    private TextView huolvshui;
    private TextView yinshuiQita;
    private TextView weishengcesuo;
    private TextView matong;
    private TextView lutiankeng;
    private TextView jianyipengce;
    private TextView fenchishi;
    private TextView cesuoQita;
    private TextView danshe;
    private TextView shinei;
    private TextView shiwai;
    private TextView jiaqinWu;
    private TextView tvSignUpGoBack;
    private TextView tvSignUpGoForward;
    private String paiqi_result;
    private String ranliao_result;
    private String yinshui_result;
    private String cesuo_result;
    private String qinchu_result;

    public void setOnFragmentChange(IFragmentChange iFragmentChange) {
        this.iFragmentChange = iFragmentChange;
    }
    private boolean[] switch_paiqi=new boolean[4];
    private String[] paiqi=new String[]{"油烟机","换气扇","烟囱","无"};

    private boolean[] switch_ranliao=new boolean[6];
    private String[] ranliao=new String[]{"液化气","煤","天然气","沼气","柴火","其他"};

    private boolean[] switch_yinshui=new boolean[6];
    private String[] yinshui=new String[]{"自来水","井水","河湖水","塘水","经净化过滤的水","其他"};

    private boolean[] switch_cesuo=new boolean[6];
    private String[] cesuo=new String[]{"卫生厕所","马桶","露天粪坑","简易棚厕","一格或二格粪池式","其他"};

    private boolean[] switch_qinchu=new boolean[4];
    private String[] qinchu=new String[]{"单设","室内","室外","无"};
    private List<TextView> tv_paiqi;
    private List<TextView> tv_ranliao;
    private List<TextView> tv_yinshui;
    private List<TextView> tv_cesuo;
    private List<TextView> tv_qinchu;
    private String[] index1=new String[]{"2","3","4","1"};
    private String[] index2=new String[]{"1","2","3","4","5","6"};
    private String[] index3=new String[]{"1","3","4","5","2","6"};
    private String[] index4=new String[]{"1","3","4","5","2","6"};
    private String[] index5=new String[]{"2","3","4","1"};
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_environment, container, false);
            initView(view);
        }
        return view;
    }


    private void initView(View view) {
        youyanji = (TextView) view.findViewById(R.id.youyanji);
        youyanji.setOnClickListener(this);
        huanqishan = (TextView) view.findViewById(R.id.huanqishan);
        huanqishan.setOnClickListener(this);
        yanchong = (TextView) view.findViewById(R.id.yanchong);
        yanchong.setOnClickListener(this);
        paiqiWu = (TextView) view.findViewById(R.id.paiqi_wu);
        paiqiWu.setOnClickListener(this);
        tv_paiqi=new ArrayList<>();
        tv_paiqi.add(youyanji);
        tv_paiqi.add(huanqishan);
        tv_paiqi.add(yanchong);
        tv_paiqi.add(paiqiWu);

        yehuaqi = (TextView) view.findViewById(R.id.yehuaqi);
        yehuaqi.setOnClickListener(this);
        mei = (TextView) view.findViewById(R.id.mei);
        mei.setOnClickListener(this);
        tianranqi = (TextView) view.findViewById(R.id.tianranqi);
        tianranqi.setOnClickListener(this);
        zhaoqi = (TextView) view.findViewById(R.id.zhaoqi);
        zhaoqi.setOnClickListener(this);
        chaihuo = (TextView) view.findViewById(R.id.chaihuo);
        chaihuo.setOnClickListener(this);
        ranliaoWu = (TextView) view.findViewById(R.id.ranliao_wu);
        ranliaoWu.setOnClickListener(this);
        tv_ranliao=new ArrayList<>();
        tv_ranliao.add(yehuaqi);
        tv_ranliao.add(mei);
        tv_ranliao.add(tianranqi);
        tv_ranliao.add(zhaoqi);
        tv_ranliao.add(chaihuo);
        tv_ranliao.add(ranliaoWu);

        zilaishui = (TextView) view.findViewById(R.id.zilaishui);
        zilaishui.setOnClickListener(this);
        jingshui = (TextView) view.findViewById(R.id.jingshui);
        jingshui.setOnClickListener(this);
        hupanshui = (TextView) view.findViewById(R.id.hupanshui);
        hupanshui.setOnClickListener(this);
        tangshui = (TextView) view.findViewById(R.id.tangshui);
        tangshui.setOnClickListener(this);
        huolvshui = (TextView) view.findViewById(R.id.huolvshui);
        huolvshui.setOnClickListener(this);
        yinshuiQita = (TextView) view.findViewById(R.id.yinshui_qita);
        yinshuiQita.setOnClickListener(this);
        tv_yinshui=new ArrayList<>();
        tv_yinshui.add(zilaishui);
        tv_yinshui.add(jingshui);
        tv_yinshui.add(hupanshui);
        tv_yinshui.add(tangshui);
        tv_yinshui.add(huolvshui);
        tv_yinshui.add(yinshuiQita);


        weishengcesuo = (TextView) view.findViewById(R.id.weishengcesuo);
        weishengcesuo.setOnClickListener(this);
        matong = (TextView) view.findViewById(R.id.matong);
        matong.setOnClickListener(this);
        lutiankeng = (TextView) view.findViewById(R.id.lutiankeng);
        lutiankeng.setOnClickListener(this);
        jianyipengce = (TextView) view.findViewById(R.id.jianyipengce);
        jianyipengce.setOnClickListener(this);
        fenchishi = (TextView) view.findViewById(R.id.fenchishi);
        fenchishi.setOnClickListener(this);
        cesuoQita = (TextView) view.findViewById(R.id.cesuo_qita);
        cesuoQita.setOnClickListener(this);
        tv_cesuo=new ArrayList<>();
        tv_cesuo.add(weishengcesuo);
        tv_cesuo.add(matong);
        tv_cesuo.add(lutiankeng);
        tv_cesuo.add(jianyipengce);
        tv_cesuo.add(fenchishi);
        tv_cesuo.add(cesuoQita);

        danshe = (TextView) view.findViewById(R.id.danshe);
        danshe.setOnClickListener(this);
        shinei = (TextView) view.findViewById(R.id.shinei);
        shinei.setOnClickListener(this);
        shiwai = (TextView) view.findViewById(R.id.shiwai);
        shiwai.setOnClickListener(this);
        jiaqinWu = (TextView) view.findViewById(R.id.jiaqin_wu);
        jiaqinWu.setOnClickListener(this);
        tvSignUpGoBack = (TextView) view.findViewById(R.id.tv_sign_up_go_back);
        tvSignUpGoBack.setOnClickListener(this);
        tvSignUpGoForward = (TextView) view.findViewById(R.id.tv_sign_up_go_forward);
        tvSignUpGoForward.setOnClickListener(this);
        tv_qinchu=new ArrayList<>();
        tv_qinchu.add(danshe);
        tv_qinchu.add(shinei);
        tv_qinchu.add(shiwai);
        tv_qinchu.add(jiaqinWu);

    }

    @Override
    public void onResume() {
        super.onResume();
        ((BuildingRecordActivity) getActivity()). setDisableGlobalListen(true);
        ((BuildingRecordActivity) getActivity()).speak("主人,请输入您生活环境的相关情况");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.youyanji:
                paiqi_result = MyArraysUtils.resetSwitch(switch_paiqi, paiqi, tv_paiqi, 0);
                break;
            case R.id.huanqishan:
                paiqi_result = MyArraysUtils.resetSwitch(switch_paiqi, paiqi, tv_paiqi, 1);
                break;
            case R.id.yanchong:
                paiqi_result = MyArraysUtils.resetSwitch(switch_paiqi, paiqi, tv_paiqi, 2);
                break;
            case R.id.paiqi_wu:
                paiqi_result = MyArraysUtils.resetSwitch(switch_paiqi, paiqi, tv_paiqi, 3);
                break;
            case R.id.yehuaqi:
                ranliao_result = MyArraysUtils.resetSwitch(switch_ranliao, ranliao, tv_ranliao, 0);
                break;
            case R.id.mei:
                ranliao_result = MyArraysUtils.resetSwitch(switch_ranliao, ranliao, tv_ranliao, 1);
                break;
            case R.id.tianranqi:
                ranliao_result = MyArraysUtils.resetSwitch(switch_ranliao, ranliao, tv_ranliao, 2);
                break;
            case R.id.zhaoqi:
                ranliao_result = MyArraysUtils.resetSwitch(switch_ranliao, ranliao, tv_ranliao, 3);
                break;
            case R.id.chaihuo:
                ranliao_result = MyArraysUtils.resetSwitch(switch_ranliao, ranliao, tv_ranliao, 4);
                break;
            case R.id.ranliao_wu:
                ranliao_result = MyArraysUtils.resetSwitch(switch_ranliao, ranliao, tv_ranliao, 5);
                break;
            case R.id.zilaishui:
                yinshui_result = MyArraysUtils.resetSwitch(switch_yinshui, yinshui, tv_yinshui, 0);
                break;
            case R.id.jingshui:
                yinshui_result = MyArraysUtils.resetSwitch(switch_yinshui, yinshui, tv_yinshui, 1);
                break;
            case R.id.hupanshui:
                yinshui_result = MyArraysUtils.resetSwitch(switch_yinshui, yinshui, tv_yinshui, 2);
                break;
            case R.id.tangshui:
                yinshui_result = MyArraysUtils.resetSwitch(switch_yinshui, yinshui, tv_yinshui, 3);
                break;
            case R.id.huolvshui:
                yinshui_result = MyArraysUtils.resetSwitch(switch_yinshui, yinshui, tv_yinshui, 4);
                break;
            case R.id.yinshui_qita:
                yinshui_result = MyArraysUtils.resetSwitch(switch_yinshui, yinshui, tv_yinshui, 5);
                break;
            case R.id.weishengcesuo:
                cesuo_result = MyArraysUtils.resetSwitch(switch_cesuo, cesuo, tv_cesuo, 0);
                break;
            case R.id.matong:
                cesuo_result = MyArraysUtils.resetSwitch(switch_cesuo, cesuo, tv_cesuo, 1);
                break;
            case R.id.lutiankeng:
                cesuo_result = MyArraysUtils.resetSwitch(switch_cesuo, cesuo, tv_cesuo, 2);
                break;
            case R.id.jianyipengce:
                cesuo_result = MyArraysUtils.resetSwitch(switch_cesuo, cesuo, tv_cesuo, 3);
                break;
            case R.id.fenchishi:
                cesuo_result = MyArraysUtils.resetSwitch(switch_cesuo, cesuo, tv_cesuo, 4);
                break;
            case R.id.cesuo_qita:
                cesuo_result = MyArraysUtils.resetSwitch(switch_cesuo, cesuo, tv_cesuo, 5);
                break;
            case R.id.danshe:
                qinchu_result = MyArraysUtils.resetSwitch(switch_qinchu, qinchu, tv_qinchu, 0);
                break;
            case R.id.shinei:
                qinchu_result = MyArraysUtils.resetSwitch(switch_qinchu, qinchu, tv_qinchu, 1);
                break;
            case R.id.shiwai:
                qinchu_result = MyArraysUtils.resetSwitch(switch_qinchu, qinchu, tv_qinchu, 2);
                break;
            case R.id.jiaqin_wu:
                qinchu_result = MyArraysUtils.resetSwitch(switch_qinchu, qinchu, tv_qinchu, 3);
                break;
            case R.id.tv_sign_up_go_back:
                if (iFragmentChange != null) {
                    iFragmentChange.lastStep(this);
                }
                break;
            case R.id.tv_sign_up_go_forward:
                if (!MyArraysUtils.isContainTrue(switch_paiqi)||
                        !MyArraysUtils.isContainTrue(switch_ranliao)||
                        !MyArraysUtils.isContainTrue(switch_cesuo)||
                        !MyArraysUtils.isContainTrue(switch_qinchu)||
                        !MyArraysUtils.isContainTrue(switch_yinshui)){
                    ((BuildingRecordActivity) getActivity()).speak(R.string.select_least_one);
                    return;
                }
                ((BuildingRecordActivity) getActivity()).buildingRecordBean.setKitchenExhaust(MyArraysUtils.getIndex(paiqi_result,paiqi,index1));
                ((BuildingRecordActivity) getActivity()).buildingRecordBean.setKitchenFuel(MyArraysUtils.getIndex(ranliao_result,ranliao,index2));
                ((BuildingRecordActivity) getActivity()).buildingRecordBean.setWaterEnvironment(MyArraysUtils.getIndex(yinshui_result,yinshui,index3));
                ((BuildingRecordActivity) getActivity()).buildingRecordBean.setToiletPosition(MyArraysUtils.getIndex(cesuo_result,cesuo,index4));
                ((BuildingRecordActivity) getActivity()).buildingRecordBean.setLivestockBar(MyArraysUtils.getIndex(qinchu_result,qinchu,index5));
                if (iFragmentChange != null) {
                    iFragmentChange.nextStep(this);
                }
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (view != null) {
            ((ViewGroup) view.getParent()).removeView(view);
        }
    }
}
