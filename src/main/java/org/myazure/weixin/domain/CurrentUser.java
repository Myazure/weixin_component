package org.myazure.weixin.domain;

import org.springframework.security.core.authority.AuthorityUtils;


/**
 * @author csyangchsh@gmail.com
 */
public class CurrentUser extends org.springframework.security.core.userdetails.User {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private MaUser user;

    public CurrentUser(MaUser user) {
        super(user.getUserName(), user.getPassword(), AuthorityUtils.createAuthorityList(user.getRole().toString()));
        this.user = user;
    }

    public MaUser getUser() {
        return user;
    }

    public Long getId() {
        return user.getId();
    }

    public MaRole getRole() {
        return user.getRole();
    }

    @Override
    public String toString() {
        return "CurrentUser{" +
                "user=" + user +
                "} " + super.toString();
    }
}
