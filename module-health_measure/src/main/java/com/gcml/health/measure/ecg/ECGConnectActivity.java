package com.gcml.health.measure.ecg;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.gcml.health.measure.R;


/**
 * progress dialog for bluetooth connecting
 */
public class ECGConnectActivity extends Activity implements OnClickListener{

	/** ��ʾ��ǰ���� */
	private TextView tvStatus;

	/** Բ�ν�����ʾͼƬ */
	private ImageView imgProgress;

	/**
	 * ��Ҫ���ӵ��豸��
	 * connect device name
	 */
	private int conDeviceName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.health_measure_ecg_progressdialog);
		tvStatus = findViewById(R.id.dialog_text);
		imgProgress = findViewById(R.id.dialog_pro);
		imgProgress.setOnClickListener(reConListener);
		conDeviceName = getIntent().getExtras().getInt("device");
		findViewById(R.id.icon_back).setOnClickListener(this);

		IntentFilter filter = new IntentFilter(
				ReceiveService.BLU_ACTION_STATE_CHANGE);
		filter.addAction(ReceiveService.ACTION_BLUETOOH_OFF);
		registerReceiver(receiver, filter);
		// ��API11�Ժ����ֱ�ӵ��ø÷��� ������dialog��ʽ���ֵ�activity����ⲿʱ�Ƿ���ʧ
		if (Build.VERSION.SDK_INT >= 11)
			setFinishOnTouchOutside(false);
		startCon();
	}

	private BroadcastReceiver receiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(ReceiveService.BLU_ACTION_STATE_CHANGE)) {
				tvStatus.setVisibility(View.VISIBLE);
				String state = intent.getExtras().getString("arg1");
				if (state.equals("OPENING")) {
					tvStatus.setText(R.string.connect_opening);
				} else if (state.equals("OPENINGFILE")) {
					tvStatus.setText(R.string.connect_openfail);
					imgProgress.clearAnimation();
					imgProgress.setImageResource(R.drawable.health_measure_bluecon_0);
					dismiss(5);
				} else if (state.equals("DISCOVERYING")) {
					tvStatus.setText(R.string.connect_startseach);
				} else if (state.equals("CONNECTING")) {
					// tvStatus.setText(R.string.connect_startconnect);
					tvStatus.setText(R.string.connect_startconnect);
				} else if (state.equals("CONNECTED")) {
					tvStatus.setText(R.string.connect_success);
					imgProgress.clearAnimation();
					imgProgress.setImageResource(R.drawable.health_measure_bluecon_1);
					dismiss(1);
				} else if (state.equals("CONNECTFILE")
						|| state.equals("DISCOVERYED")) {
					if (ECGBluetooth.bluStatus == ECGBluetooth.BLU_STATUS_NORMAL) {
						tvStatus.setText(R.string.connect_fail);
						imgProgress.clearAnimation();
						imgProgress.setImageResource(R.drawable.health_measure_bluecon_0);
					}
				}
			} else if (action.equals(ReceiveService.ACTION_BLUETOOH_OFF)) {
				tvStatus.setText(R.string.connect_close);
				imgProgress.clearAnimation();
				imgProgress.setImageResource(R.drawable.health_measure_bluecon_0);
				sendBroadcast(new Intent(
						ReceiveService.BLU_ACTION_STOPDISCOVERY));
				sendBroadcast(new Intent(ReceiveService.BLU_ACTION_DISCONNECT));
			}
		}
	};

	private void startCon() {
		imgProgress.setImageResource(R.drawable.health_measure_ecg_img_progress);
		imgProgress.startAnimation(ECGAnimationFactory.rotate2Self());
		sendBroadcast(new Intent(ReceiveService.BLU_ACTION_STARTDISCOVERY)
				.putExtra("device", conDeviceName));
	}

	private OnClickListener reConListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (!ECGBluetooth.isConnected) {
				startCon();
			}
		}
	};

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(receiver);
		sendBroadcast(new Intent(ReceiveService.BLU_ACTION_STOPDISCOVERY));
	}

	@Override
	public void onClick(View v) {
		int i = v.getId();
		if (i == R.id.icon_back) {
			finish();

		}
	}

	/**
	 * ��ǰ�������ӵ��豸��
	 */
	public static final String CONNECTED_DEVICE_NAME = "CONNAME";

	public void dismiss(final long time) {
		new Thread() {
			public void run() {
				try {
					Thread.sleep(time * 1000);
					BluetoothDevice d = ECGBluetooth.getConDevice();
					if (ECGBluetooth.isConnected && d != null) {
						Intent i = new Intent();
						i.putExtra(CONNECTED_DEVICE_NAME, d.getName());
						ECGConnectActivity.this.setResult(1, i);
					}
					ECGConnectActivity.this.finish();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
        }.start();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (ECGBluetooth.bluStatus == ECGBluetooth.BLU_STATUS_CONNECTING) {
				// Toast.makeText(ConnectActivity.this,
				// R.string.connect_connecting, Toast.LENGTH_SHORT).showShort();
				return false;
			} else {
				sendBroadcast(new Intent(
						ReceiveService.BLU_ACTION_STOPDISCOVERY));
			}
		}
		return super.onKeyDown(keyCode, event);
	}

}
