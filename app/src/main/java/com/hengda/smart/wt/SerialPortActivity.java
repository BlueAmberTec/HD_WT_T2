package com.hengda.smart.wt;

/*
 * Copyright 2009 Cedric Priscal
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. 
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;

import com.hengda.smart.application.Application;
import com.hengda.smart.blelib.CommonUtil;

import org.kymjs.kjframe.utils.KJLoger;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidParameterException;

import android_serialport_api.SerialPort;

public abstract class SerialPortActivity extends Activity {
    protected Application mApplication;
    protected SerialPort mSerialPort;
    protected OutputStream mOutputStream;
    private InputStream mInputStream;
    private ReadThread mReadThread;

    // 回传参数字符品串
    private byte[] tem1 = new byte[4];
    private byte[] tem2 = new byte[4];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mApplication = (Application) getApplication();
        try {
            mSerialPort = mApplication.getSerialPort();
            mOutputStream = mSerialPort.getOutputStream();
            mInputStream = mSerialPort.getInputStream();
            /* Create a receiving thread */
            mReadThread = new ReadThread();
            mReadThread.start();
            KJLoger.debug("RFID串口线程开始");

        } catch (SecurityException e) {
//            DisplayError(R.string.error_security);
        } catch (IOException e) {
//            DisplayError(R.string.error_unknown);
        } catch (InvalidParameterException e) {
//            DisplayError(R.string.error_configuration);
        }
    }

    private class ReadThread extends Thread {
        @Override
        public void run() {
            super.run();
            while (true) {

                int size;
                byte[] buffer = new byte[16];
                try {
                    if (mInputStream == null) {
                        return;
                    }
                    size = 0;
                    size = mInputStream.read(buffer);
                    KJLoger.debug(CommonUtil.bytesToHexString(buffer)+"==="+size);
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
                        sleep(200);
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

    private void DisplayError(int resourceId) {
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setTitle("Error");
        b.setMessage(resourceId);
        b.setPositiveButton("OK", new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // SerialPortActivity.this.finish();
            }
        });
        b.show();
    }

    protected abstract void onDataReceived(final byte[] buffer, final int size);

    @Override
    public void onDestroy() {
        if (mReadThread != null) {
            mReadThread.interrupt();
        }
        mApplication.closeSerialPort();
        mSerialPort = null;
        super.onDestroy();
    }


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
