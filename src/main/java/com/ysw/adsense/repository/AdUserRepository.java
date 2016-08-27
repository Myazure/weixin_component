package com.ysw.adsense.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.ysw.adsense.domain.AdUser;


public interface AdUserRepository extends PagingAndSortingRepository<AdUser, Long> {

    AdUser findOneByUserName(String userName);
}
