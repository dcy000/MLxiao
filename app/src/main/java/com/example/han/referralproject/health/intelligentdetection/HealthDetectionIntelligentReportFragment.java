package com.example.han.referralproject.health.intelligentdetection;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.han.referralproject.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class HealthDetectionIntelligentReportFragment extends Fragment {

    private ImageView ivRight;
    private RecyclerView rvReport;

    public HealthDetectionIntelligentReportFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(layoutId(), container, false);
        initView(view, savedInstanceState);
        return view;
    }

    private int layoutId() {
        return R.layout.health_fragment_detection_intelligent_report;
    }

    private void initView(View view, Bundle savedInstanceState) {
        ((TextView) view.findViewById(R.id.tv_top_title)).setText(R.string.healthy_report);
        view.findViewById(R.id.ll_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getFragmentManager() != null) {
                    getFragmentManager().popBackStack();
                }
            }
        });
        ivRight = ((ImageView) view.findViewById(R.id.iv_top_right));
        ivRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() != null) {
                    getActivity().finish();
                }
            }
        });
        rvReport = ((RecyclerView) view.findViewById(R.id.rv_report));
        rvReport.setLayoutManager(new LinearLayoutManager(
                getActivity(), LinearLayoutManager.VERTICAL, false));

    }


    private class VH extends RecyclerView.ViewHolder {

        public VH(View itemView) {
            super(itemView);
        }
    }




    private class Adapter extends RecyclerView.Adapter<VH> {

        @Override
        public int getItemViewType(int position) {
            return super.getItemViewType(position);
        }

        @NonNull
        @Override
        public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            return null;
        }

        @Override
        public void onBindViewHolder(@NonNull VH holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 0;
        }
    }
}
