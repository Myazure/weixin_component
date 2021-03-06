package org.myazure.weixin.service;

import java.util.Collection;

import org.myazure.weixin.domain.MaUser;
import org.myazure.weixin.domain.entity.MaUserEntity;
import org.myazure.weixin.repository.MaUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * 
 * @author WangZhen
 *
 */
@Service
public class MaUserServiceImpl implements MaUserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MaUserServiceImpl.class);

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    private final MaUserRepository userRepository;

    @Autowired
    public MaUserServiceImpl(MaUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Cacheable(value = "userCache",keyGenerator = "wiselyKeyGenerator")
    public MaUser getMaUserById(long id) {
    	LOGGER.info("[Myazure Weixin]: getMaUserById()");
        return userRepository.findOne(id);
    }

    @Override
    public MaUser getMaUserByName(String name) {
        return userRepository.findOneByUserName(name);
    }

    @Override
    @Cacheable(value = "userCache",keyGenerator = "wiselyKeyGenerator")
    public Collection<MaUser> getAllMaUsers() {
    	LOGGER.info("[Myazure Weixin]: getAllAdUsers()");
        return (Collection<MaUser>) userRepository.findAll(new Sort("userName"));
    }

    @Override
    public MaUser create(MaUserEntity form) {
        MaUser user = new MaUser();
    	if(null != form.getId() && form.getId() > 0) {
    		user = this.getMaUserById(form.getId());
    	}
        user.setUserName(form.getUserName());
        user.setPassword(passwordEncoder.encode(form.getPassword()));
        user.setRole(form.getRole());
        return userRepository.save(user);
    }
}
