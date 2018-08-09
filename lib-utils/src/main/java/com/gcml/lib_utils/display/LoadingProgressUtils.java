package com.gcml.lib_utils.display;

import android.content.Context;

import com.kaopiz.kprogresshud.KProgressHUD;

public class LoadingProgressUtils {
    public interface LABEL {
        String REFRESHING = "刷新中...";
        String UPLOADING = "上传中...";
        String ON_CLOSE_CAMERA = "正在关闭摄像头";
        String ON_OPEN_CAMERA = "正在打开摄像头";
        String ON_ANALYSIS_DATA = "正在分析数据...";
    }

    private static Context mContext;
    private static KProgressHUD hud;

    /**
     * 没有提示文字
     *
     * @param context
     */
    public static void showViewWithNonLabel(Context context) {
        mContext = context;
        if (hud == null) {
            hud = KProgressHUD.create(mContext)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setCancellable(false)
                    .setDimAmount(0.5f)
                    .show();
        } else {
            hud.show();
        }
    }

    /**
     * 有提示文字
     *
     * @param context
     * @param title
     */
    public static void showViewWithLabel(Context context, String title) {
        mContext = context;
        if (hud == null) {
            hud = KProgressHUD.create(mContext)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel(title)
                    .setCancellable(false)
                    .setDimAmount(0.5f)
                    .show();
        } else {
            hud.setLabel(title);
            hud.show();
        }
    }

    public static void dismissView() {
        if (hud != null) {
            if (hud.isShowing()) {
                hud.dismiss();
            }
            mContext = null;
            hud = null;
        }
    }
}
