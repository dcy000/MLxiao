package com.zane.androidupnpdemo.utils;

import android.text.TextUtils;
import android.util.Log;

import com.zane.androidupnpdemo.pinyin.HanziToPinyin;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2018/4/19.
 */

public class PinyinHelper {
    public static String parseString(String str) {
        if (TextUtils.isEmpty(str)) {
            Log.e("转换的字符串不能为空", "parseString: ");
            return null;
        }
        StringBuffer sb_int = new StringBuffer();
        StringBuffer sb_result = new StringBuffer();
        for (int i = 0; i < str.length(); i++) {
            if (isSpecialChar(String.valueOf(str.charAt(i)))) {
                continue;
            }
            if (isInteger(str.charAt(i))) {
                sb_int.append(str.charAt(i));
                Log.e("数字", "parseString: " + str.charAt(i));
            } else if (isCharacter(str.charAt(i))) {
                if (sb_int.length() != 0) {
                    sb_result.append(getChineseNumber(Integer.parseInt(sb_int.toString())));
                    sb_int.setLength(0);
                }
                sb_result.append(str.charAt(i));
            } else {
                if (sb_int.length() != 0) {
                    sb_result.append(getChineseNumber(Integer.parseInt(sb_int.toString())));
                    sb_int.setLength(0);//清空
                }
                sb_result.append(str.charAt(i));
            }

        }
        if (sb_int.length() != 0) {
            sb_result.append(getChineseNumber(Integer.parseInt(sb_int.toString())));
            sb_int.setLength(0);
        }
        return sb_result.toString();
    }

    public static String getChineseNumber(int number) {
        String[] str = {"零", "一", "二", "三", "四", "五", "六", "七", "八", "九"};
        String[] str2 = {"十", "百", "千", "万", "亿"};
        if (number < 10) {
            return str[number];
        }
        if (number < 20) {
            int gewei = number - 10;
            if (gewei == 0) {
                return str2[0];
            }
            return str2[0] + str[gewei];
        }
        if (number < 100) {
            int shiwei = number / 10;
            int gewei = number % 10;
            return str[shiwei] + str2[0] + str[gewei];
        }
        if (number == 100) {
            return "一百";
        }
        if (number < 1000) {
            int baiwei = number / 100;
            int shiwei = (number % 100) / 10;
            int gewei = number % 10;
            if (shiwei == 0) {
                return str[baiwei] + "百零" + str[gewei];
            }
            return str[baiwei] + "百" + str[shiwei] + "十" + str[gewei];
        }
        if (number == 1000) {
            return "一千";
        }
        if (number < 10000) {
            int qianwei = number / 1000;
            int baiwei = (number % 1000) / 100;
            int shiwei = (number % 100) / 10;
            int gewei = number % 10;
            if (baiwei == 0 && shiwei == 0) {
                return str[qianwei] + "千零" + str[gewei];
            }
            if (baiwei == 0) {
                return str[qianwei] + "千零" + str[shiwei] + "十" + str[gewei];
            }
            if (shiwei == 0) {
                return str[qianwei] + "千" + str[baiwei] + "零" + str[gewei];
            }
            return str[qianwei] + "千" + str[baiwei] + "百" + str[shiwei] + "十" + str[gewei];
        }
        if (number == 10000) {
            return "一万";
        }
        return null;
    }

    public static boolean isInteger(char c) {
//        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
//        return pattern.matcher(String.valueOf(c)).matches();
        if (c >= '0' && c <= '9')
            return true;
        return false;
    }

    public static boolean isCharacter(char c) {
        if ((c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z'))
            return true;
        return false;
    }

    public static boolean isSpecialChar(String str) {
        String regEx = "[ _`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]|\n|\r|\t";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.find();
    }

    public static boolean IsCharChinese(char c) {
        if (0x4e00 < c && c < 0x9fa5)
            return true;
        return false;
    }

    public static String getPinYin(String hanzi) {
        //先将字符串中的阿拉伯数字转成中文
        ArrayList<HanziToPinyin.Token> tokens = HanziToPinyin.getInstance().get(parseString(hanzi));
        StringBuilder sb = new StringBuilder();
        if (tokens != null && tokens.size() > 0) {
            for (HanziToPinyin.Token token : tokens) {
                if (HanziToPinyin.Token.PINYIN == token.type) {
                    sb.append(token.target);
                } else {
                    sb.append(token.source);
                }
            }
        }

        return sb.toString().toLowerCase();
    }
}
