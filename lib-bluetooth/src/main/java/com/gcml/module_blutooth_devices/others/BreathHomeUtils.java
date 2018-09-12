package com.gcml.module_blutooth_devices.others;

import static java.lang.Math.log10;
import static java.lang.Math.pow;

/**
 * copyright：杭州国辰迈联机器人科技有限公司
 * version:V1.2.5
 * created on 2018/9/11 10:30
 * created by:gzq
 * description:TODO
 */
public class BreathHomeUtils {
    public static final int MALE = 0;
    public static final int FEMALE = 1;

    static double male_pef(double age, double height, double weight) { // L/s

        return (61.46 * height - 43 * age + 154) / 1000;
    }

    static double male_fev1(double age, double height, double weight) {// L

        return (43.01 * height - 29 * age - 2492) / 1000;
    }

    static double male_fvc(double age, double height, double weight) {// L

        return (57.57 * height - 26 * age - 4345) / 1000;
    }

    static double male_mef75(double age, double height, double weight) { // L/s

        return (54.59 * height - 29 * age - 470) / 1000;
    }

    static double male_mef50(double age, double height, double weight) { // L/s

        return (37.94 * height - 31 * age - 352) / 1000;
    }

    static double male_mef25(double age, double height, double weight) {// L/s

        return (26.05 * height - 26 * age - 1336) / 1000;
    }

    static double male_mmef(double age, double height, double weight) { // L/s

        return (19.4 * height - 43.0 * age + 2700) / 1000;
    }

    static double female_pef(double age, double height, double weight) {// L/s

        return (55.01 * height - 30 * age - 1106) / 1000;
    }

    static double female_fev1(double age, double height, double weight) { // L

        return (39.53 * height - 25 * age - 2604) / 1000;
    }

    static double female_fvc(double age, double height, double weight) {// L

        return (44.26 * height - 26 * age - 2887) / 1000;
    }

    static double female_mef75(double age, double height, double weight) {// L/s

        return (32.18 * height - 25 * age + 1596) / 1000;
    }

    static double female_mef50(double age, double height, double weight) { // L/s

        return (24.50 * height - 25 * age + 1156) / 1000;
    }

    static double female_mef25(double age, double height, double weight) { // L/s

        return (10.50 * height - 25 * age + 1107) / 1000;
    }

    static double female_mmef(double age, double height, double weight) { // L/s

        return (12.5 * height - 34.0 * age + 2920) / 1000;
    }

    //=========================================================================
    static double zapletal_male_pef(double age, double height, double weight) { // L/s

        return pow(10, -4.37221 + 2.34275 * log10(height));
    }

    static double zapletal_male_fev1(double age, double height, double weight) {// L

        return pow(10, -2.86521 + 2.87294 * log10(height)) / 1000;
    }

    static double zapletal_male_fvc(double age, double height, double weight) { // L

        return pow(10, -2.9239 + 2.9360 * log10(height)) / 1000;
    }

    static double zapletal_male_mef75(double age, double height, double weight) { // L/s

        return pow(10, -4.01648 + 2.1541 * log10(height));
    }

    static double zapletal_male_mef50(double age, double height, double weight) { // L/s

        return pow(10, -4.21684 + 2.17719 * log10(height));
    }

    static double zapletal_male_mef25(double age, double height, double weight) { // L/s

        return pow(10, -4.58082 + 2.21169 * log10(height));
    }

    static double zapletal_male_mmef(double age, double height, double weight) { // L/s

        return pow(10, -4.6651 + 2.3588 * log10(height));
    }

    static double zapletal_female_pef(double age, double height, double weight) {// L/s

        return pow(10, -4.37221 + 2.34275 * log10(height));
    }

    static double zapletal_female_fev1(double age, double height, double weight) {// L

        return pow(10, -2.60565 + 2.74136 * log10(height)) / 1000;
    }

    static double zapletal_female_fvc(double age, double height, double weight) { // L

        return pow(10, -2.7040 + 2.8181 * log10(height)) / 1000;
    }

    static double zapletal_female_mef75(double age, double height, double weight) { // L/s

        return pow(10, -4.01648 + 2.15414 * log10(height));
    }

    static double zapletal_female_mef50(double age, double height, double weight) { // L/s

        return pow(10, -4.21684 + 2.17719 * log10(height));
    }

    static double zapletal_female_mef25(double age, double height, double weight) { // L/s

        return pow(10, -4.58082 + 2.21169 * log10(height));
    }

    static double zapletal_female_mmef(double age, double height, double weight) { // L/s

        return pow(10, -4.6651 + 2.3588 * log10(height));
    }

    /**
     * pef预计值
     *
     * @param gender
     * @param age
     * @param height
     * @param weight
     * @return
     */
    static double drv_pred_pef(int gender, double age, double height, double weight) { // L/s

        if (age >= 4 && age < 18) {
            if (gender == MALE) {
                return zapletal_male_pef(age, height, weight);
            } else {
                return zapletal_female_pef(age, height, weight);
            }
        } else if (age >= 18 && age < 70) {
            if (gender == MALE) {
                return male_pef(age, height, weight);
            } else {
                return female_pef(age, height, weight);
            }
        } else if (age >= 70) {
            if (gender == MALE) {
                return male_pef(age, height, weight);
            } else {
                return female_pef(age, height, weight);
            }
        } else {
            return -1;
        }
    }

    static double drv_pred_fev1(int gender, double age, double height, double weight) {// L

        if (age >= 4 && age < 18) {
            if (gender == MALE) {
                return zapletal_male_fev1(age, height, weight);
            } else {
                return zapletal_female_fev1(age, height, weight);
            }
        } else if (age >= 18 && age < 70) {
            if (gender == MALE) {
                return male_fev1(age, height, weight);
            } else {
                return female_fev1(age, height, weight);
            }
        } else if (age >= 70) {
            if (gender == MALE) {
                return male_fev1(age, height, weight);
            } else {
                return female_fev1(age, height, weight);
            }
        } else {
            return -1;
        }
    }

    static double drv_pred_fvc(int gender, double age, double height, double weight) { // L

        if (age >= 4 && age < 18) {
            if (gender == MALE) {
                return zapletal_male_fvc(age, height, weight);
            } else {
                return zapletal_female_fvc(age, height, weight);
            }
        } else if (age >= 18 && age < 70) {
            if (gender == MALE) {
                return male_fvc(age, height, weight);
            } else {
                return female_fvc(age, height, weight);
            }
        } else if (age >= 70) {
            if (gender == MALE) {
                return male_fvc(age, height, weight);
            } else {
                return female_fvc(age, height, weight);
            }
        } else {
            return -1;
        }
    }

    static double drv_pred_mef75(int gender, double age, double height, double weight) { // L/s

        if (age >= 4 && age < 18) {
            if (gender == MALE) {
                return zapletal_male_mef75(age, height, weight);
            } else {
                return zapletal_female_mef75(age, height, weight);
            }
        } else if (age >= 18 && age < 70) {
            if (gender == MALE) {
                return male_mef75(age, height, weight);
            } else {
                return female_mef75(age, height, weight);
            }
        } else if (age >= 70) {
            if (gender == MALE) {
                return male_mef75(age, height, weight);
            } else {
                return female_mef75(age, height, weight);
            }
        } else {
            return -1;
        }
    }

    static double drv_pred_mef50(int gender, double age, double height, double weight) { // L/s

        if (age >= 4 && age < 18) {
            if (gender == MALE) {
                return zapletal_male_mef50(age, height, weight);
            } else {
                return zapletal_female_mef50(age, height, weight);
            }
        } else if (age >= 18 && age < 70) {
            if (gender == MALE) {
                return male_mef50(age, height, weight);
            } else {
                return female_mef50(age, height, weight);
            }
        } else if (age >= 70) {
            if (gender == MALE) {
                return male_mef50(age, height, weight);
            } else {
                return female_mef50(age, height, weight);
            }
        } else {
            return -1;
        }
    }

    static double drv_pred_mef25(int gender, double age, double height, double weight) {
        // L/s
        if (age >= 4 && age < 18) {
            if (gender == MALE) {
                return zapletal_male_mef25(age, height, weight);
            } else {
                return zapletal_female_mef25(age, height, weight);
            }
        } else if (age >= 18 && age < 70) {
            if (gender == MALE) {
                return male_mef25(age, height, weight);
            } else {
                return female_mef25(age, height, weight);
            }
        } else if (age >= 70) {
            if (gender == MALE) {
                return male_mef25(age, height, weight);
            } else {
                return female_mef25(age, height, weight);
            }
        } else {
            return -1;
        }
    }

    static double drv_pred_mmef(int gender, double age, double height, double weight) {
        // L/s
        if (age >= 4 && age < 18) {
            if (gender == MALE) {
                return zapletal_male_mmef(age, height, weight);
            } else {
                return zapletal_female_mmef(age, height, weight);
            }
        } else if (age >= 18 && age < 70) {
            if (gender == MALE) {
                return male_mmef(age, height, weight);
            } else {
                return female_mmef(age, height, weight);
            }
        } else if (age >= 70) {
            if (gender == MALE) {
                return male_mmef(age, height, weight);
            } else {
                return female_mmef(age, height, weight);
            }
        } else {
            return -1;
        }
    }

    /**
     * 检查pef的危险程度 -1 无效 0 正常1 警告2 危险
     *
     * @return
     */
    static int checkPEF(int gender, double age, double height, double weight, float reality) {
        double percent = reality / drv_pred_pef(gender, age, height, weight);
        if (percent < 0.6) {
            return 2;
        } else if (percent < 0.8) {
            return 1;
        } else if (percent <= 1) {
            return 0;
        }
        return -1;
    }

    /**
     * 检查fev1的危险程度 -1 无效0 正常1 轻2 中3 重
     *
     * @param gender
     * @param age
     * @param height
     * @param weight
     * @param reality
     * @return
     */
    static int checkFEV1(int gender, double age, double height, double weight, float reality) {
        double percent = reality / drv_pred_fev1(gender, age, height, weight);
        if (percent < 0.3) {
            return 3;
        } else if (percent < 0.5) {
            return 2;
        } else if (percent < 0.8) {
            return 1;
        } else if (percent <= 1) {
            return 0;
        }
        return -1;
    }

    /**
     * 检查fvc危险程度 -1 无效0 正常1 轻2 中3 重4 极重
     *
     * @param gender
     * @param age
     * @param height
     * @param weight
     * @return
     */
    static int checkFEV1_FVC(int gender, double age, double height, double weight, float fev1, float fvc) {
        float v = fev1 / fvc;
        if (v < 0.7 && checkFEV1(gender, age, height, weight, fev1) == 3) {
            return 4;
        } else if (v < 0.7 && checkFEV1(gender, age, height, weight, fev1) == 2) {
            return 3;
        } else if (v < 0.7 && checkFEV1(gender, age, height, weight, fev1) == 1) {
            return 2;
        } else if (v < 0.7 && checkFEV1(gender, age, height, weight, fev1) == 0) {
            return 1;
        } else if (v <= 1) {
            return 0;
        }
        return -1;
    }
}
