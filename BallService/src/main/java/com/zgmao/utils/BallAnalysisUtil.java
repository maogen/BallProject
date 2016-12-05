package com.zgmao.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

import com.zgmao.table.TBall;
import com.zgmao.vo.Ball;
import com.zgmao.vo.NumberRate;
import com.zgmao.vo.RecommendBall;
import com.zgmao.vo.WinInfo;

/**
 * 号码分析工具
 * @author mzg
 *
 */
public class BallAnalysisUtil {
	/**
	 * 将从网站解析得到的实体类，转成数据库实体类
	 * 
	 * @param ball
	 * @return
	 */
	public static TBall getTableVoByView(Ball ball) {
		if (ball == null) {
			return null;
		}
		TBall tBall = new TBall();
		tBall.setNumber(ball.getBallNumber());
		tBall.setBallDate(ball.getBallDate());

		List<Integer> redNum = ball.getRedNumber();
		if (redNum != null) {
			StringBuilder sbBuilder = new StringBuilder();
			int size = redNum.size();
			for (int i = 0; i < size; i++) {
				if (i < size - 1) {
					sbBuilder.append(redNum.get(i)).append(",");
				} else {
					sbBuilder.append(redNum.get(i));
				}
			}
			tBall.setRed(sbBuilder.toString());
		}

		tBall.setBlue(ball.getBlueNumber());
		WinInfo first = ball.getFirstInfo();
		WinInfo second = ball.getSecondInfo();

		StringBuilder sbInfo = new StringBuilder();
		if (first != null) {
			sbInfo.append(first.getTitle() + "，共" + first.getWinCount() + "注，每注"
					+ first.getMoney() + "；");
		}
		if (second != null) {
			sbInfo.append(second.getTitle() + "，共" + second.getWinCount()
					+ "注，每注" + second.getMoney());
		}
		tBall.setWinInfo(sbInfo.toString());

		return tBall;
	}

	/**
	 * 将数据库实体类，转成从网站解析得到的实体类
	 * 
	 * @param tBall
	 * @return
	 */
	public static Ball getViewByTableVo(TBall tBall) {
		if (tBall == null) {
			return null;
		}
		Ball ball = new Ball();
		ball.setBallDate(tBall.getBallDate());
		ball.setBallNumber(tBall.getNumber());
		ball.setBlueNumber(tBall.getBlue());
		// 处理红球
		String redStr = tBall.getRed();
		String[] redStrArray = redStr.split(",");
		int length = redStrArray.length;
		for (int i = 0; i < length; i++) {
			ball.addRedNumber(Integer.valueOf(redStrArray[i]));
		}
		// 处理获奖信息
		String winInfo = tBall.getWinInfo();
		String[] infoArray = winInfo.split("；");
		for (int i = 0; i < infoArray.length; i++) {
			Pattern pattern = Pattern.compile("\\d+");
			Matcher matcher = pattern.matcher(infoArray[i]);
			int countIndex = 0;
			WinInfo winInfo2 = new WinInfo();
			while (matcher.find()) {
				if (countIndex == 0) {
					// 第一次扫描到整数，是注数
					winInfo2.setWinCount(Integer.valueOf(matcher.group()));
				} else if (countIndex == 1) {
					// 第二次扫描到整数，是每注奖金
					winInfo2.setMoney(Integer.valueOf(matcher.group()));
				}
				countIndex++;
			}
			if (i == 0) {
				// 一等奖
				winInfo2.setTitle("一等奖");
				ball.setFirstInfo(winInfo2);
			} else if (i == 1) {
				// 二等奖
				winInfo2.setTitle("二等奖");
				ball.setSecondInfo(winInfo2);
			}
		}
		StringBuilder sb = new StringBuilder();
		sb.append("本期").append(ball.getFirstInfo().getTitle()).append("：")
				.append(ball.getFirstInfo().getWinCount()).append("注，每注")
				.append(NumberUtils
						.format3DotNumber(ball.getFirstInfo().getMoney()))
				.append("元\n").append("本期")
				.append(ball.getSecondInfo().getTitle()).append("：")
				.append(ball.getSecondInfo().getWinCount()).append("注，每注")
				.append(NumberUtils
						.format3DotNumber(ball.getSecondInfo().getMoney()))
				.append("元");
		ball.setWinInfo(sb.toString());
		return ball;
	}

	/**
	 * 判断某个是否在列表中
	 * @param redNumberList
	 * @param number
	 * @return
	 */
	public static boolean isExistRedBall(List<Integer> redNumberList,
			int number) {
		for (Integer item : redNumberList) {
			if (number == item) {
				return true;
			}
		}
		return false;
	}

	@Test
	public void test() {
		List<Integer> list = new ArrayList<>();
		list.add(1);
		list.add(4);
		Lg.d(isExistRedBall(list, 1) + "");
		Lg.d(isExistRedBall(list, 2) + "");
	}

	/**
	 * 打印号码信息（并且返回）
	 * @param rateArray
	 */
	public static String printNumberRate(NumberRate[] rateArray, int type) {
		StringBuilder sb = new StringBuilder();
		if (type == 0) {
			sb.append("红球近期出现频率统计：\n");
		} else {
			sb.append("蓝球近期出现频率统计：\n");
		}
		for (NumberRate rate : rateArray) {
			sb.append("号码" + rate.getNumber() + "连续出现" + rate.getContinueCount()
					+ "次;连续未出现" + rate.getDismissCount() + "次;近");
			if (type == 0) {
				sb.append(NumberRate.MAX_COUNT_RED + "期该号码出现"
						+ rate.getShowCount() + "次\n");
			} else {
				sb.append(NumberRate.MAX_COUNT_BLUE + "期该号码出现"
						+ rate.getShowCount() + "次\n");
			}
		}

		Lg.d(sb.toString());
		return sb.toString();
	}

	/**
	 *  循环打印蓝球出现数目
	
	 * @param blueMap
	 */
	public static void printBlue(Map<Integer, Integer> blueMap) {
		Set<Integer> set = blueMap.keySet();
		Iterator<Integer> iterator = set.iterator();
		while (iterator.hasNext()) {
			Integer key = iterator.next();
			Integer value = blueMap.get(key);
			Lg.d(key + ":" + value);
		}
	}

	/**
	 * @param rateArray
	 */
	public static RecommendBall analysisRedBall(NumberRate[] rateArray) {
		if (rateArray == null) {
			return null;
		}
		// 强烈推荐下次必选
		List<NumberRate> mustNumberList = new ArrayList<>();
		for (NumberRate item : rateArray) {
			if (item.getDismissCount() >= 20) {
				// 如果某个号码连续未出现XX次
				mustNumberList.add(item);
			}
			if (item.getShowCount() <= 1) {
				// 如果某个号码近期仅出现1次
				mustNumberList.add(item);
			}
		}
		List<NumberRate> needNumberList = new ArrayList<>();
		// 强烈推荐之后，剩下的号码，由按照未出现次数排序，未出现次数越大，下次号码出现次数大
		sortNumber(rateArray);
		for (NumberRate item : rateArray) {
			if (needNumberList.size() + mustNumberList.size() >= 6) {
				// 号码已经有6个，结束
				break;
			}
			if (isInList(item, mustNumberList)) {
				// 号码已经在强烈推荐列表中
				continue;
			}
			if (item.getShowCount() >= 3) {
				// 号码最近20期出现频率高，不记录
				continue;
			}
			needNumberList.add(item);
		}
		RecommendBall recBall = new RecommendBall();
		recBall.setMustNumberList(mustNumberList);
		recBall.setNeedNumberList(needNumberList);
		return recBall;
		// for (NumberRate item : mustNumberList) {
		// Lg.d("强烈推荐号码：" + item.getNumber());
		// }
		// for (NumberRate item : needNumberList) {
		// Lg.d("推荐号码：" + item.getNumber());
		// }
	}

	/**
	 * @param rateArray
	 */
	public static RecommendBall analysisBlueBall(NumberRate[] rateArray) {
		if (rateArray == null) {
			return null;
		}
		// 强烈推荐下次
		List<NumberRate> mustNumberList = new ArrayList<>();
		for (NumberRate item : rateArray) {
			if (item.getDismissCount() >= 50) {
				// 如果某个号码连续未出现XX次
				mustNumberList.add(item);
			}
			if (item.getShowCount() <= 2) {
				// 如果某个号码近期仅出现XX次
				mustNumberList.add(item);
			}
		}
		List<NumberRate> needNumberList = new ArrayList<>();
		// 强烈推荐之后，剩下的号码，由按照未出现次数排序，未出现次数越大，下次号码出现次数大
		sortNumber(rateArray);
		for (NumberRate item : rateArray) {
			if (needNumberList.size() + mustNumberList.size() >= 3) {
				// 蓝色号码，仅推荐3个
				break;
			}
			if (isInList(item, mustNumberList)) {
				// 号码已经在强烈推荐列表中
				continue;
			}
			if (item.getShowCount() >= 6) {
				// 号码最近100期出现频率高，不记录
				continue;
			}
			needNumberList.add(item);
		}
		RecommendBall recBall = new RecommendBall();
		recBall.setMustNumberList(mustNumberList);
		recBall.setNeedNumberList(needNumberList);
		return recBall;
	}

	/**
	 * 某个元素是否在列表中
	 * @param bean
	 * @param list
	 * @return
	 */
	private static boolean isInList(NumberRate bean, List<NumberRate> list) {
		for (NumberRate item : list) {
			if (bean.getNumber() == item.getNumber()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 按照连续未出现次数排序
	 * @param rateArray
	 */
	public static void sortNumber(NumberRate[] rateArray) {
		if (rateArray == null) {
			return;
		}
		// 按照未出现的次数排序，插入排序
		int length = rateArray.length;
		for (int i = 1; i < length; i++) {
			NumberRate item = rateArray[i];
			NumberRate reItem = rateArray[i - 1];
			// 1：判断连续未出现次数，连续未出现次数越高，下次出现几率越大
			if (item.getDismissCount() > reItem.getDismissCount()) {
				// 后一个未出现的次数小于前一个，插入到前面
				for (int j = i; j > 0; j--) {
					if (rateArray[j - 1].getDismissCount() < rateArray[j]
							.getDismissCount()) {
						NumberRate temp = rateArray[j - 1];
						rateArray[j - 1] = rateArray[j];
						rateArray[j] = temp;
					}
				}
			}
		}
	}

	/**
	 *  分析中奖情况
	 * @param oriBall 原始号码
	 * @param myBall 我的号码
	 * @return
	 */
	public static String analyseWin(Ball oriBall, Ball myBall) {
		String myNumber = myBall.getBallNumber();
		if (StringUtils.isNotNul(myNumber)) {
			// 如果期号不为空，判断期号是否一致；如果为空，则默认为是最新的一期
			if (!myNumber.equals(oriBall.getBallNumber())) {
				return "期号不一致";
			}
		}
		List<Integer> oriRedBall = oriBall.getRedNumber();
		Integer oriBlueBall = oriBall.getBlueNumber();

		List<Integer> myRedBall = myBall.getRedNumber();
		Integer myBlueBall = myBall.getBlueNumber();

		int redCount = 0;// 红球中的个数
		for (Integer myRedItem : myRedBall) {
			for (Integer oriRedItem : oriRedBall) {
				if (myRedItem == oriRedItem) {
					redCount++;
					break;
				}
			}
		}
		if (redCount == 6 && myBlueBall == oriBlueBall) {
			return "恭喜您中了一等奖。\n本期" + oriBall.getWinInfo();
		} else if (redCount == 6) {
			return "恭喜您中了二等奖。\n本期" + oriBall.getWinInfo();
		} else if (redCount == 5 && myBlueBall == oriBlueBall) {
			return "恭喜您中了三等奖3000元。\n本期" + oriBall.getWinInfo();
		} else if ((redCount == 5)
				|| (redCount == 4 && myBlueBall == oriBlueBall)) {
			return "恭喜您中了四等奖200元。\n本期" + oriBall.getWinInfo();
		} else if ((redCount == 4)
				|| (redCount == 3 && myBlueBall == oriBlueBall)) {
			return "恭喜您中了五等奖10元。\n本期" + oriBall.getWinInfo();
		} else if ((redCount == 2 && myBlueBall == oriBlueBall)
				|| (redCount == 1 && myBlueBall == oriBlueBall)
				|| (myBlueBall == oriBlueBall)) {
			return "恭喜您中了六等奖5元。\n本期" + oriBall.getWinInfo();
		}
		return "很遗憾，您未中奖";
	}
}
