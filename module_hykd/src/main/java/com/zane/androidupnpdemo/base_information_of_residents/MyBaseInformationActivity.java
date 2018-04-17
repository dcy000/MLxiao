package com.zane.androidupnpdemo.base_information_of_residents;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;

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
import com.zane.androidupnpdemo.net.HYKD_API;
import com.zane.androidupnpdemo.utils.AesUtil;
import com.zane.androidupnpdemo.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gzq on 2018/4/13.
 */

public class MyBaseInformationActivity extends CommonBaseActivity {
    private MyBaseInformation myBaseInformation;
    private RecyclerView mHyUserBaseInformationList;
    private List<PageInfo> pageInfos;
    private BaseQuickAdapter<PageInfo, BaseViewHolder> adapter;

    public static void startActivity(Context context, Class<?> clazz) {
        context.startActivity(new Intent(context, clazz));

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hy_activity_mybase_information);
        initView();
        setRecycleview();
        getData();
    }

    private void setRecycleview() {
        LinearLayoutManager manager=new LinearLayoutManager(this);
        mHyUserBaseInformationList.setLayoutManager(manager);
        mHyUserBaseInformationList.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        mHyUserBaseInformationList.setAdapter(adapter=new BaseQuickAdapter<PageInfo,BaseViewHolder>(R.layout.hy_item_base_information,pageInfos) {
            @Override
            protected void convert(BaseViewHolder helper, PageInfo item) {
                helper.setText(R.id.hy_tv_base_title,item.title);
                helper.setText(R.id.hy_tv_base_content,item.content);
            }
        });
    }

    private void getData() {
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
                                dealData();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void dealData() {
        if (myBaseInformation!=null){
            pageInfos.add(new PageInfo("姓名",myBaseInformation.ResidentRecords.name));
            pageInfos.add(new PageInfo("性别",myBaseInformation.ResidentRecords.sex.equals("1")?"男":"女"));
            pageInfos.add(new PageInfo("民族",myBaseInformation.ResidentRecords.nation));
            pageInfos.add(new PageInfo("出生日期",myBaseInformation.ResidentRecords.birthday));
            pageInfos.add(new PageInfo("身份证号码",myBaseInformation.ResidentRecords.identityCard));
            pageInfos.add(new PageInfo("联系电话",myBaseInformation.ResidentRecords.contactsPhone));
            pageInfos.add(new PageInfo("邮政编码",myBaseInformation.ResidentRecords.name));
            pageInfos.add(new PageInfo("户籍地址",myBaseInformation.ResidentRecords.postcode));
            String edu=myBaseInformation.ResidentRecords.education;
            String edu_tips ="";
            if (!TextUtils.isEmpty(edu)){
                switch (Integer.parseInt(edu)){
                    case 1:
                        edu_tips="文盲或者半文盲";
                        break;
                    case 2:
                        edu_tips="小学";
                        break;
                    case 3:
                        edu_tips="初中";
                        break;
                    case 4:
                        edu_tips="高中";
                        break;
                    case 5:
                        edu_tips="大学专科和专科学校";
                        break;
                    case 6:
                        edu_tips="不详";
                        break;
                    case 7:
                        edu_tips="研究生";
                        break;
                    case 8:
                        edu_tips="大学本科";
                        break;
                }
            }
            String prof=myBaseInformation.ResidentRecords.profession;
            String prof_s="";
            if (!TextUtils.isEmpty(prof)){
                switch (Integer.parseInt(prof)){
                    case 1:
                        prof_s="国家机关、党群组织、企业、事业单位负责人";
                        break;
                    case 2:
                        prof_s="专业技术人员";
                        break;
                    case 3:
                        prof_s="办事人员和有关人员";
                        break;
                    case 4:
                        prof_s="商业、服务业人员";
                        break;
                    case 5:
                        prof_s="农、林、牧、渔、水利业生产人员";
                        break;
                    case 6:
                        prof_s="生产、运输设备操作人员及有关人员";
                        break;
                    case 7:
                        prof_s="军人";
                        break;
                    case 8:
                        prof_s="不便分类的其他从业人员";
                        break;
                    case 9:
                        prof_s="无职业";
                        break;
                }
            }
            pageInfos.add(new PageInfo("文化程度",edu_tips));
            pageInfos.add(new PageInfo("工作单位",myBaseInformation.ResidentRecords.workAddress));
            pageInfos.add(new PageInfo("详细地址",""));
            pageInfos.add(new PageInfo("建档单位",myBaseInformation.ResidentRecords.recordUnit));
            pageInfos.add(new PageInfo("现居地",myBaseInformation.ResidentRecords.addressCurrent));
            pageInfos.add(new PageInfo("职业",prof_s));
            String family=myBaseInformation.ResidentRecords.familyRelation;
            String family_s="";
            if (!TextUtils.isEmpty(family)){
                switch (Integer.parseInt(family)){
                    case 0:
                        family_s="户主";
                        break;
                    case 1:
                        family_s="配偶";
                        break;
                    case 2:
                        family_s="儿子";
                        break;
                    case 3:
                        family_s="女儿";
                        break;
                    case 4:
                        family_s="孙子、孙女或外孙子、外孙女";
                        break;
                    case 5:
                        family_s="父母";
                        break;
                    case 6:
                        family_s="祖父母或外祖父母";
                        break;
                    case 7:
                        family_s="兄、弟、姐、妹";
                        break;
                    case 8:
                        family_s="其他";
                        break;
                }
            }
            pageInfos.add(new PageInfo("社会关系",family_s));
            String marita=myBaseInformation.ResidentRecords.maritalStatus;
            String marita_s="";
            if (!TextUtils.isEmpty(marita)){
                switch (Integer.parseInt(marita)){
                    case 1:
                        marita_s="未婚";
                        break;
                    case 2:
                        marita_s="已婚";
                        break;
                    case 3:
                        marita_s="丧偶";
                        break;
                    case 4:
                        marita_s="离婚";
                        break;
                    case 5:
                        marita_s="未说明的婚姻状况";
                        break;
                }
            }
            pageInfos.add(new PageInfo("婚姻状况",marita_s));
            pageInfos.add(new PageInfo("建档日期",myBaseInformation.ResidentRecords.recordDate.split("\\s+")[0]));
            adapter.notifyDataSetChanged();
        }
    }

    private void initView() {
        mHyUserBaseInformationList = (RecyclerView) findViewById(R.id.hy_user_base_information_list);
        pageInfos=new ArrayList<>();
    }
}
