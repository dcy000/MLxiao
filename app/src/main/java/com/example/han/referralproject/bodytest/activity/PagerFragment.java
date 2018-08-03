package com.example.han.referralproject.bodytest.activity;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.bodytest.constant.ConstitutionJudgmentEnum;

/**
 * A simple {@link Fragment} subclass.
 */
public class PagerFragment extends Fragment implements ViewPager.OnPageChangeListener {


    private int mIndex;
    private String[] mTags;
    private RecyclerView rvTags;
    private ConstitutionJudgmentEnum mElement;
    private TextView tvContent;
    private TextView tvContentSolution;
    private TextView tvTag;

    public static PagerFragment newInstance(int index, String[] tags) {
        PagerFragment pagerFragment = new PagerFragment();
        Bundle args = new Bundle();
        args.putInt("index", index);
        args.putStringArray("tags", tags);
        pagerFragment.setArguments(args);
        return pagerFragment;
    }

    public PagerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments != null) {
            mIndex = arguments.getInt("index");
            mTags = arguments.getStringArray("tags");
            if (mTags != null && mIndex < mTags.length && mIndex >= 0) {
                mElement = ConstitutionJudgmentEnum.getElement(mTags[mIndex]);
            }
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pager, container, false);
        initView(view, savedInstanceState);
        return view;
    }

    private void initView(View view, Bundle savedInstanceState) {
        tvTag = (TextView) view.findViewById(R.id.tv_tag);
        tvTag.setText(mTags[mIndex]);
        rvTags = ((RecyclerView) view.findViewById(R.id.rv_tags));
        rvTags.setLayoutManager(
                new LinearLayoutManager(
                        getActivity(),
                        LinearLayoutManager.HORIZONTAL,
                        false));
        rvTags.setAdapter(new RecyclerView.Adapter<VH>() {
            @Override
            public VH onCreateViewHolder(ViewGroup viewGroup, int i) {
                Context context = viewGroup.getContext();
                View itemView = LayoutInflater.from(context).inflate(
                        R.layout.body_test_item_tag, viewGroup, false);
                return new VH(itemView);
            }

            @Override
            public void onBindViewHolder(VH viewHolder, int i) {
                viewHolder.onBind(i);
            }

            @Override
            public int getItemCount() {
                int size = mTags != null ? mTags.length : 0;
                if (size > 1) {
                    size = 1;
                }
                return size;
            }
        });
        tvContent = (TextView) view.findViewById(R.id.tv_content);
        tvContentSolution = (TextView) view.findViewById(R.id.tv_content_solution);
        if (mElement != null) {
            String feature = mElement.getFeatrue();
            String maintenance = mElement.getMaintenance();

            SpannableStringBuilder featureBuilder = new SpannableStringBuilder(feature);
            String[] features = {
                    "形体特征：",
                    "常见表现：",
                    "心理特征：",
                    "发病倾向：",
                    "对外界环境适应能力："
            };
            for (String theFeature : features) {
                if (feature.contains(theFeature)) {
                    int start = feature.indexOf(theFeature);
                    featureBuilder.setSpan(
                            new ForegroundColorSpan(
                                    Color.parseColor("#000000")
                            ),
                            start,
                            start + theFeature.length(),
                            Spanned.SPAN_EXCLUSIVE_INCLUSIVE
                    );
                }
            }
            SpannableStringBuilder maintenanceBuilder = new SpannableStringBuilder(maintenance);
            String[] solutions = {
                    "环境起居调摄：",
                    "体育锻炼：",
                    "精神调适：",
                    "饮食调理：",
                    "药物调养："
            };
            for (String solution : solutions) {
                if (maintenance.contains(solution)) {
                    int start = maintenance.indexOf(solution);
                    maintenanceBuilder.setSpan(
                            new ForegroundColorSpan(
                                    Color.parseColor("#000000")
                            ),
                            start,
                            start + solution.length(),
                            Spanned.SPAN_EXCLUSIVE_INCLUSIVE
                    );
                }
            }
            tvContent.setText(featureBuilder);
            tvContentSolution.setText(maintenanceBuilder);
        }
    }

    private class VH extends RecyclerView.ViewHolder {

        private final TextView tvTag;

        public VH(View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mViewPager != null) {
                        mViewPager.setCurrentItem(getAdapterPosition());
                    }
                }
            });
            tvTag = ((TextView) itemView.findViewById(R.id.tv_item_tag));
        }

        public void onBind(int i) {
            tvTag.setSelected(i == mIndex);
            tvTag.setText(mTags[i]);
        }
    }

    private ViewPager mViewPager;

    public void setViewPager(ViewPager viewPager) {
        mViewPager = viewPager;
    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int i) {

    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }
}
