package com.witspring.util;

import java.security.MessageDigest;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class StringUtil {

    public static boolean isEqual(String first, String... anothers) {
        if (isTrimBlank(first)) {
            return false;
        }
        for (String other : anothers) {
            boolean equaled = isNotTrimBlank(other) && first.equals(other);
            if (equaled) {
                return true;
            }
        }
        return false;
    }

    public static boolean contains(String one, String...anothers) {
        if (isTrimBlank(one) || !CommUtil.notEmpty(anothers)) {
            return false;
        } else {
            for (String other : anothers) {
                boolean contained = isNotTrimBlank(other) && one.contains(other);
                if (contained) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isNotBlank(String str) {
        return (str != null) && (str.length() != 0);
    }

    public static boolean isBlank(String str) {
        return (str == null) || (str.length() == 0);
    }

    public static boolean isNotTrimBlank(String str) {
        return (str != null) && (str.trim().length() != 0);
    }

    public static boolean isNotTrimBlanks(String... strs) {
        boolean result = true;
        for (String str : strs) {
            result = result && (str != null) && (str.trim().length() != 0);
        }
        return result;
    }

    public static boolean isTrimBlank(String str) {
        return (str == null) || (str.trim().length() == 0);
    }

    public static boolean isLengthBetween(String str, int min, int max) {
        return isNotTrimBlank(str) && str.length() >= min && str.length() <= max;
    }

    public static String capFirstUpperCase(String str) {
        if (isBlank(str)) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    public static String capFirstLowerCase(String str) {
        if (isBlank(str)) {
            return str;
        }
        return str.substring(0, 1).toLowerCase() + str.substring(1);
    }

    public static boolean isIdNo(String idNo) {
        if (isTrimBlank(idNo)) {
            return false;
        }
        return StringUtil.isTrimBlank(IDCardUtil.IDCardValidate(idNo));
    }

    /**
     * 手机号码匹配
     * @param mobile
     */
    public static boolean isMobile(String mobile) {
        if (isTrimBlank(mobile)) {
            return false;
        }
        Pattern p = Pattern.compile("^((13[0-9])|(14[5,7])|(15[^4,\\D])|(17[6-8])|(170)|(18[0-9]))\\d{8}$");
        Matcher matcher = p.matcher(mobile);
        return matcher.matches();
    }

    public static boolean isEmail(String email) {
        if (isTrimBlank(email)) {
            return false;
        }
        Pattern p = Pattern.compile("^(\\w+((-\\w+)|(\\.\\w+))*)\\+\\w+((-\\w+)|(\\.\\w+))*\\@[A-Za-z0-9]+((\\.|-)[A-Za-z0-9]+)*\\.[A-Za-z0-9]+$");
        Matcher matcher = p.matcher(email);
        return matcher.matches();
    }

    /**
     * 匹配数字、字母，长度在指定范围内
     * @param content
     */
    public static boolean isContent(String content, int minLen, int maxLen) {
        Pattern pattern = Pattern.compile("^[\u4E00-\u9FA50-9a-zA-Z]{" + minLen + "," + maxLen + "}+$");
        Matcher matcher = pattern.matcher(content.trim());
        return matcher.matches();
    }

    public static boolean isContent(String content) {
        Pattern pattern = Pattern.compile("^[\u4E00-\u9FA50-9a-zA-Z_]+$");
        Matcher matcher = pattern.matcher(content.trim());
        return matcher.matches();
    }

    public static boolean isPassword(String content) {
        Pattern pattern = Pattern.compile("^[0-9a-zA-Z_]+$");
        Matcher matcher = pattern.matcher(content.trim());
        return matcher.matches() && !content.contains(" ");
    }

    /**
     * 获得汉语拼音首字母
     */
    public static String getFirstLetter(String str){
        if (str.equals("-")) {
            return "&";
        }
        if (str == null) {
            return "#";
        }
        if (str.trim().length() == 0) {
            return "#";
        }
        char c = str.trim().substring(0, 1).charAt(0);
        // 正则表达式，判断首字母是否是英文字母
        Pattern pattern = Pattern.compile("^[A-Za-z]+$");
        if (pattern.matcher(c + "").matches()) {
            return (c + "").toUpperCase();
        } else {
            return "#";
        }
    }

    /**
     * 非法字符过滤
     * @param content
     * @return
     */
    public static boolean StringFilter(String content) {
        String regEx = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Pattern pattern = Pattern.compile(regEx);
        Matcher matcher = pattern.matcher(content.trim());
        return matcher.matches();
    }

    public static String num2String(Long no) {
        if (no.longValue() < 10L)
            return String.valueOf(no);
        if (no.longValue() < 36L)
            return String.valueOf((char) (int) (no.longValue() - 10L + 65L));
        if (no.longValue() < 62L) {
            return String.valueOf((char) (int) (no.longValue() - 36L + 97L));
        }
        return num2String(Long.valueOf(no.longValue() / 62L)) + num2String(Long.valueOf(no.longValue() % 62L));
    }

    /**
     * 返回￥0.00格式的金额
     */
    public static String moneyFormat(Object money) {
        return String.format("￥%,.2f", money);
    }

    /**
     * 验证日期字符串是否是YYYY-MM-DD格式
     */
    public static boolean isDataFormat(String str) {
        boolean flag = false;
        // String
        // regxStr="[1-9][0-9]{3}-[0-1][0-2]-((0[1-9])|([12][0-9])|(3[01]))";
        String regxStr = "^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))(\\s(((0?[0-9])|([1-2][0-3]))\\:([0-5]?[0-9])((\\s)|(\\:([0-5]?[0-9])))))?$";
        Pattern pattern1 = Pattern.compile(regxStr);
        Matcher isNo = pattern1.matcher(str);
        if (isNo.matches()) {
            flag = true;
        }
        return flag;
    }

    public static String replaceSpace(String info) {
        String str = null;
        if (info != null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(info);
            str = m.replaceAll("");
        }
        return str;
    }

    public static String nullToEmpty(String info) {
        return info == null ? "" : info;
    }

    /**
     * 判断特殊字符
     */
    public static boolean hasSpecialStr(String str) throws PatternSyntaxException {
        String regEx="[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.find();
    }

    /**
     * 功能：判断字符串是否为数字
     */
    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("^(-?[1-9]\\d*\\.?\\d*)|(-?0\\.\\d*[1-9])|(-?[0])|(-?[0]\\.\\d*)$");
        Matcher isNum = pattern.matcher(str);
        return isNum.matches();
    }

    // 以下生成一个随机的字符串
    private static long id = 0;
    private static String prefix = randomString(5) + "-";

    public static String getPacketID() {
        return prefix + Long.toString(id++);
    }

    public static String randomString(int length) {
        char[] numbersAndLetters = ("0123456789abcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ").toCharArray();
        if (length < 1) {
            return null;
        }
        char[] randBuffer = new char[length];
        for (int i = 0; i < randBuffer.length; i++) {
            randBuffer[i] = numbersAndLetters[new Random().nextInt(71)];
        }
        return new String(randBuffer);
    }


    /**
     * 对字符串进行MD5加密
     */
    //十六进制下数字到字符的映射数组
    private final static String[] hexDigits = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};

    public static String encodeByMD5(String originString) {
        if (originString != null) {
            try {
                //创建具有指定算法名称的信息摘要  
                MessageDigest md = MessageDigest.getInstance("MD5");
                //使用指定的字节数组对摘要进行最后更新，然后完成摘要计算 
                byte[] results = md.digest(originString.getBytes());
                //将得到的字节数组变成字符串返回  
                return byteArrayToHexString(results);
            } catch (Exception ex) {
                return null;
            }
        }
        return null;
    }

    /**
     * 转换字节数组为十六进制字符串
     * @return 十六进制字符串
     */
    private static String byteArrayToHexString(byte[] b) {
        StringBuffer resultSb = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            resultSb.append(byteToHexString(b[i]));
        }
        return resultSb.toString();
    }

    /**
     * 将一个字节转化成十六进制形式的字符串
     */
    private static String byteToHexString(byte b) {
        int n = b;
        if (n < 0)
            n = 256 + n;
        int d1 = n / 16;
        int d2 = n % 16;
        return hexDigits[d1] + hexDigits[d2];
    }

    public static boolean isContainsChinese(String str) {
        Matcher matcher = Pattern.compile("[\u4E00-\u9FA50]").matcher(str);
        boolean flg = false;
        if (matcher.find()) {
            flg = true;
        }
        return flg;
    }

    public static boolean needUpdate(String localCode, String serverCode) {
        try {
            if (isNotTrimBlanks(localCode, serverCode)) {
                String[] localNumbers = localCode.split(".");
                String[] serverNumbers = serverCode.split(".");
                int local = 0, server = 0;
                for (int i = 0; i < localNumbers.length; i++) {
                    if (i == 0) {
                        local += Integer.parseInt(localNumbers[i]) * 1000000;
                    } else if (i == 1) {
                        local += Integer.parseInt(localNumbers[i]) * 100000;
                    } else if (i == 2) {
                        local += Integer.parseInt(localNumbers[i]) * 10000;
                    } else if (i == 3) {
                        local += Integer.parseInt(localNumbers[i]) * 1000;
                    } else {
                        local += Integer.parseInt(localNumbers[i]);
                    }
                }
                for (int i = 0; i < serverNumbers.length; i++) {
                    if (i == 0) {
                        server += Integer.parseInt(serverNumbers[i]) * 1000000;
                    } else if (i == 1) {
                        server += Integer.parseInt(serverNumbers[i]) * 100000;
                    } else if (i == 2) {
                        server += Integer.parseInt(serverNumbers[i]) * 10000;
                    } else if (i == 3) {
                        server += Integer.parseInt(serverNumbers[i]) * 1000;
                    } else {
                        server += Integer.parseInt(serverNumbers[i]);
                    }
                }
                return server > local;
            }
        } catch (Exception e) {}
        return false;
    }

}