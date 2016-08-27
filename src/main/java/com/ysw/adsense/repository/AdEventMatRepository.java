package com.ysw.adsense.repository;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.ysw.adsense.domain.AdEventMat;


public interface AdEventMatRepository extends PagingAndSortingRepository<AdEventMat, Long> {

	List<AdEventMat> findByEventId(long eventId);
}
