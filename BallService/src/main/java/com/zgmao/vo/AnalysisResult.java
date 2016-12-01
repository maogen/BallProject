package com.zgmao.vo;

/**
 * 分析结果
 * @author mzg
 *
 */
public class AnalysisResult {
	private String nextNumber;// 下一期期号
	private RecommendBall redBall;
	private RecommendBall blueBall;

	public String getNextNumber() {
		return nextNumber;
	}

	public void setNextNumber(String nextNumber) {
		this.nextNumber = nextNumber;
	}

	public RecommendBall getRedBall() {
		return redBall;
	}

	public void setRedBall(RecommendBall redBall) {
		this.redBall = redBall;
	}

	public RecommendBall getBlueBall() {
		return blueBall;
	}

	public void setBlueBall(RecommendBall blueBall) {
		this.blueBall = blueBall;
	}

}
