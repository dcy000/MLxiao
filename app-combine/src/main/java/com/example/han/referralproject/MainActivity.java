package com.example.han.referralproject;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gcml.common.data.UserEntity;
import com.gcml.common.imageloader.ImageLoader;
import com.gcml.common.router.AppRouter;
import com.gcml.common.utils.display.ToastUtils;
import com.gcml.common.widget.GridViewDividerItemDecoration;
import com.gcml.lib_widget.CircleImageView;
import com.gcml.lib_widget.EclipseImageView;
import com.gcml.web.WebActivity;
import com.sjtu.yifei.route.Routerfit;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DefaultObserver;
import io.reactivex.schedulers.Schedulers;

//@Route(path = "/app/homepage/main/activity")
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    ArrayList<MainMenuBean> mainMenuBeans = new ArrayList<>();
    private StatusBarFragment statusBarFragment;

    {
        mainMenuBeans.add(new MainMenuBean(R.drawable.main_health_measure, "健康测量"));
        mainMenuBeans.add(new MainMenuBean(R.drawable.main_self_check_and_guide, "自诊导诊"));
        mainMenuBeans.add(new MainMenuBean(R.drawable.main_self_check_and_medical, "自测用药"));
        mainMenuBeans.add(new MainMenuBean(R.drawable.main_health_self_check, "健康自测"));
        mainMenuBeans.add(new MainMenuBean(R.drawable.main_yizhinang, "医智囊"));
        mainMenuBeans.add(new MainMenuBean(R.drawable.main_video_doctor, "视频医生"));
        mainMenuBeans.add(new MainMenuBean(R.drawable.main_phone_doctor, "电话医生"));
        mainMenuBeans.add(new MainMenuBean(R.drawable.main_family_doctor_service, "家庭医生"));
        mainMenuBeans.add(new MainMenuBean(R.drawable.main_nurse, "护士上门"));

        mainMenuBeans.add(new MainMenuBean(R.drawable.main_self_check_and_guide, "智能诊断"));
        mainMenuBeans.add(new MainMenuBean(R.drawable.main_self_check_and_medical, "智能问药"));
    }

    private CircleImageView mCivHead;
    private TextView mUserName;
    private EclipseImageView mEivOff;
    private RecyclerView mRvMenu;
    private BaseQuickAdapter<MainMenuBean, BaseViewHolder> adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        statusBarFragment = StatusBarFragment.show(getSupportFragmentManager(), R.id.fl_status_bar);
        initView();
        setAdapter();
        getPersonInfo();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (statusBarFragment != null) {
            statusBarFragment.showStatusBar(true);
        }
    }

    private void getPersonInfo() {
        Routerfit.register(AppRouter.class).getUserProvider().getUserEntity()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<UserEntity>() {
                    @Override
                    public void onNext(UserEntity userEntity) {
                        if (userEntity == null) return;
                        if (!isDestroyed()) {
                            ImageLoader.with(MainActivity.this)
                                    .load(userEntity.avatar)
                                    .into(mCivHead);
                            mUserName.setText(userEntity.name);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void setAdapter() {
        mRvMenu.setLayoutManager(new GridLayoutManager(this, 5));
        mRvMenu.addItemDecoration(new GridViewDividerItemDecoration(0, 32));
        mRvMenu.setAdapter(adapter = new BaseQuickAdapter<MainMenuBean, BaseViewHolder>(R.layout.layout_item_main_menu, mainMenuBeans) {
            @Override
            protected void convert(BaseViewHolder helper, MainMenuBean item) {
                helper.getView(R.id.menu_image).setBackgroundResource(item.getMenuImage());
                helper.setText(R.id.menu_title, item.getMenuTitle());
            }
        });
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                switch (position) {
                    case 0:
                        //健康测量
//                        Routerfit.register(AppRouter.class).skipMeasureChooseDeviceActivity(false, "", "");
                        Routerfit.register(AppRouter.class).skipChooseDetectionTypeActivity();
                        break;
                    case 1:
                        //自诊导诊
                        Routerfit.register(AppRouter.class).getBodyTestProvider().gotoPage(MainActivity.this);
                        break;
                    case 2:
                        //自测用药
                        ToastUtils.showLong("正在努力开发中");
                        break;
                    case 3:
                        //健康自测
                        ToastUtils.showLong("正在努力开发中");
                        break;
                    case 4:
                        //医智囊
                        Routerfit.register(AppRouter.class).skipZenDuanActivity();
                        break;
                    case 5:
                        //视频医生
                        ToastUtils.showLong("正在努力开发中");
                        break;
                    case 6:
                        //电话医生
                        ToastUtils.showLong("正在努力开发中");
                        break;
                    case 7:
                        //家庭医生服务
                        ToastUtils.showLong("正在努力开发中");
                        break;
                    case 8:
                        //护士上门
//                        ToastUtils.showLong("正在努力开发中");
                        Routerfit.register(AppRouter.class).getSystemSettingProvider().skipSettingDisplay(MainActivity.this);
                        break;
                    case 9:
                        //智能诊断
                        WebActivity.start(MainActivity.this, WebActivity.URL_DIAGNOSIS);
                        break;
                    case 10:
                        //智能问药
                        WebActivity.start(MainActivity.this, WebActivity.URL_MEDICAL);
                        break;
                }
            }
        });
    }

    private void initView() {
        mCivHead = (CircleImageView) findViewById(R.id.civ_head);
        mUserName = (TextView) findViewById(R.id.user_name);
        mEivOff = (EclipseImageView) findViewById(R.id.eiv_off);
        mEivOff.setOnClickListener(this);
        mRvMenu = (RecyclerView) findViewById(R.id.rv_menu);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.eiv_off:
                Routerfit.register(AppRouter.class).skipUserLogins2Activity();
                break;
        }
    }
}
