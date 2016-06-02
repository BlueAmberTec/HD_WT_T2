package com.hengda.smart.blelib;

public class HDBLEUtil {

	/**
	 * @Description: 根据二进制数据获取HD10GBeacon
	 * @param buffer
	 * @return
	 * @return HD10GBeacon
	 * @throws
	 * @autour wzq
	 * @date 2015-10-15 下午2:09:20
	 * @update (date)
	 */
	public static HD10GBeacon getHD10GBeacon(byte[] buffer) {
		HD10GBeacon hdBeacon = new HD10GBeacon();

		hdBeacon.setMacAdd(HD_UTIL.getHexStringByByteArray(new byte[]{buffer[8],buffer[9],buffer[10], buffer[11],buffer[12],buffer[13]}));
		hdBeacon.setMajor(Integer.valueOf( HD_UTIL.getHexStringByByteArray(new byte[]{buffer[37],buffer[38]}),16));
		hdBeacon.setMinor(Integer.valueOf( HD_UTIL.getHexStringByByteArray(new byte[]{buffer[39],buffer[40]}),16));
		byte	rssi[]	= 	{buffer[buffer[0]]};
		String srssi		=	Byte.toString(rssi[0]);
		int irssi = Integer.parseInt(srssi);
		hdBeacon.setRssi(irssi);
//		  hdBeacon.setRssi(Integer.valueOf( HD_UTIL.getHexStringByByteArray(new byte[]{rssi[0]}),16));
		hdBeacon.setUuid(HD_UTIL.getHexStringByByteArray(new byte[]{buffer[21],buffer[22],buffer[23],
				buffer[24],buffer[25],buffer[26],
				buffer[27],buffer[28],buffer[29],
				buffer[30],buffer[31],buffer[32],
				buffer[33],buffer[34],buffer[35],
				buffer[36]}));
		return hdBeacon;
	}
}