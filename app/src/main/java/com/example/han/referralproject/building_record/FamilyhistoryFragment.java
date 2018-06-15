package com.example.han.referralproject.building_record;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.han.referralproject.R;

import java.util.ArrayList;
import java.util.List;

public class FamilyhistoryFragment extends Fragment implements View.OnClickListener {
    private View view;
    private TextView title1;
    private TextView fuqin;
    private TextView muqin;
    private TextView xiongdi;
    private TextView zinv;
    private LinearLayout ll1;
    private TextView tvSignUpGoBack;
    private TextView tvSignUpGoForward;
    private IFragmentChange iFragmentChange;
    private TextView wu;
    private String result;
    private TextView dialogTitle;
    private TextView gaoxueya;
    private TextView tangniaobing;
    private TextView guanxinbing;
    private TextView zhongliu;
    private TextView naocuzhong;
    private TextView jihebing;
    private TextView feibing;
    private TextView jingshenzhangai;
    private TextView ganyan;
    private TextView xiantianjixing;
    private TextView qita;
    private String subResult;
    private AlertDialog alertDialog;
    private String parent_result;

    public void setOnFragmentChange(IFragmentChange iFragmentChange) {
        this.iFragmentChange = iFragmentChange;
    }

    private boolean[] switch_relative = new boolean[5];
    private String[] relative = new String[]{"父亲", "母亲", "兄弟姐妹", "子女", "无"};
    private List<TextView> textViews;
    private boolean[] switch_subRelative = new boolean[11];
    private String[] subRelative = new String[]{"高血压", "糖尿病", "冠心病", "恶性肿瘤", "脑卒中", "结核病", "慢性阻塞性肺疾病", "严重精神障碍", "肝炎", "先天畸形", "其他"};
    private List<TextView> subTextviews;
    private String[] index=new String[]{"1","2","3","4","0"};
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_family_history, container, false);
            initView(view);
        }
        return view;
    }

    private void initView(View view) {
        title1 = (TextView) view.findViewById(R.id.title1);
        fuqin = (TextView) view.findViewById(R.id.fuqin);
        fuqin.setOnClickListener(this);
        muqin = (TextView) view.findViewById(R.id.muqin);
        muqin.setOnClickListener(this);
        xiongdi = (TextView) view.findViewById(R.id.xiongdi);
        xiongdi.setOnClickListener(this);
        zinv = (TextView) view.findViewById(R.id.zinv);
        zinv.setOnClickListener(this);
        ll1 = (LinearLayout) view.findViewById(R.id.ll_1);
        tvSignUpGoBack = (TextView) view.findViewById(R.id.tv_sign_up_go_back);
        tvSignUpGoBack.setOnClickListener(this);
        tvSignUpGoForward = (TextView) view.findViewById(R.id.tv_sign_up_go_forward);
        tvSignUpGoForward.setOnClickListener(this);
        wu = (TextView) view.findViewById(R.id.wu);
        wu.setOnClickListener(this);
        textViews = new ArrayList<>();
        textViews.add(fuqin);
        textViews.add(muqin);
        textViews.add(xiongdi);
        textViews.add(zinv);
        textViews.add(wu);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((BuildingRecordActivity) getActivity()).setDisableGlobalListen(true);
        ((BuildingRecordActivity) getActivity()).speak("主人,您是否有家族病史？");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.fuqin:
                showDialog(relative[0]);
                parent_result = MyArraysUtils.resetSwitch(switch_relative, relative, textViews, 0);
                break;
            case R.id.muqin:
                showDialog(relative[1]);
                parent_result =MyArraysUtils.resetSwitch(switch_relative, relative, textViews, 1);
                break;
            case R.id.xiongdi:
                showDialog(relative[2]);
                parent_result =MyArraysUtils.resetSwitch(switch_relative, relative, textViews, 2);
                break;
            case R.id.zinv:
                showDialog(relative[3]);
                parent_result =MyArraysUtils.resetSwitch(switch_relative, relative, textViews, 3);
                break;
            case R.id.wu:
//                showDialog(relative[4]);
                parent_result =MyArraysUtils.resetSwitch(switch_relative, relative, textViews, 4);
                break;
            case R.id.tv_sign_up_go_back:
                if (iFragmentChange != null) {
                    iFragmentChange.lastStep(this);
                }
                break;
            case R.id.tv_sign_up_go_forward:
                if (!MyArraysUtils.isContainTrue(switch_relative)) {
                    ((BuildingRecordActivity) getActivity()).speak(R.string.select_least_one);
                    return;
                }
                ((BuildingRecordActivity) getActivity()).buildingRecordBean.setDiseasesKinsfolk(MyArraysUtils.getIndex(parent_result,relative,index));
                if (iFragmentChange != null) {
                    iFragmentChange.nextStep(this);
                }
                break;

            case R.id.gaoxueya:
                subResult = MyArraysUtils.resetSwitch(switch_subRelative, subRelative, subTextviews, 0);
                alertDialog.dismiss();
                break;
            case R.id.tangniaobing:
                subResult = MyArraysUtils.resetSwitch(switch_subRelative, subRelative, subTextviews, 1);
                alertDialog.dismiss();
                break;
            case R.id.guanxinbing:
                subResult = MyArraysUtils.resetSwitch(switch_subRelative, subRelative, subTextviews, 2);
                alertDialog.dismiss();
                break;
            case R.id.zhongliu:
                subResult = MyArraysUtils.resetSwitch(switch_subRelative, subRelative, subTextviews, 3);
                alertDialog.dismiss();
                break;
            case R.id.naocuzhong:
                subResult = MyArraysUtils.resetSwitch(switch_subRelative, subRelative, subTextviews, 4);
                alertDialog.dismiss();
                break;
            case R.id.jihebing:
                subResult = MyArraysUtils.resetSwitch(switch_subRelative, subRelative, subTextviews, 5);
                alertDialog.dismiss();
                break;
            case R.id.feibing:
                subResult = MyArraysUtils.resetSwitch(switch_subRelative, subRelative, subTextviews, 6);
                alertDialog.dismiss();
                break;
            case R.id.jingshenzhangai:
                subResult = MyArraysUtils.resetSwitch(switch_subRelative, subRelative, subTextviews, 7);
                alertDialog.dismiss();
                break;
            case R.id.ganyan:
                subResult = MyArraysUtils.resetSwitch(switch_subRelative, subRelative, subTextviews, 8);
                alertDialog.dismiss();
                break;
            case R.id.xiantianjixing:
                subResult = MyArraysUtils.resetSwitch(switch_subRelative, subRelative, subTextviews, 9);
                alertDialog.dismiss();
                break;
            case R.id.qita:
                subResult = MyArraysUtils.resetSwitch(switch_subRelative, subRelative, subTextviews, 10);
                alertDialog.dismiss();
                break;
        }
    }

    private void showDialog(final String title) {
        View inflate = LayoutInflater.from(getContext()).inflate(R.layout.family_disease, null, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(inflate);
        alertDialog = builder.create();
        alertDialog.show();
        alertDialog.setCanceledOnTouchOutside(false);
        WindowManager.LayoutParams attributes = alertDialog.getWindow().getAttributes();
        attributes.width = 1400;
        alertDialog.getWindow().setAttributes(attributes);
        dialogTitle = (TextView) inflate.findViewById(R.id.dialog_title);
        //您母亲患有以下哪种疾病？
        dialogTitle.setText("您的"+title+"患有以下哪种疾病？");
        gaoxueya = (TextView) inflate.findViewById(R.id.gaoxueya);
        gaoxueya.setOnClickListener(this);
        tangniaobing = (TextView) inflate.findViewById(R.id.tangniaobing);
        tangniaobing.setOnClickListener(this);
        guanxinbing = (TextView) inflate.findViewById(R.id.guanxinbing);
        guanxinbing.setOnClickListener(this);
        zhongliu = (TextView) inflate.findViewById(R.id.zhongliu);
        zhongliu.setOnClickListener(this);
        naocuzhong = (TextView) inflate.findViewById(R.id.naocuzhong);
        naocuzhong.setOnClickListener(this);
        jihebing = (TextView) inflate.findViewById(R.id.jihebing);
        jihebing.setOnClickListener(this);
        feibing = (TextView) inflate.findViewById(R.id.feibing);
        feibing.setOnClickListener(this);
        jingshenzhangai = (TextView) inflate.findViewById(R.id.jingshenzhangai);
        jingshenzhangai.setOnClickListener(this);
        ganyan = (TextView) inflate.findViewById(R.id.ganyan);
        ganyan.setOnClickListener(this);
        xiantianjixing = (TextView) inflate.findViewById(R.id.xiantianjixing);
        xiantianjixing.setOnClickListener(this);
        qita = (TextView) inflate.findViewById(R.id.qita);
        qita.setOnClickListener(this);
        subTextviews = new ArrayList<>();
        subTextviews.add(gaoxueya);
        subTextviews.add(tangniaobing);
        subTextviews.add(guanxinbing);
        subTextviews.add(zhongliu);
        subTextviews.add(naocuzhong);
        subTextviews.add(jihebing);
        subTextviews.add(feibing);
        subTextviews.add(jingshenzhangai);
        subTextviews.add(ganyan);
        subTextviews.add(xiantianjixing);
        subTextviews.add(qita);
        alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                switch (title) {
                    case "父亲":
                        subResult = "父亲：" + subResult;
                        break;
                    case "母亲":
                        subResult = "母亲：" + subResult;
                        break;
                    case "兄弟姐妹":
                        subResult = "兄弟姐妹：" + subResult;
                        break;
                    case "子女":
                        subResult = "子女：" + subResult;
                        break;
                    case "无":
                        subResult = "";
                        break;
                }
            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (view != null) {
            ((ViewGroup) view.getParent()).removeView(view);
        }
    }
}
