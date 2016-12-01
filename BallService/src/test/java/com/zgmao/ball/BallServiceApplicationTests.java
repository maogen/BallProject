package com.zgmao.ball;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.zgmao.controller.BallController;
import com.zgmao.utils.Lg;
import com.zgmao.vo.AnalysisResult;
import com.zgmao.vo.NumberRate;
import com.zgmao.vo.RecommendBall;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BallServiceApplicationTests {

	@Autowired
	private BallController controll;

	@Test
	public void contextLoads() {
		try {
			AnalysisResult re = controll.analysis();
			Lg.d("第" + re.getNextNumber() + "期：");
			// 获取红球
			RecommendBall red = re.getRedBall();
			List<NumberRate> rates = red.getMustNumberList();
			if (rates == null || rates.size() == 0) {
				Lg.d("无强烈推荐红球");
			} else {
				for (NumberRate item : rates) {
					Lg.d("推荐红球:" + item.getNumber());
				}

			}
			List<NumberRate> rates2 = red.getNeedNumberList();
			if (rates2 != null && rates2.size() > 0) {
				for (NumberRate item : rates2) {
					Lg.d("推荐红球:" + item.getNumber());
				}

			}
			// 获取蓝球
			RecommendBall blue = re.getBlueBall();
			List<NumberRate> rates3 = blue.getMustNumberList();
			if (rates3 == null || rates3.size() == 0) {
				Lg.d("无强烈推荐蓝球");
			}
			List<NumberRate> rates4 = blue.getNeedNumberList();
			if (rates4 != null && rates4.size() > 0) {
				for (NumberRate item : rates4) {
					Lg.d("推荐蓝球:" + item.getNumber());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
