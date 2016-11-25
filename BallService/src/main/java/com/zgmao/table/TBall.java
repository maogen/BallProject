package com.zgmao.table;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "history")
public class TBall {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String number;// 期号
	private String ballDate;// 日期
	private String red;// 红色号码用,隔开
	private Integer blue;
	private String winInfo;// 获奖信息

	public Long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getBallDate() {
		return ballDate;
	}

	public void setBallDate(String ballDate) {
		this.ballDate = ballDate;
	}

	public String getRed() {
		return red;
	}

	public void setRed(String red) {
		this.red = red;
	}

	public Integer getBlue() {
		return blue;
	}

	public void setBlue(Integer blue) {
		this.blue = blue;
	}

	public String getWinInfo() {
		return winInfo;
	}

	public void setWinInfo(String winInfo) {
		this.winInfo = winInfo;
	}

}
