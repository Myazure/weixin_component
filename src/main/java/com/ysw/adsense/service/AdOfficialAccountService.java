package com.ysw.adsense.service;

import java.util.List;

import com.ysw.adsense.domain.AdOfficialAccount;
import com.ysw.adsense.domain.AdUser;


public interface AdOfficialAccountService {

    List<AdOfficialAccount> findByUser(String userName);
    
    AdOfficialAccount findByUserName(String userName);

    AdOfficialAccount findByAppId(String appId);

    void addAdOfficialAccount(AdUser user, String appId, String refreshToken, String userName, String nickName);

    void updateAdOfficialAccount(AdOfficialAccount oa);
}
