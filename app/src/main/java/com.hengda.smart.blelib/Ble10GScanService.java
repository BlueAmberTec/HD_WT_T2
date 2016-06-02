package com.hengda.smart.blelib;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Currency;

import org.kymjs.kjframe.utils.KJLoger;

import android.app.Service;
import android.content.Intent;
import android.net.LocalServerSocket;
import android.net.LocalSocket;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

/**
 * @author pengyiming
 * @note 启动localSocketServer
 *
 */

public class Ble10GScanService extends Service {
	/* 数据段begin */

	private final String TAG = "SOCKET_GET";
	BufferedReader mBufferedReader = null;
	String readString = null;
	InputStream bleIns = null;
	int receiveLen = 0;
	byte[] data;
	static int serial_max = 1;

	byte[][] data_all = new byte[6][50];
	int serial = 0;
	private HD10GBeacon hdBeacon = new HD10GBeacon();
	private ServerSocketThread mServerSocketThread;
	// 声明原生函数 参数为String类型 返回类型为String
	private native void startHcitool();
	private native void stopHcitool();
	ArrayList<byte[]> tempArrayList = new ArrayList<byte[]>();

	//传号广播
	public static final String ACTION_BLE_BEST="ACTION_BLE_BEST";
	public static final String ACTION_BLE_DETIAL="ACTION_BLE_DETAIL";
	public static final String ACTION_BLE_CHULI="ACTION_BLE_CHULI";
	public static final String TAG_BLE_NO="TAG_BLE_NO";
	public static final String TAG_BEACON = "TAG_BEACON";
	private Intent bleIntent; //实时Intent
	private Intent bleDetailIntent;//详情Intent
	private Intent bleChuliIntent;//处理后结果Intent
	//收号处理
	private IBeaconNoUtil beaconUtil;
	private int limitRssi =-69;
	@Override
	public void onCreate() {
		super.onCreate();
		System.loadLibrary("zsljni"); // /system/lib/libzsljni.so
		mServerSocketThread = new ServerSocketThread();
		mServerSocketThread.start();
		startHcitool();
		bleIntent=new Intent(ACTION_BLE_BEST);
		bleDetailIntent=new Intent(ACTION_BLE_DETIAL);
		bleChuliIntent=new Intent(ACTION_BLE_CHULI);
		beaconUtil=new IBeaconNoUtil(10, 8);
		handler.sendEmptyMessageDelayed(1, 500);
		handler.sendEmptyMessageDelayed(2, 2000);
	}


	private Handler handler =new Handler(){

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
				case 1:
					if(hdBeacon.getRssi()>limitRssi){
						beaconUtil.addBleNo(hdBeacon.getMinor());
					}
					handler.sendEmptyMessageDelayed(1,160);
					break;
				case 2:
					bleChuliIntent.putExtra(TAG_BLE_NO, beaconUtil.getBestBeaconNo());
					sendBroadcast(bleChuliIntent);
					handler.sendEmptyMessageDelayed(2,1500);
					break;
				default:
					break;
			}
		}
	};

	@Override
	public void onDestroy() {
		super.onDestroy();
		stopHcitool();
		mServerSocketThread.stopRun();
	}

	private class ServerSocketThread extends Thread {

		private boolean keepRunning = true;
		private LocalServerSocket serverSocket;

		private void stopRun() {
			keepRunning = false;
		}

		@Override
		public void run() {
			try {
				serverSocket = new LocalServerSocket("pym_local_socket");
			} catch (IOException e) {
				e.printStackTrace();

				keepRunning = false;
			}

			while (keepRunning) {

				try {
					LocalSocket interactClientSocket = serverSocket.accept();
					interactClientSocket.setReceiveBufferSize(50);
					mBufferedReader = new BufferedReader(new InputStreamReader(
							interactClientSocket.getInputStream()));
					bleIns = interactClientSocket.getInputStream();
					receiveLen = interactClientSocket.getReceiveBufferSize();
					data = new byte[100];
					bleIns.read(data);
					KJLoger.debug(HD_UTIL.getHexStringByByteArray(data));
//					try {
//						sleep(100);
//					} catch (InterruptedException e) {
//						e.printStackTrace();
//					}
					data_all[serial] = data;

					if (serial == serial_max - 1)
						serial = 0;
					else
						serial++;

					// int line = FindStrongRssi();
					int line = 0;
					KJLoger.debug(HD_UTIL.getHexStringByByteArray(data));
					hdBeacon = HDBLEUtil.getHD10GBeacon(data_all[line]);
					bleIntent.putExtra(TAG_BLE_NO,hdBeacon.getMajor());
//					beaconUtil.addBleNo(hdBeacon.getMajor());
					Bundle bundle =new Bundle();
					bundle.putSerializable(TAG_BEACON, hdBeacon);
					bleIntent.putExtras(bundle);
					sendBroadcast(bleIntent);

				} catch (IOException e) {
					e.printStackTrace();
					keepRunning = false;
				}
			}

			if (serverSocket != null) {
				try {
					serverSocket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}


	public int FindStrongRssi() {
		int rssi = 0;
		int num = 0;
		int i;
		for (i = 0; i < serial_max; i++)
			if (rssi < data_all[i][(data_all[i][0])]) {
				rssi = data_all[i][(data_all[i][0])];
				num = i;
			}
		return num;
	}

	public static String bytesToHexString(byte[] src) {

		StringBuilder stringBuilder = new StringBuilder();

		if (src == null || src.length <= 0) {

			return null;

		}

		for (int i = 0; i < src.length; i++) {

			int v = src[i] & 0xFF;

			String hv = Integer.toHexString(v);

			if (hv.length() < 2) {

				stringBuilder.append(0);

			}
			stringBuilder.append(hv);

		}

		return stringBuilder.toString().toUpperCase();

	}


	/* 内部类end */
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
}


