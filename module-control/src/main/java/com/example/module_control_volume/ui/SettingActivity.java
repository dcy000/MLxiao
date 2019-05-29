package com.example.module_control_volume.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.module_control_volume.BuildConfig;
import com.example.module_control_volume.R;
import com.example.module_control_volume.dialog.TalkTypeDialog;
import com.example.module_control_volume.dialog.VoicerSetDialog;
import com.example.module_control_volume.net.ControlRepository;
import com.example.module_control_volume.update.UpdateAppManager;
import com.gcml.common.data.UserSpHelper;
import com.gcml.common.recommend.bean.get.VersionInfoBean;
import com.gcml.common.router.AppRouter;
import com.gcml.common.utils.display.ToastUtils;
import com.gcml.common.widget.GridViewDividerItemDecoration;
import com.gcml.common.widget.dialog.AlertDialog;
import com.gcml.common.widget.dialog.LoadingDialog;
import com.gcml.common.widget.fdialog.BaseNiceDialog;
import com.gcml.common.widget.fdialog.NiceDialog;
import com.gcml.common.widget.fdialog.ViewConvertListener;
import com.gcml.common.widget.fdialog.ViewHolder;
import com.gcml.common.widget.toolbar.ToolBarClickListener;
import com.gcml.common.widget.toolbar.TranslucentToolBar;
import com.iflytek.synthetize.MLVoiceSynthetize;
import com.sjtu.yifei.annotation.Route;
import com.sjtu.yifei.route.Routerfit;

import java.util.Arrays;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DefaultObserver;
import io.reactivex.schedulers.Schedulers;
import me.jessyan.retrofiturlmanager.RetrofitUrlManager;
import tech.linjiang.pandora.Pandora;

@Route(path = "/module/control/setting/activity")
public class SettingActivity extends AppCompatActivity implements View.OnClickListener {

    TranslucentToolBar mToolBar;
    TextView mVoice, mWifi, mKeyword, mInformant, mTalktype, mUpdate, mAbout, mReset, mClearcache, mChangeIp, mDebugTool;
    // 外存sdcard存放路径
    private static final String FILE_PATH = Environment.getExternalStorageDirectory() + "/autoupdate/";
    // 下载应用存放全路径
    private static final String FILE_NAME = FILE_PATH + "xiaoe_autoupdate.apk";
    String[] ips = {
            "http://192.168.200.210:5555/",
            "http://192.168.200.222:5555/",
            "http://47.110.60.249:8070/",
            "http://47.110.60.249:8090/",
            "http://47.96.98.60:8030/",
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        bindView();
        bindData();
    }

    private void bindView() {
        mToolBar = findViewById(R.id.tb_setting);
        mVoice = findViewById(R.id.tv_setting_voice);
        mWifi = findViewById(R.id.tv_setting_wifi);
        mKeyword = findViewById(R.id.tv_setting_keyword);
        mInformant = findViewById(R.id.tv_setting_informant);
        mTalktype = findViewById(R.id.tv_setting_talktype);
        mUpdate = findViewById(R.id.tv_setting_update);
        mAbout = findViewById(R.id.tv_setting_about);
        mReset = findViewById(R.id.tv_setting_reset);
        mClearcache = findViewById(R.id.tv_setting_clearcache);
        mChangeIp = findViewById(R.id.tv_setting_ip);
        mDebugTool = findViewById(R.id.tv_setting_debug_tool);
        if (BuildConfig.DEBUG) {
            mChangeIp.setVisibility(View.VISIBLE);
            mDebugTool.setVisibility(View.VISIBLE);
        } else {
            mChangeIp.setVisibility(View.GONE);
            mDebugTool.setVisibility(View.GONE);
        }

        mVoice.setOnClickListener(this);
        mWifi.setOnClickListener(this);
        mKeyword.setOnClickListener(this);
        mInformant.setOnClickListener(this);
        mTalktype.setOnClickListener(this);
        mUpdate.setOnClickListener(this);
        mAbout.setOnClickListener(this);
        mReset.setOnClickListener(this);
        mClearcache.setOnClickListener(this);
        mChangeIp.setOnClickListener(this);
        mDebugTool.setOnClickListener(this);
    }

    private void bindData() {
        MLVoiceSynthetize.startSynthesize(getApplicationContext(), "欢迎来到设置页面。", false);
        mToolBar.setData("设 置", R.drawable.common_icon_back, "返回", R.drawable.common_icon_home, null, new ToolBarClickListener() {
            @Override
            public void onLeftClick() {
                finish();
            }

            @Override
            public void onRightClick() {
                Routerfit.register(AppRouter.class).skipMainActivity();
                finish();
            }
        });
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.tv_setting_voice) {//声音设置
            startActivity(new Intent(this, VoiceSettingActivity.class));

        } else if (i == R.id.tv_setting_wifi) {//设置页面
            Routerfit.register(AppRouter.class).skipWifiConnectActivity(false);

        } else if (i == R.id.tv_setting_clearcache) {//清理缓存
            showClearCacheDialog();

        } else if (i == R.id.tv_setting_update) {//检测更新
            Routerfit.register(AppRouter.class).getAppUpdateProvider().checkAppVersion(this, true);

        } else if (i == R.id.tv_setting_about) {//关于
            startActivity(new Intent(this, AboutActivity.class));

        } else if (i == R.id.tv_setting_reset) {//恢复出厂设置
            showResetDialog();

        } else if (i == R.id.tv_setting_keyword) {//设置关键词
            startActivity(new Intent(this, CustomKeyWordsActivity.class));

        } else if (i == R.id.tv_setting_informant) {//设置发音人
            showInformantDialog();

        } else if (i == R.id.tv_setting_talktype) {//设置聊天模式
            showTalkTypeDialog();

        } else if (i == R.id.tv_setting_ip) {
            //IP更换
            showIPList();
        } else if (i == R.id.tv_setting_debug_tool) {
            Pandora.get().open();
        }
    }

    private void showIPList() {
        NiceDialog.init()
                .setLayoutId(R.layout.dialog_ip_list)
                .setConvertListener(new ViewConvertListener() {
                    @Override
                    protected void convertView(ViewHolder holder, BaseNiceDialog dialog) {
                        RecyclerView recyclerView = (RecyclerView) holder.getView(R.id.rv);
                        recyclerView.setLayoutManager(new LinearLayoutManager(dialog.getContext()));
                        recyclerView.addItemDecoration(new DividerItemDecoration(dialog.getContext(), LinearLayout.VERTICAL));
                        BaseQuickAdapter<String, BaseViewHolder> adapter;
                        recyclerView.setAdapter(adapter = new BaseQuickAdapter<String, BaseViewHolder>(R.layout.layout_item_ip, Arrays.asList(ips)) {
                            @Override
                            protected void convert(BaseViewHolder helper, String item) {
                                helper.setText(R.id.tv_ip, item);
                            }
                        });
                        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                                ToastUtils.showShort("切换成功");
                                RetrofitUrlManager.getInstance().setGlobalDomain(ips[position]);
                                dialog.dismiss();
                            }
                        });
                    }
                })
                .setWidth(700)
                .setHeight(400)
                .show(getSupportFragmentManager());
    }

    private void showTalkTypeDialog() {
        TalkTypeDialog dialog = new TalkTypeDialog();
        dialog.show(getFragmentManager(), "talkType");
    }

    private void showInformantDialog() {
        VoicerSetDialog dialog = new VoicerSetDialog();
        dialog.show(getFragmentManager(), "voicedialog");
    }

    private void showClearCacheDialog() {
        new AlertDialog(SettingActivity.this).builder()
                .setMsg("确认清除本地缓存吗？")
                .setPositiveButton("确认", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                })
                .setNegativeButton("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }).show();
    }

    private void showResetDialog() {
        new AlertDialog(SettingActivity.this).builder()
                .setMsg("确认恢复出厂设置吗？")
                .setPositiveButton("确认", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //恢复出厂设置
                        UserSpHelper.clear(getApplicationContext());
                        Routerfit.register(AppRouter.class).skipWelcomeActivity();
                    }
                })
                .setNegativeButton("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }).show();
    }
}
