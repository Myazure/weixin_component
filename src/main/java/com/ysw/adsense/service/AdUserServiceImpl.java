package com.ysw.adsense.service;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.ysw.adsense.domain.AdUser;
import com.ysw.adsense.domain.entity.AdUserEntity;
import com.ysw.adsense.repository.AdUserRepository;


@Service
public class AdUserServiceImpl implements AdUserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdUserServiceImpl.class);

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    private final AdUserRepository userRepository;

    @Autowired
    public AdUserServiceImpl(AdUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Cacheable(value = "userCache",keyGenerator = "wiselyKeyGenerator")
    public AdUser getAdUserById(long id) {
    	LOGGER.info("[YSW Adsense]: getAdUserById()");
        return userRepository.findOne(id);
    }

    @Override
    public AdUser getAdUserByName(String name) {
        return userRepository.findOneByUserName(name);
    }

    @Override
    @Cacheable(value = "userCache",keyGenerator = "wiselyKeyGenerator")
    public Collection<AdUser> getAllAdUsers() {
    	LOGGER.info("[YSW Adsense]: getAllAdUsers()");
        return (Collection<AdUser>) userRepository.findAll(new Sort("userName"));
    }

    @Override
    public AdUser create(AdUserEntity form) {
        AdUser user = new AdUser();
    	if(null != form.getId() && form.getId() > 0) {
    		user = this.getAdUserById(form.getId());
    	}
        user.setUserName(form.getUserName());
        user.setPassword(passwordEncoder.encode(form.getPassword()));
        user.setRole(form.getRole());
        return userRepository.save(user);
    }
}
