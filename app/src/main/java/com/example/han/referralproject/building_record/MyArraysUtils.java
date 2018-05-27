package com.example.han.referralproject.building_record;

import android.widget.TextView;

import org.apache.commons.lang.ArrayUtils;

import java.util.List;

public class MyArraysUtils {
    public static boolean isContainTrue(boolean[] array) {
        for (boolean ay : array) {
            if (ay) {
                return true;
            }
        }
        return false;
    }

    //判断数组是否包含有true开关
    public static boolean isContainTrue(boolean[] array1, boolean[] array2) {
        boolean[] both = ArrayUtils.addAll(array1, array2);
        for (boolean array : both) {
            if (array) {
                return true;
            }
        }
        return false;
    }

    public static String jointString(boolean[] booleans, String[] needJoint) {
        if (booleans.length != needJoint.length) {
            return "两数组长度不一致";
        }
        if (booleans.length == 0 && needJoint.length == 0) {
            return "两数组均为空数组";
        }
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < booleans.length; i++) {
            if (booleans[i]) {
                buffer.append(needJoint[i] + ",");
            }
        }
        if (buffer.length() > 1) {
            return buffer.substring(0, buffer.length() - 1);
        }
        return "";
    }

    public static String resetSwitch(boolean[] switchs, String[] contents, List<TextView> textViews, int selectedPosition) {
        String targetString = "";
        if (textViews == null) {
            return "错误";
        }
        if (textViews.size() != switchs.length) {
            return "错误";
        }
        if (selectedPosition < 0 || selectedPosition >= textViews.size()) {
            return "错误";
        }
        switchs[selectedPosition] = true;
        for (int i = 0; i < textViews.size(); i++) {
            if (selectedPosition == i) {
                textViews.get(i).setSelected(true);
                targetString = contents[selectedPosition];
                continue;
            }
            textViews.get(i).setSelected(false);
        }
        return targetString;
    }
}
