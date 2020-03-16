package com.simple.util;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataUtil {
	 private DataUtil() {}
	/** 金额为分的格式 */
	public static final String CURRENCY_FEN_REGEX = "\\-?[0-9]+";
	/**
	 * 校验字符是否为空
	 * null, "" , " " 返回true
	 * @param val
	 * @return
	 */
	public static boolean isEmpty(String val) {
		if(val==null||("").equals(val.trim())) {
			return true;
		}
		return false;
	}
	
	public static String substring(String str,int maxLength) {
		if(!isEmpty(str)&&str.length()>maxLength) {
			return str.substring(0, maxLength);
		}
		return str;
	}
	
	/**
	 * 比较两个对象的toString 是否相等,不区分大小写
	 * @param str1
	 * @param str2
	 * @return
	 */
	public static boolean equalsIgnoreCaseToString(Object str1,Object str2) {
		if(str1==null) {
			str1 = "";
		}
		if(str2==null) {
			str2 = "";
		}
		return str1.toString().equalsIgnoreCase(str2.toString());
	}
	
	public static int intVal(String val) {
		try {
			if(val!=null&&!("").equals(val.trim())) {
				return Integer.parseInt(val);
			}
		}catch(Exception e) {
			return 0;
		}
		return 0;
	}
	
	public static long longVal(String val) {
		try {
			if(val!=null&&!("").equals(val.trim())) {
				return Long.parseLong(val);
			}
		}catch(Exception e) {
			return 0;
		}
		return 0;
	}
	
	public static int intVal(Object val) {
		try {
			if(val!=null&&!("").equals(val.toString().trim())) {
				return Integer.parseInt(val.toString());
			}
		}catch(Exception e) {
			return 0;
		}
		return 0;
	}
	
	public static String stringVal(Object val) {
		try {
			if(val!=null) {
				return val.toString();
			}
		}catch(Exception e) {
			return "";
		}
		return "";
	}
	
	public static Double getDouble(String value) {
		Double valueOf = null;
		try {
			valueOf = Double.valueOf(value);
		}catch(Exception e) {
			return null;
		}
		return valueOf;
	}
	
	public static long changeY2F(String amount){    
        return BigDecimal.valueOf(Double.valueOf(amount)).multiply(new BigDecimal(100)).longValue();    
    }
	/**
	 * 将分为单位的转换为元 （除100）
	 * 
	 * @param amount
	 * @return
	 * @throws Exception
	 */
	public static String changeF2Y(String amount) throws Exception {
		if (!amount.matches(CURRENCY_FEN_REGEX)) {
			throw new Exception("金额格式有误");
		}
		return BigDecimal.valueOf(Long.parseLong(amount))
				.divide(new BigDecimal(100)).toString();
	}

	/**
	 * 将分为单位的转换为元并返回金额格式的字符串 （除100）
	 * 
	 * @param amount
	 * @return
	 * @throws Exception
	 */
	public static String changeF2Y(Long amount) throws Exception {
		if (!amount.toString().matches(CURRENCY_FEN_REGEX)) {
			throw new Exception("金额格式有误");
		}

		int flag = 0;
		String amString = amount.toString();
		if ('-' == amString.charAt(0)) {
			flag = 1;
			amString = amString.substring(1);
		}
		StringBuilder result = new StringBuilder();
		if (amString.length() == 1) {
			result.append("0.0").append(amString);
		} else if (amString.length() == 2) {
			result.append("0.").append(amString);
		} else {
			String intString = amString.substring(0, amString.length() - 2);
			for (int i = 1; i <= intString.length(); i++) {
				result.append(intString.substring(intString.length() - i,intString.length() - i + 1));
			}
			result.reverse().append(".").append(amString.substring(amString.length() - 2));
		}
		if (flag == 1) {
			return "-" + result.toString();
		} else {
			return result.toString();
		}
	}
	/** 
     * 手机号验证 
     *  
     * @param  str 
     * @return 验证通过返回true 
     */  
    public static boolean isMobile(String str) {   
        Pattern p ;  
        Matcher m ;  
        boolean b ;   
        p = Pattern.compile("^1[1-9][0-9]\\d{4,8}$"); // 验证手机号  
        m = p.matcher(str);  
        b = m.matches();   
        return b;  
    }  
    /** 
     * 电话号码验证 
     *  
     * @param  str 
     * @return 验证通过返回true 
     */  
    public static boolean isPhone(String str) {   
        Pattern p1,p2 ;  
        Matcher m ;  
        boolean b ;    
        p1 = Pattern.compile("^[0][1-9]{2,3}-[0-9]{5,10}$");  // 验证带区号的  
        p2 = Pattern.compile("^[1-9]{1}[0-9]{5,8}$");         // 验证没有区号的  
        if(str.length() >9)  
        {   m = p1.matcher(str);  
            b = m.matches();    
        }else{  
            m = p2.matcher(str);  
            b = m.matches();   
        }    
        return b;  
    }  
	
	/**
	 * 数据库的字段名，转java中的字段名
	 * 首字母大写，_去下划线，字母大写
	 * @param name 
	 * @param type 1 首字母大写  其他小写
	 * @return
	 */
	public static String getJavaName(String name,String type) {
		name = name.toLowerCase();
		//首字母大写
		String head = name.substring(0,1);
		if("1".equals(type)) {
			name = head.toUpperCase() + name.substring(1,name.length());
		}
		name = getJavaFiled(name);
		return name;
	}
	
	private static String getJavaFiled(String name) {
		//去下划线字母大写
		int index = name.indexOf('_');
		if(index!=-1) {
			name = name.substring(0,index) + name.substring(index+1,index+2).toUpperCase() + name.substring(index+2, name.length());
		}
		if(name.indexOf('_')!=-1) {
			return getJavaFiled(name);
		}
		return name;
	}
	
	/**
	 * 删除字符串最后的标记
	 * @param str
	 * @param tag
	 * @return
	 */
	public static String removeLastTag(String str,String tag) {
		if(!isEmpty(str)&&!isEmpty(tag)) {
			int index = str.lastIndexOf(tag);
			if(index!=-1) {
				return str.substring(0, index);
			}
		}
		return str;
	}
	
	/**
	 * 给值补0
	 * @param val
	 * @param max
	 * @return
	 */
	public static String supplyZero(String val,int max) {
		if(val == null) {
			val = "";
		}
		int valLength = val.length();
		for(int i=0;i<max-valLength;i++) {
			val += "0";
		}
		return val;
	}

	/**
	 * 将字符串中的中文替换为指定的字符串 replaceStr
	 * @param sourceStr 原字符串
	 * @param replaceStr 指定的字符串
	 * @return
	 */
	public static String tranformStr(String sourceStr, String replaceStr){
		String regex ="[\u4e00-\u9fa5]";
		Pattern pat = Pattern.compile(regex);
		Matcher mat = pat.matcher(sourceStr);
		return mat.replaceAll(replaceStr);
	}
	
	/**
	 * 获取请求ID 根据KEY_时间戳_随机数(4位)
	 * @param key
	 * @return
	 */
	public static String getRequestId(String key) {
		String time = String.valueOf(System.currentTimeMillis());
		return key + "_" + time + "_" + (int)(Math.random()*10000);
	}
	
	public static boolean find(String val,String expression) {
		Pattern p = Pattern.compile(expression); // 正则表达式 
		Matcher m = p.matcher(val);  //操作的字符串 
		return m.find();
	}

	/**
	 * 判断是否为整数
	 * @param str 传入的字符串
	 * @return 是整数返回true,否则返回false
	 */
	public static boolean isInteger(String str) {
		Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
		return pattern.matcher(str).matches();
	}
}
