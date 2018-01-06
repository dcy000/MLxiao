package com.witspring.model.entity;

import com.witspring.model.Constants;
import com.witspring.util.CommUtil;
import com.witspring.util.StringUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 查询对象，家属成员
 */
public class Member implements Serializable {

    private static final long serialVersionUID = -439087630782415987L;
    private long id;
    private String name;
    private String photo;
    private int sex;// 1男，2女
    private int ageMonths;
    private long timestamp;// 时间戳
    public boolean checked;

    public Member() {}

    public Member(int sex, int ageMonths, String name, long timestamp) {
        this.ageMonths = ageMonths;
        this.timestamp = timestamp;
        this.name = name;
        this.sex = sex;
    }

    public static Member parseMember(String json) {
        try {
            JSONObject jObj = new JSONObject(json);
            Member member = new Member();
            member.setSex(jObj.optInt("sex"));
            member.setAgeMonths(jObj.optInt("age"));
            member.setName(jObj.optString("name"));
            member.setPhoto(jObj.optString("photo"));
            return member;
        } catch (Exception e) {
            return null;
        }
    }

    public static List<Member> parseMemberList(String json) {
        List<Member> members = new ArrayList<>();
        try {
            JSONArray jArr = new JSONArray(json);
            if (CommUtil.notEmpty(jArr)) {
                for (int i = 0; i < jArr.length(); i++) {
                    JSONObject jObj = jArr.optJSONObject(i);
                    Member member = new Member();
                    member.setSex(jObj.optInt("sex"));
                    member.setAgeMonths(jObj.optInt("age"));
                    member.setName(jObj.optString("name"));
                    member.setPhoto(jObj.optString("photo"));
                    members.add(member);
                }
            }
        } catch (Exception e) {}
        return members;
    }

    public boolean isEqual(Member other) {
        return timestamp == other.timestamp;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        if (StringUtil.isTrimBlank(name)) {
            if (sex != Constants.GENDER_NULL) {
                name = sex == Constants.GENDER_MAN ? "男 " : "女 ";
            }
            if (ageMonths != Constants.AGE_NULL) {
                String age;
                if (ageMonths < 12) {
                    age = ageMonths + "个月";
                } else if (ageMonths < 36 && ageMonths >= 12) {
                    age = ageMonths / 12 + "岁";
                    if (ageMonths % 12 != 0) {
                        age = ageMonths / 12 + "岁" + ageMonths % 12 +"月";
                    }
                } else {
                    age = ageMonths / 12 + "岁";
                }
                name += age;
            }
        }
        return name;
    }

    public String getSexAndAgeStr() {
        String text = "";
        if (sex != Constants.GENDER_NULL) {
            text = sex == Constants.GENDER_MAN ? "男 " : "女 ";
        }
        if (ageMonths != Constants.AGE_NULL) {
            String age;
            if (ageMonths < 12) {
                age = ageMonths + "个月";
            } else if (ageMonths < 36 && ageMonths >= 12) {
                age = ageMonths / 12 + "岁";
                if (ageMonths % 12 != 0) {
                    age = ageMonths / 12 + "岁" + ageMonths % 12 +"月";
                }
            } else {
                age = ageMonths / 12 + "岁";
            }
            text += age;
        }
        return text;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public int getSex() {
        return sex;
    }

    public int getAgeMonths() {
        return ageMonths;
    }

    public void setAgeMonths(int ageMonths) {
        this.ageMonths = ageMonths;
    }

    public int getAgeRange() {
        if (ageMonths < 0) {
            return -1;
        } else if (ageMonths >= 0 && ageMonths < 4) {
            return 1;
        } else if (ageMonths < 36) {
            return 2;
        } else if (ageMonths < 120) {
            return 3;
        } else if (ageMonths < 216) {
            return 4;
        } else if (ageMonths < 600) {
            return 5;
        } else if (ageMonths < 780) {
            return 6;
        } else if (ageMonths < 1116) {
            return 7;
        }
        return -1;
    }

    public String getAgeStr() {
        return ageMonths == 0 ? "0.0" : ageMonths + "";
    }
    public static String getAgeStr(int ageMonths) {
        return ageMonths == 0 ? "0.0" : ageMonths + "";
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

}