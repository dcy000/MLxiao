package com.example.han.referralproject.recyclerview;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.activity.OfflineActivity;
import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.network.NetworkManager;
import com.example.han.referralproject.speechsynthesis.PinYinUtils;
import com.gcml.common.utils.display.ToastUtils;

import java.util.ArrayList;
import java.util.List;

public class RecoDocActivity extends BaseActivity implements View.OnClickListener {

    private RecyclerView mRecyclerView;
    private List<Docter> mlist;
    DoctorAdapter mDoctorAdapter;
    private int mCurrPage = 9;//默认加载9调数据

    public View mTvContractOffline;
    public TextView tvGoBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reco_doc);
        //隐藏软键盘
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

         mlist= new ArrayList<>();
        tvGoBack = findViewById(R.id.tv_sign_up_go_back);
        tvGoBack.setOnClickListener(this);

        mTvContractOffline = findViewById(R.id.tv_sign_up_contract_offline);
        mTvContractOffline.setOnClickListener(this);

        mRecyclerView = findViewById(R.id.forum_list);


        Spinner sp = findViewById(R.id.spinner);
        Spinner sp1 = findViewById(R.id.spinner1);
        Spinner sp2 = findViewById(R.id.spinner2);

        setSpinner(sp,sp1,sp2);

        setAdapter();
        getData();

    }

    private void setSpinner(Spinner sp,Spinner sp1,Spinner sp2) {
        List<String> list = new ArrayList<String>();
        list.add("杭州");
        list.add("上海");
        list.add("北京");

        SpinnerAdapter adapter = new SpinnerAdapter(this, android.R.layout.simple_spinner_item, list);
        sp.setAdapter(adapter);

        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            // parent： 为控件Spinner view：显示文字的TextView position：下拉选项的位置从0开始
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //获取Spinner控件的适配器
                ArrayAdapter<String> adapter = (ArrayAdapter<String>) parent.getAdapter();
                Log.e("==============", adapter.getItem(position));
            }

            //没有选中时的处理
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        List<String> list1 = new ArrayList<String>();
        list1.add("萧山");
        list1.add("滨江");
        list1.add("西湖");
        SpinnerAdapter adapter1 = new SpinnerAdapter(this, android.R.layout.simple_spinner_dropdown_item, list1);
        sp1.setAdapter(adapter1);

        sp1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            // parent： 为控件Spinner view：显示文字的TextView position：下拉选项的位置从0开始
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //获取Spinner控件的适配器
                ArrayAdapter<String> adapter = (ArrayAdapter<String>) parent.getAdapter();
                Log.e("==============", adapter.getItem(position));

            }

            //没有选中时的处理
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        List<String> list2 = new ArrayList<String>();
        list2.add("萧山第一人民医院");
        list2.add("萧山开发区医院");
        SpinnerAdapter adapter2 = new SpinnerAdapter(this, android.R.layout.simple_spinner_dropdown_item, list2);
        sp2.setAdapter(adapter2);

        sp2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            // parent： 为控件Spinner view：显示文字的TextView position：下拉选项的位置从0开始
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //获取Spinner控件的适配器
                ArrayAdapter<String> adapter = (ArrayAdapter<String>) parent.getAdapter();
                Log.e("==============", adapter.getItem(position));
            }

            //没有选中时的处理
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    public void getData() {
        NetworkApi.doctor_list(0, mCurrPage, new NetworkManager.SuccessCallback<ArrayList<Docter>>() {
            @Override
            public void onSuccess(ArrayList<Docter> response) {
                mlist.clear();
                mlist.addAll(response);
                mDoctorAdapter.notifyDataSetChanged();

            }

        }, new NetworkManager.FailedCallback() {
            @Override
            public void onFailed(String message) {
                ToastUtils.showShort( message);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_sign_up_contract_offline:
                startActivity(new Intent(mContext, OfflineActivity.class));
                break;
            case R.id.tv_sign_up_go_back:
//                CCFaceRecognitionActions.jump2RegisterHead2XunfeiActivity(this);
                finish();
                break;
        }
    }

    // 处理点击事件
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                RecoDocActivity.this.finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setDisableGlobalListen(true);
        speak(R.string.tips_doctor);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    public void setAdapter() {
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        SpacesItemDecoration decoration = new SpacesItemDecoration(-1);
        mRecyclerView.addItemDecoration(decoration);
        mRecyclerView.addItemDecoration(new SpaceItemDecoration(DensityUtils.dp2px(getApplicationContext(), -5)));
        mDoctorAdapter = new DoctorAdapter(mlist, getApplicationContext());
        mRecyclerView.setAdapter(mDoctorAdapter);

        mDoctorAdapter.setOnItemClistListener(new DoctorAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int postion) {
                Intent intent = new Intent(RecoDocActivity.this, DoctorMesActivity.class);
                intent.putExtra("docMsg", mlist.get(postion));
                intent.putExtra("sign", "0");
                startActivity(intent);

            }
        });

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {}

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


    public static final String REGEX_IN_GO_BACK = ".*(shangyibu|houtui|fanhui).*";
    public static final String REGEX_CHECK_OFFLINE = ".*xianxiaqianyue.*";

    @Override
    protected void onSpeakListenerResult(String result) {
        Toast.makeText(mContext, result, Toast.LENGTH_SHORT).show();

        String inSpell = PinYinUtils.converterToSpell(result);
        if (!TextUtils.isEmpty(inSpell) && inSpell.matches(REGEX_CHECK_OFFLINE)) {
            mTvContractOffline.performClick();
        }

        if (!TextUtils.isEmpty(inSpell) && inSpell.matches(REGEX_IN_GO_BACK)) {
            tvGoBack.performClick();
        }

        List<Docter> list = this.mlist;
        for (int i = 0; i < list.size(); i++) {
            Docter doctor = list.get(i);
            if (result.contains(doctor.getDoctername())) {
                mDoctorAdapter.getOnItemClistListener().onItemClick(i);
                return;
            }
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        mDoctorAdapter = null;
    }
}
