package com.simple.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 类描述：对密码进行加密包装处理<br/>
 * 创建人：gaoqiang<br/>
 * 创建时间：2015-4-17下午6:30:20
 */
public class EncryptUtil {

	/**
	 * 方法描述：md5加密
	 * @param inputText
	 * @return
	 * String
	 */
	public static String md5(String inputText){
		return encrypt(inputText, "md5");
	}
	public static String sha1(String inputText){
		return encrypt(inputText, "SHA-1");
	}

	/**
	 * 方法描述：<br/>
	 * @param inputText
	 * @param algorithmName
	 * @return
	 * String
	 */
	private static String encrypt(String inputText, String algorithmName){
		if (inputText == null || "".equals(inputText.trim())) {
			throw new IllegalArgumentException("请输入要加密的内容");
		}
		if (algorithmName == null || "".equals(algorithmName.trim())) {
			algorithmName = "md5";
		}
		String encryptText = null;
		try {
			MessageDigest m = MessageDigest.getInstance(algorithmName);
			m.update(inputText.getBytes("UTF8"));
			return hex(m.digest());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return encryptText;
	}
	
//	private static String md52(String message) throws UnsupportedEncodingException {
//		return DigestUtils.md5Hex(message.getBytes("UTF-8"));
//	}
	/**
	 * 
	 * 方法描述：返回十六进制字符串
	 * @param arr
	 * @return
	 * String
	 */ 
	private static String hex(byte[] arr) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < arr.length; ++i) {
			sb.append(Integer.toHexString((arr[i] & 0xFF) | 0x100).substring(1, 3));
		}
		return sb.toString();
	}


	public static String convertMD5(String inStr) {
		char[] a = inStr.toCharArray();
		for (int i = 0; i < a.length; i++) {
			a[i] = (char) (a[i] ^ 't');
		}
		String s = new String(a);
		return s;
	}

}
