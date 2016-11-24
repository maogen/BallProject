package com.zgmao.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.UUID;

/**
 * 字符串判断工具类
 * 
 * @author mzg
 *
 */
public class StringUtils {
	/**
	 * 判断字符串是空
	 * 
	 * @param string
	 * @return
	 */
	public static boolean isNull(String string) {
		if (null == string || "".equals(string)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 判断字符串不是空
	 * 
	 * @param string
	 * @return
	 */
	public static boolean isNotNul(String string) {
		return !isNull(string);
	}

	/**
	 * InputStream流转成String，流不在本方法中关闭
	 * 
	 * @param is
	 * @return
	 */
	public static String streamToString(InputStream is) {
		StringBuffer sb = new StringBuffer();
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "utf-8"));
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line).append("\n");
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

	/**
	 * 获取随机的uid
	 * 
	 * @return
	 */
	public static String randomUid() {
		UUID uuid = UUID.randomUUID();
		return uuid.toString();
		// int uidLength = 20;// 十位数的uid
		// String keys = "qwertyuiopasdfghjklzxcvbnm1234567890";
		// int keyLength = keys.length();
		// StringBuffer uid = new StringBuffer();
		// for (int i = 0; i < uidLength; i++) {
		// int count = (int) (Math.random() * keyLength);
		// uid.append(keys.charAt(count));
		// }
		// return uid.toString();
	}

	/**
	 * 判断字符串是否是数字
	 * 
	 * @param string
	 * @return
	 */
	public static boolean isNumber(String string) {
		Pattern pattern = Pattern.compile("[-]*[0-9]*");
		Matcher isNum = pattern.matcher(string);
		if (!isNum.matches()) {
			return false;
		}
		return true;
	}

	/**
	 * 将字符串转成数字
	 * 
	 * @param string
	 * @return
	 */
	public static int string2Number(String string) {
		if (isNull(string)) {
			return -1;
		}
		if (!isNumber(string)) {
			return -1;
		}
		return Integer.valueOf(string);
	}

	public static void main(String[] args) {
		System.out.println(randomUid());
		System.out.println(isNumber("-123"));
		System.out.println(isNumber("avd"));
		System.out.println(isNumber("1a1vd"));
		System.out.println(isNumber(""));
		System.out.println(string2Number(""));
	}
}
