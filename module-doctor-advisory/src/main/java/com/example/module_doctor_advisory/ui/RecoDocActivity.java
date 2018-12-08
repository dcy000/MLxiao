package com.example.module_doctor_advisory.ui;

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

import com.example.module_doctor_advisory.R;
import com.example.module_doctor_advisory.adapter.DoctorAdapter;
import com.example.module_doctor_advisory.adapter.SpinnerAdapter;
import com.example.module_doctor_advisory.bean.Docter;
import com.example.module_doctor_advisory.service.DoctorAPI;
import com.gcml.auth.face.FaceConstants;
import com.gcml.auth.face.ui.FaceSignUpActivity;
import com.gcml.lib_widget.ToolbarBaseActivity;
import com.gzq.lib_core.base.Box;
import com.gzq.lib_core.base.ui.BasePresenter;
import com.gzq.lib_core.base.ui.IPresenter;
import com.gzq.lib_core.handle.SpaceItemDecoration;
import com.gzq.lib_core.handle.SpacesItemDecoration;
import com.gzq.lib_core.http.observer.CommonObserver;
import com.gzq.lib_core.utils.ActivityUtils;
import com.gzq.lib_core.utils.PinYinUtils;
import com.gzq.lib_core.utils.RxUtils;
import com.gzq.lib_core.utils.ScreenUtils;
import com.gzq.lib_core.utils.ToastUtils;
import com.iflytek.cloud.SpeechError;
import com.iflytek.recognition.MLRecognizerListener;
import com.iflytek.recognition.MLVoiceRecognize;
import com.iflytek.synthetize.MLVoiceSynthetize;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RecoDocActivity extends ToolbarBaseActivity implements View.OnClickListener {

    private RecyclerView mRecyclerView;
    private List<Docter> mlist;
    DoctorAdapter mDoctorAdapter;
    private int mCurrPage = 9;//默认加载9调数据

    public View mTvContractOffline;
    public TextView tvGoBack;


    @Override
    public int layoutId(Bundle savedInstanceState) {
        return R.layout.activity_reco_doc;
    }

    @Override
    public void initParams(Intent intentArgument) {
        mlist = new ArrayList<>();
        MLVoiceRecognize.startRecognize(recognizeLisener);
    }

    @Override
    public void initView() {
        //隐藏软键盘
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        tvGoBack = (TextView) findViewById(R.id.tv_sign_up_go_back);
        tvGoBack.setOnClickListener(this);

        mTvContractOffline = findViewById(R.id.tv_sign_up_contract_offline);
        mTvContractOffline.setOnClickListener(this);

        mRecyclerView = (RecyclerView) findViewById(R.id.forum_list);


        Spinner sp = (Spinner) findViewById(R.id.spinner);
        Spinner sp1 = (Spinner) findViewById(R.id.spinner1);
        Spinner sp2 = (Spinner) findViewById(R.id.spinner2);

        setSpinner(sp, sp1, sp2);

        setAdapter();
        getData();
    }

    @Override
    public IPresenter obtainPresenter() {
        return new BasePresenter(this) {
        };
    }

    private void setSpinner(Spinner sp, Spinner sp1, Spinner sp2) {
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
        Box.getRetrofit(DoctorAPI.class)
                .getDoctors(0, mCurrPage)
                .compose(RxUtils.httpResponseTransformer())
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new CommonObserver<List<Docter>>() {
                    @Override
                    public void onNext(List<Docter> docters) {
                        mlist.clear();
                        mlist.addAll(docters);
                        mDoctorAdapter.notifyDataSetChanged();
                    }
                });
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        int i = v.getId();
        if (i == R.id.tv_sign_up_contract_offline) {
            ActivityUtils.skipActivity(OfflineActivity.class);
        } else if (i == R.id.tv_sign_up_go_back) {
            ActivityUtils.skipActivityForResult(FaceSignUpActivity.class, 1001);

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1001) {
            if (data != null) {
                int extra = data.getIntExtra(FaceConstants.KEY_AUTH_FACE_RESULT, 0);
                switch (extra) {
                    case FaceConstants.AUTH_FACE_SUCCESS:
                        ActivityUtils.skipActivity(RecoDocActivity.class);
                        break;
                    case FaceConstants.AUTH_FACE_FAIL:
                        break;
                }
            }
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
        MLVoiceSynthetize.startSynthesize(R.string.tips_doctor);
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
        mRecyclerView.addItemDecoration(new SpaceItemDecoration(ScreenUtils.dp2px(-5)));
        mDoctorAdapter = new DoctorAdapter(mlist, getApplicationContext());
        mRecyclerView.setAdapter(mDoctorAdapter);

        mDoctorAdapter.setOnItemClistListener(new DoctorAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int postion) {
                Intent intent = new Intent(RecoDocActivity.this, DoctorMesActivity.class);
                intent.putExtra("docMsg", (Serializable) mlist.get(postion));
                intent.putExtra("sign", "0");
                startActivity(intent);

            }
        });

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
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


    public static final String REGEX_IN_GO_BACK = ".*(shangyibu|houtui|fanhui).*";
    public static final String REGEX_CHECK_OFFLINE = ".*xianxiaqianyue.*";
    private MLRecognizerListener recognizeLisener = new MLRecognizerListener() {
        @Override
        public void onMLVolumeChanged(int i, byte[] bytes) {

        }

        @Override
        public void onMLBeginOfSpeech() {

        }

        @Override
        public void onMLEndOfSpeech() {

        }

        @Override
        public void onMLResult(String result) {
            ToastUtils.showShort(result);
            String inSpell = PinYinUtils.converterToSpell(result);
            if (!TextUtils.isEmpty(inSpell) && inSpell.matches(REGEX_CHECK_OFFLINE)) {
                mTvContractOffline.performClick();
            }

            if (!TextUtils.isEmpty(inSpell) && inSpell.matches(REGEX_IN_GO_BACK)) {
                tvGoBack.performClick();
            }

            List<Docter> list = RecoDocActivity.this.mlist;
            for (int i = 0; i < list.size(); i++) {
                Docter doctor = list.get(i);
                if (result.contains(doctor.getDoctername())) {
                    mDoctorAdapter.getOnItemClistListener().onItemClick(i);
                    return;
                }
            }
        }

        @Override
        public void onMLError(SpeechError error) {

        }
    };

    @Override
    protected void onStop() {
        super.onStop();
        mDoctorAdapter = null;
    }
}
