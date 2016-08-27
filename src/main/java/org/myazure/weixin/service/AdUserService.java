package org.myazure.weixin.service;

import java.util.Collection;

import org.myazure.weixin.domain.MaUser;
import org.myazure.weixin.domain.entity.MaUserEntity;


public interface AdUserService {

    MaUser getAdUserById(long id);

    MaUser getAdUserByName(String name);

    Collection<MaUser> getAllAdUsers();

    MaUser create(MaUserEntity form);
}
