package com.zgmao.bean;

import java.util.List;

public class Ball {
    private String ballNumber = null;// 期号
    private String ballDate = null;// 日期
    private List<Integer> redNumber;
    private Integer blueNumber;
    private String winInfo;// 获奖信息

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

    public void setRedNumber(List<Integer> redNumber) {
        this.redNumber = redNumber;
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

}
