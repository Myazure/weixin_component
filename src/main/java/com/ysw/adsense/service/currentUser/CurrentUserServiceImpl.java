package com.ysw.adsense.service.currentUser;

import com.ysw.adsense.domain.CurrentUser;
import com.ysw.adsense.domain.AdRole;

/**
 * @author csyangchsh@gmail.com
 */
public class CurrentUserServiceImpl implements CurrentUserService {

    @Override
    public boolean canAccessUser(CurrentUser currentUser, Long userId) {
        return currentUser != null
                && (currentUser.getRole() == AdRole.ADMIN || currentUser.getId().equals(userId));
    }
}
