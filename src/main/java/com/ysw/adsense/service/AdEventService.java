package com.ysw.adsense.service;

import java.util.Date;
import java.util.List;

import com.ysw.adsense.domain.AdEvent;
import com.ysw.adsense.domain.AdEventHistory;
import com.ysw.adsense.domain.AdEventMat;
import com.ysw.adsense.domain.AdMat;
import com.ysw.adsense.domain.AdOfficialAccountWxUser;
import com.ysw.adsense.domain.AdWxUser;


public interface AdEventService {

	List<AdEvent> fetchAdEventList();
	
	AdEvent fetchAdEventById(long eventId);
    
	void createEventHistory(AdEventHistory history);
	
	AdEvent findByOfficialAccountUserName(String userName);
    
	AdWxUser findAdWxUserByUnionId(String unionId);

    void createWxUser(AdWxUser wxUser);
    
    AdOfficialAccountWxUser findOAWxUserByOAIdAndWxUserOpenId(long oaId, String openId);
    
    void saveOAWxUser(AdOfficialAccountWxUser oaWzUser);
    
    List<AdEventMat> findEventMatByEventId(long eventId);
    
    AdMat findMatByMatId(long matId);
    
    AdEventHistory findEventHistoryById(long historyId);
    
    AdEventHistory findEventHistoryByEventIdAndWxUserId(long eventId, long wsUserId, String action);
    
    List<AdEventHistory> findLocationEventHistorySinceWhen(Date when);
}
