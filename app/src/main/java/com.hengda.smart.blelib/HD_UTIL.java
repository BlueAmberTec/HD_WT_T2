package com.hengda.smart.blelib;
import android.text.Html;
import android.widget.EditText;

import java.security.SecureRandom;
import java.util.regex.Pattern;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

public class HD_UTIL {

	private final static String DES = "DES";
//    private final static String DES = "DES/ECB/NoPadding";

	/**
	 * Description 根据键值进行加密
	 * @param data
	 * @param key  加密键byte数组
	 * @return
	 * @throws Exception
	 */
	private static byte[] encrypt(byte[] data, byte[] key) throws Exception {
		// 生成一个可信任的随机数源
		SecureRandom sr = new SecureRandom();

		// 从原始密钥数据创建DESKeySpec对象
		DESKeySpec dks = new DESKeySpec(key);

		// 创建一个密钥工厂，然后用它把DESKeySpec转换成SecretKey对象
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
		SecretKey securekey = keyFactory.generateSecret(dks);

		// Cipher对象实际完成加密操作
		Cipher cipher = Cipher.getInstance(DES);

		// 用密钥初始化Cipher对象
		cipher.init(Cipher.ENCRYPT_MODE, securekey, sr);

		return cipher.doFinal(data);
	}


	/**
	 * Description 根据键值进行解密
	 * @param data
	 * @param key  加密键byte数组
	 * @return
	 * @throws Exception
	 */
	private static byte[] decrypt(byte[] data, byte[] key) throws Exception {
		// 生成一个可信任的随机数源
		SecureRandom sr = new SecureRandom();

		// 从原始密钥数据创建DESKeySpec对象
		DESKeySpec dks = new DESKeySpec(key);

		// 创建一个密钥工厂，然后用它把DESKeySpec转换成SecretKey对象
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
		SecretKey securekey = keyFactory.generateSecret(dks);

		// Cipher对象实际完成解密操作
		Cipher cipher = Cipher.getInstance(DES);

		// 用密钥初始化Cipher对象
		cipher.init(Cipher.DECRYPT_MODE, securekey, sr);

		return cipher.doFinal(data);
	}

	/**
	 *  格式化字符串
	 */

	public static String getString(String formatString){
		String lowerString =formatString.toLowerCase();
		String newstr = "";
		int size = ((lowerString.length())%2 == 0) ? ((lowerString.length())/2):((lowerString.length())/2 + 1);
		for(int i=0;i<size ;i++){
			int endIndex = (i+1)*2;
			if((i+1)==size){
				endIndex = lowerString.length();
			}
			if(i==0){
				newstr += lowerString.substring(i,endIndex);
			}else{
				newstr += ":"+lowerString.substring(i*2, endIndex);
			}
		}
		return newstr;
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

	/**
	 *  检测三个EditView格式是否为IP格式？
	 */

	public static boolean  allIsIp(EditText one,EditText two,EditText three){
		if(isIP(one.getText().toString())){
			if(isIP(two.getText().toString())){
				if(isIP(three.getText().toString())){
					return true;
				}else{
//        			three.setError("输入内容格式不正确");
					three.setError(Html.fromHtml("<font color=#ff0000>输入内容格式不正确</font>"));
					return false;
				}
			}else{
//    			two.setError("输入内容格式不正确");
				two.setError(Html.fromHtml("<font color=#ff0000>输入内容格式不正确</font>"));
				return false;
			}
		}else{
//    		one.setError("输入IP 格式不正确");
			one.setError(Html.fromHtml("<font color=#ff0000>输入内容格式不正确</font>"));
			return false;
		}
	}

	/**
	 *
	 * @param buffer
	 * @return 获取十六进制
	 */

	public static  String getHexStringByByteArray(byte[] buffer) {
		StringBuilder stringBuilder = new StringBuilder("");
		for (int i = 0; i < buffer.length; i++) {
			int v = buffer[i] & 0xFF;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				stringBuilder.append(0);
			}
			stringBuilder.append(hv);
		}
		return stringBuilder.toString().toUpperCase();
	}






}