package com.ysw.adsense.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.ysw.adsense.domain.AdEvent;
import com.ysw.adsense.domain.AdEventHistory;
import com.ysw.adsense.domain.AdEventMat;
import com.ysw.adsense.domain.AdMat;
import com.ysw.adsense.domain.AdOfficialAccountWxUser;
import com.ysw.adsense.domain.AdWxUser;
import com.ysw.adsense.repository.AdEventHistoryRepository;
import com.ysw.adsense.repository.AdEventMatRepository;
import com.ysw.adsense.repository.AdEventRepository;
import com.ysw.adsense.repository.AdMatRepository;
import com.ysw.adsense.repository.AdOfficialAccountWxUserRepository;
import com.ysw.adsense.repository.AdWxUserRepository;


@Service("eventService")
public class AdEventServiceImpl implements AdEventService {

    @Autowired
    private AdEventRepository eventRepository;
    
    @Autowired
    private AdOfficialAccountWxUserRepository oaWxUserRepository;

    @Autowired
    private AdEventHistoryRepository eventHistoryRepository;
    
    @Autowired
    private AdEventMatRepository eventMatRepository;
    
    @Autowired
    private AdWxUserRepository wxUserRepository;
    
    @Autowired
    private AdMatRepository matRepository;

	@Override
    @Cacheable(value = "eventCache", keyGenerator = "wiselyKeyGenerator")
	public List<AdEvent> fetchAdEventList() {
		return (List<AdEvent>) eventRepository.findAll();
	}

	@Override
	public AdEvent fetchAdEventById(long eventId) {
		return eventRepository.findOne(eventId);
	}
	
	@Override
	public void createEventHistory(AdEventHistory history) {
		eventHistoryRepository.save(history);
	}

	@Override
	public AdEvent findByOfficialAccountUserName(String userName) {
		return eventRepository.findByOfficialAccountUserName(userName);
	}
    
	@Override
	public void createWxUser(AdWxUser wxUser) {
		wxUserRepository.save(wxUser);
	}

	@Override
	public AdWxUser findAdWxUserByUnionId(String unionId) {
		return wxUserRepository.findByUnionId(unionId);
	}

	@Override
	public AdOfficialAccountWxUser findOAWxUserByOAIdAndWxUserOpenId(long oaId,
			String openId) {
		return oaWxUserRepository.findByOAIdAndWxUserOpenId(oaId, openId);
	}
	
	@Override
	public void saveOAWxUser(AdOfficialAccountWxUser oaWzUser) {
		oaWxUserRepository.save(oaWzUser);
	}

	@Override
	public List<AdEventMat> findEventMatByEventId(long eventId) {
		return eventMatRepository.findByEventId(eventId);
	}

	@Override
	public AdMat findMatByMatId(long matId) {
		return matRepository.findOne(matId);
	}

	@Override
	public AdEventHistory findEventHistoryByEventIdAndWxUserId(long eventId,
			long wsUserId, String action) {
		return eventHistoryRepository.findEventHistoryByEventIdAndWxUserId(eventId, wsUserId, action);
	}

	@Override
	public AdEventHistory findEventHistoryById(long historyId) {
		return eventHistoryRepository.findOne(historyId);
	}

	@Override
	public List<AdEventHistory> findLocationEventHistorySinceWhen(Date when) {
		return eventHistoryRepository.findLocationEventHistorySinceWhen(when);
	}
}
