package com.zgmao.tableImpl;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.zgmao.table.TAnalysis;

@Transactional
@Repository
public interface TAnalysisDao
		extends PagingAndSortingRepository<TAnalysis, Long> {
	List<TAnalysis> findByNumber(String number);
}
