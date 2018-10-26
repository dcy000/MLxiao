package com.example.han.referralproject.settting.dialog;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.settting.bean.TextSizeBean;
import com.gcml.common.utils.data.SPUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by lenovo on 2018/3/23.
 */

public class TextSizeDialog extends DialogFragment {
    @BindView(R.id.rv_voices)
    RecyclerView rvVoices;
    Unbinder unbinder;
    private List<TextSizeBean> data;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(android.support.v4.app.DialogFragment.STYLE_NO_TITLE, R.style.XDialog);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_voicer_set, container, false);
        unbinder = ButterKnife.bind(this, view);
        initRV();
        return view;
    }

    private void initRV() {
        LinearLayoutManager lm = new LinearLayoutManager(getActivity());
        lm.setOrientation(LinearLayoutManager.VERTICAL);
        rvVoices.setLayoutManager(lm);
        rvVoices.setAdapter(new TextSizeAdapter(initData()));
    }

    private List<TextSizeBean> initData() {
        data = new ArrayList<>();
        data.add(new TextSizeBean(false, "标准字体", "little"));
        data.add(new TextSizeBean(false, "大号字体", "middle"));
        data.add(new TextSizeBean(false, "超大字体", "large"));
        return data;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    class TextSizeAdapter extends RecyclerView.Adapter<TextSizeAdapter.ViewHolder> {

        List<TextSizeBean> data;

        public TextSizeAdapter(List<TextSizeBean> data) {
            this.data = data;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
            View view = View.inflate(viewGroup.getContext(), R.layout.dialog_item_voicer_set, null);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            final TextSizeBean bean = data.get(position);

            int index = (int) SPUtil.get("homeTextIndex", 0);
            holder.ivCheck.setImageResource(index == position ? R.drawable.ic_check : R.drawable.ic_no_check);
            holder.tvVoicer.setText(bean.name);

            holder.rvItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (int i = 0; i < data.size(); i++) {
                        data.get(i).check = false;
                    }
                    data.get(position).check = true;
                    notifyDataSetChanged();
                    saveData(position);
                }
            });
        }

        private void saveData(int position) {
            SPUtil.put("homeTextSize", data.get(position).size);
            SPUtil.put("homeTextIndex", position);
            dismiss();
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        @OnClick(R.id.rv_item)
        public void onViewClicked() {
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.iv_check)
            ImageView ivCheck;
            @BindView(R.id.tv_voicer)
            TextView tvVoicer;
            @BindView(R.id.rv_item)
            RelativeLayout rvItem;

            ViewHolder(View view) {
                super(view);
                ButterKnife.bind(this, view);
            }
        }
    }
}
