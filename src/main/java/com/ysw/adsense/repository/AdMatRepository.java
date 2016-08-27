package com.ysw.adsense.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.ysw.adsense.domain.AdMat;


public interface AdMatRepository extends PagingAndSortingRepository<AdMat, Long> {
	
}
