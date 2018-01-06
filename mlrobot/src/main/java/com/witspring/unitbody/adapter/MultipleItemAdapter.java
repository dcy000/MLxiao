package com.witspring.unitbody.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.witspring.mlrobot.R;
import com.witspring.unitbody.InquiryActivity;
import com.witspring.unitbody.model.entity.ChooseItem;
import com.witspring.unitbody.model.entity.DiseaseItem;
import com.witspring.unitbody.model.entity.InquiryChatMessage;
import com.witspring.unitdisease.DiseaseActivity;
import com.witspring.util.DensityUtil;
import com.witspring.view.FlowLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenqiang on 2018/1/4.
 * 多item类型的适配器
 */

public class MultipleItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    protected List<InquiryChatMessage> mData;

    public static enum ITEM_TYPE {
        ITEM_TYPE_USER_ANSWER_TEXT,
        ITEM_TYPE_COMPUTER_ASK_RADIO,
        ITEM_TYPE_COMPUTER_ASK_CHECKBOX,
        ITEM_TYPE_COMPUTER_DISEASE_RESULT
    }

    private final LayoutInflater mLayoutInflater;
    private final Context mContext;
    private final InquiryActivity mActivity;

    public MultipleItemAdapter(Context context ,InquiryActivity activity) {
        mData = new ArrayList<>();
        mContext = context;
        mActivity = activity;
        mLayoutInflater = LayoutInflater.from(context);
    }

    public void setData(List<InquiryChatMessage> data) {
        mData.clear();
        addAll(data);
        notifyDataSetChanged();
    }

    public void addAll(List<InquiryChatMessage> data){
        mData.addAll(data);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE.ITEM_TYPE_USER_ANSWER_TEXT.ordinal()) {
            return new UserAnswerTextViewHolder(mLayoutInflater.inflate(R.layout.wsbody_item_user_answer, parent, false));
        } else if(viewType == ITEM_TYPE.ITEM_TYPE_COMPUTER_ASK_RADIO.ordinal()){
            return new ComputerAskRadioViewHolder(mLayoutInflater.inflate(R.layout.wsbody_item_computer_ask_radio, parent, false));
        } else if(viewType == ITEM_TYPE.ITEM_TYPE_COMPUTER_ASK_CHECKBOX.ordinal()){
            return new ComputerAskCheckBoxViewHolder(mLayoutInflater.inflate(R.layout.wsbody_item_computer_ask_checkbox, parent, false));
        } else if(viewType == ITEM_TYPE.ITEM_TYPE_COMPUTER_DISEASE_RESULT.ordinal()){
            return new ComputerDiseaseResultViewHolder(mLayoutInflater.inflate(R.layout.wsbody_item_computer_disease_result, parent, false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof UserAnswerTextViewHolder) {
            ((UserAnswerTextViewHolder) holder).mTextView.setText(mData.get(position).getTextContent());
        } else if (holder instanceof ComputerAskRadioViewHolder) {
            ((ComputerAskRadioViewHolder) holder).mTextView.setText(mData.get(position).getBoxTitle());
            List<ChooseItem> chooseItems = mData.get(position).getRadioButtons();
            FlowLayout mflowLayout = ((ComputerAskRadioViewHolder) holder).flowLayout;
            mflowLayout.removeAllViews();
            FlowLayout.LayoutParams layoutParams = new FlowLayout.LayoutParams(FlowLayout.LayoutParams.WRAP_CONTENT, FlowLayout.LayoutParams.WRAP_CONTENT);

            for(int i =0;i<chooseItems.size();i++){
                CheckedTextView mcheckedTextView = createRadioItem(position == getItemCount() - 1, mflowLayout, chooseItems.get(i),mData.get(position).getLocation());
                mcheckedTextView.setChecked(chooseItems.get(i).isChecked());
                mflowLayout.addView(mcheckedTextView,layoutParams);
            }
        } else if (holder instanceof ComputerAskCheckBoxViewHolder) {
            ComputerAskCheckBoxViewHolder $holder = (ComputerAskCheckBoxViewHolder) holder;
            $holder.mTextView.setText(mData.get(position).getBoxTitle());
            final List<ChooseItem> chooseItems = mData.get(position).getCheckboxButtons();
            FlowLayout mflowLayout = $holder.flowLayout;
            mflowLayout.removeAllViews();
            FlowLayout.LayoutParams layoutParams = new FlowLayout.LayoutParams(FlowLayout.LayoutParams.WRAP_CONTENT, FlowLayout.LayoutParams.WRAP_CONTENT);

            for(int i =0;i<chooseItems.size();i++){
                CheckedTextView mcheckedTextView = createTvItem(position == getItemCount() - 1, mflowLayout, chooseItems.get(i),mData.get(position).getLocation());
                mcheckedTextView.setChecked(chooseItems.get(i).isChecked());
                mflowLayout.addView(mcheckedTextView,layoutParams);
            }
            if (chooseItems.size() > 0) {
                $holder.btnSure.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        StringBuilder sb = new StringBuilder();
                        for(int i =0;i<chooseItems.size();i++){
                            if (chooseItems.get(i).isChecked()) {
                                sb.append("," + chooseItems.get(i).getName());
                            }
                        }
                        if(sb.length()>0){
                            sb.deleteCharAt(0);
                            mActivity.onCheckBtnClick(sb.toString(),mData.get(position).getLocation());
                        }
                    }
                });
            }

            if(position != getItemCount() - 1){
                $holder.btnSure.setOnClickListener(null);
            }
        } else if (holder instanceof ComputerDiseaseResultViewHolder) {
            ComputerDiseaseResultViewHolder viewHolder = (ComputerDiseaseResultViewHolder) holder;
            mActivity.end();
            if (viewHolder.llContent.getChildCount() > 1) {
                return;
            }
            LayoutInflater inflater = LayoutInflater.from(viewHolder.llContent.getContext());
            List<DiseaseItem> diseaseItems = mData.get(position).getDiseaseList();
            for(int i =0;i<diseaseItems.size();i++){
                System.out.println(diseaseItems.get(i).getName());
                View view = inflater.inflate(R.layout.wsbody_item_disease, null);
                TextView tvName = view.findViewById(R.id.tvName);
                TextView tvProbability = view.findViewById(R.id.tvProbability);
                tvName.setText(diseaseItems.get(i).getName());
                tvProbability.setText(String.format("%.2f%%", diseaseItems.get(i).getPossible() * 100));
                viewHolder.llContent.addView(view);
                view.setTag(diseaseItems.get(i));
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DiseaseItem item = (DiseaseItem) view.getTag();
                        Intent intent = new Intent(mActivity, DiseaseActivity.class);
                        intent.putExtra("diseaseName", item.getName());
                        mActivity.startActivity(intent);
                    }
                });
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        return mData.get(position).getBoxType();
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class UserAnswerTextViewHolder extends RecyclerView.ViewHolder {
        TextView mTextView;
        UserAnswerTextViewHolder(View view) {
            super(view);
            mTextView =(TextView)view.findViewById(R.id.tvTitle);
        }
    }

    public static class ComputerAskRadioViewHolder extends RecyclerView.ViewHolder {
        TextView mTextView;
        FlowLayout flowLayout;

        ComputerAskRadioViewHolder(View view) {
            super(view);
            mTextView =(TextView)view.findViewById(R.id.tvTitle);
            flowLayout = (FlowLayout)view.findViewById(R.id.flowlayout);
        }
    }

    public static class ComputerAskCheckBoxViewHolder extends RecyclerView.ViewHolder {
        TextView mTextView;
        FlowLayout flowLayout;
        View btnSure;


        ComputerAskCheckBoxViewHolder(View view) {
            super(view);
            mTextView =(TextView)view.findViewById(R.id.tvTitle);
            flowLayout = (FlowLayout)view.findViewById(R.id.flowlayout);
            btnSure = view.findViewById(R.id.btnSure);
        }
    }

    public static class ComputerDiseaseResultViewHolder extends RecyclerView.ViewHolder {
        TextView mTextView;
        LinearLayout llContent;

        ComputerDiseaseResultViewHolder(View view) {
            super(view);
            mTextView =(TextView)view.findViewById(R.id.tvTitle);
            llContent = (LinearLayout) view;
        }
    }

    private CheckedTextView createRadioItem(boolean isFinal, final FlowLayout parent, final ChooseItem mchooseItem,final String location){
        final CheckedTextView tvItem = new CheckedTextView(mContext);
        tvItem.setGravity(Gravity.CENTER);
        tvItem.setText(mchooseItem.getName());
        Drawable drawable = mContext.getResources().getDrawable(R.drawable.wsbody_radio_green) ;
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight()) ;
        tvItem.setCompoundDrawables(drawable, null, null, null)  ;
        tvItem.setCompoundDrawablePadding(DensityUtil.dip2px(mContext,5)) ;
        tvItem.setTextColor(mContext.getResources().getColorStateList(R.color.ws_gray_66));
        int padding = DensityUtil.dip2px(mContext, 4);
        tvItem.setMaxWidth(DensityUtil.getWindowWidth(mContext) - 12 * padding);
        tvItem.setTextSize(TypedValue.COMPLEX_UNIT_PX, mActivity.getResources().getDimensionPixelOffset(R.dimen.ws_font_medium));
        tvItem.setPadding(padding, (int)(1.2f * padding), 5 * padding, (int)(1.2f * padding));
        FlowLayout.LayoutParams layoutParams = new FlowLayout.LayoutParams(FlowLayout.LayoutParams.WRAP_CONTENT, FlowLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.bottomMargin = 3 * padding;
        if (isFinal) {
            tvItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    tvItem.setChecked(!tvItem.isChecked());
                    mchooseItem.setChecked(!mchooseItem.isChecked());
                    mActivity.onRadioChecked(mchooseItem.getName(),location);
                    for (int i = 0; i < parent.getChildCount(); i++) {
                        CheckedTextView itemView = (CheckedTextView) parent.getChildAt(i);
                        itemView.setOnClickListener(null);
                    }

                }
            });
        }
        return tvItem;
    }

    private CheckedTextView createTvItem(boolean isFinal, final FlowLayout parent, final ChooseItem mchooseItem,final String location){
        final CheckedTextView tvItem = new CheckedTextView(mContext);
        tvItem.setGravity(Gravity.CENTER);
        tvItem.setText(mchooseItem.getName());
        Drawable drawable = mContext.getResources().getDrawable(R.drawable.wsbody_checkbox_green) ;
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight()) ;
        tvItem.setCompoundDrawables(drawable, null, null, null)  ;
        tvItem.setCompoundDrawablePadding(DensityUtil.dip2px(mContext,5)) ;
        tvItem.setTextColor(mContext.getResources().getColorStateList(R.color.ws_gray_66));
        int padding = DensityUtil.dip2px(mContext, 4);
        tvItem.setMaxWidth(DensityUtil.getWindowWidth(mContext) - 12 * padding);
        tvItem.setTextSize(TypedValue.COMPLEX_UNIT_PX, mActivity.getResources().getDimensionPixelOffset(R.dimen.ws_font_medium));
        tvItem.setPadding(padding, (int)(1.2f * padding), 5 * padding, (int)(1.2f * padding));
        FlowLayout.LayoutParams layoutParams = new FlowLayout.LayoutParams(FlowLayout.LayoutParams.WRAP_CONTENT, FlowLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.bottomMargin = 3 * padding;
        tvItem.setTag(mchooseItem);

        if (isFinal) {
            tvItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    tvItem.setChecked(!tvItem.isChecked());
                    mchooseItem.setChecked(!mchooseItem.isChecked());
                    if (mchooseItem.isChecked() && mchooseItem.getExclusive() == 1) {
                        for (int i = 0; i < parent.getChildCount(); i++) {
                            CheckedTextView otherView = (CheckedTextView) parent.getChildAt(i);
                            ChooseItem otherItem = (ChooseItem) otherView.getTag();
                            if (otherItem.getExclusive() == 0) {
                                otherItem.setChecked(false);
                                otherView.setChecked(false);
                            }
                        }
                    } else if (mchooseItem.isChecked()) {
                        for (int i = parent.getChildCount() - 1; i >= 0; i--) {
                            CheckedTextView otherView = (CheckedTextView) parent.getChildAt(i);
                            ChooseItem otherItem = (ChooseItem) otherView.getTag();
                            if (otherItem.getExclusive() == 1) {
                                otherItem.setChecked(false);
                                otherView.setChecked(false);
                                break;
                            }
                        }
                    }

                }
            });
        }
        return tvItem;
    }
}
