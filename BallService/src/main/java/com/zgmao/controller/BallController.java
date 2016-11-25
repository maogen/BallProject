package com.zgmao.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.zgmao.table.TBall;
import com.zgmao.tableImpl.TBallDao;
import com.zgmao.utils.RequestUtil;
import com.zgmao.utils.StringUtils;
import com.zgmao.vo.Ball;
import com.zgmao.vo.WinInfo;

@RestController
@RequestMapping("/api/ball")
public class BallController {

	@GetMapping("/index")
	public String index() {
		return "HelloBall";
	}

	/**
	 * 获取双色球内容
	 * 
	 * @return
	 */
	@GetMapping("/getBall")
	public String getBallNumber() {
		Ball ball = new Ball();

		// 搜索双色球
		String url = "http://www.baidu.com.cn/s?wd=双色球";
		String result = RequestUtil.baseGet(url, "", null);
		// 由于双色球结果用特殊的格式显示，只需要根据样式获取子串
		String ballResultClass = "c-border c-gap-bottom-small";// 开奖结果显示的div的class
		// 截取有关双色球的内容
		int startIndex = result.indexOf(ballResultClass)
				+ ballResultClass.length() + 5;
		int endIndex = startIndex + 1152;
		result = result.substring(startIndex, endIndex);
		// 获取期号
		Pattern pattern = Pattern.compile("第.*期开奖结果");
		Matcher matcher = pattern.matcher(result);
		while (matcher.find()) {
			ball.setBallNumber(matcher.group().replaceAll("第", "")
					.replaceAll("期开奖结果", ""));
			break;
		}
		// 获取日期
		pattern = Pattern.compile("开奖日期：.{14}");
		matcher = pattern.matcher(result);
		while (matcher.find()) {
			ball.setBallDate(matcher.group().replaceAll("开奖日期：", ""));
			break;
		}
		// 获取红色号码和蓝色号码
		int index = 0;
		pattern = Pattern.compile("c-gap-right-small\">.{2}");
		matcher = pattern.matcher(result);
		while (matcher.find()) {
			String numStr = matcher.group();
			if (StringUtils.isNotNul(numStr) && numStr.length() > 2) {
				Integer number = Integer.valueOf(
						numStr.substring(numStr.length() - 2, numStr.length()));
				if (index < 6) {
					// 红色号码
					ball.addRedNumber(number);
				} else {
					ball.setBlueNumber(number);
				}

			}
			index++;
		}
		// 获取本地一等奖、二等奖信息
		pattern = Pattern.compile("本期.{1}等奖.*");
		matcher = pattern.matcher(result);
		while (matcher.find()) {
			String info = matcher.group().replaceAll("</p>", "");
			ball.addWinInfo(info);
		}
		return new Gson().toJson(ball);
	}

	/**
	 * 得到历史记录
	 * 
	 * @return
	 */
	@GetMapping("/getHistory")
	public List<Ball> getHistoryBall() {
		Pattern pattern;
		Matcher matcher;
		// 搜索双色球
		String url = "http://baidu.lecai.com/lottery/draw/list/50?type=latest&num=50";
		String result = RequestUtil.baseGet(url, "", null);
		// Lg.d(result);
		String[] linesReslut = result.split("\n");// 将结果按行读取
		int lines = linesReslut.length;
		int startLine = 10000;// 开始的历史行数

		boolean isNumberSave = false;// 是否解析到了期数
		List<Ball> ballList = new ArrayList<>();// 保存历史记录
		Ball itemBall = null;// 当解析到了期数，初始化
		for (int i = 0; i < lines; i++) {
			String itemLine = linesReslut[i].replaceAll(" ", "");
			if (itemLine.contains("historylist")) {
				// 保存历史记录开始位置
				startLine = i;
			}
			if (i >= startLine) {
				// 如果文本是在历史记录之后
				// Lg.d("第" + i + "行：" + itemLine);
				if (itemLine.contains("/lottery/draw/view/50?phase=")) {
					// 扫描到期数行
					pattern = Pattern.compile("\\d{7}");
					matcher = pattern.matcher(itemLine);

					itemBall = new Ball();
					while (matcher.find()) {
						String number = matcher.group();
						itemBall.setBallNumber(number);
						break;
					}
					isNumberSave = true;// 期号已经保存
					// 读取下一行的日期
					String dateStr = linesReslut[i + 1].replaceAll("<td>", "")
							.replaceAll("</td>", "");
					itemBall.setBallDate(dateStr.replaceAll(" ", ""));
				} else if (itemLine.contains("redBalls") && isNumberSave) {
					// 扫描到红球样式行，下一行是红球号码
					String redStr = linesReslut[i + 1];
					pattern = Pattern.compile("\\d{1,2}");
					matcher = pattern.matcher(redStr);
					while (matcher.find()) {
						itemBall.addRedNumber(Integer.valueOf(matcher.group()));
					}
				} else if (itemLine.contains("blueBalls") && isNumberSave) {
					// 扫描到篮球，下一行是篮球号码
					String blueStr = linesReslut[i + 1];
					pattern = Pattern.compile("\\d{1,2}");
					matcher = pattern.matcher(blueStr);
					while (matcher.find()) {
						itemBall.setBlueNumber(
								Integer.valueOf(matcher.group()));
						break;
					}
				} else if (itemLine.contains("NotNumber") && isNumberSave) {
					// 扫描到注数
					// 1:<tdclass="NotNumber">注数</td>
					// 2： <tdclass="NotNumber">12</td>
					String winCountStr = linesReslut[i].replaceAll(" ", "");
					// 下一行是单注奖金
					String moneyStr = linesReslut[i + 1].replaceAll(" ", "");

					WinInfo winInfo = new WinInfo();
					pattern = Pattern.compile("\\d+");
					matcher = pattern.matcher(winCountStr);
					// if (!matcher.matches()) {
					// // 如果扫描到注数没有数字，说明是表头信息，下一次循环
					// continue;
					// }
					while (matcher.find()) {
						winInfo.setWinCount(Integer.valueOf(matcher.group()));
						break;
					}
					matcher = pattern.matcher(moneyStr);
					while (matcher.find()) {
						winInfo.setMoney(Integer.valueOf(matcher.group()));
						break;
					}
					if (itemBall.getFirstInfo() == null) {
						// 一等奖信息为空
						winInfo.setTitle("一等奖");
						itemBall.setFirstInfo(winInfo);
					} else {
						// 设置二等奖信息
						winInfo.setTitle("二等奖");
						itemBall.setSecondInfo(winInfo);
						// 设置完二等奖信息后，信息保存
						ballList.add(itemBall);
						isNumberSave = false;
					}
				}
			}
		}
		// if (ballList != null) {
		// int size = ballList.size();
		// for (int i = 0; i < size; i++) {
		// daoImpl.save(ballList.get(i));
		// }
		// }
		return ballList;
	}

	// @Autowired
	// TBallDao tBallDao;
	//
	// @GetMapping("/select")
	// public String getSelect() {
	// TBall tball = tBallDao.findBall("2016123");
	// return new Gson().toJson(tball);
	// }
	// @Autowired
	// private TBallDaoImpl daoImpl;
}
