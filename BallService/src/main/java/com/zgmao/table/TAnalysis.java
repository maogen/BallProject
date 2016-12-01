package com.zgmao.table;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "analysis")
public class TAnalysis implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@Column
	private String number;// 期号
	@Column
	private String mustred;// 强烈推荐
	@Column
	private String needred;// 一般推荐
	@Column
	private String mustredblue;// 强烈推荐
	@Column
	private String needblue;// 一般推荐

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getMustred() {
		return mustred;
	}

	public void setMustred(String mustred) {
		this.mustred = mustred;
	}

	public String getNeedred() {
		return needred;
	}

	public void setNeedred(String needred) {
		this.needred = needred;
	}

	public String getMustredblue() {
		return mustredblue;
	}

	public void setMustredblue(String mustredblue) {
		this.mustredblue = mustredblue;
	}

	public String getNeedblue() {
		return needblue;
	}

	public void setNeedblue(String needblue) {
		this.needblue = needblue;
	}


}
