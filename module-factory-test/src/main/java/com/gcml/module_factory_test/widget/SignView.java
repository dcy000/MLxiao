package com.gcml.module_factory_test.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.gcml.module_factory_test.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class SignView extends View {
 
	private Paint linePaint;// 画笔
	private ArrayList<Path> lines;// 写字的笔迹，支持多笔画
	private int lineCount;// 记录笔画数目
	private final int DEFAULT_LINE_WIDTH = 10;// 默认笔画宽度
	private int lineColor = Color.BLACK;// 默认字迹颜色（黑色）
	private float lineWidth = DEFAULT_LINE_WIDTH;// 笔画宽度

	private int mLastMotionX, mLastMotionY;
	// 是否移动了
	private boolean isMoved;
	// 是否释放了
	private boolean isReleased;
	// 计数器，防止多次点击导致最后一次形成longpress的时间变短
	private int mCounter;
	// 长按的runnable
	private Runnable mLongPressRunnable;
	// 移动的阈值
	private static final int TOUCH_SLOP = 20;

	public SignView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		init();// 普通初始化
		initLinePpaint();
	}
 
	public SignView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		if (attrs != null) {
			TypedArray a = context.obtainStyledAttributes(attrs,
					R.styleable.SignView);
 
			parseTyepdArray(a);
		}
		init();// 普通初始化
		initLinePpaint();
	}
 
	public SignView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub
		if (attrs != null) {
			TypedArray a = context.obtainStyledAttributes(attrs,
					R.styleable.SignView);
			parseTyepdArray(a);
		}
 
		init();// 普通初始化
		initLinePpaint();
	}
 
	private void parseTyepdArray(TypedArray a) {
		lineColor = a.getColor(R.styleable.SignView_lineColor, Color.RED);
		lineWidth = a.getDimension(R.styleable.SignView_lineWidth, 25);
		a.recycle();
	}
 
	private void init() {
		lines = new ArrayList<Path>();
		mLongPressRunnable = new Runnable() {

			@Override
			public void run() {
				System.out.println("thread");
				System.out.println("mCounter--->>>"+mCounter);
				System.out.println("isReleased--->>>"+isReleased);
				System.out.println("isMoved--->>>"+isMoved);
				mCounter--;
				// 计数器大于0，说明当前执行的Runnable不是最后一次down产生的。
				if (mCounter > 0 || isReleased || isMoved)
					return;
				performLongClick();// 回调长按事件
			}
		};
	}
 
	/**
	 * 初始化画笔
	 */
	private void initLinePpaint() {
		linePaint = new Paint();
		linePaint.setColor(lineColor);// 画笔颜色
		linePaint.setStrokeWidth(lineWidth);// 画笔宽度
		linePaint.setStrokeCap(Paint.Cap.ROUND);// 设置笔迹的起始、结束为圆形
		linePaint.setPathEffect(new CornerPathEffect(200));// PahtEfect指笔迹的风格，CornerPathEffect在拐角处添加弧度，弧度半径50像素点
		linePaint.setStyle(Paint.Style.STROKE);// 设置画笔风格
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		int x = (int) event.getX();
		int y = (int) event.getY();
		/**
		 * 考虑到这个view会出现在lib工程里，因此使用if else
		 */
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			mLastMotionX = x;
			mLastMotionY = y;
			mCounter++;
			isReleased = false;
			isMoved = false;
			postDelayed(mLongPressRunnable, 1000);// 按下 3秒后调用线程

			Path path = new Path();
			path.moveTo(event.getX(), event.getY());
			lines.add(path);
			lineCount = lines.size();
		} else if (event.getAction() == MotionEvent.ACTION_MOVE) {
			if (Math.abs(mLastMotionX - x) > TOUCH_SLOP || Math.abs(mLastMotionY - y) > TOUCH_SLOP) {
				// 移动超过阈值，则表示移动了
				isMoved = true;
			}

			lines.get(lineCount - 1).lineTo(event.getX(), event.getY());
			invalidate();
		} else if (event.getAction() == MotionEvent.ACTION_UP) {
			// 释放了
			isReleased = true;
		}
		return true;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		if (lines != null && lines.size() > 0) {
			for (Path path : lines) {
				canvas.drawPath(path, linePaint);
			}
		}
	}
 
	// 开放设置画笔颜色和宽度的接口
 
	/**
	 * 开放设置画笔颜色的接口
	 * 
	 * @param lineColor
	 */
	public void setLineColor(int lineColor) {
		this.lineColor = lineColor;
		linePaint.setColor(lineColor);
	}
 
	/**
	 * 开放设置画笔宽度的接口
	 * 
	 * @param lineWidth
	 */
	public void setLineWidth(float lineWidth) {
		this.lineWidth = lineWidth;
		linePaint.setStrokeWidth(lineWidth);
	}
 
	/**
	 * 清空输入
	 */
	public void clearPath() {
		lines.removeAll(lines);
		invalidate();
	}

	/**
	 * 将图片保存到文件
	 */
	public boolean saveImageToFile(String filePath) {
		try {
			File file = new File(filePath);
			if (!file.exists()) {
				file.mkdirs();
			}
			String localFile=file.getAbsolutePath()+"/"+ System.currentTimeMillis()+".png";
			File f=new File(localFile);

			FileOutputStream fos=new FileOutputStream(f);
			getImage().compress(Bitmap.CompressFormat.PNG, 100, fos);
			fos.flush();
			fos.close();
			return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 将View保存为Bitmap
	 */
	public Bitmap getImage() {
		Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(),
				Bitmap.Config.RGB_565);
		Canvas canvas = new Canvas(bitmap);
		// 绘制背景
		Drawable bgDrawable = getBackground();
		if (bgDrawable != null) {
			bgDrawable.draw(canvas);
		} else {
			canvas.drawColor(Color.WHITE);
		}
		// 绘制View视图内容
		draw(canvas);
		return bitmap;
	}

}