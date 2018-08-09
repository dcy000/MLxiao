package com.example.han.referralproject.xindian;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import com.creative.base.InputStreamReader;
import com.creative.base.OutputStreamSender;

import java.io.IOException;


public class ReceiveService extends Service {

	private MyBluetooth myBluetooth;

	@Override
	public void onCreate() {
		super.onCreate();
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		init();
	}

	@Override
	public void onDestroy() {
		unregisterReceiver();
		myBluetooth.disConnected();
		super.onDestroy();
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	private void init() {
		registerReceiver();// ע��㲥������
		// ��ʼ����������, init bluetooth operation
		myBluetooth = new MyBluetooth(this, mHandler);
	}

	/**
	 * �������������ݶ�ȡ�з��͵���Ϣ
	 * updata bluetooth status handler
	 */
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case MyBluetooth.BLUETOOTH_MSG_OPENING: {
				sendBroadcast(BLU_ACTION_STATE_CHANGE, "OPENING");
			}
				break;
			case MyBluetooth.BLUETOOTH_MSG_OPENINGFILE: {
				sendBroadcast(BLU_ACTION_STATE_CHANGE, "OPENINGFILE");
			}
				break;
			case MyBluetooth.BLUETOOTH_MSG_DISCOVERYING: {
				sendBroadcast(BLU_ACTION_STATE_CHANGE, "DISCOVERYING");
			}
				break;
			case MyBluetooth.BLUETOOTH_MSG_CONNECTING: {
				sendBroadcast(BLU_ACTION_STATE_CHANGE, "CONNECTING");
			}
				break;
			case MyBluetooth.BLUETOOTH_MSG_CONNECTED: {
				sendBroadcast(BLU_ACTION_STATE_CHANGE, "CONNECTED");
				startRece(true);
			}
				break;
			case MyBluetooth.BLUETOOTH_MSG_CONNECTFILE: {
				sendBroadcast(BLU_ACTION_STATE_CHANGE, "CONNECTFILE");
			}
			case MyBluetooth.BLUETOOTH_MSG_DISCOVERYED: {
				sendBroadcast(BLU_ACTION_STATE_CHANGE, "DISCOVERYED");
			}
				break;
			default:break;
			}
		}
	};

	/**
	 * ��ʼ�����豸����
	 * start to receive data
	 * 
	 * @param start
	 *            �Ƿ���
	 */
	private void startRece(boolean start) {
		if (start) {
			try {
				if (MyBluetooth.bluSocket != null) {
					String conDeviceName = MyBluetooth.bluSocket
							.getRemoteDevice().getName();
					InputStreamReader reader = new InputStreamReader(
							MyBluetooth.bluSocket.getInputStream());
					OutputStreamSender sender = new OutputStreamSender(
							MyBluetooth.bluSocket.getOutputStream());
					StaticReceive.startReceive(this, conDeviceName, reader,
							sender, mHandler);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			StaticReceive.StopReceive();
		}
	}

	/**
	 * ע��㲥������
	 */
	private void registerReceiver() {
		IntentFilter filter = new IntentFilter();
		// ������ع㲥������������
		filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);

		this.registerReceiver(bluetoothReceiver, filter);
		filter = null;
		filter = new IntentFilter();
		filter.addAction(Intent.ACTION_MEDIA_MOUNTED);
		filter.addAction(Intent.ACTION_MEDIA_EJECT);
		filter.addAction(Intent.ACTION_MEDIA_REMOVED);
		filter.addDataScheme("file");
		this.registerReceiver(bluetoothReceiver, filter);

		filter = new IntentFilter();
		filter.setPriority(Integer.MAX_VALUE);
		filter.addAction(BLU_ACTION_STARTDISCOVERY);
		filter.addAction(BLU_ACTION_STOPDISCOVERY);
		filter.addAction(BLU_ACTION_DISCONNECT);
		filter.addAction(ACTION_BLU_DISCONNECT);
		filter.addAction(ACTION_USER_EXIT);
		this.registerReceiver(bluetoothReceiver, filter);
	}

	/**
	 * ע���㲥������
	 */
	private void unregisterReceiver() {
		this.unregisterReceiver(bluetoothReceiver);
	}

	private BroadcastReceiver bluetoothReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
				int state = intent.getExtras().getInt(
						BluetoothAdapter.EXTRA_STATE);
				if (state == BluetoothAdapter.STATE_OFF) {
					sendBroadcast(ACTION_BLUETOOH_OFF);
				} else if (state == BluetoothAdapter.STATE_ON) {
					// sendBroadcast(ACTION_BLUETOOH_ON);
				}
			} else if (action.equals(Intent.ACTION_MEDIA_MOUNTED)) {// �洢�豸����
				sendBroadcast(ACTION_MEDIA_MOUNTED);
			} else if (action.equals(Intent.ACTION_MEDIA_EJECT)) {// �洢�豸��ж��
				sendBroadcast(ACTION_MEDIA_EJECT);
			} else if (action.equals(Intent.ACTION_MEDIA_REMOVED)) {// �洢�豸���Ƴ�
				sendBroadcast(ACTION_MEDIA_EJECT);
			} else if (action.equals(BLU_ACTION_STARTDISCOVERY)) {
				// ��ʼ�����豸   start connect device
				int deviceName = intent.getExtras().getInt("device");
				myBluetooth.startDiscovery(deviceName);
			} else if (action.equals(BLU_ACTION_STOPDISCOVERY)) {
				// ֹͣ�豸����  stop device discovery
				myBluetooth.stopDiscovery();
			} else if (action.equals(BLU_ACTION_DISCONNECT)
					|| action.equals(ACTION_BLU_DISCONNECT)
					|| action.equals(ACTION_USER_EXIT)) {// �Ͽ����豸������				
				//close receive data
				startRece(false);
				//bluetooth disconnect
				myBluetooth.disConnected();
			}
		}
	};

	/**
	 * �����رչ㲥
	 */
	public static final String ACTION_BLUETOOH_OFF = "bluetooth_off";

	/**
	 * �����򿪹㲥
	 */
	public static final String ACTION_BLUETOOH_ON = "bluetooth_on";

	/**
	 * �洢�豸��ж��
	 */
	public static final String ACTION_MEDIA_EJECT = "media_eject";

	/**
	 * �������ӶϿ�
	 */
	public static final String ACTION_BLU_DISCONNECT = "disconnect";

	/**
	 * �洢�豸�ѹ���
	 */
	public static final String ACTION_MEDIA_MOUNTED = "media_mounted";

	/**
	 * ��������״̬�ı�
	 */
	public static final String BLU_ACTION_STATE_CHANGE = "state_change";

	/**
	 * �����㲥 ��ʼ�����豸
	 */
	public static final String BLU_ACTION_STARTDISCOVERY = "startDiscovery";

	/**
	 * �����㲥 ֹͣ�����豸
	 */
	public static final String BLU_ACTION_STOPDISCOVERY = "stopDiscovery";

	/**
	 * �����㲥 �Ͽ����豸������
	 */
	public static final String BLU_ACTION_DISCONNECT = "disconnect";

	/**
	 * �û��㲥����������ǰ�û��˳���¼
	 */
	public static final String ACTION_USER_EXIT = "userexit";

	/**
	 * ���͹㲥 ��Ҫ����֪ͨӦ������״̬���豸�洢����״̬
	 * 
	 * @param action
	 */
	private void sendBroadcast(String action) {
		Intent intent = new Intent(action);
		this.sendBroadcast(intent);
	}

	/**
	 * ���͹㲥 ��Ҫ����֪ͨӦ��������ǰ������״̬
	 * 
	 * @param arg
	 */
	private void sendBroadcast(String... arg) {
		Intent i = new Intent(arg[0]);
		for (int j = 1; j < arg.length; j++) {
			i.putExtra("arg" + j, arg[j]);
		}
		this.sendBroadcast(i);
	}

}
