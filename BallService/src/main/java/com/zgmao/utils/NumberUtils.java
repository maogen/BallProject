package com.zgmao.utils;

import java.text.DecimalFormat;

import org.junit.Test;

/**
 * 数字格式化工具
 * 
 * @author mzg
 *
 */
public class NumberUtils {
	/**
	 * 将数字格式化输出
	 * 
	 * @param number
	 * @param format
	 * @return
	 */
	public static String formatNumber(int number, String format) {
		return new DecimalFormat(format).format(number);
	}

	/**
	 * 将数字转成三位数字逗号分割
	 * 
	 * @param number
	 * @return
	 */
	public static String format3DotNumber(int number) {
		return formatNumber(number, ",###");
	}

	@Test
	public void test() {
		Lg.d(format3DotNumber(12345676));
		Lg.d(format3DotNumber(1234));
		Lg.d(format3DotNumber(123));
	}
}
