package com.zane.androidupnpdemo.base_information_of_residents;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.gson.Gson;
import com.gzq.administrator.lib_common.base.CommonBaseActivity;
import com.gzq.administrator.lib_common.utils.ToastTool;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.zane.androidupnpdemo.R;
import com.zane.androidupnpdemo.bean.MyBaseInformation;
import com.zane.androidupnpdemo.bean.PageInfo;
import com.zane.androidupnpdemo.bean.PageInformation;
import com.zane.androidupnpdemo.net.HYKD_API;
import com.zane.androidupnpdemo.utils.AesUtil;
import com.zane.androidupnpdemo.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gzq on 2018/4/16.
 */

public class HY_HealthArchivesActivity extends CommonBaseActivity {
    private RadioButton mRbBuildInfo;
    private RadioButton mRbBaseInfo;
    private RadioButton mRbSignInfo;
    private RadioGroup mRgHealthRecord;
    private RecyclerView mHyHealthInformation;
    private MyBaseInformation myBaseInformation;
    private List<PageInfo> pageInfos;
    private BaseQuickAdapter<PageInfo, BaseViewHolder> adapter;
    private String signDoctorName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hy_activity_health_archives);
        mTitleText.setText("健康档案");
        initView();
        setAdapter();
        getBuildInfo();
    }


    private void setAdapter() {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        mHyHealthInformation.setLayoutManager(manager);
        mHyHealthInformation.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mHyHealthInformation.setAdapter(adapter = new BaseQuickAdapter<PageInfo, BaseViewHolder>(R.layout.hy_item_base_info, pageInfos) {
            @Override
            protected void convert(BaseViewHolder helper, PageInfo item) {
                helper.setText(R.id.title, item.title);
                helper.setText(R.id.content, item.content);
            }
        });
    }

    private void getBuildInfo() {
        String param = "";
        try {
            param = AesUtil.urlEncode(Utils.parseCurrentTime2String() + "_" + HYKD_API.RESIDENT_NAME + "_" + HYKD_API.RESIDENT_IDCARD, AesUtil.KeyCode);
            Log.e("加密前", "加密前的参数：" + Utils.parseCurrentTime2String() + "_" + HYKD_API.RESIDENT_NAME + "_" + HYKD_API.RESIDENT_IDCARD);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.e("参数", "请求的参数" + param);
        OkGo.<String>post(HYKD_API.GET_RESIDENT_INFORMATION)
                .params("param", param)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Log.e("请求结果", response.body());
                        try {
                            JSONObject object = new JSONObject(response.body());
                            if (object.optInt("code") == -1) {
                                ToastTool.showShort("网络请求出现错误");
                                return;
                            }
                            if (object.optInt("code") == 200) {
                                JSONObject data = object.optJSONObject("data");
                                myBaseInformation = new Gson().fromJson(data.toString(), MyBaseInformation.class);
                                dealBuildInfoData();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

        try {
            param = AesUtil.urlEncode(Utils.parseCurrentTime2String() + "_" + HYKD_API.RESIDENT_NAME + "_" + HYKD_API.RESIDENT_IDCARD, AesUtil.KeyCode);
            Log.e("加密前", "加密前的参数：" + Utils.parseCurrentTime2String() + "_" + HYKD_API.RESIDENT_NAME + "_" + HYKD_API.RESIDENT_IDCARD);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.e("参数", "请求的参数" + param);
        OkGo.<String>post(HYKD_API.RESIDENT_SERVER_HISTORY)
                .params("param", param)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Log.e("请求结果", response.body());
                        try {
                            JSONObject object = new JSONObject(response.body());
                            if (object.optInt("code") == -1) {
                                ToastTool.showShort("网络请求出现错误");
                                return;
                            }
                            if (object.optInt("code") == 200) {
                                JSONObject data = object.optJSONObject("data");
                                JSONObject pageInfo = data.optJSONObject("pageInfo");
                                PageInformation pageInformation = new Gson().fromJson(pageInfo.toString(), PageInformation.class);
                                signDoctorName = pageInformation.list.get(0).doctorName;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void dealBuildInfoData() {
        if (myBaseInformation != null) {
            pageInfos.clear();
            pageInfos.add(new PageInfo("姓名", myBaseInformation.ResidentRecords.name));
            pageInfos.add(new PageInfo("联系电话", myBaseInformation.ResidentRecords.contactsPhone));
            pageInfos.add(new PageInfo("现住地址", myBaseInformation.ResidentRecords.addressCurrent));
            pageInfos.add(new PageInfo("户籍地址", myBaseInformation.ResidentRecords.address));
            pageInfos.add(new PageInfo("乡镇", myBaseInformation.ResidentRecords.townName));
            pageInfos.add(new PageInfo("村组", myBaseInformation.ResidentRecords.villageName));
            pageInfos.add(new PageInfo("建档单位", myBaseInformation.ResidentRecords.recordUnit));
            pageInfos.add(new PageInfo("建档人", myBaseInformation.ResidentRecords.recordName));
            pageInfos.add(new PageInfo("负责医生", myBaseInformation.ResidentRecords.doctorName));
            if (!TextUtils.isEmpty(myBaseInformation.ResidentRecords.recordDate)) {
                String time = myBaseInformation.ResidentRecords.recordDate.split("\\s+")[0];
                String[] time_s = time.split("-");
                pageInfos.add(new PageInfo("建档日期", time_s[0] + "年" + time_s[1] + "月" + time_s[2] + "日"));
            }
            adapter.notifyDataSetChanged();
        }
    }

    private void dealBaseInfoData() {
        if (myBaseInformation != null) {
            pageInfos.clear();
            pageInfos.add(new PageInfo("档案编号", myBaseInformation.ResidentRecords.healthFileCode));
            pageInfos.add(new PageInfo("性别", myBaseInformation.ResidentRecords.sex.equals("1") ? "男" : "女"));

            String birth=myBaseInformation.ResidentRecords.birthday;
            if (!TextUtils.isEmpty(birth)) {
                String[] birth_s = birth.split("\\s+")[0].split("-");
                pageInfos.add(new PageInfo("出生日期", birth_s[0] + "年" + birth_s[1] + "月" + birth_s[2] + "日"));
            }
            pageInfos.add(new PageInfo("身份证号码", myBaseInformation.ResidentRecords.identityCard));
            pageInfos.add(new PageInfo("工作单位", myBaseInformation.ResidentRecords.workAddress));
            pageInfos.add(new PageInfo("户籍单位", myBaseInformation.ResidentRecords.residentType == 1 ? "城镇" : "农村"));
            String family = myBaseInformation.ResidentRecords.familyRelation;
            String family_s = "";
            if (!TextUtils.isEmpty(family)) {
                switch (Integer.parseInt(family)) {
                    case 0:
                        family_s = "户主";
                        break;
                    case 1:
                        family_s = "配偶";
                        break;
                    case 2:
                        family_s = "儿子";
                        break;
                    case 3:
                        family_s = "女儿";
                        break;
                    case 4:
                        family_s = "孙子、孙女或外孙子、外孙女";
                        break;
                    case 5:
                        family_s = "父母";
                        break;
                    case 6:
                        family_s = "祖父母或外祖父母";
                        break;
                    case 7:
                        family_s = "兄、弟、姐、妹";
                        break;
                    case 8:
                        family_s = "其他";
                        break;
                }
            }
            pageInfos.add(new PageInfo("社会关系", family_s));
            pageInfos.add(new PageInfo("所属人群", ""));
            pageInfos.add(new PageInfo("联系人", myBaseInformation.ResidentRecords.contacts));
            pageInfos.add(new PageInfo("联系人电话", myBaseInformation.ResidentRecords.contactsPhone));
            pageInfos.add(new PageInfo("常住类型", myBaseInformation.ResidentRecords.addressType == 1 ? "户籍" : "非户籍"));
            pageInfos.add(new PageInfo("民族", myBaseInformation.ResidentRecords.nation));
            String edu = myBaseInformation.ResidentRecords.education;
            String edu_tips = "";
            if (!TextUtils.isEmpty(edu)) {
                switch (Integer.parseInt(edu)) {
                    case 1:
                        edu_tips = "文盲或者半文盲";
                        break;
                    case 2:
                        edu_tips = "小学";
                        break;
                    case 3:
                        edu_tips = "初中";
                        break;
                    case 4:
                        edu_tips = "高中";
                        break;
                    case 5:
                        edu_tips = "大学专科和专科学校";
                        break;
                    case 6:
                        edu_tips = "不详";
                        break;
                    case 7:
                        edu_tips = "研究生";
                        break;
                    case 8:
                        edu_tips = "大学本科";
                        break;
                }
            }
            pageInfos.add(new PageInfo("文化程度", edu_tips));
            String prof = myBaseInformation.ResidentRecords.profession;
            String prof_s = "";
            if (!TextUtils.isEmpty(prof)) {
                switch (Integer.parseInt(prof)) {
                    case 1:
                        prof_s = "国家机关、党群组织、企业、事业单位负责人";
                        break;
                    case 2:
                        prof_s = "专业技术人员";
                        break;
                    case 3:
                        prof_s = "办事人员和有关人员";
                        break;
                    case 4:
                        prof_s = "商业、服务业人员";
                        break;
                    case 5:
                        prof_s = "农、林、牧、渔、水利业生产人员";
                        break;
                    case 6:
                        prof_s = "生产、运输设备操作人员及有关人员";
                        break;
                    case 7:
                        prof_s = "军人";
                        break;
                    case 8:
                        prof_s = "不便分类的其他从业人员";
                        break;
                    case 9:
                        prof_s = "无职业";
                        break;
                }
            }
            pageInfos.add(new PageInfo("职业", prof_s));
            String marita = myBaseInformation.ResidentRecords.maritalStatus;
            String marita_s = "";
            if (!TextUtils.isEmpty(marita)) {
                switch (Integer.parseInt(marita)) {
                    case 1:
                        marita_s = "未婚";
                        break;
                    case 2:
                        marita_s = "已婚";
                        break;
                    case 3:
                        marita_s = "丧偶";
                        break;
                    case 4:
                        marita_s = "离婚";
                        break;
                    case 5:
                        marita_s = "未说明的婚姻状况";
                        break;
                }
            }
            pageInfos.add(new PageInfo("婚姻状况", marita_s));
            pageInfos.add(new PageInfo("当前状态", myBaseInformation.ResidentRecords.status == 0 ? "不正常" : "正常"));
            pageInfos.add(new PageInfo("来源方式", myBaseInformation.ResidentRecords.sourceCode + ""));
            pageInfos.add(new PageInfo("复核人", myBaseInformation.ResidentRecords.checkName));
            String checkTi=myBaseInformation.ResidentRecords.checkDate;
            if (!TextUtils.isEmpty(checkTi)) {
                String[] checkTi_s = checkTi.split("\\s+")[0].split("-");
                pageInfos.add(new PageInfo("复核时间", checkTi_s[0] + "年" + checkTi_s[1] + "月" + checkTi_s[2] + "日"));
            }
            String changeTi=myBaseInformation.ResidentRecords.changeDate;
            if (!TextUtils.isEmpty(changeTi)) {
                String[] changeTi_s = changeTi.split("\\s+")[0].split("-");
                pageInfos.add(new PageInfo("变动时间", changeTi_s[0] + "年" + changeTi_s[1] + "月" + changeTi_s[2] + "日"));
            }
            String registTi=myBaseInformation.ResidentRecords.registerDate;
            if (!TextUtils.isEmpty(registTi)) {
                String[] registTi_s = registTi.split("\\s+")[0].split("-");
                pageInfos.add(new PageInfo("登记时间", registTi_s[0] + "年" + registTi_s[1] + "月" + registTi_s[2] + "日"));
            }
            pageInfos.add(new PageInfo("中医建档人", myBaseInformation.ResidentRecords.tcmName));
            String tcmTi=myBaseInformation.ResidentRecords.tcmDate;
            if (!TextUtils.isEmpty(tcmTi)) {
                String[] tcmTi_s = tcmTi.split("\\s+")[0].split("-");
                pageInfos.add(new PageInfo("中医建档时间", tcmTi_s[0] + "年" + tcmTi_s[1] + "月" + tcmTi_s[2] + "日"));
            }
            pageInfos.add(new PageInfo("人户关系", myBaseInformation.ResidentRecords.houseRalation + ""));
            pageInfos.add(new PageInfo("流动人口", myBaseInformation.ResidentRecords.migrant == 0 ? "非流动人口" : "流动人口"));
            pageInfos.add(new PageInfo("档案修改医生", myBaseInformation.ResidentRecords.lastChangeDoctorName));
            adapter.notifyDataSetChanged();
        }
    }

    private void dealSignInfoData() {
        pageInfos.clear();
        pageInfos.add(new PageInfo("签约信息",myBaseInformation.isSign));
        pageInfos.add(new PageInfo("签约医生",signDoctorName));
        adapter.notifyDataSetChanged();

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        int i = v.getId();
        if (i == R.id.rb_build_info) {
            dealBuildInfoData();

        } else if (i == R.id.rb_base_info) {
            dealBaseInfoData();

        } else if (i == R.id.rb_sign_info) {
            dealSignInfoData();

        }
    }

    private void initView() {
        mRbBuildInfo = (RadioButton) findViewById(R.id.rb_build_info);
        mRbBuildInfo.setOnClickListener(this);
        mRbBaseInfo = (RadioButton) findViewById(R.id.rb_base_info);
        mRbBaseInfo.setOnClickListener(this);
        mRbSignInfo = (RadioButton) findViewById(R.id.rb_sign_info);
        mRbSignInfo.setOnClickListener(this);
        mRgHealthRecord = (RadioGroup) findViewById(R.id.rg_health_record);
        mHyHealthInformation = (RecyclerView) findViewById(R.id.hy_health_information);
        pageInfos = new ArrayList<>();
        mRgHealthRecord.check(R.id.rb_build_info);
    }
}
