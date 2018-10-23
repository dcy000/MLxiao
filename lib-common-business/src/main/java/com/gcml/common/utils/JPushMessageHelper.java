package com.gcml.common.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.gcml.common.app.lifecycle.TopActivityHelper;
import com.gcml.common.bus.RxBus;
import com.gcml.common.business.R;
import com.gcml.common.data.MessageBean;
import com.gcml.common.utils.data.TimeUtils;
import com.github.mmin18.widget.RealtimeBlurView;
import com.iflytek.synthetize.MLVoiceSynthetize;

import java.text.SimpleDateFormat;

import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * copyright：杭州国辰迈联机器人科技有限公司
 * version: V1.2.5
 * created on 2018/10/18 16:55
 * created by: gzq
 * description: TODO
 */
public class JPushMessageHelper {
    public static void init() {
        RxBus.getDefault().on(MessageBean.class)
                .subscribeOn(Schedulers.io())
                .subscribe(RxBus.consumer(new Consumer<MessageBean>() {
                    @Override
                    public void accept(MessageBean messageBean) throws Exception {
                        String title = messageBean.getTitle();
                        String message = messageBean.getMessage();
                        if (!TextUtils.isEmpty(title) && !TextUtils.isEmpty(message)) {
                            showJipushMessagePopwindow(title, message);
                        }
                    }
                }));
    }

    private static void showJipushMessagePopwindow(String title, String message) {
        Activity activity = null;
        while (activity == null) {
            activity = TopActivityHelper.topActivity;
        }
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.common_jpush_popwin, null);
        PopupWindow window = new PopupWindow(view,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT);
        // 设置popWindow弹出窗体可点击，这句话必须添加，并且是true
        window.setFocusable(true);

        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0x00000000);
        window.setBackgroundDrawable(dw);
        backgroundAlpha(activity, 1f);
        // 设置popWindow的显示和消失动画
        window.setAnimationStyle(R.style.jpush_popwindow_anim_style);
//            // 在底部显示

        window.showAtLocation(activity.getWindow().getDecorView(),
                Gravity.TOP, 0, 148);

        //popWindow消失监听方法
        Activity finalActivity = activity;
        window.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(finalActivity, 1f);
            }
        });
        TextView jpushText = view.findViewById(R.id.jpush_text);
        TextView jpushTitle = view.findViewById(R.id.jpush_title);
        TextView jpushTime = view.findViewById(R.id.jpush_time);
        if (!TextUtils.isEmpty(title)) {
            jpushTitle.setVisibility(View.VISIBLE);
            jpushTitle.setText(title);
        }
        jpushText.setText(message);
        jpushTime.setText(TimeUtils.milliseconds2String(System.currentTimeMillis(), new SimpleDateFormat("yyyy.MM.dd HH:mm")));
        final LinearLayout jpushLl = view.findViewById(R.id.jpush_ll);
        final RealtimeBlurView jpushRbv = view.findViewById(R.id.jpush_rbv);
        ViewTreeObserver vto = jpushLl.getViewTreeObserver();
        final ViewGroup.LayoutParams lp = jpushRbv.getLayoutParams();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                jpushLl.getViewTreeObserver().removeOnGlobalLayoutListener(this);
//                    int width=jpushLl.getMeasuredWidth();
                int height = jpushLl.getMinimumHeight();
                lp.height = height;
                jpushRbv.setLayoutParams(lp);
            }
        });
        MLVoiceSynthetize.startSynthesize(activity.getApplication(), "主人，新消息。" + message);
    }

    /**
     * 调节屏幕透明度
     *
     * @param context
     * @param bgAlpha
     */
    protected static void backgroundAlpha(Activity context, float bgAlpha) {
        WindowManager.LayoutParams lp = context.getWindow().getAttributes();
        lp.alpha = bgAlpha;
        context.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        context.getWindow().setAttributes(lp);
    }
}
