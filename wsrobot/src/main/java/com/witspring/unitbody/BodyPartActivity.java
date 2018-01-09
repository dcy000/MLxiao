package com.witspring.unitbody;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.witspring.base.BaseActivity;
import com.witspring.mlrobot.R;
import com.witspring.mlrobot.databinding.WsbodyActivityBodyPartBinding;
import com.witspring.model.ApiCallback;
import com.witspring.model.Constants;
import com.witspring.model.entity.Member;
import com.witspring.model.entity.Result;
import com.witspring.unitbody.model.BodyApi;
import com.witspring.unitbody.model.entity.Organ;
import com.witspring.util.CommUtil;
import com.witspring.util.DensityUtil;
import com.witspring.util.StringUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Created by Goven on 2018/1/2 下午7:40
 * @email gxl3999@gmail.com
 */
public class BodyPartActivity extends BaseActivity {

    private WsbodyActivityBodyPartBinding binding;
    private int partId, partIndex;
    private Member member;
    private BodyApi api;
    private BodyOrganAdapter organsAdapter;

    // 二级器官列表
    private List<Organ> secondOrgans;
    /**
     * key为二级器官索引，value为三级器官列表
     */
    private Map<Integer, List<Organ>> thirdOrgans;
    /**
     * 1.key为二级器官索引，value为二级器官的症状列表；
     * 2.key为二级器官索引乘以100加上100加上三级器官索引，value为三级器官的症状列表
     */
    private Map<Integer, List<String[]>> symptomsOfOrgan;
    /**
     * 1.索引若为二级器官，直接是二级器官列表的索引
     * 2.索引若为三级器官，是二级器官索引乘以100加上100加上三级器官索引
     * 3.索引若为-1，则表示一级器官
     */
    private int selectedOrganIndex = -1, opendedGroupIndex = -1;
    private String[] partNames = {"头部", "颈部", "胸部", "腹部", "生殖器", "下肢", "上肢", "枕部", "后颈部", "背部", "臀部", "下肢", "上肢", "全身"};
    private int[] bodyPartImgs = {R.drawable.wsbody_selector_body_part_head, R.drawable.wsbody_selector_body_part_neck, R.drawable.wsbody_selector_body_part_chest_woman,
            R.drawable.wsbody_selector_body_part_abdomen, R.drawable.wsbody_selector_body_part_genital, R.drawable.wsbody_selector_body_part_lower, R.drawable.wsbody_selector_body_part_upper,
            R.drawable.wsbody_selector_body_part_head_back, R.drawable.wsbody_selector_body_part_neck_back, R.drawable.wsbody_selector_body_part_back, R.drawable.wsbody_selector_body_part_butt_woman,
            R.drawable.wsbody_selector_body_part_lower, R.drawable.wsbody_selector_body_part_upper, R.drawable.wsbody_selector_body_part_all};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.binding = DataBindingUtil.setContentView(this, R.layout.wsbody_activity_body_part);
        partIndex = getIntent().getIntExtra("partIndex", 13);
        partId = getIntent().getIntExtra("partId", 0);
        member = (Member) getIntent().getSerializableExtra("member");
        setToolbar(binding.toolbar, "选择" + partNames[partIndex]  + "症状");
        Button btnRight = binding.toolbar.findViewById(R.id.btnRight);
        btnRight.setText(member.getName());
        int resId = bodyPartImgs[partIndex];
        if (member.getSex() == Constants.GENDER_MAN) {
            if (partIndex == 2) {
                resId = R.drawable.wsbody_selector_body_part_chest_man;
            } else if (partIndex == 3){
                resId = R.drawable.wsbody_selector_body_part_abdomen_man;
            } else if (partIndex == 9) {
                resId = R.drawable.wsbody_selector_body_part_back_man;
            } else if (partIndex == 10) {
                resId = R.drawable.wsbody_selector_body_part_butt_man;
            }
        }
        Drawable drawable = getResources().getDrawable(resId);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        binding.ctvName.setCompoundDrawables(null, drawable, null, null);
        binding.ctvName.setText(partNames[partIndex] + "相关");
        binding.ivIcon.setVisibility(View.GONE);
        initView();
    }

    private void initView() {
        startLoading(null);
        api = new BodyApi();
        binding.elvContent.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int position, long id) {
                selectedOrganIndex = position;
                if (parent.isGroupExpanded(position)) {
                    opendedGroupIndex = -1;
                    parent.collapseGroup(position);
                    updateTags();
                } else {
                    opendedGroupIndex = position;
                    binding.ctvName.setChecked(false);
                    Organ organ = secondOrgans.get(position);
                    if (StringUtil.isNotTrimBlank(organ.getIconUrl())) {
                        binding.ivIcon.setVisibility(View.VISIBLE);
                        binding.ivIcon.setImageURI(Uri.parse(organ.getIconUrl()));
                    } else {
                        binding.ivIcon.setVisibility(View.GONE);
                    }
                    if (!CommUtil.notEmpty(thirdOrgans.get(position)) && !CommUtil.notEmpty(symptomsOfOrgan.get(position))) {
                        startLoading(null);
                        updateTags();
                        loadData(organ.getId(), organ.getName());
                    } else {
                        //第二个参数false表示展开时是否触发默认滚动动画
                        parent.expandGroup(position, false);
                        updateTags();
                    }
                }
                return true;
            }
        });
        binding.elvContent.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int position) {
                for (int i = 0; i < organsAdapter.getGroupCount(); i++) {
                    if (i != position && binding.elvContent.isGroupExpanded(i)) {
                        binding.elvContent.collapseGroup(i);
                        binding.elvContent.setSelectedGroup(position);
                    }
                }
            }
        });
        binding.elvContent.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int groupPosition, int childPosition, long l) {
                selectedOrganIndex = (groupPosition + 1) * 100 + childPosition;
                binding.ctvName.setChecked(false);
                Organ organ = thirdOrgans.get(groupPosition).get(childPosition);
                if (StringUtil.isNotTrimBlank(organ.getIconUrl())) {
                    binding.ivIcon.setVisibility(View.VISIBLE);
                    binding.ivIcon.setImageURI(Uri.parse(organ.getIconUrl()));
                } else {
                    binding.ivIcon.setVisibility(View.GONE);
                }
                if (CommUtil.notEmpty(symptomsOfOrgan.get(selectedOrganIndex))) {
                    startLoading(null);
                    loadData(organ.getId(), organ.getName());
                } else {
                    organsAdapter.notifyDataSetChanged();
                    updateTags();
                }
                return false;
            }
        });
        loadData(partId, partNames[partIndex]);
    }

    private void loadData(int organId, String organName) {
        api.organsAndSymptoms(member.getSex(), member.getAgeMonths(), organId, organName, new ApiCallback<Map<String, Object>>() {
            @Override
            public void onResult(Result<Map<String, Object>> result) {
                stopLoading();
                if (result.successed()) {
                    Object organObj = result.getContent().get("organs");
                    Object symptomObj = result.getContent().get("symptoms");
                    List<Organ> organData = null;
                    List<String[]> symptomData = null;
                    if (organObj != null) {
                        organData = (List<Organ>) organObj;
                    }
                    if (symptomObj != null) {
                        symptomData = (List<String[]>) symptomObj;
                    }
                    if (symptomsOfOrgan == null) {
                        symptomsOfOrgan = new HashMap<>();
                    }
                    if (CommUtil.notEmpty(organData)) {
                        if (thirdOrgans == null) {
                            thirdOrgans = new HashMap<>();
                        }
                        if (selectedOrganIndex == -1) {
                            secondOrgans = organData;
                        } else {
                            thirdOrgans.put(selectedOrganIndex, organData);
                        }
                    }
                    if (selectedOrganIndex != -1 || CommUtil.notEmpty(organData)) {
                        if (organsAdapter == null) {
                            organsAdapter = new BodyOrganAdapter();
                            binding.elvContent.setAdapter(organsAdapter);
                        } else {
                            if (opendedGroupIndex >= 0 && !binding.elvContent.isGroupExpanded(opendedGroupIndex)) {
                                binding.elvContent.expandGroup(opendedGroupIndex, false);
                            } else {
                                organsAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                    if (CommUtil.notEmpty(symptomData)) {
                        symptomsOfOrgan.put(selectedOrganIndex, symptomData);
                    }
                    updateTags();
                } else {
                    warningUnknow(result);
                }
            }
        });
    }

    private void updateTags() {
        List<String[]> symptoms = symptomsOfOrgan.get(selectedOrganIndex);
        if (CommUtil.notEmpty(symptoms)) {
            binding.llBlank.setVisibility(View.GONE);
            binding.flowlayout.setVisibility(View.VISIBLE);
            addSymptomTags(symptoms);
        } else {
            if (!isLoading()) {
                binding.llBlank.setVisibility(View.VISIBLE);
            }
            binding.flowlayout.setVisibility(View.INVISIBLE);
        }
        binding.scrollview.scrollTo(0, 0);
    }

    private void addSymptomTags(List<String[]> symptoms) {
        int oldCount = binding.flowlayout.getChildCount();
        int newCount = symptoms.size();
        if (oldCount <= newCount) {
            for (int i = 0; i < newCount; i++) {
                String[] texts = symptoms.get(i);
                if (i < oldCount) {
                    CheckedTextView tvItem = (CheckedTextView) binding.flowlayout.getChildAt(i);
                    tvItem.setTag(texts[0]);
                    tvItem.setText(texts[0]);
                    tvItem.setVisibility(View.VISIBLE);
                } else {
                    binding.flowlayout.addView(getTagView(texts[0]));
                }
            }
        } else {
            for (int i = 0; i < oldCount; i++) {
                if (i < newCount) {
                    String[] texts = symptoms.get(i);
                    CheckedTextView tvItem = (CheckedTextView) binding.flowlayout.getChildAt(i);
                    tvItem.setTag(texts[0]);
                    tvItem.setText(texts[0]);
                    tvItem.setVisibility(View.VISIBLE);
                } else {
                    binding.flowlayout.getChildAt(i).setVisibility(View.GONE);
                }
            }
        }
    }

    private CheckedTextView getTagView(String text) {
        int margin = DensityUtil.dip2px(this, 4);
        CheckedTextView tvItem = new CheckedTextView(this);
        tvItem.setBackgroundResource(R.drawable.wsbody_selector_tag_comm_symptom);
        tvItem.setTextColor(getResources().getColorStateList(R.color.wsbody_selector_comm_symptom));
        tvItem.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelOffset(R.dimen.ws_font_medium));
        tvItem.setPadding(3 * margin, (int)(1.5f * margin), 3 * margin, (int)(1.5f * margin));
        tvItem.setTag(text);
        tvItem.setText(text);
        tvItem.setOnClickListener(listener);
        return tvItem;
    }

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String text = (String) view.getTag();
            Intent intent = new Intent(getContext(), InquiryActivity.class);
            intent.putExtra("symptom", text);
            intent.putExtra("member", member);
            startActivity(intent);
        }
    };

    public void ctvName(View view) {
        if (!binding.ctvName.isChecked()) {
            binding.ivIcon.setVisibility(View.GONE);
            binding.ctvName.setChecked(true);
            selectedOrganIndex = -1;
            opendedGroupIndex = -1;
            updateTags();
            int opendedPostion = -1;
            for (int i = 0; i < organsAdapter.getGroupCount(); i++) {
                if (binding.elvContent.isGroupExpanded(i)) {
                    binding.elvContent.collapseGroup(i);
                    opendedPostion = i;
                }
            }
            if (opendedPostion == -1) {
                organsAdapter.notifyDataSetChanged();
            }
        }
    }

    public void tvSearch(View view) {
    }

    private class BodyOrganAdapter extends BaseExpandableListAdapter {

        @Override
        public int getGroupCount() {
            return secondOrgans.size();
        }

        @Override
        public int getChildrenCount(int groupPos) {
            List<Organ> organs = thirdOrgans.get(groupPos);
            if (organs == null) {
                return 0;
            } else {
                return organs.size();
            }
        }

        @Override
        public Organ getGroup(int position) {
            return secondOrgans.get(position);
        }

        @Override
        public Organ getChild(int groupPos, int childPos) {
            List<Organ> organs = thirdOrgans.get(groupPos);
            return organs.get(childPos);
        }

        @Override
        public long getGroupId(int groupPos) {
            return groupPos;
        }

        @Override
        public long getChildId(int groupPos, int childPos) {
            return childPos;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int groupPos, boolean isExpanded, View view, ViewGroup viewGroup) {
            BodyPartItemView itemView;
            if (view == null) {
                itemView = BodyPartItemView.build(BodyPartActivity.this);
            } else {
                itemView = (BodyPartItemView) view;
            }
            itemView.bindSecondOrgan(getGroup(groupPos), groupPos == selectedOrganIndex, groupPos == opendedGroupIndex);
            return itemView;
        }

        @Override
        public View getChildView(int groupPos, int childPos, boolean isLastChild, View view, ViewGroup viewGroup) {
            BodyPartItemView itemView;
            if (view == null) {
                itemView = BodyPartItemView.build(BodyPartActivity.this);
            } else {
                itemView = (BodyPartItemView) view;
            }
            int currentOrganIndex = (groupPos + 1) * 100 + childPos;
            itemView.bindThirdOrgan(getChild(groupPos, childPos), currentOrganIndex == selectedOrganIndex);
            return itemView;
        }

        @Override
        public boolean isChildSelectable(int groupPos, int childPos) {
            return true;
        }
    }
}

/**
 * 自定义控件，身体部位和器官通用
 */
class BodyPartItemView extends LinearLayout {

    private CheckedTextView tvTitle, ctvChoose;
    private ImageView ivFlag;
    private View vLine;
    private boolean alreadyInflated_ = false;

    public void bindSecondOrgan(Organ item, boolean checked, boolean opended) {
        tvTitle.setBackgroundResource(R.drawable.wsbody_selector_body_part);
        tvTitle.setText(item.getName());
        tvTitle.setChecked(checked);
        ctvChoose.setChecked(opended);
        if (item.isLeaf()) {
            ctvChoose.setVisibility(VISIBLE);
            ctvChoose.setCheckMarkDrawable(checked ? R.drawable.wsbody_selector_arrow_black_up_down : R.drawable.wsbody_selector_arrow_white_up_down);
            ivFlag.setVisibility(opended ? VISIBLE : GONE);
            vLine.setVisibility(opended ? GONE : VISIBLE);
        } else {
            ctvChoose.setVisibility(GONE);
            ivFlag.setVisibility(GONE);
            vLine.setVisibility(VISIBLE);
        }
    }

    public void bindThirdOrgan(Organ item, boolean checked) {
        tvTitle.setBackgroundResource(R.drawable.wsbody_selector_body_organ);
        tvTitle.setText(item.getName());
        tvTitle.setChecked(checked);
        vLine.setVisibility(VISIBLE);
    }

    public static BodyPartItemView build(Context context) {
        BodyPartItemView instance = new BodyPartItemView(context);
        instance.onFinishInflate();
        return instance;
    }

    public BodyPartItemView(Context context) {
        super(context);
    }

    @Override
    public void onFinishInflate() {
        if (!alreadyInflated_) {
            alreadyInflated_ = true;
            inflate(getContext(), R.layout.wsbody_item_organ, this);
            tvTitle = findViewById(R.id.tvTitle);
            ctvChoose = findViewById(R.id.ctvChoose);
            ivFlag = findViewById(R.id.ivFlag);
            vLine = findViewById(R.id.vLine);
        }
        super.onFinishInflate();
    }

}