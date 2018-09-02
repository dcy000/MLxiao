package com.gcml.health.measure.health_inquiry;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gcml.health.measure.R;
import com.gcml.health.measure.divider.GridViewDividerItemDecoration;
import com.gcml.health.measure.divider.LinearLayoutDividerItemDecoration;
import com.gcml.health.measure.health_inquiry.bean.HealthInquiryBean;
import com.gcml.lib_utils.UtilsManager;
import com.gcml.lib_utils.display.ToastUtils;
import com.gcml.module_blutooth_devices.base.BluetoothBaseFragment;
import com.iflytek.synthetize.MLVoiceSynthetize;

import java.util.List;

/**
 * copyright：杭州国辰迈联机器人科技有限公司
 * version:V1.2.5
 * created on 2018/8/28 16:35
 * created by:gzq
 * description:TODO
 */
public class HealthInquiryFragment extends BluetoothBaseFragment implements View.OnClickListener {
    /**
     * 5.您有没有以下病史？(5/6)
     */
    private TextView mQuestionTitle;
    /**
     * (可多选，选择“无”后自动跳转到下一页)
     */
    private TextView mQuestionTip;
    private RecyclerView mAnswerList;
    /**
     * 下一步
     */
    private TextView mBtnNext;
    private Context context;
    private List<HealthInquiryBean.QuestionListBean.AnswerListBean> mData;
    private HealthInquiryBean.QuestionListBean questionListBean;
    private int pageIndex;
    private BaseQuickAdapter<HealthInquiryBean.QuestionListBean.AnswerListBean, BaseViewHolder> adapter;
    private boolean is432 = true;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    protected int initLayout() {
        return R.layout.health_measure_fragment_inquiry;
    }

    @Override
    protected void initView(View view, Bundle bundle) {
        if (bundle != null) {
            questionListBean = bundle.getParcelable(HealthInquiryActivity.KEY_BUNDLE);
            pageIndex = bundle.getInt(HealthInquiryActivity.KEY_PAGEINDEX, 0);
            if (questionListBean != null) {
                mData = questionListBean.getAnswerList();
            } else {
                ToastUtils.showShort("获取数据失败");
                return;
            }
        }
        mQuestionTitle = (TextView) view.findViewById(R.id.question_title);
        mQuestionTitle.setText((++pageIndex) + "." + questionListBean.getQuestionName());
        mQuestionTip = (TextView) view.findViewById(R.id.question_tip);
        mAnswerList = (RecyclerView) view.findViewById(R.id.answer_list);
//        mBtnNext = (TextView) view.findViewById(R.id.btnNext);
//        mBtnNext.setOnClickListener(this);
        mBtnNext = findClickView(R.id.btnNext);
        checkBtnBG();
        initRecycleview();
        changeTips();
    }

    private void changeTips() {
        for (HealthInquiryBean.QuestionListBean.AnswerListBean bean : mData) {
            if ("1".equals(bean.getExclusiveStatus())) {
                mQuestionTip.setText("(可多选，选择“"+bean.getAnswerInfo()+"”后自动跳转到下一页)");
                break;
            }
        }
    }

    //遍历一边题目 如果其中有一个答案字数较多，则使用宽度较大的背景
    private void checkBtnBG() {
        for (HealthInquiryBean.QuestionListBean.AnswerListBean bean : mData) {
            if (bean.getAnswerInfo().length() > 6) {
                is432 = false;
                break;
            }
        }
    }

    private void initRecycleview() {
        String questionType = questionListBean.getQuestionType();
        if ("0".equals(questionType)) {
            mBtnNext.setVisibility(View.GONE);
            mQuestionTip.setVisibility(View.INVISIBLE);
            mAnswerList.setLayoutManager(new LinearLayoutManager(context));
            mAnswerList.addItemDecoration(new LinearLayoutDividerItemDecoration(0, 48));
        } else if ("1".equals(questionType)) {
            mBtnNext.setVisibility(View.VISIBLE);
            mQuestionTip.setVisibility(View.VISIBLE);
            mAnswerList.setLayoutManager(new GridLayoutManager(context, 3));
            mAnswerList.addItemDecoration(new GridViewDividerItemDecoration(32, 48));
        }
        mAnswerList.setAdapter(adapter = new BaseQuickAdapter<HealthInquiryBean.QuestionListBean.AnswerListBean,
                BaseViewHolder>(R.layout.health_measure_item_inquiry, mData) {
            @Override
            protected void convert(BaseViewHolder helper, HealthInquiryBean.QuestionListBean.AnswerListBean item) {
                if (is432) {
                    helper.getView(R.id.tv_answer).setBackgroundResource(R.drawable.health_measure_selector_inquiry_432_full);
                } else {
                    helper.getView(R.id.tv_answer).setBackgroundResource(R.drawable.health_measure_selector_inquiry_700_full);
                }
                if (item.getChoosed()) {
                    helper.getView(R.id.tv_answer).setSelected(true);
                } else {
                    helper.getView(R.id.tv_answer).setSelected(false);
                }
                String answerInfo = item.getAnswerInfo();
                helper.setText(R.id.tv_answer, answerInfo);

            }
        });
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                //先将选择的结果在Activity中缓存，然后跳转下一个页面
                HealthInquiryBean.QuestionListBean.AnswerListBean answerListBean = mData.get(position);
                if (answerListBean.getChoosed()) {
                    answerListBean.setChoosed(false);
                    view.setSelected(false);
                } else {
                    answerListBean.setChoosed(true);
                    view.setSelected(true);
                    //0是单选，1是多选
                    if ("1".equals(questionType)) {
                        //选中非"无"按钮，需要将"无"的选中状态取消
                        for (HealthInquiryBean.QuestionListBean.AnswerListBean bean : mData) {
                            if ("1".equals(bean.getExclusiveStatus()) && bean.getChoosed()) {
                                bean.setChoosed(false);
                                adapter.notifyDataSetChanged();
                            }
                        }
                        //选中"无"按钮，其他的按钮状态都取消
                        if ("1".equals(answerListBean.getExclusiveStatus())) {
                            for (int i = 0; i < mData.size(); i++) {
                                if (i != position) {
                                    mData.get(i).setChoosed(false);
                                } else {
                                    mData.get(i).setChoosed(true);
                                }
                            }
                            adapter.notifyDataSetChanged();
                            //自动跳转到下一页
                            dealClick();
                        }
                    } else if ("0".equals(questionType)) {
                        //单选互斥
                        for (HealthInquiryBean.QuestionListBean.AnswerListBean bean : mData) {
                            if (answerListBean == bean) {
                                bean.setChoosed(true);
                            } else {
                                bean.setChoosed(false);
                            }
                            adapter.notifyDataSetChanged();
                        }
                    }

                }
                //单选点击后是直接到下个页面
                if ("0".equals(questionType)) {
                    dealClick();
                }

            }
        });
    }

    private void dealClick() {
        ((HealthInquiryActivity) getActivity()).addHealthInquiryBean(questionListBean);
        if (fragmentChanged != null) {
            fragmentChanged.onFragmentChanged(HealthInquiryFragment.this, null);
        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btnNext) {
            if (!checkSelectOne()){
                ToastUtils.showShort("请至少选择一项");
                MLVoiceSynthetize.startSynthesize(UtilsManager.getApplication(),"请至少选择一项",false);
                return;
            }
            dealClick();

        } else {
        }
    }

    /**
     * 检查是否至少选中一个
     * @return
     */
    private boolean checkSelectOne() {
        boolean isSelectOne = false;
        for (HealthInquiryBean.QuestionListBean.AnswerListBean bean : mData) {
            if (bean.getChoosed()) {
                isSelectOne = true;
                break;
            }
        }
        return isSelectOne;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        MLVoiceSynthetize.destory();
    }
}
