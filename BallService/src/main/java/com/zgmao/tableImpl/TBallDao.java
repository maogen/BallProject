package com.zgmao.tableImpl;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.zgmao.table.TBall;

@Transactional
@Repository
public interface TBallDao extends PagingAndSortingRepository<TBall, Long> {
	/**
	 * 根据期号搜索
	 * @param number
	 * @return
	 */
	List<TBall> findByNumber(String number);

	/**
	 * 按照number顺序查询
	 * @return
	 */
	List<TBall> findAllByOrderByNumberAsc();

	/**
	 * 按照number逆序查询
	 * @return
	 */
	List<TBall> findAllByOrderByNumberDesc();
}
