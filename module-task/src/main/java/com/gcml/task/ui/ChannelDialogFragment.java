package com.gcml.task.ui;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.gcml.common.bus.RxBus;
import com.gcml.common.widget.toolbar.ToolBarClickListener;
import com.gcml.common.widget.toolbar.TranslucentToolBar;
import com.gcml.task.R;
import com.gcml.task.adapter.ChannelDialogAdapter;
import com.gcml.task.bean.ChannelModel;
import com.gcml.task.event.MoreChannelEvent;
import com.gcml.task.event.SelectChannelEvent;
import com.gcml.task.ui.inter.ItemDragHelperCallBack;
import com.gcml.task.ui.inter.OnChannelListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class ChannelDialogFragment extends DialogFragment implements OnChannelListener {
    private ArrayList<ChannelModel> mDatas = new ArrayList<>();
    RecyclerView mRecyclerView;
    private ItemTouchHelper mHelper;
    private TranslucentToolBar mToolBar;
    private boolean isUpdate = false;
    private ChannelDialogAdapter mAdapter;
    List<ChannelModel> mSelectedDatas;
    List<ChannelModel> mUnSelectedDatas;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar);
    }

    private OnChannelListener onChannelListener;

    public void setOnChannelListener(OnChannelListener onChannelListener) {
        this.onChannelListener = onChannelListener;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Dialog dialog = getDialog();
        if (dialog != null) {
            //添加动画
            getDialog().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            dialog.getWindow().setWindowAnimations(R.style.PopupAnimaFade);
        }
        return inflater.inflate(R.layout.layout_task_daily_more, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = view.findViewById(R.id.rv_task_dialy_more);
        mToolBar = view.findViewById(R.id.tb_task_dialy_more);
        mToolBar.setData("每 日 任 务", R.drawable.common_icon_back, "返回", 0, "完成", new ToolBarClickListener() {
            @Override
            public void onLeftClick() {
                dismiss();
            }

            @Override
            public void onRightClick() {
                RxBus.getDefault().post(new MoreChannelEvent(mAdapter.getData(), firstAddChannelName));
                dismiss();
            }
        });
        processLogic();
    }

    public static ChannelDialogFragment newInstance(List<ChannelModel> selectedDatas, List<ChannelModel> unselectedDatas) {
        ChannelDialogFragment dialogFragment = new ChannelDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("dataSelected", (Serializable) selectedDatas);
        bundle.putSerializable("dataUnselected", (Serializable) unselectedDatas);
        dialogFragment.setArguments(bundle);
        return dialogFragment;
    }

    private void setDataType(List<ChannelModel> datas, int type) {
        for (int i = 0; i < datas.size(); i++) {
            datas.get(i).setItemtype(type);
        }
    }

    private void processLogic() {
        ChannelModel channel = new ChannelModel();
        channel.setItemtype(ChannelModel.TYPE_MY);
        channel.setChannelName("可选项目");
        mDatas.add(channel);

        Bundle bundle = getArguments();
        if (bundle != null) {
            mSelectedDatas = (List<ChannelModel>) bundle.getSerializable("dataSelected");
            mUnSelectedDatas = (List<ChannelModel>) bundle.getSerializable("dataUnselected");
        }
        setDataType(mSelectedDatas, ChannelModel.TYPE_MY_CHANNEL);
        setDataType(mUnSelectedDatas, ChannelModel.TYPE_OTHER_CHANNEL);
        mDatas.addAll(mSelectedDatas);

        ChannelModel morechannel = new ChannelModel();
        morechannel.setItemtype(ChannelModel.TYPE_OTHER);
        morechannel.setChannelName("更多项目");
        mDatas.add(morechannel);

        mDatas.addAll(mUnSelectedDatas);

        ItemDragHelperCallBack callback = new ItemDragHelperCallBack(this);
        final ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(mRecyclerView);

        mAdapter = new ChannelDialogAdapter(mDatas, helper);
        GridLayoutManager manager = new GridLayoutManager(getActivity(), 5);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setAdapter(mAdapter);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                int itemViewType = mAdapter.getItemViewType(position);
                return itemViewType == ChannelModel.TYPE_MY_CHANNEL || itemViewType == ChannelModel.TYPE_OTHER_CHANNEL ? 1 : 5;
            }
        });
//        mHelper = new ItemTouchHelper(callBack);
        mAdapter.OnChannelListener(this);
//        //attachRecyclerView
//        mHelper.attachToRecyclerView(mRecyclerView);
    }

    @Override
    public void onItemMove(int starPos, int endPos) {
        if (starPos < 0 || endPos < 0) return;
        //我的频道之间移动
        if (onChannelListener != null)
            onChannelListener.onItemMove(starPos - 1, endPos - 1);//去除标题所占的一个index
        onMove(starPos, endPos, false);
    }

    private String firstAddChannelName = "";

    private void onMove(int starPos, int endPos, boolean isAdd) {
        isUpdate = true;
        ChannelModel startChannel = mDatas.get(starPos);
        //先删除之前的位置
        mDatas.remove(starPos);
        //添加到现在的位置
        mDatas.add(endPos, startChannel);
        mAdapter.notifyItemMoved(starPos, endPos);
        if (isAdd) {
            if (TextUtils.isEmpty(firstAddChannelName)) {
                firstAddChannelName = startChannel.getChannelName();
            }
        } else {
            if (startChannel.getChannelName().equals(firstAddChannelName)) {
                firstAddChannelName = "";
            }
        }
    }

    @Override
    public void onMoveToMyChannel(int starPos, int endPos) {
        onMove(starPos, endPos, true);
    }

    @Override
    public void onMoveToOtherChannel(int starPos, int endPos) {
        onMove(starPos, endPos, false);
    }

    @Override
    public void onFinish(String selectedChannelName) {

    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
