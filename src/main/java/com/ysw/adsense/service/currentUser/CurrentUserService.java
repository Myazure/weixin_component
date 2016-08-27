package com.ysw.adsense.service.currentUser;

import com.ysw.adsense.domain.CurrentUser;

/**
 * @author csyangchsh@gmail.com
 */
public interface CurrentUserService {
    boolean canAccessUser(CurrentUser currentUser, Long userId);
}
