package com.ysw.adsense.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ysw.adsense.domain.AdOfficialAccount;
import com.ysw.adsense.domain.AdUser;
import com.ysw.adsense.repository.AdOfficialAccountRepository;


@Service
@Transactional
public class AdOfficialAccountServiceImpl implements AdOfficialAccountService {

	@Autowired
    private AdOfficialAccountRepository repository;


    @Override
    @Transactional(readOnly = true)
    public List<AdOfficialAccount> findByUser(String userName) {
        List<AdOfficialAccount> oaList = repository.findByUser(userName);
        if(oaList != null && !oaList.isEmpty()) {
            return oaList;
        }
        return null;
    }
    
    @Override
    @Transactional(readOnly = true)
    public AdOfficialAccount findByUserName(String userName) {
        AdOfficialAccount oa = repository.findByUserName(userName);
        return oa;
    }

    @Override
    public AdOfficialAccount findByAppId(String appId) {
        List<AdOfficialAccount> oaList = repository.findByAppId(appId);
        if(oaList != null && !oaList.isEmpty()) {
            return oaList.get(0);
        }
        return null;
    }


    @Override
    public void addAdOfficialAccount(AdUser user, String appId, String refreshToken, String userName, String nickName) {
        AdOfficialAccount oa = findByAppId(appId);
        if(oa == null) {
        	oa = new AdOfficialAccount();
        }
        oa.setUser(user);
        oa.setAppId(appId);
        oa.setUserName(userName);
        oa.setNickName(nickName);
        oa.setRefreshToken(refreshToken);
        oa.setAuthorized(true);
        repository.save(oa);
    }

    @Override
    public void updateAdOfficialAccount(AdOfficialAccount oa) {
        repository.save(oa);
    }
}
