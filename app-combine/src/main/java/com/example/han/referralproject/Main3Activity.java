package com.example.han.referralproject;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gcml.common.data.UserEntity;
import com.gcml.common.data.UserSpHelper;
import com.gcml.common.imageloader.ImageLoader;
import com.gcml.common.router.AppRouter;
import com.gcml.common.user.IUserService;
import com.gcml.common.user.UserPostBody;
import com.gcml.common.utils.RxUtils;
import com.gcml.common.utils.ui.UiUtils;
import com.gcml.common.widget.ShadowLayout;
import com.gcml.common.widget.recyclerview.banner.BannerRecyclerView;
import com.gcml.common.widget.recyclerview.banner.BannerScaleHelper;
import com.gcml.web.WebActivity;
import com.sjtu.yifei.annotation.Route;
import com.sjtu.yifei.route.Routerfit;
import com.yinglan.shadowimageview.ShadowImageView;

import java.util.ArrayList;

import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.observers.DefaultObserver;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;


@Route(path = "/app/homepage/main/activity")
public class Main3Activity extends AppCompatActivity {

    private TextView tvLogout;
    private BannerRecyclerView rvBanner;
    private RecyclerView rvMenuItems;
    private ImageView ivAvatar;
    private TextView tvUserName;
    private StatusBarFragment statusBarFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        initView();

    }


    private void initView() {
        statusBarFragment = StatusBarFragment.show(getSupportFragmentManager(), R.id.fl_status_bar);
        //启动音量控制悬浮按钮
        Routerfit.register(AppRouter.class).getVolumeControlProvider().init(getApplication());
        tvUserName = (TextView) findViewById(R.id.tvUserName);
        ivAvatar = (ImageView) findViewById(R.id.ivAvatar);
        tvLogout = (TextView) findViewById(R.id.tvLogout);
        rvBanner = (BannerRecyclerView) findViewById(R.id.rvBanner);
        rvMenuItems = (RecyclerView) findViewById(R.id.rvMenuItems);

        tvLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Routerfit.register(AppRouter.class).skipUserLogins2Activity();
            }
        });

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
        Routerfit.register(AppRouter.class).getUserProvider().getUserEntity()
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
        }
    };

    private ArrayList<Integer> bannerItems = new ArrayList<>();

    {
        bannerItems.add(R.drawable.main_ic_mask);
        bannerItems.add(R.drawable.main_ic_mask);
        bannerItems.add(R.drawable.main_ic_mask);
        bannerItems.add(R.drawable.main_ic_mask);
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
            switch (position) {
                case 0:
                    //健康测量
                    Routerfit.register(AppRouter.class).skipChooseDetectionTypeActivity();
                    break;
                case 1:
                    //自诊导诊
                    Routerfit.register(AppRouter.class).getBodyTestProvider().gotoPage(Main3Activity.this);
                    break;
                case 2:
                    //自测用药 智能问药（左手）
                    WebActivity.start(Main3Activity.this, WebActivity.URL_MEDICAL);
                    break;
                case 3:
                    //健康自测 智能诊断（左手）
                    WebActivity.start(Main3Activity.this, WebActivity.URL_DIAGNOSIS);
                    break;
                case 4:
                    //医疗百科 医智囊
                    Routerfit.register(AppRouter.class).skipZenDuanActivity();
                    break;
                case 5:
                    //家庭医生
                    Routerfit.register(AppRouter.class).skipHealthProfileActivity();
                    break;
                case 6:
                    //健康生活
                    Routerfit.register(AppRouter.class).skipHealthLifeActivity();
                    break;
                case 7:
                    //帮助中心
                    Routerfit.register(AppRouter.class).skipHelpActivity();
                    break;
                case 8:
                    //健康数据
                    Routerfit.register(AppRouter.class).skipHealthRecordActivity(0);
                    break;
                case 9:
                    //个人资料
                    break;
                case 10:
                    //设置
                    Routerfit.register(AppRouter.class).skipSettingActivity();
                    break;
            }
        }
    };

    private ArrayList<MenuEntity> menuEntities = new ArrayList<>();
    private MenuAdapter menuAdapter = new MenuAdapter();

    {
        menuEntities.add(new MenuEntity(R.drawable.main_ic_health_detection_normal, "健康测量")); // 0
        menuEntities.add(new MenuEntity(R.drawable.main_ic_self_check_normal, "自诊导诊")); // 1
        menuEntities.add(new MenuEntity(R.drawable.main_ic_self_check_medical_normal, "自测用药")); // 2
        menuEntities.add(new MenuEntity(R.drawable.main_ic_health_self_check_normal, "健康自测")); // 3
        menuEntities.add(new MenuEntity(R.drawable.main_ic_health_wiki_normal, "医疗百科"));// 4
        menuEntities.add(new MenuEntity(R.drawable.main_ic_family_dcotor_normal, "家庭医生")); // 5
        menuEntities.add(new MenuEntity(R.drawable.main_ic_family_nurse_normal, "健康生活")); // 6
        menuEntities.add(new MenuEntity(R.drawable.main_ic_help_normal, "帮助中心")); // 7
        menuEntities.add(new MenuEntity(R.drawable.main_ic_help_normal, "健康数据")); // 8
        menuEntities.add(new MenuEntity(R.drawable.main_ic_user_info_normal, "个人资料")); // 9
        menuEntities.add(new MenuEntity(R.drawable.main_ic_settings_normal, "设置")); // 10
    }

    private class MenuEntity {
        public int menuImage;
        public String menuLabel;

        public MenuEntity(int menuImage, String menuLabel) {
            this.menuImage = menuImage;
            this.menuLabel = menuLabel;
        }
    }

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
        super.onResume();
        getPersonInfo();
        if (statusBarFragment != null) {
            statusBarFragment.showStatusBar(true);
        }
    }
}
