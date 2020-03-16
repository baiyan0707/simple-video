package com.simple.util;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Charset {
	
	private Charset(){
	}
	
	/**
	 * map 排序，value为Integer
	 * @param oldMap
	 * @return
	 */
	public static Map<String, Integer> sortMap(Map<String, Integer> oldMap) {
		List<Map.Entry<String, Integer>> list = new ArrayList<Map.Entry<String, Integer>>(oldMap.entrySet());
		Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {

			@Override
			public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
				return o2.getValue() - o1.getValue();
			}
		});
		Map<String, Integer> newMap = new LinkedHashMap<String, Integer>();
		for (Map.Entry<String, Integer> entry : list){
			newMap.put(entry.getKey(), entry.getValue());
		}
		return newMap;
	}

	public static String getMacStr(String signStr){
		return MD5.MD5Encode(signStr).toUpperCase();
	}

	/**
	 * unicode 编码转中文
	 * @param str
	 * @return
	 */
	public static String unicodeToString(String str) {

		if ( str == null) {
			return "";
		}
		Pattern pattern = Pattern.compile("(\\\\u(\\p{XDigit}{4}))");
		Matcher matcher = pattern.matcher(str);
		char ch;
		while (matcher.find()) {
			ch = (char) Integer.parseInt(matcher.group(2), 16);
			str = str.replace(matcher.group(1), Character.toString(ch));
		}
		return str;
	}
}
