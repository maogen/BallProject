package com.zgmao.tableImpl;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.zgmao.table.TBall;

@Transactional
public interface TBallDao extends CrudRepository<TBall, Long> {

	TBall find(String number);

	@Query("from history u where u.number=:number")
	TBall findBall(@Param("number") String number);
}
