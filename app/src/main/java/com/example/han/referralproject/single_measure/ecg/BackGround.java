package com.example.han.referralproject.single_measure.ecg;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

public class BackGround extends View {

	public static DisplayMetrics dm;
	/** x����ÿ�����ص�ռ��ʵ��mm���� */
	public static float xPX2MMUnit = 0.0f;
	/** y����ÿ�����ص�ռ��ʵ��mm���� */
	public static float yPX2MMUnit = 0.0f;
	/** ��ǰview�Ŀ�� (mm) */
	private float width = 0.0f;
	/** ��ǰview�ĸ߶� (mm) */
	public static float height = 0.0f;

	/** ������ߵ���С�߶ȣ�mm�� */
	protected static final int minHeight = 4;

	/** ������ߵ�ʵ�ʸ߶�(mm) */
	public static float gridHeigh = 0.0f;

	/** �ܹ���Ҫ���ٸ����� */
	public static int gridCnt = 0;

	/** ��������Ŀ��(����) */
	public static int mWidth = 0;
	/** ��������ĸ߶�(����) */
	public static int mHeight = 0;

	private int backgroundColor = 0;

	private Paint mPaint;

	public BackGround(Context context) {
		super(context);
		initScreen(context);
	}

	public BackGround(Context context, AttributeSet attrs) {
		super(context, attrs);
		initScreen(context);
	}

	public BackGround(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initScreen(context);
	}

	private void initScreen(Context context) {
		WindowManager wmManager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		dm = new DisplayMetrics();
		wmManager.getDefaultDisplay().getMetrics(dm);
		xPX2MMUnit = 25.4f / dm.densityDpi;
		yPX2MMUnit = 25.4f / dm.densityDpi;
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setStrokeWidth(1);
		backgroundColor = Color.WHITE;
	}

	/**
	 * ���ñ�����ɫ
	 */
	@Override
	public void setBackgroundColor(int backgroundColor) {
		this.backgroundColor = backgroundColor;
	}

	private boolean isDrawBG = true;

	/**
	 * �����Ƿ���Ʊ���
	 * 
	 * @param isDrawBG
	 */
	public void setDrawBG(boolean isDrawBG) {
		this.isDrawBG = isDrawBG;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (!isDrawBG)
			return;
//		canvas.drawColor(backgroundColor);
		if (gridCnt < 2) {
			return;
		}
		mPaint.setStrokeWidth(1);
		mPaint.setColor(Color.parseColor("#66767676"));
		// ����������
		for (float i = 0; i < width; i += gridHeigh) {
			canvas.drawLine(fMMgetPxforX(i), 0, fMMgetPxforX(i), mHeight,
					mPaint);
		}

		int i = gridCnt / 2;
		for (int j = 0; j < i; j++) {
			canvas.drawLine(0, fMMgetPxfory(fPXgetMMforY(mHeight / 2)
					- gridHeigh * j), mWidth,
					fMMgetPxfory(fPXgetMMforY(mHeight / 2) - gridHeigh * j),
					mPaint);
		}

		for (int j = 0; j < i; j++) {
			canvas.drawLine(0, fMMgetPxfory(fPXgetMMforY(mHeight / 2)
					+ gridHeigh * j), mWidth,
					fMMgetPxfory(fPXgetMMforY(mHeight / 2) + gridHeigh * j),
					mPaint);
		}
		drawScale(canvas);
	}

	/**
	 * �Ƿ����������
	 */
	private boolean isDrawScale = true;

	/**
	 * �����Ƿ����������
	 */
	public void setDrawScale(boolean isDrawScale) {
		this.isDrawScale = isDrawScale;
	}

	/** ���Ʊ�� */
	private void drawScale(Canvas canvas) {
		if (gridHeigh > 1 && isDrawScale) {
			int h = mHeight / gridCnt;// һ��ĸ߶�
			mPaint.setColor(Color.BLUE);
			mPaint.setStrokeWidth(dm.density);
			float i = (h * gain) / 2f;
			canvas.drawLine(0, mHeight / 2 - i, h / 2, mHeight / 2 - i, mPaint);
			canvas.drawLine(0, mHeight / 2 + i, h / 2, mHeight / 2 + i, mPaint);
			canvas.drawLine(h / 4, mHeight / 2 - i, h / 4, mHeight / 2 + i,
					mPaint);
		}
	}

	private float gain = 2;

	public float getGain() {
		return gain;
	}

	public void setGain(float gain) {
		if (gain == 0) {
			this.gain = 0.5f;
		} else
			this.gain = gain;
		postInvalidate();
	}

	/**
	 * ͨ��mm��λ��ȡ�þ�����X������ص���
	 * 
	 * @param mm
	 * @return
	 */
	public static float fMMgetPxforX(float mm) {
		return mm / xPX2MMUnit;
	}

	/**
	 * ��ָ�������ص���Ŀ�õ���Щ���ص��mm���ȣ�X�ᣩ
	 * 
	 * @param px
	 * @return
	 */
	public static float fPXgetMMforX(int px) {
		return px * xPX2MMUnit;
	}

	/**
	 * ͨ��mm��λ��ȡ�þ�����Y������ص���
	 * 
	 * @param mm
	 * @return
	 */
	public static float fMMgetPxfory(float mm) {
		return mm / yPX2MMUnit;
	}

	/**
	 * ��ָ�������ص���Ŀ�õ���Щ���ص��mm���ȣ�Y�ᣩ
	 * 
	 * @param px
	 * @return
	 */
	public static float fPXgetMMforY(int px) {
		return px * yPX2MMUnit;
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		setViewHeight(w, h);
	}

	public void setViewHeight(int w, int h) {
		// ����Ļ�����ض�ת��Ϊ���׵�λ
		this.width = fPXgetMMforX(w);
		height = fPXgetMMforY(h);
		mHeight = h;
		mWidth = w;

		gridCnt = 6;
		gridHeigh = height / gridCnt;
		postInvalidate();
	}

	/** ��ȡ�ܹ���Ҫ���ٸ����� */
	public int getGridCnt() {
		return gridCnt;
	}

	/** �����ܹ���Ҫ���ٸ����� */
	public void setGridCnt(int gridCnt) {
		BackGround.gridCnt = gridCnt;
	}

	/** ��ȡ������ߵ�ʵ�ʸ߶� (mm) */
	public float getGridHeigh() {
		return gridHeigh;
	}

	/** ��ȡ������ߵ�ʵ�ʸ߶� (mm) */
	public void setGridHeigh(float gridHeigh) {
		BackGround.gridHeigh = gridHeigh;
	}

}
