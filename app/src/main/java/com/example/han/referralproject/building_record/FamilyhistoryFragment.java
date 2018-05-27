package com.example.han.referralproject.building_record;

import android.app.AlertDialog;
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
    /**
     * 您母亲患有以下哪种疾病？
     */
    private TextView dialogTitle;
    /**
     * 高血压
     */
    private TextView gaoxueya;
    /**
     * 糖尿病
     */
    private TextView tangniaobing;
    /**
     * 冠心病
     */
    private TextView guanxinbing;
    /**
     * 恶性肿瘤
     */
    private TextView zhongliu;
    /**
     * 脑卒中
     */
    private TextView naocuzhong;
    /**
     * 结核病
     */
    private TextView jihebing;
    /**
     * 慢性阻塞性肺疾病
     */
    private TextView feibing;
    /**
     * 严重精神障碍
     */
    private TextView jingshenzhangai;
    /**
     * 肝炎
     */
    private TextView ganyan;
    /**
     * 先天畸形
     */
    private TextView xiantianjixing;
    /**
     * 其他
     */
    private TextView qita;

    public void setOnFragmentChange(IFragmentChange iFragmentChange) {
        this.iFragmentChange = iFragmentChange;
    }

    private boolean[] switch_relative = new boolean[5];
    private String[] relative = new String[]{"父亲", "母亲", "兄弟姐妹", "子女", "无"};
    private List<TextView> textViews;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_family_history, container, false);
            initView(view);
        }
        initView(view);
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
//                showDialog(relative[0]);
                result = MyArraysUtils.resetSwitch(switch_relative, relative, textViews, 0);
                break;
            case R.id.muqin:
                result = MyArraysUtils.resetSwitch(switch_relative, relative, textViews, 1);
//                showDialog(relative[1]);
                break;
            case R.id.xiongdi:
                result = MyArraysUtils.resetSwitch(switch_relative, relative, textViews, 2);
//                showDialog(relative[2]);
                break;
            case R.id.zinv:
                result = MyArraysUtils.resetSwitch(switch_relative, relative, textViews, 3);
//                showDialog(relative[3]);
                break;
            case R.id.wu:
                result = MyArraysUtils.resetSwitch(switch_relative, relative, textViews, 4);
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
                ((BuildingRecordActivity) getActivity()).buildingRecordBean.setDiseasesKinsfolk(result);
                if (iFragmentChange != null) {
                    iFragmentChange.nextStep(this);
                }
                break;

            case R.id.gaoxueya:
                break;
            case R.id.tangniaobing:
                break;
            case R.id.guanxinbing:
                break;
            case R.id.zhongliu:
                break;
            case R.id.naocuzhong:
                break;
            case R.id.jihebing:
                break;
            case R.id.feibing:
                break;
            case R.id.jingshenzhangai:
                break;
            case R.id.ganyan:
                break;
            case R.id.xiantianjixing:
                break;
            case R.id.qita:
                break;
        }
    }

    private void showDialog(String title) {
        View inflate = LayoutInflater.from(getContext()).inflate(R.layout.family_disease, null, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(inflate);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        WindowManager.LayoutParams attributes = alertDialog.getWindow().getAttributes();
        attributes.width = 1400;
        alertDialog.getWindow().setAttributes(attributes);
        dialogTitle = (TextView) inflate.findViewById(R.id.dialog_title);
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

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (view != null) {
            ((ViewGroup) view.getParent()).removeView(view);
        }
    }
}
