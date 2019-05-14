package com.gcml.family.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.gcml.common.router.AppRouter;
import com.gcml.common.widget.toolbar.ToolBarClickListener;
import com.gcml.common.widget.toolbar.TranslucentToolBar;
import com.gcml.family.R;
import com.gcml.family.adapter.FamilyMenuAdapter;
import com.gcml.family.bean.FamilyBean;
import com.iflytek.synthetize.MLVoiceSynthetize;
import com.sjtu.yifei.route.Routerfit;

import java.util.ArrayList;
import java.util.List;

/**
 * desc: 拨号中心任务页面 .
 * author: wecent .
 * date: 2018/8/20 .
 */

public class FamilyActivity extends AppCompatActivity {

    TranslucentToolBar mToolBar;
    RecyclerView mRecycler;
    FamilyMenuAdapter mAdapter;
    TextView mNewsText;
    String startType;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_family);
        startType = getIntent().getStringExtra("startType");

        bindView();
        bindData();
    }

    private void bindView() {
        mToolBar = findViewById(R.id.tb_family);
        mRecycler = findViewById(R.id.rv_family);
        mNewsText = findViewById(R.id.tv_family);
    }

    private void bindData() {
        MLVoiceSynthetize.startSynthesize(getApplicationContext(), "欢迎来到拨号中心。", false);
        mToolBar.setData("拨 号", R.drawable.common_btn_back, "返回", R.drawable.common_btn_home, null, new ToolBarClickListener() {
            @Override
            public void onLeftClick() {
                if (startType.equals("MLMain")) {
                    Routerfit.register(AppRouter.class).skipMainActivity();
                    finish();
                } else if (startType.equals("MLSpeech")) {
                    finish();
                }
            }

            @Override
            public void onRightClick() {
                Routerfit.register(AppRouter.class).skipMainActivity();
                finish();
            }
        });
        mRecycler.setLayoutManager(new GridLayoutManager(FamilyActivity.this, 2));
        getFamilyNews();
        getFamilyData();
    }

    private void getFamilyNews() {
        int num = 2;
        if (num > 0) {
            mNewsText.setVisibility(View.VISIBLE);
            mNewsText.setText("您有" + num + "条未读消息");
        } else {
            mNewsText.setVisibility(View.GONE);
        }
    }

    private void getFamilyData() {
        List<FamilyBean> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            if (i < 5) {
                list.add(new FamilyBean("郭志强", "孙子"));
            } else {
                list.add(new FamilyBean("曾庆森", "儿子"));
            }
        }
        mAdapter = new FamilyMenuAdapter(R.layout.item_family_menu, list);
        mRecycler.setAdapter(mAdapter);

//        LoadingDialog tipDialog = new LoadingDialog.Builder(FamilyActivity.this)
//                .setIconType(LoadingDialog.Builder.ICON_TYPE_LOADING)
//                .setTipWord("正在加载")
//                .create();
//        mTaskRepository.taskListFromApi(UserSpHelper.getUserId())
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .doOnSubscribe(new Consumer<Disposable>() {
//                    @Override
//                    public void accept(Disposable disposable) {
//                        tipDialog.show();
//                    }
//                })
//                .doOnTerminate(new Action() {
//                    @Override
//                    public void run() {
//                        tipDialog.dismiss();
//                    }
//                })
//                .as(RxUtils.autoDisposeConverter(this))
//                .subscribeWith(new DefaultObserver<TaskBean>() {
//                    @Override
//                    public void onNext(TaskBean body) {
//                        super.onNext(body);
//                        if (body.complitionStatus) {
//                            TaskFinishFragment instanceFinish = TaskFinishFragment.newInstance(body);
//                            getSupportFragmentManager().beginTransaction().replace(R.id.fl_task, instanceFinish).commit();
//                        } else {
//                            TaskNormalFragment instanceNoemal = TaskNormalFragment.newInstance(body);
//                            getSupportFragmentManager().beginTransaction().replace(R.id.fl_task, instanceNoemal).commit();
//                        }
//                    }
//
//                    @Override
//                    public void onError(Throwable throwable) {
//                        super.onError(throwable);
//                        LoadingDialog errorDialog = new LoadingDialog.Builder(FamilyActivity.this)
//                                .setIconType(LoadingDialog.Builder.ICON_TYPE_FAIL)
//                                .setTipWord("请求失败")
//                                .create();
//                        errorDialog.show();
//                        mHandler.postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                errorDialog.dismiss();
//                            }
//                        }, 500);
//                    }
//                });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        MLVoiceSynthetize.stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MLVoiceSynthetize.destory();
    }

}
