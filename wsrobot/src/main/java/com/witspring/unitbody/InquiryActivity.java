package com.witspring.unitbody;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.witspring.base.BaseActivity;
import com.witspring.mlrobot.R;
import com.witspring.model.entity.Member;
import com.witspring.unitbody.adapter.MultipleItemAdapter;
import com.witspring.unitbody.contract.InquiryContract;
import com.witspring.unitbody.model.entity.ChooseItem;
import com.witspring.unitbody.model.entity.DiseaseItem;
import com.witspring.unitbody.model.entity.InquiryChatMessage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class InquiryActivity extends BaseActivity implements InquiryContract.View  {

    private Member member;

    private String mainSymptom;
    private InquiryContract.Presenter presenter;
    private MultipleItemAdapter multipleItemAdapter;
    private List<InquiryChatMessage> chatTotalMessages;
    private String sessionId;

    private Toolbar toolbar;
    private RecyclerView lvContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wsbody_activity_inquiry);
        toolbar = findViewById(R.id.toolbar);
        setToolbar(toolbar, "问诊");
        Button btnRight = toolbar.findViewById(R.id.btnRight);
        btnRight.setText("提前结束问诊");
        btnRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog("结束问诊提示", "确定要提前结束问诊吗？", "是", "否", true, new DialogActionListener() {
                    @Override
                    public void onPositive() {
                        presenter.chatResultBackNow("2", sessionId);
                    }
                });
            }
        });

        lvContent = findViewById(R.id.lvContent);
        member = (Member) getIntent().getSerializableExtra("member");
        mainSymptom = getIntent().getStringExtra("symptom");

        presenter = new InquiryContract.Presenter(this);
        presenter.chatInit("0", member.getSex(), member.getAgeMonths(), mainSymptom);
        multipleItemAdapter = new MultipleItemAdapter(this,(InquiryActivity)this);
        chatTotalMessages = new ArrayList<>();

        lvContent.setLayoutManager(new LinearLayoutManager(this));
        lvContent.setAdapter(multipleItemAdapter);

        InquiryChatMessage inquiryChatMessage =new InquiryChatMessage();
        inquiryChatMessage.setTextContent(member.getSexAndAgeStr() + ", " + mainSymptom);
        inquiryChatMessage.setBoxType(MultipleItemAdapter.ITEM_TYPE.ITEM_TYPE_USER_ANSWER_TEXT.ordinal());
        chatTotalMessages.add(inquiryChatMessage);
        multipleItemAdapter.setData(chatTotalMessages);
    }

    public void onRadioChecked(String name, String location){
        InquiryChatMessage inquiryChatMessage =new InquiryChatMessage();
        inquiryChatMessage.setTextContent(name);
        inquiryChatMessage.setBoxType(MultipleItemAdapter.ITEM_TYPE.ITEM_TYPE_USER_ANSWER_TEXT.ordinal());
        chatTotalMessages.add(inquiryChatMessage);
        multipleItemAdapter.setData(chatTotalMessages);

        presenter.chatNextStep("1",sessionId,location,name);
    }

    public void onCheckBtnClick(String name,String location){
        InquiryChatMessage inquiryChatMessage =new InquiryChatMessage();
        inquiryChatMessage.setTextContent(name);
        inquiryChatMessage.setBoxType(MultipleItemAdapter.ITEM_TYPE.ITEM_TYPE_USER_ANSWER_TEXT.ordinal());
        chatTotalMessages.add(inquiryChatMessage);
        multipleItemAdapter.setData(chatTotalMessages);

        presenter.chatNextStep("1",sessionId,location,name);
    }

    @Override
    public void chatInit(final String chatContent) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject chatJsonObj = new JSONObject(chatContent);
                    int status = chatJsonObj.getInt("status");
                    sessionId = chatJsonObj.getString("session_id");
                    System.out.println("sessionId:"+sessionId);
                    if(status == 200){
                        dealSucceedResult(chatJsonObj);
                    }else{
                        showToastShort("出错了啦");
                    }
                } catch (Exception e) {
                }
            }
        });

    }

    public void end() {
        toolbar.findViewById(R.id.btnRight).setOnClickListener(null);
    }

    @Override
    public void chatNextStep(final String chatNextStepContent) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject chatJsonObj = new JSONObject(chatNextStepContent);
                    int status = chatJsonObj.getInt("status");
                    if(status == 200){
                        dealSucceedResult(chatJsonObj);
                    }else{
                        showToastShort("出错了啦");
                    }
                } catch (Exception e) {
                }
            }
        });
    }

    @Override
    public void chatResultBackNow(final String chatResultBackNowContent) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject chatJsonObj = new JSONObject(chatResultBackNowContent);
                    int status = chatJsonObj.getInt("status");
                    if(status == 200){
                        dealSucceedResult(chatJsonObj);
                    }else{
                        showToastShort("出错了啦");
                    }
                } catch (Exception e) {
                }
            }
        });
    }

    public void dealSucceedResult(JSONObject chatJsonObj){
        try {

            String option = chatJsonObj.getString("option");
            System.out.println("option:"+option);
            if(option.equals("0")){
                //单选
                String question = chatJsonObj.getString("question");
                System.out.println("question:"+question);
                String location = chatJsonObj.getString("location");
                System.out.println("location:"+location);
                InquiryChatMessage inquiryChatMessage =new InquiryChatMessage();
                inquiryChatMessage.setLocation(location);
                inquiryChatMessage.setBoxTitle(question);
                List<ChooseItem> tempButtonList = new ArrayList<>();
                JSONArray answerListJsonArray = chatJsonObj.optJSONArray("answer_list");
                for (int i = 0; i < answerListJsonArray.length(); i++) {
                    JSONObject jsonObject = answerListJsonArray.optJSONObject(i);
                    String name = jsonObject.optString("name");
                    tempButtonList.add(new ChooseItem(false,name,false));
                }
                inquiryChatMessage.setRadioButtons(tempButtonList);
                inquiryChatMessage.setBoxType(MultipleItemAdapter.ITEM_TYPE.ITEM_TYPE_COMPUTER_ASK_RADIO.ordinal());
                chatTotalMessages.add(inquiryChatMessage);
            }else if(option.equals("1")){
                //多选
                String question = chatJsonObj.getString("question");
                System.out.println("question:"+question);
                String location = chatJsonObj.getString("location");
                System.out.println("location:"+location);
                InquiryChatMessage inquiryChatMessage =new InquiryChatMessage();
                inquiryChatMessage.setLocation(location);
                inquiryChatMessage.setBoxTitle(question);
                List<ChooseItem> tempButtonList = new ArrayList<>();
                JSONArray answerListJsonArray = chatJsonObj.optJSONArray("answer_list");
                for (int i = 0; i < answerListJsonArray.length(); i++) {
                    JSONObject jsonObject = answerListJsonArray.optJSONObject(i);
                    String name = jsonObject.optString("name");
                    int exclusive = jsonObject.optInt("exclusive");
                    tempButtonList.add(new ChooseItem(false,name,false,exclusive));
                }
                inquiryChatMessage.setCheckboxButtons(tempButtonList);
                inquiryChatMessage.setBoxType(MultipleItemAdapter.ITEM_TYPE.ITEM_TYPE_COMPUTER_ASK_CHECKBOX.ordinal());
                chatTotalMessages.add(inquiryChatMessage);
            }else if(option.equals("2")){
                //疾病列表
                InquiryChatMessage inquiryChatMessage =new InquiryChatMessage();
                List<DiseaseItem> diseaseItemList = new ArrayList<>();
                JSONArray diseaseListJsonArray = chatJsonObj.optJSONArray("di_list");
                for (int i = 0; i < diseaseListJsonArray.length(); i++) {
                    JSONObject jsonObject = diseaseListJsonArray.optJSONObject(i);
                    String name = jsonObject.optString("name");
                    int Id = jsonObject.optInt("name_id");
                    String prob = jsonObject.optString("prob");
                    diseaseItemList.add(new DiseaseItem(name,Id,Float.parseFloat(prob)));
                }
                inquiryChatMessage.setDiseaseList(diseaseItemList);
                inquiryChatMessage.setBoxType(MultipleItemAdapter.ITEM_TYPE.ITEM_TYPE_COMPUTER_DISEASE_RESULT.ordinal());
                chatTotalMessages.add(inquiryChatMessage);
            }

            multipleItemAdapter.setData(chatTotalMessages);
            lvContent.smoothScrollToPosition(chatTotalMessages.size());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
