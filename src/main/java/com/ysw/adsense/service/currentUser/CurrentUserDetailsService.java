package com.ysw.adsense.service.currentUser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ysw.adsense.domain.AdUser;
import com.ysw.adsense.domain.CurrentUser;
import com.ysw.adsense.service.AdUserService;

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
        AdUser user = userService.getAdUserByName(name);
        if(user == null) {
            throw new UsernameNotFoundException(name + " not found");
        }
        return new CurrentUser(user);
    }
}
