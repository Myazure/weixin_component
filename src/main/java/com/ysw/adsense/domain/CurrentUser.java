package com.ysw.adsense.domain;

import org.springframework.security.core.authority.AuthorityUtils;


/**
 * @author csyangchsh@gmail.com
 */
public class CurrentUser extends org.springframework.security.core.userdetails.User {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private AdUser user;

    public CurrentUser(AdUser user) {
        super(user.getUserName(), user.getPassword(), AuthorityUtils.createAuthorityList(user.getRole().toString()));
        this.user = user;
    }

    public AdUser getUser() {
        return user;
    }

    public Long getId() {
        return user.getId();
    }

    public AdRole getRole() {
        return user.getRole();
    }

    @Override
    public String toString() {
        return "CurrentUser{" +
                "user=" + user +
                "} " + super.toString();
    }
}
