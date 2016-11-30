package com.zgmao.vo;

/**
 * 数字出现消失频率统计
 * @author mzg
 *
 */
public class NumberRate {
	// 数字
	private int number;
	// 连续出现次数，默认未出现
	private int continueCount = 0;
	// 连续消失次数，默认已经消失
	private int dismissCount = 0;
	// 在记录未出现过程中，是否出现了数字
	private boolean isShow = false;
	// 在记录出现过程中，是否出现了空白
	private boolean isNotShow = false;

	public static int MAX_COUNT_RED = 20;
	public static int MAX_COUNT_BLUE = 100;
	// 最近${MAX_COUNT}期，该号码出现的次数
	private int showCount = 0;

	/**
	 * 参数构造函数
	 * @param number 号码
	 * @param ballType 0-红球，1-蓝球
	 */
	public NumberRate(Integer number, int ballType) {
		this.number = number;
		// dismissCount = 0;
		// 设置默认号码未出现频率
		// if (ballType == 0) {
		// dismissCount = dismissRed;
		// } else {
		// dismissCount = dismissBlue;
		// }
	}

	/**
	 * 参数构造函数
	 * @param number 号码
	 */
	public NumberRate(Integer number) {
		this.number = number;
	}

	/**
	 * 增加一次连续出现次数
	 */
	public void addContinueCount() {
		if (!isNotShow) {
			continueCount++;
		}
	}

	/**
	 * 增加一次连续消失次数
	 */
	public void addDismissCount() {
		if (!isShow) {
			// 如果之前没有出现，则未出现次数加1
			dismissCount++;
		}

	}

	/**
	 * 出现了数字
	 */
	public void setShow() {
		isShow = true;
	}

	/**
	 * 出现了空白
	 */
	public void setNotShow() {
		isNotShow = true;
	}

	/**
	 * 出现的次数
	 */
	public void addShowCount() {
		showCount++;
	}

	public int getNumber() {
		return number;
	}

	public int getContinueCount() {
		return continueCount;
	}

	public int getDismissCount() {
		return dismissCount;
	}

	public int getShowCount() {
		return showCount;
	}
}
