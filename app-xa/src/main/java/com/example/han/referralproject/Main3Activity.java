package com.example.han.referralproject;

import android.bluetooth.BluetoothAdapter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gcml.common.constant.Global;
import com.gcml.common.data.UserEntity;
import com.gcml.common.data.UserSpHelper;
import com.gcml.common.imageloader.ImageLoader;
import com.gcml.common.menu.EMenu;
import com.gcml.common.menu.MenuEntity;
import com.gcml.common.menu.MenuHelperProviderImp;
import com.gcml.common.router.AppRouter;
import com.gcml.common.utils.display.ToastUtils;
import com.gcml.common.utils.ui.UiUtils;
import com.gcml.common.widget.ShadowLayout;
import com.gcml.common.widget.recyclerview.banner.BannerRecyclerView;
import com.gcml.common.widget.recyclerview.banner.BannerScaleHelper;
import com.sjtu.yifei.annotation.Route;
import com.sjtu.yifei.route.Routerfit;
import com.yinglan.shadowimageview.ShadowImageView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DefaultObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * 合版首页 @see app-combine
 */
//@Route(path = "/app/homepage/main/activity")
public class Main3Activity extends AppCompatActivity {

    private TextView tvLogout;
    private BannerRecyclerView rvBanner;
    private RecyclerView rvMenuItems;
    private ImageView ivAvatar;
    private TextView tvUserName;
    private StatusBarFragment statusBarFragment;
    private ConstraintLayout clUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        initView();
    }

    private void getMenus() {
        Routerfit.register(AppRouter.class)
                .getMenuHelperProvider()
                .menu(false,EMenu.MAIN, new MenuHelperProviderImp.MenuResult() {
                    @Override
                    public void onSuccess(List<MenuEntity> menus) {
                        dealMenu(menus);
                    }

                    @Override
                    public void onError(String msg) {
                        ToastUtils.showShort(msg);
                    }
                });
    }

    private void dealMenu(List<MenuEntity> menus) {
        for (MenuEntity entity : menus) {
            String name = entity.getMenuLabel();
            if (TextUtils.isEmpty(name)) continue;
            switch (name) {
                case "健康测量":
                    entity.setMenuImage(R.drawable.main_ic_health_detection);
                    break;
                case "自诊导诊":
                    entity.setMenuImage(R.drawable.main_ic_self_check);
                    break;
                case "自测用药":
                    entity.setMenuImage(R.drawable.main_ic_self_check_medical);
                    break;
                case "健康自测":
                    entity.setMenuImage(R.drawable.main_ic_health_self_check);
                    break;
                case "医疗百科":
                    entity.setMenuImage(R.drawable.main_ic_health_wiki);
                    break;
                case "家庭医生":
                    entity.setMenuImage(R.drawable.main_ic_family_doctor);
                    break;
                case "健康生活":
                    entity.setMenuImage(R.drawable.main_ic_health_life);
                    break;
                case "帮助中心":
                    entity.setMenuImage(R.drawable.main_ic_help);
                    break;
                case "健康数据":
                    entity.setMenuImage(R.drawable.main_ic_health_data);
                    break;
                case "个人资料":
                    entity.setMenuImage(R.drawable.main_ic_user_info);
                    break;
                case "设置":
                    entity.setMenuImage(R.drawable.main_ic_settings);
                    break;
                case "护士上门":
                    entity.setMenuImage(R.drawable.main_ic_family_nurse);
                    break;
                case "视频医生":
                    entity.setMenuImage(R.drawable.main_ic_call_doctor_disabled);
                    break;
            }
        }
        menuEntities.clear();
        menuEntities.addAll(menus);
        Collections.sort(menuEntities);
        menuAdapter.notifyDataSetChanged();
    }

    private void initView() {
        statusBarFragment = StatusBarFragment.show(getSupportFragmentManager(), R.id.fl_status_bar);
        //启动音量控制悬浮按钮
//        Routerfit.register(AppRouter.class).getVolumeControlProvider().init(getApplication());
        tvUserName = (TextView) findViewById(R.id.tvUserName);
        ivAvatar = (ImageView) findViewById(R.id.ivAvatar);
        tvLogout = (TextView) findViewById(R.id.tvLogout);
        rvBanner = (BannerRecyclerView) findViewById(R.id.rvBanner);
        rvMenuItems = (RecyclerView) findViewById(R.id.rvMenuItems);
        clUser = findViewById(R.id.clUser);
        tvLogout.setOnClickListener(v -> {
            UserSpHelper.setToken(Global.TOURIST_TOKEN);
            UserSpHelper.setUserId("");
            Routerfit.register(AppRouter.class).skipUserLogins2Activity();
        });
        clUser.setOnClickListener(v -> Routerfit.register(AppRouter.class).skipUserInfoActivity());
        initBanner();

        initMenuItems();
    }

    private void initMenuItems() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(
                this, LinearLayoutManager.HORIZONTAL, false);
        rvMenuItems.setLayoutManager(layoutManager);
        rvMenuItems.setAdapter(menuAdapter);
    }

    private void initBanner() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(
                this, LinearLayoutManager.HORIZONTAL, false);
        bannerScaleHelper.setPagePadding(UiUtils.pt(20));
        bannerScaleHelper.setShowLeftCardWidth(UiUtils.pt(40));
        bannerScaleHelper.setScale(0.9f);
        bannerScaleHelper.setPagerLike(true);
        bannerScaleHelper.setFirstItemPosition(1000);
        bannerScaleHelper.attachToRecyclerView(rvBanner);

        rvBanner.setLayoutManager(layoutManager);
        rvBanner.setAdapter(bannerAdapter);
    }

    private void getPersonInfo() {
        Routerfit.register(AppRouter.class)
                .getUserProvider()
                .getUserEntity()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<UserEntity>() {
                    @Override
                    public void onNext(UserEntity userEntity) {
                        if (userEntity == null) return;
                        ImageLoader.with(Main3Activity.this)
                                .load(userEntity.avatar)
                                .circle()
                                .placeholder(R.drawable.avatar_placeholder)
                                .error(R.drawable.avatar_placeholder)
                                .into(ivAvatar);
                        tvUserName.setText(userEntity.name);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private View.OnClickListener bannerOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int position = rvBanner.getChildAdapterPosition(v);
            int realPosition = position % bannerItems.size();
            switch (realPosition) {
                case 0:
                    ToastUtils.showShort("开发中");
                    break;
                case 1:
                    Routerfit.register(AppRouter.class).skipOlderHealthManagementSerciveActivity();
                    break;
                case 2:
                    ToastUtils.showShort("开发中");
                    break;
            }
        }
    };

    private ArrayList<Integer> bannerItems = new ArrayList<>();

    {
        bannerItems.add(R.drawable.main_ic_banner_0);
        bannerItems.add(R.drawable.main_ic_banner_1);
        bannerItems.add(R.drawable.main_ic_banner_2);
    }

    private BannerScaleHelper bannerScaleHelper = new BannerScaleHelper();
    private BannerAdapter bannerAdapter = new BannerAdapter();

    private class BannerVH extends RecyclerView.ViewHolder {

        private ShadowImageView slBanner;
        private ShadowLayout slShadow;
        private ImageView ivBanner;

        public BannerVH(View itemView) {
            super(itemView);
            slBanner = itemView.findViewById(R.id.slBanner);
            slShadow = itemView.findViewById(R.id.slShadow);
            ivBanner = itemView.findViewById(R.id.ivBanner);
            itemView.setOnClickListener(bannerOnClickListener);
        }

        public void onBind(int position) {
            setMarginIfNeed(position);
            int realPosition = position % bannerItems.size();
//            ivBanner.setImageResource(bannerItems.get(realPosition));

//            slShadow.post(new Runnable() {
//                @Override
//                public void run() {
//                    int currentItem = bannerScaleHelper.getCurrentItem();
//                    if (currentItem == position) {
//                        slShadow.setShadowRadius(24f);
//                        slShadow.invalidateShadow();
//                    } else {
//                        slShadow.setShadowRadius(UiUtils.pt(0));
//                        slShadow.invalidateShadow();
//                    }
//                }
//            });
            ImageLoader.with(ivBanner.getContext())
                    .load(bannerItems.get(realPosition))
                    .into(ivBanner);

//            slBanner.setImageResource(bannerItems.get(realPosition));
//            slBanner.setImageShadowColor(Color.parseColor("#29666666"));
        }

        private int margin = UiUtils.pt(40);

        private void setMarginIfNeed(int position) {
            RecyclerView.LayoutParams layoutParams =
                    (RecyclerView.LayoutParams) itemView.getLayoutParams();
            int leftMargin = position == 0 ? margin : 0;
            int rightMargin = position == bannerAdapter.getItemCount() - 1 ? margin : 0;
            boolean changed = false;
            if (layoutParams.leftMargin != leftMargin) {
                layoutParams.leftMargin = leftMargin;
                changed = true;
            }
            if (layoutParams.rightMargin != rightMargin) {
                layoutParams.rightMargin = rightMargin;
                changed = true;
            }
            if (changed) {
                itemView.setLayoutParams(layoutParams);
            }
        }
    }

    private class BannerAdapter extends RecyclerView.Adapter<BannerVH> {

        @NonNull
        @Override
        public BannerVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.item_main_banner, parent, false);
            return new BannerVH(view);
        }

        @Override
        public void onBindViewHolder(@NonNull BannerVH holder, int position) {
            holder.onBind(position);
        }

        @Override
        public int getItemCount() {
            return bannerItems.size() > 0 ? Integer.MAX_VALUE : 0;
        }
    }


    private View.OnClickListener menuItemOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int position = rvMenuItems.getChildAdapterPosition(v);
            String label = menuEntities.get(position).getMenuLabel();
            switch (label) {
                case "健康检测":
                    Routerfit.register(AppRouter.class).skipChooseDetectionTypeActivity();
                    break;
                case "自诊导诊":
                    Routerfit.register(AppRouter.class).getBodyTestProvider().gotoPage(Main3Activity.this);
                    break;
                case "自测用药":
                    Routerfit.register(AppRouter.class).skipWebActivity(AppRouter.URL_MEDICAL);
                    break;
                case "健康自测":
                    Routerfit.register(AppRouter.class).skipWebActivity(AppRouter.URL_DIAGNOSIS);
                    break;
                case "医疗百科":
                    Routerfit.register(AppRouter.class).skipZenDuanActivity();
                    break;
                case "家庭医生":
                    Routerfit.register(AppRouter.class).skipHealthProfileActivity();
                    break;
                case "健康生活":
                    Routerfit.register(AppRouter.class).skipHealthLifeActivity();
                    break;
                case "帮助中心":
                    Routerfit.register(AppRouter.class).skipHelpActivity();
                    break;
                case "健康数据":
                    Routerfit.register(AppRouter.class).skipHealthRecordActivity(0);
                    break;
                case "个人资料":
                    Routerfit.register(AppRouter.class).skipUserInfoActivity();
                    break;
                case "设置":
//                    Routerfit.register(AppRouter.class).goSettingActivity("gcml://gcmlrt.com:8080/module/control/setting/activity");
                    Routerfit.register(AppRouter.class).skipSettingActivity();
                    break;
                case "护士上门":
                    ToastUtils.showShort("开发中");
                    break;
                case "视频医生":
                    ToastUtils.showShort("开发中");
                    break;
            }
        }
    };
    private ArrayList<MenuEntity> menuEntities = new ArrayList<>();
    private MenuAdapter menuAdapter = new MenuAdapter();


    private class MenuVH extends RecyclerView.ViewHolder {

        private final ImageView ivMenuItem;
        private final TextView tvMenuItem;

        public MenuVH(View itemView) {
            super(itemView);
            ivMenuItem = (ImageView) itemView.findViewById(R.id.ivMenuItem);
            tvMenuItem = (TextView) itemView.findViewById(R.id.tvMenuItem);
            ivMenuItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    menuItemOnClickListener.onClick(itemView);
                }
            });
//            itemView.setOnClickListener(menuItemOnClickListener);
        }

        public void onBind(int position) {
            setMarginIfNeed(position);

            MenuEntity menuEntity = menuEntities.get(position);
            ivMenuItem.setImageResource(menuEntity.menuImage);
            tvMenuItem.setText(menuEntity.menuLabel);
        }

        private int margin = UiUtils.pt(63);

        private void setMarginIfNeed(int position) {
            RecyclerView.LayoutParams layoutParams =
                    (RecyclerView.LayoutParams) itemView.getLayoutParams();
            int leftMargin = position == 0 ? margin : 0;
            int rightMargin = position == menuEntities.size() - 1 ? margin : 0;
            boolean changed = false;
            if (layoutParams.leftMargin != leftMargin) {
                layoutParams.leftMargin = leftMargin;
                changed = true;
            }
            if (layoutParams.rightMargin != rightMargin) {
                layoutParams.rightMargin = rightMargin;
                changed = true;
            }
            if (changed) {
                itemView.setLayoutParams(layoutParams);
            }
        }
    }

    private class MenuAdapter extends RecyclerView.Adapter<MenuVH> {

        @NonNull
        @Override
        public MenuVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.item_main_menu, parent, false);
            return new MenuVH(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MenuVH holder, int position) {
            holder.onBind(position);
        }

        @Override
        public int getItemCount() {
            return menuEntities.size();
        }
    }

    @Override
    protected void onResume() {
        BluetoothAdapter defaultAdapter = BluetoothAdapter.getDefaultAdapter();
        if (defaultAdapter != null) {
            defaultAdapter.disable();
        }

        super.onResume();
        getMenus();
        if (statusBarFragment != null) {
            statusBarFragment.showStatusBar(true);
        }

        if (UserSpHelper.isTourist()) {
            tvLogout.setVisibility(View.GONE);
            tvUserName.setText("游客");
        } else {
            tvLogout.setVisibility(View.VISIBLE);
            getPersonInfo();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
