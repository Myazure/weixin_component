package org.myazure.weixin.service;

import java.util.List;

import org.myazure.weixin.domain.MaOfficialAccount;
import org.myazure.weixin.domain.MaUser;


public interface MaOfficialAccountService {
	 	List<MaOfficialAccount> findByUser(String userName);
	    
	    MaOfficialAccount findByUserName(String userName);

	    MaOfficialAccount findByAppId(String appId);

	    void addAdOfficialAccount(MaUser user, String appId, String refreshToken, String userName, String nickName);

	    void updateAdOfficialAccount(MaOfficialAccount oa);
}
