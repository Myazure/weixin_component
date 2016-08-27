package org.myazure.weixin.service.currentUser;

import org.myazure.weixin.domain.MaUser;
import org.myazure.weixin.domain.CurrentUser;
import org.myazure.weixin.service.AdUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @author csyangchsh@gmail.com
 */
@Service
public class CurrentUserDetailsService implements UserDetailsService {

    private final AdUserService userService;

    @Autowired
    public CurrentUserDetailsService(AdUserService userService) {
        this.userService = userService;
    }


    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        MaUser user = userService.getAdUserByName(name);
        if(user == null) {
            throw new UsernameNotFoundException(name + " not found");
        }
        return new CurrentUser(user);
    }
}
