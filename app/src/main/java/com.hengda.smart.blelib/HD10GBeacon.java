package com.hengda.smart.blelib;

import java.io.Serializable;

/**
 *
 * @Description 十代机BLE扫描实体类
 * @author wzq
 * @date 2015-10-15 下午1:33:27
 * @update (date)
 * @version V1.0
 */
public class HD10GBeacon implements Serializable{

	/**
	 * @Fields serialVersionUID : TODO
	 * @date 2015-10-15 下午4:49:36
	 * @author wzq
	 */
	private static final long serialVersionUID = 1L;
	private String macAdd;//add
	private int major;//major值
	private int minor;//minor值
	private int rssi; //RSSI值
	private String uuid;//UUID值


	public String getMacAdd() {
		return macAdd;
	}

	public void setMacAdd(String macAdd) {
		this.macAdd = macAdd;
	}

	public int getMajor() {
		return major;
	}
	public void setMajor(int major) {
		this.major = major;
	}

	public int getMinor() {
		return minor;
	}

	public void setMinor(int minor) {
		this.minor = minor;
	}

	public int getRssi() {
		return rssi;
	}

	public void setRssi(int rssi) {
		this.rssi = rssi;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}


	@Override
	public String toString() {
		return  "|beacon数据="
				+"|macAdd=="+macAdd
				+"|major=="+major
				+"|minor=="+minor
				+"|rssi=="+rssi
				+"|uuid=="+uuid
				;
	}

}
