package com.ysw.adsense.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.ysw.adsense.domain.AdWxUser;


public interface AdWxUserRepository extends PagingAndSortingRepository<AdWxUser, Long> {
	
	AdWxUser findByUnionId(String unionId);
}
