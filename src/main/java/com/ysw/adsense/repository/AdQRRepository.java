package com.ysw.adsense.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.ysw.adsense.domain.AdQR;


public interface AdQRRepository extends PagingAndSortingRepository<AdQR, Long> {
	
	AdQR findByQrTicket(String ticket);
}
