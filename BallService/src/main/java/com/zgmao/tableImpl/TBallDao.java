package com.zgmao.tableImpl;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.zgmao.table.TBall;

@Transactional
@Repository
public interface TBallDao extends JpaRepository<TBall, Long> {
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

	/**
	 * 按照number逆序查询
	 * @return
	 */
	List<TBall> findAllByOrderByNumberDesc(Pageable pageable);

//	@Query("select h from history h order by h.number desc limit :start_number,:max_number")
//	List<TBall> findAllByOrderByNumberDescLimit(@Param("start_number") int start_number,
//			@Param("max_number") int max_number);

}
