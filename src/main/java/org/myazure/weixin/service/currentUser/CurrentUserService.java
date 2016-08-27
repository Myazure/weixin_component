package org.myazure.weixin.service.currentUser;

import org.myazure.weixin.domain.CurrentUser;

/**
 * @author csyangchsh@gmail.com
 */
public interface CurrentUserService {
    boolean canAccessUser(CurrentUser currentUser, Long userId);
}
