package com.ysw.adsense.service;

import java.util.Collection;

import com.ysw.adsense.domain.AdUser;
import com.ysw.adsense.domain.entity.AdUserEntity;


public interface AdUserService {

    AdUser getAdUserById(long id);

    AdUser getAdUserByName(String name);

    Collection<AdUser> getAllAdUsers();

    AdUser create(AdUserEntity form);
}
