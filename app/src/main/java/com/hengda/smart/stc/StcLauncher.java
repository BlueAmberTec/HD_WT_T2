package com.hengda.smart.stc;

import android.content.Context;

import com.hengda.smart.application.Application;
import com.hengda.smart.blelib.CommonUtil;
import com.orhanobut.logger.Logger;

import java.io.IOException;
import java.io.OutputStream;
import java.security.InvalidParameterException;

//		AA 55 05 11 频道 音量   ID号 提醒 模式  OR         打开人工讲解
//		AA 55 05 08 频道 音量   ID号 提醒 模式  OR         频道设置（1-64）
//		AA 55 05 04 频道 音量   ID号 提醒 模式  OR         关闭通信
//		AA 55 05 05 频道 音量   ID号 提醒 模式  OR         进入ID匹配
//		AA 55 05 06 频道 音量   ID号 提醒 模式  OR         退出ID匹配
//		AA 55 05 09 频道 音量   ID号 提醒 模式  OR         调节音量
//		AA 55 05 07 频道 音量   ID号 提醒 模式  OR         ID号设置(0-255)
//		AA 55 05 0C 频道 音量   ID号 提醒 模式  OR         高音质模式设置(01)
//		AA 55 05 0D 频道 音量   ID号 提醒 模式  OR         抗干扰模式设置(02)
//		AA 55 05 0F 频道 音量   ID号 提醒 模式  OR         开闭掉队提醒设置（开：0x80 120关：0）

/**
 * @Description 应用向STC发送数据的应用
 * @author wzq
 * @date 2015-6-11 下午3:24:25
 * @update (date)
 * @version V1.0
 */
public class StcLauncher {
	private OutputStream stcOutputStream;
	private SharePreStcUtil spUitl;

	/**
	 * <p>Title: </p>
	 * <p>Description: 构造器</p>
	 * @author wzq
	 * @date 2015-6-11 下午3:26:54
	 * @update (date)
	 */
	public StcLauncher(Application application, Context context) {
		try {
			this.stcOutputStream = application.getSerialPort()
					.getOutputStream();
			spUitl=new SharePreStcUtil(context, StcConfig.SHARENAME);
		} catch (InvalidParameterException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}


	/**
	 *  打开人工讲解
	 */
	public void openRenGongAudio() {
		byte[] command = new byte[10];
		command[0]=(byte) 0xAA;
		command[1]=(byte) 0x55;
		command[2]=(byte) 0x05 ;
		command[3]=(byte) 0x11 ;
		command[4]= CommonUtil.getSingleByte(spUitl.getChannel());
		command[5]= CommonUtil.getSingleByte(spUitl.getVolume());
		command[6]=CommonUtil.getSingleByte(spUitl.getID());
		command[7]=CommonUtil.getSingleByte(spUitl.getTiXing());
		command[8]= CommonUtil.getSingleByte(spUitl.getModle());
		command[9]=(byte) (command[0]^command[1]^command[2]^command[3] ^ command[4]^ command[5]^ command[6]^ command[7]^ command[8]);
		try {
			stcOutputStream.write(command);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Logger.d("openRenGongAudio[打开人工讲解]"+CommonUtil.bytesToHexString(command));
	}

	/**
	 *  关闭通信
	 */
	public void closeTongXin() {
		byte[] command = new byte[10];
		command[0]=(byte) 0xAA;
		command[1]=(byte) 0x55;
		command[2]=(byte) 0x05 ;
		command[3]=(byte) 0x04 ;
		command[4]= CommonUtil.getSingleByte(spUitl.getChannel());
		command[5]= CommonUtil.getSingleByte(spUitl.getVolume());
		command[6]=CommonUtil.getSingleByte(spUitl.getID());
		command[7]=CommonUtil.getSingleByte(spUitl.getTiXing());
		command[8]= CommonUtil.getSingleByte(spUitl.getModle());
		command[9]=(byte) (command[0]^command[1]^command[2]^command[3] ^ command[4]^ command[5]^ command[6]^ command[7]^ command[8]);
		try {
			stcOutputStream.write(command);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Logger.d("closeTongXin[关闭通信]"+CommonUtil.bytesToHexString(command));
	}


	/**
	 *  进入ID匹配
	 */
	public void openIDPiPei() {
		byte[] command = new byte[10];
		command[0]=(byte) 0xAA;
		command[1]=(byte) 0x55;
		command[2]=(byte) 0x05 ;
		command[3]=(byte) 0x05 ;
		command[4]= CommonUtil.getSingleByte(spUitl.getChannel());
		command[5]= CommonUtil.getSingleByte(spUitl.getVolume());
		command[6]=CommonUtil.getSingleByte(spUitl.getID());
		command[7]=CommonUtil.getSingleByte(spUitl.getTiXing());
		command[8]= CommonUtil.getSingleByte(spUitl.getModle());
		command[9]=(byte) (command[0]^command[1]^command[2]^command[3] ^ command[4]^ command[5]^ command[6]^ command[7]^ command[8]);
		try {
			stcOutputStream.write(command);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Logger.d("openIDPiPei[进入ID匹配]"+CommonUtil.bytesToHexString(command));
	}

	/**
	 *  进入ID匹配
	 */
	public void exitIDPiPei() {
		byte[] command = new byte[10];
		command[0]=(byte) 0xAA;
		command[1]=(byte) 0x55;
		command[2]=(byte) 0x05 ;
		command[3]=(byte) 0x06 ;
		command[4]= CommonUtil.getSingleByte(spUitl.getChannel());
		command[5]= CommonUtil.getSingleByte(spUitl.getVolume());
		command[6]= CommonUtil.getSingleByte(spUitl.getID());
		command[7]= CommonUtil.getSingleByte(spUitl.getTiXing());
		command[8]= CommonUtil.getSingleByte(spUitl.getModle());
		command[9]=(byte) (command[0]^command[1]^command[2]^command[3] ^ command[4]^ command[5]^ command[6]^ command[7]^ command[8]);
		try {
			stcOutputStream.write(command);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Logger.d("exitIDPiPei[退出ID匹配]"+CommonUtil.bytesToHexString(command));
	}


	/**
	 *  高音质设置
	 */
	public void setModleGaoYinZ() {
		byte[] command = new byte[10];
		command[0]=(byte) 0xAA;
		command[1]=(byte) 0x55;
		command[2]=(byte) 0x05 ;
		command[3]=(byte) 0x0C ;
		command[4]= CommonUtil.getSingleByte(spUitl.getChannel());
		command[5]= CommonUtil.getSingleByte(spUitl.getVolume());
		command[6]=CommonUtil.getSingleByte(spUitl.getID());
		command[7]=CommonUtil.getSingleByte(spUitl.getTiXing());
		command[8]=(byte) 0x01;
		command[9]=(byte) (command[0]^command[1]^command[2]^command[3] ^ command[4]^ command[5]^ command[6]^ command[7]^ command[8]);
		try {
			stcOutputStream.write(command);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Logger.d("setModleGaoYinZ[高音质设置]"+CommonUtil.bytesToHexString(command));
	}



	/**
	 *  抗干扰模式设置
	 */
	public void setModleKangGanR() {
		byte[] command = new byte[10];
		command[0]=(byte) 0xAA;
		command[1]=(byte) 0x55;
		command[2]=(byte) 0x05 ;
		command[3]=(byte) 0x0D ;
		command[4]= CommonUtil.getSingleByte(spUitl.getChannel());
		command[5]= CommonUtil.getSingleByte(spUitl.getVolume());
		command[6]=CommonUtil.getSingleByte(spUitl.getID());
		command[7]=CommonUtil.getSingleByte(spUitl.getTiXing());
		command[8]=(byte) 0x02;
		command[9]=(byte) (command[0]^command[1]^command[2]^command[3] ^ command[4]^ command[5]^ command[6]^ command[7]^ command[8]);
		try {
			stcOutputStream.write(command);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Logger.d("setModleKangGanR[抗干扰模式设置]"+CommonUtil.bytesToHexString(command));
	}
	/**
	 *  模式设置
	 */
	public void setModle() {
		byte[] command = new byte[10];
		command[0]=(byte) 0xAA;
		command[1]=(byte) 0x55;
		command[2]=(byte) 0x05 ;
		command[3]=(byte) 0x0D ;
		command[4]= CommonUtil.getSingleByte(spUitl.getChannel());
		command[5]= CommonUtil.getSingleByte(spUitl.getVolume());
		command[6]=CommonUtil.getSingleByte(spUitl.getID());
		command[7]=CommonUtil.getSingleByte(spUitl.getTiXing());
		command[8]= CommonUtil.getSingleByte(spUitl.getModle());
		command[9]=(byte) (command[0]^command[1]^command[2]^command[3] ^ command[4]^ command[5]^ command[6]^ command[7]^ command[8]);
		try {
			stcOutputStream.write(command);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Logger.d("setModle 干扰模式设置"+CommonUtil.bytesToHexString(command));
	}

	/**
	 * 频道设置
	 */
	public void setChannel() {
		byte[] command = new byte[10];
		command[0]=(byte) 0xAA;
		command[1]=(byte) 0x55;
		command[2]=(byte) 0x05 ;
		command[3]=(byte) 0x08 ;
		command[4]= CommonUtil.getSingleByte(spUitl.getChannel());
		command[5]= CommonUtil.getSingleByte(spUitl.getVolume());
		command[6]=CommonUtil.getSingleByte(spUitl.getID());
		command[7]=CommonUtil.getSingleByte(spUitl.getTiXing());
		command[8]= CommonUtil.getSingleByte(spUitl.getModle());
		command[9]=(byte) (command[0]^command[1]^command[2]^command[3] ^ command[4]^ command[5]^ command[6]^ command[7]^ command[8]);
		try {
			stcOutputStream.write(command);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Logger.d("setChannel[频道设置]"+CommonUtil.bytesToHexString(command));
	}

	/**
	 * 音量设置
	 */
	public void setVolume() {
		byte[] command = new byte[10];
		command[0]=(byte) 0xAA;
		command[1]=(byte) 0x55;
		command[2]=(byte) 0x05 ;
		command[3]=(byte) 0x09 ;
		command[4]= CommonUtil.getSingleByte(spUitl.getChannel());
		command[5]= CommonUtil.getSingleByte(spUitl.getVolume());
		command[6]=CommonUtil.getSingleByte(spUitl.getID());
		command[7]=CommonUtil.getSingleByte(spUitl.getTiXing());
		command[8]= CommonUtil.getSingleByte(spUitl.getModle());
		command[9]=(byte) (command[0]^command[1]^command[2]^command[3] ^ command[4]^ command[5]^ command[6]^ command[7]^ command[8]);
		try {
			stcOutputStream.write(command);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Logger.d("setVolume[音量设置]"+CommonUtil.bytesToHexString(command));
	}


	/**
	 * ID设置
	 */
	public void setID() {
		byte[] command = new byte[10];
		command[0]=(byte) 0xAA;
		command[1]=(byte) 0x55;
		command[2]=(byte) 0x05 ;
		command[3]=(byte) 0x07 ;
		command[4]= CommonUtil.getSingleByte(spUitl.getChannel());
		command[5]= CommonUtil.getSingleByte(spUitl.getVolume());
		command[6]=CommonUtil.getSingleByte(spUitl.getID());
		command[7]=CommonUtil.getSingleByte(spUitl.getTiXing());
		command[8]= CommonUtil.getSingleByte(spUitl.getModle());
		command[9]=(byte) (command[0]^command[1]^command[2]^command[3] ^ command[4]^ command[5]^ command[6]^ command[7]^ command[8]);
		try {
			stcOutputStream.write(command);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Logger.d("setID[ID设置]"+CommonUtil.bytesToHexString(command));
	}

	/**
	 * 掉队提醒设置
	 */
	public void setTiXing() {
		byte[] command = new byte[10];
		command[0]=(byte) 0xAA;
		command[1]=(byte) 0x55;
		command[2]=(byte) 0x05 ;
		command[3]=(byte) 0x0F ;
		command[4]= CommonUtil.getSingleByte(spUitl.getChannel());
		command[5]= CommonUtil.getSingleByte(spUitl.getVolume());
		command[6]=CommonUtil.getSingleByte(spUitl.getID());
		command[7]=CommonUtil.getSingleByte(spUitl.getTiXing());
		command[8]= CommonUtil.getSingleByte(spUitl.getModle());
		command[9]=(byte) (command[0]^command[1]^command[2]^command[3] ^ command[4]^ command[5]^ command[6]^ command[7]^ command[8]);
		try {
			stcOutputStream.write(command);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Logger.d("setTiXing[掉队提醒设置]"+CommonUtil.bytesToHexString(command));
	}


}
