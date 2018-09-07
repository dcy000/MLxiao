package com.example.han.referralproject.homepage;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;

import com.example.han.referralproject.R;
import com.example.han.referralproject.StatusBarFragment;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.application.MyApplication;
import com.example.han.referralproject.util.LocalShared;
import com.example.module_control_volume.VolumeControlFloatwindow;
import com.gcml.common.data.UserSpHelper;
import com.gcml.old.auth.entity.UserInfoBean;
import com.example.han.referralproject.network.NetworkApi;
import com.example.lenovo.rto.accesstoken.AccessToken;
import com.example.lenovo.rto.accesstoken.AccessTokenModel;
import com.example.lenovo.rto.http.HttpListener;
import com.example.lenovo.rto.sharedpreference.EHSharedPreferences;
import com.gcml.lib_utils.data.DataUtils;
import com.gcml.lib_utils.display.ToastUtils;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.medlink.danbogh.call2.NimAccountHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

import static com.example.lenovo.rto.Constans.ACCESSTOKEN_KEY;

/**
 * copyright：杭州国辰迈联机器人科技有限公司
 * version:V1.2.5
 * created on 2018/7/29 16:24
 * created by:gzq
 * description:新的主界面
 */
public class MainActivity extends BaseActivity implements HttpListener<AccessToken> {

    private ViewPager mViewpage;
    private LinearLayout mNewmainBottomIndicator;
    private View mIndicatorLeft;
    private View mIndicatorRight;
    private View mIndicatorMiddle;
    private List<Fragment> fragments;
    private NewMain1Fragment newMain1Fragment;
    private NewMain2Fragment newMain2Fragment;
    private NewMain3Fragment newMain3Fragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_main);
        StatusBarFragment.show(getSupportFragmentManager(), R.id.fl_status_bar);
        speak(R.string.tips_splash);
        initView();
        initFragments();
        initViewpage();
        initAToken();
    }

    private void initAToken() {
        AccessTokenModel tokenModel = new AccessTokenModel();
        tokenModel.getAccessToken(this);
    }


    private void initViewpage() {
        mViewpage.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int i) {
                return fragments.get(i);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }
        });
        mViewpage.setCurrentItem(0);
        mViewpage.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    mIndicatorLeft.setVisibility(View.VISIBLE);
                    mIndicatorMiddle.setVisibility(View.INVISIBLE);
                    mIndicatorRight.setVisibility(View.INVISIBLE);
                    if (showStateBar != null) {
                        showStateBar.showStateBar(false);
                    }
                } else if (position == 1) {
                    mIndicatorLeft.setVisibility(View.INVISIBLE);
                    mIndicatorMiddle.setVisibility(View.VISIBLE);
                    mIndicatorRight.setVisibility(View.INVISIBLE);
                    if (showStateBar != null) {
                        showStateBar.showStateBar(true);
                    }
                } else if (position == 2) {
                    mIndicatorLeft.setVisibility(View.INVISIBLE);
                    mIndicatorMiddle.setVisibility(View.INVISIBLE);
                    mIndicatorRight.setVisibility(View.VISIBLE);
                    if (showStateBar != null) {
                        showStateBar.showStateBar(true);
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }


    private void initFragments() {
        fragments = new ArrayList<>();
        newMain1Fragment = new NewMain1Fragment();
        newMain2Fragment = new NewMain2Fragment();
        newMain3Fragment = new NewMain3Fragment();

        fragments.add(newMain1Fragment);
        fragments.add(newMain2Fragment);
        fragments.add(newMain3Fragment);
    }

    private void initView() {
        mViewpage = findViewById(R.id.viewpage);
        mNewmainBottomIndicator = findViewById(R.id.newmain_bottom_indicator);
        mIndicatorLeft = findViewById(R.id.indicator_left);
        mIndicatorRight = findViewById(R.id.indicator_right);
        mIndicatorMiddle = findViewById(R.id.indicator_middle);
    }


    @Override
    protected void onResume() {
        MyApplication.getInstance().userId = UserSpHelper.getUserId();
        //启动音量控制悬浮按钮
        VolumeControlFloatwindow.init(this.getApplicationContext());
        setEnableListeningLoop(false);
        super.onResume();
//        NetworkApi.clueNotify(new NetworkManager.SuccessCallback<ArrayList<ClueInfoBean>>() {
//            @Override
//            public void onSuccess(ArrayList<ClueInfoBean> response) {
//                if (response == null || response.size() == 0) {
//                    return;
//                }
//                List<AlarmModel> models = DataSupport.findAll(AlarmModel.class);
//                //DataSupport.deleteAll(AlarmModel.class);
//                for (ClueInfoBean itemBean : response) {
//                    String[] timeString = itemBean.cluetime.split(":");
//                    boolean isSetted = false;
//                    for (AlarmModel itemModel : models) {
//                        if (itemModel.getHourOfDay() == Integer.valueOf(timeString[0])
//                                && itemModel.getMinute() == Integer.valueOf(timeString[1])
//                                && itemModel.getContent() != null
//                                && itemModel.getContent().equals(itemBean.medicine)) {
//                            isSetted = true;
//                            break;
//                        }
//                    }
//                    if (!isSetted) {
//                        AlarmHelper.setupAlarm(mContext, Integer.valueOf(timeString[0]),
//                                Integer.valueOf(timeString[1]), itemBean.medicine);
//                    }
//                }
//            }
//        });

        getPersonInfo();
    }

    //获取个人信息，得到网易账号登录所需的账号和密码
    private void getPersonInfo() {
        if ("123456".equals(MyApplication.getInstance().userId)) {
            return;
        }
        OkGo.<String>get(NetworkApi.Get_PersonInfo)
                .params("bid", MyApplication.getInstance().userId)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Timber.e(response.body());
                        try {
                            JSONObject object = new JSONObject(response.body());
                            if (object.optBoolean("tag")) {
                                JSONObject data = object.optJSONObject("data");
                                if (!DataUtils.isEmpty(data)) {
                                    UserInfoBean userInfoBean = new Gson().fromJson(data.toString(), UserInfoBean.class);
                                    if (userInfoBean != null) {
                                        LocalShared.getInstance(MainActivity.this).setUserInfo(userInfoBean);
                                        //保存惯用手到SP中
                                        UserSpHelper.setUserHypertensionHand(userInfoBean.hypertensionHand);
                                        String wyyxId = userInfoBean.wyyxId;
                                        String wyyxPwd = userInfoBean.wyyxPwd;
                                        if (TextUtils.isEmpty(wyyxId) || TextUtils.isEmpty(wyyxPwd)) {
                                            Timber.e("获取网易账号信息出错");
                                            return;
                                        }
                                        NimAccountHelper.getInstance().login(wyyxId, wyyxPwd, null);
                                    }
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private ShowStateBar showStateBar;

    /**
     * 控制状态bar中日期信息的显示或者隐藏
     *
     * @param showStateBar
     */
    public void setShowStateBarListener(ShowStateBar showStateBar) {
        this.showStateBar = showStateBar;
    }

    public interface ShowStateBar {
        void showStateBar(boolean isshow);
    }

    @Override
    public void onSuccess(AccessToken data) {
        EHSharedPreferences.WriteInfo(ACCESSTOKEN_KEY, data);
    }

    @Override
    public void onError() {
        ToastUtils.showShort("初始化AK失败");
    }

    @Override
    public void onComplete() {

    }


}
