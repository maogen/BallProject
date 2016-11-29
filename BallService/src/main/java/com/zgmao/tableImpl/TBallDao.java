package com.zgmao.tableImpl;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.zgmao.table.TBall;
import java.lang.String;
import java.util.List;

@Transactional
@Repository
public interface TBallDao extends CrudRepository<TBall, Long> {

	List<TBall> findByNumber(String number);
//	TBall find(String number);

//	@Query("from history u where u.number = :number")
//	TBall findBall(@Param("number") String number);
	
}
