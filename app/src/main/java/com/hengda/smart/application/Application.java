package com.hengda.smart.application;

import org.kymjs.kjframe.utils.KJLoger;

import java.io.File;
import java.io.IOException;
import java.security.InvalidParameterException;

import android_serialport_api.SerialPort;

/**
 *  Application
 */
public class Application extends android.app.Application {
	private static SerialPort mSerialPort = null;
	
    public static int screenW;
    public static int screenH;

	public SerialPort getSerialPort() throws SecurityException, IOException,
			InvalidParameterException {
		if (mSerialPort == null) {
			String path = "/dev/s3c2410_serial3";
			int baudrate = 57600;
			/* Check parameters */
			if ((path.length() == 0) || (baudrate == -1)) {
				throw new InvalidParameterException();
			}
			/* Open the serial port */
			mSerialPort = new SerialPort(new File(path), baudrate, 0);
			KJLoger.debug("打开串口");
		}
		return mSerialPort;
	}
    

	public void closeSerialPort() {
		if (mSerialPort != null) {
			mSerialPort.close();
			mSerialPort = null;
		}
	}


	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
	    KJLoger.IS_DEBUG=true;
	}
	
}
