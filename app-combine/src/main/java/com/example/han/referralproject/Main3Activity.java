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
import com.gcml.common.imageloader.ImageLoader;
import com.gcml.common.router.AppRouter;
import com.gcml.common.utils.ui.UiUtils;
import com.gcml.common.widget.recyclerview.banner.BannerAdapterHelper;
import com.gcml.common.widget.recyclerview.banner.BannerRecyclerView;
import com.gcml.common.widget.recyclerview.banner.BannerScaleHelper;
import com.gcml.web.WebActivity;
import com.sjtu.yifei.annotation.Route;
import com.sjtu.yifei.route.Routerfit;
import com.yinglan.shadowimageview.ShadowImageView;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DefaultObserver;
import io.reactivex.schedulers.Schedulers;

//@Route(path = "/app/homepage/main/activity")
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
        getPersonInfo();
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
                Routerfit.register(AppRouter.class).skipAuthActivity();
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

        bannerAdapterHelper.setPagePadding(UiUtils.pt(20));
        bannerAdapterHelper.setShowLeftCardWidth(UiUtils.pt(40));
        bannerScaleHelper.setPagePadding(UiUtils.pt(20));
        bannerScaleHelper.setShowLeftCardWidth(UiUtils.pt(40));
        bannerScaleHelper.setScale(1f);
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
    private BannerAdapterHelper bannerAdapterHelper = new BannerAdapterHelper();
    private BannerAdapter bannerAdapter = new BannerAdapter();

    private class BannerVH extends RecyclerView.ViewHolder {

        private ShadowImageView slBanner;
        private ImageView ivBanner;

        public BannerVH(View itemView) {
            super(itemView);
            slBanner = itemView.findViewById(R.id.slBanner);
            ivBanner = itemView.findViewById(R.id.ivBanner);
            itemView.setOnClickListener(bannerOnClickListener);
        }

        public void onBind(int position) {
            int realPosition = position % bannerItems.size();
//            ivBanner.setImageResource(bannerItems.get(realPosition));
            ImageLoader.with(ivBanner.getContext())
                    .load(bannerItems.get(realPosition))
                    .skipMemoryCache()
                    .radius(UiUtils.pt(40))
                    .resize(UiUtils.pt(1760), UiUtils.pt(400))
                    .into(ivBanner);

//            slBanner.setImageResource(bannerItems.get(realPosition));
//            slBanner.setImageShadowColor(Color.parseColor("#29666666"));
        }
    }

    private class BannerAdapter extends RecyclerView.Adapter<BannerVH> {

        @NonNull
        @Override
        public BannerVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.item_main_banner, parent, false);
            bannerAdapterHelper.onCreateViewHolder(parent, view);
            return new BannerVH(view);
        }

        @Override
        public void onBindViewHolder(@NonNull BannerVH holder, int position) {
            bannerAdapterHelper.onBindViewHolder(holder.itemView, position, getItemCount());
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
                    Routerfit.register(AppRouter.class).skipMeasureChooseDeviceActivity(false, "", "");
                    break;
                case 1:
                    //自诊导诊
                    Routerfit.register(AppRouter.class).getBodyTestProvider().gotoPage(Main3Activity.this);
                    break;
                case 2:
                    //自测用药
                    break;
                case 3:
                    //健康自测
                    break;
                case 4:
                    //医智囊
                    Routerfit.register(AppRouter.class).skipZenDuanActivity();
                    break;
                case 5:
                    //视频医生
                    break;
                case 6:
                    //家庭医生服务
                    break;
                case 7:
                    //护士上门
                    Routerfit.register(AppRouter.class).skipChooseDetectionTypeActivity();
                    break;
                case 8:
                    //智能问药
                    WebActivity.start(Main3Activity.this, WebActivity.URL_1);
                    break;
                case 9:
                    //智能诊断
                    WebActivity.start(Main3Activity.this, WebActivity.URL_0);
                    break;
                case 10:
                    //个人中心
                    break;
                case 11:
                    //帮助中心
                    break;
                case 12:
                    //设置
                    break;
            }
        }
    };

    private ArrayList<MenuEntity> menuEntities = new ArrayList<>();
    private MenuAdapter menuAdapter = new MenuAdapter();

    {
        menuEntities.add(new MenuEntity(R.drawable.main_ic_health_detection, "健康测量")); // 0
        menuEntities.add(new MenuEntity(R.drawable.main_ic_self_check, "自诊导诊")); // 1
        menuEntities.add(new MenuEntity(R.drawable.main_ic_self_check_medical, "自测用药")); // 2
        menuEntities.add(new MenuEntity(R.drawable.main_ic_health_self_check, "健康自测")); // 3
        menuEntities.add(new MenuEntity(R.drawable.main_ic_health_news, "健康资讯"));// 4
        menuEntities.add(new MenuEntity(R.drawable.main_ic_call_doctor, "视频医生")); // 5
        menuEntities.add(new MenuEntity(R.drawable.main_ic_family_dcotor, "家医服务")); // 6
        menuEntities.add(new MenuEntity(R.drawable.main_ic_family_nurse, "护士上门")); // 7
        menuEntities.add(new MenuEntity(R.drawable.main_ic_self_qa, "智能问药")); // 8
        menuEntities.add(new MenuEntity(R.drawable.main_ic_self_diagnosis, "智能诊断")); // 9
        menuEntities.add(new MenuEntity(R.drawable.main_ic_user_certer, "个人中心")); // 10
        menuEntities.add(new MenuEntity(R.drawable.main_ic_help, "帮助中心")); // 11
        menuEntities.add(new MenuEntity(R.drawable.main_ic_settings, "设置")); // 12
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
            RecyclerView.LayoutParams layoutParams =
                    (RecyclerView.LayoutParams) itemView.getLayoutParams();
            int leftMargin = position == 0 ? UiUtils.pt(63) : 0;
            if (layoutParams.leftMargin != leftMargin) {
                layoutParams.leftMargin = leftMargin;
                itemView.setLayoutParams(layoutParams);
            }

            MenuEntity menuEntity = menuEntities.get(position);
            ivMenuItem.setImageResource(menuEntity.menuImage);
            tvMenuItem.setText(menuEntity.menuLabel);
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
        if (statusBarFragment != null) {
            statusBarFragment.showStatusBar(true);
        }
    }
}
