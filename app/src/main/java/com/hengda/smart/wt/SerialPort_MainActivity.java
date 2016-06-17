package com.hengda.smart.wt;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hengda.smart.blelib.CommonUtil;
import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;

import org.kymjs.kjframe.utils.KJLoger;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SerialPort_MainActivity extends SerialPortActivity {

    @BindView(R.id.tv_rfid)
    TextView tvRfid;
    @BindView(R.id.container)
    LinearLayout container;

    //data
    private int rfid_num=0;
    private Intent  consoleAction;

    public static final int SHOW_RFID=0x01;

    public  static final String TAG ="AndroidLOG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rfid);
        ButterKnife.bind(this);
        Logger
                .init("BlueAmer")                  // 默认为PRETTYLOGGER，可以设置成为自定义tag
                .setMethodCount(2)                 // logger所在方法显示开关 0 为不显示，1、2 为不同的方法信息显示样式
                .hideThreadInfo()                  // 线程信息显示，默认打开
                .setLogLevel(LogLevel.FULL)        // 默认是打开日志显示（FULL），关闭（NONE）
                .setMethodOffset(2);                // 默认为0 ,方法体样式
        Logger.v("这是 全局 Logger.init(\"BlueAmer\")输出显示");
        Logger.wtf("这是wtf");
        Logger.t("BlueAmer_Child").v("这是 全局 Logger.t(\"BlueAmer_Child\").v()输出显示");
        Logger.t("BlueAmer_JSON").json("{\"code\":\"success\",\"info\":\"\\u6ce8\\u518c\\u6210\\u529f\",\"referer\":\"\",\"state\":\"fail\"}");

        consoleAction = new Intent();

    }

    private Handler rfidShowHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case SHOW_RFID:
                    tvRfid.setText(rfid_num+"");
                    Log.v(TAG,"这是Android Log 输出显示");
                    Log.v(TAG,"{\"code\":\"success\",\"info\":\"\\u6ce8\\u518c\\u6210\\u529f\",\"referer\":\"\",\"state\":\"fail\"}");
                break;
            }
        }
    };

    @Override
    protected void onDataReceived(byte[] buffer, int size) {

        KJLoger.debug(CommonUtil.bytesToHexString(buffer) + "===Main==" + size);
        sendBroadcast(consoleAction);
        for (int m = 0; m < 8; m++) {
            if (buffer[m] == (byte) 0xAA && buffer[m + 1] == (byte) 0x55) {
                // rfid
                if ((buffer[m + 2] == (byte) 0x05)
                        && buffer[m + 6] == (buffer[m + 4] ^ buffer[m + 5])) {
                    rfid_num = (buffer[m + 4] & 0x0FF) * 256
                            + (buffer[m + 5] & 0x0FF);

                    /************ 防盗报警 ********************/
                    if (rfid_num == 2003) {
                    }
                    if (rfid_num == 2001 || rfid_num == 2002) {
                    }
                    /*************************************/
                    rfidShowHandler.sendEmptyMessage(SHOW_RFID);
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
                    KJLoger.debug(CommonUtil.bytesToHexString(buffer));
                    if ((buffer[m + 4] == (byte) 0x01)
                            && buffer[m + 7] == (buffer[m + 4] ^ buffer[m + 5] ^ buffer[m + 6])) { // 红外自动适配
                        rfid_num = (buffer[m + 5] & 0xFF) * 256
                                + (buffer[m + 6] & 0xFF); // 群组号
                        KJLoger.debug("===红外号码==="+rfid_num);
                        rfidShowHandler.sendEmptyMessage(SHOW_RFID);

                    } else if ((buffer[m + 4] == (byte) 0x05) // 20151028
                            // 增加占用通道编号回传
                            && buffer[m + 7] == (buffer[m + 4] ^ buffer[m + 5] ^ buffer[m + 6])) {
                        int hostNo = (buffer[m + 5] & 0xFF) * 256
                                + (buffer[m + 6] & 0xFF); // 群组号
                        KJLoger.debug("当前+=" + hostNo );

                    } else if ((buffer[m + 4] == (byte) 0x02)
                            && buffer[m + 7] == (buffer[m + 4] ^ buffer[m + 5] ^ buffer[m + 6])) { // 首次冲突

                    } else if ((buffer[m + 4] == (byte) 0x03)
                            && buffer[m + 7] == (buffer[m + 4] ^ buffer[m + 5] ^ buffer[m + 6])) { // 中途冲突

                    }
                }
            }
        }


    }
}
