package com.zgmao.vo;

import java.util.ArrayList;
import java.util.List;

import com.zgmao.utils.StringUtils;

public class Ball {
	private String ballNumber = null;// 期号
	private String ballDate = null;// 日期
	private List<Integer> redNumber;
	private Integer blueNumber;
	private String winInfo;// 获奖信息

	public Ball() {
		redNumber = new ArrayList<>();
	}

	public String getBallNumber() {
		return ballNumber;
	}

	public void setBallNumber(String ballNumber) {
		this.ballNumber = ballNumber;
	}

	public String getBallDate() {
		return ballDate;
	}

	public void setBallDate(String ballDate) {
		this.ballDate = ballDate;
	}

	public List<Integer> getRedNumber() {
		return redNumber;
	}

	public void addRedNumber(Integer redNum) {
		// int size = this.redNumber.size();
		// for (int i = 0; i < size; i++) {
		// if (redNumber.get(i) > redNum) {
		// this.redNumber.add(i, redNum);
		// return;
		// }
		// }
		this.redNumber.add(redNum);
	}

	public Integer getBlueNumber() {
		return blueNumber;
	}

	public void setBlueNumber(Integer blueNumber) {
		this.blueNumber = blueNumber;
	}

	public String getWinInfo() {
		return winInfo;
	}

	public void setWinInfo(String winInfo) {
		this.winInfo = winInfo;
	}

	public void addWinInfo(String info) {
		if (StringUtils.isNotNul(winInfo)) {
			this.winInfo = this.winInfo + "\n" + info;
		} else {
			this.winInfo = info;
		}

	}

}
