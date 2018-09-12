package com.gzq.test_all_devices;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gcml.module_blutooth_devices.base.BluetoothClientManager;
import com.gcml.module_blutooth_devices.base.IPresenter;
import com.gcml.module_blutooth_devices.base.Logg;
import com.inuker.bluetooth.library.search.SearchRequest;
import com.inuker.bluetooth.library.search.SearchResult;
import com.inuker.bluetooth.library.search.response.SearchResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ChooseBluetoothActivity extends AppCompatActivity implements View.OnClickListener {
    private RecyclerView mList;
    private Button mSearch;
    private Button mStopSearch;
    private boolean isSearching = false;
    private HashMap<String, String> tiwen = new HashMap<String, String>() {
        {
            put("AET-WD", "爱立康耳温枪");
            put("ClinkBlood", "福达康耳温枪");
            put("MEDXING-IRT", "美的连耳温枪");
            put("FSRKB-EWQ01", "自带耳温枪");

        }
    };

    private HashMap<String, String> xueyang = new HashMap<String, String>() {
        {
            put("iChoice", "超思指夹血氧仪");
            put("SpO2080971", "康泰血氧仪");
        }
    };
    private HashMap<String, String> xuetang = new HashMap<String, String>() {
        {
            put("BLE-Glucowell", "好糖血糖仪");
            put("BDE_WEIXIN_TTM", "三诺血糖仪");
        }
    };
    private HashMap<String, String> xueya = new HashMap<String, String>() {
        {
            put("eBlood-Pressure", "自带血压计");
            put("iChoice", "超思血压计");
            put("KN-550BT 110", "九安血压计");
        }
    };
    private HashMap<String, String> tizhong = new HashMap<String, String>() {
        {
            put("VScale", "同方体重秤");
            put("SHHC-60F1", "怡可体重秤");
            put("iChoice", "超思体重秤");
            put("SENSSUN CLOUD", "香山体重秤");
        }
    };
    private HashMap<String, String> xindian = new HashMap<String, String>() {
        {
            put("WeCardio STD", "博声心电");
            put("A12-B", "超思心电");
        }
    };
    private HashMap<String, String> zhiwen = new HashMap<String, String>() {
        {
            put("zjwellcom", "精驰指纹仪");
        }
    };
    private int measureType;
    private List<BluetoothBean> mData;
    private BaseQuickAdapter<BluetoothBean, BaseViewHolder> adapter;
    private LinearLayout mLlBack;
    /**
     * 目前耗时：100s
     */
    private TextView mTime;
    private long sumTime = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose);
        initView();
    }

    private void initView() {
        mList = findViewById(R.id.list);
        mSearch = findViewById(R.id.search);
        mSearch.setOnClickListener(this);
        mStopSearch = findViewById(R.id.stop_search);
        mStopSearch.setOnClickListener(this);
        mList.setOnClickListener(this);
        measureType = getIntent().getIntExtra(IPresenter.MEASURE_TYPE, IPresenter.MEASURE_TEMPERATURE);
        mData = new ArrayList<>();
        setAdapter();
        mLlBack = findViewById(R.id.ll_back);
        mLlBack.setOnClickListener(this);
        mTime = findViewById(R.id.time);
    }

    private void setAdapter() {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        mList.setLayoutManager(manager);
        adapter = new BaseQuickAdapter<BluetoothBean, BaseViewHolder>(R.layout.item_advice, mData) {
            @Override
            protected void convert(BaseViewHolder baseViewHolder, BluetoothBean searchResult) {
                String name = searchResult.getName();
                if (!TextUtils.isEmpty(name)) {
                    baseViewHolder.setText(R.id.name, name);
                }
            }
        };
        mList.setAdapter(adapter);

        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                BluetoothClientManager.getClient().stopSearch();
                Logg.e(ChooseBluetoothActivity.class, mData.get(i).getBluetoothName() + "---" + mData.get(i).getAddress());
                isSearching = false;
                startActivity(new Intent(ChooseBluetoothActivity.this, AllMeasureActivity.class)
                        .putExtra("bluetoothbean", mData.get(i)));
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.search:
                Logg.e(ChooseBluetoothActivity.class, "被点击次数");
                mData.clear();
                adapter.notifyDataSetChanged();
                if (isSearching) {
                    isSearching = false;
                    BluetoothClientManager.getClient().stopSearch();
                }
                searchDevice();
                break;
            case R.id.stop_search:
//                BluetoothClientManager.getClient().stopSearch();
//                DialogSure dialogSure=new DialogSure(this);
//                dialogSure.setTitle("测试样式");
//                dialogSure.setContent("测试这段话的样式，测试这段话的样式，测试这大段话的样式，测试这段话的样式");
//                dialogSure.setSureListener(v1 -> dialogSure.dismiss());
//                dialogSure.show();
//                DialogSure builder = new DialogSure.Builder(this)
//                        .setTitle("测试样式")
//                        .setContent("测试这段话的样式，测试这段话的样式，测试这大段话的样式，测试这段话的样式")
//                        .show();
                break;
            case R.id.list:
                break;
            case R.id.ll_back:
                finish();
                break;
        }
    }

    private void searchDevice() {
        isSearching = true;
        SearchRequest build = new SearchRequest.Builder()
                .searchBluetoothLeDevice(100)
                .build();
        BluetoothClientManager.getClient().search(build, choose);

        SearchRequest build1 = new SearchRequest.Builder()
                .searchBluetoothLeDevice(100000, 1)
                .build();
        BluetoothClientManager.getClient().search(build1, choose);
    }

    private final SearchResponse choose = new SearchResponse() {
        @Override
        public void onSearchStarted() {
            Logg.e(ChooseBluetoothActivity.class, "这个方法被执行的次数onSearchStarted: ");
            sumTime = System.currentTimeMillis();
        }

        @Override
        public void onDeviceFounded(SearchResult searchResult) {
            String name1 = searchResult.getName();
            Log.e("choose", "onDeviceFounded: " + name1 + "-----" + searchResult.getAddress());
            String address = searchResult.getAddress();
            String name = name1;
            if (!TextUtils.isEmpty(name)) {
                BluetoothBean bluetoothBean = null;
                switch (measureType) {
                    case IPresenter.MEASURE_TEMPERATURE://体温
                        if (tiwen.containsKey(name)) {
                            bluetoothBean = new BluetoothBean(name1, name1 + "(" + tiwen.get(name) + ")", address, measureType);
                        }
                        break;
                    case IPresenter.MEASURE_BLOOD_OXYGEN:
                        if (xueyang.containsKey(name)) {
                            bluetoothBean = new BluetoothBean(name1, name1 + "(" + xueyang.get(name) + ")", address, measureType);
                        }
                        break;
                    case IPresenter.MEASURE_BLOOD_PRESSURE:
                        if (xueya.containsKey(name)) {
                            bluetoothBean = new BluetoothBean(name1, name1 + "(" + xueya.get(name) + ")", address, measureType);
                        }
                        break;
                    case IPresenter.MEASURE_BLOOD_SUGAR:
                        if (xuetang.containsKey(name)) {
                            bluetoothBean = new BluetoothBean(name1, name1 + "(" + xuetang.get(name) + ")", address, measureType);
                        }
                        break;
                    case IPresenter.MEASURE_WEIGHT:
                        if (tizhong.containsKey(name) && address.equals("18:7A:93:4F:43:11")) {
                            bluetoothBean = new BluetoothBean(name1, name1 + "(" + tizhong.get(name) + "白色版)", address, measureType);
                        } else if (tizhong.containsKey(name) && address.equals("18:7A:93:50:10:5C")) {
                            bluetoothBean = new BluetoothBean(name1, name1 + "(" + tizhong.get(name) + "黑色版)", address, measureType);
                        } else if (tizhong.containsKey(name)) {
                            bluetoothBean = new BluetoothBean(name1, name1 + "(" + tizhong.get(name) + ")", address, measureType);
                        }
                        break;
                    case IPresenter.MEASURE_ECG:
                        if (xindian.containsKey(name)) {
                            bluetoothBean = new BluetoothBean(name1, name1 + "(" + xindian.get(name) + ")", address, measureType);
                        }
                        break;
                    case IPresenter.CONTROL_FINGERPRINT:
                        if (zhiwen.containsKey(name)) {
                            bluetoothBean = new BluetoothBean(name1, name1 + "(" + zhiwen.get(name) + ")", address, measureType);
                        }
                        break;
                }
                boolean isContain = false;
                for (BluetoothBean bean : mData) {
                    if (bluetoothBean != null && bean.getAddress().equals(bluetoothBean.getAddress())) {
                        isContain = true;
                        break;
                    }
                }
                if (!isContain && bluetoothBean != null) {
                    sumTime = System.currentTimeMillis() - sumTime;
                    mTime.setText("目前耗时：" + String.format("%.2f", sumTime / 1000f) + "秒");
                    mData.add(bluetoothBean);
                    adapter.notifyDataSetChanged();
                }
            }
        }

        @Override
        public void onSearchStopped() {
            isSearching = false;
            Logg.e(ChooseBluetoothActivity.class, "耗时：" + (System.currentTimeMillis() - sumTime));
        }

        @Override
        public void onSearchCanceled() {
            isSearching = false;
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        if (isSearching) {
            Logg.e(ChooseBluetoothActivity.class, "onPause: 停止扫描了");
            BluetoothClientManager.getClient().stopSearch();
            isSearching = false;
        }
    }
}
