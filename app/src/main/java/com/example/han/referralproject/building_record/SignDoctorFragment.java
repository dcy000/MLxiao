package com.example.han.referralproject.building_record;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.han.referralproject.MainActivity;
import com.example.han.referralproject.R;
import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.recyclerview.DensityUtils;
import com.example.han.referralproject.recyclerview.Docter;
import com.example.han.referralproject.recyclerview.DoctorAdapter;
import com.example.han.referralproject.recyclerview.SpaceItemDecoration;
import com.example.han.referralproject.recyclerview.SpacesItemDecoration;
import com.example.han.referralproject.yiyuan.activity.DoctorMesInfoActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SignDoctorFragment extends Fragment implements View.OnClickListener {
    private View view;
    private int mCurrPage = 9;//默认加载9调数据
    private LinearLayout llBack;
    private ImageView ivTopRight;
    private RecyclerView signDoctors;
    /**
     * 上一步
     */
    private TextView tvSignUpGoBack;
    /**
     * 下一步
     */
    private TextView tvSignUpGoForward;
    private DoctorAdapter mDoctorAdapter;
    private List<Docter> mData;
    private IFragmentChange iFragmentChange;

    public void setOnFragmentChange(IFragmentChange iFragmentChange) {
        this.iFragmentChange = iFragmentChange;
    }

    private Docter selectedDoctor;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_sign_doctor, container, false);
            initView(view);
            setAdapter();
            getData();
        }
        return view;
    }

    private void setAdapter() {
        signDoctors.setHasFixedSize(true);
        signDoctors.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        SpacesItemDecoration decoration = new SpacesItemDecoration(-1);
        signDoctors.addItemDecoration(decoration);
        signDoctors.addItemDecoration(new SpaceItemDecoration(DensityUtils.dp2px(getContext().getApplicationContext(), -5)));
        mDoctorAdapter = new DoctorAdapter(mData, getContext().getApplicationContext());
        signDoctors.setAdapter(mDoctorAdapter);

        mDoctorAdapter.setOnItemClistListener(new DoctorAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int postion) {
                selectedDoctor = mData.get(postion);
                for (int i = 0; i < mData.size(); i++) {
                    if (i == postion) {
                        mData.get(i).setSelected(true);
                    } else {
                        mData.get(i).setSelected(false);
                    }
                }
                mDoctorAdapter.notifyDataSetChanged();
                ((BuildingRecordActivity) getActivity()).buildingRecordBean.setDoctorId(selectedDoctor.getDocterid());
                ((BuildingRecordActivity) getActivity()).buildingRecordBean.setHiHealthRecordId("");
                Intent docMsg = new Intent(getContext(), DoctorMesInfoActivity.class).putExtra("docMsg", mData.get(postion));
                startActivityForResult(docMsg,120);

            }
        });

        signDoctors.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                GridLayoutManager manager = (GridLayoutManager) recyclerView.getLayoutManager();

                int totalItemCount = recyclerView.getAdapter().getItemCount();
                int lastVisibleItemPosition = manager.findLastVisibleItemPosition();
                int visibleItemCount = recyclerView.getChildCount();
                // 屏幕滑动后停止（空闲状态）
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItemPosition >= totalItemCount - 1 && visibleItemCount > 0) {
                    mCurrPage += 9;
                    getData();
                }
            }

        });

    }

    private void getData() {
        OkGo.<String>get(NetworkApi.DOCTER_LIST)
                .params("start", 0)
                .params("limit", mCurrPage)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            JSONObject object = new JSONObject(response.body());
                            if (object.optBoolean("tag", false)) {
                                List<Docter> docters = new Gson().fromJson(object.optJSONArray("data").toString(), new TypeToken<ArrayList<Docter>>() {
                                }.getType());
                                dealData(docters);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {

                    }
                });
    }

    private void dealData(List<Docter> docters) {
        if (docters == null) {
            return;
        }
        mData.clear();
        mData.addAll(docters);
        mDoctorAdapter.notifyDataSetChanged();
    }

    private void initView(View view) {
        mData = new ArrayList<>();
        llBack = (LinearLayout) view.findViewById(R.id.ll_back);
        llBack.setOnClickListener(this);
        ivTopRight = (ImageView) view.findViewById(R.id.iv_top_right);
        ivTopRight.setOnClickListener(this);
        signDoctors = (RecyclerView) view.findViewById(R.id.sign_doctors);
        tvSignUpGoBack = (TextView) view.findViewById(R.id.tv_sign_up_go_back);
        tvSignUpGoBack.setVisibility(View.GONE);
        tvSignUpGoBack.setOnClickListener(this);
        tvSignUpGoForward = (TextView) view.findViewById(R.id.tv_sign_up_go_forward);
        tvSignUpGoForward.setVisibility(View.GONE);
        tvSignUpGoForward.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((BuildingRecordActivity) getActivity()). setDisableGlobalListen(true);
        ((BuildingRecordActivity) getActivity()).speak("主人,请输入您的签约医生");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.ll_back:
                getActivity().finish();
                break;
            case R.id.iv_top_right:
                startActivity(new Intent(getActivity(), MainActivity.class));
                break;
            case R.id.tv_sign_up_go_back:
                getActivity().finish();
                break;
            case R.id.tv_sign_up_go_forward:
                if (selectedDoctor == null) {
                    ((BuildingRecordActivity) getActivity()).speak(R.string.select_least_one);
                    return;
                }
                if (iFragmentChange != null) {
                    iFragmentChange.nextStep(this);
                }
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (view != null) {
            ((ViewGroup) view.getParent()).removeView(view);
        }
    }

}
