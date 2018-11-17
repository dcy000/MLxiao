package com.example.han.referralproject.intelligent_diagnosis;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.bean.MonthlyReport;
import com.example.han.referralproject.util.LocalShared;
import com.iflytek.synthetize.MLVoiceSynthetize;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class MonthlyReport5Fragment extends Fragment {
    @BindView(R.id.yan_1)
    TextView yan1;
    @BindView(R.id.yan_2)
    TextView yan2;
    @BindView(R.id.yan_3)
    TextView yan3;
    @BindView(R.id.yan_4)
    TextView yan4;
    @BindView(R.id.yundong_1)
    TextView yundong1;
    @BindView(R.id.yundong_2)
    TextView yundong2;
    @BindView(R.id.yundong_3)
    TextView yundong3;
    @BindView(R.id.yundong_4)
    TextView yundong4;
    @BindView(R.id.tizhong_1)
    TextView tizhong1;
    @BindView(R.id.tizhong_2)
    TextView tizhong2;
    @BindView(R.id.tizhong_3)
    TextView tizhong3;
    @BindView(R.id.tizhong_4)
    TextView tizhong4;
    @BindView(R.id.yinjiu_1)
    TextView yinjiu1;
    @BindView(R.id.yinjiu_2)
    TextView yinjiu2;
    @BindView(R.id.yinjiu_3)
    TextView yinjiu3;
    @BindView(R.id.yinjiu_4)
    TextView yinjiu4;
    Unbinder unbinder;
    private View view;
    private float nas,nam,yan_week1,yan_week2,yan_week3,yan_week4,yan_avg;
    private float yundongs,yundongm,yundong_week1,yundong_week2,yundong_week3,yundong_week4,yundong_avg;
    private float tizhongs,tizhongm,tizhong_week1,tizhong_week2,tizhong_week3,tizhong_week4,tizhong_avg,bmis,bmim;
    private float yinjius,yinjium,yinjiu_week1,yinjiu_week2,yinjiu_week3,yinjiu_week4,yinjiu_avg;
    private String tips="";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_monthly_report5, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    public void notifyData(MonthlyReport.MAPQ response) {
        nas=Float.parseFloat(response.nas);
        nam=Float.parseFloat(response.nam);
        if (nas>nam) {
            yan_avg= (float) ((nas-nam)/4.0);
            yan_week1 =nas-yan_avg;
            yan_week2=yan_week1-yan_avg;
            yan_week3=yan_week2-yan_avg;
            yan_week4=yan_week3-yan_avg;
            yan1.setText(String.format("%.2f",yan_week1));
            yan2.setText(String.format("%.2f",yan_week2));
            yan3.setText(String.format("%.2f",yan_week3));
            yan4.setText(String.format("%.2f",yan_week4));
        }else{
            yan1.setText(response.nas);
            yan2.setText(response.nas);
            yan3.setText(response.nas);
            yan4.setText(response.nas);
        }

        yundongs=Float.parseFloat(response.sportss);
        yundongm=Float.parseFloat(response.sportsm);
        if (yundongs<yundongm){
            yundong_avg= (float) ((yundongm-yundongs)/4.0);
            yundong_week1=yundongs+yundong_avg;
            yundong_week2=yundong_week1+yundong_avg;
            yundong_week3=yundong_week2+yundong_avg;
            yundong_week4=yundong_week3+yundong_avg;

            yundong1.setText(String.format("%.0f",yundong_week1));
            yundong2.setText(String.format("%.0f",yundong_week2));
            yundong3.setText(String.format("%.0f",yundong_week3));
            yundong4.setText(String.format("%.0f",yundong_week4));
        }else{
            yundong1.setText(response.sportss);
            yundong2.setText(response.sportss);
            yundong3.setText(response.sportss);
            yundong4.setText(response.sportss);
        }
        bmis=Float.parseFloat(response.bmis);
        bmim=Float.parseFloat(response.bmim);
        String height_s = LocalShared.getInstance(getActivity()).getUserHeight();
        float height_f = Float.parseFloat(height_s);
        tizhongs=bmis*(height_f / 100.0f) * (height_f / 100.0f);
        tizhongm=bmim*(height_f / 100.0f) * (height_f / 100.0f);

        if (tizhongs>tizhongm){
            tizhong_avg= (float) ((tizhongs-tizhongm)/4.0);
            tizhong_week1=tizhongs-tizhong_avg;
            tizhong_week2=tizhong_week1-tizhong_avg;
            tizhong_week3=tizhong_week2-tizhong_avg;
            tizhong_week4=tizhong_week3-tizhong_avg;

            tizhong1.setText(String.format("%.0f",tizhong_week1));
            tizhong2.setText(String.format("%.0f",tizhong_week2));
            tizhong3.setText(String.format("%.0f",tizhong_week3));
            tizhong4.setText(String.format("%.0f",tizhong_week4));
        }else{
            tizhong1.setText(String.format("%.0f",tizhongs));
            tizhong2.setText(String.format("%.0f",tizhongs));
            tizhong3.setText(String.format("%.0f",tizhongs));
            tizhong4.setText(String.format("%.0f",tizhongs));
        }

        yinjius=Float.parseFloat(response.drinks);
        yinjium=Float.parseFloat(response.drinkm);
        if (yinjius>yinjium){
            yinjiu_avg= (float) ((yinjius-yinjium)/4.0);
            yinjiu_week1=yinjius-yinjiu_avg;
            yinjiu_week2=yinjiu_week1-yinjiu_avg;
            yinjiu_week3=yinjiu_week2-yinjiu_avg;
            yinjiu_week4=yinjiu_week3-yinjiu_avg;

            yinjiu1.setText(String.format("%.0f",yinjiu_week1));
            yinjiu2.setText(String.format("%.0f",yinjiu_week2));
            yinjiu3.setText(String.format("%.0f",yinjiu_week3));
            yinjiu4.setText(String.format("%.0f",yinjiu_week4));
        }else{
            yinjiu1.setText(response.drinks);
            yinjiu2.setText(response.drinks);
            yinjiu3.setText(response.drinks);
            yinjiu4.setText(response.drinks);
        }
        tips="主人，您下月的健康目标如下：吃盐应少于"+String.format("%.0f",nam)+
                "克，运动应大于"+String.format("%.0f",yundongm)+"分钟，体重减少至"+
                String.format("%.0f",tizhongm)+"千克以下,饮酒应少于"+String.format("%.0f",yinjium)+"毫升";
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser){
            MLVoiceSynthetize.startSynthesize(tips);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
