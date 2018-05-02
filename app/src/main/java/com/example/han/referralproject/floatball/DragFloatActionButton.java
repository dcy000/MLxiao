package com.example.han.referralproject.floatball;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.ImageView;

import com.example.han.referralproject.R;

/**
 * Created by lenovo on 2018/3/28.
 */

@SuppressLint("AppCompatCustomView")
public class DragFloatActionButton extends ImageView {
    interface onClickListener {
        void onclick();
    }

    onClickListener listener;

    public void setListener(onClickListener listener) {
        this.listener = listener;
    }

    public static String TAG = "DragFloatActionButton";
    private Context context;

    float lastX, lastY;
    float originX, originY;
    int screenWidth;
    int screenHeight;
    private int originWidth;

    private WindowManager windowManager;
    //    // 此windowManagerParams变量为获取的全局变量，用以保存悬浮窗口的属性
    private WindowManager.LayoutParams windowManagerParams;


    public DragFloatActionButton(Context context) {
        this(context, null);
    }

    public DragFloatActionButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DragFloatActionButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;

        Point screenSize = getScreenSize(context);
        screenWidth = screenSize.x;
        screenHeight = screenSize.y;
        windowManager = (WindowManager) getContext().getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
    }
    public static Point getScreenSize(Context context) {
        Point point = new Point();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getSize(point);
        return point;
    }

    public int getOriginWidth() {
        return originWidth;
    }

    public void setOriginWidth(int originWidth) {
        this.originWidth = originWidth;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        windowManagerParams = (WindowManager.LayoutParams) this.getLayoutParams();
        //获取到状态栏的高度
        Rect frame = new Rect();
        getWindowVisibleDisplayFrame(frame);
        int ea = event.getAction();
        switch (ea) {
            case MotionEvent.ACTION_DOWN:
                lastX = event.getRawX();// 获取触摸事件触摸位置的原始X坐标
                lastY = event.getRawY();
                originX = lastX;
                originY = lastY;
                break;
            case MotionEvent.ACTION_MOVE:
                float dx = event.getRawX() - lastX;
                float dy = event.getRawY() - lastY;
                windowManagerParams.x += dx;
                windowManagerParams.y += dy;
                showAllBtn();
                lastX = (int) event.getRawX();
                lastY = (int) event.getRawY();
                break;
            case MotionEvent.ACTION_UP:
                float lastMoveDx = Math.abs(event.getRawX() - originX);
                float lastMoveDy = Math.abs(event.getRawY() - originY);
                if (lastMoveDx < 10 && lastMoveDy < 10) {
                    //移动距离太小，视为点击，
                    if (listener != null) {
                        listener.onclick();
                    }
                    return false;
                } else {
                    updateViewLayout(event);
                    return true;
                }

        }
        return false;
    }

    /**
     * 显示整个图标
     */
    public void showAllBtn() {
        setImageDrawable(getResources().getDrawable(R.drawable.icons));
        windowManagerParams.width = originWidth;
        windowManagerParams.height = originWidth;
        windowManager.updateViewLayout(this, windowManagerParams); // 刷新显示
    }

    /**
     * 悬浮按钮显示在左边
     */
    private void showInLeft() {
        setImageDrawable(getResources().getDrawable(R.drawable.icons));
        windowManagerParams.x = 0;
        windowManagerParams.width = originWidth;
        windowManagerParams.height = originWidth;
        windowManager.updateViewLayout(this, windowManagerParams); // 刷新显示
    }

    /**
     * 悬浮按钮显示在右边
     */
    private void showInRight() {
        setImageDrawable(getResources().getDrawable(R.drawable.icons));
        windowManagerParams.width = originWidth;
        windowManagerParams.height = originWidth;
        windowManagerParams.x = screenWidth - windowManagerParams.width;
        windowManager.updateViewLayout(this, windowManagerParams); // 刷新显示
    }

    /**
     * 悬浮按钮显示在上面
     */
    private void showInTop() {
        setImageDrawable(getResources().getDrawable(R.drawable.icons));
        windowManagerParams.y = 0;
        windowManagerParams.width = originWidth;
        windowManagerParams.height = originWidth;
        windowManager.updateViewLayout(this, windowManagerParams); // 刷新显示
    }

    /**
     * 悬浮按钮显示在下面
     */
    private void showInBottom() {
        setImageDrawable(getResources().getDrawable(R.drawable.icons));
        windowManagerParams.width = originWidth;
        windowManagerParams.height = originWidth;
        windowManagerParams.y = screenHeight - windowManagerParams.width;
        windowManager.updateViewLayout(this, windowManagerParams); // 刷新显示
    }

    /**
     * 更新悬浮图标
     *
     * @param event 手动移动事件
     */
    public void updateViewLayout(MotionEvent event) {
        Point center = new Point(screenWidth / 2, screenHeight / 2); //屏幕中心点
        float xOffset, yOffset;//以屏幕中心点为原点，X轴和Y轴上的偏移量
        if (event != null) {//手动移动的
            xOffset = event.getRawX() - center.x;
            yOffset = event.getRawY() - center.y;
        } else {//自动隐藏
            xOffset = lastX - center.x;
            yOffset = lastY - center.y;
        }
        if (Math.abs(xOffset) >= Math.abs(yOffset)) {//向左或向右缩进隐藏
            if (xOffset <= 0) { //向左缩进
                showInLeft();
            } else {
                showInRight();
            }
        } else {//向上或向下缩进隐藏
            if (yOffset <= 0) {//向上缩进
                showInTop();
            } else {
                showInBottom();
            }
        }
    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        Point screenSize = getScreenSize(context);
        if (screenWidth != screenSize.x) {//屏幕旋转切换
            screenWidth = screenSize.x;
            screenHeight = screenSize.y;
            lastY = windowManagerParams.x;
            lastX = windowManagerParams.y;
            windowManagerParams.x = (int) lastX;
            windowManagerParams.y = (int) lastY;
            updateViewLayout(null);
        }
    }


}
