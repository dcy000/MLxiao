package com.example.han.referralproject;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gcml.common.data.UserEntity;
import com.gcml.common.imageloader.ImageLoader;
import com.gcml.common.router.AppRouter;
import com.gcml.lib_widget.CircleImageView;
import com.gcml.lib_widget.EclipseImageView;
import com.sjtu.yifei.annotation.Route;
import com.sjtu.yifei.route.Routerfit;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DefaultObserver;
import io.reactivex.schedulers.Schedulers;

@Route(path = "/app/homepage/main/activity")
public class MainActivity extends AppCompatActivity {
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
        initMainPage();
//        getPersonInfo();
    }

    private void initMainPage() {
        MainXaFragment mainXaFragment = new MainXaFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.flContent, mainXaFragment)
                .commitAllowingStateLoss();
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

}
