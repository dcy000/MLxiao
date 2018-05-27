package com.example.han.referralproject.building_record;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.han.referralproject.R;

import java.util.ArrayList;
import java.util.List;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

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

        }
    }

    private void showDialog(String title) {
        DisplayMetrics dm = new DisplayMetrics();
        View inflate = LayoutInflater.from(getContext()).inflate(R.layout.family_disease, null, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(inflate);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        alertDialog.getWindow().setLayout((int) (dm.widthPixels * 0.8), WRAP_CONTENT);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (view != null) {
            ((ViewGroup) view.getParent()).removeView(view);
        }
    }
}
