package com.simple.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

@Slf4j
public class MD5 {
	private MD5() {
	}

	// 兼容cps加密方式
	private static final char[] HEX_DIGITS = { '0', '1', '2', '3', '4', '5',
			'6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

	private final static String[] hexDigits = { "0", "1", "2", "3", "4", "5",
			"6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };

	public static String byteArrayToHexString(byte[] b) {
		StringBuilder resultSb = new StringBuilder();
		for (int i = 0; i < b.length; i++) {
			resultSb.append(byteToHexString(b[i]));
		}
		return resultSb.toString();
	}

	private static String byteToHexString(byte b) {
		int n = b;
		if (n < 0)
			n = 256 + n;
		int d1 = n / 16;
		int d2 = n % 16;
		return hexDigits[d1] + hexDigits[d2];
	}

	// 兼容cps加密方式
	public static String getMD5(byte[] bytesSrc) {
		String result = "";
		// 用来将字节转换成16进制表示的字符
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(bytesSrc);
			// MD5的计算结果是一个128 位的长整数，字节表示是16个字节
			byte tmp[] = md.digest();
			// 每个字节用16进制表示，使用两个字符，表示成16进制需要32个字符
			char str[] = new char[16 * 2];
			// 表示转换结果中对应的字符位置
			int k = 0;
			// 从第一个字节开始，对 MD5 的每一个字节
			for (int i = 0; i < 16; i++) {
				// 转换成 16 进制字符的转换
				byte byte0 = tmp[i]; // 取第i个字节
				// 取字节中高 4 位的数字转换，>>> 为逻辑右移，将符号位一起右移
				str[k++] = HEX_DIGITS[byte0 >>> 4 & 0xf];
				// 取字节中低 4 位的数字转换
				str[k++] = HEX_DIGITS[byte0 & 0xf];
			}
			// 换后的结果转换为字符串
			result = new String(str);
		} catch (Exception e) {
			log.info("错误信息：{}", e.getMessage());
		}
		return result;
	}

	public static String MD5Encode(String origin) {
		String resultString = null;
		try {
			resultString = origin;
			MessageDigest md = MessageDigest.getInstance("MD5");
			resultString = byteArrayToHexString(md.digest(resultString
					.getBytes("UTF-8")));
		} catch (Exception e) {
			log.info("错误信息：{}", e.getMessage());
		}
		return resultString;
	}

	/**
	 *
	 * 方法的描述: 验证MD5是否一致
	 * 
	 * @author: jmx
	 * @version: Feb 24, 2012 2:20:54 PM
	 * @param splitInput
	 * @param input
	 *            内部加密字符串
	 * @return
	 * @return boolean
	 */
	public static boolean checkMD5(String splitInput, String input) {

		if (StringUtils.isEmpty(input)) {
			return true;
		}
		if (splitInput.equals(input)) {
			return true;
		}
		return false;
	}

	public static String getSignKey(int length) {
		String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		Random random = new Random();
		StringBuilder buf = new StringBuilder();
		for (int i = 0; i < length; i++) {
			int num = random.nextInt(62);
			buf.append(str.charAt(num));
		}
		return buf.toString();
	}

	public static String getMD5Pass(String pass, String salt) {
		if (DataUtil.isEmpty(pass)) {
			return "";
		}
		return getPass("MD5", pass, salt);
	}

	public static String getSHAPass(String pass, String salt) {
		return getPass("SHA", pass, salt);
	}

	private static String getPass(String digestType, String pass, String salt) {
		String result = null;
		try {
			MessageDigest digest = MessageDigest.getInstance(digestType);
			// digest在底层也调用了update方法
			result = byteArrayToString(digest.digest(addSalt(pass, salt).getBytes()));
		} catch (NoSuchAlgorithmException e) {
			log.error("getPass exception:" + e.getMessage());
			return "";
		}
		return result.toUpperCase();
	}

	/**
	 * 在加密对象后加盐
	 * 
	 * @param object
	 * @return
	 */
	private static String addSalt(String pass, String salt) {
		if (pass == null) {
			pass = "";
		}
		if (salt == null || "".equals(salt)) {
			return pass;
		} else {
			return pass + "{" + salt + "}";
		}
	}

	private static String byteArrayToString(byte[] ss) {
		StringBuilder result = new StringBuilder();
		for (int i = 0; i < ss.length; i++) {
			result.append(byteToString(ss[i]));
		}
		return result.toString();
	}

	private static String byteToString(byte ss) {
		int temp;
		temp = ss < 0 ? ss + 256 : ss;
		return hexDigits[temp / 16] + hexDigits[temp % 16]; // 自己实现转化
	}
}
