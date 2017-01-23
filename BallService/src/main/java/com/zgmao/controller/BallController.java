package com.zgmao.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zgmao.table.TAnalysis;
import com.zgmao.table.TBall;
import com.zgmao.tableImpl.TAnalysisDao;
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
	@Autowired
	private TAnalysisDao tAnalysisDao;

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
	public Ball getBallNumber() {
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
		return ball;
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
	 * http://localhost:9001/api/ball/getHistory
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
	 * 从数据中查找全部历史记录
	 * http://localhost:9001/api/ball/getHistoryByPage
	 * @return
	 */
	@GetMapping("/getHistoryByPage")
	public List<Ball> getHistoryByPage(Integer page) {
		int pageNum = 20;// 每页20条数据
		if (page > 0) {
			// 前端传的页码减一
			page = page - 1;
		}
		// select * from history order by number desc limit start,pageNum;
		List<TBall> tBalls = tBallDao
				.findAllByOrderByNumberDesc(new PageRequest(page, pageNum));
		// List<TBall> tBalls = pageBalls.getContent();
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
	 * 查看特定数目的数据
	 * http://localhost:9001/api/ball/getHistoryByNumber
	 * @return
	 */
	@GetMapping("/getHistoryByNumber")
	public List<Ball> getHistoryByNumber(Integer number) {
		// select * from history order by number desc limit 0,number;
		List<TBall> tBalls = tBallDao
				.findAllByOrderByNumberDesc(new PageRequest(0, number));
		// List<TBall> tBalls = pageBalls.getContent();
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
		String lastNumber = "";// 下一期期号
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
		// 得到近20期的红球号码
		List<Ball> redBalls = getHistoryByNumber(NumberRate.MAX_COUNT_RED);
		if (redBalls == null) {
			throw new Exception("数据库双色球数据为空");
		}
		if (redBalls.size() > 0) {
			// 得到下一期的期号
			try {
				lastNumber = String.valueOf(
						Integer.valueOf(redBalls.get(0).getBallNumber()) + 1);
			} catch (Exception e) {
			}
		}
		redRates = BallAnalysisUtil.countRedBall(redRates, redBalls);
		// 得到近100期的红球号码
		List<Ball> blueBalls = getHistoryByNumber(NumberRate.MAX_COUNT_BLUE);
		buleRates = BallAnalysisUtil.countBlueBall(buleRates, blueBalls);
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
		recommendBall.setNextNumber(lastNumber);

		saveAnalysis(recommendBall);
		return recommendBall;
	}

	/**
	 * 将预测结果保存到数据库中
	 * @param recommendBall
	 */
	private void saveAnalysis(AnalysisResult analysisResult) {
		if (analysisResult == null) {
			return;
		}
		TAnalysis tAnalysis = new TAnalysis();
		tAnalysis.setNumber(analysisResult.getNextNumber());

		// 获取红球
		RecommendBall red = analysisResult.getRedBall();
		if (red != null) {
			List<NumberRate> rates = red.getMustNumberList();
			if (rates != null && rates.size() > 0) {
				// 保存强烈推荐号码
				StringBuilder sb = new StringBuilder();
				for (NumberRate item : rates) {
					sb.append(item.getNumber() + ",");
				}
				tAnalysis.setMustred(sb.toString());
			}
			List<NumberRate> rates2 = red.getNeedNumberList();
			if (rates2 != null && rates2.size() > 0) {
				// 保存一般推荐号码
				StringBuilder sb = new StringBuilder();
				for (NumberRate item : rates2) {
					sb.append(item.getNumber() + ",");
				}
				tAnalysis.setNeedred(sb.toString());
			}
		}
		// 获取蓝球
		RecommendBall blue = analysisResult.getBlueBall();
		if (blue != null) {
			List<NumberRate> rates3 = blue.getMustNumberList();
			if (rates3 != null && rates3.size() > 0) {
				// 保存强烈推荐号码
				StringBuilder sb = new StringBuilder();
				for (NumberRate item : rates3) {
					sb.append(item.getNumber() + ",");
				}
				tAnalysis.setMustredblue(sb.toString());
			}
			List<NumberRate> rates4 = blue.getNeedNumberList();
			if (rates4 != null && rates4.size() > 0) {
				// 保存一般推荐号码
				StringBuilder sb = new StringBuilder();
				for (NumberRate item : rates4) {
					sb.append(item.getNumber() + ",");
				}
				tAnalysis.setNeedblue(sb.toString());
			}
		}

		String lastNumber = analysisResult.getNextNumber();
		List<TAnalysis> oldList = tAnalysisDao.findByNumber(lastNumber);
		if (oldList != null && oldList.size() > 0) {
			Lg.d("第" + lastNumber + "期已经预测过，更新");
			// 同一期之前已经预测过了，覆盖
			tAnalysis.setId(oldList.get(0).getId());
		}
		tAnalysisDao.save(tAnalysis);
	}

	/**
	 * 根据传进来的双色球信息，判断是否中奖
	 * http://localhost:9001/api/ball/analyseWin
	 * @param ball
	 * @return
	 */
	@PostMapping("analyseWin")
	public String analyseWinMsg(@RequestBody Ball ball) {
		if (ball == null) {
			return "信息为空";
		}
		List<Integer> redBall = ball.getRedNumber();
		if (redBall == null || redBall.size() < 6) {
			return "请输入6个红球号码";
		}
		Integer blueBall = ball.getBlueNumber();
		if (blueBall == null) {
			return "请输入蓝球号码";
		}
		Ball oriBall = getBallNumber();
		return BallAnalysisUtil.analyseWin(oriBall, ball);
	}

	/**
	 * 计算所有球出现的次数
	 */
	@PostMapping("countBall")
	public String countBall() {
		// 数据库查询历史号码
		List<TBall> tBalls = (List<TBall>) tBallDao
				.findAllByOrderByNumberDesc();
		// 把历史号码转成可解析的数据结构
		List<Ball> ballList = new ArrayList<>();
		if (tBalls != null) {
			for (TBall tBall : tBalls) {
				Ball item = BallAnalysisUtil.getViewByTableVo(tBall);
				if (item != null) {
					ballList.add(item);
				}
			}
		}
		// 所有号码实体类
		int redCount = 33;
		NumberRate[] redRates = new NumberRate[redCount];
		for (int i = 0; i < redCount; i++) {
			redRates[i] = new NumberRate(i + 1, 0);
		}
		// 循环遍历号码
		for (Ball ball : ballList) {
			List<Integer> redNumberList = ball.getRedNumber();
			for (Integer number : redNumberList) {
				redRates[number - 1].addShowCount();
			}
		}
		StringBuilder sb = new StringBuilder();
		// 打印所有号码出现的次数
		for (NumberRate rate : redRates) {
			Lg.d("号码" + rate.getNumber() + "出现" + rate.getShowCount() + "次");
			sb.append("号码").append(rate.getNumber()).append("出现")
					.append(rate.getShowCount()).append("次").append("\n");
		}
		return sb.toString();
	}

	@GetMapping("getHistoryByStart")
	public void getHistoryByStart(int start, int number) {
		// List<TBall> tBalls = tBallDao.findAllByOrderByNumberDescLimit(start,
		// number);
		PageRequest pageRequest = new PageRequest(start, number, Direction.DESC,
				"number");
		List<TBall> tBalls = tBallDao.findAllByOrderByNumberDesc(pageRequest);
		for (TBall tBall : tBalls) {
			Lg.d(tBall.getNumber());
		}
	}
}
