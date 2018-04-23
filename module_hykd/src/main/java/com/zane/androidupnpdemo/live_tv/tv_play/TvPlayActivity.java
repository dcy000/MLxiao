package com.zane.androidupnpdemo.live_tv.tv_play;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gzq.administrator.lib_common.utils.UiUtils;
import com.ksyun.media.player.KSYTextureView;
import com.zane.androidupnpdemo.R;
import com.zane.androidupnpdemo.connect_tv.entity.ClingDevice;
import com.zane.androidupnpdemo.connect_tv.entity.IDevice;
import com.zane.androidupnpdemo.connect_tv.service.manager.ClingManager;
import com.zane.androidupnpdemo.connect_tv.ui.DevicesAdapter;
import com.zane.androidupnpdemo.connect_tv.util.Utils;
import com.zane.androidupnpdemo.live_tv.LiveBean;

import org.fourthline.cling.model.meta.Device;

import java.util.Collection;
import java.util.List;

/**
 * Created by gzq on 2018/3/26.
 */

public class TvPlayActivity extends AppCompatActivity implements View.OnClickListener, ITvPlayView {
    private LinearLayout mVideoPlay;
    private ImageView mLivemediaBack;
    private TextView mLiveTitle;
    private RelativeLayout mLiveControl;
    private LinearLayout mLoaddingView;
    private List<LiveBean> tvs;
    private int playFirstPosition = 0;
    private ITvPlayPresenter tvPlayPresenter;
    private View mViewVoice;
    private LinearLayout mStartVoice;
    private ImageView mIvBtnLast;
    private ImageView mIvBtnNext;
    private RelativeLayout mLiveControl2;
    private LinearLayout mConnectTv;
    private DevicesAdapter mDevicesAdapter;
    private PopupWindow popupWindow;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tv_display);
        initView();
        tvs = getIntent().getParcelableArrayListExtra("tvs");

        playFirstPosition = getIntent().getIntExtra("position", 0);
        tvPlayPresenter = new TvPlayPresenterImp(this, tvs);
        tvPlayPresenter.startPlay(tvs.get(playFirstPosition).getTvUrl());
    }

    public static void startTvPlayActivity(Context context, Intent intent) {
        context.startActivity(intent);
    }

    private void initView() {
        mVideoPlay = (LinearLayout) findViewById(R.id.video_play);
        mLivemediaBack = (ImageView) findViewById(R.id.livemedia_back);
        mLivemediaBack.setOnClickListener(this);
        mLiveTitle = (TextView) findViewById(R.id.live_title);
        mLiveControl = (RelativeLayout) findViewById(R.id.live_control);
        mLoaddingView = (LinearLayout) findViewById(R.id.loadding_view);
        mViewVoice = (View) findViewById(R.id.view_voice);
        mConnectTv = (LinearLayout) findViewById(R.id.connectTv);
        mConnectTv.setOnClickListener(this);
        mStartVoice = (LinearLayout) findViewById(R.id.startVoice);
        mIvBtnLast = (ImageView) findViewById(R.id.iv_btn_last);
        mIvBtnNext = (ImageView) findViewById(R.id.iv_btn_next);
        mLiveControl2 = (RelativeLayout) findViewById(R.id.live_control2);
        mStartVoice.setOnClickListener(this);
        mIvBtnLast.setOnClickListener(this);
        mIvBtnNext.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.connectTv) {
            connectTv();

        } else if (i == R.id.livemedia_back) {
            closeTv();

        }else if(i==R.id.iv_btn_last){
            int playNewPosition=tvPlayPresenter.getOnPlayingPosition()-1;
            if (playNewPosition>=0) {
                tvPlayPresenter.playLast(tvs.get(playNewPosition).getTvUrl());
            }
        }else if (i==R.id.iv_btn_next){
            int playNewPosition=tvPlayPresenter.getOnPlayingPosition()+1;
            if (playNewPosition<tvs.size()) {
                tvPlayPresenter.playNext(tvs.get(playNewPosition).getTvUrl());
            }
        }else if (i==R.id.startVoice){
            tvPlayPresenter.onBehindWakeuped();
        }
    }

    @Override
    public void showLoadingDialog() {
        mLoaddingView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoadingDialog() {
        mLoaddingView.setVisibility(View.GONE);
    }

    @Override
    public void showControlBar() {
        mLiveControl.setVisibility(View.VISIBLE);
        mLiveControl2.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideControlBar() {
        mLiveControl.setVisibility(View.GONE);
        mLiveControl2.setVisibility(View.GONE);
    }

    @Override
    public void closeTv() {
//        finish();
    }

    @Override
    public void connectTv() {
//        startActivity(new Intent(this, TVConnectMainActivity.class).putExtra("url", tvs.get(playFirstPosition).getTvUrl()));
//        startActivity(new Intent(this,TestActivity.class));
        showPopWindow();
    }

    private void showPopWindow() {
        View popupView = null;
        TranslateAnimation animation = null;
        if (popupWindow == null) {
            popupView = View.inflate(this, R.layout.hy_search_devices, null);
            // 参数2,3：指明popupwindow的宽度和高度
            popupWindow = new PopupWindow(popupView, WindowManager.LayoutParams.MATCH_PARENT,
                    UiUtils.pt(600));
            // 设置背景图片， 必须设置，不然动画没作用
            popupWindow.setBackgroundDrawable(new BitmapDrawable());
            popupWindow.setFocusable(true);
            // 设置点击popupwindow外屏幕其它地方消失
            popupWindow.setOutsideTouchable(true);
            // 平移动画相对于手机屏幕的底部开始，X轴不变，Y轴从1变0
            animation = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0, Animation.RELATIVE_TO_PARENT, 0,
                    Animation.RELATIVE_TO_PARENT, 1, Animation.RELATIVE_TO_PARENT, 0);
            animation.setInterpolator(new AccelerateInterpolator());
            animation.setDuration(200);
        }
        // 设置popupWindow的显示位置，此处是在手机屏幕底部且水平居中的位置
        popupWindow.showAtLocation(TvPlayActivity.this.findViewById(R.id.live_control2), Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL,0,0);
        popupView.startAnimation(animation);
        ImageView ivRefresh=popupView.findViewById(R.id.iv_refresh);
        ListView devicesList=popupView.findViewById(R.id.list);
        TextView tvCancle=popupView.findViewById(R.id.tv_cancle);
        Animation animation1=AnimationUtils.loadAnimation(this,R.anim.hy_rotate);

        animation1.setInterpolator(new LinearInterpolator());
        ivRefresh.setAnimation(animation1);
        mDevicesAdapter = new DevicesAdapter(this);
        devicesList.setAdapter(mDevicesAdapter);
        tvPlayPresenter.refreshDevices();

        devicesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 选择连接设备
                ClingDevice item = mDevicesAdapter.getItem(position);
                if (Utils.isNull(item)) {
                    return;
                }

                ClingManager.getInstance().setSelectedDevice(item);

                Device device = item.getDevice();
                if (Utils.isNull(device)) {
                    return;
                }

                String selectedDeviceName = String.format(getString(R.string.selectedText), device.getDetails().getFriendlyName());
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        tvPlayPresenter.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        tvPlayPresenter.onPause();
    }

    @Override
    public void showStatusBar() {
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setAttributes(params);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }

    @Override
    public void hideStatusBar() {
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
        getWindow().setAttributes(params);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }

    @Override
    public void addVideoView(KSYTextureView ksyTextureView) {
        mVideoPlay.addView(ksyTextureView);
    }

    @Override
    public void removeVideoView(KSYTextureView ksyTextureView) {
        mVideoPlay.removeView(ksyTextureView);
    }

    @Override
    public int getControlBarVisibility() {
        return mLiveControl.getVisibility();
    }

    @Override
    public void showVoiceView() {
        mViewVoice.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideVoiceView() {
        mViewVoice.setVisibility(View.GONE);
    }

    @Override
    public void findNewDevice(IDevice device) {

    }

    @Override
    public void removeDevice(IDevice device) {

    }

    @Override
    public void refreshDeices(Collection<ClingDevice> devices) {
        mDevicesAdapter.clear();
        mDevicesAdapter.addAll(devices);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (popupWindow!=null&&popupWindow.isShowing()){
            popupWindow.dismiss();
        }
        tvPlayPresenter.onDestroy();
    }
}
