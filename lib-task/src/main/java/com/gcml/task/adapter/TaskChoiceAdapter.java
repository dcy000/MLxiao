package com.gcml.task.adapter;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.gcml.task.R;
import com.gcml.task.bean.get.TaskHealthBean;

import java.util.ArrayList;
import java.util.List;

import javax.security.auth.login.LoginException;

public class TaskChoiceAdapter extends RecyclerView.Adapter<TaskChoiceAdapter.TaskChoiceViewHodler> {

    public List<TaskHealthBean.QuestionListBean.AnswerListBean> list;
    public Context context;
    public boolean isMultiple;
    public List<String> selectedList = new ArrayList<>();
    public OnSelectListener onSelectListener;
    public List<Boolean> booleanList = new ArrayList<>();

    public TaskChoiceAdapter(Context context, List<TaskHealthBean.QuestionListBean.AnswerListBean> list, boolean isMultiple) {
        this.list = list;
        this.context = context;
        this.isMultiple = isMultiple;
        for (int i = 0; i < list.size(); i++) {
            //设置默认的显示
            booleanList.add(false);
        }
    }

    @Override
    public TaskChoiceViewHodler onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_task_choice, parent, false);
        return new TaskChoiceViewHodler(view);
    }

    @Override
    public void onBindViewHolder(final TaskChoiceViewHodler holder, int position) {
        if (isMultiple) {
            bindMultipleView(holder, position);
        } else {
            bindRadioView(holder, position);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    private void bindMultipleView(final TaskChoiceViewHodler holder, final int position) {
        holder.checkBox.setChecked(selectedList.contains(position));
        holder.checkBox.setText(list.get(position).answerInfo);
        holder.checkBox.setChecked(booleanList.get(position));
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    holder.checkBox.setChecked(true);
                    selectedList.add(position + "");
                } else {
                    holder.checkBox.setChecked(false);
                    selectedList.remove(position + "");
                }
            }
        });
    }

    private void bindRadioView(final TaskChoiceViewHodler holder, final int position) {
        holder.checkBox.setChecked(selectedList.contains(position));
        holder.checkBox.setText(list.get(position).answerInfo);
        holder.checkBox.setChecked(false);
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    selectedList.add(position + "");
                    onSelectListener.onSelected(position);
                } else {
                    //do something else
                }
            }
        });
    }

    public String[] getSelected() {
        String[] result = new String[selectedList.size()];
        for (int i = 0; i < selectedList.size(); i++) {
            result[i] = selectedList.get(i);
        }
        return result;
    }

    class TaskChoiceViewHodler extends RecyclerView.ViewHolder {


        CheckBox checkBox;

        public TaskChoiceViewHodler(View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.cb_task_choice);
        }
    }

    public void setOnSelectListener(OnSelectListener onSelectListener) {
        this.onSelectListener = onSelectListener;
    }

    public interface OnSelectListener {
        void onSelected(int p);
    }

}
