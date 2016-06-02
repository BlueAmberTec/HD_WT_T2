package com.hengda.smart.blelib;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.os.Environment;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.DisplayMetrics;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class CommonUtil {

	/**
	 * byte数组转换成16进制字符数组
	 * @param src
	 * @return
	 */

	public static String[] bytesToHexStrings(byte[] src){

		if (src == null || src.length <= 0) {

			return null;

		}

		String[] str = new String[src.length];

		for (int i = 0; i < src.length; i++) {

			int v = src[i] & 0xFF;

			String hv = Integer.toHexString(v);

			if (hv.length() < 2) {

				str[i] = "0";

			}

			str[i] = hv;

		}

		return str;

	}


	/**

	 * byte数组转换成16进制字符串

	 * @param src

	 * @return

	 */

	public static String bytesToHexString(byte[] src){

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


	/**
	 * wang zhi qiang
	 * @param hexString
	 * @return byte
	 */

	public static byte hexStringToBytes(String hexString) {

		if (hexString == null || hexString.equals("")) {
			return 0;
		}

		if(hexString.length()==1){
			hexString="0"+hexString;
		}
		hexString = hexString.toUpperCase();
		int length = hexString.length() / 2;
		char[] hexChars = hexString.toCharArray();
		byte[] d = new byte[length];
		for (int i = 0; i < length; i++) {
			int pos = i * 2;
			d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
		}
		return d[0];
	}

	public static byte charToByte(char c) {
		return (byte) "0123456789ABCDEF".indexOf(c);
	}


	public static byte[] getDoubleByteArray(int temInt) {
		byte[] tem = new byte[2];
		if (temInt > 0 && temInt < 65535) {
			if (temInt < 256) {
				tem[0] = (byte) 0x00;
				tem[1] = hexStringToBytes(Integer
						.toHexString(temInt));
			} else {
				String[] hexString = CommonUtil.get256HEX(temInt);
				tem[0]=hexStringToBytes(hexString[0]);
				tem[1]=hexStringToBytes(hexString[1]);
			}

		}else{
			tem[0]=(byte) 0x00;
			tem[1]=(byte) 0x00;
		}
		return tem;
	}

	public static byte getSingleByte(int temInt) {
		return hexStringToBytes(Integer.toHexString(temInt));
	}


	/**
	 *   获取
	 */
	public static String[]get256HEX(int no) {
		String[] hext = new String[2];
		if(no>255){
			hext[0]=Integer.toHexString(no/256);
			hext[1]=Integer.toHexString(no%256);
		}
		return hext;
	}


	/**
	 *
	 * @param buffer
	 * @return 获取十六进制
	 */

	public static String getHexStringByByteArray(byte[] buffer) {
		StringBuilder stringBuilder = new StringBuilder("");
		for (int i = 0; i < 22; i++) {
			int v = buffer[i] & 0xFF;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				stringBuilder.append(0);
			}
			stringBuilder.append(hv);
		}
		return stringBuilder.toString().toUpperCase();

	}



	public static boolean checkSDcard() {
		String status = Environment.getExternalStorageState();
		if (status.equals(Environment.MEDIA_MOUNTED)) {
			return true;
		} else {
			return false;
		}
	}



	/**
	 * 检查服务是否在运行
	 */
	public static boolean isMyServiceRunning(Context context, String serviceName) {
		ActivityManager manager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		for (ActivityManager.RunningServiceInfo service : manager
				.getRunningServices(Integer.MAX_VALUE)) {
			if (serviceName.equals(service.service.getClassName())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 递归删除文件和文件夹
	 *
	 * @param file
	 *            要删除的根目录
	 */
	public static void RecursionDeleteFile(File file) {
		if (file.isFile()) {
			file.delete();
			return;
		}
		if (file.isDirectory()) {
			File[] childFile = file.listFiles();
			if (childFile == null || childFile.length == 0) {
				file.delete();
				return;
			}
			for (File f : childFile) {
				RecursionDeleteFile(f);
			}
			file.delete();
		}
	}

	/**
	 *  检测当前服务时候在运行
	 */
	private static boolean isServiceRunning(Context context,String serviceName) {
		ActivityManager manager = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
		for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
			if (serviceName.equals(service.service.getClassName())) {
				return true;
			}
		}
		return false;
	}



	/**
	 * 检查网络是否已经连接
	 */
	public static boolean checkNetworkConnection(Context context) {
		final ConnectivityManager connMgr = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		final android.net.NetworkInfo wifi = connMgr
				.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		final android.net.NetworkInfo mobile = connMgr
				.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

		if (wifi.isAvailable() || mobile.isAvailable())
			return true;
		else
			return false;
	}

	/**
	 * 判断当前运行的程序
	 */
	public static boolean checkCurrentTask(Context context, String pakageName) {
		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
		String packageName = cn.getPackageName();

		if (packageName != null && packageName.equals(pakageName)) {
			return true;
		} else {
			return false;
		}
	}
	/**
	 * 判断activity是否在最顶部显示
	 */
	public static boolean checkCurrentActivity(Context context, String activityName) {
		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
		String packageName = cn.getShortClassName();
		if (packageName != null && packageName.equals(activityName)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 设置本地语言
	 *
	 * @param Language
	 * @return
	 */
	public static boolean setLocalLanguage(String Language, Context context) {
		Configuration config = context.getResources().getConfiguration();
		DisplayMetrics dm = context.getResources().getDisplayMetrics();
		if ("CHINESE".equals(Language)) {
			config.locale = Locale.SIMPLIFIED_CHINESE;
		} else if ("ENGLISH".equals(Language)) {
			config.locale = Locale.ENGLISH;
		} else {
			config.locale = Locale.SIMPLIFIED_CHINESE;
		}
		context.getResources().updateConfiguration(config, dm);
		return true;
	}

	public static boolean checkFileExits(String path) {
		File file = new File(path);
		if (file.exists()) {
			return true;
		} else {
			return false;
		}
	}

	// ====================================================获取设备====================================================================
	/**
	 * 获取当前系统sdk版本
	 */
	public static int getSystemSdkVersion() {
		int versionCode = android.os.Build.VERSION.SDK_INT;
		return versionCode;
	}

	public static String getDeviceModel() {
		String phoneName = android.os.Build.MODEL;
		return phoneName;
	}

	// ============================================================================================================================


	// 获取路劲底下的图片链接
	public static List<String> getImgUriList(String imgPath) {
		List<String> imgPathList = new ArrayList<String>();
		if (CommonUtil.checkFileExits(imgPath)) {
			File filePath = new File(imgPath);
			File[] files = filePath.listFiles();
			for (File file : files) {
				if (file.getName().endsWith(".jpg")) {
					imgPathList.add(file.getAbsolutePath());
					System.out.println("显示图片文件"+ file.getAbsolutePath());
				}
			}
		}
		return imgPathList;
	}


	public static int Dp2Px(Context context, float dp) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dp * scale + 0.5f);
	}

	public static int Px2Dp(Context context, float px) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (px / scale + 0.5f);
	}


	public static boolean isInstall(String packageName,Context context) {

		PackageInfo packageInfo;

		try {
			packageInfo = context.getPackageManager().getPackageInfo(packageName,
					0);
		} catch (PackageManager.NameNotFoundException e) {
			packageInfo = null;
			e.printStackTrace();
		}
		if (packageInfo == null) {
			return false;
		} else {
			return true;
		}
	}

	public static String getAndroidDeviceUnique(Context context){
		return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
	}





	public static final String removeBOM(String data) {

		if (TextUtils.isEmpty(data)) {

			return data;

		}

		if (data.startsWith("\ufeff")) {

			return data.substring(1);

		} else {

			return data;

		}


	}

	/**
	 *
	 * @param str
	 * @return  判断输入内容是否为IP true为真，false为假
	 */
	public static boolean isIP(String str) {
		Pattern pattern = Pattern.compile("\\b((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])" +
				"\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\." +
				"((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\." +
				"((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\b");
		return pattern.matcher(str).matches();
	}

	/*
	 * @param str 源字符串转换成字节数组的字符串
	 *
	 * @return
	 */
	public static byte[] StringToByte(String str) {
		return StringToByte(str, "UTF-8");
	}

	/**
	 * UTF-8 一个汉字占三个字节
	 * @param str
	 * 源字符串 转换成字节数组的字符串
	 * @return
	 */
	public static byte[] StringToByte(String str, String charEncode) {
		byte[] destObj = null;
		try {
			if (null == str || str.trim().equals("")) {
				destObj = new byte[0];
				return destObj;
			} else {
				destObj = str.getBytes(charEncode);
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return destObj;
	}


	/**
	 *
	 * @Description: 判断文件夹时候
	 * @param path
	 * @return void
	 * @throws
	 * @autour wzq
	 * @date 2015-8-25 上午10:28:41
	 * @update (date)
	 */
	public static boolean isExist(String path) {
		File file = new File(path);
		//判断文件夹是否存在,如果不存在则创建文件夹
		if (!file.exists()) {
//		     file.mkdir();
			return false;
		}else{
			return true;
		}
	}

	/**
	 * @param context
	 * @param TargetFilePath
	 * @param TargetFileName
	 * @param file_id
	 *            将raw下的文件转移到TargetFilePath
	 */
	public static void LoadFile(Context context, String TargetFilePath,
								String TargetFileName, int file_id) {

		try {
			File dir = new File(TargetFilePath);
			if (!dir.exists()) {
				dir.mkdirs();
			}

			if (!(new File(TargetFileName)).exists()) {
				InputStream is = context.getResources()
						.openRawResource(file_id);
				FileOutputStream fos = new FileOutputStream(TargetFileName);
				byte[] buffer = new byte[8192];
				int count = 0;

				while ((count = is.read(buffer)) > 0) {
					fos.write(buffer, 0, count);
				}

				fos.close();
				is.close();
			}
		} catch (Exception e) {

		}
	}

	/**
	 * 解压缩文件到指定的目录.
	 *
	 * @param unZipfileName
	 *            需要解压缩的文件
	 * @param mDestPath
	 *            解压缩后存放的路径
	 **/
	public static void unZip(String unZipfileName, String mDestPath) {
		if (!mDestPath.endsWith("/")) {
			mDestPath = mDestPath + "/";
		}
		FileOutputStream fileOut = null;
		ZipInputStream zipIn = null;
		ZipEntry zipEntry = null;
		File file = null;
		int readedBytes = 0;
		byte buf[] = new byte[4096];
		try {
			zipIn = new ZipInputStream(new BufferedInputStream(
					new FileInputStream(unZipfileName)));
			while ((zipEntry = zipIn.getNextEntry()) != null) {
				file = new File(mDestPath + zipEntry.getName());
				if (zipEntry.isDirectory()) {
					file.mkdirs();
				} else {
					// 如果指定文件的目录不存在,则创建之.
					File parent = file.getParentFile();
					if (!parent.exists()) {
						parent.mkdirs();
					}
					fileOut = new FileOutputStream(file);
					while ((readedBytes = zipIn.read(buf)) > 0) {
						fileOut.write(buf, 0, readedBytes);
					}
					fileOut.close();
				}
				zipIn.closeEntry();
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();

		}
	}
	/**
	 * 删除单个文件
	 * @param   filePath    被删除文件的文件名
	 * @return 文件删除成功返回true，否则返回false
	 */
	public static boolean deleteFile(String filePath) {
		File file = new File(filePath);
		if (file.isFile() && file.exists()) {
			return file.delete();
		}
		return false;
	}




}
