package org.myazure.weixin.service.currentUser;

import org.myazure.weixin.domain.CurrentUser;

/**
 * @author WangZhen
 */
public interface CurrentUserService {
    boolean canAccessUser(CurrentUser currentUser, Long userId);
}
