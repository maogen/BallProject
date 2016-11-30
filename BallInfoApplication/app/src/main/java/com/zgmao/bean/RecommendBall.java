package com.zgmao.bean;

import java.util.List;

/**
 * 推荐
 * @author mzg
 *
 */
public class RecommendBall {
	private List<NumberRate> mustNumberList;// 强烈推荐
	private List<NumberRate> needNumberList;// 一般推荐

	public List<NumberRate> getMustNumberList() {
		return mustNumberList;
	}

	public void setMustNumberList(List<NumberRate> mustNumberList) {
		this.mustNumberList = mustNumberList;
	}

	public List<NumberRate> getNeedNumberList() {
		return needNumberList;
	}

	public void setNeedNumberList(List<NumberRate> needNumberList) {
		this.needNumberList = needNumberList;
	}

}
