package com.zgmao.vo;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.zgmao.utils.StringUtils;

public class Ball {
	private String ballNumber = null;// 期号
	private String ballDate = null;// 日期
	private List<Integer> redNumber;
	private Integer blueNumber;
	private String winInfo;// 获奖信息
	private WinInfo firstInfo;// 获奖详情
	private WinInfo secondInfo;

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
		if (StringUtils.isNull(info)) {
			return;
		}
		if (StringUtils.isNotNul(winInfo)) {
			this.winInfo = this.winInfo + "\n" + info;
		} else {
			this.winInfo = info;
		}
		info = info.replaceAll(",", "");// 去除数字之间的,号
		int index = 0;// 记录第几次获取数字，第一个数字表示中奖注数；第二个数字表示每注中奖金额
		if (info.contains("一等奖")) {
			// 一等奖
			firstInfo = new WinInfo();
			firstInfo.setTitle("一等奖");
			Pattern pattern = Pattern.compile("\\d+");
			Matcher matcher = pattern.matcher(info);
			while (matcher.find()) {
				if (index == 0) {
					firstInfo.setWinCount(Integer.valueOf(matcher.group()));
				} else if (index == 1) {
					firstInfo.setMoney(Integer.valueOf(matcher.group()));
				}
				index++;
			}
		} else if (info.contains("二等奖")) {
			// 二等奖
			secondInfo = new WinInfo();
			secondInfo.setTitle("二等奖");
			Pattern pattern = Pattern.compile("\\d+");
			Matcher matcher = pattern.matcher(info);
			while (matcher.find()) {
				if (index == 0) {
					secondInfo.setWinCount(Integer.valueOf(matcher.group()));
				} else if (index == 1) {
					secondInfo.setMoney(Integer.valueOf(matcher.group()));
				}
				index++;
			}
		}
	}

	public WinInfo getFirstInfo() {
		return firstInfo;
	}

	public void setFirstInfo(WinInfo firstInfo) {
		this.firstInfo = firstInfo;
	}

	public WinInfo getSecondInfo() {
		return secondInfo;
	}

	public void setSecondInfo(WinInfo secondInfo) {
		this.secondInfo = secondInfo;
	}
	

}
