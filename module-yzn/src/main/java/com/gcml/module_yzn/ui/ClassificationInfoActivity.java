package com.gcml.module_yzn.ui;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.gcml.common.data.UserSpHelper;
import com.gcml.common.router.AppRouter;
import com.gcml.common.utils.DefaultObserver;
import com.gcml.common.utils.RxUtils;
import com.gcml.common.utils.display.ToastUtils;
import com.gcml.common.utils.ui.UiUtils;
import com.gcml.common.widget.dialog.LoadingDialog;
import com.gcml.common.widget.toolbar.ToolBarClickListener;
import com.gcml.common.widget.toolbar.TranslucentToolBar;
import com.gcml.module_yzn.R;
import com.gcml.module_yzn.bean.CaseInputBean;
import com.gcml.module_yzn.bean.FenLeiInfoOutBean;
import com.gcml.module_yzn.repository.YZNRepository;
import com.gcml.module_yzn.ui.fragment.ClassificationInfoFragment;
import com.gcml.module_yzn.util.BASE64Encoder;
import com.gcml.module_yzn.util.MD5Util;
import com.google.gson.Gson;
import com.sjtu.yifei.route.Routerfit;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.gcml.module_yzn.constant.Global.APP_ID;
import static com.gcml.module_yzn.constant.Global.APP_KEY;
//@Route(path = "/module/yzn/zenduan/activity")
public class ClassificationInfoActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {
    private TranslucentToolBar tbFeileiInfo;
    private FrameLayout flContaniner;
    private RadioGroup rgMenu;
    private ViewPager vpInfos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classification_info);
        initView();
        initData();
    }

    YZNRepository repository = new YZNRepository();

    private void initView() {
        tbFeileiInfo = findViewById(R.id.tv_fenlei_info);
        tbFeileiInfo.setData("资 讯 分 类 ", R.drawable.common_icon_back, "返回",
                R.drawable.auth_hospital_ic_setting, null,
                new ToolBarClickListener() {
                    @Override
                    public void onLeftClick() {
                        finish();
                    }

                    @Override
                    public void onRightClick() {
                        Routerfit.register(AppRouter.class).skipSettingActivity();
                    }
                });
        rgMenu = (RadioGroup) findViewById(R.id.rg_menu);
        vpInfos = (ViewPager) findViewById(R.id.vp_infos);
        rgMenu.setOnCheckedChangeListener(this);
        vpInfos.setOffscreenPageLimit(1);
    }

    private void initData() {
        CaseInputBean inputBean = new CaseInputBean();
        inputBean.userId = UserSpHelper.getUserId();
        inputBean.url = "news";

        requestFeiLeiInfo(inputBean);
    }

    LoadingDialog dialog;

    private void requestFeiLeiInfo(CaseInputBean inputBean) {
        dialog = new LoadingDialog.Builder(this)
                .setIconType(LoadingDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord("正在加载")
                .create();
        String inputJson = new Gson().toJson(inputBean);
        String param = BASE64Encoder.encodeString(inputJson);
        String currentTime = System.currentTimeMillis() / 1000 + "";

        String tokenTemp = APP_KEY + currentTime + param;
        String token = MD5Util.md5Encrypt32Upper(tokenTemp);

        repository.classificationInfo(APP_ID, currentTime, param, token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> dialog.show())
                .doOnTerminate(() -> dialog.dismiss())
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new DefaultObserver<List<FenLeiInfoOutBean>>() {
                    @Override
                    public void onNext(List<FenLeiInfoOutBean> data) {
                        super.onNext(data);
                        initFragments(data);
                        initRadioGroup(data);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        dialog.dismiss();
                        ToastUtils.showShort(throwable.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                        dialog.dismiss();
                    }
                });

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        for (int i = 0; i < group.getChildCount(); i++) {
            RadioButton childAt = (RadioButton) group.getChildAt(i);
            if (childAt.getId() == checkedId) {
                vpInfos.setCurrentItem(i);
                childAt.setTextSize(32);
            } else {
                childAt.setTextSize(28);
            }
        }
    }

    private List<Fragment> fragments;

    private void initFragments(List<FenLeiInfoOutBean> data) {
        fragments = new ArrayList<>();
        int size = data.size();
        for (int i = 0; i < size; i++) {
            FenLeiInfoOutBean fenLeiInfoOutBean = data.get(i);
            fragments.add(ClassificationInfoFragment.newInstance(fenLeiInfoOutBean.datas));
        }

        vpInfos.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public int getCount() {
                return fragments == null ? 0 : fragments.size();
            }

            @Override
            public Fragment getItem(int i) {
                return fragments.get(i);
            }
        });

    }

    /**
     * 商品列表
     *
     * @param data
     */
    private void initRadioGroup(List<FenLeiInfoOutBean> data) {
//        initFirstRadioButton();
        rgMenu.removeAllViews();
        for (int i = 0; i < data.size(); i++) {
            RadioButton button = new RadioButton(this);
            button.setTextSize(28);
            button.setText(data.get(i).group + "");
            button.setButtonDrawable(android.R.color.transparent);
            ViewCompat.setBackground(button, ResourcesCompat.getDrawable(getResources(), R.drawable.bg_rb_history_record, getTheme()));
            button.setTextColor(getResources().getColorStateList(R.color.good_menu_text_color));

            Drawable drawableLeft = getResources().getDrawable(
                    R.drawable.bg_rb_history_record_shape);
            button.setCompoundDrawablesWithIntrinsicBounds(drawableLeft,
                    null, null, null);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, UiUtils.pt(160f));
            button.setGravity(Gravity.CENTER);
            rgMenu.addView(button, lp);
        }
        rgMenu.check(rgMenu.getChildAt(0).getId());
    }
}
