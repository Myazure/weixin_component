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


@Service
public class AdUserServiceImpl implements AdUserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdUserServiceImpl.class);

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    private final MaUserRepository userRepository;

    @Autowired
    public AdUserServiceImpl(MaUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Cacheable(value = "userCache",keyGenerator = "wiselyKeyGenerator")
    public MaUser getAdUserById(long id) {
    	LOGGER.info("[Myazure Weixin]: getAdUserById()");
        return userRepository.findOne(id);
    }

    @Override
    public MaUser getAdUserByName(String name) {
        return userRepository.findOneByUserName(name);
    }

    @Override
    @Cacheable(value = "userCache",keyGenerator = "wiselyKeyGenerator")
    public Collection<MaUser> getAllAdUsers() {
    	LOGGER.info("[Myazure Weixin]: getAllAdUsers()");
        return (Collection<MaUser>) userRepository.findAll(new Sort("userName"));
    }

    @Override
    public MaUser create(MaUserEntity form) {
        MaUser user = new MaUser();
    	if(null != form.getId() && form.getId() > 0) {
    		user = this.getAdUserById(form.getId());
    	}
        user.setUserName(form.getUserName());
        user.setPassword(passwordEncoder.encode(form.getPassword()));
        user.setRole(form.getRole());
        return userRepository.save(user);
    }
}
