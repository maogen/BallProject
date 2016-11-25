package com.zgmao.tableImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zgmao.table.TBall;
import com.zgmao.vo.Ball;
import com.zgmao.vo.WinInfo;

@Service
public class TBallDaoImpl {
	@Autowired
	private TBallDao tBallDao;

	public TBall save(Ball ball) {
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

		TBall oldBall = tBallDao.find(tBall.getNumber());
		if (oldBall != null) {
			tBall.setId(oldBall.getId());
		}
		tBallDao.save(tBall);
		return tBall;
	}
}
