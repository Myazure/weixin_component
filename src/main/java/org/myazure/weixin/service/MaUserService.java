package org.myazure.weixin.service;

import java.util.Collection;

import org.myazure.weixin.domain.MaUser;
import org.myazure.weixin.domain.entity.MaUserEntity;


public interface MaUserService {

    MaUser getMaUserById(long id);

    MaUser getMaUserByName(String name);

    Collection<MaUser> getAllMaUsers();

    MaUser create(MaUserEntity form);
}
