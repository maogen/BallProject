package com.zgmao.controller;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.zgmao.utils.RequestUtil;
import com.zgmao.utils.StringUtils;
import com.zgmao.vo.Ball;

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
			ball.setBallNumber(matcher.group());
			break;
		}
		// 获取日期
		pattern = Pattern.compile("开奖日期：.{14}");
		matcher = pattern.matcher(result);
		while (matcher.find()) {
			ball.setBallDate(matcher.group());
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
}
