package com.zgmao.utils;

/**
 * 打印消息工具
 * 
 * @author mzg
 *
 */
public class Lg {
	// 是否打印消息
	private static boolean isLog = true;

	/**
	 * 打印消息
	 * 
	 * @param msg
	 */
	public static void d(String msg) {
		if (isLog) {
			System.out.println(msg);
			FileUtils.writeLog(msg);
		}
	}

	public static void write(String msg) {
		if (isLog) {
			System.out.println(msg);
			FileUtils.writeLog(msg);
		}
	}

	public static void writeError(String msg) {
		if (isLog) {
			System.out.println(msg);
			// log写到文件中
			FileUtils.writeCrashLog(msg);
		}

	}
}
