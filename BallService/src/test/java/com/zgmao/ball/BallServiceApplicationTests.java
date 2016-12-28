package com.zgmao.ball;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.google.gson.Gson;
import com.zgmao.controller.BallController;
import com.zgmao.utils.Lg;
import com.zgmao.vo.AnalysisResult;
import com.zgmao.vo.Ball;
import com.zgmao.vo.NumberRate;
import com.zgmao.vo.RecommendBall;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BallServiceApplicationTests {

	@Autowired
	private BallController controll;

	/**
	 * 测试得到最近号码
	 */
	@Test
	public void getBallByNumber() {
		Lg.d(new Gson().toJson(controll.getHistoryByNumber(100)));
	}

	/**
	 * 测试得到最近号码
	 */
	@Test
	public void getBall() {
		Lg.d(new Gson().toJson(controll.getBallNumber()));
	}

	/**
	 * 测试获奖信息
	 */
	@Test
	public void getWin() {
		Ball ball = new Ball();
		ball.addRedNumber(3);
		ball.addRedNumber(11);
		ball.addRedNumber(14);
		ball.addRedNumber(22);
		ball.addRedNumber(23);
		ball.addRedNumber(31);

		ball.setBlueNumber(5);
		Lg.d(controll.analyseWinMsg(ball));
	}

	@Test
	public void getHistoryByPage() {
		List<Ball> balls = controll.getHistoryByPage(1);
		if (balls != null) {
			for (Ball ball : balls) {
				Lg.d(ball.getBallDate());
			}
		}
		Lg.d("==============");
		List<Ball> balls2 = controll.getHistoryByPage(2);
		if (balls2 != null) {
			for (Ball ball : balls2) {
				Lg.d(ball.getBallDate());
			}
		}
		Lg.d("==============");
		List<Ball> balls3 = controll.getHistoryByPage(3);
		if (balls3 != null) {
			for (Ball ball : balls3) {
				Lg.d(ball.getBallDate());
			}
		}
	}

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

	@Test
	public void headRequest() throws ClientProtocolException, IOException {
		int fileSize = 0;

		String url = "https://v0.api.upyun.com/ytbtest";
		String image_url = "/2016/09/d3533af7-9bae-437a-866a-bbc5fb975007.JPG";
		String token = "Basic aW5mbzpUcEZxamthaTNKOUE5SkRvYytOd1c2N01QcWtNZlNCQk1MQlo4ZnJnSGtBPQ==";

		CloseableHttpClient httpClient = HttpClientBuilder.create().build();
		HttpHead httpHead = new HttpHead(url + image_url);
		httpHead.addHeader("Authorization", token);
		CloseableHttpResponse httpResponse = null;
		httpResponse = httpClient.execute(httpHead);
		Header[] headers = httpResponse.getAllHeaders();
		if (headers != null) {
			int length = headers.length;
			for (int i = 0; i < length; i++) {
				Header header = headers[i];
				System.out.println(header.getName() + ":" + header.getValue());
				if ("x-upyun-file-size".equals(header.getName())) {
					fileSize = Integer.valueOf(header.getValue());
				}
			}
		}
		httpResponse.close();
		httpClient.close();
		fileSize = (int) (1024);
		if (fileSize >= (1024 * 1024)) {
			// 图片大小大于1M
			double size = fileSize / 1024.0 / 1024.0;
			String numStr = new DecimalFormat("#").format(size);
			System.out.println(numStr + "M");
		} else if (fileSize >= 1024) {
			// 图片大于1KB
			double size = fileSize / 1024.0;
			String numStr = new DecimalFormat("#").format(size);
			System.out.println(numStr + "K");
		} else {
			// 图片
			System.out.println(fileSize + "B");
		}

	}

}
