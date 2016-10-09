package org.myazure.weixin.service;

import java.util.List;

import org.myazure.weixin.domain.MaOfficialAccount;
import org.myazure.weixin.domain.MaUser;
import org.myazure.weixin.repository.MaOfficialAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * 
 * @author WangZhen
 *
 */
@Service
@Transactional
public class MaOfficialAccountServiceImpl implements MaOfficialAccountService{
	@Autowired
    private MaOfficialAccountRepository repository;

	@Override
	public List<MaOfficialAccount> findByUser(String userName) {
		 List<MaOfficialAccount> oaList = repository.findByUser(userName);
	        if(oaList != null && !oaList.isEmpty()) {
	            return oaList;
	        }
	        return null;
	}

	@Override
	public MaOfficialAccount findByUserName(String userName) {
		MaOfficialAccount oa = repository.findByUserName(userName);
        return oa;
	}

	@Override
	public MaOfficialAccount findByAppId(String appId) {
		try {
			List<MaOfficialAccount> oaList = repository.findByAppId(appId);
			if(oaList != null && !oaList.isEmpty()) {
				return oaList.get(0);
			}
		} catch (Exception e) {
			 return null;
		}
        return null;
	}

	@Override
	public void addAdOfficialAccount(MaUser user, String appId, String refreshToken, String userName, String nickName) {
		MaOfficialAccount oa = findByAppId(appId);
        if(oa == null) {
        	oa = new MaOfficialAccount();
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
	public void updateAdOfficialAccount(MaOfficialAccount oa) {
		repository.save(oa);
	}

}
