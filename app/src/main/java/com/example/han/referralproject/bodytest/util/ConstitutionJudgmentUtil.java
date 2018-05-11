package com.example.han.referralproject.bodytest.util;

import android.graphics.Point;


import com.example.han.referralproject.bodytest.bean.MonitorRequestionBean;
import com.example.han.referralproject.bodytest.constant.ConstitutionJudgmentEnum;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lenovo on 2018/5/9.
 * 体质测试
 */

public class ConstitutionJudgmentUtil {
    /**
     * 偏颇类型数
     */
    public static final int TYPE_COUNT = 8;

    public static final int PING_HE = 0;
    public static final int QIONG_XIANG = 1;
    public static final int PIAN_PO = 2;
    public static final int PIAN_PO_NOT = 3;
    public static final int PING_HE_NOT = 4;


//    原始分=各个条目的分会相加。
//    转化分数=[（原始分-条目数（每项问题数））/（条目数×4）] ×100

    /**
     * 每一种体质的的原始分 和转化分
     *
     * @param data 问题数据
     * @param info 体质类型信息
     * @return point.x 原始分;point.y 转化分
     */
    public static Point getScore(List<MonitorRequestionBean> data, ConstitutionJudgmentEnum info) {
        Point point = new Point();

        if (data == null || data.size() == 0) {
            return point;
        }

        int score = 0;
        int size = data.size();
        for (int i = 0; i < size; i++) {
            if (data.get(i).requestionType.equals(info.getDescription())) {
                score += data.get(i).score;
            }
        }

        point.x = score;
        float itemCount = info.getItemCount();
        point.y = (int) (((score - itemCount) / itemCount) * 25);
        return point;
    }


    public static String getMonitorResult(List<MonitorRequestionBean> data) {
        Point scoreA = getScore(data, ConstitutionJudgmentEnum.A);
        int size = getPianPoNot(data).size();
        if (scoreA.y >= 60 && size == TYPE_COUNT) {
            return "平和质";
        }

        if (scoreA.y >= 60 && getQingXiang(data).size() > 0) {

            Map<String, Integer> qingXiang = getQingXiang(data);
            String info = "偏";
            for (Map.Entry<String, Integer> entry : qingXiang.entrySet()) {
                info = info + "," + entry.getKey();
            }
            return "平和质" + info;
        }

        Map<String, Integer> pianPO = getPianPO(data);

        if (pianPO.size() != 0) {
            String info = "";
            for (Map.Entry<String, Integer> entry : pianPO.entrySet()) {
                info = info + "" + entry.getKey();
            }
            return info;

        }
        return "体质比较综合";
    }

    /**
     * 偏颇
     */
    public static Map<String, Integer> getPianPO(List<MonitorRequestionBean> data) {
        Map<String, Integer> map = new HashMap<>();
        Point scoreB = getScore(data, ConstitutionJudgmentEnum.B);
        Point scoreC = getScore(data, ConstitutionJudgmentEnum.C);
        Point scoreD = getScore(data, ConstitutionJudgmentEnum.D);
        Point scoreE = getScore(data, ConstitutionJudgmentEnum.E);
        Point scoreF = getScore(data, ConstitutionJudgmentEnum.F);
        Point scoreG = getScore(data, ConstitutionJudgmentEnum.G);
        Point scoreH = getScore(data, ConstitutionJudgmentEnum.H);
        Point scoreI = getScore(data, ConstitutionJudgmentEnum.I);
        if (scoreB.y >= 40) {
            map.put(ConstitutionJudgmentEnum.B.getDescription(), scoreB.y);
        }
        if (scoreC.y >= 40) {
            map.put(ConstitutionJudgmentEnum.C.getDescription(), scoreC.y);
        }
        if (scoreD.y >= 40) {
            map.put(ConstitutionJudgmentEnum.D.getDescription(), scoreD.y);
        }
        if (scoreE.y >= 40) {
            map.put(ConstitutionJudgmentEnum.E.getDescription(), scoreE.y);
        }
        if (scoreF.y >= 40) {
            map.put(ConstitutionJudgmentEnum.F.getDescription(), scoreF.y);
        }
        if (scoreG.y >= 40) {
            map.put(ConstitutionJudgmentEnum.G.getDescription(), scoreG.y);
        }
        if (scoreH.y >= 40) {
            map.put(ConstitutionJudgmentEnum.H.getDescription(), scoreH.y);
        }
        if (scoreI.y >= 40) {
            map.put(ConstitutionJudgmentEnum.I.getDescription(), scoreI.y);
        }
        return map;
    }

    /**
     * 倾向
     */
    public static Map<String, Integer> getQingXiang(List<MonitorRequestionBean> data) {
        Map<String, Integer> map = new HashMap<>();
        Point scoreB = getScore(data, ConstitutionJudgmentEnum.B);
        Point scoreC = getScore(data, ConstitutionJudgmentEnum.C);
        Point scoreD = getScore(data, ConstitutionJudgmentEnum.D);
        Point scoreE = getScore(data, ConstitutionJudgmentEnum.E);
        Point scoreF = getScore(data, ConstitutionJudgmentEnum.F);
        Point scoreG = getScore(data, ConstitutionJudgmentEnum.G);
        Point scoreH = getScore(data, ConstitutionJudgmentEnum.H);
        Point scoreI = getScore(data, ConstitutionJudgmentEnum.I);
        if (scoreB.y < 40 && scoreB.y >= 30) {
            map.put(ConstitutionJudgmentEnum.B.getDescription(), scoreB.y);
        }
        if (scoreC.y < 40 && scoreC.y >= 30) {
            map.put(ConstitutionJudgmentEnum.C.getDescription(), scoreC.y);
        }
        if (scoreD.y < 40 && scoreD.y >= 30) {
            map.put(ConstitutionJudgmentEnum.D.getDescription(), scoreD.y);
        }
        if (scoreE.y < 40 && scoreE.y >= 30) {
            map.put(ConstitutionJudgmentEnum.E.getDescription(), scoreE.y);
        }
        if (scoreF.y < 40 && scoreF.y >= 30) {
            map.put(ConstitutionJudgmentEnum.F.getDescription(), scoreF.y);
        }
        if (scoreG.y < 40 && scoreG.y >= 30) {
            map.put(ConstitutionJudgmentEnum.G.getDescription(), scoreG.y);
        }
        if (scoreH.y < 40 && scoreH.y >= 30) {
            map.put(ConstitutionJudgmentEnum.H.getDescription(), scoreH.y);
        }
        if (scoreI.y < 40 && scoreI.y >= 30) {
            map.put(ConstitutionJudgmentEnum.I.getDescription(), scoreI.y);
        }
        return map;
    }

    /**
     * 倾向
     */
    public static Map<String, Integer> getPianPoNot(List<MonitorRequestionBean> data) {
        Map<String, Integer> map = new HashMap<>();
        Point scoreB = getScore(data, ConstitutionJudgmentEnum.B);
        Point scoreC = getScore(data, ConstitutionJudgmentEnum.C);
        Point scoreD = getScore(data, ConstitutionJudgmentEnum.D);
        Point scoreE = getScore(data, ConstitutionJudgmentEnum.E);
        Point scoreF = getScore(data, ConstitutionJudgmentEnum.F);
        Point scoreG = getScore(data, ConstitutionJudgmentEnum.G);
        Point scoreH = getScore(data, ConstitutionJudgmentEnum.H);
        Point scoreI = getScore(data, ConstitutionJudgmentEnum.I);
        if (scoreB.y < 30) {
            map.put(ConstitutionJudgmentEnum.B.getDescription(), scoreB.y);
        }
        if (scoreC.y < 30) {
            map.put(ConstitutionJudgmentEnum.C.getDescription(), scoreC.y);
        }
        if (scoreD.y < 30) {
            map.put(ConstitutionJudgmentEnum.D.getDescription(), scoreD.y);
        }
        if (scoreE.y < 30) {
            map.put(ConstitutionJudgmentEnum.E.getDescription(), scoreE.y);
        }
        if (scoreF.y < 30) {
            map.put(ConstitutionJudgmentEnum.F.getDescription(), scoreF.y);
        }
        if (scoreG.y < 30) {
            map.put(ConstitutionJudgmentEnum.G.getDescription(), scoreG.y);
        }
        if (scoreH.y < 30) {
            map.put(ConstitutionJudgmentEnum.H.getDescription(), scoreH.y);
        }
        if (scoreI.y < 30) {
            map.put(ConstitutionJudgmentEnum.I.getDescription(), scoreI.y);
        }
        return map;
    }

    public static Map<String, Integer> getAll(List<MonitorRequestionBean> data) {
        Map<String, Integer> map = new HashMap<>();
        Point scoreA = getScore(data, ConstitutionJudgmentEnum.A);
        Point scoreB = getScore(data, ConstitutionJudgmentEnum.B);
        Point scoreC = getScore(data, ConstitutionJudgmentEnum.C);
        Point scoreD = getScore(data, ConstitutionJudgmentEnum.D);
        Point scoreE = getScore(data, ConstitutionJudgmentEnum.E);
        Point scoreF = getScore(data, ConstitutionJudgmentEnum.F);
        Point scoreG = getScore(data, ConstitutionJudgmentEnum.G);
        Point scoreH = getScore(data, ConstitutionJudgmentEnum.H);
        Point scoreI = getScore(data, ConstitutionJudgmentEnum.I);

        map.put(ConstitutionJudgmentEnum.A.getDescription(), scoreA.y);
        map.put(ConstitutionJudgmentEnum.B.getDescription(), scoreB.y);
        map.put(ConstitutionJudgmentEnum.C.getDescription(), scoreC.y);
        map.put(ConstitutionJudgmentEnum.D.getDescription(), scoreD.y);
        map.put(ConstitutionJudgmentEnum.E.getDescription(), scoreE.y);
        map.put(ConstitutionJudgmentEnum.F.getDescription(), scoreF.y);
        map.put(ConstitutionJudgmentEnum.G.getDescription(), scoreG.y);
        map.put(ConstitutionJudgmentEnum.H.getDescription(), scoreH.y);
        map.put(ConstitutionJudgmentEnum.I.getDescription(), scoreI.y);
        return map;
    }

    public static String getResultScores(List<MonitorRequestionBean> data) {
        Map<String, Integer> all = ConstitutionJudgmentUtil.getAll(data);
        String info = "主人您的各体质类型转化分:";
        for (Map.Entry<String, Integer> entry : all.entrySet()) {
            info += entry.getKey() + entry.getValue() + "分,";
        }
        return info;
    }


}
