package com.example.han.referralproject.single_measure.ecg;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.creative.base.BaseDate;
import com.creative.base.InputStreamReader;
import com.creative.base.OutputStreamSender;
import com.creative.ecg.ECG;
import com.creative.ecg.IECGCallBack;
import com.example.han.referralproject.util.ECGUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;


public class StaticReceive {
	
	/**
	 * PC80B系列协议解析
	 * analyse PC80B protocal 
	 */
	private static ECG ecg;

	/**
	 * 通知上层各种数据消息
	 * updata UI handler
	 */
	private static Handler mHandler;
	protected static Context mContext;
	
	/** buffer of saving wave ,write to file */
	public static List<BaseDate.Wave> mWaveBuffer = new ArrayList<BaseDate.Wave>();
	
	
	/**
	 * 开始接收数据
	 * start to receive data
	 * @param bluName
	 */
	public static void startReceive(Context context, String bluName,
									InputStreamReader iReader, OutputStreamSender iSender, Handler _handler) {
		if (bluName != null && !bluName.equals("")) {
			start = true;
			mHandler = _handler;
			mContext = context;
			if (bluName.equals("PC80B")) {
				ecg = new ECG(iReader, iSender, new ECGCallBack());
				ecg.Start();
				ecg.QueryDeviceVer();
				
			}
		}
	}

	/**
	 * 停止接收数据
	 * stop receiving data
	 */
	public static void StopReceive() {
		start = false;
		if (ecg != null) {
			ecg.Stop();
			ecg = null;
		}
		HWMajor = HWMinor = SWMajor = SWMinor = ALMajor = ALMinor = 0;
	}

	/**
	 * 暂停数据接收
	 * pause receiving data
	 */
	public static void Pause() {
		pause = true;
		if (ecg != null) {
			ecg.Pause();
		}
	}

	/**
	 *  恢复数据接收
	 *  recover receiving data
	 */
	public static void Continue() {
		pause = false;
		if (ecg != null) {
			ecg.Continue();
		}
	}

	public static boolean pause = false;

	public static boolean start = false;

	public static void setmHandler(Handler mHandler) {
		StaticReceive.mHandler = mHandler;
	}

	/**
	 *  数据类型key——心电文件
	 */
	public static final String DATATYPEKEY_ECG_FILE = "ecgFile";
	/**
	 * 数据类型key——心电波形
	 */
	public static final String DATATYPEKEY_ECG_WAVE = "ecgwave";

	/**
	 * 设备数据消息——设备ID
	 */
	public static final int MSG_DATA_DEVICE_ID = 0x201;

	/**
	 *  设备数据消息——设备版本信息
	 *  device version
	 */
	public static final int MSG_DATA_DEVICE_VS = 0x202;

	/**
	 *  设备数据消息——心电波形数据
	 *  ECG WAVE
	 */
	public static final int MSG_DATA_ECG_WAVE = 0x20d;

	/**
	 *  设备数据消息——电池电量
	 *  Battery Power
	 */
	public static final int MSG_DATA_BATTERY = 0x20e;
	/**
	 * 设备数据消息——心电测量状态改变
	 * ecg measure status change
	 */
	public static final int MSG_DATA_ECG_STATUS_CH = 0x209;

	/**
	 * 设备数据消息——搏动标记
	 * pulse flag
	 */
	public static final int MSG_DATA_PULSE = 0x20f;

	/**
	 * 超时 
	 * time out
	 */
	public static final int MSG_DATA_TIMEOUT = 0x210;
	
	/**
	 * 终端离线
	 * termimal offline
	 */
	public static final int MSG_TERMINAL_OFFLINE = 0x212;

	/**
	 * 设备版本信息
	 * device version info
	 */
	public static int HWMajor, HWMinor, SWMajor, SWMinor, ALMajor, ALMinor;

	/**
	 * 保存ECG波形数据,用于绘制波形
	 * save for drawing wave of ECG
	 */
	public static List<BaseDate.Wave> DRAWDATA = new ArrayList<BaseDate.Wave>();


	private static class ECGCallBack implements IECGCallBack {

		@Override
		public void OnConnectLose() {
			Log.d("TAG", "-->bluetooth disconnect..");
			mHandler.sendEmptyMessage(MSG_TERMINAL_OFFLINE );
		}

		@Override
		public void OnGetDeviceVer(int nHWMajor, int nHWMinor, int nSWMajor,
				int nSWMinor, int nALMajor, int nALMinor) {
			HWMajor = nHWMajor;
			HWMinor = nHWMinor;
			SWMajor = nSWMajor;
			SWMinor = nSWMinor;
			ALMajor = nALMajor;
			ALMinor = nALMinor;
		}

		//终端应答有响应：每隔1秒发送心跳报文,电量信息.
		// terminal responsed, send powerInfo per-second 
		@Override
		public void OnGetPower(int nPower) {
			mHandler.obtainMessage(MSG_DATA_BATTERY, nPower, 0).sendToTarget();
		}

		@Override
		public void OnGetRealTimeResult(String time, int nTransMode,
				int nResult, int arg3) {
			Message msg = mHandler.obtainMessage();
			msg.what = MSG_DATA_ECG_STATUS_CH;
			msg.arg1 = 6;//result msg
			Bundle data = new Bundle();
			data.putInt("nTransMode", nTransMode);
			data.putInt("nResult", nResult);
			data.putInt("nHR", arg3);
			data.putString("time", time);
			msg.setData(data);
			mHandler.sendMessage(msg);
			//nResult,nHR,time can be saved to xml
			//writeResutl2xml();
			
			//save wave data to file
			writeWave2File();
		}

		@Override
		public void OnGetRealTimeMeasure(boolean arg0, BaseDate.ECGData arg1, int arg2,
										 int arg3, int arg4, int arg5) {
			Message msg = mHandler.obtainMessage();
			msg.what = MSG_DATA_ECG_STATUS_CH;
			msg.arg1 = 5;
			Bundle data = new Bundle();
			data.putBoolean("bLeadoff", arg0);
			data.putInt("nTransMode", arg2);
			data.putInt("nHR", arg3);
			data.putInt("nPower", arg4);
			data.putInt("nGain", arg5);
			msg.setData(data);
			mHandler.sendMessage(msg);
			// save wave for drawing
			DRAWDATA.addAll(arg1.data); //arg1.data.size() = 25
			//Logg.d("TAG","wave data->"+arg1.data);
			mWaveBuffer.addAll(arg1.data); //total of realTime wave are 4500 point 
		}

		@Override
		public void OnGetRealTimePrepare(boolean arg0, BaseDate.ECGData arg1, int arg2) {
			Message msg = mHandler.obtainMessage();
			msg.what = MSG_DATA_ECG_STATUS_CH;
			msg.arg1 = 4;
			Bundle data = new Bundle();
			data.putBoolean("bLeadoff", arg0);
			data.putInt("nGain", arg2);
			DRAWDATA.addAll(arg1.data); //arg1.data.size() = 25
			msg.setData(data);
			mHandler.sendMessage(msg);	
						
			mWaveBuffer.addAll(arg1.data);//total of prepare wave are 2250 point 	
		}

		@Override
		public void OnGetRequest(String sDeviceID, String sProductID,
				int nSmoothingMode, int nTransMode) {
			mHandler.obtainMessage(MSG_DATA_ECG_STATUS_CH, 7, nSmoothingMode,
					nTransMode).sendToTarget();
		}

		@Override
		public void OnGetFileTransmit(int arg0, Vector<Integer> arg1) {
			System.out.println("OnGetFileTransmit " + arg0);
			mHandler.obtainMessage(MSG_DATA_ECG_STATUS_CH, arg0, 0, arg1)
					.sendToTarget();
		}

		@Override
		public void OnReceiveTimeOut(int code) {
			Log.d("frf", "OnReceiveTimeOut code:"+code);
			mHandler.obtainMessage(MSG_DATA_ECG_STATUS_CH, MSG_DATA_TIMEOUT, 0,
					0).sendToTarget();
		}
	}

//----------- save wave ----	
	public static String filePath = Environment.getExternalStorageDirectory()+"/PC80B";
	public static String fileName = "1.ECG";
	private static void writeWave2File(){
		new Thread(){
			public void run() {
				List<Integer> datas = new ArrayList<Integer>();
				while (mWaveBuffer.size()>0) {
					BaseDate.Wave wave = mWaveBuffer.remove(0);
					datas.add(wave.data);//存放波形Y轴数据, Y axis of wave 
				}
				//Logg.e("TAG", "write size:"+datas.size());
				ECGUtil.writeFile(filePath, fileName, datas);
			}
        }.start();
	}
		
}
