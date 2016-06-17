package com.hengda.smart.stc;

import android.app.AlertDialog;
import android.app.Service;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import com.hengda.smart.application.Application;
import com.hengda.smart.blelib.CommonUtil;
import com.orhanobut.logger.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidParameterException;

import android_serialport_api.SerialPort;

/**
 *
 * @Description STC数传服务接收底层上传的数据
 * @author wzq
 * @date 2015-6-11 下午3:22:19
 * @update (date)
 * @version V1.0
 */
public class ControlService extends Service implements StcManager {
	// 1==1 stc用
	private Application mApplication;
	protected SerialPort mSerialPort;
	protected OutputStream mOutputStream;
	private InputStream mInputStream;
	private ReadThread mReadThread;
	private boolean runFlag = true;// 线程标志
	private StcLauncher stcLauncher; // 发送STC指令UTIL
	// 回传参数字符品串
	private byte[] tem1 = new byte[4];
	private byte[] tem2 = new byte[4];

	// 2 ==handler MsgWhat
	private static final int IR_AUTONUM_ = 1;
	private static final int RFID_AUTONUM_ = 2;

	private Intent intent_IR = null;
	private Intent intent_RFID = null;
	private int errorChannel;
	private int rfid_num = 0;
	private int ir_num = 0;
	public static final String ACTION_REICEIVE_RFID = "ACTION_REICEIVE_RFID_PLAY";
	private Intent rfidReceiveIntent;

	// 4== 服务中的其他工具
	// 弹框工具
	// 存储配置工具
	private int i = 0;
	// 当前上传成功的逻辑群主编号
	private int currentSucAutoNo = 0;
	private int UPLOAD_DELEY = 4000;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		mApplication = (Application) getApplication();
		stcLauncher = new StcLauncher(mApplication, getApplicationContext());
		try {
			mSerialPort = mApplication.getSerialPort();
			mInputStream = mSerialPort.getInputStream();
			mReadThread = new ReadThread();
			this.start();
		} catch (SecurityException e) {
			DisplayError("You do not have read/write permission to the serial port");
		} catch (IOException e) {
			DisplayError("he serial port can not be opened for an unknown reason");
		} catch (InvalidParameterException e) {
			DisplayError("Please configure your serial port first");
		}
		Logger.v("控制服务开启");
		initIntent();
	}


	private void initIntent() {
		intent_IR = new Intent("HENGDA.IR_AUTONUM_.BROADCAST");
		intent_RFID = new Intent("HENGDA.RFID_AUTONUM_.BROADCAST");
		rfidReceiveIntent = new Intent(ACTION_REICEIVE_RFID);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return super.onStartCommand(intent, flags, startId);
	}

	/**
	 * 启动线程
	 */
	private void start() {
		mReadThread.start();
	}

	/**
	 * 停止线程
	 */
	private void stop() {
		runFlag = false;
	}

	/**
	 * @Description 获取底层传入数据的线程
	 * @author wzq
	 * @date 2015-6-11 下午3:23:25
	 * @update (date)
	 * @version V1.0
	 */

	private class ReadThread extends Thread {
		@Override
		public void run() {
			super.run();
			while (true) // !isInterrupted()
			{
				int size;
				byte[] buffer = new byte[16];
				try {
					if (mInputStream == null) {
						return;
					}
					size = 0;
					size = mInputStream.read(buffer);

					if (size > 0) {
						if (size == 4) {
							if (buffer[0] == (byte) 0xAA) {
								tem1 = subBytes(buffer, 0, 4);
							} else {
								tem2 = subBytes(buffer, 0, 4);
								if (tem1 != null) {
									onDataReceived(byteMerger(tem1, tem2), 8);
								}
							}
						} else {
							onDataReceived(buffer, size);
						}
					}
					try {
						sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				} catch (IOException e) {
					e.printStackTrace();
					return;
				}
			}
		}
	}

	private void DisplayError(String  message) {
		AlertDialog.Builder b = new AlertDialog.Builder(this);
		b.setTitle("Error");
		b.setMessage(message);
		b.setPositiveButton("OK", new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				stopSelf();
			}
		});
		b.show();
	}

	@Override
	public void onDataReceived(byte[] buffer, int size) {
		System.err.println(CommonUtil.bytesToHexString(buffer) + "=====" + size);
		for (int m = 0; m < 8; m++) {
			if (buffer[m] == (byte) 0xAA && buffer[m + 1] == (byte) 0x55) {
				// rfid
				if ((buffer[m + 2] == (byte) 0x05)
						&& buffer[m + 6] == (buffer[m + 4] ^ buffer[m + 5])) {
					rfid_num = (buffer[m + 4] & 0x0FF) * 256
							+ (buffer[m + 5] & 0x0FF);

					if (rfid_num == 2003) {
					}
					if (rfid_num == 2001 || rfid_num == 2002) {
					}
					mHandler.sendEmptyMessage(RFID_AUTONUM_);
					break;
				} else if ((buffer[m + 2] == (byte) 0x01)
						&& buffer[m + 3] == (byte) 0x02) { // 音频模块返回
					if ((buffer[m + 4] == (byte) 0x01)
							&& buffer[m + 6] == (buffer[m + 4] ^ buffer[m + 5])) { // 800M
						// 模块打开成功

					} else if ((buffer[m + 4] == (byte) 0x02)
							&& buffer[m + 6] == (buffer[m + 4] ^ buffer[m + 5])) { // 800M模块打开失败
					}

				} else if ((buffer[m + 2] == (byte) 0x02)
						&& buffer[m + 3] == (byte) 0x02) { // 数传模块返回
					Logger.v(CommonUtil.bytesToHexString(buffer));
					if ((buffer[m + 4] == (byte) 0x01)
							&& buffer[m + 7] == (buffer[m + 4] ^ buffer[m + 5] ^ buffer[m + 6])) { // 红外自动适配
						int irNo = (buffer[m + 5] & 0xFF) * 256
								+ (buffer[m + 6] & 0xFF); // 群组号

					} else if ((buffer[m + 4] == (byte) 0x05) // 20151028
							// 增加占用通道编号回传
							&& buffer[m + 7] == (buffer[m + 4] ^ buffer[m + 5] ^ buffer[m + 6])) {
						int hostNo = (buffer[m + 5] & 0xFF) * 256
								+ (buffer[m + 6] & 0xFF); // 群组号

					} else if ((buffer[m + 4] == (byte) 0x06)// 2016/6/2 增加异常显示
							&& buffer[m + 7] == (buffer[m + 4] ^ buffer[m + 5] ^ buffer[m + 6])) {

					} else if ((buffer[m + 4] == (byte) 0x02)
							&& buffer[m + 7] == (buffer[m + 4] ^ buffer[m + 5] ^ buffer[m + 6])) { // 首次冲突

					} else if ((buffer[m + 4] == (byte) 0x03)
							&& buffer[m + 7] == (buffer[m + 4] ^ buffer[m + 5] ^ buffer[m + 6])) { // 中途冲突

					}
				}
			}
		}

	}


	Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
				case IR_AUTONUM_:
					if (ir_num > 0) {
						intent_IR.putExtra("IR_", ir_num);
						sendBroadcast(intent_IR);
					}
					break;
				case RFID_AUTONUM_:
					if (rfid_num > 0) {
						intent_RFID.putExtra("RFID_", rfid_num);
						sendBroadcast(intent_RFID);
					}
					break;
			}
		}

	};

	/**
	 *
	 * @Description:合并两个字节数组、解决异常
	 * @param byte_1
	 * @param byte_2
	 * @return
	 * @return byte[]
	 * @throws
	 * @autour wzq
	 * @date 2015-6-23 下午6:50:50
	 * @update (date)
	 */

	public static byte[] byteMerger(byte[] byte_1, byte[] byte_2) {
		byte[] byte_3 = new byte[byte_1.length + byte_2.length];
		System.arraycopy(byte_1, 0, byte_3, 0, byte_1.length);
		System.arraycopy(byte_2, 0, byte_3, byte_1.length, byte_2.length);
		return byte_3;
	}

	/**
	 * 从一个byte[]数组中截取一部分0
	 *
	 * @param src
	 * @param begin
	 * @param count
	 * @return
	 */
	public static byte[] subBytes(byte[] src, int begin, int count) {
		byte[] bs = new byte[count];
		for (int i = begin; i < begin + count; i++)
			bs[i - begin] = src[i];
		return bs;
	}

}
