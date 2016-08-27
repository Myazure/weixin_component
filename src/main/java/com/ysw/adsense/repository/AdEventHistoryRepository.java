package com.ysw.adsense.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.ysw.adsense.domain.AdEventHistory;


public interface AdEventHistoryRepository extends PagingAndSortingRepository<AdEventHistory, Long> {

    @Query("SELECT eh FROM AdEventHistory eh LEFT JOIN eh.event e LEFT JOIN eh.wxUser wxu "
    		+ "WHERE e.id = :eventId and wxu.id = :wsUserId and eh.action = :action")
	AdEventHistory findEventHistoryByEventIdAndWxUserId(@Param("eventId") long eventId,
			@Param("wsUserId") long wsUserId, @Param("action") String action);
    
    @Query("SELECT eh FROM AdEventHistory eh "
    		+ "WHERE eh.createdAt > :when AND eh.action = 'LOCATION'")
	List<AdEventHistory> findLocationEventHistorySinceWhen(@Param("when") Date when);
}
