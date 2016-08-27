package com.ysw.adsense.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.ysw.adsense.domain.AdEvent;


public interface AdEventRepository extends PagingAndSortingRepository<AdEvent, Long> {

    @Query("SELECT e FROM AdEvent e LEFT JOIN e.officialAccount oa WHERE oa.userName = :userName")
	AdEvent findByOfficialAccountUserName(@Param("userName") String userName);
}
