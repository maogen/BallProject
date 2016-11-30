package com.zgmao.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.zgmao.table.TBall;
import com.zgmao.tableImpl.TBallDao;
import com.zgmao.utils.BallAnalysisUtil;
import com.zgmao.utils.Lg;
import com.zgmao.utils.RequestUtil;
import com.zgmao.utils.StringUtils;
import com.zgmao.vo.AnalysisResult;
import com.zgmao.vo.Ball;
import com.zgmao.vo.NumberRate;
import com.zgmao.vo.RecommendBall;
import com.zgmao.vo.WinInfo;

@RestController
@RequestMapping("/api/ball")
public class BallController {
	/**
	 * 数据库操作
	 */
	@Autowired
	private TBallDao tBallDao;

	@GetMapping("/index")
	public String index() {
		return "HelloBall";
	}

	/**
	 * 获取最新一期双色球内容，并且插入数据库中
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
		// 将数据插入到数据库中
		insertVo(BallAnalysisUtil.getTableVoByView(ball));
		return new Gson().toJson(ball);
	}

	/**
	 * 得到历史记录，并且插入到数据库
	 * 
	 * @return
	 */
	@GetMapping("/insertHistory")
	public List<Ball> insertHistoryBall() {
		Pattern pattern;
		Matcher matcher;
		// 搜索双色球
		String url = "http://baidu.lecai.com/lottery/draw/list/50?type=latest&num=100";
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
		// 将数据插入到数据库中
		insertListVo(ballList);
		return ballList;
	}

	/**
	 * 从数据中查找全部历史记录
	 * http:localhost:9001/api/ball/getHistory
	 * @return
	 */
	@GetMapping("/getHistory")
	public List<Ball> getHistoryByDB() {
		List<TBall> tBalls = (List<TBall>) tBallDao
				.findAllByOrderByNumberDesc();
		List<Ball> ballList = new ArrayList<>();
		if (tBalls != null) {
			for (TBall tBall : tBalls) {
				Ball item = BallAnalysisUtil.getViewByTableVo(tBall);
				if (item != null) {
					ballList.add(item);
				}
			}
		}
		return ballList;
	}

	/**
	 * 将列表插入到数据库
	 * 
	 * @param ballList
	 */
	private void insertListVo(List<Ball> ballList) {
		if (ballList == null) {
			return;
		}
		int size = ballList.size();
		for (int i = 0; i < size; i++) {
			TBall tBall = BallAnalysisUtil.getTableVoByView(ballList.get(i));
			insertVo(tBall);
		}
	}

	/**
	 * 插入实体类
	 * 
	 * @param tBall
	 */
	private void insertVo(TBall tBall) {
		if (tBall == null) {
			return;
		}
		List<TBall> existList = tBallDao.findByNumber(tBall.getNumber());
		if (existList != null && existList.size() > 0) {
			Lg.d("该期号已经存在，不插入");
		} else {
			tBallDao.save(tBall);
		}
	}

	/**
	 * 出现总次数 	16 	15 	23 	13 	15 	23 	14 	20 	18 	16 	15 	25 	17 	19 	18 	14 	19 	21 	22 	24 	15 	21 	16 	15 	18 	23 	22 	18 	19 	15 	17 	23 	11 	10 	5 	7 	10 	4 	3 	5 	5 	8 	5 	6 	7 	4 	6 	5 	10
	 * 平均遗漏值 	5 	6 	3 	5 	5 	3 	5 	4 	3 	4 	6 	3 	5 	4 	4 	7 	4 	3 	3 	4 	6 	3 	4 	5 	4 	3 	5 	3 	4 	6 	4 	2 	9 	7 	12 	20 	5 	13 	25 	14 	12 	8 	12 	12 	7 	16 	9 	15 	8
	 * 最大遗漏值 	22 	24 	10 	17 	22 	11 	17 	14 	15 	13 	25 	16 	22 	17 	15 	25 	15 	13 	15 	22 	20 	14 	15 	18 	15 	11 	23 	11 	13 	25 	14 	8 	36 	21 	37 	60 	16 	37 	63 	37 	31 	20 	29 	33 	22 	44 	32 	40 	32
	 * 最大连出值 	2 	2 	3 	2 	3 	2 	2 	3 	2 	2 	2 	3 	3 	3 	2 	3 	3 	3 	2 	4 	2 	2 	2 	2 	2 	2 	3 	3 	2 	4 	2 	2 	2 	2 	2 	1 	1 	1 	2 	2 	2 	2 	1 	2 	1 	1 	1 	3 	1
	            1 	2 	3 	4 	5 	6 	7 	8 	9 	10 	11 	12 	13 	14 	15 	16 	17 	18 	19 	20 	21 	22 	23 	24 	25 	26 	27 	28 	29 	30 	31 	32 	33 	1 	2 	3 	4 	5 	6 	7 	8 	9 	10 	11 	12 	13 	14 	15 	16
	 * 分析
	 * http:localhost:9001/api/ball/analysis
	 * @throws Exception 
	 */
	@GetMapping("/analysis")
	public AnalysisResult analysis() throws Exception {
		// 初始化红球数组
		int redCount = 33;
		int buleCount = 16;
		NumberRate[] redRates = new NumberRate[redCount];
		NumberRate[] buleRates = new NumberRate[buleCount];
		for (int i = 0; i < redCount; i++) {
			redRates[i] = new NumberRate(i + 1, 0);
		}
		for (int i = 0; i < buleCount; i++) {
			buleRates[i] = new NumberRate(i + 1, 1);
		}
		// 得到数据库数据
		List<Ball> balls = getHistoryByDB();
		if (balls == null) {
			throw new Exception("数据库双色球数据为空");
		}
		// 保存蓝球出现个数
		Map<Integer, Integer> blueMap = new HashMap<>();
		// 循环遍历，处理数字
		for (int i = 0; i < balls.size(); i++) {
			Ball ball = balls.get(i);
			// 得到红球号码和蓝球号码
			int blueNumber = ball.getBlueNumber();
			List<Integer> redNumberList = ball.getRedNumber();
			// 循环记录33个红球号码
			for (int j = 0; j < redRates.length; j++) {
				NumberRate itemRedRate = redRates[j];
				if (BallAnalysisUtil.isExistRedBall(redNumberList,
						itemRedRate.getNumber())) {
					// 红球出现
					itemRedRate.addContinueCount();
					itemRedRate.setShow();

					if (i < NumberRate.MAX_COUNT_RED) {
						// 最近红球出现一次，记录
						itemRedRate.addShowCount();
					}

				} else {
					// 出现空白
					// 红球没出现
					itemRedRate.addDismissCount();
					itemRedRate.setNotShow();
				}
			}
			// 循环记录16个红球号码
			for (int j = 0; j < buleRates.length; j++) {
				NumberRate itemBlueRate = buleRates[j];
				if (blueNumber == itemBlueRate.getNumber()) {
					// 篮球出现
					itemBlueRate.addContinueCount();
					itemBlueRate.setShow();
					if (i < NumberRate.MAX_COUNT_BLUE) {
						// 最近篮球出现一次，记录
						itemBlueRate.addShowCount();
					}
				} else {
					itemBlueRate.addDismissCount();
					itemBlueRate.setNotShow();
				}
			}
			// 保存蓝球出现的个数
			if (blueMap.containsKey(blueNumber)) {
				int count = blueMap.get(ball.getBlueNumber());
				count++;
				blueMap.put(blueNumber, count);
			} else {
				blueMap.put(blueNumber, 1);
			}
		}
		// BallAnalysisUtil.printBlue(blueMap);
		// 排序，打印结果
		BallAnalysisUtil.sortNumber(redRates);
		BallAnalysisUtil.printNumberRate(redRates, 0);
		BallAnalysisUtil.sortNumber(buleRates);
		BallAnalysisUtil.printNumberRate(buleRates, 1);
		// 设置结果
		AnalysisResult recommendBall = new AnalysisResult();
		RecommendBall redBall = BallAnalysisUtil.analysisRedBall(redRates);
		recommendBall.setRedBall(redBall);
		RecommendBall blueBall = BallAnalysisUtil.analysisBlueBall(buleRates);
		recommendBall.setBlueBall(blueBall);
		return recommendBall;
	}

}
